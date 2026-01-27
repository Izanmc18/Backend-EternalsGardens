package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.SolicitudExhumacionRequest;
import com.imc.EternalsGardens.DTO.Response.SolicitudExhumacionResponse;
import com.imc.EternalsGardens.Service.Interfaces.ISolicitudExhumacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exhumaciones")
@RequiredArgsConstructor
@Tag(name = "Exhumaciones", description = "Gestión de solicitudes de exhumación")
public class SolicitudExhumacionController {

    private final ISolicitudExhumacionService servicio;
    // Necesitas inyectar UsuarioService o Repository para sacar el ID del email del token

    @PostMapping
    @Operation(summary = "Crear solicitud (Ciudadano)")
    public ResponseEntity<SolicitudExhumacionResponse> crearSolicitud(@RequestBody SolicitudExhumacionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        // TODO: Buscar ID de usuario por email usando tu servicio de usuarios
        Integer usuarioId = 1;

        return ResponseEntity.ok(servicio.crearSolicitud(usuarioId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERARIO', 'ADMINISTRADOR')")
    @Operation(summary = "Listar todas (Operadores)")
    public ResponseEntity<List<SolicitudExhumacionResponse>> obtenerTodas() {
        return ResponseEntity.ok(servicio.obtenerTodas());
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('OPERARIO', 'ADMINISTRADOR')")
    public ResponseEntity<SolicitudExhumacionResponse> cambiarEstado(@PathVariable Integer id, @RequestParam String estado) {
        return ResponseEntity.ok(servicio.cambiarEstado(id, estado));
    }
}