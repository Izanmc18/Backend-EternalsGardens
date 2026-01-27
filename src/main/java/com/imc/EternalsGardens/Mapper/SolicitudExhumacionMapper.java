package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.SolicitudExhumacionRequest;
import com.imc.EternalsGardens.DTO.Response.SolicitudExhumacionResponse;
import com.imc.EternalsGardens.Entity.SolicitudExhumacion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SolicitudExhumacionMapper {

    private final ModelMapper modelMapper;

    public SolicitudExhumacion toEntity(SolicitudExhumacionRequest request) {
        if (request == null) return null;
        return modelMapper.map(request, SolicitudExhumacion.class);
    }

    public SolicitudExhumacionResponse toResponse(SolicitudExhumacion entity) {
        if (entity == null) return null;

        SolicitudExhumacionResponse response = modelMapper.map(entity, SolicitudExhumacionResponse.class);

        if (entity.getDifunto() != null) {

            response.setDifuntoId(entity.getDifunto().getId());
            response.setDifuntoNombre(entity.getDifunto().getNombre() + " " + entity.getDifunto().getApellidos());
        }

        if (entity.getUsuario() != null) {
            response.setUsuarioSolicitanteId(entity.getUsuario().getId());
            response.setUsuarioSolicitanteNombre(entity.getUsuario().getNombre());
        }

        if (entity.getUsuarioAprobador() != null) {
            response.setUsuarioAprobadorNombre(entity.getUsuarioAprobador().getNombre());
        }

        return response;
    }

    public void updateEntity(SolicitudExhumacionRequest request, SolicitudExhumacion entity) {
        if (request == null || entity == null) return;
        modelMapper.map(request, entity);
    }

    public List<SolicitudExhumacionResponse> toResponseList(List<SolicitudExhumacion> lista) {
        if (lista == null) return List.of();
        return lista.stream().map(this::toResponse).toList();
    }
}