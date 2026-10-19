-- ----------------------------------------------------
-- INSERCIÓN DE DATOS INICIALES (PDD 2024-2027)
-- ----------------------------------------------------

INSERT INTO planes_desarrollo (id, nombre, ano_inicio, ano_fin, descripcion)
VALUES (1, 'Plan de Desarrollo Distrital Cartagena 2024-2027', 2024, 2027, 'Plan de desarrollo para la transformación distrital.');

INSERT INTO lineas_estrategicas (id, codigo, nombre, ods_relacionado, plan_desarrollo_id)
VALUES 
(1, 'L1', 'Cartagena con Educación Social y de Calidad', 'ODS 4 - Educación de Calidad', 1),
(2, 'L2', 'Infraestructura Sostenible y Ciudad Viva', 'ODS 9 - Industria, Innovación e Infraestructura', 1);

INSERT INTO impulsores_avance (id, codigo, nombre, linea_estrategica_id)
VALUES 
(1, 'IMP-01', 'Fortalecimiento de Cobertura y Permanencia Escolar', 1),
(2, 'IMP-02', 'Mejoramiento de Infraestructura Educativa', 1);

INSERT INTO metas_resultado (id, codigo, descripcion, linea_base, meta_cuatrienio, unidad_medida, impulsor_avance_id)
VALUES 
(1, 'MR-01', 'Aumentar la tasa de cobertura neta en educación superior al 60%', 42.5, 60.0, 'Porcentaje', 1);

INSERT INTO programas (id, codigo, nombre, meta_resultado_id)
VALUES 
(1, 'PROG-01', 'Acceso e Inclusión a la Educación Superior Distrital', 1);

INSERT INTO metas_producto (id, codigo, descripcion, linea_base, meta_cuatrienio, unidad_medida, programa_id)
VALUES 
(1, 'MP-01', 'Beneficiar a estudiantes con créditos condonables/tarifa diferencial', 1000, 5000, 'Estudiantes', 1);