package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.ServicioParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.ServicioParcelaResponse;
import com.imc.EternalsGardens.Service.Interfaces.IServicioParcelaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios-parcela")
@RequiredArgsConstructor
@Tag(name = "Servicios de Parcela", description = "Gestión de la contratación de servicios en parcelas")
public class ServicioParcelaController {

    private final IServicioParcelaService servicioParcelaService;

    @PostMapping("/contratar")
    @Operation(summary = "Contratar un servicio para una parcela")
    public ResponseEntity<ServicioParcelaResponse> contratarServicio(@Valid @RequestBody ServicioParcelaRequest request) {
        return new ResponseEntity<>(servicioParcelaService.contratarServicio(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas las contrataciones de servicios")
    public ResponseEntity<List<ServicioParcelaResponse>> obtenerTodos() {
        return ResponseEntity.ok(servicioParcelaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una contratación por ID")
    public ResponseEntity<ServicioParcelaResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(servicioParcelaService.obtenerPorId(id));
    }

    @GetMapping("/parcela/{parcelaId}")
    @Operation(summary = "Listar servicios contratados en una parcela")
    public ResponseEntity<List<ServicioParcelaResponse>> obtenerPorParcela(@PathVariable Integer parcelaId) {
        return ResponseEntity.ok(servicioParcelaService.obtenerPorParcela(parcelaId));
    }

    @GetMapping("/servicio/{servicioId}")
    @Operation(summary = "Listar contrataciones de un tipo de servicio específico")
    public ResponseEntity<List<ServicioParcelaResponse>> obtenerPorServicio(@PathVariable Integer servicioId) {
        return ResponseEntity.ok(servicioParcelaService.obtenerPorServicio(servicioId));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado de una contratación (ej: PENDIENTE -> COMPLETADO)")
    public ResponseEntity<ServicioParcelaResponse> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String estado) {
        return ResponseEntity.ok(servicioParcelaService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una contratación")
    public ResponseEntity<Void> eliminarServicioParcela(@PathVariable Integer id) {
        servicioParcelaService.eliminarServicioParcela(id);
        return ResponseEntity.noContent().build();
    }
}