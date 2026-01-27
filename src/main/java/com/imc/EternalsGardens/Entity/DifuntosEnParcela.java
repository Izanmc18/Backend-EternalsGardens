package com.imc.EternalsGardens.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "difuntos_en_parcela")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DifuntosEnParcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difunto_id", nullable = false)
    private Difunto difunto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcela_id", nullable = false)
    private Parcela parcela;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concesion_id", nullable = false)
    private Concesion concesion;

    @Column(nullable = false)
    private LocalDate fechaEnterramiento;

    @Column()
    private LocalDate fechaExhumacion;

    @Column(nullable = false, length = 20)
    private String estado = "ENTERRADO";

    @Column(columnDefinition = "TEXT")
    private String motivoExhumacion;
}
