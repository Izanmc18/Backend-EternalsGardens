package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.DifuntosEnParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.DifuntosEnParcelaResponse;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface IDifuntosEnParcelaService {
    DifuntosEnParcelaResponse registrarEnterramiento(DifuntosEnParcelaRequest request);
    List<DifuntosEnParcelaResponse> obtenerPorParcela(Integer parcelaId);
    List<DifuntosEnParcelaResponse> obtenerPorConcesion(Integer concesionId);

    @Transactional(readOnly = true)
    List<DifuntosEnParcelaResponse> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);

    void registrarExhumacion(Integer enterramientoId, String motivo);
}