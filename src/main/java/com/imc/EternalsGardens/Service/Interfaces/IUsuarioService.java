package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.UsuarioRequest;
import com.imc.EternalsGardens.DTO.Response.UsuarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface IUsuarioService {

        Page<UsuarioResponse> obtenerTodos(Pageable pageable, String rol);

        UsuarioResponse obtenerPorId(Integer id);

        UsuarioResponse actualizarUsuario(Integer id, UsuarioRequest request, MultipartFile foto);

        UsuarioResponse crearUsuario(UsuarioRequest request, MultipartFile foto);

        void eliminarUsuario(Integer id);

        UsuarioResponse buscarPorDni(String dni);

        UsuarioResponse buscarPorEmail(String email);
}