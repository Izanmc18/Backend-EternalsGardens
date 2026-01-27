package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.ZonaRequest;
import com.imc.EternalsGardens.DTO.Response.ZonaResponse;
import com.imc.EternalsGardens.Entity.Zona;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ZonaMapper {

    private final ModelMapper modelMapper;

    public Zona toEntity(ZonaRequest request) {
        if (request == null) return null;
        // El Service se encargará de buscar y setear Cementerio y TipoZona
        return modelMapper.map(request, Zona.class);
    }

    public ZonaResponse toResponse(Zona zona) {
        if (zona == null) return null;

        ZonaResponse response = modelMapper.map(zona, ZonaResponse.class);

        // Mapeo manual de relaciones para facilitar la vida al Frontend
        if (zona.getCementerio() != null) {
            response.setCementerioId(zona.getCementerio().getId());
            response.setCementerioNombre(zona.getCementerio().getNombre());
        }
        if (zona.getTipoZona() != null) {
            response.setTipoZonaId(zona.getTipoZona().getId());
            response.setTipoZonaNombre(zona.getTipoZona().getNombre());
        }

        return response;
    }

    public void updateEntity(ZonaRequest request, Zona zonaExistente) {
        if (request == null || zonaExistente == null) return;
        modelMapper.map(request, zonaExistente);
    }

    public List<ZonaResponse> toResponseList(List<Zona> zonas) {
        if (zonas == null) return List.of();
        return zonas.stream().map(this::toResponse).toList();
    }
}