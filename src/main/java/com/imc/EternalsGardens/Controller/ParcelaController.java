package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.ParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.ParcelaResponse;
import com.imc.EternalsGardens.Service.Interfaces.IParcelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcelas")
@RequiredArgsConstructor
public class ParcelaController {

    private final IParcelaService parcelaService;
    private final com.imc.EternalsGardens.Service.Interfaces.IUsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<ParcelaResponse>> obtenerTodas() {
        return ResponseEntity.ok(parcelaService.obtenerTodas());
    }

    @GetMapping("/mis-parcelas")
    @PreAuthorize("hasRole('CIUDADANO')")
    public ResponseEntity<List<ParcelaResponse>> obtenerMisParcelas(java.security.Principal principal) {
        String email = principal.getName();
        var usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(parcelaService.obtenerPorUsuario(usuario.getId()));
    }

    @GetMapping("/zona/{zonaId}")
    public ResponseEntity<List<ParcelaResponse>> obtenerPorZona(@PathVariable Integer zonaId) {
        return ResponseEntity.ok(parcelaService.obtenerPorZona(zonaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParcelaResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(parcelaService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<ParcelaResponse> crearParcela(@Valid @RequestBody ParcelaRequest request) {
        return new ResponseEntity<>(parcelaService.crearParcela(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<ParcelaResponse> actualizarParcela(
            @PathVariable Integer id,
            @Valid @RequestBody ParcelaRequest request) {
        return ResponseEntity.ok(parcelaService.actualizarParcela(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarParcela(@PathVariable Integer id) {
        parcelaService.eliminarParcela(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generar/{zonaId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<Void> generarParcelas(@PathVariable Integer zonaId) {
        parcelaService.generarParcelasPorZona(zonaId);
        return ResponseEntity.ok().build();
    }
}