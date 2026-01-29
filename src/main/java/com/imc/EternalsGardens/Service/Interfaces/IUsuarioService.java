package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.UsuarioRequest;
import com.imc.EternalsGardens.DTO.Response.UsuarioResponse;
import java.util.List;

public interface IUsuarioService {

    List<UsuarioResponse> obtenerTodos();

    UsuarioResponse obtenerPorId(Integer id);

    UsuarioResponse actualizarUsuario(Integer id, UsuarioRequest request);

    UsuarioResponse crearUsuario(UsuarioRequest request);

    void eliminarUsuario(Integer id);

    UsuarioResponse buscarPorDni(String dni);
}