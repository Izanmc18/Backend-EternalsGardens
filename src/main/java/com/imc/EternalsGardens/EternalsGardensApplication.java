package com.imc.EternalsGardens;

import com.imc.EternalsGardens.Entity.Rol;
import com.imc.EternalsGardens.Entity.Usuario;
import com.imc.EternalsGardens.Enum.RolEnum;
import com.imc.EternalsGardens.Repository.RolRepository;
import com.imc.EternalsGardens.Repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public class EternalsGardensApplication {

    public static void main(String[] args) {
        SpringApplication.run(EternalsGardensApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner initData(RolRepository rolRepository,
                               UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {



            if (rolRepository.count() == 0) {
                Rol adminRole = new Rol(null, RolEnum.ADMINISTRADOR.name(), "Administrador del sistema con acceso total", true);
                Rol operadorRole = new Rol(null, RolEnum.OPERADOR_CEMENTERIO.name(), "Trabajador del cementerio", true);
                Rol clienteRole = new Rol(null, RolEnum.CLIENTE.name(), "Usuario final o titular de concesión", true);

                rolRepository.saveAll(List.of(adminRole, operadorRole, clienteRole));
                System.out.println("✅ Roles creados correctamente en la Base de Datos.");
            }

            // Crear Usuario ADMIN por defecto
            if (usuarioRepository.findByEmail("admin@eternal.com").isEmpty()) {
                Rol rolAdmin = rolRepository.findByNombre(RolEnum.ADMINISTRADOR.name())
                        .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado"));

                Usuario admin = new Usuario();
                admin.setNombre("Super");
                admin.setApellidos("Administrador");
                admin.setEmail("admin@eternal.com");
                admin.setDni("00000000X");
                admin.setTelefono("600000000");
                admin.setDireccion("Calle Principal 1");
                admin.setCiudad("Ciudad Eterna");
                admin.setCodigoPostal("00000");
                admin.setPais("España");
                admin.setContraseña(passwordEncoder.encode("admin123"));
                admin.setActivo(true);
                admin.setRol(rolAdmin);
                admin.setFechaCreacion(LocalDateTime.now());

                // Asegúrate de que tu entidad Usuario tenga este campo (o usa el setter si lo añadiste)
                // Si no lo añadiste en la entidad, comenta esta línea:
                admin.setTipoUsuario("ADMIN");

                usuarioRepository.save(admin);
                System.out.println(" Usuario ADMIN creado: admin@eternal.com / admin123");
            }
        };
    }
}