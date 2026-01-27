package com.imc.EternalsGardens.DTO.Response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SolicitudExhumacionResponse {
    private Integer id;
    private String numeroSolicitud;
    private String motivo;
    private String observaciones;
    private LocalDateTime fechaSolicitud;
    private String estado;

    // --- CAMPOS QUE FALTABAN ---
    private Integer difuntoId;
    private String difuntoNombre;

    private Integer usuarioSolicitanteId;
    private String usuarioSolicitanteNombre;

    private Integer usuarioAprobadorId; // Opcional, pero recomendable
    private String usuarioAprobadorNombre;
    // ---------------------------
}