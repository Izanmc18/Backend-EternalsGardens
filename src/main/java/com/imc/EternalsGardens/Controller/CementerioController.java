package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.CementerioRequest;
import com.imc.EternalsGardens.DTO.Response.CementerioResponse;
import com.imc.EternalsGardens.Service.Interfaces.ICementerioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cementerios")
@RequiredArgsConstructor
public class CementerioController {

    private final ICementerioService cementerioService;

    @GetMapping
    public ResponseEntity<List<CementerioResponse>> obtenerTodos() {
        return ResponseEntity.ok(cementerioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CementerioResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(cementerioService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CementerioResponse> crearCementerio(@Valid @RequestBody CementerioRequest request) {
        return new ResponseEntity<>(cementerioService.crearCementerio(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<CementerioResponse> actualizarCementerio(
            @PathVariable Integer id,
            @Valid @RequestBody CementerioRequest request) {
        return ResponseEntity.ok(cementerioService.actualizarCementerio(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarCementerio(@PathVariable Integer id) {
        cementerioService.eliminarCementerio(id);
        return ResponseEntity.noContent().build();
    }
}