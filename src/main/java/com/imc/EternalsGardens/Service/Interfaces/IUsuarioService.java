package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.UsuarioRequest;
import com.imc.EternalsGardens.DTO.Response.UsuarioResponse;
import java.util.List;

public interface IUsuarioService {

    org.springframework.data.domain.Page<UsuarioResponse> obtenerTodos(
            org.springframework.data.domain.Pageable pageable, String rol);

    UsuarioResponse obtenerPorId(Integer id);

    UsuarioResponse actualizarUsuario(Integer id, UsuarioRequest request,
            org.springframework.web.multipart.MultipartFile foto);

    UsuarioResponse crearUsuario(UsuarioRequest request, org.springframework.web.multipart.MultipartFile foto);

    void eliminarUsuario(Integer id);

    UsuarioResponse buscarPorDni(String dni);

    UsuarioResponse buscarPorEmail(String email);
}