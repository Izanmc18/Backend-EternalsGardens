package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.ServicioRequest;
import com.imc.EternalsGardens.DTO.Response.ServicioResponse;
import java.util.List;

public interface IServicioService {

    ServicioResponse crearServicio(ServicioRequest request);

    List<ServicioResponse> obtenerTodos();

    ServicioResponse obtenerPorId(Integer id);

    List<ServicioResponse> obtenerPorTipoZona(Integer tipoZonaId);

    ServicioResponse actualizarServicio(Integer id, ServicioRequest request);

    void eliminarServicio(Integer id);
}