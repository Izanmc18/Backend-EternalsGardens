package com.imc.EternalsGardens.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para gestionar una parcela/nicho")
public class ParcelaRequest {

    @NotNull(message = "La zona es obligatoria")
    private Integer zonaId;

    @NotNull
    @Schema(description = "Fila física donde se ubica", example = "1")
    private Integer numeroFila;

    @NotNull
    @Schema(description = "Columna física donde se ubica", example = "5")
    private Integer numeroColumna;

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Código único visual (ej: N-105-B)", example = "Z1-F1-C5")
    private String numeroIdentificadorUnico;

    @NotNull
    private Integer tipoZonaId;

    @Schema(description = "Estado inicial (LIBRE, OCUPADO...)", example = "LIBRE")
    private String estado;

    @Schema(description = "ID de la concesión si ya está ocupada", example = "10")
    private Integer concesionId;

    // ========================================================================
    // CAMPOS PARA KONVA.JS - POSICIONES VISUALES
    // ========================================================================

    @Schema(description = "Posición X visual en la zona", example = "10.5")
    private java.math.BigDecimal posicionVisualX;

    @Schema(description = "Posición Y visual en la zona", example = "20.5")
    private java.math.BigDecimal posicionVisualY;

    @Schema(description = "Ancho visual de la parcela", example = "30")
    private java.math.BigDecimal anchoVisual;

    @Schema(description = "Alto visual de la parcela", example = "30")
    private java.math.BigDecimal altoVisual;
}