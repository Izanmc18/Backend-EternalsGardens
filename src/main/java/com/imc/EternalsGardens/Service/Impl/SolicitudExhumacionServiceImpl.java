package com.imc.EternalsGardens.Service.Impl;

import java.util.UUID;
import com.imc.EternalsGardens.DTO.Request.SolicitudExhumacionRequest;
import com.imc.EternalsGardens.DTO.Response.SolicitudExhumacionResponse;
import com.imc.EternalsGardens.Entity.Difunto;
import com.imc.EternalsGardens.Entity.Parcela;
import com.imc.EternalsGardens.Entity.SolicitudExhumacion;
import com.imc.EternalsGardens.Entity.Usuario;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Mapper.SolicitudExhumacionMapper;
import com.imc.EternalsGardens.Repository.DifuntoRepository;
import com.imc.EternalsGardens.Repository.ParcelaRepository;
import com.imc.EternalsGardens.Repository.SolicitudExhumacionRepository;
import com.imc.EternalsGardens.Repository.UsuarioRepository;
import com.imc.EternalsGardens.Service.Interfaces.ISolicitudExhumacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudExhumacionServiceImpl implements ISolicitudExhumacionService {

    private final SolicitudExhumacionRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final DifuntoRepository difuntoRepository;
    private final ParcelaRepository parcelaRepository; // <--- INYECTAR ESTO
    private final SolicitudExhumacionMapper mapper;

    @Override
    @Transactional
    public SolicitudExhumacionResponse crearSolicitud(Integer usuarioId, SolicitudExhumacionRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + usuarioId));

        Difunto difunto = difuntoRepository.findById(request.getDifuntoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Difunto no encontrado con ID: " + request.getDifuntoId()));

        SolicitudExhumacion solicitud = mapper.toEntity(request);
        solicitud.setUsuario(usuario);
        solicitud.setDifunto(difunto);
        solicitud.setFechaSolicitud(LocalDateTime.now());

        // --- GENERAR NÚMERO DE SOLICITUD ---
        // Ejemplo simple: EXH + milisegundos
        String numSolicitud = "EXH-" + System.currentTimeMillis();
        solicitud.setNumeroSolicitud(numSolicitud);
        // -----------------------------------

        if (request.getParcelaId() != null) {
            Parcela parcela = parcelaRepository.findById(request.getParcelaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Parcela no encontrada"));
            solicitud.setParcela(parcela);
        }

        if (solicitud.getEstado() == null) {
            solicitud.setEstado("PENDIENTE");
        }

        return mapper.toResponse(repository.save(solicitud));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudExhumacionResponse> obtenerTodas() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudExhumacionResponse> obtenerPorUsuario(Integer usuarioId) {
        // Validación opcional: Verificar si el usuario existe antes de buscar
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }
        // Ahora sí funciona porque lo añadimos al repositorio
        return mapper.toResponseList(repository.findByUsuarioId(usuarioId));
    }

    @Override
    @Transactional
    public SolicitudExhumacionResponse cambiarEstado(Integer id, String nuevoEstado) {
        SolicitudExhumacion solicitud = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada con ID: " + id));

        solicitud.setEstado(nuevoEstado);

        // Lógica extra: Si se aprueba, podrías registrar fecha de aprobación, etc.
        // if ("APROBADA".equals(nuevoEstado)) { ... }

        return mapper.toResponse(repository.save(solicitud));
    }
}