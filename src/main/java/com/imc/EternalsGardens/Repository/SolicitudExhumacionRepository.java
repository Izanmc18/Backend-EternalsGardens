package com.imc.EternalsGardens.Repository;

import com.imc.EternalsGardens.Entity.SolicitudExhumacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudExhumacionRepository extends JpaRepository<SolicitudExhumacion, Integer> {

    // Buscar por ID de usuario solicitante
    List<SolicitudExhumacion> findByUsuarioId(Integer usuarioId);

    // Buscar por ID de la parcela asociada (ahora que ya añadiste el campo 'parcela')
    List<SolicitudExhumacion> findByParcelaId(Integer parcelaId);

    // Buscar por número de solicitud generado
    Optional<SolicitudExhumacion> findByNumeroSolicitud(String numeroSolicitud);

    // Si necesitas buscar por difunto, usa esto:
    List<SolicitudExhumacion> findByDifuntoId(Integer difuntoId);
}