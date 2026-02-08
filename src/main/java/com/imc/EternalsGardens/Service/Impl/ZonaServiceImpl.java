package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.ZonaRequest;
import com.imc.EternalsGardens.DTO.Response.ZonaResponse;
import com.imc.EternalsGardens.Entity.Cementerio;
import com.imc.EternalsGardens.Entity.TipoZona;
import com.imc.EternalsGardens.Entity.Zona;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Mapper.ZonaMapper;
import com.imc.EternalsGardens.Repository.CementerioRepository;
import com.imc.EternalsGardens.Repository.TipoZonaRepository;
import com.imc.EternalsGardens.Repository.ZonaRepository;
import com.imc.EternalsGardens.Service.Interfaces.IZonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZonaServiceImpl implements IZonaService {

    private final ZonaRepository zonaRepository;
    private final CementerioRepository cementerioRepository;
    private final TipoZonaRepository tipoZonaRepository;
    private final ZonaMapper zonaMapper;

    @Override
    @Transactional
    public ZonaResponse crearZona(ZonaRequest request) {
        Cementerio cementerio = cementerioRepository.findById(request.getCementerioId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cementerio no encontrado ID: " + request.getCementerioId()));

        TipoZona tipoZona = tipoZonaRepository.findById(request.getTipoZonaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "TipoZona no encontrado ID: " + request.getTipoZonaId()));

        Zona zona = zonaMapper.toEntity(request);
        zona.setCementerio(cementerio);
        zona.setTipoZona(tipoZona);
        zona.setTipoZona(tipoZona);
        zona.setActiva(true);
        // Asegurar que es una NUEVA entidad
        zona.setId(null);

        // Prevenir NullPointerException o DataIntegrityViolation si vienen nulos
        if (zona.getFilas() == null)
            zona.setFilas(0);
        if (zona.getColumnas() == null)
            zona.setColumnas(0);
        if (zona.getCapacidadTotal() == null) {
            zona.setCapacidadTotal(zona.getFilas() * zona.getColumnas());
        }

        return zonaMapper.toResponse(zonaRepository.save(zona));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ZonaResponse> obtenerTodas() {
        return zonaMapper.toResponseList(zonaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ZonaResponse> obtenerPorCementerio(Integer cementerioId) {
        return zonaMapper.toResponseList(zonaRepository.findByCementerioIdAndActivaTrue(cementerioId));
    }

    @Override
    @Transactional(readOnly = true)
    public ZonaResponse obtenerPorId(Integer id) {
        Zona zona = zonaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Zona no encontrada ID: " + id));
        return zonaMapper.toResponse(zona);
    }

    @Override
    @Transactional
    public ZonaResponse actualizarZona(Integer id, ZonaRequest request) {
        Zona zona = zonaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Zona no encontrada ID: " + id));

        zonaMapper.updateEntity(request, zona);

        if (request.getTipoZonaId() != null) {
            TipoZona tipo = tipoZonaRepository.findById(request.getTipoZonaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("TipoZona no encontrado"));
            zona.setTipoZona(tipo);
        }

        if (request.getFilas() != null && request.getColumnas() != null) {
            zona.setCapacidadTotal(request.getFilas() * request.getColumnas());
        }

        // Protección contra nulos tras mapeo
        if (zona.getFilas() == null)
            zona.setFilas(0);
        if (zona.getColumnas() == null)
            zona.setColumnas(0);
        if (zona.getCapacidadTotal() == null)
            zona.setCapacidadTotal(0);

        return zonaMapper.toResponse(zonaRepository.save(zona));
    }

    @Override
    @Transactional
    public void eliminarZona(Integer id) {
        Zona zona = zonaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Zona no encontrada ID: " + id));
        zona.setActiva(false);
        zonaRepository.save(zona);
    }
}