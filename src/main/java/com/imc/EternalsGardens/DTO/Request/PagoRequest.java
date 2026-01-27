package com.imc.EternalsGardens.DTO.Request;

import jakarta.validation.constraints.*;
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
public class PagoRequest {

    @NotNull
    private Integer concesionId;

    @NotNull
    private Integer usuarioId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal monto;

    @NotBlank
    @Size(max = 50)
    private String concepto;

    @NotNull
    private LocalDateTime fechaPago;

    @NotBlank
    private String metodoPago;

    private String referenciaTransaccion;

    private String estado;
}