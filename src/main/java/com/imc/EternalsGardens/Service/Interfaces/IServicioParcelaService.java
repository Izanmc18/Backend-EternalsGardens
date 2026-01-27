package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.ServicioParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.ServicioParcelaResponse;
import java.util.List;

public interface IServicioParcelaService {

    // Contratar un servicio para una parcela
    ServicioParcelaResponse contratarServicio(ServicioParcelaRequest request);

    List<ServicioParcelaResponse> obtenerTodos();

    ServicioParcelaResponse obtenerPorId(Integer id);

    // Método correcto: Buscar servicios por la parcela donde se aplican
    List<ServicioParcelaResponse> obtenerPorParcela(Integer parcelaId);

    // Método útil: Ver en qué parcelas se ha contratado cierto servicio
    List<ServicioParcelaResponse> obtenerPorServicio(Integer servicioId);

    // Actualizar estado (ej: PENDIENTE -> COMPLETADO)
    ServicioParcelaResponse cambiarEstado(Integer id, String nuevoEstado);

    void eliminarServicioParcela(Integer id);
}