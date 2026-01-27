package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.OperadorRequest;
import com.imc.EternalsGardens.DTO.Response.OperadorResponse;
import com.imc.EternalsGardens.Service.Interfaces.IOperadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operadores")
@RequiredArgsConstructor
@Tag(name = "Operadores", description = "Gestión de usuarios tipo Operador de Cementerio")
public class OperadorController {

    private final IOperadorService operadorService;

    // Solo el ADMIN puede crear operadores
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Crear un nuevo operador (Solo Admin)")
    public ResponseEntity<OperadorResponse> crearOperador(@Valid @RequestBody OperadorRequest request) {
        return new ResponseEntity<>(operadorService.crearOperador(request), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Listar todos los operadores")
    public ResponseEntity<List<OperadorResponse>> obtenerTodos() {
        return ResponseEntity.ok(operadorService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Obtener operador por ID")
    public ResponseEntity<OperadorResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(operadorService.obtenerPorId(id));
    }

    // Endpoint para que el propio operador edite sus datos
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('OPERARIO', 'ADMINISTRADOR')")
    @Operation(summary = "Actualizar perfil propio (Operador logueado)")
    public ResponseEntity<OperadorResponse> actualizarPerfilPropio(@RequestBody OperadorRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // El email viene del token
        return ResponseEntity.ok(operadorService.actualizarPerfilPropio(email, request));
    }

    // Endpoint para que el admin edite cualquier operador
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar operador por ID (Solo Admin)")
    public ResponseEntity<OperadorResponse> actualizarOperador(@PathVariable Integer id, @RequestBody OperadorRequest request) {
        return ResponseEntity.ok(operadorService.actualizarOperadorAdmin(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Eliminar operador")
    public ResponseEntity<Void> eliminarOperador(@PathVariable Integer id) {
        operadorService.eliminarOperador(id);
        return ResponseEntity.noContent().build();
    }
}