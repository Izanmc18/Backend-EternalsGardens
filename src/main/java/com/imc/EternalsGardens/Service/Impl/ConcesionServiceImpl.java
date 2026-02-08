package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.ConcesionRequest;
import com.imc.EternalsGardens.DTO.Response.ConcesionResponse;
import com.imc.EternalsGardens.Entity.*;
import com.imc.EternalsGardens.Enum.EstadoConcesionEnum;
import com.imc.EternalsGardens.Enum.EstadoParcelaEnum;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Exception.ReglaNegocioException;
import com.imc.EternalsGardens.Mapper.ConcesionMapper;
import com.imc.EternalsGardens.Repository.*;
import com.imc.EternalsGardens.Service.Interfaces.IConcesionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcesionServiceImpl implements IConcesionService {

    private final ConcesionRepository concesionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CementerioRepository cementerioRepository;
    private final ParcelaRepository parcelaRepository;
    private final ConcesionMapper concesionMapper;

    @Override
    @Transactional
    public ConcesionResponse crearConcesion(ConcesionRequest request) {
        Usuario titular = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario titular no encontrado"));

        Cementerio cementerio = cementerioRepository.findById(request.getCementerioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cementerio no encontrado"));

        // Validar parcelas
        List<Parcela> parcelas = parcelaRepository.findAllById(request.getParcelasIds());
        if (parcelas.isEmpty()) {
            throw new ReglaNegocioException("Debe seleccionar al menos una parcela válida.");
        }

        for (Parcela p : parcelas) {
            // Validar que estén libres
            if (!EstadoParcelaEnum.LIBRE.name().equals(p.getEstado())) {
                throw new ReglaNegocioException(
                        "La parcela " + p.getNumeroIdentificadorUnico() + " no está disponible.");
            }
            // Validar que pertenezcan al cementerio correcto
            if (!p.getZona().getCementerio().getId().equals(cementerio.getId())) {
                throw new ReglaNegocioException(
                        "La parcela " + p.getNumeroIdentificadorUnico() + " no pertenece al cementerio seleccionado.");
            }
        }

        Concesion concesion = concesionMapper.toEntity(request);
        concesion.setUsuario(titular);
        concesion.setCementerio(cementerio);
        concesion.setEstado(EstadoConcesionEnum.ACTIVA.name());

        Concesion concesionGuardada = concesionRepository.save(concesion);

        // Actualizar parcelas
        for (Parcela p : parcelas) {
            p.setConcesion(concesionGuardada);
            p.setEstado(EstadoParcelaEnum.OCUPADA.name());
            parcelaRepository.save(p);
        }

        concesionGuardada.setParcelas(parcelas);
        return concesionMapper.toResponse(concesionGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcesionResponse> obtenerTodas() {
        return concesionMapper.toResponseList(concesionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcesionResponse> obtenerPorTitular(Integer usuarioId) {
        return concesionMapper.toResponseList(concesionRepository.findByUsuarioId(usuarioId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcesionResponse> obtenerPorCementerio(Integer cementerioId) {
        return concesionMapper.toResponseList(concesionRepository.findByCementerioId(cementerioId));
    }

    @Override
    @Transactional(readOnly = true)
    public ConcesionResponse obtenerPorId(Integer id) {
        Concesion concesion = concesionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Concesión no encontrada ID: " + id));
        return concesionMapper.toResponse(concesion);
    }

    @Override
    @Transactional
    public void cambiarEstadoConcesion(Integer id, String nuevoEstado) {
        Concesion concesion = concesionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Concesión no encontrada ID: " + id));
        concesion.setEstado(nuevoEstado);
        concesionRepository.save(concesion);
    }

    @Override
    @Transactional
    public ConcesionResponse comprarParcela(Integer usuarioId, Integer parcelaId) {
        // 1. Buscar Usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        // 2. Buscar Parcela Inicial
        Parcela parcelaInicial = parcelaRepository.findById(parcelaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Parcela no encontrada"));

        // 3. Determinar si es Panteón y qué parcelas comprar
        boolean esPanteon = parcelaInicial.getZona().getTipoZona().getNombre().toUpperCase().contains("PANTEON");
        List<Parcela> parcelasAComprar;

        if (esPanteon) {
            // Si es panteón, se adquieren TODAS las parcelas de esa zona (el panteón
            // completo)
            parcelasAComprar = parcelaRepository.findByZonaId(parcelaInicial.getZona().getId());
        } else {
            // Si es un nicho/parcela normal, solo esa
            parcelasAComprar = List.of(parcelaInicial);
        }

        // 4. Validar disponibilidad de TODAS las parcelas implicada
        for (Parcela p : parcelasAComprar) {
            if (!EstadoParcelaEnum.LIBRE.name().equals(p.getEstado())) {
                throw new ReglaNegocioException(esPanteon
                        ? "El panteón no está disponible completo (algunas partes están ocupadas)."
                        : "La parcela seleccionada ya no está disponible.");
            }
        }

        // 5. Crear Concesión Única
        Concesion concesion = new Concesion();
        concesion.setUsuario(usuario);
        // Usamos el cementerio de la primera parcela (todas son del mismo)
        concesion.setCementerio(parcelaInicial.getZona().getCementerio());
        concesion.setFechaInicio(java.time.LocalDate.now());
        concesion.setFechaFin(java.time.LocalDate.now().plusYears(50)); // Default 50 years
        concesion.setPeriodoAnios(50);

        // Calcular precio: (Base x Número de parcelas) o Fijo?
        // Por ahora mantenemos lógica simple multiplicando el base por el número de
        // unidades para ser justos,
        // o lo dejamos fijo si el precio es 'por concesión'.
        // Asumiremos precio base * cantidad para escalar el coste.
        java.math.BigDecimal precioBase = new java.math.BigDecimal("1500.00");
        java.math.BigDecimal precioFinal = precioBase.multiply(new java.math.BigDecimal(parcelasAComprar.size()));

        concesion.setPrecioPeriodo(precioFinal);
        concesion.setPrecioMantenimientoAnual(
                new java.math.BigDecimal("50.00").multiply(new java.math.BigDecimal(parcelasAComprar.size())));

        concesion.setEstado(EstadoConcesionEnum.ACTIVA.name());
        concesion.setTipoAlta("COMPRA_ONLINE");

        Concesion concesionGuardada = concesionRepository.save(concesion);

        // 6. Actualizar TODAS las parcelas
        for (Parcela p : parcelasAComprar) {
            p.setConcesion(concesionGuardada);
            p.setEstado(EstadoParcelaEnum.RESERVADA.name());
            parcelaRepository.save(p);
        }

        // 7. Vincular lista para retorno correcto
        concesionGuardada.setParcelas(parcelasAComprar);

        return concesionMapper.toResponse(concesionGuardada);
    }
}