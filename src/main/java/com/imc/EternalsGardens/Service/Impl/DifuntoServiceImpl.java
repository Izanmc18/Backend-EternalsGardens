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

@Service
@RequiredArgsConstructor
public class DifuntoServiceImpl implements IDifuntoService {

    private final DifuntoRepository difuntoRepository;
    private final DifuntoMapper difuntoMapper;

    @Override
    @Transactional
    public DifuntoResponse crearDifunto(DifuntoRequest request) {
        Difunto difunto = difuntoMapper.toEntity(request);
        return difuntoMapper.toResponse(difuntoRepository.save(difunto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DifuntoResponse> obtenerTodos() {
        return difuntoMapper.toResponseList(difuntoRepository.findAll());
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
    public DifuntoResponse actualizarDifunto(Integer id, DifuntoRequest request) {
        Difunto difunto = difuntoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Difunto no encontrado ID: " + id));

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
}