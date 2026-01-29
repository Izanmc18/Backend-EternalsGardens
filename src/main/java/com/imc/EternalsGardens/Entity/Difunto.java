package com.imc.EternalsGardens.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "difunto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Difunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column()
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private LocalDate fechaDefuncion;

    @Column(length = 20, unique = true)
    private String dni;

    @Column(length = 5)
    private String sexo;

    @Column(columnDefinition = "TEXT")
    private String causa;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(length = 500)
    private String fotoPerfil;

    @Column(columnDefinition = "TEXT")
    private String biografiaDigital;

    @ManyToOne
    @JoinColumn(name = "parcela_id")
    private Parcela parcela;
}
