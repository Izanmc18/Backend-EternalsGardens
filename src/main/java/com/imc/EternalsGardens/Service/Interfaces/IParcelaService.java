package com.imc.EternalsGardens.Service.Interfaces;

import com.imc.EternalsGardens.DTO.Request.ParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.ParcelaResponse;
import java.util.List;

public interface IParcelaService {
    ParcelaResponse crearParcela(ParcelaRequest request);
    List<ParcelaResponse> obtenerTodas();
    List<ParcelaResponse> obtenerPorZona(Integer zonaId);
    ParcelaResponse obtenerPorId(Integer id);
    ParcelaResponse actualizarParcela(Integer id, ParcelaRequest request);
    void eliminarParcela(Integer id);
}