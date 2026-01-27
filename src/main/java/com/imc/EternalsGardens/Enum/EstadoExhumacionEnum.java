package com.imc.EternalsGardens.Enum;

public enum EstadoExhumacionEnum {
    PENDIENTE("Solicitud pendiente de revisión"),
    APROBADA("Solicitud aprobada por administración"),
    RECHAZADA("Solicitud rechazada"),
    EJECUTADA("Exhumación ejecutada"),
    CANCELADA("Solicitud cancelada");

    private final String descripcion;

    EstadoExhumacionEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
