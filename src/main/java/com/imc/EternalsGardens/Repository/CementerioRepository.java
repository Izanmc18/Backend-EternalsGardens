package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.Cementerio;
import com.imc.EternalsGardens.Entity.OperadorCementerio;
import com.imc.EternalsGardens.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository para gestionar operaciones de persistencia con la entidad
 * Cementerio.
 *
 * Proporciona métodos para buscar cementerios por nombre, localización,
 * estado activo/inactivo y por su operador responsable.
 *
 * @author Izan Martinez Castro
 * @version 1.0
 */
@Repository
public interface CementerioRepository extends JpaRepository<Cementerio, Integer> {

        /**
         * Busca un cementerio por su nombre exacto.
         *
         * Se utiliza para búsquedas precisas donde el usuario
         * conoce el nombre exacto del cementerio.
         *
         * @param nombre el nombre del cementerio
         * @return Optional con el cementerio si existe
         */
        Optional<Cementerio> findByNombre(String nombre);

        /**
         * Busca un cementerio por nombre sin importar mayúsculas o minúsculas.
         *
         * Útil para garantizar validaciones de duplicados
         * independientemente del formato de texto utilizado.
         * Los validators la usarán para verificar nombres únicos.
         *
         * @param nombre el nombre del cementerio
         * @return Optional con el cementerio si existe
         */
        @Query("SELECT c FROM Cementerio c WHERE LOWER(c.nombre) = LOWER(:nombre)")
        Optional<Cementerio> buscarPorNombre(@Param("nombre") String nombre);

        /**
         * Obtiene todos los cementerios que están activos.
         *
         * Un cementerio inactivo no aparece en listados públicos
         * ni permite nuevas operaciones de compra o gestión.
         *
         * @return lista de cementerios activos ordenados alfabéticamente
         */
        @Query("SELECT c FROM Cementerio c WHERE c.activo = true " +
                        "ORDER BY c.nombre ASC")
        List<Cementerio> obtenerCementeriosActivos();

        /**
         * Obtiene todos los cementerios sin importar su estado.
         *
         * Se utiliza en el panel administrativo donde es necesario
         * ver y gestionar todos los cementerios, incluso los inactivos.
         *
         * @return lista de todos los cementerios ordenados alfabéticamente
         */
        @Query("SELECT c FROM Cementerio c ORDER BY c.nombre ASC")
        List<Cementerio> obtenerTodosCementerios();

        List<Cementerio> findByResponsable(Usuario responsable);

        // Si necesitas buscar por ID del responsable directamente:
        List<Cementerio> findByResponsableId(Integer responsableId);

        // Buscar por ID del responsable
        // CORREGIDO: Cambiamos 'c.operador.id' por 'c.responsable.id'
        @Query("SELECT c FROM Cementerio c WHERE c.responsable.id = :responsableId AND c.activo = true ORDER BY c.nombre ASC")
        List<Cementerio> obtenerCementeriosActivosPorResponsable(@Param("responsableId") Integer responsableId);

        // Método alternativo sin @Query (Spring lo hace solo)
        List<Cementerio> findByResponsableIdAndActivoTrueOrderByNombreAsc(Integer responsableId);

        /**
         * Busca cementerios cuya localización contiene el texto proporcionado.
         *
         * Se utiliza en búsquedas geográficas donde el usuario
         * quiere encontrar cementerios en una ciudad o provincia específica.
         *
         *
         * @return lista de cementerios que coinciden con la búsqueda
         */
        @Query("SELECT c FROM Cementerio c WHERE " +
                        "(LOWER(c.municipio) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
                        " LOWER(c.provincia) LIKE LOWER(CONCAT('%', :busqueda, '%'))) " +
                        "AND c.activo = true " +
                        "ORDER BY c.nombre ASC")
        List<Cementerio> buscarPorLocalizacion(@Param("busqueda") String busqueda);

        /**
         * Busca cementerios por nombre o localización combinados.
         *
         * Feature de búsqueda principal donde el usuario puede
         * escribir nombre o ubicación y encontrar rápidamente el cementerio.
         *
         * @param busqueda el texto a buscar en nombre o localización
         * @return lista de cementerios que coinciden
         */
        @Query("SELECT c FROM Cementerio c WHERE " +
                        "(LOWER(c.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
                        " LOWER(c.municipio) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
                        " LOWER(c.provincia) LIKE LOWER(CONCAT('%', :busqueda, '%'))) " +
                        "AND c.activo = true " +
                        "ORDER BY c.nombre ASC")
        List<Cementerio> buscarPorNombreOLocalizacion(@Param("busqueda") String busqueda);

        /**
         * Cuenta cuántos cementerios existen con un nombre específico.
         *
         * Los validators la usarán para validar duplicados de nombre
         * sin necesidad de capturar excepciones.
         *
         * @param nombre el nombre a verificar
         * @return cantidad de cementerios con ese nombre
         */
        @Query("SELECT COUNT(c) FROM Cementerio c WHERE LOWER(c.nombre) = LOWER(:nombre)")
        Long contarPorNombre(@Param("nombre") String nombre);

        /**
         * Cuenta el total de cementerios activos en el sistema.
         *
         * Se utiliza para estadísticas y para saber cuántos
         * cementerios operativos hay disponibles.
         *
         * @return cantidad de cementerios activos
         */
        @Query("SELECT COUNT(c) FROM Cementerio c WHERE c.activo = true")
        Long contarCementeriosActivos();

        /**
         * Cuenta cuántos cementerios gestiona un operador específico.
         *
         * Útil para saber la carga de trabajo de cada operador
         * y en reportes administrativos.
         *
         *
         * @return cantidad de cementerios del operador
         */
        @Query("SELECT COUNT(c) FROM Cementerio c WHERE c.responsable.id = :responsableId")
        Long contarCementeriosPorResponsable(@Param("responsableId") Integer responsableId);

        /**
         * Busca cementerios por provincia con soporte de paginación.
         *
         * Se utiliza en el panel administrativo para filtrar cementerios
         * por provincia específica.
         *
         * @param provincia la provincia a filtrar
         * @param pageable  configuración de paginación y ordenación
         * @return página de cementerios de la provincia especificada
         */
        org.springframework.data.domain.Page<Cementerio> findByProvincia(
                        String provincia,
                        org.springframework.data.domain.Pageable pageable);
}
