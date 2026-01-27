package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.DTO.Request.OperadorRequest;
import com.imc.EternalsGardens.DTO.Response.OperadorResponse;
import com.imc.EternalsGardens.Entity.OperadorCementerio;
import com.imc.EternalsGardens.Entity.Rol;
import com.imc.EternalsGardens.Exception.RecursoNoEncontradoException;
import com.imc.EternalsGardens.Exception.ReglaNegocioException;
import com.imc.EternalsGardens.Mapper.OperadorMapper;
import com.imc.EternalsGardens.Repository.OperadorRepository;
import com.imc.EternalsGardens.Repository.RolRepository;
import com.imc.EternalsGardens.Service.Interfaces.IOperadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperadorServiceImpl implements IOperadorService {

    private final OperadorRepository operadorRepository;
    private final RolRepository rolRepository;
    private final OperadorMapper operadorMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OperadorResponse crearOperador(OperadorRequest request) {
        if (operadorRepository.existsByEmail(request.getEmail())) {
            throw new ReglaNegocioException("El email ya está registrado");
        }

        OperadorCementerio operador = operadorMapper.toEntity(request);

        // Encriptar contraseña
        operador.setContraseña(passwordEncoder.encode(request.getPassword()));

        // Asignar rol OPERARIO
        Rol rolOperario = rolRepository.findByNombre("OPERARIO")
                .orElseThrow(() -> new ReglaNegocioException("El rol OPERARIO no existe en la BD"));
        operador.setRol(rolOperario);

        // Asignar tipo usuario (discriminador)
        operador.setTipoUsuario("OPERADOR");

        if (operador.getFechaCreacion() == null) {
            operador.setFechaCreacion(LocalDateTime.now());
        }
        if (operador.getActivo() == null) {
            operador.setActivo(true);
        }

        return operadorMapper.toResponse(operadorRepository.save(operador));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OperadorResponse> obtenerTodos() {
        return operadorMapper.toResponseList(operadorRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public OperadorResponse obtenerPorId(Integer id) {
        OperadorCementerio operador = operadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Operador no encontrado con ID: " + id));
        return operadorMapper.toResponse(operador);
    }

    @Override
    @Transactional
    public OperadorResponse actualizarOperadorAdmin(Integer id, OperadorRequest request) {
        OperadorCementerio operador = operadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Operador no encontrado con ID: " + id));

        // Actualizamos datos generales
        operadorMapper.updateEntity(request, operador);

        // Si el admin envía contraseña nueva, la actualizamos
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            operador.setContraseña(passwordEncoder.encode(request.getPassword()));
        }

        return operadorMapper.toResponse(operadorRepository.save(operador));
    }

    @Override
    @Transactional
    public OperadorResponse actualizarPerfilPropio(String emailUsuario, OperadorRequest request) {
        OperadorCementerio operador = operadorRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Operador no encontrado: " + emailUsuario));

        // El operador NO puede cambiar su propio Rol ni su estado Activo a false (normalmente)
        // Aquí actualizamos sus datos personales y empresa
        if (request.getNombre() != null) operador.setNombre(request.getNombre());
        if (request.getApellidos() != null) operador.setApellidos(request.getApellidos());
        if (request.getTelefono() != null) operador.setTelefono(request.getTelefono());
        if (request.getDireccion() != null) operador.setDireccion(request.getDireccion());
        if (request.getEmpresa() != null) operador.setEmpresa(request.getEmpresa());

        // Opcional: Permitir cambio de password
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            operador.setContraseña(passwordEncoder.encode(request.getPassword()));
        }

        return operadorMapper.toResponse(operadorRepository.save(operador));
    }

    @Override
    @Transactional
    public void eliminarOperador(Integer id) {
        if (!operadorRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Operador no encontrado con ID: " + id);
        }
        operadorRepository.deleteById(id);
    }
}