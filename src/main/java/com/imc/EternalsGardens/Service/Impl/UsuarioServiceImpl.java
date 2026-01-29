package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.UsuarioRequest;
import com.imc.EternalsGardens.DTO.Response.UsuarioResponse;
import com.imc.EternalsGardens.Entity.Rol;
import com.imc.EternalsGardens.Entity.Usuario;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Exception.ReglaNegocioException;
import com.imc.EternalsGardens.Mapper.UsuarioMapper;
import com.imc.EternalsGardens.Repository.RolRepository;
import com.imc.EternalsGardens.Repository.UsuarioRepository;
import com.imc.EternalsGardens.Service.Interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
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
        usuario.setDireccion("Sin dirección"); // Default or add to Request
        usuario.setCiudad("Sin ciudad");
        usuario.setCodigoPostal("00000");
        usuario.setPais("España");

        usuario.setContraseña(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);
        usuario.setActivo(request.getActivo() != null ? request.getActivo() : true);
        usuario.setTipoUsuario(rol.getNombre()); // Legacy field

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarioMapper.toResponseList(usuarios);
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
    public UsuarioResponse actualizarUsuario(Integer id, UsuarioRequest request) {
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
            Rol nuevoRol = rolRepository.findById(request.getRolId())
                    .orElseThrow(
                            () -> new RecursoNoEncontradoException("Rol no encontrado con ID: " + request.getRolId()));
            usuarioExistente.setRol(nuevoRol);
        }

        usuarioMapper.updateEntity(request, usuarioExistente);

        Usuario usuarioGuardado = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toResponse(usuarioGuardado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorDni(String dni) {
        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con DNI: " + dni));
        return usuarioMapper.toResponse(usuario);
    }
}