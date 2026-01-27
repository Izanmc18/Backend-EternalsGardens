package com.imc.EternalsGardens.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioParcelaResponse {
    private Integer id;

    private Integer servicioId;
    private String servicioNombre;
    private BigDecimal precioCobrado;

    private Integer parcelaId;
    private String parcelaIdentificador;

    private LocalDateTime fechaProgramada;
    private LocalDateTime fechaRealizacion;
    private String estado;
    private String observaciones;
}