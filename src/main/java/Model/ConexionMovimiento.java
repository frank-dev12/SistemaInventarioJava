package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexionMovimiento {

    public List<Movimiento> listar() throws SQLException {
        String sql = "SELECT m.id, p.codigo, p.descripcion, m.tipo, m.cantidad, "
                + "m.fecha, m.observacion "
                + "FROM Movimientos m "
                + "INNER JOIN Productos p ON p.id = m.producto_id "
                + "ORDER BY m.fecha DESC, m.id DESC";
        List<Movimiento> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Movimiento(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getString("tipo"),
                        rs.getInt("cantidad"),
                        rs.getTimestamp("fecha"),
                        rs.getString("observacion")));
            }
        }
        return lista;
    }

    /**
     * Registra una entrada (E) o salida (S) y actualiza el stock del producto
     * dentro de una misma transacción.
     */
    public void registrar(int productoId, String tipo, int cantidad, String observacion)
            throws SQLException {
        if (!"E".equals(tipo) && !"S".equals(tipo)) {
            throw new SQLException("Tipo de movimiento inválido.");
        }
        if (cantidad <= 0) {
            throw new SQLException("La cantidad debe ser mayor que cero.");
        }

        try (Connection cn = Conexion.getConexion()) {
            boolean autoCommit = cn.getAutoCommit();
            cn.setAutoCommit(false);
            try {
                int stockActual;
                try (PreparedStatement ps = cn.prepareStatement(
                        "SELECT stock FROM Productos WHERE id = ? FOR UPDATE")) {
                    ps.setInt(1, productoId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("El producto seleccionado ya no existe.");
                        }
                        stockActual = rs.getInt("stock");
                    }
                }

                int stockNuevo = "E".equals(tipo) ? stockActual + cantidad : stockActual - cantidad;
                if (stockNuevo < 0) {
                    throw new SQLException("Stock insuficiente. Stock actual: " + stockActual
                            + ", cantidad solicitada: " + cantidad + ".");
                }

                try (PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO Movimientos (producto_id, tipo, cantidad, observacion) "
                                + "VALUES (?, ?, ?, ?)")) {
                    ps.setInt(1, productoId);
                    ps.setString(2, tipo);
                    ps.setInt(3, cantidad);
                    ps.setString(4, observacion);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = cn.prepareStatement(
                        "UPDATE Productos SET stock = ? WHERE id = ?")) {
                    ps.setInt(1, stockNuevo);
                    ps.setInt(2, productoId);
                    ps.executeUpdate();
                }

                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(autoCommit);
            }
        }
    }
}
