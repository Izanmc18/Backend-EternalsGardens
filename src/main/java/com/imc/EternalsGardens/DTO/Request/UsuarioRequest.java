package com.imc.EternalsGardens.DTO.Request;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;

/**
 * DTO para recibir datos de registro y actualización de usuario.
 *
 * Contiene todas las validaciones necesarias para crear o modificar
 * cuentas de usuario en el sistema. Se usa en los endpoints de
 * registro, login y actualización de perfil.
 *
 * @author Izan Martinez Castro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
@Schema(description = "Datos para registro y actualización de usuario", example = "{\n" +
                "  \"nombre\": \"Juan\",\n" +
                "  \"apellidos\": \"García López\",\n" +
                "  \"dni\": \"12345678Z\",\n" +
                "  \"email\": \"juan@email.com\",\n" +
                "  \"password\": \"password123\",\n" +
                "  \"telefono\": \"612345678\",\n" +
                "  \"fechaNacimiento\": \"1985-03-15\",\n" +
                "  \"rolId\": 1,\n" +
                "  \"activo\": true\n" +
                "}")
public class UsuarioRequest {

        /**
         * Nombre completo del usuario.
         *
         * ANOTACIONES:
         * - @NotBlank: No puede ser null, vacío o solo espacios en blanco
         * - @Size(min=2, max=50): Debe tener entre 2 y 50 caracteres
         * - @Schema: Documentación para Swagger (OpenAPI)
         * * description: Explicación del campo
         * * example: Valor de ejemplo en la documentación
         *
         * USO: Se valida automáticamente cuando llega una petición
         * Si no cumple, devuelve error 400 (Bad Request)
         */
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        @Schema(description = "Nombre completo del usuario", example = "Juan", minLength = 2, maxLength = 50)
        private String nombre;

        /**
         * Apellidos completos del usuario.
         *
         * ANOTACIONES:
         * - @NotBlank: Obligatorio, no vacío
         * - @Size: Entre 2 y 100 caracteres
         * - @Schema: Documentación OpenAPI
         */
        @NotBlank(message = "Los apellidos no pueden estar vacíos")
        @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
        @Schema(description = "Apellidos completos del usuario", example = "García López", minLength = 2, maxLength = 100)
        private String apellidos;

        /**
         * Documento Nacional de Identidad del usuario.
         *
         * ANOTACIONES:
         * - @NotBlank: Obligatorio
         * - @Pattern: Validación con expresión regular
         * * regexp: Patrón regex que debe cumplir
         * * ^[0-9]{8}[A-Z]$: Exactamente 8 dígitos seguidos de 1 letra mayúscula
         * * ^ = inicio de cadena
         * * $ = final de cadena
         * * [0-9]{8} = 8 números
         * * [A-Z] = una letra mayúscula
         * - @Schema: Documentación
         *
         * VALIDACIÓN REAL: Los validators también verificarán que no exista
         * otro usuario con el mismo DNI
         */
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Formato de DNI inválido (8 números + letra)")
        @Schema(description = "DNI del usuario (8 dígitos + letra mayúscula)", example = "12345678Z", pattern = "^[0-9]{8}[A-Z]$")
        private String dni;

        /**
         * Email del usuario.
         *
         * ANOTACIONES:
         * - @NotBlank: Obligatorio
         * - @Email: Valida formato de email estándar (user@domain.com)
         * - @Size: Máximo 100 caracteres
         * - @Schema: Documentación
         *
         * VALIDACIÓN REAL: Los validators también verificarán que no exista
         * otro usuario con el mismo email
         */
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de email inválido")
        @Size(max = 100, message = "El email no puede exceder 100 caracteres")
        @Schema(description = "Email único del usuario (debe ser válido)", example = "juan.garcia@email.com", maxLength = 100)
        private String email;

        /**
         * Contraseña del usuario.
         *
         * ANOTACIONES:
         * - @NotBlank: Obligatorio
         * - @Size(min=8): Mínimo 8 caracteres (requisito de seguridad)
         * - @Schema: Documentación
         *
         * LOMBOK:
         * - exclude = "password" en @ToString(): NO incluye password en logs
         * (por seguridad, evita que se imprima la contraseña)
         *
         * VALIDACIÓN REAL: Los validators pueden añadir más reglas:
         * - Mayúsculas, minúsculas, números, caracteres especiales
         * - Se encriptará antes de guardar en BD
         * - NUNCA se devuelve en respuestas
         */
        /**
         * Contraseña del usuario.
         *
         * ANOTACIONES:
         * - @Schema: Documentación
         *
         * LOMBOK:
         * - exclude = "password" en @ToString(): NO incluye password en logs
         * (por seguridad, evita que se imprima la contraseña)
         *
         * VALIDACIÓN REAL:
         * - Se valida manualmente en el Servicio (UsuarioServiceImpl)
         * - Crear: Obligatoria + min 8 chars
         * - Actualizar: Opcional (si se envía, valida min 8 chars)
         */
        @Schema(description = "Contraseña del usuario (mínimo 8 caracteres, se encriptará)", example = "password123", minLength = 8)
        private String password;

        /**
         * Teléfono móvil del usuario.
         *
         * ANOTACIONES:
         * - @NotBlank: Obligatorio
         * - @Pattern: Expresión regular para formato español
         * * ^[6-7][0-9]{8}$: 9 dígitos empezando por 6 o 7
         * * [6-7] = primer dígito entre 6 y 7
         * * [0-9]{8} = 8 dígitos más
         * - @Schema: Documentación
         *
         * USO: Se usará para notificaciones por SMS
         */
        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^(\\+34|0034)?[\\s\\.-]?[6-7][0-9]{2}[\\s\\.-]?[0-9]{3}[\\s\\.-]?[0-9]{3}$", message = "Formato de teléfono inválido (Ej: +34 612 345 678)")
        @Schema(description = "Teléfono móvil del usuario (formato español: 9 dígitos)", example = "612345678", pattern = "^[6-7][0-9]{8}$")
        private String telefono;

        /**
         * Fecha de nacimiento del usuario.
         *
         * ANOTACIONES:
         * - @NotNull: No puede ser null (pero SÍ puede estar ausente si es opcional)
         * - @Past: La fecha debe ser en el pasado (no del futuro)
         * - @Schema: Documentación
         *
         * USO: Para calcular edad y validar mayoría de edad si es necesario
         */
        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        @Schema(description = "Fecha de nacimiento del usuario (debe ser en el pasado)", example = "1985-03-15", type = "string", format = "date")
        private LocalDate fechaNacimiento;

        /**
         * ID del rol del usuario.
         *
         * ANOTACIONES:
         * - @NotNull: Obligatorio (referencia a tabla Rol)
         * - @Min(1): El ID debe ser mínimo 1 (no puede ser 0 o negativo)
         * - @Schema: Documentación
         *
         * ROLES VÁLIDOS:
         * - 1: ADMINISTRADOR (acceso total)
         * - 2: OPERADOR_CEMENTERIO (gestión del cementerio)
         * - 3: USUARIO (cliente final)
         *
         * VALIDACIÓN REAL: Los validators verificarán que el rol exista
         */
        @NotNull(message = "El rol es obligatorio")
        @Min(value = 1, message = "El rol debe ser válido (ID mínimo: 1)")
        @Schema(description = "ID del rol del usuario (1=Admin, 2=Operador, 3=Usuario)", example = "1", minimum = "1")
        private Integer rolId;

        /**
         * Estado de activación del usuario.
         *
         * ANOTACIONES:
         * - @NotNull: Obligatorio, debe ser true o false
         * - @Schema: Documentación
         *
         * USO:
         * - true: Usuario activo, puede acceder al sistema
         * - false: Usuario desactivado, no puede acceder
         *
         * GESTIÓN: Los usuarios inactivos no aparecen en búsquedas,
         * pero sus datos se conservan en BD
         */
        @NotNull(message = "El estado activo es obligatorio")
        @Schema(description = "Estado de activación del usuario (true=activo, false=inactivo)", example = "true", type = "boolean")
        private Boolean activo;

        private String fotoUrl;

        @Schema(description = "ID del cementerio asignado (obligatorio si es Operador)", example = "1")
        private Integer cementerioId;
}
