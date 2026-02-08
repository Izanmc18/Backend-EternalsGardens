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
        if (request == null)
            return null;
        Difunto difunto = modelMapper.map(request, Difunto.class);
        // Ensure ID is null for new creations to avoid ModelMapper mapping 'parcelaId'
        // -> 'id'
        difunto.setId(null);
        return difunto;
    }

    public DifuntoResponse toResponse(Difunto difunto) {
        if (difunto == null)
            return null;
        DifuntoResponse response = modelMapper.map(difunto, DifuntoResponse.class);

        if (difunto.getParcela() != null) {
            String ubicacion = String.format("Fila %d - Col %d (%s)",
                    difunto.getParcela().getNumeroFila(),
                    difunto.getParcela().getNumeroColumna(),
                    difunto.getParcela().getNumeroIdentificadorUnico());
            response.setParcelaUbicacion(ubicacion);
            response.setParcelaId(difunto.getParcela().getId());

            if (difunto.getParcela().getZona() != null) {
                response.setZonaId(difunto.getParcela().getZona().getId());
                if (difunto.getParcela().getZona().getCementerio() != null) {
                    response.setCementerioId(difunto.getParcela().getZona().getCementerio().getId());
                }
            }
        }

        return response;
    }

    public void updateEntity(DifuntoRequest request, Difunto entity) {
        if (request == null || entity == null)
            return;
        modelMapper.map(request, entity);
    }

    public List<DifuntoResponse> toResponseList(List<Difunto> difuntos) {
        if (difuntos == null)
            return List.of();
        return difuntos.stream().map(this::toResponse).toList();
    }
}