package com.imc.EternalsGardens.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "parcela")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @Column(nullable = false)
    private Integer numeroFila;

    @Column(nullable = false)
    private Integer numeroColumna;

    @Column(nullable = false, unique = true, length = 50)
    private String numeroIdentificadorUnico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_zona_id", nullable = false)
    private TipoZona tipoZona;

    @Column(nullable = false, length = 20)
    private String estado = "LIBRE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concesion_id")
    private Concesion concesion;
}