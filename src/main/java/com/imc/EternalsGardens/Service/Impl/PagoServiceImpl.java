package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.PagoRequest;
import com.imc.EternalsGardens.DTO.Response.PagoResponse;
import com.imc.EternalsGardens.Entity.Concesion;
import com.imc.EternalsGardens.Entity.Pago;
import com.imc.EternalsGardens.Entity.Usuario;
import com.imc.EternalsGardens.Enum.EstadoPagoEnum;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Exception.ReglaNegocioException;
import com.imc.EternalsGardens.Mapper.PagoMapper;
import com.imc.EternalsGardens.Repository.ConcesionRepository;
import com.imc.EternalsGardens.Repository.PagoRepository;
import com.imc.EternalsGardens.Repository.UsuarioRepository;
import com.imc.EternalsGardens.Service.Interfaces.IPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements IPagoService {

    private final PagoRepository pagoRepository;
    private final ConcesionRepository concesionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagoMapper pagoMapper;

    @Override
    @Transactional
    public PagoResponse registrarPago(PagoRequest request) {
        // 1. Validar duplicidad de referencia (si aplica)
        if (request.getReferenciaTransaccion() != null &&
                pagoRepository.existsByReferenciaTransaccion(request.getReferenciaTransaccion())) {
            throw new ReglaNegocioException("Ya existe un pago con la referencia: " + request.getReferenciaTransaccion());
        }

        // 2. Buscar entidades relacionadas
        Concesion concesion = concesionRepository.findById(request.getConcesionId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Concesión no encontrada con ID: " + request.getConcesionId()));

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + request.getUsuarioId()));

        // 3. Crear Entidad
        Pago pago = pagoMapper.toEntity(request);
        pago.setConcesion(concesion);
        pago.setUsuario(usuario);

        // 4. Asignar valores por defecto
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }
        if (pago.getEstado() == null) {
            pago.setEstado("COMPLETADO"); // O EstadoPagoEnum.COMPLETADO.name()
        }

        return pagoMapper.toResponse(pagoRepository.save(pago));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> obtenerTodos() {
        return pagoMapper.toResponseList(pagoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse obtenerPorId(Integer id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado con ID: " + id));
        return pagoMapper.toResponse(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> obtenerPorConcesion(Integer concesionId) {
        if (!concesionRepository.existsById(concesionId)) {
            throw new RecursoNoEncontradoException("Concesión no encontrada con ID: " + concesionId);
        }
        return pagoMapper.toResponseList(pagoRepository.findByConcesionId(concesionId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> obtenerPorUsuario(Integer usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }
        return pagoMapper.toResponseList(pagoRepository.findByUsuarioId(usuarioId));
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse obtenerPorReferencia(String referencia) {
        Pago pago = pagoRepository.buscarPorReferencia(referencia)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado con referencia: " + referencia));
        return pagoMapper.toResponse(pago);
    }

    @Override
    @Transactional
    public void eliminarPago(Integer id) {
        if (!pagoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Pago no encontrado con ID: " + id);
        }
        pagoRepository.deleteById(id);
    }
}