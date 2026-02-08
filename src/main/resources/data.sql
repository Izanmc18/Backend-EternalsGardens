-- 1. Roles (Schema: id, nombre, descripcion) - Omit 'activo' to match schema
INSERT IGNORE INTO rol (id, nombre, descripcion) VALUES 
(1, 'ADMINISTRADOR', 'Acceso total al sistema'),
(2, 'OPERADOR_CEMENTERIO', 'Gestión de cementerios asignados'),
(3, 'USUARIO', 'Usuario final titular de derechos');

-- 2. Tipos de Zona (Schema: id, nombre, descripcion)
INSERT IGNORE INTO tipo_zona (id, nombre, descripcion) VALUES
(1, 'NICHOS', 'Estructura vertical de enterramiento'),
(2, 'FOSA', 'Enterramiento en suelo'),
(3, 'PANTEON', 'Construcción familiar exclusiva');

-- 3. Usuarios (Necesarios para responsable_id si se usa)
-- Simple usuario admin para pruebas
INSERT IGNORE INTO usuario (id, nombre, apellidos, email, dni, contraseña, rol_id, active, fecha_creacion) VALUES
(1, 'Admin', 'Sistema', 'admin@eternals.com', '00000000X', '$2a$10$X/hX/x/x/x/x/x/x/x/x/x', 1, true, NOW());

-- 4. Cementerios (IDs 11-15 to avoid conflict with existing ID 1)
-- Schema: nombre, municipio, provincia, codigo_postal, telefono, email, activo, fecha_creacion
INSERT IGNORE INTO cementerio (id, nombre, municipio, provincia, codigo_postal, telefono, email, activo, fecha_creacion) VALUES
(11, 'Cementerio de San Fernando', 'Sevilla', 'Sevilla', '41009', '955 47 20 00', 'info@cementeriosevilla.es', true, NOW()),
(12, 'Cementerio de la Almudena', 'Madrid', 'Madrid', '28017', '915 10 84 64', 'cementerios@madrid.es', true, NOW()),
(13, 'Cementerio de Montjuïc', 'Barcelona', 'Barcelona', '08038', '934 84 19 99', 'cbsa@cbsa.cat', true, NOW()),
(14, 'Cementerio Municipal de Valencia', 'Valencia', 'Valencia', '46017', '963 52 54 78', 'cementerios@valencia.es', true, NOW()),
(15, 'Cementerio de Polloe', 'San Sebastián', 'Gipuzkoa', '20012', '943 48 35 40', 'polloe@donostia.eus', true, NOW());

-- 5. Zonas (Linked to Cementerio 11 and 12)
-- Schema: cementerio_id, tipo_zona_id, nombre, filas, columnas, capacidad_total, activa, fecha_creacion
INSERT IGNORE INTO zona (id, cementerio_id, tipo_zona_id, nombre, filas, columnas, capacidad_total, activa, fecha_creacion) VALUES
(11, 11, 1, 'Patio de San António', 5, 20, 100, true, NOW()), -- Nichos en Sevilla
(12, 11, 2, 'Jardín de los Olivos', 10, 10, 100, true, NOW()), -- Fosas en Sevilla
(13, 12, 1, 'Galería Principal', 4, 25, 100, true, NOW()), -- Nichos en Madrid
(14, 12, 3, 'Zona Panteones Históricos', 5, 5, 25, true, NOW()); -- Panteones en Madrid

-- 6. Parcelas (Linked to Zona 11 - Patio San Antonio)
-- Schema: zona_id, tipo_zona_id, numero_fila, numero_columna, numero_identificador_unico, estado
INSERT IGNORE INTO parcela (id, zona_id, tipo_zona_id, numero_fila, numero_columna, numero_identificador_unico, estado) VALUES
(11, 11, 1, 1, 1, 'N-SA-01-01', 'OCUPADO'),
(12, 11, 1, 1, 2, 'N-SA-01-02', 'OCUPADO'),
(13, 11, 1, 1, 3, 'N-SA-01-03', 'LIBRE'),
(14, 11, 1, 1, 4, 'N-SA-01-04', 'LIBRE'),
(15, 11, 1, 2, 1, 'N-SA-02-01', 'OCUPADO');

-- Parcelas (Linked to Zona 12 - Jardín Olivos)
INSERT IGNORE INTO parcela (id, zona_id, tipo_zona_id, numero_fila, numero_columna, numero_identificador_unico, estado) VALUES
(16, 12, 2, 1, 1, 'P-JO-01-01', 'OCUPADO'),
(17, 12, 2, 1, 2, 'P-JO-01-02', 'LIBRE');

-- 7. Difuntos
-- Schema: parcela_id, nombre, apellidos, fecha_nacimiento, fecha_defuncion, dni, sexo
INSERT IGNORE INTO difunto (id, parcela_id, nombre, apellidos, fecha_nacimiento, fecha_defuncion, dni, sexo) VALUES
(1, 11, 'Antonio', 'García López', '1945-05-20', '2025-01-10', '12345678A', 'H'),
(2, 12, 'María', 'Rodríguez Pérez', '1950-08-15', '2025-01-15', '87654321B', 'M'),
(3, 15, 'José', 'Martínez Soria', '1930-12-01', '2024-12-30', '11223344C', 'H'),
(4, 16, 'Carmen', 'Fernández Ruiz', '1960-03-30', '2025-02-01', '55667788D', 'M');

-- 8. Corrección de Schema (Servicio)
-- Asegurar que la descripción sea TEXT para evitar errores de longitud
-- Nota: Esto puede fallar si la tabla no existe, pero data.sql se ejecuta tras schema init
ALTER TABLE servicio MODIFY COLUMN descripcion TEXT;
