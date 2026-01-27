package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.CementerioRequest;
import com.imc.EternalsGardens.DTO.Response.CementerioResponse;
import com.imc.EternalsGardens.Entity.Cementerio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CementerioMapper {

    private final ModelMapper modelMapper;

    public Cementerio toEntity(CementerioRequest request) {
        if (request == null) return null;

        Cementerio cementerio = modelMapper.map(request, Cementerio.class);

        // NOTA: El Service buscará el Usuario responsable y hará:
        // cementerio.setResponsable(usuarioEncontrado);

        cementerio.setFechaCreacion(LocalDateTime.now());
        return cementerio;
    }

    public CementerioResponse toResponse(Cementerio cementerio) {
        if (cementerio == null) return null;

        CementerioResponse response = modelMapper.map(cementerio, CementerioResponse.class);

        // Mapeo de datos del responsable (si existe)
        if (cementerio.getResponsable() != null) {
            response.setResponsableId(cementerio.getResponsable().getId());
            response.setResponsableNombre(
                    cementerio.getResponsable().getNombre() + " " +
                            cementerio.getResponsable().getApellidos()
            );
        }

        return response;
    }

    public void updateEntity(CementerioRequest request, Cementerio cementerioExistente) {
        if (request == null || cementerioExistente == null) return;

        // ModelMapper actualiza los campos coincidentes
        modelMapper.map(request, cementerioExistente);

        // El cambio de responsable lo gestiona el Service
    }

    public List<CementerioResponse> toResponseList(List<Cementerio> cementerios) {
        if (cementerios == null) return List.of();
        return cementerios.stream().map(this::toResponse).toList();
    }
}