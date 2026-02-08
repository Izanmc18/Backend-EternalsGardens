package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.DifuntoRequest;
import com.imc.EternalsGardens.DTO.Response.DifuntoResponse;
import java.util.List;

public interface IDifuntoService {
    DifuntoResponse crearDifunto(DifuntoRequest request);

    List<DifuntoResponse> obtenerTodos();

    List<DifuntoResponse> buscarDifuntos(String query);

    DifuntoResponse obtenerPorId(Integer id);

    DifuntoResponse actualizarDifunto(Integer id, DifuntoRequest request);

    void eliminarDifunto(Integer id);
}