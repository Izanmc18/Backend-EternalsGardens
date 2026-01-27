package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.Usuario;
import com.imc.EternalsGardens.Entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository para gestionar operaciones de persistencia con la entidad Usuario.
 *
 * Proporciona métodos para buscar usuarios por email, DNI, nombre y otros criterios
 * necesarios para autenticación, gestión de cuentas y operaciones administrativas.
 *
 * @author Izan Martinez Castro
 * @version 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su email.
     *
     * Se utiliza principalmente en el proceso de login para verificar que
     * el usuario existe en el sistema.
     *
     * @param email el email del usuario a buscar
     * @return Optional con el usuario si existe, vacío en caso contrario
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca un usuario por su email de forma case-insensitive.
     *
     * Útil para garantizar que no haya duplicados independientemente
     * de mayúsculas o minúsculas en el email.
     *
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Usuario> buscarPorEmail(@Param("email") String email);

    /**
     * Busca un usuario por su DNI.
     *
     * Se usa para verificar duplicados y en validaciones de documento
     * durante registro de nuevos usuarios.
     *
     * @param dni el número de DNI del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByDni(String dni);

    /**
     * Obtiene todos los usuarios cuyo nombre o apellidos coincidan
     * parcialmente con el texto buscado.
     *
     * Se utiliza en búsquedas de usuarios en el panel administrativo
     * y para encontrar clientes de forma rápida.
     *
     * @param busqueda el texto a buscar en nombre o apellidos
     * @return lista de usuarios que cumplen el criterio
     */
    @Query("SELECT u FROM Usuario u WHERE " +
            "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Usuario> buscarPorNombreOApellidos(@Param("busqueda") String busqueda);

    /**
     * Obtiene todos los usuarios que tengan un rol específico.
     *
     * Útil para listar administradores, operadores de cementerio,
     * o usuarios normales según sea necesario.
     *
     * @param rol el rol del usuario
     * @return lista de usuarios con ese rol
     */
    List<Usuario> findByRol(Rol rol);

    /**
     * Obtiene todos los usuarios que tengan un rol específico por ID.
     *
     * Alternativa más eficiente que buscar por objeto Rol
     * cuando solo tenemos el ID disponible.
     *
     * @param rolId el ID del rol
     * @return lista de usuarios con ese rol
     */
    List<Usuario> findByRolId(Integer rolId);

    /**
     * Obtiene todos los usuarios que están activos en el sistema.
     *
     * Los usuarios inactivos no pueden acceder a su cuenta
     * ni realizar operaciones.
     *
     * @return lista de usuarios activos
     */
    @Query("SELECT u FROM Usuario u WHERE u.activo = true " +
            "ORDER BY u.nombre ASC")
    List<Usuario> obtenerUsuariosActivos();


    /**
     * Cuenta el total de usuarios activos en el sistema.
     *
     * Se utiliza para estadísticas y reportes administrativos.
     *
     * @return cantidad de usuarios activos
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    Long contarUsuariosActivos();

    /**
     * Cuenta cuántos usuarios tienen un rol específico.
     *
     * Útil para ver cuántos administradores o operadores hay
     * en el sistema.
     *
     * @param rolId el ID del rol
     * @return cantidad de usuarios con ese rol
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol.id = :rolId")
    Long contarUsuariosPorRol(@Param("rolId") Integer rolId);
}
