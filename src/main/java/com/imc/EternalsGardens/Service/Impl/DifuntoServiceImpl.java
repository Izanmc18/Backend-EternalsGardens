package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.DifuntoRequest;
import com.imc.EternalsGardens.DTO.Response.DifuntoResponse;
import com.imc.EternalsGardens.Entity.Difunto;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Mapper.DifuntoMapper;
import com.imc.EternalsGardens.Repository.DifuntoRepository;
import com.imc.EternalsGardens.Service.Interfaces.IDifuntoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DifuntoServiceImpl implements IDifuntoService {

    private final DifuntoRepository difuntoRepository;
    private final com.imc.EternalsGardens.Repository.ParcelaRepository parcelaRepository;
    private final com.imc.EternalsGardens.Repository.DifuntosEnParcelaRepository difuntosEnParcelaRepository;
    private final DifuntoMapper difuntoMapper;
    private final com.imc.EternalsGardens.Service.Interfaces.IStorageService storageService;

    @Override
    @Transactional
    public DifuntoResponse crearDifunto(DifuntoRequest request, org.springframework.web.multipart.MultipartFile file) {
        System.out.println("DEBUG: Inicio crearDifunto");
        try {
            // 0. Handle File Upload
            if (file != null && !file.isEmpty()) {
                storageService.init(); // Ensure dir exists
                String url = storageService.store(file, "difuntos");
                request.setFotoUrl(url);
            }

            String normalizedDni = request.getDni().trim().toUpperCase();
            System.out.println("DEBUG: DNI normalizado: " + normalizedDni);

            Optional<Difunto> existing = difuntoRepository.findByDni(normalizedDni);
            if (existing.isPresent()) {
                System.out.println("DEBUG: DNI duplicado encontrado: " + existing.get().getId());
                throw new com.imc.EternalsGardens.Exception.ReglaNegocioException(
                        "Ya existe un difunto registrado con el DNI " + normalizedDni);
            }

            // Update request
            request.setDni(normalizedDni);

            System.out.println("DEBUG: Mapeando a entidad...");
            Difunto difunto = difuntoMapper.toEntity(request);
            System.out.println("DEBUG: Mapeo correcto. Entidad: " + difunto);

            if (request.getParcelaId() != null) {
                System.out.println("DEBUG: Buscando parcela ID: " + request.getParcelaId());
                com.imc.EternalsGardens.Entity.Parcela parcela = parcelaRepository.findById(request.getParcelaId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Parcela no encontrada ID: " + request.getParcelaId()));

                System.out.println("DEBUG: Parcela encontrada. Estado: " + parcela.getEstado());
                if (!"LIBRE".equalsIgnoreCase(parcela.getEstado())
                        && !"RESERVADA".equalsIgnoreCase(parcela.getEstado())) {
                    throw new com.imc.EternalsGardens.Exception.ReglaNegocioException(
                            "La parcela seleccionada no está disponible (Estado: " + parcela.getEstado() + ")");
                }

                parcela.setEstado("OCUPADA");
                parcelaRepository.save(parcela);
                difunto.setParcela(parcela);

                System.out.println("DEBUG: Parcela asignada y actualizada.");
            }

            System.out.println("DEBUG: Guardando difunto en BD...");
            Difunto guardado = difuntoRepository.save(difunto);
            System.out.println("DEBUG: Difunto guardado ID: " + guardado.getId());

            // Si hay parcela asignada, crear registro en DifuntosEnParcela
            if (guardado.getParcela() != null) {
                com.imc.EternalsGardens.Entity.DifuntosEnParcela dep = new com.imc.EternalsGardens.Entity.DifuntosEnParcela();
                dep.setDifunto(guardado);
                dep.setParcela(guardado.getParcela());

                // Safe handling of Concesion
                if (guardado.getParcela().getConcesion() != null) {
                    dep.setConcesion(guardado.getParcela().getConcesion());
                }

                dep.setFechaEnterramiento(java.time.LocalDate.now());
                dep.setEstado("ENTERRADO");

                difuntosEnParcelaRepository.save(dep); // Throws RuntimeException if fails
                System.out.println("DEBUG: Registro Histórico DifuntosEnParcela creado.");
            }

            return difuntoMapper.toResponse(guardado);
        } catch (Exception e) {
            System.out.println("ERROR CRITICO EN CREARDIFUNTO:");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DifuntoResponse> obtenerTodos() {
        return difuntoMapper.toResponseList(difuntoRepository.findAll());
    }

    @Override
    public List<DifuntoResponse> buscarDifuntos(String query) {
        if (query == null || query.trim().isEmpty()) {
            return obtenerTodos();
        }
        return difuntoMapper.toResponseList(difuntoRepository.buscarGlobal(query.trim()));
    }

    @Override
    @Transactional(readOnly = true)
    public DifuntoResponse obtenerPorId(Integer id) {
        Difunto difunto = difuntoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Difunto no encontrado ID: " + id));
        return difuntoMapper.toResponse(difunto);
    }

    @Override
    @Transactional
    public DifuntoResponse actualizarDifunto(Integer id, DifuntoRequest request,
            org.springframework.web.multipart.MultipartFile file) {
        Difunto difunto = difuntoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Difunto no encontrado ID: " + id));

        // Handle File Upload
        if (file != null && !file.isEmpty()) {
            storageService.init();
            String url = storageService.store(file, "difuntos");
            request.setFotoUrl(url);
        }

        difuntoMapper.updateEntity(request, difunto);
        return difuntoMapper.toResponse(difuntoRepository.save(difunto));
    }

    @Override
    @Transactional
    public void eliminarDifunto(Integer id) {
        // Validar si tiene enterramientos asociados antes de borrar
        if (!difuntoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Difunto no encontrado ID: " + id);
        }
        difuntoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<DifuntoResponse> obtenerPorZona(
            Integer zonaId,
            org.springframework.data.domain.Pageable pageable) {
        return difuntoRepository.findByZonaId(zonaId, pageable)
                .map(difuntoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<DifuntoResponse> obtenerPorCementerio(
            Integer cementerioId,
            org.springframework.data.domain.Pageable pageable) {
        return difuntoRepository.findByCementerioId(cementerioId, pageable)
                .map(difuntoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DifuntoResponse> obtenerPorUsuario(Integer usuarioId) {
        return difuntoMapper.toResponseList(difuntoRepository.findByParcela_Concesion_Usuario_Id(usuarioId));
    }
}