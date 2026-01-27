package com.imc.EternalsGardens.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DifuntosEnParcelaRequest {

    @NotNull(message = "El difunto es obligatorio")
    private Integer difuntoId;

    @NotNull(message = "La parcela es obligatoria")
    private Integer parcelaId;

    // CAMPO AÑADIDO: Faltaba este campo para vincular el entierro a la concesión
    @NotNull(message = "La concesión es obligatoria")
    private Integer concesionId;

    @NotNull
    @PastOrPresent
    private LocalDateTime fechaEnterramiento;

    private Integer operarioId;

    private String observaciones;
}