package com.imc.EternalsGardens.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir datos de creación y actualización de tipo de zona.
 *
 * Se usa para:
 * <ul>
 *   <li>POST /api/tipo-zonas - Crear nuevo tipo</li>
 *   <li>PUT /api/tipo-zonas/{id} - Actualizar tipo</li>
 * </ul>
 *
 * VALIDACIONES:
 * <ul>
 *   <li>nombre: Obligatorio, único, 2-50 caracteres</li>
 *   <li>descripcion: Opcional, máximo 500 caracteres</li>
 * </ul>
 *
 * @author Izan Martinez Castro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Datos para crear o actualizar un tipo de zona",
        example = "{\n" +
                "  \"nombre\": \"NICHO\",\n" +
                "  \"descripcion\": \"Espacios verticales en paredes para ataúdes\"\n" +
                "}"
)
public class TipoZonaRequest {

    /**
     * Nombre único del tipo de zona.
     *
     * ANOTACIONES:
     * - @NotBlank: No puede ser null, vacío o solo espacios
     * - @Size(min=2, max=50): Entre 2 y 50 caracteres
     * - @Pattern: Solo mayúsculas y caracteres especiales permitidos
     * - @Schema: Documentación para Swagger
     *
     * VALORES TÍPICOS:
     * - NICHO
     * - PARCELA
     * - COLUMBARIO
     * - OSARIO
     */
    @NotBlank(message = "El nombre del tipo de zona es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(
            regexp = "^[A-Z][A-Z_]*$",
            message = "El nombre debe comenzar con mayúscula y contener solo mayúsculas y guiones bajos"
    )
    @Schema(
            description = "Nombre único del tipo de zona",
            example = "NICHO",
            minLength = 2,
            maxLength = 50,
            pattern = "^[A-Z][A-Z_]*$"
    )
    private String nombre;

    /**
     * Descripción detallada del tipo de zona.
     *
     * ANOTACIONES:
     * - @Size(max=500): Máximo 500 caracteres
     * - Campo OPCIONAL (no tiene @NotBlank)
     *
     * EJEMPLOS:
     * - "Espacios verticales en paredes para ataúdes"
     * - "Terrenos en el suelo para enterramientos"
     * - "Compartimentos para urnas de cenizas"
     */
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Schema(
            description = "Descripción del tipo de zona (opcional)",
            example = "Espacios verticales en paredes para ataúdes",
            maxLength = 500
    )
    private String descripcion;
}
