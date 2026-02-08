package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.ConcesionRequest;
import com.imc.EternalsGardens.DTO.Response.ConcesionResponse;
import java.util.List;

public interface IConcesionService {
    ConcesionResponse crearConcesion(ConcesionRequest request);

    List<ConcesionResponse> obtenerTodas();

    List<ConcesionResponse> obtenerPorTitular(Integer usuarioId);

    List<ConcesionResponse> obtenerPorCementerio(Integer cementerioId);

    ConcesionResponse obtenerPorId(Integer id);

    void cambiarEstadoConcesion(Integer id, String nuevoEstado);

    ConcesionResponse comprarParcela(Integer usuarioId, Integer parcelaId);
}