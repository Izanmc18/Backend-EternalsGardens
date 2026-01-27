package com.imc.EternalsGardens.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DifuntosEnParcelaResponse {
    private Integer id;

    private Integer difuntoId;
    private String difuntoNombreCompleto;
    private String difuntoDni;

    private Integer parcelaId;
    private String parcelaIdentificador;

    private LocalDateTime fechaEnterramiento;
    private LocalDateTime fechaExhumacion;
    private String observaciones;
}