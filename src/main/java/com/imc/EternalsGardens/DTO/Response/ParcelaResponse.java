package com.imc.EternalsGardens.DTO.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelaResponse {
    private Integer id;
    private Integer zonaId;
    private String zonaNombre;
    private Integer cementerioId; // Added for completeness
    private String cementerioNombre;
    private Integer numeroFila;
    private Integer numeroColumna;
    private String numeroIdentificadorUnico;
    private Integer tipoZonaId;
    private String tipoZonaNombre;
    private String estado;
    private Integer concesionId;

    // CAMPOS PARA KONVA.JS
    private java.math.BigDecimal posicionVisualX;
    private java.math.BigDecimal posicionVisualY;
    private java.math.BigDecimal anchoVisual;
    private java.math.BigDecimal altoVisual;

    // Listado de difuntos para mostrar en el mapa (tooltip/detalle)
    private java.util.List<DifuntosEnParcelaResponse> difuntos;
}