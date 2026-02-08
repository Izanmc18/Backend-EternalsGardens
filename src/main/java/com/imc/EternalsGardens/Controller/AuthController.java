package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.DTO.Request.LoginRequest;
import com.imc.EternalsGardens.DTO.Request.UsuarioRequest;
import com.imc.EternalsGardens.DTO.Response.AuthResponse;
import com.imc.EternalsGardens.Service.Impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("dni") String dni,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("telefono") String telefono,
            @RequestParam("fechaNacimiento") String fechaNacimiento,
            @RequestParam("rolId") Integer rolId,
            @RequestParam("activo") Boolean activo,
            @RequestParam(value = "foto", required = false) MultipartFile foto) {

        UsuarioRequest request = UsuarioRequest.builder()
                .nombre(nombre)
                .apellidos(apellidos)
                .dni(dni)
                .email(email)
                .password(password)
                .telefono(telefono)
                .fechaNacimiento(LocalDate.parse(fechaNacimiento))
                .rolId(rolId)
                .activo(activo)
                .build();

        return ResponseEntity.ok(authService.register(request, foto));
    }
}