package com.imc.EternalsGardens.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Rol - Representa los roles de usuario en el sistema.
 *
 * ROLES DISPONIBLES:
 * <ul>
 *   <li>ADMINISTRADOR - Acceso total al sistema</li>
 *   <li>OPERADOR_CEMENTERIO - Gestión del cementerio</li>
 *   <li>USUARIO - Cliente final</li>
 * </ul>
 *
 * PROPÓSITO:
 * Definir permisos y autorización de usuarios en la aplicación.
 * Cada usuario tiene exactamente un rol que determina qué puede hacer.
 *
 * @author Izan Martinez Castro
 */
@Entity
@Table(name = "rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    /**
     * Identificador único del rol.
     *
     * ANOTACIONES:
     * - @Id: Clave primaria
     * - @GeneratedValue: Auto-incrementa
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre único del rol (ADMINISTRADOR, OPERADOR_CEMENTERIO, USUARIO).
     *
     * ANOTACIONES:
     * - @Column(nullable = false): Obligatorio
     * - unique = true: No puede haber dos roles con el mismo nombre
     * - length = 50: Máximo 50 caracteres
     *
     * VALORES ESTÁNDAR:
     * - ADMINISTRADOR
     * - OPERADOR_CEMENTERIO
     * - USUARIO
     */
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    /**
     * Descripción detallada del rol.
     *
     * ANOTACIONES:
     * - @Column: Campo opcional
     * - columnDefinition = "TEXT": Puede tener texto largo
     *
     * EJEMPLOS:
     * - "Acceso total a todas las funcionalidades del sistema"
     * - "Gestiona cementerios, parcelas, nichos y concesiones"
     * - "Acceso a sus propias concesiones y datos"
     */
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Estado de activación del rol.
     *
     * ANOTACIONES:
     * - @Column(nullable = false): Obligatorio
     * - columnDefinition = "BOOLEAN DEFAULT TRUE": Valor por defecto true
     *
     * VALORES:
     * - true: Rol activo, se puede asignar a usuarios
     * - false: Rol desactivado, no se puede asignar a nuevos usuarios
     *
     * USO: Desactivar roles sin eliminarlos (auditoría)
     */
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;


}
