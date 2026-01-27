package com.imc.EternalsGardens.Enum;

public enum MetodoPagoEnum {
    TRANSFERENCIA("Transferencia bancaria"),
    TARJETA_CREDITO("Tarjeta de crédito/débito"),
    EFECTIVO("Efectivo en persona"),
    CHEQUE("Cheque bancario");

    private final String descripcion;

    MetodoPagoEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
