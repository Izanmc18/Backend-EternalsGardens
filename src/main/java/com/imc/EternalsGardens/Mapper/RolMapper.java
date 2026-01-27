package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.RolRequest;
import com.imc.EternalsGardens.DTO.Response.RolResponse;
import com.imc.EternalsGardens.Entity.Rol;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RolMapper {

    private final ModelMapper modelMapper;

    public Rol toEntity(RolRequest request) {
        if (request == null) return null;
        return modelMapper.map(request, Rol.class);
    }

    public RolResponse toResponse(Rol rol) {
        if (rol == null) return null;
        return modelMapper.map(rol, RolResponse.class);
    }

    public void updateEntity(RolRequest request, Rol rolExistente) {
        if (request == null || rolExistente == null) return;
        modelMapper.map(request, rolExistente);
    }

    public List<RolResponse> toResponseList(List<Rol> roles) {
        if (roles == null) return List.of();
        return roles.stream().map(this::toResponse).toList();
    }
}