package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.Concesion;
import com.imc.EternalsGardens.Entity.Usuario;
import com.imc.EternalsGardens.Entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConcesionRepository extends JpaRepository<Concesion, Integer> {

    List<Concesion> findByUsuario(Usuario usuario);

    List<Concesion> findByUsuarioId(Integer usuarioId);

    List<Concesion> findByCementerioId(Integer cementerioId);


    @Query("SELECT c FROM Concesion c WHERE c.usuario.id = :usuarioId " +
            "AND c.fechaFin > CURRENT_DATE " +
            "ORDER BY c.fechaFin ASC")
    List<Concesion> obtenerConcesionesActivasPorUsuario(@Param("usuarioId") Integer usuarioId);

    @Query("SELECT c FROM Concesion c WHERE c.usuario.id = :usuarioId " +
            "AND c.fechaFin <= CURRENT_DATE " +
            "ORDER BY c.fechaFin DESC")
    List<Concesion> obtenerConcesionesVencidasPorUsuario(@Param("usuarioId") Integer usuarioId);

    Optional<Concesion> findByParcelas(Parcela parcela);

    @Query("SELECT c FROM Concesion c JOIN c.parcelas p WHERE p.id = :parcelaId")
    Optional<Concesion> findByParcelaId(@Param("parcelaId") Integer parcelaId);

    @Query("SELECT c FROM Concesion c WHERE c.fechaFin > CURRENT_DATE ORDER BY c.fechaFin ASC")
    List<Concesion> obtenerConcesionesActivas();

    @Query("SELECT c FROM Concesion c WHERE c.fechaFin <= CURRENT_DATE ORDER BY c.fechaFin DESC")
    List<Concesion> obtenerConcesionesVencidas();


    List<Concesion> findByFechaInicioBetween(LocalDate inicio, LocalDate fin);

    @Query("SELECT c FROM Concesion c WHERE c.fechaFin > CURRENT_DATE AND c.fechaFin <= :proximoVencimiento ORDER BY c.fechaFin ASC")
    List<Concesion> obtenerConcesionesProximasAVencer(@Param("proximoVencimiento") LocalDate proximoVencimiento);
}