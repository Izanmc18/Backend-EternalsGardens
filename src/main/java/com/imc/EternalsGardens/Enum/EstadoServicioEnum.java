package com.imc.EternalsGardens.Enum;

public enum EstadoServicioEnum {
    PENDIENTE("Servicio pendiente de ejecución"),
    EN_PROCESO("Servicio en ejecución"),
    COMPLETADO("Servicio completado exitosamente"),
    CANCELADO("Servicio cancelado");

    private final String descripcion;

    EstadoServicioEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
