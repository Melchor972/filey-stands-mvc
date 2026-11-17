-- Datos de ejemplo. Las fechas son relativas a hoy (sintaxis de H2) para que
-- las notificaciones siempre tengan casos que mostrar.

INSERT INTO editorial VALUES ('E-001', 'Editorial Gaceta',     'contacto@gaceta.com');
INSERT INTO editorial VALUES ('E-002', 'Letras Libres',        'contacto@letraslibres.com');
INSERT INTO editorial VALUES ('E-003', 'Fondo Peninsular',     'ventas@fondopeninsular.com');

INSERT INTO stand VALUES ('S-01', '3x3', 9.0,  4500.0,  'RESERVADO');
INSERT INTO stand VALUES ('S-02', '4x4', 16.0, 8000.0,  'OCUPADO');
INSERT INTO stand VALUES ('S-03', '3x3', 9.0,  4500.0,  'DISPONIBLE');
INSERT INTO stand VALUES ('S-04', '4x5', 20.0, 10000.0, 'BLOQUEADO');
INSERT INTO stand VALUES ('S-05', '3x4', 12.0, 6000.0,  'RESERVADO');

INSERT INTO reservacion VALUES (1, 'S-01', 'E-001', CURRENT_DATE, DATEADD('DAY', 1, CURRENT_DATE), 0.0);
INSERT INTO reservacion VALUES (2, 'S-02', 'E-002', DATEADD('DAY', -10, CURRENT_DATE), NULL, 0.6);
INSERT INTO reservacion VALUES (3, 'S-05', 'E-003', CURRENT_DATE, DATEADD('DAY', 10, CURRENT_DATE), 0.0);
