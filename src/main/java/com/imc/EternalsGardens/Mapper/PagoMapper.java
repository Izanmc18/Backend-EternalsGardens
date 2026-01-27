package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.PagoRequest;
import com.imc.EternalsGardens.DTO.Response.PagoResponse;
import com.imc.EternalsGardens.Entity.Pago;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PagoMapper {

    private final ModelMapper modelMapper;

    public Pago toEntity(PagoRequest request) {
        if (request == null) return null;
        return modelMapper.map(request, Pago.class);
    }

    public PagoResponse toResponse(Pago pago) {
        if (pago == null) return null;

        PagoResponse response = modelMapper.map(pago, PagoResponse.class);

        if (pago.getUsuario() != null) {
            response.setUsuarioId(pago.getUsuario().getId());
            response.setUsuarioNombre(pago.getUsuario().getNombre());
        }

        if (pago.getConcesion() != null) {
            response.setConcesionId(pago.getConcesion().getId());
        }

        return response;
    }

    public void updateEntity(PagoRequest request, Pago entity) {
        if (request == null || entity == null) return;
        modelMapper.map(request, entity);
    }

    public List<PagoResponse> toResponseList(List<Pago> pagos) {
        if (pagos == null) return List.of();
        return pagos.stream().map(this::toResponse).toList();
    }
}