package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.TipoZonaRequest;
import com.imc.EternalsGardens.DTO.Response.TipoZonaResponse;
import com.imc.EternalsGardens.Entity.TipoZona;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TipoZonaMapper {

    private final ModelMapper modelMapper;

    public TipoZona toEntity(TipoZonaRequest request) {
        if (request == null) return null;
        return modelMapper.map(request, TipoZona.class);
    }

    public TipoZonaResponse toResponse(TipoZona tipoZona) {
        if (tipoZona == null) return null;
        return modelMapper.map(tipoZona, TipoZonaResponse.class);
    }

    public void updateEntity(TipoZonaRequest request, TipoZona tipoZonaExistente) {
        if (request == null || tipoZonaExistente == null) return;
        modelMapper.map(request, tipoZonaExistente);
    }

    public List<TipoZonaResponse> toResponseList(List<TipoZona> tipoZonas) {
        if (tipoZonas == null) return List.of();
        return tipoZonas.stream().map(this::toResponse).toList();
    }
}