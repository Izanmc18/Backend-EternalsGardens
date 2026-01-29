package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.UsuarioRequest;
import com.imc.EternalsGardens.DTO.Response.UsuarioResponse;
import com.imc.EternalsGardens.Service.Interfaces.IUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(usuarioService.crearUsuario(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or @userSecurity.esElMismoUsuario(#id)")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @GetMapping("/buscar/{dni}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<UsuarioResponse> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(usuarioService.buscarPorDni(dni));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}