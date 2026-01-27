package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.ConcesionRequest;
import com.imc.EternalsGardens.DTO.Response.ConcesionResponse;
import com.imc.EternalsGardens.Entity.*;
import com.imc.EternalsGardens.Enum.EstadoConcesionEnum;
import com.imc.EternalsGardens.Enum.EstadoParcelaEnum;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Exception.ReglaNegocioException;
import com.imc.EternalsGardens.Mapper.ConcesionMapper;
import com.imc.EternalsGardens.Repository.*;
import com.imc.EternalsGardens.Service.Interfaces.IConcesionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcesionServiceImpl implements IConcesionService {

    private final ConcesionRepository concesionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CementerioRepository cementerioRepository;
    private final ParcelaRepository parcelaRepository;
    private final ConcesionMapper concesionMapper;

    @Override
    @Transactional
    public ConcesionResponse crearConcesion(ConcesionRequest request) {
        Usuario titular = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario titular no encontrado"));

        Cementerio cementerio = cementerioRepository.findById(request.getCementerioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cementerio no encontrado"));

        // Validar parcelas
        List<Parcela> parcelas = parcelaRepository.findAllById(request.getParcelasIds());
        if (parcelas.isEmpty()) {
            throw new ReglaNegocioException("Debe seleccionar al menos una parcela válida.");
        }

        for (Parcela p : parcelas) {
            // Validar que estén libres
            if (!EstadoParcelaEnum.LIBRE.name().equals(p.getEstado())) {
                throw new ReglaNegocioException("La parcela " + p.getNumeroIdentificadorUnico() + " no está disponible.");
            }
            // Validar que pertenezcan al cementerio correcto
            if (!p.getZona().getCementerio().getId().equals(cementerio.getId())) {
                throw new ReglaNegocioException("La parcela " + p.getNumeroIdentificadorUnico() + " no pertenece al cementerio seleccionado.");
            }
        }

        Concesion concesion = concesionMapper.toEntity(request);
        concesion.setUsuario(titular);
        concesion.setCementerio(cementerio);
        concesion.setEstado(EstadoConcesionEnum.ACTIVA.name());

        Concesion concesionGuardada = concesionRepository.save(concesion);

        // Actualizar parcelas
        for (Parcela p : parcelas) {
            p.setConcesion(concesionGuardada);
            p.setEstado(EstadoParcelaEnum.OCUPADA.name());
            parcelaRepository.save(p);
        }

        concesionGuardada.setParcelas(parcelas);
        return concesionMapper.toResponse(concesionGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcesionResponse> obtenerTodas() {
        return concesionMapper.toResponseList(concesionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcesionResponse> obtenerPorTitular(Integer usuarioId) {
        return concesionMapper.toResponseList(concesionRepository.findByUsuarioId(usuarioId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcesionResponse> obtenerPorCementerio(Integer cementerioId) {
        return concesionMapper.toResponseList(concesionRepository.findByCementerioId(cementerioId));
    }

    @Override
    @Transactional(readOnly = true)
    public ConcesionResponse obtenerPorId(Integer id) {
        Concesion concesion = concesionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Concesión no encontrada ID: " + id));
        return concesionMapper.toResponse(concesion);
    }

    @Override
    @Transactional
    public void cambiarEstadoConcesion(Integer id, String nuevoEstado) {
        Concesion concesion = concesionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Concesión no encontrada ID: " + id));
        concesion.setEstado(nuevoEstado);
        concesionRepository.save(concesion);
    }
}