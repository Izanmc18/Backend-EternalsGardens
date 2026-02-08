package com.imc.EternalsGardens.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "parcela")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @Column(nullable = false)
    private Integer numeroFila;

    @Column(nullable = false)
    private Integer numeroColumna;

    @Column(nullable = false, unique = true, length = 50)
    private String numeroIdentificadorUnico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_zona_id", nullable = false)
    private TipoZona tipoZona;

    @Column(nullable = false, length = 20)
    private String estado = "LIBRE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concesion_id")
    private Concesion concesion;

    // Relación añadida para facilitar la carga de difuntos en el mapa
    @OneToMany(mappedBy = "parcela", fetch = FetchType.LAZY)
    @lombok.ToString.Exclude
    private java.util.List<DifuntosEnParcela> difuntosEnParcela;

    // Relación directa de respaldo (Fallback)
    @OneToMany(mappedBy = "parcela", fetch = FetchType.LAZY)
    @lombok.ToString.Exclude
    private java.util.List<Difunto> difuntosDirectos;

    // ========================================================================
    // CAMPOS PARA KONVA.JS - POSICIONES VISUALES EN EL MAPA
    // ========================================================================

    /**
     * Posición X visual de la parcela en el canvas (en píxeles).
     *
     * CALCULADO AUTOMÁTICAMENTE: Al crear la parcela basado en:
     * - Posición de la zona padre
     * - Número de columna
     * - Ancho de la zona / número de columnas
     *
     * USO: Renderizar la parcela en su posición correcta dentro de la zona en
     * Konva.js
     */
    @Column(name = "posicion_visual_x", precision = 10, scale = 2)
    private BigDecimal posicionVisualX;

    /**
     * Posición Y visual de la parcela en el canvas (en píxeles).
     *
     * CALCULADO AUTOMÁTICAMENTE: Al crear la parcela basado en:
     * - Posición de la zona padre
     * - Número de fila
     * - Alto de la zona / número de filas
     *
     * USO: Renderizar la parcela en su posición correcta dentro de la zona en
     * Konva.js
     */
    @Column(name = "posicion_visual_y", precision = 10, scale = 2)
    private BigDecimal posicionVisualY;

    /**
     * Ancho visual de la parcela en el canvas (en píxeles).
     *
     * CALCULADO AUTOMÁTICAMENTE: ancho de zona / número de columnas
     * VALOR POR DEFECTO: 30 píxeles
     *
     * USO: Propiedad 'width' del rectángulo de la parcela en Konva.js
     */
    @Column(name = "ancho_visual", precision = 10, scale = 2)
    private BigDecimal anchoVisual = BigDecimal.valueOf(30.0);

    /**
     * Alto visual de la parcela en el canvas (en píxeles).
     *
     * CALCULADO AUTOMÁTICAMENTE: alto de zona / número de filas
     * VALOR POR DEFECTO: 30 píxeles
     *
     * USO: Propiedad 'height' del rectángulo de la parcela en Konva.js
     */
    @Column(name = "alto_visual", precision = 10, scale = 2)
    private BigDecimal altoVisual = BigDecimal.valueOf(30.0);
}