package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.DifuntosEnParcela;
import com.imc.EternalsGardens.Entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DifuntosEnParcelaRepository extends JpaRepository<DifuntosEnParcela, Integer> {

    List<DifuntosEnParcela> findByParcela(Parcela parcela);

    List<DifuntosEnParcela> findByParcelaId(Integer parcelaId);

    List<DifuntosEnParcela> findByConcesionId(Integer concesionId);

    // Búsqueda por rango de fechas
    List<DifuntosEnParcela> findByFechaEnterramientoBetween(LocalDate inicio, LocalDate fin);

    // --- MÉTODOS POR PARCELA ---

    @Query("SELECT d FROM DifuntosEnParcela d WHERE d.parcela.id = :parcelaId AND d.estado = 'ENTERRADO' ORDER BY d.fechaEnterramiento DESC")
    List<DifuntosEnParcela> obtenerDifuntosActivosPorParcela(@Param("parcelaId") Integer parcelaId);

    @Query("SELECT d FROM DifuntosEnParcela d WHERE d.parcela.id = :parcelaId AND d.estado = 'EXHUMADO' ORDER BY d.fechaExhumacion DESC")
    List<DifuntosEnParcela> obtenerDifuntosExhumadosPorParcela(@Param("parcelaId") Integer parcelaId);

    @Query("SELECT d FROM DifuntosEnParcela d WHERE d.parcela.id = :parcelaId ORDER BY d.fechaEnterramiento DESC LIMIT 1")
    Optional<DifuntosEnParcela> obtenerDifuntoMasReciente(@Param("parcelaId") Integer parcelaId);

    // --- MÉTODOS GLOBALES ---

    // ESTE ES EL MÉTODO QUE DABA EL ERROR EN EL LOG
    // Corregido: d.estado = 'ENTERRADO' (antes d.exhumado = false)
    @Query("SELECT d FROM DifuntosEnParcela d WHERE d.fechaEnterramiento >= :desdeHace AND d.estado = 'ENTERRADO' ORDER BY d.fechaEnterramiento DESC")
    List<DifuntosEnParcela> obtenerDifuntosInhumadosRecientes(@Param("desdeHace") LocalDate desdeHace);

    @Query("SELECT d FROM DifuntosEnParcela d WHERE d.estado = 'ENTERRADO' ORDER BY d.fechaEnterramiento DESC")
    List<DifuntosEnParcela> obtenerDifuntosActivos();

    @Query("SELECT d FROM DifuntosEnParcela d WHERE d.estado = 'EXHUMADO' ORDER BY d.fechaExhumacion DESC")
    List<DifuntosEnParcela> obtenerDifuntosExhumados();

    // --- CONTEOS (Corregidos también) ---

    @Query("SELECT COUNT(d) FROM DifuntosEnParcela d WHERE d.fechaEnterramiento = :fecha")
    Long contarInhumadosEnFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT COUNT(d) FROM DifuntosEnParcela d WHERE d.fechaExhumacion = :fecha")
    Long contarExhumadosEnFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DifuntosEnParcela d WHERE d.parcela.id = :parcelaId AND d.estado = 'ENTERRADO'")
    boolean tieneDifuntosActivos(@Param("parcelaId") Integer parcelaId);

    @Query("SELECT COUNT(d) FROM DifuntosEnParcela d WHERE d.parcela.id = :parcelaId AND d.estado = 'ENTERRADO'")
    Long contarActivosPorParcela(@Param("parcelaId") Integer parcelaId);

    @Query("SELECT COUNT(d) FROM DifuntosEnParcela d WHERE d.parcela.id = :parcelaId AND d.estado = 'EXHUMADO'")
    Long contarExhumadosPorParcela(@Param("parcelaId") Integer parcelaId);

    @Query("SELECT COUNT(d) FROM DifuntosEnParcela d WHERE d.parcela.id = :parcelaId")
    Long contarTotalesPorParcela(@Param("parcelaId") Integer parcelaId);

    @Query("SELECT COUNT(d) FROM DifuntosEnParcela d WHERE d.estado = 'ENTERRADO'")
    Long contarDifuntosActivos();

    @Query("SELECT COUNT(d) FROM DifuntosEnParcela d WHERE d.estado = 'EXHUMADO'")
    Long contarDifuntosExhumados();
}