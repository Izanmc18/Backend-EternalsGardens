package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.ParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.ParcelaResponse;
import com.imc.EternalsGardens.Entity.Parcela;
import com.imc.EternalsGardens.Entity.TipoZona;
import com.imc.EternalsGardens.Entity.Zona;
import com.imc.EternalsGardens.Enum.EstadoParcelaEnum;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Exception.ReglaNegocioException;
import com.imc.EternalsGardens.Mapper.ParcelaMapper;
import com.imc.EternalsGardens.Repository.ParcelaRepository;
import com.imc.EternalsGardens.Repository.TipoZonaRepository;
import com.imc.EternalsGardens.Repository.ZonaRepository;
import com.imc.EternalsGardens.Service.Interfaces.IParcelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcelaServiceImpl implements IParcelaService {

    private final ParcelaRepository parcelaRepository;
    private final ZonaRepository zonaRepository;
    private final TipoZonaRepository tipoZonaRepository;
    private final ParcelaMapper parcelaMapper;

    @Override
    @Transactional
    public ParcelaResponse crearParcela(ParcelaRequest request) {
        // 1. Buscar dependencias
        Zona zona = zonaRepository.findById(request.getZonaId())
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("Zona no encontrada con ID: " + request.getZonaId()));

        TipoZona tipoZona = tipoZonaRepository.findById(request.getTipoZonaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Tipo de Zona no encontrado con ID: " + request.getTipoZonaId()));

        // 2. Generar Identificador Único (Si no viene en el request)
        // Formato: Z{idZona}-F{Fila}-C{Columna} (Ej: Z1-F5-C2)
        String codigoUnico = request.getNumeroIdentificadorUnico();
        if (codigoUnico == null || codigoUnico.isBlank()) {
            codigoUnico = "Z" + zona.getId() + "-F" + request.getNumeroFila() + "-C" + request.getNumeroColumna();
        }

        // 3. Validar que no exista ya esa ubicación física ocupada
        if (parcelaRepository.existsByNumeroIdentificadorUnico(codigoUnico)) {
            throw new ReglaNegocioException("Ya existe una parcela con el identificador: " + codigoUnico);
        }

        // 4. Crear entidad
        Parcela parcela = parcelaMapper.toEntity(request);
        parcela.setZona(zona);
        parcela.setTipoZona(tipoZona);
        parcela.setNumeroIdentificadorUnico(codigoUnico);

        // Estado inicial por defecto: LIBRE (si no se especifica)
        if (parcela.getEstado() == null) {
            parcela.setEstado(EstadoParcelaEnum.LIBRE.name()); // Asumiendo que usas String en entity
        }

        return parcelaMapper.toResponse(parcelaRepository.save(parcela));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelaResponse> obtenerTodas() {
        return parcelaMapper.toResponseList(parcelaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelaResponse> obtenerPorZona(Integer zonaId) {
        // Necesitarás añadir este método al repositorio: findByZonaId(Integer id)
        return parcelaMapper.toResponseList(parcelaRepository.findByZonaId(zonaId));
    }

    @Override
    @Transactional(readOnly = true)
    public ParcelaResponse obtenerPorId(Integer id) {
        Parcela parcela = parcelaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Parcela no encontrada con ID: " + id));
        return parcelaMapper.toResponse(parcela);
    }

    @Override
    @Transactional
    public ParcelaResponse actualizarParcela(Integer id, ParcelaRequest request) {
        Parcela parcela = parcelaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Parcela no encontrada con ID: " + id));

        parcelaMapper.updateEntity(request, parcela);

        // Actualizar zona/tipo si vienen
        if (request.getZonaId() != null) {
            Zona zona = zonaRepository.findById(request.getZonaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Zona no encontrada"));
            parcela.setZona(zona);
        }

        return parcelaMapper.toResponse(parcelaRepository.save(parcela));
    }

    @Override
    @Transactional
    public void eliminarParcela(Integer id) {
        if (!parcelaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Parcela no encontrada con ID: " + id);
        }
        // Validar si tiene difuntos o concesiones activas antes de borrar
        parcelaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelaResponse> obtenerPorUsuario(Integer usuarioId) {
        return parcelaMapper.toResponseList(parcelaRepository.findByConcesion_Usuario_Id(usuarioId));
    }
}