package com.imc.EternalsGardens.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List; // Importante

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcesionRequest {

    @NotNull(message = "El usuario titular es obligatorio")
    private Integer usuarioId;

    @NotNull(message = "El cementerio es obligatorio")
    private Integer cementerioId;

    // AÑADIDO: Lista de parcelas a incluir en la concesión
    @NotEmpty(message = "Debe seleccionar al menos una parcela")
    private List<Integer> parcelasIds;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @Min(1)
    private Integer periodoAnios;

    @DecimalMin("0.0")
    private BigDecimal precioPeriodo;

    @DecimalMin("0.0")
    private BigDecimal precioMantenimientoAnual;

    @NotBlank
    private String estado;

    @NotBlank
    private String tipoAlta;
}