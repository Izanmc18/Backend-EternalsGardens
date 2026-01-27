package com.imc.EternalsGardens.Enum;

public enum EstadoPagoEnum {
    PENDIENTE("Pago pendiente de procesamiento"),
    COMPLETADO("Pago realizado y confirmado"),
    FALLIDO("Pago rechazado o fallido"),
    REEMBOLSADO("Pago reembolsado al cliente");

    private final String descripcion;

    EstadoPagoEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
