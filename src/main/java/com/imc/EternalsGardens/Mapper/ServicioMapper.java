package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.ServicioRequest;
import com.imc.EternalsGardens.DTO.Response.ServicioResponse;
import com.imc.EternalsGardens.Entity.Servicio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServicioMapper {

    private final ModelMapper modelMapper;

    public Servicio toEntity(ServicioRequest request) {
        if (request == null)
            return null;
        return modelMapper.map(request, Servicio.class);
    }

    public ServicioResponse toResponse(Servicio servicio) {
        if (servicio == null)
            return null;

        ServicioResponse response = modelMapper.map(servicio, ServicioResponse.class);

        if (servicio.getTipoZona() != null) {
            response.setTipoZonaId(servicio.getTipoZona().getId());
            response.setTipoZonaNombre(servicio.getTipoZona().getNombre());
        }

        return response;
    }

    public void updateEntity(ServicioRequest request, Servicio entity) {
        if (request == null || entity == null)
            return;

        // Mapeo manual para evitar que ModelMapper toque el ID u otros campos sensibles
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setPrecioActual(request.getPrecioActual());
        entity.setActivo(request.getActivo());
    }

    public List<ServicioResponse> toResponseList(List<Servicio> servicios) {
        if (servicios == null)
            return List.of();
        return servicios.stream().map(this::toResponse).toList();
    }
}