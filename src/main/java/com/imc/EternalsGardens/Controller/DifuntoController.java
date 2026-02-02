package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.DifuntoRequest;
import com.imc.EternalsGardens.DTO.Response.DifuntoResponse;
import com.imc.EternalsGardens.Service.Interfaces.IDifuntoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/difuntos")
@RequiredArgsConstructor
public class DifuntoController {

    private final IDifuntoService difuntoService;
    private final com.imc.EternalsGardens.Service.Interfaces.IUsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<Page<DifuntoResponse>> obtenerTodos(
            @RequestParam(required = false) Integer cementerioId,
            @RequestParam(required = false) Integer zonaId,
            Pageable pageable) {

        // Si se proporciona zonaId, filtrar por zona
        if (zonaId != null) {
            return ResponseEntity.ok(difuntoService.obtenerPorZona(zonaId, pageable));
        }

        // Si se proporciona cementerioId, filtrar por cementerio
        if (cementerioId != null) {
            return ResponseEntity.ok(difuntoService.obtenerPorCementerio(cementerioId, pageable));
        }

        // Si no hay filtros, devolver página vacía
        return ResponseEntity.ok(Page.empty(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<List<DifuntoResponse>> buscarDifuntos(@RequestParam("q") String query) {
        return ResponseEntity.ok(difuntoService.buscarDifuntos(query));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<DifuntoResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(difuntoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<DifuntoResponse> crearDifunto(@Valid @RequestBody DifuntoRequest request) {
        return new ResponseEntity<>(difuntoService.crearDifunto(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'OPERADOR_CEMENTERIO')")
    public ResponseEntity<DifuntoResponse> actualizarDifunto(
            @PathVariable Integer id,
            @Valid @RequestBody DifuntoRequest request) {
        return ResponseEntity.ok(difuntoService.actualizarDifunto(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarDifunto(@PathVariable Integer id) {
        difuntoService.eliminarDifunto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mis-difuntos")
    @PreAuthorize("hasRole('CIUDADANO')")
    public ResponseEntity<List<DifuntoResponse>> obtenerMisDifuntos(java.security.Principal principal) {
        String email = principal.getName();
        var usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(difuntoService.obtenerPorUsuario(usuario.getId()));
    }
}