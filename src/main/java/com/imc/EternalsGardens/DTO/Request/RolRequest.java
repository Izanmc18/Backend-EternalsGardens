package com.imc.EternalsGardens.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir datos de creación y actualización de rol.
 *
 * Se usa para:
 * <ul>
 *   <li>POST /api/roles - Crear nuevo rol</li>
 *   <li>PUT /api/roles/{id} - Actualizar rol existente</li>
 * </ul>
 *
 * VALIDACIONES:
 * <ul>
 *   <li>nombre: Obligatorio, único, 2-50 caracteres</li>
 *   <li>descripcion: Opcional, máximo 500 caracteres</li>
 *   <li>activo: Obligatorio, true/false</li>
 * </ul>
 *
 * @author Izan Martinez Castro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Datos para crear o actualizar un rol",
        example = "{\n" +
                "  \"nombre\": \"ADMINISTRADOR\",\n" +
                "  \"descripcion\": \"Acceso total al sistema\",\n" +
                "  \"activo\": true\n" +
                "}"
)
public class RolRequest {

    /**
     * Nombre único del rol.
     *
     * ANOTACIONES:
     * - @NotBlank: No puede ser null, vacío o solo espacios
     * - @Size(min=2, max=50): Entre 2 y 50 caracteres
     * - @Pattern: Validación con expresión regular
     *   * ^[A-Z_]+$: Solo letras mayúsculas y guiones bajos
     *   * Ejemplos válidos: ADMINISTRADOR, OPERADOR_CEMENTERIO, USUARIO
     * - @Schema: Documentación para Swagger
     *
     * VALORES TÍPICOS:
     * - ADMINISTRADOR
     * - OPERADOR_CEMENTERIO
     * - USUARIO
     */
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(
            regexp = "^[A-Z_]+$",
            message = "El nombre debe contener solo letras mayúsculas y guiones bajos (ej: ADMINISTRADOR)"
    )
    @Schema(
            description = "Nombre único del rol (mayúsculas y guiones bajos)",
            example = "ADMINISTRADOR",
            minLength = 2,
            maxLength = 50,
            pattern = "^[A-Z_]+$"
    )
    private String nombre;

    /**
     * Descripción detallada del rol.
     *
     * ANOTACIONES:
     * - @Size(max=500): Máximo 500 caracteres
     * - Campo OPCIONAL (no tiene @NotBlank)
     * - @Schema: Documentación
     *
     * EJEMPLOS:
     * - "Acceso total a todas las funcionalidades del sistema"
     * - "Gestiona cementerios, parcelas, nichos y concesiones"
     * - "Acceso a sus propias concesiones y datos personales"
     */
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Schema(
            description = "Descripción del rol (opcional)",
            example = "Acceso total al sistema",
            maxLength = 500
    )
    private String descripcion;

    /**
     * Estado de activación del rol.
     *
     * ANOTACIONES:
     * - @NotNull: Obligatorio, debe ser true o false
     * - @Schema: Documentación
     *
     * VALORES:
     * - true: Rol activo, se puede asignar a usuarios
     * - false: Rol desactivado, no se puede asignar
     *
     * CASO DE USO:
     * - Desactivar roles sin eliminarlos
     * - Mantener histórico de roles usados anteriormente
     */
    @NotNull(message = "El estado activo es obligatorio")
    @Schema(
            description = "Estado de activación (true=activo, false=inactivo)",
            example = "true",
            type = "boolean"
    )
    private Boolean activo;
}
