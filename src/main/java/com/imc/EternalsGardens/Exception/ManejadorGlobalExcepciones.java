package com.imc.EternalsGardens.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    // 1. Recurso No Encontrado (404)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex,
            HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 2. Reglas de Negocio (400)
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> manejarReglaNegocio(ReglaNegocioException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 3. Credenciales Inválidas (401)
    @ExceptionHandler({ BadCredentialsException.class, UsernameNotFoundException.class })
    public ResponseEntity<ApiError> manejarCredencialesInvalidas(Exception ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.UNAUTHORIZED,
                "Credenciales de acceso incorrectas",
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // 4. Acceso Denegado / Falta de Permisos (403) - ¡NUEVO!
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> manejarAccesoDenegado(AccessDeniedException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.FORBIDDEN,
                "No tienes permisos suficientes para realizar esta acción.",
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // 5. Validaciones de Formularios (@Valid) (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidaciones(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String mensajesError = ex.getBindingResult().getAllErrors().stream()
                .map(objectError -> {
                    if (objectError instanceof FieldError) {
                        return ((FieldError) objectError).getField() + ": " + objectError.getDefaultMessage();
                    }
                    return objectError.getDefaultMessage();
                })
                .collect(Collectors.joining(", "));

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Error en los datos enviados: " + mensajesError,
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 6. Integridad de Datos / Duplicados (409) - ¡NUEVO!
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarViolacionIntegridad(DataIntegrityViolationException ex,
            HttpServletRequest request) {
        String mensaje = "Error de integridad en la base de datos.";
        if (ex.getCause() != null && ex.getCause().getMessage() != null
                && ex.getCause().getMessage().toLowerCase().contains("duplicate")) {
            mensaje = "Ya existe un registro con esos datos (posiblemente email, DNI o código duplicado).";
        } else if (ex.getRootCause() != null) {
            mensaje += " Detalle: " + ex.getRootCause().getMessage();
        }

        ApiError error = new ApiError(
                HttpStatus.CONFLICT,
                mensaje,
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 7. JSON Mal formado (400) - ¡NUEVO!
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonMalFormado(HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                "El formato del JSON es incorrecto o contiene valores inválidos (revisa fechas, números o enums).",
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 8. Error General (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarExcepcionGeneral(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ha ocurrido un error interno en el servidor: " + ex.getMessage(), // LEAKING ERROR FOR DEBUGGING
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}