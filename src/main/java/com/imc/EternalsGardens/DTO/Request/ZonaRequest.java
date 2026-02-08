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
@Schema(description = "Datos para crear o actualizar una zona en un cementerio")
public class ZonaRequest {

    @NotNull(message = "El ID del cementerio es obligatorio")
    @Schema(description = "ID del cementerio al que pertenece", example = "1")
    private Integer cementerioId;

    @NotBlank(message = "El nombre de la zona es obligatorio")
    @Size(max = 100)
    @Schema(description = "Nombre de la zona (ej: Zona Norte, Sector A)", example = "Sector San Pedro")
    private String nombre;

    @NotNull(message = "El tipo de zona es obligatorio")
    @Schema(description = "ID del tipo de zona (ej: Nicho, Parcela)", example = "1")
    private Integer tipoZonaId;

    @Min(value = 1, message = "Debe haber al menos 1 fila")
    @Schema(description = "Número de filas en la zona", example = "10")
    private Integer filas;

    @Min(value = 1, message = "Debe haber al menos 1 columna")
    @Schema(description = "Número de columnas en la zona", example = "20")
    private Integer columnas;

    @Min(value = 0)
    @Schema(description = "Capacidad total de enterramientos", example = "200")
    private Integer capacidadTotal;

    @Schema(description = "Coordenadas del polígono para dibujar en mapa", example = "[[-3.4, 40.1], [-3.5, 40.2]]")
    private String poligonoCoordinadas;

    // ========================================================================
    // CAMPOS PARA KONVA.JS - MAPAS INTERACTIVOS
    // ========================================================================

    @Schema(description = "Tipo de forma (RECTANGLE, CIRCLE, POLYGON, PATH)", example = "RECTANGLE")
    private String formaTipo;

    @Schema(description = "JSON completo de configuración de Konva.js", example = "{\"attrs\":{...}}")
    private String coordenadasJson;

    @Schema(description = "Posición X en el canvas", example = "100.50")
    private BigDecimal posicionX;

    @Schema(description = "Posición Y en el canvas", example = "200.50")
    private BigDecimal posicionY;

    @Schema(description = "Ancho de la zona (rectángulo)", example = "300")
    private BigDecimal ancho;

    @Schema(description = "Alto de la zona (rectángulo)", example = "150")
    private BigDecimal alto;

    @Schema(description = "Radio de la zona (círculo)", example = "50")
    private BigDecimal radio;

    @Schema(description = "Rotación en grados", example = "0")
    private BigDecimal rotacion;

    @Schema(description = "Color de relleno", example = "#4CAF50")
    private String colorRelleno;

    @Schema(description = "Color de borde", example = "#2E7D32")
    private String colorBorde;

    @Schema(description = "Opacidad (0-1)", example = "0.6")
    private BigDecimal opacidad;

    @Schema(description = "Si la zona está operativa", example = "true")
    private Boolean activa;
}