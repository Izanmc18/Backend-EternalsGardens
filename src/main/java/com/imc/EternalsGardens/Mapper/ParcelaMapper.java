package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.ParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.ParcelaResponse;
import com.imc.EternalsGardens.Entity.Parcela;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParcelaMapper {

    private final ModelMapper modelMapper;

    public Parcela toEntity(ParcelaRequest request) {
        if (request == null) return null;
        return modelMapper.map(request, Parcela.class);
    }

    public ParcelaResponse toResponse(Parcela parcela) {
        if (parcela == null) return null;

        ParcelaResponse response = modelMapper.map(parcela, ParcelaResponse.class);

        if (parcela.getZona() != null) {
            response.setZonaId(parcela.getZona().getId());
            response.setZonaNombre(parcela.getZona().getNombre());
            if (parcela.getZona().getCementerio() != null) {
                response.setCementerioNombre(parcela.getZona().getCementerio().getNombre());
            }
        }

        if (parcela.getTipoZona() != null) {
            response.setTipoZonaId(parcela.getTipoZona().getId());
            response.setTipoZonaNombre(parcela.getTipoZona().getNombre());
        }

        if (parcela.getConcesion() != null) {
            response.setConcesionId(parcela.getConcesion().getId());
        }

        return response;
    }

    public void updateEntity(ParcelaRequest request, Parcela entity) {
        if (request == null || entity == null) return;
        modelMapper.map(request, entity);
    }

    public List<ParcelaResponse> toResponseList(List<Parcela> parcelas) {
        if (parcelas == null) return List.of();
        return parcelas.stream().map(this::toResponse).toList();
    }
}