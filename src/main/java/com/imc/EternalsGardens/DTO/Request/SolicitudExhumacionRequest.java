package com.imc.EternalsGardens.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExhumacionRequest {

    @NotNull(message = "Debe indicar qué enterramiento se exhuma")
    private Integer difuntosEnParcelaId;

    @NotNull(message = "La concesión asociada es obligatoria")
    private Integer concesionId;

    @NotNull(message = "El usuario solicitante es obligatorio")
    private Integer usuarioSolicitanteId;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    private String descripcion;

    @NotNull(message = "El ID del difunto es obligatorio")
    private Integer difuntoId;

    private String estado;

    private Integer parcelaId;
}