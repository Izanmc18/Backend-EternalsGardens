package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.PagoRequest;
import com.imc.EternalsGardens.DTO.Response.PagoResponse;
import com.imc.EternalsGardens.Service.Interfaces.IPagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Gestión de pagos y transacciones")
public class PagoController {

    private final IPagoService pagoService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo pago")
    public ResponseEntity<PagoResponse> registrarPago(@Valid @RequestBody PagoRequest request) {
        return new ResponseEntity<>(pagoService.registrarPago(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos los pagos")
    public ResponseEntity<List<PagoResponse>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pago por ID")
    public ResponseEntity<PagoResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    @GetMapping("/concesion/{concesionId}")
    @Operation(summary = "Listar pagos de una concesión")
    public ResponseEntity<List<PagoResponse>> obtenerPorConcesion(@PathVariable Integer concesionId) {
        return ResponseEntity.ok(pagoService.obtenerPorConcesion(concesionId));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar pagos realizados por un usuario")
    public ResponseEntity<List<PagoResponse>> obtenerPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(pagoService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/referencia")
    @Operation(summary = "Buscar pago por referencia de transacción")
    public ResponseEntity<PagoResponse> obtenerPorReferencia(@RequestParam String referencia) {
        return ResponseEntity.ok(pagoService.obtenerPorReferencia(referencia));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro de pago")
    public ResponseEntity<Void> eliminarPago(@PathVariable Integer id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}