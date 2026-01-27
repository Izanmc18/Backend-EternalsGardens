package com.imc.EternalsGardens.DTO.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de una zona")
public class ZonaResponse {

    private Integer id;
    private Integer cementerioId;
    private String cementerioNombre;
    private String nombre;
    private Integer tipoZonaId;
    private String tipoZonaNombre;
    private Integer filas;
    private Integer columnas;
    private Integer capacidadTotal;
    private String poligonoCoordinadas;
    private BigDecimal coordenadaX;
    private BigDecimal coordenadaY;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
}