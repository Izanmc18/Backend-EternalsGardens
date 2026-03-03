package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.UsuarioRequest;
import com.imc.EternalsGardens.DTO.Response.UsuarioResponse;
import com.imc.EternalsGardens.Entity.Cementerio;
import com.imc.EternalsGardens.Entity.Rol;
import com.imc.EternalsGardens.Entity.Usuario;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Exception.ReglaNegocioException;
import com.imc.EternalsGardens.Mapper.UsuarioMapper;
import com.imc.EternalsGardens.Repository.CementerioRepository;
import com.imc.EternalsGardens.Repository.RolRepository;
import com.imc.EternalsGardens.Repository.UsuarioRepository;
import com.imc.EternalsGardens.Service.Interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CementerioRepository cementerioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponse crearUsuario(UsuarioRequest request, MultipartFile foto) {
        // Validaciones manuales de contraseña
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ReglaNegocioException("La contraseña es obligatoria");
        }
        if (request.getPassword().length() < 8) {
            throw new ReglaNegocioException("La contraseña debe tener al menos 8 caracteres");
        }

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ReglaNegocioException("El email " + request.getEmail() + " ya está en uso.");
        }
        if (usuarioRepository.findByDni(request.getDni()).isPresent()) {
            throw new ReglaNegocioException("El DNI " + request.getDni() + " ya está registrado.");
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con ID: " + request.getRolId()));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setDni(request.getDni());
        usuario.setTelefono(request.getTelefono());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setDireccion("Sin dirección");
        usuario.setCiudad("Sin ciudad");
        usuario.setCodigoPostal("00000");
        usuario.setPais("España");

        if (rol.getNombre().equals("OPERADOR_CEMENTERIO")) {
            if (request.getCementerioId() == null) {
                throw new ReglaNegocioException("Un Operador de Cementerio debe tener un cementerio asignado.");
            }
            Cementerio cementerio = cementerioRepository.findById(request.getCementerioId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Cementerio no encontrado con ID: " + request.getCementerioId()));
            usuario.setCementerio(cementerio);
        } else {
            usuario.setCementerio(null);
        }

        if (foto != null && !foto.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + foto.getOriginalFilename();

                String uploadDir = "c:/Users/Izan/DAW/2DAW/EternalsGardens/eternals-gardens-front/src/assets/images/fotosperfil/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                usuario.setFotoUrl("/assets/images/fotosperfil/" + fileName);
            } catch (Exception e) {
                throw new ReglaNegocioException("Error al guardar la foto de perfil: " + e.getMessage());
            }
        }

        usuario.setContraseña(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);
        usuario.setActivo(request.getActivo() != null ? request.getActivo() : true);
        usuario.setTipoUsuario(rol.getNombre());

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> obtenerTodos(Pageable pageable, String rol) {
        Page<Usuario> usuariosPage;

        if (rol != null && !rol.isBlank()) {

            usuariosPage = usuarioRepository.findByRolNombreContainingIgnoreCase(rol, pageable);
        } else {
            usuariosPage = usuarioRepository.findAll(pageable);
        }

        return usuariosPage.map(usuarioMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse actualizarUsuario(Integer id, UsuarioRequest request,
            MultipartFile foto) {
        System.out.println("DEBUG: Service actualizarUsuario. Foto: " + (foto != null ? "present" : "null"));

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));

        if (!usuarioExistente.getEmail().equalsIgnoreCase(request.getEmail())) {
            Optional<Usuario> userConMismoEmail = usuarioRepository.findByEmail(request.getEmail());
            if (userConMismoEmail.isPresent()) {
                throw new ReglaNegocioException("El email " + request.getEmail() + " ya está en uso por otro usuario.");
            }
        }

        if (!usuarioExistente.getDni().equalsIgnoreCase(request.getDni())) {
            Optional<Usuario> userConMismoDni = usuarioRepository.findByDni(request.getDni());
            if (userConMismoDni.isPresent()) {
                throw new ReglaNegocioException("El DNI " + request.getDni() + " ya está registrado en el sistema.");
            }
        }

        if (request.getRolId() != null) {
            // Seguridad: Solo ADMIN puede cambiar roles
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth != null && auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));

            if (!isAdmin) {
                // Si no es admin y intenta cambiar rol -> Error
                if (!request.getRolId().equals(usuarioExistente.getRol().getId())) {
                    throw new ReglaNegocioException("No tienes permisos para cambiar tu rol.");
                }
                // Si es el mismo rol, ignoramos o dejamos pasar
            } else {
                Rol nuevoRol = rolRepository.findById(request.getRolId())
                        .orElseThrow(
                                () -> new RecursoNoEncontradoException(
                                        "Rol no encontrado con ID: " + request.getRolId()));
                usuarioExistente.setRol(nuevoRol);
                usuarioExistente.setTipoUsuario(nuevoRol.getNombre());
            }
        }

        // Gestionar cambio de cementerio (si se envía o si el rol lo requiere)
        if (usuarioExistente.getRol().getNombre().equals("OPERADOR_CEMENTERIO")) {
            if (request.getCementerioId() != null) {
                Cementerio cementerio = cementerioRepository.findById(request.getCementerioId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Cementerio no encontrado con ID: " + request.getCementerioId()));
                usuarioExistente.setCementerio(cementerio);
            } else if (usuarioExistente.getCementerio() == null) {
                // Si es operador y no tiene cementerio, y no s envia uno nuevo -> Error
                throw new ReglaNegocioException("Un Operador de Cementerio debe tener un cementerio asignado.");
            }
        } else {
            // Si no es operador, eliminar asignación de cementerio
            usuarioExistente.setCementerio(null);
        }

        // Lógica de actualización de contraseña (Opcional en update)
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (request.getPassword().length() < 8) {
                throw new ReglaNegocioException("La contraseña debe tener al menos 8 caracteres");
            }
            usuarioExistente.setContraseña(passwordEncoder.encode(request.getPassword()));
        }

        usuarioMapper.updateEntity(request, usuarioExistente);

        if (foto != null && !foto.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + foto.getOriginalFilename();

                String uploadDir = "c:/Users/Izan/DAW/2DAW/EternalsGardens/eternals-gardens-front/src/assets/images/fotosperfil/";
                Path uploadPath2 = Paths.get(uploadDir);

                System.out.println("DEBUG: Upload Path resolved to: " + uploadPath2.toString());

                if (!Files.exists(uploadPath2)) {
                    Files.createDirectories(uploadPath2);
                    System.out.println("DEBUG: Created directory: " + uploadPath2.toString());
                }

                Path filePath2 = uploadPath2.resolve(fileName);
                Files.copy(foto.getInputStream(), filePath2, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("DEBUG: File saved to: " + filePath2.toString());
                System.out.println("DEBUG: File exists after save? " + Files.exists(filePath2));

                usuarioExistente.setFotoUrl("/assets/images/fotosperfil/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ReglaNegocioException("Error al guardar la foto de perfil: " + e.getMessage());
            }
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toResponse(usuarioGuardado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));

        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorDni(String dni) {
        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con DNI: " + dni));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con Email: " + email));
        return usuarioMapper.toResponse(usuario);
    }
}