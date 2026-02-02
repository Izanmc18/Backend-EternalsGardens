package com.imc.EternalsGardens.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DifuntoResponse {
    private Integer id;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private LocalDate fechaDefuncion;
    private String dni;
    private String sexo;
    private String fotoUrl;
    private String causa;
    private String notas;
    private String fotoPerfil;
    private String biografiaDigital;
    private String parcelaUbicacion;

}