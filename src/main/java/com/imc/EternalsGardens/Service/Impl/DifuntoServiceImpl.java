package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.DifuntoRequest;
import com.imc.EternalsGardens.DTO.Response.DifuntoResponse;
import com.imc.EternalsGardens.Entity.Difunto;
import com.imc.EternalsGardens.Entity.DifuntosEnParcela;
import com.imc.EternalsGardens.Entity.Parcela;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Exception.ReglaNegocioException;
import com.imc.EternalsGardens.Mapper.DifuntoMapper;
import com.imc.EternalsGardens.Repository.DifuntoRepository;
import com.imc.EternalsGardens.Repository.DifuntosEnParcelaRepository;
import com.imc.EternalsGardens.Repository.ParcelaRepository;
import com.imc.EternalsGardens.Service.Interfaces.IDifuntoService;
import com.imc.EternalsGardens.Service.Interfaces.IStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DifuntoServiceImpl implements IDifuntoService {

    private final DifuntoRepository difuntoRepository;
    private final ParcelaRepository parcelaRepository;
    private final DifuntosEnParcelaRepository difuntosEnParcelaRepository;
    private final DifuntoMapper difuntoMapper;
    private final IStorageService storageService;

    @Override
    @Transactional
    public DifuntoResponse crearDifunto(DifuntoRequest request, MultipartFile file) {
        System.out.println("DEBUG: Inicio crearDifunto");
        try {

            if (file != null && !file.isEmpty()) {
                storageService.init();
                String url = storageService.store(file, "difuntos");
                request.setFotoUrl(url);
            }

            String normalizedDni = request.getDni().trim().toUpperCase();
            System.out.println("DEBUG: DNI normalizado: " + normalizedDni);

            Optional<Difunto> existing = difuntoRepository.findByDni(normalizedDni);
            if (existing.isPresent()) {
                System.out.println("DEBUG: DNI duplicado encontrado: " + existing.get().getId());
                throw new ReglaNegocioException(
                        "Ya existe un difunto registrado con el DNI " + normalizedDni);
            }

            request.setDni(normalizedDni);

            System.out.println("DEBUG: Mapeando a entidad...");
            Difunto difunto = difuntoMapper.toEntity(request);
            System.out.println("DEBUG: Mapeo correcto. Entidad: " + difunto);

            if (request.getParcelaId() != null) {
                System.out.println("DEBUG: Buscando parcela ID: " + request.getParcelaId());
                Parcela parcela = parcelaRepository.findById(request.getParcelaId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Parcela no encontrada ID: " + request.getParcelaId()));

                System.out.println("DEBUG: Parcela encontrada. Estado: " + parcela.getEstado());
                if (!"LIBRE".equalsIgnoreCase(parcela.getEstado())
                        && !"RESERVADA".equalsIgnoreCase(parcela.getEstado())) {
                    throw new ReglaNegocioException(
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
                DifuntosEnParcela dep = new DifuntosEnParcela();
                dep.setDifunto(guardado);
                dep.setParcela(guardado.getParcela());

                if (guardado.getParcela().getConcesion() != null) {
                    dep.setConcesion(guardado.getParcela().getConcesion());
                }

                dep.setFechaEnterramiento(LocalDate.now());
                dep.setEstado("ENTERRADO");

                difuntosEnParcelaRepository.save(dep);
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
    public DifuntoResponse actualizarDifunto(Integer id, DifuntoRequest request, MultipartFile file) {
        Difunto difunto = difuntoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Difunto no encontrado ID: " + id));

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
    public Page<DifuntoResponse> obtenerPorZona(Integer zonaId, Pageable pageable) {
        return difuntoRepository.findByZonaId(zonaId, pageable)
                .map(difuntoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DifuntoResponse> obtenerPorCementerio(Integer cementerioId, Pageable pageable) {
        return difuntoRepository.findByCementerioId(cementerioId, pageable)
                .map(difuntoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DifuntoResponse> obtenerPorUsuario(Integer usuarioId) {
        return difuntoMapper.toResponseList(difuntoRepository.findByParcela_Concesion_Usuario_Id(usuarioId));
    }
}