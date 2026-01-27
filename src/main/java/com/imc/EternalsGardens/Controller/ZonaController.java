package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.ZonaRequest;
import com.imc.EternalsGardens.DTO.Response.ZonaResponse;
import com.imc.EternalsGardens.Service.Interfaces.IZonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zonas")
@RequiredArgsConstructor
public class ZonaController {

    private final IZonaService zonaService;

    @GetMapping
    public ResponseEntity<List<ZonaResponse>> obtenerTodas() {
        return ResponseEntity.ok(zonaService.obtenerTodas());
    }

    @GetMapping("/cementerio/{cementerioId}")
    public ResponseEntity<List<ZonaResponse>> obtenerPorCementerio(@PathVariable Integer cementerioId) {
        return ResponseEntity.ok(zonaService.obtenerPorCementerio(cementerioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZonaResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(zonaService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<ZonaResponse> crearZona(@Valid @RequestBody ZonaRequest request) {
        return new ResponseEntity<>(zonaService.crearZona(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<ZonaResponse> actualizarZona(
            @PathVariable Integer id,
            @Valid @RequestBody ZonaRequest request) {
        return ResponseEntity.ok(zonaService.actualizarZona(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarZona(@PathVariable Integer id) {
        zonaService.eliminarZona(id);
        return ResponseEntity.noContent().build();
    }
}