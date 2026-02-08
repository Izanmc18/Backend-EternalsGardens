package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.Difunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para gestionar operaciones de persistencia con la entidad Difunto.
 *
 * Proporciona métodos para buscar difuntos por nombre, apellidos, DNI,
 * fecha de fallecimiento y otras operaciones necesarias para el registro
 * de personas fallecidas en el sistema.
 *
 * @author Izan Martinez Castro
 * @version 1.0
 */
@Repository
public interface DifuntoRepository extends JpaRepository<Difunto, Integer> {

        // =====================================================================
        // BÚSQUEDAS BÁSICAS
        // =====================================================================

        Optional<Difunto> findByDni(String dni);

        /**
         * Busca un difunto por su DNI sin importar mayúsculas o minúsculas.
         *
         * Útil para garantizar validaciones de duplicados de DNI
         * independientemente del formato utilizado. Los validators
         * la usarán para verificar documentos únicos.
         *
         * @param dni el DNI del difunto
         * @return Optional con el difunto si existe
         */
        @Query("SELECT d FROM Difunto d WHERE LOWER(d.dni) = LOWER(:dni)")
        Optional<Difunto> buscarPorDni(@Param("dni") String dni);

        // =====================================================================
        // BÚSQUEDAS POR NOMBRE
        // =====================================================================

        /**
         * Obtiene todos los difuntos cuyo nombre o apellidos coincidan
         * parcialmente con el texto buscado.
         *
         * Se utiliza en búsquedas de difuntos donde el usuario
         * solo recuerda parte del nombre.
         *
         * @param busqueda el texto a buscar en nombre o apellidos
         * @return lista de difuntos que coinciden con la búsqueda
         */
        @Query("SELECT d FROM Difunto d WHERE LOWER(d.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR LOWER(d.apellidos) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
        List<Difunto> buscarPorNombreOApellidos(@Param("busqueda") String busqueda);

        @Query("SELECT d FROM Difunto d WHERE " +
                        "LOWER(d.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "LOWER(d.apellidos) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "LOWER(d.dni) LIKE LOWER(CONCAT('%', :query, '%')) " +
                        "ORDER BY d.fechaDefuncion DESC")
        List<Difunto> buscarGlobal(@Param("query") String query);

        /**
         * Busca difuntos por nombre exacto sin importar mayúsculas.
         *
         * Útil para búsquedas precisas cuando se conoce el nombre exacto.
         *
         * @param nombre el nombre del difunto
         * @return lista de difuntos con ese nombre
         */
        @Query("SELECT d FROM Difunto d WHERE LOWER(d.nombre) = LOWER(:nombre) " +
                        "ORDER BY d.apellidos ASC")
        List<Difunto> buscarPorNombre(@Param("nombre") String nombre);

        /**
         * Busca difuntos por apellidos exactos sin importar mayúsculas.
         *
         * Útil para encontrar todos los difuntos con unos apellidos específicos.
         *
         * @param apellidos los apellidos del difunto
         * @return lista de difuntos con esos apellidos
         */
        @Query("SELECT d FROM Difunto d WHERE LOWER(d.apellidos) = LOWER(:apellidos) " +
                        "ORDER BY d.nombre ASC")
        List<Difunto> buscarPorApellidos(@Param("apellidos") String apellidos);

        // =====================================================================
        // BÚSQUEDAS POR FECHAS
        // =====================================================================

        /**
         * Obtiene difuntos fallecidos en un rango de fechas específico.
         *
         * Se utiliza para reportes sobre fallecimientos en períodos
         * determinados, análisis demográficos y estadísticas.
         *
         * @param inicio la fecha de inicio del rango
         * @param fin    la fecha de fin del rango
         * @return lista de difuntos fallecidos en el período
         */
        List<Difunto> findByFechaDefuncionBetween(LocalDate inicio, LocalDate fin);

        /**
         * Obtiene difuntos fallecidos recientemente.
         *
         * Se utiliza para ver fallecimientos en los últimos días,
         * útil para gestión reciente y notificaciones.
         *
         * @param desdeHace la fecha a partir de la cual buscar
         * @return lista de difuntos fallecidos desde esa fecha
         */
        @Query("SELECT d FROM Difunto d WHERE d.fechaDefuncion >= :desdeHace ORDER BY d.fechaDefuncion DESC")
        List<Difunto> obtenerDifuntosRecientes(@Param("desdeHace") LocalDate desdeHace);

        /**
         * Obtiene difuntos fallecidos en un año específico.
         *
         * Se utiliza para estadísticas anuales de fallecimientos.
         *
         * @param anio el año a consultar
         * @return lista de difuntos fallecidos en ese año
         */
        @Query("SELECT d FROM Difunto d WHERE YEAR(d.fechaDefuncion) = :anio ORDER BY d.fechaDefuncion DESC")
        List<Difunto> obtenerDifuntosPorAnio(@Param("anio") Integer anio);

        // =====================================================================
        // CONTEOS (Para usar en Validators y Helpers)
        // =====================================================================

        /**
         * Cuenta cuántos difuntos existen con un DNI específico.
         *
         * Los validators la usarán para validar duplicados de DNI
         * sin necesidad de capturar excepciones.
         *
         * @param dni el DNI a verificar
         * @return cantidad de difuntos con ese DNI
         */
        @Query("SELECT COUNT(d) FROM Difunto d WHERE LOWER(d.dni) = LOWER(:dni)")
        Long contarPorDni(@Param("dni") String dni);

        /**
         * Cuenta cuántos difuntos fallecieron en una fecha específica.
         *
         * Se utiliza para estadísticas diarias de fallecimientos.
         *
         * @param fecha la fecha a consultar
         * @return cantidad de difuntos fallecidos en esa fecha
         */
        @Query("SELECT COUNT(d) FROM Difunto d WHERE d.fechaDefuncion = :fecha")
        Long contarFallecidosEnFecha(@Param("fecha") LocalDate fecha);

        /**
         * Cuenta cuántos difuntos fallecieron en un año específico.
         *
         * Se utiliza para estadísticas anuales de fallecimientos.
         *
         * @param anio el año a consultar
         * @return cantidad de difuntos fallecidos en ese año
         */
        @Query("SELECT COUNT(d) FROM Difunto d WHERE YEAR(d.fechaDefuncion) = :anio")
        Long contarFallecidosPorAnio(@Param("anio") Integer anio);

        /**
         * Cuenta el total de difuntos registrados en el sistema.
         *
         * Método alternativo al count() que permite personalizar
         * la consulta para estadísticas específicas.
         *
         * @return cantidad total de difuntos
         */
        @Query("SELECT COUNT(d) FROM Difunto d")
        Long contarTotalDifuntos();

        /**
         * Cuenta cuántos difuntos fallecieron en un rango de fechas.
         *
         * Se utiliza para estadísticas sobre períodos específicos.
         *
         * @param inicio la fecha de inicio
         * @param fin    la fecha de fin
         * @return cantidad de difuntos en el rango
         */
        @Query("SELECT COUNT(d) FROM Difunto d WHERE " +
                        "d.fechaDefuncion >= :inicio AND " +
                        "d.fechaDefuncion <= :fin")
        Long contarPorRangoFechas(@Param("inicio") LocalDate inicio,
                        @Param("fin") LocalDate fin);

        // =====================================================================
        // BÚSQUEDAS POR UBICACIÓN (ZONA Y CEMENTERIO)
        // =====================================================================

        /**
         * Busca difuntos por zona específica con paginación.
         *
         * Se utiliza en la gestión de difuntos para filtrar por zona
         * y evitar cargar todos los registros de la base de datos.
         *
         * @param zonaId   el ID de la zona
         * @param pageable configuración de paginación
         * @return página de difuntos de la zona especificada
         */
        @Query("SELECT d FROM Difunto d WHERE d.parcela.zona.id = :zonaId")
        org.springframework.data.domain.Page<Difunto> findByZonaId(
                        @Param("zonaId") Integer zonaId,
                        org.springframework.data.domain.Pageable pageable);

        /**
         * Busca difuntos por cementerio (a través de zona) con paginación.
         *
         * Se utiliza para obtener todos los difuntos de un cementerio específico.
         *
         * @param cementerioId el ID del cementerio
         * @param pageable     configuración de paginación
         * @return página de difuntos del cementerio especificado
         */
        @Query("SELECT d FROM Difunto d WHERE d.parcela.zona.cementerio.id = :cementerioId")
        org.springframework.data.domain.Page<Difunto> findByCementerioId(
                        @Param("cementerioId") Integer cementerioId,
                        org.springframework.data.domain.Pageable pageable);

        // =====================================================================
        // ESTADÍSTICAS
        // =====================================================================

        /**
         * Obtiene la cantidad de difuntos registrados por mes en los últimos 12 meses.
         * Útil para gráficos de tendencias.
         * 
         * @return Lista de arrays de objetos [mes (String YYYY-MM), cantidad (Long)]
         */
        @Query(value = "SELECT DATE_FORMAT(d.fecha_defuncion, '%Y-%m') as mes, COUNT(*) as cantidad " +
                        "FROM difunto d " +
                        "WHERE d.fecha_defuncion >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) " +
                        "GROUP BY mes " +
                        "ORDER BY mes ASC", nativeQuery = true)
        List<Object[]> countDifuntosLast12Months();

        /**
         * Cuenta difuntos agrupados por sexo.
         * 
         * @return Lista de arrays [sexo (String), cantidad (Long)]
         */
        @Query("SELECT d.sexo, COUNT(d) FROM Difunto d GROUP BY d.sexo")
        List<Object[]> countDifuntosBySexo();

        // =====================================================================
        // USUARIO
        // =====================================================================
        List<Difunto> findByParcela_Concesion_Usuario_Id(Integer usuarioId);
}
