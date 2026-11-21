package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexionProducto {

    public List<Producto> listar(String filtro) throws SQLException {
        String sql = "SELECT id, codigo, descripcion, categoria, precio, stock, stock_minimo "
                + "FROM Productos "
                + "WHERE codigo LIKE ? OR descripcion LIKE ? OR categoria LIKE ? "
                + "ORDER BY codigo";
        String patron = "%" + (filtro == null ? "" : filtro.trim()) + "%";

        List<Producto> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);
            try (ResultSet rs = ps.executeQuery()) {
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
        }
        return lista;
    }

    public List<String> listarCategorias() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre FROM Categorias ORDER BY nombre";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
        }
        return lista;
    }

    public void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO Productos (codigo, descripcion, categoria, precio, stock, stock_minimo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getStockMinimo());
            ps.executeUpdate();
        }
    }

    public void actualizar(Producto p) throws SQLException {
        String sql = "UPDATE Productos SET codigo=?, descripcion=?, categoria=?, "
                + "precio=?, stock=?, stock_minimo=? WHERE id=?";
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getStockMinimo());
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        }
    }

    /** Elimina un producto siempre que no tenga movimientos registrados. */
    public void eliminar(int id) throws SQLException {
        try (Connection cn = Conexion.getConexion()) {
            try (PreparedStatement ps = cn.prepareStatement(
                    "SELECT COUNT(*) FROM Movimientos WHERE producto_id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Este producto tiene movimientos registrados "
                                + "y no se puede eliminar. Puedes dejar su stock en 0.");
                    }
                }
            }
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Productos WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }
}
