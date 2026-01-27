package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.SolicitudExhumacionRequest;
import com.imc.EternalsGardens.DTO.Response.SolicitudExhumacionResponse;
import java.util.List;

public interface ISolicitudExhumacionService {
    SolicitudExhumacionResponse crearSolicitud(Integer usuarioId, SolicitudExhumacionRequest request);
    List<SolicitudExhumacionResponse> obtenerTodas();
    List<SolicitudExhumacionResponse> obtenerPorUsuario(Integer usuarioId);
    SolicitudExhumacionResponse cambiarEstado(Integer id, String nuevoEstado); // APROBADA, RECHAZADA
}