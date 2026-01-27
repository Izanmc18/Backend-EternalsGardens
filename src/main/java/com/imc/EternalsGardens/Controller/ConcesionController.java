package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.ConcesionRequest;
import com.imc.EternalsGardens.DTO.Response.ConcesionResponse;
import com.imc.EternalsGardens.Service.Interfaces.IConcesionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/concesiones")
@RequiredArgsConstructor
public class ConcesionController {

    private final IConcesionService concesionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<List<ConcesionResponse>> obtenerTodas() {
        return ResponseEntity.ok(concesionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<ConcesionResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(concesionService.obtenerPorId(id));
    }

    @GetMapping("/titular/{usuarioId}")
    public ResponseEntity<List<ConcesionResponse>> obtenerPorTitular(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(concesionService.obtenerPorTitular(usuarioId));
    }

    @GetMapping("/cementerio/{cementerioId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<List<ConcesionResponse>> obtenerPorCementerio(@PathVariable Integer cementerioId) {
        return ResponseEntity.ok(concesionService.obtenerPorCementerio(cementerioId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<ConcesionResponse> crearConcesion(@Valid @RequestBody ConcesionRequest request) {
        return new ResponseEntity<>(concesionService.crearConcesion(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        concesionService.cambiarEstadoConcesion(id, nuevoEstado);
        return ResponseEntity.noContent().build();
    }
}