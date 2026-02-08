package com.imc.EternalsGardens.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO para recibir datos de creación y actualización de cementerio.
 *
 * Se usa para:
 * <ul>
 * <li>POST /api/cementerios - Crear nuevo cementerio</li>
 * <li>PUT /api/cementerios/{id} - Actualizar cementerio existente</li>
 * </ul>
 *
 * VALIDACIONES:
 * <ul>
 * <li>nombre: Obligatorio, 2-150 caracteres</li>
 * <li>municipio: Obligatorio, 2-100 caracteres</li>
 * <li>provincia: Obligatorio, 2-100 caracteres</li>
 * <li>coordenadas: Opcionales, formato decimal válido</li>
 * <li>responsableId: Opcional, debe existir usuario en BD</li>
 * </ul>
 *
 * @author Izan Martinez Castro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para crear o actualizar un cementerio", example = "{\n" +
                "  \"nombre\": \"Cementerio de Jaén\",\n" +
                "  \"municipio\": \"Jaén\",\n" +
                "  \"provincia\": \"Jaén\",\n" +
                "  \"codigoPostal\": \"23001\",\n" +
                "  \"coordenadaX\": -3.78945678,\n" +
                "  \"coordenadaY\": 37.77945678,\n" +
                "  \"telefono\": \"+34 953 123456\",\n" +
                "  \"email\": \"contacto@cementerio-jaen.es\",\n" +
                "  \"responsableId\": 1,\n" +
                "  \"activo\": true\n" +
                "}")
public class CementerioRequest {

        /**
         * Nombre del cementerio.
         *
         * ANOTACIONES:
         * - @NotBlank: No puede ser null, vacío o solo espacios
         * - @Size(min=2, max=150): Entre 2 y 150 caracteres
         * - @Schema: Documentación para Swagger
         *
         * EJEMPLOS:
         * - "Cementerio de Jaén"
         * - "Necrópolis Municipal de Madrid"
         */
        @NotBlank(message = "El nombre del cementerio es obligatorio")
        @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres")
        @Schema(description = "Nombre del cementerio", example = "Cementerio de Jaén", minLength = 2, maxLength = 150)
        private String nombre;

        /**
         * Municipio donde se encuentra el cementerio.
         *
         * ANOTACIONES:
         * - @NotBlank: Obligatorio
         * - @Size(min=2, max=100): Entre 2 y 100 caracteres
         *
         * EJEMPLOS:
         * - "Jaén"
         * - "Madrid"
         * - "Barcelona"
         */
        @NotBlank(message = "El municipio es obligatorio")
        @Size(min = 2, max = 100, message = "El municipio debe tener entre 2 y 100 caracteres")
        @Schema(description = "Municipio del cementerio", example = "Jaén", minLength = 2, maxLength = 100)
        private String municipio;

        /**
         * Provincia donde se encuentra el cementerio.
         *
         * ANOTACIONES:
         * - @NotBlank: Obligatorio
         * - @Size(min=2, max=100): Entre 2 y 100 caracteres
         *
         * EJEMPLOS:
         * - "Jaén"
         * - "Madrid"
         * - "Barcelona"
         */
        @NotBlank(message = "La provincia es obligatoria")
        @Size(min = 2, max = 100, message = "La provincia debe tener entre 2 y 100 caracteres")
        @Schema(description = "Provincia del cementerio", example = "Jaén", minLength = 2, maxLength = 100)
        private String provincia;

        /**
         * Código postal del cementerio.
         *
         * ANOTACIONES:
         * - @Size(max=10): Máximo 10 caracteres
         * - Campo OPCIONAL
         * - @Pattern: Validación de formato
         *
         * FORMATO: Puede ser número o incluir guiones
         * EJEMPLOS:
         * - "23001"
         * - "28001"
         */
        @Size(max = 10, message = "El código postal no puede exceder 10 caracteres")
        @Pattern(regexp = "^[0-9]{5}$|^[0-9A-Z]{2,10}$", message = "El código postal debe ser válido (ej: 23001)")
        @Schema(description = "Código postal (opcional)", example = "23001", maxLength = 10)
        private String codigoPostal;

        /**
         * Coordenada X (Longitud) para geolocalización.
         *
         * ANOTACIONES:
         * - @DecimalMin/@DecimalMax: Rango válido -180 a 180
         * - Campo OPCIONAL
         * - @Schema: Documentación
         *
         * FORMATO: Número decimal con máximo 8 decimales
         * EJEMPLO: -3.78945678 (Jaén, España)
         * RANGO: -180 a 180 (estándar WGS84)
         */
        @DecimalMin(value = "-180.0", message = "La coordenada X debe estar entre -180 y 180")
        @DecimalMax(value = "180.0", message = "La coordenada X debe estar entre -180 y 180")
        @Schema(description = "Coordenada X (Longitud) para geolocalización", example = "-3.78945678", minimum = "-180.0", maximum = "180.0")
        private BigDecimal coordenadaX;

        /**
         * Coordenada Y (Latitud) para geolocalización.
         *
         * ANOTACIONES:
         * - @DecimalMin/@DecimalMax: Rango válido -90 a 90
         * - Campo OPCIONAL
         * - @Schema: Documentación
         *
         * FORMATO: Número decimal con máximo 8 decimales
         * EJEMPLO: 37.77945678 (Jaén, España)
         * RANGO: -90 a 90 (estándar WGS84)
         */
        @DecimalMin(value = "-90.0", message = "La coordenada Y debe estar entre -90 y 90")
        @DecimalMax(value = "90.0", message = "La coordenada Y debe estar entre -90 y 90")
        @Schema(description = "Coordenada Y (Latitud) para geolocalización", example = "37.77945678", minimum = "-90.0", maximum = "90.0")
        private BigDecimal coordenadaY;

        /**
         * Teléfono de contacto del cementerio.
         *
         * ANOTACIONES:
         * - @Size(max=20): Máximo 20 caracteres
         * - Campo OPCIONAL
         * - @Pattern: Validación de formato telefónico
         *
         * FORMATO: Número telefónico internacional
         * EJEMPLOS:
         * - "+34 953 123456"
         * - "953123456"
         * - "+34-953-123456"
         */
        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        @Pattern(regexp = "^[+]?[0-9\\s\\-.()]{7,20}$", message = "El teléfono debe tener un formato válido (mínimo 7 dígitos)")
        @Schema(description = "Teléfono de contacto (opcional)", example = "+34 953 123456", maxLength = 20)
        private String telefono;

        /**
         * Email de contacto del cementerio.
         *
         * ANOTACIONES:
         * - @Email: Validación de formato email
         * - @Size(max=100): Máximo 100 caracteres
         * - Campo OPCIONAL
         *
         * FORMATO: Email válido
         * EJEMPLO: "contacto@cementerio-jaen.es"
         */
        @Email(message = "El email debe tener un formato válido")
        @Size(max = 100, message = "El email no puede exceder 100 caracteres")
        @Schema(description = "Email de contacto (opcional)", example = "contacto@cementerio-jaen.es", maxLength = 100)
        private String email;

        /**
         * URL de la foto principal del cementerio.
         *
         * ANOTACIONES:
         * - @Size(max=255): Máximo 255 caracteres
         * - @Pattern: Validación de formato URL
         * - Campo OPCIONAL
         *
         * FORMATO: URL válida
         * EJEMPLO: "https://ejemplo.com/fotos/cementerio-jaen.jpg"
         */
        @Size(max = 255, message = "La URL de la foto no puede exceder 255 caracteres")
        @Schema(description = "URL de la foto principal del cementerio (opcional)", example = "assets/images/cementerio.jpg", maxLength = 255)
        private String fotoUrl;

        /**
         * ID del usuario responsable del cementerio.
         *
         * ANOTACIONES:
         * - @Positive: Si existe, debe ser un número positivo
         * - Campo OPCIONAL
         *
         * NOTA: El mapper verificará que el usuario exista en BD
         * Si no existe, lanzará IllegalArgumentException
         *
         * USO: Asignar gestor del cementerio
         */
        @Positive(message = "El ID del responsable debe ser un número positivo")
        @Schema(description = "ID del usuario responsable (opcional)", example = "1", type = "integer")
        private Integer responsableId;

        /**
         * Estado de activación del cementerio.
         *
         * ANOTACIONES:
         * - @NotNull: Obligatorio
         * - @Schema: Documentación
         *
         * VALORES:
         * - true: Cementerio activo
         * - false: Cementerio inactivo
         */
        @NotNull(message = "El estado activo es obligatorio")
        @Schema(description = "Estado de activación (true=activo, false=inactivo)", example = "true", type = "boolean")
        private Boolean activo;

        // ========================================================================
        // CAMPOS PARA KONVA.JS - CONFIGURACIÓN DEL MAPA INTERACTIVO
        // ========================================================================

        /**
         * Ancho del canvas del mapa interactivo en píxeles.
         */
        @Positive(message = "El ancho del mapa debe ser un número positivo")
        @Schema(description = "Ancho del canvas del mapa en píxeles (opcional)", example = "1200", type = "integer")
        private Integer mapaAncho;

        /**
         * Alto del canvas del mapa interactivo en píxeles.
         */
        @Positive(message = "El alto del mapa debe ser un número positivo")
        @Schema(description = "Alto del canvas del mapa en píxeles (opcional)", example = "800", type = "integer")
        private Integer mapaAlto;

        /**
         * Escala del mapa interactivo.
         */
        @DecimalMin(value = "0.1", message = "La escala del mapa debe estar entre 0.1 y 10.0")
        @DecimalMax(value = "10.0", message = "La escala del mapa debe estar entre 0.1 y 10.0")
        @Schema(description = "Escala del mapa (opcional, 1.0 = sin escala)", example = "1.0", minimum = "0.1", maximum = "10.0")
        private BigDecimal mapaEscala;

        /**
         * URL de la imagen de fondo del plano del cementerio.
         */
        @Size(max = 500, message = "La URL de la imagen de fondo no puede exceder 500 caracteres")
        @Schema(description = "URL de la imagen de fondo del plano del cementerio (opcional)", example = "https://storage.example.com/plano.png", maxLength = 500)
        private String imagenFondo;
}
