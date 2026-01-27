package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // Buscar todos los pagos asociados a una concesión
    List<Pago> findByConcesionId(Integer concesionId);

    // Buscar todos los pagos realizados por un usuario
    List<Pago> findByUsuarioId(Integer usuarioId);

    // Buscar por referencia de transacción (CORREGIDO: antes numeroPago)
    @Query("SELECT p FROM Pago p WHERE LOWER(p.referenciaTransaccion) = LOWER(:referencia)")
    Optional<Pago> buscarPorReferencia(@Param("referencia") String referencia);

    // Método para verificar duplicados rápidamente
    boolean existsByReferenciaTransaccion(String referenciaTransaccion);
}