package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.TipoZonaRequest;
import com.imc.EternalsGardens.DTO.Response.TipoZonaResponse;
import com.imc.EternalsGardens.Service.Interfaces.ITipoZonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-zona")
@RequiredArgsConstructor
public class TipoZonaController {

    private final ITipoZonaService tipoZonaService;

    @GetMapping
    public ResponseEntity<List<TipoZonaResponse>> obtenerTodos() {
        return ResponseEntity.ok(tipoZonaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoZonaResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoZonaService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<TipoZonaResponse> crearTipoZona(@Valid @RequestBody TipoZonaRequest request) {
        return new ResponseEntity<>(tipoZonaService.crearTipoZona(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<TipoZonaResponse> actualizarTipoZona(
            @PathVariable Integer id,
            @Valid @RequestBody TipoZonaRequest request) {
        return ResponseEntity.ok(tipoZonaService.actualizarTipoZona(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarTipoZona(@PathVariable Integer id) {
        tipoZonaService.eliminarTipoZona(id);
        return ResponseEntity.noContent().build();
    }
}