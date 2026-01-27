package com.imc.EternalsGardens.Enum;

public enum EstadoParcelaEnum {
    LIBRE("Parcela disponible para ocupar"),
    OCUPADA("Parcela ocupada con un difunto"),
    RESERVADA("Parcela reservada por el cliente"),
    MANTENIMIENTO("Parcela en mantenimiento");

    private final String descripcion;

    EstadoParcelaEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
