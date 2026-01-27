package com.imc.EternalsGardens.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OperadorRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del operador", example = "Carlos")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Schema(description = "Apellidos del operador", example = "Sánchez Ruiz")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Schema(description = "Correo electrónico", example = "carlos.operador@empresa.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña de acceso", example = "securePass123")
    private String password;

    @NotBlank(message = "El DNI es obligatorio")
    @Schema(description = "Documento de identidad", example = "12345678Z")
    private String dni;

    @Schema(description = "Teléfono de contacto", example = "+34 600 123 456")
    private String telefono;

    @Schema(description = "Dirección postal", example = "C/ Mayor 12")
    private String direccion;

    @Schema(description = "Ciudad", example = "Madrid")
    private String ciudad;

    @Schema(description = "Código Postal", example = "28001")
    private String codigoPostal;

    @Schema(description = "País", example = "España")
    private String pais;

    @Past(message = "La fecha de nacimiento debe ser pasada")
    @Schema(description = "Fecha de nacimiento", example = "1985-05-20")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Schema(description = "Empresa a la que pertenece el operador", example = "Servicios Funerarios S.L.")
    private String empresa;

    @Schema(description = "Estado del usuario", example = "true")
    private Boolean activo;
}