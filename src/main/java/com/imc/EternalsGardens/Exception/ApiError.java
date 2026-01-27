package com.imc.EternalsGardens.Exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;

    private int codigo;
    private String estado;
    private String mensaje;
    private String ruta;

    // Constructor vacío
    public ApiError() {
        this.fecha = LocalDateTime.now();
    }

    // Constructor útil
    public ApiError(HttpStatus status, String mensaje, String ruta) {
        this.fecha = LocalDateTime.now();
        this.codigo = status.value();
        this.estado = status.name();
        this.mensaje = mensaje;
        this.ruta = ruta;
    }
}