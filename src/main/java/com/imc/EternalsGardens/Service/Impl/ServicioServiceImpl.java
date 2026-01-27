package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.ServicioRequest;
import com.imc.EternalsGardens.DTO.Response.ServicioResponse;
import com.imc.EternalsGardens.Entity.Servicio;
import com.imc.EternalsGardens.Entity.TipoZona;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Exception.ReglaNegocioException;
import com.imc.EternalsGardens.Mapper.ServicioMapper;
import com.imc.EternalsGardens.Repository.ServicioRepository;
import com.imc.EternalsGardens.Repository.TipoZonaRepository;
import com.imc.EternalsGardens.Service.Interfaces.IServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements IServicioService {

    private final ServicioRepository servicioRepository;
    private final TipoZonaRepository tipoZonaRepository;
    private final ServicioMapper servicioMapper;

    @Override
    @Transactional
    public ServicioResponse crearServicio(ServicioRequest request) {
        // Validar nombre duplicado
        Optional<Servicio> existente = servicioRepository.findByNombre(request.getNombre());
        if (existente.isPresent()) {
            throw new ReglaNegocioException("Ya existe un servicio con el nombre: " + request.getNombre());
        }

        Servicio servicio = servicioMapper.toEntity(request);

        // Asignar TipoZona si viene en el request
        if (request.getTipoZonaId() != null) {
            TipoZona tipoZona = tipoZonaRepository.findById(request.getTipoZonaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de Zona no encontrado con ID: " + request.getTipoZonaId()));
            servicio.setTipoZona(tipoZona);
        }

        // Por defecto activo si no se especifica
        if (servicio.getActivo() == null) {
            servicio.setActivo(true);
        }

        return servicioMapper.toResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> obtenerTodos() {
        return servicioMapper.toResponseList(servicioRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponse obtenerPorId(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado con ID: " + id));
        return servicioMapper.toResponse(servicio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> obtenerPorTipoZona(Integer tipoZonaId) {
        // Validamos que exista el tipo de zona
        if (!tipoZonaRepository.existsById(tipoZonaId)) {
            throw new RecursoNoEncontradoException("Tipo de Zona no encontrado con ID: " + tipoZonaId);
        }
        // Usamos el método corregido del repositorio
        return servicioMapper.toResponseList(servicioRepository.findByTipoZonaIdAndActivoTrue(tipoZonaId));
    }

    @Override
    @Transactional
    public ServicioResponse actualizarServicio(Integer id, ServicioRequest request) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado con ID: " + id));

        // Actualizar campos básicos
        servicioMapper.updateEntity(request, servicio);

        // Actualizar relación con TipoZona
        if (request.getTipoZonaId() != null) {
            TipoZona tipoZona = tipoZonaRepository.findById(request.getTipoZonaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de Zona no encontrado con ID: " + request.getTipoZonaId()));
            servicio.setTipoZona(tipoZona);
        } else {
            // Si viene null explícitamente, desvincular (opcional, según tu regla de negocio)
            // servicio.setTipoZona(null);
        }

        return servicioMapper.toResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public void eliminarServicio(Integer id) {
        if (!servicioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Servicio no encontrado con ID: " + id);
        }
        // Aquí podrías validar si el servicio ya ha sido contratado en 'ServicioParcela' antes de borrar
        servicioRepository.deleteById(id);
    }
}