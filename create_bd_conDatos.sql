-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.30 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para bdcomercial
CREATE DATABASE IF NOT EXISTS `bdcomercial` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bdcomercial`;

-- Volcando estructura para tabla bdcomercial.almacen
CREATE TABLE IF NOT EXISTS `almacen` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.almacen: ~2 rows (aproximadamente)
INSERT IGNORE INTO `almacen` (`id`, `nombre`) VALUES
	(1, 'ALMACEN TIENDA PRINCIPAL'),
	(2, 'ALMACEN SUCURSAL TUCUME');

-- Volcando estructura para tabla bdcomercial.categoria
CREATE TABLE IF NOT EXISTS `categoria` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.categoria: ~3 rows (aproximadamente)
INSERT IGNORE INTO `categoria` (`id`, `nombre`) VALUES
	(1, 'LACTEOS'),
	(2, 'CEREALES'),
	(3, 'ACEITES');

-- Volcando estructura para tabla bdcomercial.ciudad
CREATE TABLE IF NOT EXISTS `ciudad` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.ciudad: ~2 rows (aproximadamente)
INSERT IGNORE INTO `ciudad` (`id`, `nombre`) VALUES
	(1, 'CHICLAYO'),
	(2, 'LIMA');

-- Volcando estructura para tabla bdcomercial.cliente
CREATE TABLE IF NOT EXISTS `cliente` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `direccion` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `ciudad_id` int NOT NULL,
  `DNI` char(8) NOT NULL,
  `firebase` text NOT NULL,
  `clave` char(32) NOT NULL,
  `estado_cliente` char(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ciudad_id` (`ciudad_id`),
  CONSTRAINT `cliente_ibfk_1` FOREIGN KEY (`ciudad_id`) REFERENCES `ciudad` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.cliente: ~6 rows (aproximadamente)
INSERT IGNORE INTO `cliente` (`id`, `nombre`, `direccion`, `email`, `ciudad_id`, `DNI`, `firebase`, `clave`, `estado_cliente`) VALUES
	(1, 'HUILDER MERA MONTENEGRO', 'AV. BALTA 425', 'hmera@usat.edu.pe', 1, '44177590', 'cHB2zhoaR9i3sFyAdoNC-O:APA91bEs6LuXG6lx1C7FL8M9VU6RrmYgfNAFPFuEYgTARoatBwvqeyF-Rt8-IbDVaJ0ETkaOpSalMDwXwo9pL0YfFvCj7yFZeIxkdQ8WzKy9T_39GjAW4D57Xfda9ui8Otg0PLPTuXXC', '78a87e8116e2ab5a9b6b6fc458b8be6b', '1'),
	(2, 'LUIS PERALES DIAZ', 'AV. LUIS GONZALES 414', 'luis.perales@gmail.com', 1, '87654321', '', '78a87e8116e2ab5a9b6b6fc458b8be6b', '1'),
	(3, '', 'TORRES PAZ 220', 'carolinacromero@gmail.com', 2, '', '', '78a87e8116e2ab5a9b6b6fc458b8be6b', '1'),
	(7, 'MARILEYDI DÌAZ HERNANDEZ', 'AV. GRAU 560', 'marileydihernandez@gmail.com', 1, '', '', '78a87e8116e2ab5a9b6b6fc458b8be6b', '1'),
	(8, 'LUIS MENDOZA ARIAS', 'AV. MEXICO CUADRA 5', 'luis_20@hotmail.com', 2, '', '', '78a87e8116e2ab5a9b6b6fc458b8be6b', '1'),
	(9, 'LUIS MENDOZA ARIAS', 'AV. MEXICO CUADRA 5', 'luis_20@hotmail.com', 2, '', '', '78a87e8116e2ab5a9b6b6fc458b8be6b', '1');

-- Volcando estructura para tabla bdcomercial.configuracion
CREATE TABLE IF NOT EXISTS `configuracion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `valor` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.configuracion: ~1 rows (aproximadamente)
INSERT IGNORE INTO `configuracion` (`id`, `nombre`, `valor`) VALUES
	(1, 'PORCENTAJE DE IGV', '18');

-- Volcando estructura para tabla bdcomercial.producto
CREATE TABLE IF NOT EXISTS `producto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `categoria_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `categoria_id` (`categoria_id`),
  CONSTRAINT `producto_ibfk_1` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.producto: ~4 rows (aproximadamente)
INSERT IGNORE INTO `producto` (`id`, `nombre`, `precio`, `categoria_id`) VALUES
	(2, 'LECHE GLORIA X 220 ML', 3.20, 1),
	(3, 'YOGURT GLORIA X 1 LITRO', 5.30, 1),
	(4, 'ARROZ EXTRA X 5KG', 13.85, 2),
	(5, 'ACEITE PRIMOR X 1 LITRO', 8.99, 3);

-- Volcando estructura para tabla bdcomercial.serie
CREATE TABLE IF NOT EXISTS `serie` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipo_comprobante_id` int NOT NULL,
  `serie` char(4) NOT NULL,
  `ndoc` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tipo_comprobante_id` (`tipo_comprobante_id`),
  CONSTRAINT `serie_ibfk_1` FOREIGN KEY (`tipo_comprobante_id`) REFERENCES `tipo_comprobante` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.serie: ~7 rows (aproximadamente)
INSERT IGNORE INTO `serie` (`id`, `tipo_comprobante_id`, `serie`, `ndoc`) VALUES
	(1, 1, 'F001', 7),
	(2, 1, 'F002', 0),
	(3, 1, 'F003', 0),
	(4, 2, 'B001', 6),
	(5, 2, 'B002', 0),
	(6, 3, 'NV01', 0),
	(7, 3, 'NV02', 0);

-- Volcando estructura para tabla bdcomercial.stock_almacen
CREATE TABLE IF NOT EXISTS `stock_almacen` (
  `id` int NOT NULL AUTO_INCREMENT,
  `producto_id` int NOT NULL,
  `almacen_id` int NOT NULL,
  `stock` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `producto_id` (`producto_id`),
  KEY `almacen_id` (`almacen_id`),
  CONSTRAINT `stock_almacen_ibfk_1` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`),
  CONSTRAINT `stock_almacen_ibfk_2` FOREIGN KEY (`almacen_id`) REFERENCES `almacen` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.stock_almacen: ~8 rows (aproximadamente)
INSERT IGNORE INTO `stock_almacen` (`id`, `producto_id`, `almacen_id`, `stock`) VALUES
	(1, 2, 1, 75),
	(2, 3, 1, 71),
	(3, 4, 1, 90),
	(4, 2, 2, 50),
	(5, 3, 2, 50),
	(6, 4, 2, 50),
	(7, 5, 1, 79),
	(8, 5, 2, 35);

-- Volcando estructura para tabla bdcomercial.tipo_comprobante
CREATE TABLE IF NOT EXISTS `tipo_comprobante` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipo_sunat` char(2) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `compra` char(1) NOT NULL,
  `venta` char(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.tipo_comprobante: ~4 rows (aproximadamente)
INSERT IGNORE INTO `tipo_comprobante` (`id`, `tipo_sunat`, `nombre`, `compra`, `venta`) VALUES
	(1, '01', 'FACTURA', '1', '1'),
	(2, '03', 'BOLETA DE VENTA', '0', '1'),
	(3, '00', 'NOTA DE VENTA', '0', '1'),
	(4, '00', 'NOTA DE COMPRA', '1', '0');

-- Volcando estructura para tabla bdcomercial.usuario
CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `clave` char(32) NOT NULL,
  `img` varchar(20) NOT NULL,
  `estado_usuario` char(1) DEFAULT NULL,
  `token` varchar(300) DEFAULT NULL,
  `estado_token` char(1) DEFAULT NULL,
  `almacen_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_usuario_almacen` (`almacen_id`),
  CONSTRAINT `FK_usuario_almacen` FOREIGN KEY (`almacen_id`) REFERENCES `almacen` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.usuario: ~2 rows (aproximadamente)
INSERT IGNORE INTO `usuario` (`id`, `nombre`, `email`, `clave`, `img`, `estado_usuario`, `token`, `estado_token`, `almacen_id`) VALUES
	(1, 'Huilder Mera Montenegro', 'hmera@usat.edu.pe', '78a87e8116e2ab5a9b6b6fc458b8be6b', 'foto1.jpg', '1', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3VhcmlvSUQiOjEsImV4cCI6MTcwMDUyODE3M30.8VzryZ045OzIPuGUHRQL6YAgpdocqfBdpyT2o49qHLU', '1', 1),
	(2, 'Jorge Sanchez Ramirez', 'jorge.sanchez@gmail.com', '78a87e8116e2ab5a9b6b6fc458b8be6b', 'foto2.jpg', '1', '', '1', 2);

-- Volcando estructura para tabla bdcomercial.venta
CREATE TABLE IF NOT EXISTS `venta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cliente_id` int NOT NULL,
  `tipo_comprobante_id` int NOT NULL,
  `nser` char(4) NOT NULL,
  `ndoc` int NOT NULL,
  `fdoc` date NOT NULL,
  `sub_total` decimal(10,2) NOT NULL,
  `igv` decimal(10,2) NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `porcentaje_igv` decimal(10,2) NOT NULL,
  `estado` char(1) NOT NULL DEFAULT '1',
  `usuario_id_registro` int NOT NULL,
  `usuario_id_anulacion` int DEFAULT NULL,
  `fecha_hora_registro` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_hora_anulacion` timestamp NULL DEFAULT NULL,
  `almacen_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cliente_id` (`cliente_id`),
  KEY `tipo_comprobante_id` (`tipo_comprobante_id`),
  KEY `usuario_id_registro` (`usuario_id_registro`),
  KEY `usuario_id_anulacion` (`usuario_id_anulacion`),
  KEY `fk_venta_almacen` (`almacen_id`),
  CONSTRAINT `fk_venta_almacen` FOREIGN KEY (`almacen_id`) REFERENCES `almacen` (`id`),
  CONSTRAINT `venta_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`),
  CONSTRAINT `venta_ibfk_2` FOREIGN KEY (`tipo_comprobante_id`) REFERENCES `tipo_comprobante` (`id`),
  CONSTRAINT `venta_ibfk_3` FOREIGN KEY (`usuario_id_registro`) REFERENCES `usuario` (`id`),
  CONSTRAINT `venta_ibfk_4` FOREIGN KEY (`usuario_id_anulacion`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.venta: ~13 rows (aproximadamente)
INSERT IGNORE INTO `venta` (`id`, `cliente_id`, `tipo_comprobante_id`, `nser`, `ndoc`, `fdoc`, `sub_total`, `igv`, `total`, `porcentaje_igv`, `estado`, `usuario_id_registro`, `usuario_id_anulacion`, `fecha_hora_registro`, `fecha_hora_anulacion`, `almacen_id`) VALUES
	(1, 1, 1, 'F001', 1, '2021-05-24', 0.00, 0.00, 0.00, 18.00, '1', 1, NULL, '2021-05-24 20:23:56', NULL, 1),
	(2, 1, 1, 'F001', 2, '2021-05-24', 0.00, 0.00, 0.00, 18.00, '1', 1, NULL, '2021-05-24 20:47:35', NULL, 1),
	(3, 1, 1, 'F001', 3, '2021-05-24', 0.00, 0.00, 0.00, 18.00, '1', 1, NULL, '2021-05-24 20:48:39', NULL, 1),
	(5, 1, 1, 'F001', 4, '2021-05-24', 0.00, 0.00, 0.00, 18.00, '1', 1, NULL, '2021-05-24 20:55:08', NULL, 1),
	(6, 1, 1, 'F001', 5, '2021-05-24', 0.00, 0.00, 0.00, 18.00, '1', 1, NULL, '2021-05-24 20:55:45', NULL, 1),
	(7, 1, 1, 'F001', 6, '2021-05-24', 0.00, 0.00, 0.00, 18.00, '1', 1, NULL, '2021-05-24 20:56:18', NULL, 1),
	(8, 1, 1, 'F001', 7, '2021-06-15', 0.00, 0.00, 0.00, 18.00, '1', 1, NULL, '2021-06-15 21:08:03', NULL, 1),
	(9, 1, 2, 'B001', 1, '2021-06-15', 26.08, 4.70, 30.78, 18.00, '1', 1, NULL, '2021-06-15 23:07:47', NULL, 1),
	(10, 1, 2, 'B001', 2, '2021-06-15', 48.54, 8.74, 57.28, 18.00, '1', 1, NULL, '2021-06-15 23:13:10', NULL, 1),
	(11, 1, 2, 'B001', 3, '2021-06-15', 80.92, 14.57, 95.49, 18.00, '1', 1, NULL, '2021-06-15 23:14:09', NULL, 1),
	(12, 1, 2, 'B001', 4, '2021-06-15', 56.39, 10.15, 66.54, 18.00, '1', 1, NULL, '2021-06-15 23:25:21', NULL, 1),
	(13, 1, 2, 'B001', 5, '2021-06-15', 68.57, 12.34, 80.91, 18.00, '1', 1, NULL, '2021-06-15 23:26:22', NULL, 1),
	(14, 2, 2, 'B001', 6, '2023-09-25', 66.09, 11.90, 77.99, 18.00, '1', 1, NULL, '2023-11-13 20:40:53', NULL, 1);

-- Volcando estructura para tabla bdcomercial.venta_detalle
CREATE TABLE IF NOT EXISTS `venta_detalle` (
  `id` int NOT NULL AUTO_INCREMENT,
  `venta_id` int NOT NULL,
  `producto_id` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `importe` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `venta_id` (`venta_id`),
  KEY `producto_id` (`producto_id`),
  CONSTRAINT `venta_detalle_ibfk_1` FOREIGN KEY (`venta_id`) REFERENCES `venta` (`id`),
  CONSTRAINT `venta_detalle_ibfk_2` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla bdcomercial.venta_detalle: ~35 rows (aproximadamente)
INSERT IGNORE INTO `venta_detalle` (`id`, `venta_id`, `producto_id`, `cantidad`, `precio`, `importe`) VALUES
	(1, 1, 2, 1, 19.00, 19.00),
	(2, 1, 3, 2, 0.60, 1.20),
	(3, 1, 4, 5, 5.37, 26.85),
	(4, 2, 2, 1, 19.00, 19.00),
	(5, 2, 3, 2, 0.60, 1.20),
	(6, 2, 4, 3, 5.37, 26.85),
	(7, 3, 2, 1, 19.00, 19.00),
	(8, 3, 3, 2, 0.60, 1.20),
	(9, 3, 4, 3, 5.37, 26.85),
	(10, 5, 2, 1, 19.00, 19.00),
	(11, 5, 3, 2, 0.60, 1.20),
	(12, 5, 4, 3, 5.37, 16.11),
	(13, 6, 2, 1, 19.00, 19.00),
	(14, 6, 3, 2, 0.60, 1.20),
	(15, 6, 4, 5, 5.37, 26.85),
	(16, 7, 2, 1, 19.00, 19.00),
	(17, 7, 3, 2, 0.60, 1.20),
	(18, 7, 4, 1, 5.37, 5.37),
	(19, 8, 2, 1, 19.00, 19.00),
	(20, 8, 3, 2, 0.60, 1.20),
	(21, 8, 4, 4, 5.37, 21.48),
	(22, 9, 5, 2, 8.99, 17.98),
	(23, 9, 2, 4, 3.20, 12.80),
	(24, 10, 5, 2, 8.99, 17.98),
	(25, 10, 2, 4, 3.20, 12.80),
	(26, 10, 3, 5, 5.30, 26.50),
	(27, 11, 5, 6, 8.99, 53.94),
	(28, 11, 4, 3, 13.85, 41.55),
	(29, 12, 5, 1, 8.99, 8.99),
	(30, 12, 4, 3, 13.85, 41.55),
	(31, 12, 2, 5, 3.20, 16.00),
	(32, 13, 5, 9, 8.99, 80.91),
	(33, 14, 2, 5, 3.20, 16.00),
	(34, 14, 3, 10, 5.30, 53.00),
	(35, 14, 5, 1, 8.99, 8.99);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
