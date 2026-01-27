package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.ServicioParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.ServicioParcelaResponse;
import com.imc.EternalsGardens.Entity.ServicioParcela;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServicioParcelaMapper {

    private final ModelMapper modelMapper;

    public ServicioParcela toEntity(ServicioParcelaRequest request) {
        if (request == null) return null;
        return modelMapper.map(request, ServicioParcela.class);
    }

    public ServicioParcelaResponse toResponse(ServicioParcela entity) {
        if (entity == null) return null;

        ServicioParcelaResponse response = modelMapper.map(entity, ServicioParcelaResponse.class);

        if (entity.getServicio() != null) {
            response.setServicioId(entity.getServicio().getId());
            response.setServicioNombre(entity.getServicio().getNombre());

        }

        if (entity.getParcela() != null) {
            response.setParcelaId(entity.getParcela().getId());
            response.setParcelaIdentificador(entity.getParcela().getNumeroIdentificadorUnico());
        }

        return response;
    }

    public void updateEntity(ServicioParcelaRequest request, ServicioParcela entity) {
        if (request == null || entity == null) return;
        modelMapper.map(request, entity);
    }

    public List<ServicioParcelaResponse> toResponseList(List<ServicioParcela> lista) {
        if (lista == null) return List.of();
        return lista.stream().map(this::toResponse).toList();
    }
}