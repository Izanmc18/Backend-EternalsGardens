package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.OperadorRequest;
import com.imc.EternalsGardens.DTO.Response.OperadorResponse;
import java.util.List;

public interface IOperadorService {

    OperadorResponse crearOperador(OperadorRequest request);

    List<OperadorResponse> obtenerTodos();

    OperadorResponse obtenerPorId(Integer id);

    // Método para que el admin actualice cualquier operador
    OperadorResponse actualizarOperadorAdmin(Integer id, OperadorRequest request);

    // Método para que el operador se actualice a sí mismo (perfil)
    OperadorResponse actualizarPerfilPropio(String emailUsuario, OperadorRequest request);

    void eliminarOperador(Integer id);
}