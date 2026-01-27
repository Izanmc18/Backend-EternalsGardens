# ⚰️ Eternals Gardens - Backend API

![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

¡Bienvenido al núcleo de **Eternals Gardens**! Este repositorio contiene la API RESTful que impulsa la plataforma de gestión integral de cementerios.

El objetivo de este backend es proporcionar una infraestructura **segura, escalable y robusta** para administrar parcelas, difuntos, trámites administrativos y servicios funerarios, sirviendo datos a clientes web (Angular) y móviles.

---

## 🚀 Características Principales

* **Seguridad de Grado Militar:** Sistema de autenticación basado en **JWT (JSON Web Tokens)** con control de acceso granular por roles (`ADMINISTRADOR`, `OPERARIO`, `CIUDADANO`).
* **Gestión Geoespacial:** Lógica de negocio para manejar la ubicación exacta de parcelas y zonas dentro del cementerio (coordenadas X/Y y polígonos).
* **Manejo Centralizado de Errores:** Implementación de un `ApiError` estandarizado que garantiza que el frontend siempre reciba respuestas JSON coherentes (404, 401, 409, etc.).
* **Trámites Automatizados:** Flujo completo para solicitudes de exhumación con generación automática de números de expediente únicos.
* **Dashboard Analytics:** Endpoints optimizados que calculan estadísticas en tiempo real (ocupación, recaudación) para alimentar gráficos.
* **Documentación Viva:** Integración con **Swagger/OpenAPI** para explorar y probar la API sin escribir código.

---

## 🛠️ Arquitectura y Construcción

Este proyecto sigue una arquitectura limpia en capas para facilitar el mantenimiento y la escalabilidad. Así es como está organizado el código:

### 1. Capa de Seguridad (Security)
El "portero" de la aplicación.
* Implementé un `JwtAuthenticationFilter` que intercepta cada petición HTTP para validar la identidad del usuario antes de que llegue al controlador.
* Uso de `BCrypt` para el hasheo de contraseñas, asegurando que ningún dato sensible se guarde en texto plano.
* Configuración de **CORS** para permitir conexiones seguras desde el frontend en Angular.

### 2. Capa de Controladores (Controllers)
Los puntos de entrada de la API. Definen los endpoints REST (GET, POST, PUT, DELETE) y se encargan de recibir las peticiones y devolver los DTOs de respuesta.
* Uso extensivo de anotaciones de validación (`@Valid`, `@NotNull`) para sanear la entrada de datos.

### 3. Lógica de Negocio (Services)
El cerebro de la aplicación. Aquí residen las reglas importantes:
* **Exhumaciones:** Verificación de que el difunto existe y generación de códigos de solicitud.
* **Usuarios:** Asignación automática de roles y validación de emails únicos.
* **Parcelas:** Control de estados (Libre/Ocupada/Reservada) para evitar conflictos.

### 4. Acceso a Datos (Repositories + Entities)
Uso de **Spring Data JPA** e **Hibernate** para interactuar con MySQL.
* Consultas optimizadas con `@Query` para búsquedas complejas (ej: buscar difuntos por parcela).
* Mapeo de relaciones SQL (`@OneToMany`, `@ManyToOne`) reflejadas fielmente en objetos Java.
* Uso de **Lombok** para reducir el código repetitivo (Getters, Setters, Builders).

### 5. Transferencia de Datos (DTOs + Mappers)
Para no exponer la estructura interna de la base de datos, utilizo el patrón DTO (*Data Transfer Object*).
* **ModelMapper** se encarga de convertir automáticamente las Entidades a DTOs y viceversa, manteniendo el código limpio y separado.

---

## 📦 Instalación y Despliegue

¿Quieres levantar el servidor en tu máquina? Sigue estos pasos:

1.  **Clona el repositorio:**
    ```bash
    git clone [https://github.com/tu-usuario/eternals-gardens-backend.git](https://github.com/tu-usuario/eternals-gardens-backend.git)
    cd eternals-gardens-backend
    ```

2.  **Configura la Base de Datos:**
    Asegúrate de tener MySQL corriendo. Crea una base de datos vacía llamada `cementerios_db`.
    *(El archivo `application.properties` está configurado para crear las tablas automáticamente al iniciar)*.

3.  **Ejecuta la aplicación (sin instalar Maven):**
    Usa el *wrapper* incluido:
    ```bash
    ./mvnw spring-boot:run
    ```

4.  **¡Listo!**
    La API estará escuchando en: `http://localhost:8080`

### 🧪 Pruebas y Documentación

Una vez levantado el servidor, accede a la interfaz visual de Swagger para probar los endpoints:


---

<p align="center">
  Desarrollado con ❤️ y Java para el Proyecto Final de DAW
</p>
