package com.imc.EternalsGardens.Mapper;

import com.imc.EternalsGardens.DTO.Request.ParcelaRequest;
import com.imc.EternalsGardens.DTO.Response.ParcelaResponse;
import com.imc.EternalsGardens.Entity.Parcela;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParcelaMapper {

    private final ModelMapper modelMapper;

    public Parcela toEntity(ParcelaRequest request) {
        if (request == null)
            return null;

        Parcela parcela = new Parcela();
        parcela.setNumeroFila(request.getNumeroFila());
        parcela.setNumeroColumna(request.getNumeroColumna());
        parcela.setNumeroIdentificadorUnico(request.getNumeroIdentificadorUnico());
        parcela.setEstado(request.getEstado());

        // Relaciones (zona, tipoZona, concesion) se gestionan en el Servicio
        return parcela;
    }

    // toResponse se mantiene con ModelMapper porque es más plano y Entity->DTO
    // suele funcionar bien
    public ParcelaResponse toResponse(Parcela parcela) {
        if (parcela == null)
            return null;

        ParcelaResponse response = modelMapper.map(parcela, ParcelaResponse.class);

        if (parcela.getZona() != null) {
            response.setZonaId(parcela.getZona().getId());
            response.setZonaNombre(parcela.getZona().getNombre());
            if (parcela.getZona().getCementerio() != null) {
                response.setCementerioNombre(parcela.getZona().getCementerio().getNombre());
                response.setCementerioId(parcela.getZona().getCementerio().getId());
            }
        }

        if (parcela.getTipoZona() != null) {
            response.setTipoZonaId(parcela.getTipoZona().getId());
            response.setTipoZonaNombre(parcela.getTipoZona().getNombre());
        }

        if (parcela.getConcesion() != null) {
            response.setConcesionId(parcela.getConcesion().getId());
        }

        return response;
    }

    public void updateEntity(ParcelaRequest request, Parcela entity) {
        if (request == null || entity == null)
            return;

        if (request.getNumeroFila() != null)
            entity.setNumeroFila(request.getNumeroFila());
        if (request.getNumeroColumna() != null)
            entity.setNumeroColumna(request.getNumeroColumna());
        if (request.getNumeroIdentificadorUnico() != null)
            entity.setNumeroIdentificadorUnico(request.getNumeroIdentificadorUnico());
        if (request.getEstado() != null)
            entity.setEstado(request.getEstado());

        // Relaciones se actualizan en el Servicio si es necesario
    }

    public List<ParcelaResponse> toResponseList(List<Parcela> parcelas) {
        if (parcelas == null)
            return List.of();
        return parcelas.stream().map(this::toResponse).toList();
    }
}