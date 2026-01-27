package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.TipoZonaRequest;
import com.imc.EternalsGardens.DTO.Response.TipoZonaResponse;
import java.util.List;

public interface ITipoZonaService {

    TipoZonaResponse crearTipoZona(TipoZonaRequest request);

    List<TipoZonaResponse> obtenerTodos();

    TipoZonaResponse obtenerPorId(Integer id);

    TipoZonaResponse actualizarTipoZona(Integer id, TipoZonaRequest request);

    void eliminarTipoZona(Integer id);
}