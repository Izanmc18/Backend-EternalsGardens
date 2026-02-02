package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.Parcela;
import com.imc.EternalsGardens.Entity.Cementerio;
import com.imc.EternalsGardens.Entity.TipoZona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Integer> {

        // Validar existencia por código único
        boolean existsByNumeroIdentificadorUnico(String numeroIdentificadorUnico);

        // Buscar parcelas de una zona concreta
        List<Parcela> findByZonaId(Integer zonaId);

        // Buscar por código único (Exacto)
        Optional<Parcela> findByNumeroIdentificadorUnico(String numeroIdentificadorUnico);

        // Buscar por código único (Ignorando mayúsculas/minúsculas)
        @Query("SELECT p FROM Parcela p WHERE LOWER(p.numeroIdentificadorUnico) = LOWER(:codigo)")
        Optional<Parcela> buscarPorCodigoUnico(@Param("codigo") String codigo);

        // Obtener parcelas de un cementerio (A través de Zona)
        List<Parcela> findByZona_Cementerio(Cementerio cementerio);

        // Obtener parcelas por ID de cementerio
        @Query("SELECT p FROM Parcela p WHERE p.zona.cementerio.id = :cementerioId")
        List<Parcela> findByCementerioId(@Param("cementerioId") Integer cementerioId);

        // Obtener parcelas LIBRES de un cementerio
        @Query("SELECT p FROM Parcela p WHERE p.zona.cementerio.id = :cementerioId " +
                        "AND p.estado = 'LIBRE' " +
                        "ORDER BY p.numeroIdentificadorUnico ASC")
        List<Parcela> obtenerParcelasDisponiblesPorCementerio(@Param("cementerioId") Integer cementerioId);

        // Obtener parcelas por Tipo de Zona
        List<Parcela> findByTipoZona(TipoZona tipoZona);

        // Obtener parcelas por ID de Tipo de Zona
        List<Parcela> findByTipoZona_Id(Integer tipoZonaId);

        // Obtener parcelas LIBRES por Tipo de Zona
        @Query("SELECT p FROM Parcela p WHERE p.tipoZona.id = :tipoZonaId " +
                        "AND p.estado = 'LIBRE' " +
                        "ORDER BY p.numeroIdentificadorUnico ASC")
        List<Parcela> obtenerParcelasDisponiblesPorTipoZona(@Param("tipoZonaId") Integer tipoZonaId);

        // Obtener TODAS las parcelas LIBRES
        @Query("SELECT p FROM Parcela p WHERE p.estado = 'LIBRE' " +
                        "ORDER BY p.numeroIdentificadorUnico ASC")
        List<Parcela> obtenerParcelasDisponibles();

        // Obtener parcelas OCUPADAS
        @Query("SELECT p FROM Parcela p WHERE p.estado <> 'LIBRE' " +
                        "ORDER BY p.numeroIdentificadorUnico ASC")
        List<Parcela> obtenerParcelasNoDisponibles();

        // =====================================================================
        // BÚSQUEDAS COMBINADAS
        // =====================================================================

        // Buscar parcela específica en un cementerio y tipo de zona
        @Query("SELECT p FROM Parcela p WHERE LOWER(p.numeroIdentificadorUnico) = LOWER(:codigo) " +
                        "AND p.zona.cementerio.id = :cementerioId " +
                        "AND p.tipoZona.id = :tipoZonaId")
        Optional<Parcela> buscarPorCodigoCementerioYTipoZona(@Param("codigo") String codigo,
                        @Param("cementerioId") Integer cementerioId,
                        @Param("tipoZonaId") Integer tipoZonaId);

        // Buscar parcelas LIBRES en un cementerio y tipo de zona
        @Query("SELECT p FROM Parcela p WHERE p.zona.cementerio.id = :cementerioId " +
                        "AND p.tipoZona.id = :tipoZonaId " +
                        "AND p.estado = 'LIBRE' " +
                        "ORDER BY p.numeroIdentificadorUnico ASC")
        List<Parcela> obtenerParcelasDisponiblesPorCementerioYTipoZona(@Param("cementerioId") Integer cementerioId,
                        @Param("tipoZonaId") Integer tipoZonaId);

        // Contar parcelas con un código específico
        @Query("SELECT COUNT(p) FROM Parcela p WHERE LOWER(p.numeroIdentificadorUnico) = LOWER(:codigo)")
        Long contarPorCodigoUnico(@Param("codigo") String codigo);

        // Contar parcelas LIBRES en un cementerio
        @Query("SELECT COUNT(p) FROM Parcela p WHERE p.zona.cementerio.id = :cementerioId " +
                        "AND p.estado = 'LIBRE'")
        Long contarDisponiblesPorCementerio(@Param("cementerioId") Integer cementerioId);

        // Contar total parcelas en un cementerio
        @Query("SELECT COUNT(p) FROM Parcela p WHERE p.zona.cementerio.id = :cementerioId")
        Long contarPorCementerio(@Param("cementerioId") Integer cementerioId);

        // Contar total parcelas por tipo de zona
        @Query("SELECT COUNT(p) FROM Parcela p WHERE p.tipoZona.id = :tipoZonaId")
        Long contarPorTipoZona(@Param("tipoZonaId") Integer tipoZonaId);

        // Contar parcelas LIBRES por tipo de zona
        @Query("SELECT COUNT(p) FROM Parcela p WHERE p.tipoZona.id = :tipoZonaId " +
                        "AND p.estado = 'LIBRE'")
        Long contarDisponiblesPorTipoZona(@Param("tipoZonaId") Integer tipoZonaId);

        // Contar total de parcelas LIBRES en todo el sistema
        @Query("SELECT COUNT(p) FROM Parcela p WHERE p.estado = 'LIBRE'")
        Long contarParcelasDisponibles();

        // =====================================================================
        // ESTADÍSTICAS
        // =====================================================================

        /**
         * Obtiene el total de parcelas y las parcelas ocupadas agrupadas por
         * cementerio.
         * Útil para calcular porcentajes de ocupación por cementerio.
         * 
         * @return Lista de arrays [NombreCementerio, TotalParcelas, ParcelasOcupadas]
         */
        @Query("SELECT p.zona.cementerio.nombre, COUNT(p), " +
                        "SUM(CASE WHEN p.estado <> 'LIBRE' THEN 1 ELSE 0 END) " +
                        "FROM Parcela p " +
                        "GROUP BY p.zona.cementerio.nombre")
        List<Object[]> countTotalAndOccupiedByCementerio();

        // =====================================================================
        // USUARIO
        // =====================================================================
        List<Parcela> findByConcesion_Usuario_Id(Integer usuarioId);
}