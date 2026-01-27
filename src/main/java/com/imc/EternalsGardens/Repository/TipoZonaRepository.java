package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.TipoZona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TipoZonaRepository extends JpaRepository<TipoZona, Integer> {

    // Buscar por nombre exacto
    Optional<TipoZona> findByNombre(String nombre);

    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);


}