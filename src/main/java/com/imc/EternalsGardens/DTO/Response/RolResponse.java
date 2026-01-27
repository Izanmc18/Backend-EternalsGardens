package com.imc.EternalsGardens.DTO.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para devolver datos de rol en respuestas.
 *
 * Se devuelve en:
 * <ul>
 *   <li>GET /api/roles - Listar roles</li>
 *   <li>GET /api/roles/{id} - Obtener rol específico</li>
 *   <li>POST /api/roles - Crear rol</li>
 *   <li>PUT /api/roles/{id} - Actualizar rol</li>
 * </ul>
 *
 * CAMPOS INCLUIDOS:
 * <ul>
 *   <li>id - Identificador único</li>
 *   <li>nombre - Nombre del rol</li>
 *   <li>descripcion - Descripción detallada</li>
 *   <li>activo - Estado actual</li>
 *   <li>fechaCreacion - Cuándo se creó</li>
 *   <li>fechaUltimaModificacion - Cuándo se actualizó</li>
 * </ul>
 *
 * @author Izan Martinez Castro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Datos de rol devueltos en respuestas",
        example = "{\n" +
                "  \"id\": 1,\n" +
                "  \"nombre\": \"ADMINISTRADOR\",\n" +
                "  \"descripcion\": \"Acceso total al sistema\",\n" +
                "  \"activo\": true,\n" +
                "  \"fechaCreacion\": \"2026-01-23T08:25:00\",\n" +
                "  \"fechaUltimaModificacion\": \"2026-01-23T08:25:00\"\n" +
                "}"
)
public class RolResponse {

    /**
     * Identificador único del rol (generado por BD).
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * USO: Referencia para relacionar usuarios con roles
     */
    @Schema(
            description = "Identificador único del rol",
            example = "1",
            required = true
    )
    private Integer id;

    /**
     * Nombre del rol.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * VALORES:
     * - ADMINISTRADOR
     * - OPERADOR_CEMENTERIO
     * - USUARIO
     */
    @Schema(
            description = "Nombre único del rol",
            example = "ADMINISTRADOR",
            required = true
    )
    private String nombre;

    /**
     * Descripción del rol.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * PUEDE SER NULL: Si no se proporcionó descripción
     */
    @Schema(
            description = "Descripción detallada del rol",
            example = "Acceso total al sistema",
            required = false
    )
    private String descripcion;

    /**
     * Estado de activación del rol.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * VALORES:
     * - true: Rol activo
     * - false: Rol inactivo
     */
    @Schema(
            description = "Estado de activación (true=activo, false=inactivo)",
            example = "true",
            type = "boolean",
            required = true
    )
    private Boolean activo;


}
