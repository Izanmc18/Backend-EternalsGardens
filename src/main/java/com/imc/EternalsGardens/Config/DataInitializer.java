package com.imc.EternalsGardens.Config;

import com.imc.EternalsGardens.Entity.Rol;
import com.imc.EternalsGardens.Entity.Usuario;
import com.imc.EternalsGardens.Repository.RolRepository;
import com.imc.EternalsGardens.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Crear Roles si no existen
        Rol rolAdmin = rolRepository.findByNombre("ADMINISTRADOR").orElseGet(() -> {
            Rol rol = new Rol();
            rol.setNombre("ADMINISTRADOR");
            rol.setDescripcion("Administrador total del sistema");
            return rolRepository.save(rol);
        });

        rolRepository.findByNombre("OPERADOR_CEMENTERIO").orElseGet(() -> {
            Rol rol = new Rol();
            rol.setNombre("OPERADOR_CEMENTERIO");
            rol.setDescripcion("Operario encargado de tareas diarias");
            return rolRepository.save(rol);
        });

        // 2. Crear o Actualizar Usuario Admin
        // Enfoque robusto: Buscar en memoria para evitar problemas de queries
        // especificas
        List<Usuario> todos = usuarioRepository.findAll();

        Usuario usuarioConflictivoDni = todos.stream()
                .filter(u -> "00000000X".equalsIgnoreCase(u.getDni()))
                .findFirst().orElse(null);

        Usuario usuarioConflictivoEmail = todos.stream()
                .filter(u -> "admin@eternals.com".equalsIgnoreCase(u.getEmail()))
                .findFirst().orElse(null);

        Usuario admin;

        if (usuarioConflictivoDni != null && usuarioConflictivoEmail != null) {
            if (usuarioConflictivoDni.getId().equals(usuarioConflictivoEmail.getId())) {
                // Mismo usuario, todo bien
                admin = usuarioConflictivoDni;
            } else {
                // Conflicto: Hay dos usuarios diferentes. Uno tiene el email y otro el DNI.
                // Eliminamos el que tiene el EMAIL para quedarnos con el del DNI (que suele
                // tener mas restricciones)
                System.out.println(">>> Resolviendo conflicto de usuarios duplicados...");
                usuarioRepository.delete(usuarioConflictivoEmail);
                usuarioRepository.flush();
                admin = usuarioConflictivoDni;
            }
        } else if (usuarioConflictivoDni != null) {
            admin = usuarioConflictivoDni;
        } else if (usuarioConflictivoEmail != null) {
            admin = usuarioConflictivoEmail;
        } else {
            admin = new Usuario();
            admin.setFechaCreacion(LocalDateTime.now());
        }

        admin.setNombre("Admin");
        admin.setApellidos("Principal");
        admin.setEmail("admin@eternals.com");
        admin.setDni("00000000X");
        admin.setTelefono("000000000");
        admin.setContraseña(passwordEncoder.encode("admin123"));
        admin.setRol(rolAdmin);
        admin.setActivo(true);

        usuarioRepository.save(admin);
        System.out.println(">>> ACCESO RESTAURADO: admin@eternals.com / admin123");
    }
}
