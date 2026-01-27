package com.imc.EternalsGardens.DTO.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para devolver datos del usuario en respuestas.
 *
 * Se usa para enviar información del usuario al cliente sin exponer
 * datos sensibles como contraseñas. Se devuelve en respuestas de:
 * - Login (devolviendo datos del usuario autenticado)
 * - Obtener perfil
 * - Listar usuarios (admin)
 * - Actualizar perfil
 *
 *  IMPORTANTE: NUNCA contiene password (por seguridad)
 *
 * @author Izan Martinez Castro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Datos de usuario devueltos en respuestas del servidor",
        example = "{\n" +
                "  \"id\": 1,\n" +
                "  \"nombre\": \"Juan\",\n" +
                "  \"apellidos\": \"García López\",\n" +
                "  \"dni\": \"12345678Z\",\n" +
                "  \"email\": \"juan@email.com\",\n" +
                "  \"telefono\": \"612345678\",\n" +
                "  \"fechaNacimiento\": \"1985-03-15\",\n" +
                "  \"rolId\": 1,\n" +
                "  \"rolNombre\": \"ADMINISTRADOR\",\n" +
                "  \"activo\": true,\n" +
                "  \"fechaCreacion\": \"2026-01-22T07:30:00\",\n" +
                "  \"fechaUltimaModificacion\": \"2026-01-22T07:30:00\"\n" +
                "}"
)
public class UsuarioResponse {


    /**
     * Identificador único del usuario.
     *
     * ANOTACIONES:
     * - @Schema: Documentación para Swagger
     *   * description: Explicación del campo
     *   * example: Valor de ejemplo
     *   * required: Este campo siempre aparece en respuestas
     *
     * USO: Se devuelve para que el cliente pueda referenciar al usuario
     *      en futuras peticiones (ej: actualizar, eliminar, etc)
     *
     * ORIGEN: Generado por la base de datos (autoincrement)
     */
    @Schema(
            description = "Identificador único del usuario (generado por BD)",
            example = "1",
            required = true
    )
    private Integer id;


    /**
     * Nombre completo del usuario.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * USO: Identificación visual del usuario en la UI
     */
    @Schema(
            description = "Nombre completo del usuario",
            example = "Juan",
            required = true
    )
    private String nombre;


    /**
     * Apellidos del usuario.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * USO: Identificación completa junto con nombre
     */
    @Schema(
            description = "Apellidos completos del usuario",
            example = "García López",
            required = true
    )
    private String apellidos;


    /**
     * DNI del usuario.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * USO: Identificación legal en el sistema
     *
     * SEGURIDAD: Se devuelve porque es información pública del usuario
     *            (pero se oculta en algunos contextos como listados)
     */
    @Schema(
            description = "DNI del usuario",
            example = "12345678Z",
            required = true
    )
    private String dni;


    /**
     * Email del usuario.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * USO: Contacto principal y login del usuario
     */
    @Schema(
            description = "Email del usuario (usado para login)",
            example = "juan.garcia@email.com",
            required = true
    )
    private String email;


    /**
     * Teléfono móvil del usuario.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * USO: Contacto alternativo para notificaciones
     */
    @Schema(
            description = "Teléfono móvil del usuario",
            example = "612345678",
            required = true
    )
    private String telefono;


    /**
     * Fecha de nacimiento del usuario.
     *
     * ANOTACIONES:
     * - @Schema: Documentación, tipo date
     *
     * USO: Para calcular edad, verificar mayoría de edad, etc
     */
    @Schema(
            description = "Fecha de nacimiento del usuario",
            example = "1985-03-15",
            type = "string",
            format = "date",
            required = true
    )
    private LocalDate fechaNacimiento;


    /**
     * ID del rol del usuario.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * USO: Referencia a la tabla Rol para permisos y autorización
     *
     * RELACIÓN: Se envía junto con rolNombre para legibilidad
     */
    @Schema(
            description = "ID del rol asignado al usuario",
            example = "1",
            required = true
    )
    private Integer rolId;


    /**
     * Nombre del rol en texto legible.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * USO: Para mostrar al usuario su rol en la UI sin otra búsqueda
     *
     * BENEFICIO: Client-side no necesita otra petición para saber
     *            qué rol tiene (rolId: 1, rolNombre: "ADMINISTRADOR")
     *
     * MAPEO: Se obtiene de Rol.nombre en el mapper
     */
    @Schema(
            description = "Nombre del rol en texto (ej: ADMINISTRADOR, OPERADOR, USUARIO)",
            example = "ADMINISTRADOR",
            required = true
    )
    private String rolNombre;


    /**
     * Estado de activación del usuario.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * USO: Para mostrar si el usuario está activo o desactivado
     *
     * SEGURIDAD: Los usuarios inactivos no pueden acceder al sistema
     */
    @Schema(
            description = "Estado de activación (true=activo, false=inactivo)",
            example = "true",
            required = true,
            type = "boolean"
    )
    private Boolean activo;


    /**
     * Fecha y hora de creación de la cuenta.
     *
     * ANOTACIONES:
     * - @Schema: Documentación, tipo date-time
     *
     * USO: Auditoría - saber cuándo se creó la cuenta
     *
     * ORIGEN: Se establece automáticamente en la entidad
     *         con @CreationTimestamp o similar
     */
    @Schema(
            description = "Fecha y hora de creación de la cuenta",
            example = "2026-01-22T07:30:00",
            type = "string",
            format = "date-time",
            required = false
    )
    private LocalDateTime fechaCreacion;


    /**
     * Fecha y hora de la última modificación.
     *
     * ANOTACIONES:
     * - @Schema: Documentación, tipo date-time
     *
     * USO: Auditoría - saber cuándo fue la última actualización
     *
     * ORIGEN: Se actualiza automáticamente en la entidad
     *         con @UpdateTimestamp o similar
     *
     * CAMBIOS QUE ACTUALIZAN:
     * - Cambio de contraseña
     * - Actualización de perfil
     * - Cambio de estado (activo/inactivo)
     * - Cambio de rol
     */
    @Schema(
            description = "Fecha y hora de la última modificación",
            example = "2026-01-22T10:45:30",
            type = "string",
            format = "date-time",
            required = false
    )
    private LocalDateTime fechaUltimaModificacion;
}
