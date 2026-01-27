package com.imc.EternalsGardens.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioParcelaRequest {

    @NotNull
    private Integer servicioId;

    @NotNull
    private Integer parcelaId;

    @NotNull
    private LocalDateTime fechaProgramada;

    private String estado;

    private String observaciones;
}