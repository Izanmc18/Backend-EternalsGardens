package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.ServicioRequest;
import com.imc.EternalsGardens.DTO.Response.ServicioResponse;
import com.imc.EternalsGardens.Service.Interfaces.IServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
@Tag(name = "Servicios", description = "Gestión del catálogo de servicios")
public class ServicioController {

    private final IServicioService servicioService;

    @PostMapping
    @Operation(summary = "Crear un nuevo servicio")
    public ResponseEntity<ServicioResponse> crearServicio(@Valid @RequestBody ServicioRequest request) {
        return new ResponseEntity<>(servicioService.crearServicio(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos los servicios")
    public ResponseEntity<List<ServicioResponse>> obtenerTodos() {
        return ResponseEntity.ok(servicioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un servicio por su ID")
    public ResponseEntity<ServicioResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @GetMapping("/tipo-zona/{tipoZonaId}")
    @Operation(summary = "Listar servicios disponibles para un tipo de zona específico")
    public ResponseEntity<List<ServicioResponse>> obtenerPorTipoZona(@PathVariable Integer tipoZonaId) {
        return ResponseEntity.ok(servicioService.obtenerPorTipoZona(tipoZonaId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar información de un servicio")
    public ResponseEntity<ServicioResponse> actualizarServicio(@PathVariable Integer id, @Valid @RequestBody ServicioRequest request) {
        return ResponseEntity.ok(servicioService.actualizarServicio(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un servicio")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Integer id) {
        servicioService.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }
}