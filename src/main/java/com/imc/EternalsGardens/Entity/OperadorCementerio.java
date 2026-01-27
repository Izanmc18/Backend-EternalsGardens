package com.imc.EternalsGardens.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "operador_cementerio")
@DiscriminatorValue("operador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperadorCementerio extends Usuario {

    @Column(length = 150)
    private String empresa;
}
