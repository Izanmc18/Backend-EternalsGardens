package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    Optional<Servicio> findByNombre(String nombre);

    List<Servicio> findByActivoTrue();

    @Query("SELECT s FROM Servicio s WHERE s.tipoZona.id = :tipoZonaId AND s.activo = true")
    List<Servicio> findByTipoZonaIdAndActivoTrue(@Param("tipoZonaId") Integer tipoZonaId);


    // Si necesitas contar servicios activos globales:
    @Query("SELECT COUNT(s) FROM Servicio s WHERE s.activo = true")
    Long contarServiciosActivos();
}