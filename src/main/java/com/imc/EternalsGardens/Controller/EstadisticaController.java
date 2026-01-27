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

        // Mock de exhumaciones pendientes (o puedes hacer un count en el repo filtrando por estado)
        stats.setExhumacionesPendientes(exhumacionRepository.count()); // Refinar con filtro "PENDIENTE"

        // Datos para gráfico de tipos
        Map<String, Long> porTipo = new HashMap<>();
        // Aquí podrías hacer una consulta GROUP BY en el repositorio para ser más eficiente
        // porTipo.put("Nicho", 150L);
        // porTipo.put("Panteón", 20L);
        stats.setParcelasPorTipo(porTipo);

        return ResponseEntity.ok(stats);
    }
}