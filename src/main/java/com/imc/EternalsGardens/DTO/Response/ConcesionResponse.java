package com.imc.EternalsGardens.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcesionResponse {
    private Integer id;
    private Integer usuarioId;
    private String usuarioNombreCompleto;
    private Integer cementerioId;
    private String cementerioNombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer periodoAnios;
    private BigDecimal precioPeriodo;
    private BigDecimal precioMantenimientoAnual;
    private String estado;
    private String tipoAlta;
}