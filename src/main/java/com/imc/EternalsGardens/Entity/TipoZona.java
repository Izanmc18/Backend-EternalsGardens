package com.imc.EternalsGardens.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

/**
 * Entidad TipoZona - Representa los tipos de zonas en un cementerio.
 *
 * TIPOS DISPONIBLES:
 * <ul>
 *   <li>NICHO - Espacios verticales en paredes</li>
 *   <li>PARCELA - Terrenos en el suelo</li>
 *   <li>COLUMBARIO - Espacios para urnas</li>
 *   <li>OSARIO - Lugares comunes</li>
 * </ul>
 *
 * PROPÓSITO:
 * Clasificar las diferentes formas de almacenar difuntos en un cementerio.
 * Cada zona pertenece a un tipo de zona específico.
 *
 * @author Izan Martinez Castro
 */
@Entity
@Table(name = "tipo_zona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoZona {

    /**
     * Identificador único del tipo de zona.
     *
     * ANOTACIONES:
     * - @Id: Clave primaria
     * - @GeneratedValue: Auto-incrementa
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre único del tipo de zona.
     *
     * ANOTACIONES:
     * - @Column(nullable = false): Obligatorio
     * - unique = true: No puede haber dos tipos iguales
     * - length = 50: Máximo 50 caracteres
     *
     * EJEMPLOS:
     * - "NICHO"
     * - "PARCELA"
     * - "COLUMBARIO"
     * - "OSARIO"
     */
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    /**
     * Descripción detallada del tipo de zona.
     *
     * ANOTACIONES:
     * - @Column: Campo opcional
     * - columnDefinition = "TEXT": Puede tener texto largo
     *
     * EJEMPLOS:
     * - "Espacios verticales en paredes para ataúdes"
     * - "Terrenos en el suelo para enterramientos"
     * - "Compartimentos para urnas de cenizas"
     */
    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
