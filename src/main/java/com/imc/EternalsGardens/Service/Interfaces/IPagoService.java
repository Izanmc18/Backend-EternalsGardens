package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.PagoRequest;
import com.imc.EternalsGardens.DTO.Response.PagoResponse;
import java.util.List;

public interface IPagoService {

    PagoResponse registrarPago(PagoRequest request);

    List<PagoResponse> obtenerTodos();

    PagoResponse obtenerPorId(Integer id);

    List<PagoResponse> obtenerPorConcesion(Integer concesionId);

    List<PagoResponse> obtenerPorUsuario(Integer usuarioId);

    PagoResponse obtenerPorReferencia(String referencia);

    void eliminarPago(Integer id);
}