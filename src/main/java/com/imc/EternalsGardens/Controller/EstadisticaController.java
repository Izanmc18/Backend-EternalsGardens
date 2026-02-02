package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Response.DashboardResponse;
import com.imc.EternalsGardens.Repository.DifuntoRepository;
import com.imc.EternalsGardens.Repository.ParcelaRepository;
import com.imc.EternalsGardens.Repository.SolicitudExhumacionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
@Tag(name = "Estadísticas", description = "Datos para el Dashboard")
public class EstadisticaController {

    private final ParcelaRepository parcelaRepository;
    private final DifuntoRepository difuntoRepository;
    private final SolicitudExhumacionRepository exhumacionRepository;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERARIO')")
    @Operation(summary = "Obtener resumen del cementerio")
    public ResponseEntity<DashboardResponse> obtenerDashboard() {
        DashboardResponse stats = new DashboardResponse();

        long totalParcelas = parcelaRepository.count();
        long parcelasOcupadas = parcelaRepository.findAll().stream()
                .filter(p -> "OCUPADA".equalsIgnoreCase(p.getEstado()))
                .count();

        stats.setTotalParcelas(totalParcelas);
        stats.setParcelasOcupadas(parcelasOcupadas);
        stats.setParcelasLibres(totalParcelas - parcelasOcupadas);
        stats.setDifuntosTotal(difuntoRepository.count());

        if (totalParcelas > 0) {
            double porcentaje = ((double) parcelasOcupadas / totalParcelas) * 100;
            stats.setOcupacionPorcentaje(Math.round(porcentaje * 100.0) / 100.0);
        }

        // Mock de exhumaciones pendientes (o puedes hacer un count en el repo filtrando
        // por estado)
        stats.setExhumacionesPendientes(exhumacionRepository.count()); // Refinar con filtro "PENDIENTE"

        // Nuevas Estadísticas
        // 1. Difuntos por mes (Últimos 12 meses)
        Map<String, Long> difuntosPorMes = new java.util.LinkedHashMap<>(); // LinkedHashMap para mantener orden
        List<Object[]> difuntosResults = difuntoRepository.countDifuntosLast12Months();
        for (Object[] row : difuntosResults) {
            String mes = (String) row[0];
            Number cantidad = (Number) row[1];
            difuntosPorMes.put(mes, cantidad.longValue());
        }
        stats.setDifuntosPorMes(difuntosPorMes);

        // 2. Ocupación por Cementerio
        Map<String, Double> ocupacionPorCementerio = new java.util.HashMap<>();
        List<Object[]> cementerioResults = parcelaRepository.countTotalAndOccupiedByCementerio();
        for (Object[] row : cementerioResults) {
            String nombre = (String) row[0];
            Number total = (Number) row[1];
            Number ocupadas = (Number) row[2];

            if (total.longValue() > 0) {
                double porcentaje = (ocupadas.doubleValue() / total.doubleValue()) * 100;
                ocupacionPorCementerio.put(nombre, Math.round(porcentaje * 100.0) / 100.0);
            } else {
                ocupacionPorCementerio.put(nombre, 0.0);
            }
        }
        stats.setOcupacionPorCementerio(ocupacionPorCementerio);

        return ResponseEntity.ok(stats);
    }
}