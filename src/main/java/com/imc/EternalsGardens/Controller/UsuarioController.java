package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.UsuarioRequest;
import com.imc.EternalsGardens.DTO.Response.UsuarioResponse;
import com.imc.EternalsGardens.Service.Interfaces.IUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<org.springframework.data.domain.Page<UsuarioResponse>> obtenerTodos(
            org.springframework.data.domain.Pageable pageable,
            @RequestParam(required = false) String rol) {
        return ResponseEntity.ok(usuarioService.obtenerTodos(pageable, rol));
    }

    @PostMapping(consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> crearUsuario(
            @Valid @ModelAttribute UsuarioRequest request,
            @RequestParam(value = "foto", required = false) org.springframework.web.multipart.MultipartFile foto) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(usuarioService.crearUsuario(request, foto));
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

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('ADMINISTRADOR') or @userSecurity.esElMismoUsuario(#id)")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Integer id,
            @Valid @ModelAttribute UsuarioRequest request,
            @RequestParam(value = "foto", required = false) org.springframework.web.multipart.MultipartFile foto) {
        System.out.println("DEBUG: actualizarUsuario ID: " + id + ", Foto: "
                + (foto != null ? foto.getOriginalFilename() : "null"));
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request, foto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}