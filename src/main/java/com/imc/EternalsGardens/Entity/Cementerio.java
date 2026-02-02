package com.imc.EternalsGardens.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Cementerio - Representa un cementerio en el sistema.
 *
 * PROPÓSITO:
 * Almacenar información de los cementerios que se gestionan en la aplicación.
 * Cada cementerio tiene zonas, parcelas y múltiples usuarios responsables.
 *
 * RELACIONES:
 * <ul>
 * <li>responsable: Usuario que gestiona el cementerio</li>
 * <li>zonas: Divisiones territoriales del cementerio</li>
 * </ul>
 *
 * @author Izan Martinez Castro
 */
@Entity
@Table(name = "cementerio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cementerio {

    /**
     * Identificador único del cementerio.
     *
     * ANOTACIONES:
     * - @Id: Clave primaria
     * - @GeneratedValue: Auto-incrementa
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del cementerio.
     *
     * ANOTACIONES:
     * - @Column(nullable = false): Obligatorio
     * - length = 150: Máximo 150 caracteres
     *
     * EJEMPLOS:
     * - "Cementerio de Jaén"
     * - "Necrópolis Municipal"
     */
    @Column(nullable = false, length = 150)
    private String nombre;

    /**
     * Municipio donde se encuentra el cementerio.
     *
     * ANOTACIONES:
     * - @Column(nullable = false): Obligatorio
     * - length = 100: Máximo 100 caracteres
     *
     * EJEMPLO: "Jaén", "Madrid", "Barcelona"
     */
    @Column(nullable = false, length = 100)
    private String municipio;

    /**
     * Provincia donde se encuentra el cementerio.
     *
     * ANOTACIONES:
     * - @Column(nullable = false): Obligatorio
     * - length = 100: Máximo 100 caracteres
     *
     * EJEMPLO: "Jaén", "Madrid", "Barcelona"
     */
    @Column(nullable = false, length = 100)
    private String provincia;

    /**
     * Código postal del cementerio.
     *
     * ANOTACIONES:
     * - @Column: Campo opcional
     * - length = 10: Máximo 10 caracteres
     *
     * EJEMPLO: "23001"
     */
    @Column(length = 10)
    private String codigoPostal;

    /**
     * Coordenada X (Longitud) del cementerio para geolocalización.
     *
     * ANOTACIONES:
     * - @Column: Campo opcional
     * - precision = 10: 10 dígitos totales
     * - scale = 8: 8 decimales
     *
     * FORMATO: Decimal de -180 a 180 (WGS84)
     * EJEMPLO: -3.78945678 (Madrid)
     */
    @Column(name = "coordenada_x", precision = 10, scale = 8)
    private BigDecimal coordenadaX;

    /**
     * Coordenada Y (Latitud) del cementerio para geolocalización.
     *
     * ANOTACIONES:
     * - @Column: Campo opcional
     * - precision = 11: 11 dígitos totales
     * - scale = 8: 8 decimales
     *
     * FORMATO: Decimal de -90 a 90 (WGS84)
     * EJEMPLO: 40.41678902 (Madrid)
     */
    @Column(name = "coordenada_y", precision = 11, scale = 8)
    private BigDecimal coordenadaY;

    /**
     * Teléfono de contacto del cementerio.
     *
     * ANOTACIONES:
     * - @Column: Campo opcional
     * - length = 20: Máximo 20 caracteres
     *
     * FORMATO: Puede incluir espacios, guiones, códigos país
     * EJEMPLO: "+34 953 123456"
     */
    @Column(length = 20)
    private String telefono;

    /**
     * Email de contacto del cementerio.
     *
     * ANOTACIONES:
     * - @Column: Campo opcional
     * - length = 100: Máximo 100 caracteres
     *
     * EJEMPLO: "contacto@cementerio-jaen.es"
     */
    @Column(length = 100)
    private String email;

    /**
     * Usuario responsable del cementerio.
     *
     * ANOTACIONES:
     * - @ManyToOne: Muchos cementerios pueden tener el mismo responsable
     * - @JoinColumn(name = "responsable_id"): Clave foránea a tabla usuario
     * - fetch = FetchType.LAZY: Carga bajo demanda
     *
     * RELACIÓN: Un Usuario puede ser responsable de múltiples cementerios
     * PUEDE SER NULL: Un cementerio podría no tener responsable asignado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;

    /**
     * Estado de activación del cementerio.
     *
     * ANOTACIONES:
     * - @Column(nullable = false): Obligatorio
     * - columnDefinition = "BOOLEAN DEFAULT TRUE": Valor por defecto true
     *
     * VALORES:
     * - true: Cementerio activo, en funcionamiento
     * - false: Cementerio inactivo, cerrado o en mantenimiento
     *
     * USO: Desactivar cementerios sin eliminarlos
     */
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;

    /**
     * URL de la foto principal del cementerio.
     *
     * ANOTACIONES:
     * - @Column(name = "foto_url"): Nombre de columna en la base de datos
     * - columnDefinition = "TEXT": Tipo de dato TEXT para URLs largas
     *
     * USO: Almacenar la URL de una imagen representativa del cementerio.
     */
    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;

    /**
     * Fecha y hora de creación del cementerio.
     *
     * ANOTACIONES:
     * - @Column(nullable = false): Obligatorio
     * - updatable = false: No se puede cambiar después de creado
     *
     * USO: Auditoría - registrar cuándo se dio de alta el cementerio
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}
