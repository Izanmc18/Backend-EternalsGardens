package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.OperadorCementerio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperadorRepository extends JpaRepository<OperadorCementerio, Integer> {
    Optional<OperadorCementerio> findByEmail(String email);
    boolean existsByEmail(String email);
}