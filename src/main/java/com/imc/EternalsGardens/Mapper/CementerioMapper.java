package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.CementerioRequest;
import com.imc.EternalsGardens.DTO.Response.CementerioResponse;
import com.imc.EternalsGardens.Entity.Cementerio;
import com.imc.EternalsGardens.Repository.ParcelaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CementerioMapper {

    private final ModelMapper modelMapper;
    private final ParcelaRepository parcelaRepository;

    public Cementerio toEntity(CementerioRequest request) {
        if (request == null)
            return null;

        Cementerio cementerio = modelMapper.map(request, Cementerio.class);

        // NOTA: El Service buscará el Usuario responsable y hará:
        // cementerio.setResponsable(usuarioEncontrado);

        cementerio.setFechaCreacion(LocalDateTime.now());
        return cementerio;
    }

    public CementerioResponse toResponse(Cementerio cementerio) {
        if (cementerio == null)
            return null;

        CementerioResponse response = modelMapper.map(cementerio, CementerioResponse.class);

        // Mapeo de datos del responsable (si existe)
        if (cementerio.getResponsable() != null) {
            response.setResponsableId(cementerio.getResponsable().getId());
            response.setResponsableNombre(
                    cementerio.getResponsable().getNombre() + " " +
                            cementerio.getResponsable().getApellidos());
        }

        // Calcular capacidad total y ocupación actual
        Long capacidadTotal = parcelaRepository.contarPorCementerio(cementerio.getId());
        Long parcelasLibres = parcelaRepository.contarDisponiblesPorCementerio(cementerio.getId());
        Long ocupacionActual = capacidadTotal - parcelasLibres; // OCUPADA + RESERVADA + MANTENIMIENTO

        response.setCapacidadTotal(capacidadTotal.intValue());
        response.setOcupacionActual(ocupacionActual.intValue());

        return response;
    }

    public void updateEntity(CementerioRequest request, Cementerio cementerioExistente) {
        if (request == null || cementerioExistente == null)
            return;

        // Mapeo MANUAL para evitar que ModelMapper sobrescriba el ID con null
        // u otros efectos colaterales no deseados.
        cementerioExistente.setNombre(request.getNombre());
        cementerioExistente.setMunicipio(request.getMunicipio());
        cementerioExistente.setProvincia(request.getProvincia());
        cementerioExistente.setCodigoPostal(request.getCodigoPostal());
        cementerioExistente.setCoordenadaX(request.getCoordenadaX());
        cementerioExistente.setCoordenadaY(request.getCoordenadaY());
        cementerioExistente.setTelefono(request.getTelefono());
        cementerioExistente.setEmail(request.getEmail());
        cementerioExistente.setActivo(request.getActivo());

        cementerioExistente.setFotoUrl(request.getFotoUrl());

        // Campos Konva.js
        cementerioExistente.setMapaAncho(request.getMapaAncho());
        cementerioExistente.setMapaAlto(request.getMapaAlto());
        cementerioExistente.setMapaEscala(request.getMapaEscala());
        cementerioExistente.setImagenFondo(request.getImagenFondo());

        // El cambio de responsable lo gestiona el Service
    }

    public List<CementerioResponse> toResponseList(List<Cementerio> cementerios) {
        if (cementerios == null)
            return List.of();
        return cementerios.stream().map(this::toResponse).toList();
    }
}