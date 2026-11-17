-- Esquema de la base de datos Filey (tipos basicos: VARCHAR, DOUBLE, INT, DATE)

CREATE TABLE editorial (
    id_editorial    VARCHAR(10)  PRIMARY KEY,
    nombre          VARCHAR(60)  NOT NULL,
    correo_contacto VARCHAR(60)  NOT NULL
);

CREATE TABLE stand (
    id_stand         VARCHAR(10) PRIMARY KEY,
    dimensiones      VARCHAR(10) NOT NULL,
    metros_cuadrados DOUBLE      NOT NULL,
    precio           DOUBLE      NOT NULL,
    estado           VARCHAR(12) NOT NULL   -- DISPONIBLE, RESERVADO, OCUPADO, BLOQUEADO
);

CREATE TABLE reservacion (
    id_reservacion    INT         PRIMARY KEY,
    id_stand          VARCHAR(10) NOT NULL,
    id_editorial      VARCHAR(10) NOT NULL,
    fecha_reservacion DATE        NOT NULL,
    vigencia_reserva  DATE,                  -- NULL cuando el stand ya esta ocupado
    porcentaje_pagado DOUBLE      NOT NULL,  -- 0.0 a 1.0
    FOREIGN KEY (id_stand)     REFERENCES stand(id_stand),
    FOREIGN KEY (id_editorial) REFERENCES editorial(id_editorial)
);
