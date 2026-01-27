package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.ZonaRequest;
import com.imc.EternalsGardens.DTO.Response.ZonaResponse;
import java.util.List;

public interface IZonaService {
    ZonaResponse crearZona(ZonaRequest request);
    List<ZonaResponse> obtenerTodas();
    List<ZonaResponse> obtenerPorCementerio(Integer cementerioId);
    ZonaResponse obtenerPorId(Integer id);
    ZonaResponse actualizarZona(Integer id, ZonaRequest request);
    void eliminarZona(Integer id);
}