package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.ServicioParcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioParcelaRepository extends JpaRepository<ServicioParcela, Integer> {

    List<ServicioParcela> findByParcelaId(Integer parcelaId);

    List<ServicioParcela> findByServicioId(Integer servicioId);

    // Método para buscar servicios contratados en una parcela concreta
    @Query("SELECT sp FROM ServicioParcela sp WHERE sp.parcela.id = :parcelaId ORDER BY sp.fechaContratacion DESC")
    List<ServicioParcela> obtenerServiciosPorParcela(@Param("parcelaId") Integer parcelaId);


}