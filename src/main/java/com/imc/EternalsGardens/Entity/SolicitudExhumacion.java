package com.imc.EternalsGardens.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "solicitud_exhumacion")
public class SolicitudExhumacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // --- AÑADIR ESTE CAMPO ---
    @Column(name = "numero_solicitud", unique = true)
    private String numeroSolicitud;
    // -------------------------

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "difunto_id", nullable = false)
    private Difunto difunto;

    @ManyToOne
    @JoinColumn(name = "parcela_id")
    private Parcela parcela;

    @ManyToOne
    @JoinColumn(name = "usuario_aprobador_id")
    private Usuario usuarioAprobador;

    private String motivo;

    private String observaciones;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    private String estado;
}