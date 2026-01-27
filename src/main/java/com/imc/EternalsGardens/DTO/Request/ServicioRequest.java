package com.imc.EternalsGardens.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioRequest {

    @NotBlank
    @Size(max = 100)
    private String nombre;

    private String descripcion;

    @Schema(description = "ID del tipo de zona asociado (opcional)", example = "1")
    private Integer tipoZonaId;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal precioActual;

    @NotNull
    private Boolean activo;
}