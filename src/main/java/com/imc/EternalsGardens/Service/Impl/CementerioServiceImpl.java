package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.CementerioRequest;
import com.imc.EternalsGardens.DTO.Response.CementerioResponse;
import com.imc.EternalsGardens.Entity.Cementerio;
import com.imc.EternalsGardens.Entity.Usuario;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Mapper.CementerioMapper;
import com.imc.EternalsGardens.Repository.CementerioRepository;
import com.imc.EternalsGardens.Repository.UsuarioRepository;
import com.imc.EternalsGardens.Repository.ZonaRepository; // <--- NUEVO IMPORT
import com.imc.EternalsGardens.Service.Interfaces.ICementerioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CementerioServiceImpl implements ICementerioService {

    private final CementerioRepository cementerioRepository;
    private final UsuarioRepository usuarioRepository;
    private final ZonaRepository zonaRepository;
    private final CementerioMapper cementerioMapper;

    @Override
    @Transactional
    public CementerioResponse crearCementerio(CementerioRequest request) {
        Cementerio cementerio = cementerioMapper.toEntity(request);

        if (request.getResponsableId() != null) {
            Usuario responsable = usuarioRepository.findById(request.getResponsableId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Usuario responsable no encontrado con ID: " + request.getResponsableId()));
            cementerio.setResponsable(responsable);
        }

        cementerio.setFechaCreacion(LocalDateTime.now());
        cementerio.setActivo(true);

        return cementerioMapper.toResponse(cementerioRepository.save(cementerio));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CementerioResponse> obtenerTodos() {
        return cementerioMapper.toResponseList(cementerioRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CementerioResponse obtenerPorId(Integer id) {
        Cementerio cementerio = cementerioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cementerio no encontrado con ID: " + id));
        return cementerioMapper.toResponse(cementerio);
    }

    @Override
    @Transactional
    public CementerioResponse actualizarCementerio(Integer id, CementerioRequest request) {
        Cementerio cementerioExistente = cementerioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cementerio no encontrado con ID: " + id));

        cementerioMapper.updateEntity(request, cementerioExistente);

        if (request.getResponsableId() != null) {
            boolean cambioResponsable = cementerioExistente.getResponsable() == null ||
                    !cementerioExistente.getResponsable().getId().equals(request.getResponsableId());

            if (cambioResponsable) {
                Usuario nuevoResponsable = usuarioRepository.findById(request.getResponsableId())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario responsable no encontrado con ID: " + request.getResponsableId()));
                cementerioExistente.setResponsable(nuevoResponsable);
            }
        } else {
            cementerioExistente.setResponsable(null);
        }

        return cementerioMapper.toResponse(cementerioRepository.save(cementerioExistente));
    }

    @Override
    @Transactional
    public void eliminarCementerio(Integer id) {
        Cementerio cementerio = cementerioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cementerio no encontrado con ID: " + id));


        cementerio.setActivo(false);
        cementerioRepository.save(cementerio);
        zonaRepository.desactivarZonasPorCementerio(id);
    }

    @Override
    public List<CementerioResponse> buscarPorLocalizacion(String busqueda) {
        List<Cementerio> resultados = cementerioRepository.buscarPorLocalizacion(busqueda);
        return cementerioMapper.toResponseList(resultados);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CementerioResponse> buscarPorNombreOLocalizacion(String busqueda) {
        return cementerioMapper.toResponseList(
                cementerioRepository.buscarPorNombreOLocalizacion(busqueda)
        );
    }
}