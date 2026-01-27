package com.imc.EternalsGardens.Enum;

public enum EstadoDifuntoEnum {
    ENTERRADO("Difunto enterrado en la parcela"),
    EXHUMADO("Difunto exhumado de la parcela"),
    TRASLADADO("Difunto trasladado a otra parcela");

    private final String descripcion;

    EstadoDifuntoEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
