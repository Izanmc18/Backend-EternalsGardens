package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.DifuntoRequest;
import com.imc.EternalsGardens.DTO.Response.DifuntoResponse;
import com.imc.EternalsGardens.Entity.Difunto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DifuntoMapper {

    private final ModelMapper modelMapper;

    public Difunto toEntity(DifuntoRequest request) {
        if (request == null) return null;
        return modelMapper.map(request, Difunto.class);
    }

    public DifuntoResponse toResponse(Difunto difunto) {
        if (difunto == null) return null;
        return modelMapper.map(difunto, DifuntoResponse.class);
    }

    public void updateEntity(DifuntoRequest request, Difunto entity) {
        if (request == null || entity == null) return;
        modelMapper.map(request, entity);
    }

    public List<DifuntoResponse> toResponseList(List<Difunto> difuntos) {
        if (difuntos == null) return List.of();
        return difuntos.stream().map(this::toResponse).toList();
    }
}