package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.DifuntosEnParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.DifuntosEnParcelaResponse;
import com.imc.EternalsGardens.Entity.*;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Mapper.DifuntosEnParcelaMapper;
import com.imc.EternalsGardens.Repository.*;
import com.imc.EternalsGardens.Service.Interfaces.IDifuntosEnParcelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DifuntosEnParcelaServiceImpl implements IDifuntosEnParcelaService {

    private final DifuntosEnParcelaRepository repository;
    private final DifuntoRepository difuntoRepository;
    private final ParcelaRepository parcelaRepository;
    private final ConcesionRepository concesionRepository;
    private final DifuntosEnParcelaMapper mapper;


    @Override
    @Transactional
    public DifuntosEnParcelaResponse registrarEnterramiento(DifuntosEnParcelaRequest request) {
        // 1. Validaciones previas
        Difunto difunto = difuntoRepository.findById(request.getDifuntoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Difunto no encontrado"));

        Parcela parcela = parcelaRepository.findById(request.getParcelaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Parcela no encontrada"));

        // 2. Validar capacidad (Opcional, según tu lógica de negocio)
        // Ejemplo: Si la parcela ya está llena de difuntos 'ENTERRADO'

        Concesion concesion = concesionRepository.findById(request.getConcesionId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Concesión no encontrada"));

        // 3. Validar que la parcela pertenece a la concesión (si aplica)
        if (parcela.getConcesion() == null || !parcela.getConcesion().getId().equals(concesion.getId())) {
            // Nota: Ajusta esta lógica según tu modelo de datos real si la parcela no tiene concesión directa
            // throw new ReglaNegocioException("La parcela seleccionada no pertenece a esta concesión.");
        }

        // 4. Crear registro
        DifuntosEnParcela enterramiento = mapper.toEntity(request);
        enterramiento.setDifunto(difunto);
        enterramiento.setParcela(parcela);
        enterramiento.setConcesion(concesion);
        // Usamos el nombre correcto del campo en la BD: fechaEnterramiento
        if(enterramiento.getFechaEnterramiento() == null) {
            enterramiento.setFechaEnterramiento(LocalDate.now());
        }

        // Estado inicial
        enterramiento.setEstado("ENTERRADO"); // O EstadoDifuntoEnum.ENTERRADO.name()

        return mapper.toResponse(repository.save(enterramiento));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DifuntosEnParcelaResponse> obtenerPorParcela(Integer parcelaId) {
        return mapper.toResponseList(repository.findByParcelaId(parcelaId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DifuntosEnParcelaResponse> obtenerPorConcesion(Integer concesionId) {
        return mapper.toResponseList(repository.findByConcesionId(concesionId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<DifuntosEnParcelaResponse> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return mapper.toResponseList(repository.findByFechaEnterramientoBetween(inicio, fin));
    }

    @Override
    @Transactional
    public void registrarExhumacion(Integer enterramientoId, String motivo) {
        DifuntosEnParcela registro = repository.findById(enterramientoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro de enterramiento no encontrado"));

        registro.setFechaExhumacion(LocalDate.now());
        registro.setMotivoExhumacion(motivo);
        registro.setEstado("EXHUMADO"); // O EstadoDifuntoEnum.EXHUMADO.name()

        repository.save(registro);
    }


}