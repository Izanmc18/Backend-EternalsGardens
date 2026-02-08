package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.DifuntoRequest;
import com.imc.EternalsGardens.DTO.Response.DifuntoResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface IDifuntoService {
        DifuntoResponse crearDifunto(DifuntoRequest request, MultipartFile file);

        List<DifuntoResponse> obtenerTodos();

        List<DifuntoResponse> buscarDifuntos(String query);

        DifuntoResponse obtenerPorId(Integer id);

        DifuntoResponse actualizarDifunto(Integer id, DifuntoRequest request, MultipartFile file);

        void eliminarDifunto(Integer id);

        org.springframework.data.domain.Page<DifuntoResponse> obtenerPorZona(
                        Integer zonaId,
                        org.springframework.data.domain.Pageable pageable);

        org.springframework.data.domain.Page<DifuntoResponse> obtenerPorCementerio(
                        Integer cementerioId,
                        org.springframework.data.domain.Pageable pageable);

        List<DifuntoResponse> obtenerPorUsuario(Integer usuarioId);
}