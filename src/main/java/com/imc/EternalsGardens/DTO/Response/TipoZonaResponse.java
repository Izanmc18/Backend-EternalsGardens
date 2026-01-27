package com.imc.EternalsGardens.DTO.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para devolver datos de tipo de zona en respuestas.
 *
 * Se devuelve en:
 * <ul>
 *   <li>GET /api/tipo-zonas - Listar tipos</li>
 *   <li>GET /api/tipo-zonas/{id} - Obtener tipo específico</li>
 *   <li>POST /api/tipo-zonas - Crear tipo</li>
 *   <li>PUT /api/tipo-zonas/{id} - Actualizar tipo</li>
 * </ul>
 *
 * CAMPOS INCLUIDOS:
 * <ul>
 *   <li>id - Identificador único</li>
 *   <li>nombre - Nombre del tipo</li>
 *   <li>descripcion - Descripción detallada</li>
 * </ul>
 *
 * @author Izan Martinez Castro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Datos de tipo de zona devueltos en respuestas",
        example = "{\n" +
                "  \"id\": 1,\n" +
                "  \"nombre\": \"NICHO\",\n" +
                "  \"descripcion\": \"Espacios verticales en paredes para ataúdes\"\n" +
                "}"
)
public class TipoZonaResponse {

    /**
     * Identificador único del tipo de zona.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * USO: Referenciar tipo en otras operaciones
     */
    @Schema(
            description = "Identificador único del tipo de zona",
            example = "1",
            required = true
    )
    private Integer id;

    /**
     * Nombre del tipo de zona.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * VALORES:
     * - NICHO
     * - PARCELA
     * - COLUMBARIO
     * - OSARIO
     */
    @Schema(
            description = "Nombre único del tipo de zona",
            example = "NICHO",
            required = true
    )
    private String nombre;

    /**
     * Descripción del tipo de zona.
     *
     * ANOTACIONES:
     * - @Schema: Documentación
     *
     * PUEDE SER NULL: Si no se proporcionó
     */
    @Schema(
            description = "Descripción del tipo de zona",
            example = "Espacios verticales en paredes para ataúdes",
            required = false
    )
    private String descripcion;
}
