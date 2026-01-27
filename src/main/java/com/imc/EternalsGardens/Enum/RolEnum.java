package com.imc.EternalsGardens.Enum;

public enum RolEnum {
    ADMINISTRADOR("Administrador del sistema con acceso total"),
    OPERADOR_CEMENTERIO("Personal del cementerio que gestiona operaciones diarias"),
    CLIENTE("Usuario final - familiar o interesado en concesiones");

    private final String descripcion;

    RolEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
