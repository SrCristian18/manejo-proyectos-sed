-- 1. PLAN DE DESARROLLO DISTRITAL
INSERT IGNORE INTO planes_desarrollo (id, nombre, ano_inicio, ano_fin, descripcion)
VALUES (1, 'Plan de Desarrollo Distrital Cartagena 2024-2027', 2024, 2027, 'Cartagena, Ciudad de derechos');

-- 2. LÍNEAS ESTRATÉGICAS
INSERT IGNORE INTO lineas_estrategicas (id, codigo, nombre, ods_relacionado, plan_desarrollo_id)
VALUES 
(1, 'L2', 'Vida Digna', 'ODS 4 - Educación de Calidad', 1);

-- 3. IMPULSORES DE AVANCE
INSERT IGNORE INTO impulsores_avance (id, codigo, nombre, linea_estrategica_id)
VALUES 
(1, 'IMP-ED-01', 'Educación', 1);

-- 4. METAS DE RESULTADO
INSERT IGNORE INTO metas_resultado (id, codigo, descripcion, linea_base, meta_cuatrienio, unidad_medida, impulsor_avance_id)
VALUES 
(1, 'MR-01', 'Incrementar a 40% el porcentaje de las Instituciones Educativas Oficiales con oferta en educación inicial (prejardín, jardín y transición)', 1.80, 40.00, 'Porcentaje', 1),
(2, 'MR-02', 'Incrementar la tasa de cobertura neta sin extraedad global (Primaria, Secundaria y Media) urbana y rural al 95%', 91.37, 95.00, 'Porcentaje', 1),
(3, 'MR-03', 'Reducir la tasa de deserción en educación preescolar, básica y media de Instituciones Educativas Oficiales a 2.47%', 3.53, 2.47, 'Porcentaje', 1),
(4, 'MR-04', 'Incrementar a treinta y dos (32) el número de Instituciones Educativas Oficiales que mejoran su clasificación en Pruebas SABER 11', 11.00, 32.00, 'Número', 1),
(5, 'MR-05', 'Incrementar la participación de los egresados de Instituciones Educativas Oficiales en la tasa de absorción de educación superior a 30%', 21.00, 30.00, 'Porcentaje', 1),
(6, 'MR-06', 'Reducir la tasa de analfabetismo en el Distrito al 0.9%', 1.90, 0.90, 'Porcentaje', 1),
(7, 'MR-07', 'Reducir la tasa de extraedad en el Distrito al 9%', 11.96, 9.00, 'Porcentaje', 1),
(8, 'MR-08', 'Incrementar a 67% el porcentaje de Instituciones Educativas Oficiales que cuentan con fortalecimiento de educación inclusiva', 0.00, 67.00, 'Porcentaje', 1);

-- 5. PROGRAMAS
INSERT IGNORE INTO programas (id, codigo, nombre, meta_resultado_id)
VALUES 
(1,  '5.1.1',  'Modernización de la Infraestructura Educativa', 1),
(2,  '5.1.2',  'Avanzando desde el Comienzo', 1),
(3,  '5.1.3',  'Me Quedo porque me Quedo', 3),
(4,  '5.1.4',  'Yo Cuento', 8),
(5,  '5.1.5',  'Escuela Hogar', 2),
(6,  '5.1.6',  'Cartagena Territorio Plurilingüe', 2),
(7,  '5.1.7',  'Cartagena Mejor Educada', 4),
(8,  '5.1.8',  'Levantemos la Voz', 4),
(9,  '5.1.9',  'Aula Global', 4),
(10, '5.1.10', 'Formación y Cualificación de Docentes y Directivos Docentes', 4),
(11, '5.1.11', 'Fortalecimiento de la Gestión Escolar en las Instituciones Educativas Oficiales', 4),
(12, '5.1.12', 'Unidos por el Sueño Superior', 5),
(13, '5.1.13', 'Avanzamos en el Fortalecimiento Institucional de la Secretaría de Educación', 2),
(14, '5.1.14', 'Cartagena, Territorio Digital', 2),
(15, '5.1.15', 'Oferta Académica Superior con Calidad', 5),
(16, '5.1.16', 'Formación Técnica y Complementaria en Oficios', 5);

-- 6. METAS PRODUCTO
INSERT IGNORE INTO metas_producto (id, codigo, descripcion, linea_base, meta_cuatrienio, unidad_medida, programa_id)
VALUES 
(1, 'MP-12-01', 'Becar en educación superior a nueve mil (9.000) estudiantes egresados de Instituciones Educativas Oficiales y con matrícula contratada', 3250.0, 9000.0, 'Estudiantes', 12),
(2, 'MP-12-02', 'Becar en educación superior a quinientos cuarenta y cuatro (544) estudiantes egresados de IEO y con matrícula contratada, con becas inclusivas (indígenas, afro, víctimas y con discapacidad)', 228.0, 544.0, 'Estudiantes', 12),
(3, 'MP-12-03', 'Graduar doce mil (12.000) estudiantes de media técnica con doble titulación', 2136.0, 12000.0, 'Estudiantes', 12),
(4, 'MP-12-04', 'Implementar doble titulación en veinticinco (25) Instituciones Educativas Oficiales con media técnica e Instituciones Educativas Oficiales académicas', 16.0, 25.0, 'Instituciones Educativas', 12),
(5, 'MP-12-05', 'Asistir con programas piloto de bilingüismo a cinco (5) Instituciones Educativas Oficiales con nodos de media técnica', 0.0, 5.0, 'Instituciones Educativas', 12),
(6, 'MP-12-06', 'Formar a quinientos (500) estudiantes en educación técnica para el trabajo y el desarrollo humano', 56.0, 500.0, 'Estudiantes', 12),
(7, 'MP-12-07', 'Vincular a doscientos (200) voluntarios universitarios para acompañamiento a los estudiantes de las Instituciones Educativas Oficiales en el fortalecimiento de sus competencias', 0.0, 200.0, 'Voluntarios', 12);

-- 7. PROYECTOS
INSERT IGNORE INTO proyectos (id, codigo_bpin, nombre, vigencia, meta_producto_id)
VALUES 
(1, '2026130010001', 'Proyecto de Fortalecimiento de la Educación Superior', 2026, 1),
(2, '2024130010250', 'Generación de Oportunidades de Acceso y Permanencia a la Educación para el Trabajo y el Desarrollo Humano para Egresados del Sistema', 2024, 6);

-- 8. ACTIVIDADES
INSERT IGNORE INTO actividades (id, nombre, presupuesto_inicial, fuente, descripcion, proyecto_id)
VALUES 
(1, 'Convocatoria y asignación de créditos condonables', 1500000000.00, 'LIBRE_DESTINACION', 'Asignación de aportes para matrícula', 1),
(2, 'Organización de procesos de acompañamiento a la SED y a las IETDH para garantizar la entrega de becas a los beneficiarios', 1500000000.00, 'LIBRE_DESTINACION', ' ', 2),
(3, 'Entregar becas de educación para el trabajo y el desarrollo humano a beneficiarios', 1500000000.00, 'LIBRE_DESTINACION', ' ', 2);