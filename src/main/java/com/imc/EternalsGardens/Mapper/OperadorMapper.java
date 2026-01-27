package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.OperadorRequest;
import com.imc.EternalsGardens.DTO.Response.OperadorResponse;
import com.imc.EternalsGardens.Entity.OperadorCementerio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OperadorMapper {

    private final ModelMapper modelMapper;

    public OperadorCementerio toEntity(OperadorRequest request) {
        return modelMapper.map(request, OperadorCementerio.class);
    }

    public OperadorResponse toResponse(OperadorCementerio entity) {
        OperadorResponse response = modelMapper.map(entity, OperadorResponse.class);
        if (entity.getRol() != null) {
            response.setRolNombre(entity.getRol().getNombre());
        }
        return response;
    }

    public List<OperadorResponse> toResponseList(List<OperadorCementerio> list) {
        return list.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntity(OperadorRequest request, OperadorCementerio entity) {
        if (request.getNombre() != null) entity.setNombre(request.getNombre());
        if (request.getApellidos() != null) entity.setApellidos(request.getApellidos());
        if (request.getDni() != null) entity.setDni(request.getDni());
        if (request.getTelefono() != null) entity.setTelefono(request.getTelefono());
        if (request.getDireccion() != null) entity.setDireccion(request.getDireccion());
        if (request.getCiudad() != null) entity.setCiudad(request.getCiudad());
        if (request.getCodigoPostal() != null) entity.setCodigoPostal(request.getCodigoPostal());
        if (request.getPais() != null) entity.setPais(request.getPais());
        if (request.getEmpresa() != null) entity.setEmpresa(request.getEmpresa());
        if (request.getFechaNacimiento() != null) entity.setFechaNacimiento(request.getFechaNacimiento());
        if (request.getActivo() != null) entity.setActivo(request.getActivo());

    }
}