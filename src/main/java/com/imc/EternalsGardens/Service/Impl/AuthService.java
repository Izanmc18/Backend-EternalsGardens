package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.LoginRequest;
import com.imc.EternalsGardens.DTO.Request.UsuarioRequest;
import com.imc.EternalsGardens.DTO.Response.AuthResponse;
import com.imc.EternalsGardens.Entity.Rol;
import com.imc.EternalsGardens.Entity.Usuario;
import com.imc.EternalsGardens.Repository.RolRepository;
import com.imc.EternalsGardens.Repository.UsuarioRepository;
import com.imc.EternalsGardens.Security.JwtUtils;
import com.imc.EternalsGardens.Security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    // MÉTODO DE REGISTRO
    public AuthResponse register(UsuarioRequest request) {


        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setDni(request.getDni());
        usuario.setTelefono(request.getTelefono());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());


        usuario.setContraseña(passwordEncoder.encode(request.getPassword()));

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
        usuario.setRol(rol);

        usuarioRepository.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .nombreUsuario(usuario.getNombre())
                .rol(usuario.getRol().getNombre())
                .build();
    }

    // MÉTODO DE LOGIN
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .nombreUsuario(usuario.getNombre())
                .rol(usuario.getRol().getNombre())
                .build();
    }
}