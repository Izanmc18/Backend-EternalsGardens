package com.imc.EternalsGardens.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioResponse {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer tipoZonaId;
    private String tipoZonaNombre;
    private BigDecimal precioActual;
    private Boolean activo;
}