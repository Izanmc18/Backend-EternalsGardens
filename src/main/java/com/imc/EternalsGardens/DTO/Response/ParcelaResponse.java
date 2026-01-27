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
    private String cementerioNombre;
    private Integer numeroFila;
    private Integer numeroColumna;
    private String numeroIdentificadorUnico;
    private Integer tipoZonaId;
    private String tipoZonaNombre;
    private String estado;
    private Integer concesionId;
}