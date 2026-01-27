package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.ServicioParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.ServicioParcelaResponse;
import com.imc.EternalsGardens.Entity.Parcela;
import com.imc.EternalsGardens.Entity.Servicio;
import com.imc.EternalsGardens.Entity.ServicioParcela;
import com.imc.EternalsGardens.Enum.EstadoServicioEnum; // Asegúrate de tener este Enum o usa String
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Mapper.ServicioParcelaMapper;
import com.imc.EternalsGardens.Repository.ParcelaRepository;
import com.imc.EternalsGardens.Repository.ServicioParcelaRepository;
import com.imc.EternalsGardens.Repository.ServicioRepository;
import com.imc.EternalsGardens.Service.Interfaces.IServicioParcelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioParcelaServiceImpl implements IServicioParcelaService {

    private final ServicioParcelaRepository repository;
    private final ParcelaRepository parcelaRepository;
    private final ServicioRepository servicioRepository;
    private final ServicioParcelaMapper mapper;

    @Override
    @Transactional
    public ServicioParcelaResponse contratarServicio(ServicioParcelaRequest request) {
        // 1. Validar existencias
        Parcela parcela = parcelaRepository.findById(request.getParcelaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Parcela no encontrada con ID: " + request.getParcelaId()));

        Servicio servicio = servicioRepository.findById(request.getServicioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado con ID: " + request.getServicioId()));

        // 2. Crear entidad
        ServicioParcela servicioParcela = mapper.toEntity(request);

        servicioParcela.setParcela(parcela);
        servicioParcela.setServicio(servicio);

        // 3. Lógica de negocio: Fijar precio y fecha
        // Importante: El precio contratado debe ser el precio actual del servicio en este momento
        servicioParcela.setPrecioContratado(servicio.getPrecioActual());

        if (servicioParcela.getFechaContratacion() == null) {
            servicioParcela.setFechaContratacion(LocalDateTime.now());
        }

        // Estado inicial
        if (servicioParcela.getEstado() == null) {
            servicioParcela.setEstado("PENDIENTE"); // O EstadoServicioEnum.PENDIENTE.name()
        }

        return mapper.toResponse(repository.save(servicioParcela));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioParcelaResponse> obtenerTodos() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioParcelaResponse obtenerPorId(Integer id) {
        ServicioParcela entidad = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Contratación de servicio no encontrada ID: " + id));
        return mapper.toResponse(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioParcelaResponse> obtenerPorParcela(Integer parcelaId) {
        if (!parcelaRepository.existsById(parcelaId)) {
            throw new RecursoNoEncontradoException("Parcela no encontrada con ID: " + parcelaId);
        }
        return mapper.toResponseList(repository.findByParcelaId(parcelaId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioParcelaResponse> obtenerPorServicio(Integer servicioId) {
        if (!servicioRepository.existsById(servicioId)) {
            throw new RecursoNoEncontradoException("Servicio no encontrado con ID: " + servicioId);
        }
        return mapper.toResponseList(repository.findByServicioId(servicioId));
    }

    @Override
    @Transactional
    public ServicioParcelaResponse cambiarEstado(Integer id, String nuevoEstado) {
        ServicioParcela entidad = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Contratación no encontrada ID: " + id));

        entidad.setEstado(nuevoEstado);

        // Si se completa, podríamos poner la fecha de ejecución automáticamente
        if ("COMPLETADO".equalsIgnoreCase(nuevoEstado) || "REALIZADO".equalsIgnoreCase(nuevoEstado)) {
            if (entidad.getFechaEjecucion() == null) {
                entidad.setFechaEjecucion(LocalDateTime.now());
            }
        }

        return mapper.toResponse(repository.save(entidad));
    }

    @Override
    @Transactional
    public void eliminarServicioParcela(Integer id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Contratación no encontrada ID: " + id);
        }
        repository.deleteById(id);
    }
}