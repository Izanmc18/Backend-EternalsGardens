package com.imc.EternalsGardens.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "concesion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Concesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cementerio_id", nullable = false)
    private Cementerio cementerio;

    @OneToMany(mappedBy = "concesion", fetch = FetchType.LAZY)
    private List<Parcela> parcelas = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column()
    private Integer periodoAnios;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioPeriodo;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioMantenimientoAnual;

    @Column(nullable = false, length = 30)
    private String estado = "ACTIVA";

    @Column(nullable = false, length = 20)
    private String tipoAlta;
}