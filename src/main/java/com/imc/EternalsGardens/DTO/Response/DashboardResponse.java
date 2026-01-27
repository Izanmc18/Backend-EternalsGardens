package com.imc.EternalsGardens.DTO.Response;

import lombok.Data;
import java.util.Map;

@Data
public class DashboardResponse {
    private long totalParcelas;
    private long parcelasOcupadas;
    private long parcelasLibres;
    private long difuntosTotal;
    private long exhumacionesPendientes;
    private double ocupacionPorcentaje;
    private Map<String, Long> parcelasPorTipo;
}