package com.imc.EternalsGardens.Enum;

public enum EstadoConcesionEnum {
    ACTIVA("Concesión activa y vigente"),
    VENCIDA("Concesión vencida por fin del período"),
    CANCELADA("Concesión cancelada por el usuario"),
    SUSPENDIDA("Concesión suspendida por falta de pago");

    private final String descripcion;

    EstadoConcesionEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
