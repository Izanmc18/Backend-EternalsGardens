package com.imc.EternalsGardens.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "zona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cementerio_id", nullable = false)
    private Cementerio cementerio;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_zona_id", nullable = false)
    private TipoZona tipoZona;

    @Column(nullable = false)
    private Integer filas;

    @Column(nullable = false)
    private Integer columnas;

    @Column(nullable = false)
    private Integer capacidadTotal;

    // ========================================================================
    // CAMPOS PARA KONVA.JS - MAPAS INTERACTIVOS
    // ========================================================================

    /**
     * Tipo de forma geométrica de la zona en el mapa.
     *
     * VALORES PERMITIDOS:
     * - 'RECTANGLE': Zona rectangular
     * - 'CIRCLE': Zona circular
     * - 'POLYGON': Zona con forma irregular (polígono)
     * - 'PATH': Zona con forma personalizada (path SVG)
     *
     * VALOR POR DEFECTO: 'RECTANGLE'
     * USO: Determinar qué tipo de shape de Konva.js crear
     */
    @Column(name = "forma_tipo", length = 20)
    private String formaTipo = "RECTANGLE";

    /**
     * JSON completo con todas las propiedades de Konva.js.
     *
     * FORMATO: JSON con estructura {type, attrs, className, metadata}
     * EJEMPLO:
     * {
     * "type": "Rect",
     * "attrs": {"x": 100, "y": 50, "width": 300, "height": 200, ...},
     * "className": "Rect",
     * "metadata": {"zonaId": 1, "nombre": "Zona A"}
     * }
     *
     * USO: Almacenar configuración completa para reconstruir la forma en Konva.js
     */
    @Column(name = "coordenadas_json", columnDefinition = "TEXT")
    private String coordenadasJson;

    /**
     * Posición X de la zona en el canvas (en píxeles).
     *
     * RANGO: 0 - mapaAncho del cementerio
     * USO: Coordenada X del punto de origen de la forma en Konva.js
     */
    @Column(name = "posicion_x", precision = 10, scale = 2)
    private BigDecimal posicionX;

    /**
     * Posición Y de la zona en el canvas (en píxeles).
     *
     * RANGO: 0 - mapaAlto del cementerio
     * USO: Coordenada Y del punto de origen de la forma en Konva.js
     */
    @Column(name = "posicion_y", precision = 10, scale = 2)
    private BigDecimal posicionY;

    /**
     * Ancho de la zona (solo para rectángulos).
     *
     * USO: Propiedad 'width' de Konva.Rect
     * NULL: Si la zona no es rectangular
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal ancho;

    /**
     * Alto de la zona (solo para rectángulos).
     *
     * USO: Propiedad 'height' de Konva.Rect
     * NULL: Si la zona no es rectangular
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal alto;

    /**
     * Radio de la zona (solo para círculos).
     *
     * USO: Propiedad 'radius' de Konva.Circle
     * NULL: Si la zona no es circular
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal radio;

    /**
     * Rotación de la zona en grados.
     *
     * RANGO: 0 - 360
     * VALOR POR DEFECTO: 0 (sin rotación)
     * USO: Propiedad 'rotation' de Konva.js
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal rotacion = BigDecimal.ZERO;

    /**
     * Color de relleno de la zona en formato hexadecimal o rgba.
     *
     * FORMATO: '#RRGGBB' o 'rgba(r, g, b, a)'
     * VALOR POR DEFECTO: '#4CAF50' (verde)
     * EJEMPLO: '#4CAF50', 'rgba(76, 175, 80, 0.6)'
     * USO: Propiedad 'fill' de Konva.js
     */
    @Column(name = "color_relleno", length = 30)
    private String colorRelleno = "#4CAF50";

    /**
     * Color del borde de la zona en formato hexadecimal.
     *
     * FORMATO: '#RRGGBB'
     * VALOR POR DEFECTO: '#2E7D32' (verde oscuro)
     * USO: Propiedad 'stroke' de Konva.js
     */
    @Column(name = "color_borde", length = 20)
    private String colorBorde = "#2E7D32";

    /**
     * Opacidad de la zona.
     *
     * RANGO: 0.0 (transparente) - 1.0 (opaco)
     * VALOR POR DEFECTO: 0.6
     * USO: Propiedad 'opacity' de Konva.js
     */
    @Column(precision = 3, scale = 2)
    private BigDecimal opacidad = BigDecimal.valueOf(0.6);

    // ========================================================================
    // CAMPOS LEGACY (Mantener por compatibilidad)
    // ========================================================================

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activa = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
