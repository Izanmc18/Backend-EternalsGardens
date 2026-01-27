package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.ConcesionRequest;
import com.imc.EternalsGardens.DTO.Response.ConcesionResponse;
import com.imc.EternalsGardens.Entity.Concesion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcesionMapper {

    private final ModelMapper modelMapper;

    public Concesion toEntity(ConcesionRequest request) {
        if (request == null) return null;
        return modelMapper.map(request, Concesion.class);
    }

    public ConcesionResponse toResponse(Concesion concesion) {
        if (concesion == null) return null;

        ConcesionResponse response = modelMapper.map(concesion, ConcesionResponse.class);

        if (concesion.getUsuario() != null) {
            response.setUsuarioId(concesion.getUsuario().getId());
            response.setUsuarioNombreCompleto(
                    concesion.getUsuario().getNombre() + " " + concesion.getUsuario().getApellidos()
            );
        }

        if (concesion.getCementerio() != null) {
            response.setCementerioId(concesion.getCementerio().getId());
            response.setCementerioNombre(concesion.getCementerio().getNombre());
        }

        return response;
    }

    public void updateEntity(ConcesionRequest request, Concesion entity) {
        if (request == null || entity == null) return;
        modelMapper.map(request, entity);
    }

    public List<ConcesionResponse> toResponseList(List<Concesion> lista) {
        if (lista == null) return List.of();
        return lista.stream().map(this::toResponse).toList();
    }
}