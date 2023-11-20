ALTER TABLE cliente ADD COLUMN DNI CHAR(8) NOT NULL;
ALTER TABLE cliente ADD COLUMN firebase text NOT NULL;
ALTER TABLE cliente ADD COLUMN clave CHAR(32) NOT NULL;
ALTER TABLE cliente ADD COLUMN estado_cliente CHAR(1) NOT NULL;

SELECT * FROM cliente;

UPDATE cliente SET clave = MD5('usat2023'), estado_cliente = '1';