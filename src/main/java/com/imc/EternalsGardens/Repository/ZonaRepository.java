package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Integer> {


    List<Zona> findByCementerioId(Integer cementerioId);

    List<Zona> findByCementerioIdAndActivaTrue(Integer cementerioId);

    List<Zona> findByTipoZonaId(Integer tipoZonaId);


    boolean existsByNombreAndCementerioId(String nombre, Integer cementerioId);

    @Query("SELECT COALESCE(SUM(z.capacidadTotal), 0) " +
            "FROM Zona z WHERE z.cementerio.id = :cementerioId AND z.activa = true")
    Integer calcularCapacidadTotalCementerio(@Param("cementerioId") Integer cementerioId);


    long countByCementerioId(Integer cementerioId);


    @Modifying
    @Query("UPDATE Zona z SET z.activa = false WHERE z.cementerio.id = :cementerioId")
    void desactivarZonasPorCementerio(@Param("cementerioId") Integer cementerioId);
}