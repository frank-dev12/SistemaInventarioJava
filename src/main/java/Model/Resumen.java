package Model;

import java.math.BigDecimal;

public record Resumen(int productos, int stockBajo, int categorias, int movimientosHoy,
                      BigDecimal valorInventario, int usuariosActivos) {
}
