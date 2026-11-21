package Model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexionReporte {

    public Resumen resumen() throws SQLException {
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM Productos) AS productos, "
                + "(SELECT COUNT(*) FROM Productos WHERE stock <= stock_minimo) AS stock_bajo, "
                + "(SELECT COUNT(*) FROM Categorias) AS categorias, "
                + "(SELECT COUNT(*) FROM Movimientos WHERE DATE(fecha) = CURDATE()) AS movimientos_hoy, "
                + "(SELECT COALESCE(SUM(precio * stock), 0) FROM Productos) AS valor, "
                + "(SELECT COUNT(*) FROM Usuarios WHERE activo = 1) AS usuarios";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal valor = rs.getBigDecimal("valor");
                return new Resumen(
                        rs.getInt("productos"),
                        rs.getInt("stock_bajo"),
                        rs.getInt("categorias"),
                        rs.getInt("movimientos_hoy"),
                        valor == null ? BigDecimal.ZERO : valor,
                        rs.getInt("usuarios"));
            }
        }
        return new Resumen(0, 0, 0, 0, BigDecimal.ZERO, 0);
    }

    public List<Producto> stockBajo() throws SQLException {
        String sql = "SELECT id, codigo, descripcion, categoria, precio, stock, stock_minimo "
                + "FROM Productos WHERE stock <= stock_minimo "
                + "ORDER BY stock ASC, codigo";
        List<Producto> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getString("categoria"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("stock"),
                        rs.getInt("stock_minimo")));
            }
        }
        return lista;
    }
}
