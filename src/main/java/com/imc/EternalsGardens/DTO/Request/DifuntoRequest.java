package com.imc.EternalsGardens.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DifuntoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @Past(message = "La fecha de nacimiento debe ser pasada")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La fecha de defunción es obligatoria")
    @PastOrPresent(message = "La fecha de defunción no puede ser futura")
    private LocalDate fechaDefuncion;

    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Formato de DNI inválido")
    private String dni;

    @Size(max = 5)
    @Schema(description = "Sexo del difunto (H/M/O)", example = "H")
    private String sexo;

    @Schema(description = "Causa del fallecimiento (opcional)")
    private String causa;

    private String notas;
    private String fotoPerfil;
    private String biografiaDigital;
    private Integer parcelaId;
}