package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository para gestionar operaciones de persistencia con la entidad Rol.
 *
 * Proporciona métodos para buscar roles por nombre, descripción
 * y otras operaciones necesarias para la gestión de permisos
 * y autorización en el sistema.
 *
 * @author Izan Martinez Castro
 * @version 1.0
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    Optional<Rol> findByNombre(String nombre);
    /**
     * Busca un rol por su nombre exacto sin importar mayúsculas.
     *
     * Se utiliza para validar que no existan roles duplicados
     * y para obtener un rol por su nombre único.
     *
     * @param nombre el nombre del rol
     * @return Optional con el rol si existe
     */
    @Query("SELECT r FROM Rol r WHERE LOWER(r.nombre) = LOWER(:nombre)")
    Optional<Rol> buscarPorNombre(@Param("nombre") String nombre);

    /**
     * Obtiene todos los roles ordenados por nombre.
     *
     * Se utiliza para:
     * - Llenar dropdowns de selección de roles
     * - Listar todos los roles disponibles
     * - Mostrar en formularios de asignación
     *
     * @return lista de roles ordenados alfabéticamente
     */
    @Query("SELECT r FROM Rol r ORDER BY r.nombre ASC")
    List<Rol> obtenerTodosOrdenados();

    /**
     * Obtiene todos los roles que están activos en el sistema.
     *
     * Se utiliza para:
     * - No asignar roles desactivados a nuevos usuarios
     * - Mostrar solo roles disponibles en dropdowns
     * - Validaciones de roles válidos
     *
     * @return lista de roles activos
     */
    @Query("SELECT r FROM Rol r WHERE r.activo = true ORDER BY r.nombre ASC")
    List<Rol> obtenerRolesActivos();

    /**
     * Obtiene todos los roles que están inactivos en el sistema.
     *
     * Se utiliza para:
     * - Auditoría: ver qué roles se han desactivado
     * - Administración de roles históricos
     * - Reportes de cambios
     *
     * @return lista de roles inactivos
     */
    @Query("SELECT r FROM Rol r WHERE r.activo = false ORDER BY r.nombre ASC")
    List<Rol> obtenerRolesInactivos();


    /**
     * Busca roles cuya descripción coincida parcialmente con el texto.
     *
     * Se utiliza para:
     * - Búsquedas de roles por palabras clave en descripción
     * - Administración de roles: encontrar roles por característica
     *
     * @param busqueda el texto a buscar en la descripción
     * @return lista de roles que contienen el texto
     */
    @Query("SELECT r FROM Rol r WHERE " +
            "LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
            "ORDER BY r.nombre ASC")
    List<Rol> buscarPorDescripcion(@Param("busqueda") String busqueda);

    /**
     * Busca roles cuyo nombre coincida parcialmente con el texto.
     *
     * Se utiliza para:
     * - Autocompletado en formularios
     * - Búsquedas fuzzy de roles
     *
     * @param busqueda el texto a buscar en el nombre
     * @return lista de roles que contienen el texto
     */
    @Query("SELECT r FROM Rol r WHERE " +
            "LOWER(r.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
            "ORDER BY r.nombre ASC")
    List<Rol> buscarPorNombreParcial(@Param("busqueda") String busqueda);


    /**
     * Cuenta cuántos roles existen con un nombre específico.
     *
     * Utilidad:
     * - Validators verifica duplicados sin excepciones
     * - Devuelve Long (0 si no existe, >0 si existe)
     *
     * @param nombre el nombre del rol a contar
     * @return cantidad de roles con ese nombre
     */
    @Query("SELECT COUNT(r) FROM Rol r WHERE LOWER(r.nombre) = LOWER(:nombre)")
    Long contarPorNombre(@Param("nombre") String nombre);

    /**
     * Cuenta el total de roles activos en el sistema.
     *
     * Utilidad:
     * - Estadísticas de roles disponibles
     * - Dashboard: cantidad de roles activos
     *
     * @return cantidad de roles activos
     */
    @Query("SELECT COUNT(r) FROM Rol r WHERE r.activo = true")
    Long contarRolesActivos();

    /**
     * Cuenta el total de roles inactivos en el sistema.
     *
     * Utilidad:
     * - Estadísticas de roles desactivados
     * - Dashboard: cantidad de roles históricos
     *
     * @return cantidad de roles inactivos
     */
    @Query("SELECT COUNT(r) FROM Rol r WHERE r.activo = false")
    Long contarRolesInactivos();

    /**
     * Cuenta el total de roles registrados en el sistema.
     *
     * Utilidad:
     * - Estadísticas generales
     * - Dashboard: cantidad total de roles
     *
     * @return cantidad total de roles
     */
    @Query("SELECT COUNT(r) FROM Rol r")
    Long contarTotalRoles();

    /**
     * Cuenta cuántos usuarios tienen un rol específico.
     *
     * Utilidad:
     * - Antes de eliminar un rol, verificar si tiene usuarios
     * - Validar: no permitir eliminar si hay usuarios asignados
     * - Estadísticas: rol más usado
     *
     * @param rolId el ID del rol a verificar
     * @return cantidad de usuarios con ese rol
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol.id = :rolId")
    Long contarUsuariosConRol(@Param("rolId") Integer rolId);


    /**
     * Obtiene el rol de ADMINISTRADOR del sistema.
     *
     * Se utiliza para:
     * - Verificar si un usuario es admin (u.getRol().getId() == admin.getId())
     * - Asignar automáticamente rol admin al primer usuario
     * - Validaciones de autorización
     *
     * @return el rol ADMINISTRADOR si existe
     */
    @Query("SELECT r FROM Rol r WHERE LOWER(r.nombre) = 'administrador'")
    Optional<Rol> obtenerRolAdmin();

    /**
     * Obtiene el rol de USUARIO (cliente) del sistema.
     *
     * Se utiliza para:
     * - Asignar por defecto a nuevos usuarios
     * - Rol más permisivo para clientes
     *
     * @return el rol USUARIO si existe
     */
    @Query("SELECT r FROM Rol r WHERE LOWER(r.nombre) = 'usuario'")
    Optional<Rol> obtenerRolUsuario();

    /**
     * Obtiene el rol de OPERADOR_CEMENTERIO del sistema.
     *
     * Se utiliza para:
     * - Asignar a empleados del cementerio
     * - Permisos para gestionar el cementerio
     *
     * @return el rol OPERADOR_CEMENTERIO si existe
     */
    @Query("SELECT r FROM Rol r WHERE LOWER(r.nombre) = 'operador_cementerio'")
    Optional<Rol> obtenerRolOperador();


    /**
     * Verifica si existe un rol con un ID específico.
     *
     * Utilidad:
     * - Validators: verificar que el rol existe antes de asignar
     * - Evita excepciones en mappers
     *
     * @param id el ID del rol
     * @return true si existe, false si no existe
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rol r WHERE r.id = :id")
    Boolean existeRolConId(@Param("id") Integer id);

    /**
     * Verifica si un rol tiene usuarios asignados.
     *
     * Utilidad:
     * - Antes de desactivar/eliminar un rol
     * - Validar: no eliminar si tiene usuarios
     *
     * @param rolId el ID del rol
     * @return true si tiene usuarios, false si está vacío
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.rol.id = :rolId")
    Boolean tieneUsuariosAsignados(@Param("rolId") Integer rolId);
}
