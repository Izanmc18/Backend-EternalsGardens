package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.CementerioRequest;
import com.imc.EternalsGardens.DTO.Response.CementerioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICementerioService {

        CementerioResponse crearCementerio(CementerioRequest request);

        List<CementerioResponse> obtenerTodos();

        Page<CementerioResponse> obtenerTodos(Pageable pageable);

        Page<CementerioResponse> obtenerTodos(Pageable pageable, String provincia);

        CementerioResponse obtenerPorId(Integer id);

        CementerioResponse actualizarCementerio(Integer id, CementerioRequest request);

        void eliminarCementerio(Integer id); // Borrado lógico

        List<CementerioResponse> buscarPorLocalizacion(String busqueda);

        @Transactional(readOnly = true)
        List<CementerioResponse> buscarPorNombreOLocalizacion(String busqueda);
}