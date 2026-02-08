package com.imc.EternalsGardens.DTO.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.imc.EternalsGardens.DTO.Response.OperadorResponse;

/**
 * DTO para devolver datos de cementerio en respuestas.
 *
 * Se devuelve en:
 * <ul>
 * <li>GET /api/cementerios - Listar cementerios</li>
 * <li>GET /api/cementerios/{id} - Obtener cementerio específico</li>
 * <li>POST /api/cementerios - Crear cementerio</li>
 * <li>PUT /api/cementerios/{id} - Actualizar cementerio</li>
 * </ul>
 *
 * CAMPOS INCLUIDOS:
 * <ul>
 * <li>id - Identificador único</li>
 * <li>nombre - Nombre del cementerio</li>
 * <li>municipio - Ubicación municipal</li>
 * <li>provincia - Ubicación provincial</li>
 * <li>codigoPostal - Código postal</li>
 * <li>coordenadas - Geolocalización</li>
 * <li>contacto - Teléfono y email</li>
 * <li>responsable - Datos del gestor</li>
 * <li>activo - Estado actual</li>
 * <li>fechaCreacion - Auditoría</li>
 * </ul>
 *
 * @author Izan Martinez Castro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de cementerio devueltos en respuestas", example = "{\n" +
                "  \"id\": 1,\n" +
                "  \"nombre\": \"Cementerio de Jaén\",\n" +
                "  \"municipio\": \"Jaén\",\n" +
                "  \"provincia\": \"Jaén\",\n" +
                "  \"codigoPostal\": \"23001\",\n" +
                "  \"coordenadaX\": -3.78945678,\n" +
                "  \"coordenadaY\": 37.77945678,\n" +
                "  \"telefono\": \"+34 953 123456\",\n" +
                "  \"email\": \"contacto@cementerio-jaen.es\",\n" +
                "  \"responsableId\": 1,\n" +
                "  \"responsableNombre\": \"Juan García\",\n" +
                "  \"activo\": true,\n" +
                "  \"fechaCreacion\": \"2026-01-23T08:30:00\"\n" +
                "}")
public class CementerioResponse {

        /**
         * Identificador único del cementerio.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         *
         * USO: Referenciar cementerio en otras operaciones
         */
        @Schema(description = "Identificador único del cementerio", example = "1", required = true)
        private Integer id;

        /**
         * Nombre del cementerio.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         */
        @Schema(description = "Nombre del cementerio", example = "Cementerio de Jaén", required = true)
        private String nombre;

        /**
         * Municipio del cementerio.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         */
        @Schema(description = "Municipio del cementerio", example = "Jaén", required = true)
        private String municipio;

        /**
         * Provincia del cementerio.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         */
        @Schema(description = "Provincia del cementerio", example = "Jaén", required = true)
        private String provincia;
        private String fotoUrl;

        /**
         * Código postal del cementerio.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         *
         * PUEDE SER NULL: Si no se proporcionó
         */
        @Schema(description = "Código postal del cementerio", example = "23001", required = false)
        private String codigoPostal;

        /**
         * Coordenada X (Longitud) para geolocalización.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         *
         * FORMATO: Decimal WGS84
         * PUEDE SER NULL: Si no se proporcionó
         */
        @Schema(description = "Coordenada X (Longitud) WGS84", example = "-3.78945678", required = false)
        private BigDecimal coordenadaX;

        /**
         * Coordenada Y (Latitud) para geolocalización.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         *
         * FORMATO: Decimal WGS84
         * PUEDE SER NULL: Si no se proporcionó
         */
        @Schema(description = "Coordenada Y (Latitud) WGS84", example = "37.77945678", required = false)
        private BigDecimal coordenadaY;

        /**
         * Teléfono de contacto del cementerio.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         *
         * PUEDE SER NULL: Si no se proporcionó
         */
        @Schema(description = "Teléfono de contacto", example = "+34 953 123456", required = false)
        private String telefono;

        /**
         * Email de contacto del cementerio.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         *
         * PUEDE SER NULL: Si no se proporcionó
         */
        @Schema(description = "Email de contacto", example = "contacto@cementerio-jaen.es", required = false)
        private String email;

        /**
         * ID del usuario responsable del cementerio.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         *
         * PUEDE SER NULL: Si el cementerio no tiene responsable
         */
        @Schema(description = "ID del usuario responsable", example = "1", required = false, type = "integer")
        private Integer responsableId;

        /**
         * Nombre completo del usuario responsable.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         *
         * FORMATO: "Nombre Apellidos"
         * PUEDE SER NULL: Si no hay responsable
         *
         * NOTA: Se extrae del Usuario responsable en el mapper
         */
        @Schema(description = "Nombre completo del responsable", example = "Juan García López", required = false)
        private String responsableNombre;

        /**
         * Estado de activación del cementerio.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         *
         * VALORES:
         * - true: Activo
         * - false: Inactivo
         */
        @Schema(description = "Estado de activación", example = "true", type = "boolean", required = true)
        private Boolean activo;

        /**
         * Fecha y hora de creación del cementerio.
         *
         * ANOTACIONES:
         * - @Schema: Documentación, formato date-time
         *
         * FORMATO: ISO 8601 (2026-01-23T08:30:00)
         *
         * USO: Auditoría
         */
        @Schema(description = "Fecha y hora de creación", example = "2026-01-23T08:30:00", type = "string", format = "date-time", required = false)
        private LocalDateTime fechaCreacion;

        @Schema(description = "Lista de operadores asignados a este cementerio")
        private List<OperadorResponse> operadores;

        // ========================================================================
        // CAMPOS PARA KONVA.JS - CONFIGURACIÓN DEL MAPA INTERACTIVO
        // ========================================================================

        @Schema(description = "Ancho del canvas del mapa en píxeles", example = "1200")
        private Integer mapaAncho;

        @Schema(description = "Alto del canvas del mapa en píxeles", example = "800")
        private Integer mapaAlto;

        @Schema(description = "Escala del mapa", example = "1.0")
        private BigDecimal mapaEscala;

        @Schema(description = "URL de la imagen de fondo del plano del cementerio", example = "https://storage.example.com/plano.png")
        private String imagenFondo;

        // ========================================================================
        // CAMPOS PARA CAPACIDAD Y OCUPACIÓN
        // ========================================================================

        @Schema(description = "Capacidad total de parcelas del cementerio", example = "500")
        private Integer capacidadTotal;

        @Schema(description = "Número de parcelas ocupadas o reservadas", example = "250")
        private Integer ocupacionActual;
}
