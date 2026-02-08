package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.DifuntosEnParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.DifuntosEnParcelaResponse;
import com.imc.EternalsGardens.Entity.DifuntosEnParcela;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DifuntosEnParcelaMapper {

    private final ModelMapper modelMapper;

    public DifuntosEnParcela toEntity(DifuntosEnParcelaRequest request) {
        if (request == null)
            return null;
        return modelMapper.map(request, DifuntosEnParcela.class);
    }

    public DifuntosEnParcelaResponse toResponse(DifuntosEnParcela entity) {
        if (entity == null)
            return null;

        DifuntosEnParcelaResponse response = modelMapper.map(entity, DifuntosEnParcelaResponse.class);

        if (entity.getDifunto() != null) {
            response.setDifuntoId(entity.getDifunto().getId());
            response.setDifuntoNombreCompleto(
                    entity.getDifunto().getNombre() + " " + entity.getDifunto().getApellidos());
            response.setDifuntoDni(entity.getDifunto().getDni());
            // Mapping Fecha Defuncion
            response.setFechaDefuncion(entity.getDifunto().getFechaDefuncion());
            response.setSexo(entity.getDifunto().getSexo());
            response.setCausa(entity.getDifunto().getCausa());
        }

        if (entity.getParcela() != null) {
            response.setParcelaId(entity.getParcela().getId());
            response.setParcelaIdentificador(entity.getParcela().getNumeroIdentificadorUnico());
        }

        return response;
    }

    public void updateEntity(DifuntosEnParcelaRequest request, DifuntosEnParcela entity) {
        if (request == null || entity == null)
            return;
        modelMapper.map(request, entity);
    }

    public List<DifuntosEnParcelaResponse> toResponseList(List<DifuntosEnParcela> lista) {
        if (lista == null)
            return List.of();
        return lista.stream().map(this::toResponse).toList();
    }
}