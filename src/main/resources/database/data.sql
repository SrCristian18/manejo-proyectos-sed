-- 1. PLAN DE DESARROLLO DISTRITAL
INSERT IGNORE INTO planes_desarrollo (id, nombre, ano_inicio, ano_fin, descripcion)
VALUES (1, 'Plan de Desarrollo Distrital Cartagena 2024-2027', 2024, 2027, 'Plan de Desarrollo Distrital Cartagena de Indias');

-- 2. LÍNEAS ESTRATÉGICAS
INSERT IGNORE INTO lineas_estrategicas (id, codigo, nombre, ods_relacionado, plan_desarrollo_id)
VALUES 
(1, 'L1', 'Cartagena con Educación Social y de Calidad', 'ODS 4 - Educación de Calidad', 1),
(2, 'L2', 'Infraestructura Sostenible y Ciudad Viva', 'ODS 9 - Industria, Innovación e Infraestructura', 1);

-- 3. IMPULSORES DE AVANCE
INSERT IGNORE INTO impulsores_avance (id, codigo, nombre, linea_estrategica_id)
VALUES 
(1, 'IMP-01', 'Fortalecimiento de Cobertura y Permanencia Escolar', 1),
(2, 'IMP-02', 'Mejoramiento de Infraestructura Educativa', 1);

-- 4. METAS DE RESULTADO
INSERT IGNORE INTO metas_resultado (id, codigo, descripcion, linea_base, meta_cuatrienio, unidad_medida, impulsor_avance_id)
VALUES 
(1, 'MR-01', 'Aumentar la tasa de cobertura neta en educación superior al 60%', 42.5, 60.0, 'Porcentaje', 1);

-- 5. PROGRAMAS
INSERT IGNORE INTO programas (id, codigo, nombre, meta_resultado_id)
VALUES 
(1, 'PROG-01', 'Acceso e Inclusión a la Educación Superior Distrital', 1);

-- 6. METAS PRODUCTO
INSERT IGNORE INTO metas_producto (id, codigo, descripcion, linea_base, meta_cuatrienio, unidad_medida, programa_id)
VALUES 
(1, 'MP-01', 'Beneficiar a estudiantes con créditos condonables y tarifa diferencial', 1000.0, 5000.0, 'Estudiantes', 1);

-- 7. PROYECTOS
INSERT IGNORE INTO proyectos (id, codigo_bpin, nombre, vigencia, meta_producto_id)
VALUES 
(1, '2026130010001', 'Programa Tarifa Diferencial Transcaribe y Apoyo Educativo', 2026, 1);

-- 8. ACTIVIDADES
INSERT IGNORE INTO actividades (id, nombre, presupuesto_inicial, fuente, descripcion, proyecto_id)
VALUES 
(1, 'Subsidio y caracterización de estudiantes Sisbén para Tarifa Diferencial', 500000000.00, 'LIBRE_DESTINACION', 'Asignación de aportes para transporte universitario', 1);