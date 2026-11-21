
DROP TABLE IF EXISTS `Categorias`;
CREATE TABLE `Categorias` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(40) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Categorias` (`id`, `nombre`) VALUES
(3, 'Accesorios'),
(4, 'Almacenamiento'),
(7, 'Mandos'),
(2, 'Monitores'),
(1, 'Periféricos'),
(5, 'Redes');

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `Productos`
-- --------------------------------------------------------

DROP TABLE IF EXISTS `Productos`;
CREATE TABLE `Productos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(10) NOT NULL,
  `descripcion` varchar(80) NOT NULL,
  `categoria` varchar(40) NOT NULL,
  `precio` decimal(10,2) NOT NULL CHECK (`precio` >= 0),
  `stock` int NOT NULL CHECK (`stock` >= 0),
  `stock_minimo` int NOT NULL DEFAULT '5',
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Productos` (`id`, `codigo`, `descripcion`, `categoria`, `precio`, `stock`, `stock_minimo`) VALUES
(1, 'P001', 'Teclado USB', 'Periféricos', 35.00, 2, 5),
(2, 'P002', 'Mouse óptico', 'Periféricos', 18.50, 40, 10),
(3, 'P003', 'Monitor 24 pulgadas', 'Monitores', 520.00, 6, 3),
(4, 'P004', 'Cable HDMI 2 m', 'Accesorios', 15.90, 45, 5),
(5, 'P005', 'Disco SSD 512 GB', 'Almacenamiento', 189.00, 0, 4),
(10, 'P006', 'Mando de Play Stetion 5', 'Mandos', 110.00, 1100, 50);

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `Movimientos`
-- --------------------------------------------------------

DROP TABLE IF EXISTS `Movimientos`;
CREATE TABLE `Movimientos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `producto_id` int NOT NULL,
  `tipo` char(1) NOT NULL CHECK (`tipo` in ('E','S')),
  `cantidad` int NOT NULL CHECK (`cantidad` > 0),
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `observacion` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `producto_id` (`producto_id`),
  CONSTRAINT `Movimientos_ibfk_1` FOREIGN KEY (`producto_id`) REFERENCES `Productos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Movimientos` (`id`, `producto_id`, `tipo`, `cantidad`, `fecha`, `observacion`) VALUES
(3, 10, 'E', 500, '2026-11-21 00:20:07', 'en buen estado importada desde Japon'),
(4, 1, 'S', 18, '2026-11-21 00:21:33', 'ya no se estan vendiendo mucho a diferencia a los inalambricos'),
(5, 5, 'S', 5, '2026-11-21 00:23:04', 'se llevo a tineda para almacen');

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `Usuarios`
-- --------------------------------------------------------

DROP TABLE IF EXISTS `Usuarios`;
CREATE TABLE `Usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario` varchar(30) NOT NULL,
  `clave` varchar(64) NOT NULL,
  `nombre` varchar(80) NOT NULL,
  `rol` varchar(20) NOT NULL CHECK (`rol` in ('ADMIN','ALMACEN')),
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario` (`usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `Usuarios` (`id`, `usuario`, `clave`, `nombre`, `rol`, `activo`) VALUES
(1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'winter', 'ADMIN', 1),
(2, 'almacen', '7439033334ff7f5d2d164c47fe54b5819c0f7cd1225ecbc3bbac5bc80206b01f', 'Encargado de almacén', 'ALMACEN', 1),
(5, 'diego_almacen', 'd68089c77e65ee87eb9504f68212289f6dcb758fae1955a824018a67c5cb2e2f', 'diego', 'ALMACEN', 1),
(6, 'winterccode', 'a3667b8cefe219ba53a104554af9f59dfab2d6ca208510dc52b615638f042daa', 'Frank', 'ADMIN', 1);

SET FOREIGN_KEY_CHECKS = 1;