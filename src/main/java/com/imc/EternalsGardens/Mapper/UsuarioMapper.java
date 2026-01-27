package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.UsuarioRequest;
import com.imc.EternalsGardens.DTO.Response.UsuarioResponse;
import com.imc.EternalsGardens.Entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final ModelMapper modelMapper;

    public Usuario toEntity(UsuarioRequest request) {
        if (request == null) return null;

        Usuario usuario = modelMapper.map(request, Usuario.class);

        if (request.getPassword() != null) {
            usuario.setContraseña(request.getPassword());
        }


        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setFechaUltimaModificacion(LocalDateTime.now());

        return usuario;
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioResponse response = modelMapper.map(usuario, UsuarioResponse.class);

        if (usuario.getRol() != null) {
            response.setRolId(usuario.getRol().getId());
            response.setRolNombre(usuario.getRol().getNombre());
        }

        return response;
    }

    public void updateEntity(UsuarioRequest request, Usuario usuarioExistente) {
        if (request == null || usuarioExistente == null) return;

        usuarioExistente.setNombre(request.getNombre());
        usuarioExistente.setApellidos(request.getApellidos());
        usuarioExistente.setEmail(request.getEmail());
        usuarioExistente.setTelefono(request.getTelefono());
        usuarioExistente.setFechaNacimiento(request.getFechaNacimiento());
        usuarioExistente.setActivo(request.getActivo());



        usuarioExistente.setFechaUltimaModificacion(LocalDateTime.now());
    }

    public List<UsuarioResponse> toResponseList(List<Usuario> usuarios) {
        if (usuarios == null) return List.of();
        return usuarios.stream().map(this::toResponse).toList();
    }
}