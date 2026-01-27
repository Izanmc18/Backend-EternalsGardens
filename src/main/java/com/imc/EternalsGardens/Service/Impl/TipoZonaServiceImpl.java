package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.TipoZonaRequest;
import com.imc.EternalsGardens.DTO.Response.TipoZonaResponse;
import com.imc.EternalsGardens.Entity.TipoZona;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Exception.ReglaNegocioException; // Por si quieres validar duplicados
import com.imc.EternalsGardens.Mapper.TipoZonaMapper;
import com.imc.EternalsGardens.Repository.TipoZonaRepository;
import com.imc.EternalsGardens.Service.Interfaces.ITipoZonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoZonaServiceImpl implements ITipoZonaService {

    private final TipoZonaRepository tipoZonaRepository;
    private final TipoZonaMapper tipoZonaMapper;

    @Override
    @Transactional
    public TipoZonaResponse crearTipoZona(TipoZonaRequest request) {
        // Validación opcional: Evitar duplicados por nombre
        if (tipoZonaRepository.existsByNombre(request.getNombre())) {
            throw new ReglaNegocioException("Ya existe un tipo de zona con el nombre: " + request.getNombre());
        }

        TipoZona tipoZona = tipoZonaMapper.toEntity(request);
        return tipoZonaMapper.toResponse(tipoZonaRepository.save(tipoZona));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoZonaResponse> obtenerTodos() {
        return tipoZonaMapper.toResponseList(tipoZonaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public TipoZonaResponse obtenerPorId(Integer id) {
        TipoZona tipoZona = tipoZonaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de Zona no encontrado con ID: " + id));
        return tipoZonaMapper.toResponse(tipoZona);
    }

    @Override
    @Transactional
    public TipoZonaResponse actualizarTipoZona(Integer id, TipoZonaRequest request) {
        TipoZona tipoZona = tipoZonaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de Zona no encontrado con ID: " + id));

        // Actualizamos los campos usando el mapper
        tipoZonaMapper.updateEntity(request, tipoZona);

        return tipoZonaMapper.toResponse(tipoZonaRepository.save(tipoZona));
    }

    @Override
    @Transactional
    public void eliminarTipoZona(Integer id) {
        if (!tipoZonaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Tipo de Zona no encontrado con ID: " + id);
        }
        // Nota: Si hay zonas usando este tipo, la BD lanzará un error de restricción FK.
        // Podrías capturarlo aquí o dejar que el GlobalExceptionHandler lo gestione.
        tipoZonaRepository.deleteById(id);
    }
}