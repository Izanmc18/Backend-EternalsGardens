package com.imc.EternalsGardens.DTO.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OperadorResponse {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String email;
    private String dni;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    private String pais;
    private LocalDate fechaNacimiento;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    @Schema(description = "Empresa del operador")
    private String empresa;

    private String rolNombre;
}