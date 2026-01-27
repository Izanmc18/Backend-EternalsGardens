package com.imc.EternalsGardens.DTO.Response;

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
public class PagoResponse {
    private Integer id;
    private Integer concesionId;
    private Integer usuarioId;
    private String usuarioNombre;
    private BigDecimal monto;
    private String concepto;
    private LocalDateTime fechaPago;
    private String metodoPago;
    private String referenciaTransaccion;
    private String estado;
}