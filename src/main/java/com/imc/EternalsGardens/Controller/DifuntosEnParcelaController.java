package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.DifuntosEnParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.DifuntosEnParcelaResponse;
import com.imc.EternalsGardens.Service.Interfaces.IDifuntosEnParcelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enterramientos")
@RequiredArgsConstructor
public class DifuntosEnParcelaController {

    private final IDifuntosEnParcelaService service;

    // Registrar un nuevo entierro
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<DifuntosEnParcelaResponse> registrarEnterramiento(
            @Valid @RequestBody DifuntosEnParcelaRequest request) {
        return new ResponseEntity<>(service.registrarEnterramiento(request), HttpStatus.CREATED);
    }

    // Ver quién está enterrado en una parcela concreta
    @GetMapping("/parcela/{parcelaId}")
    public ResponseEntity<List<DifuntosEnParcelaResponse>> obtenerPorParcela(@PathVariable Integer parcelaId) {
        return ResponseEntity.ok(service.obtenerPorParcela(parcelaId));
    }

    // Ver todos los entierros asociados a una concesión
    @GetMapping("/concesion/{concesionId}")
    public ResponseEntity<List<DifuntosEnParcelaResponse>> obtenerPorConcesion(@PathVariable Integer concesionId) {
        return ResponseEntity.ok(service.obtenerPorConcesion(concesionId));
    }

    // Registrar una exhumación
    @PatchMapping("/{id}/exhumacion")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> registrarExhumacion(
            @PathVariable Integer id,
            @RequestParam String motivo) {
        service.registrarExhumacion(id, motivo);
        return ResponseEntity.noContent().build();
    }
}