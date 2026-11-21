package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexionCategoria {

    public List<Categoria> listar() throws SQLException {
        String sql = "SELECT c.id, c.nombre, COUNT(p.id) AS productos "
                + "FROM Categorias c "
                + "LEFT JOIN Productos p ON p.categoria = c.nombre "
                + "GROUP BY c.id, c.nombre ORDER BY c.nombre";
        List<Categoria> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre"),
                        rs.getInt("productos")));
            }
        }
        return lista;
    }

    public List<String> listarNombres() throws SQLException {
        List<String> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement("SELECT nombre FROM Categorias ORDER BY nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("nombre"));
            }
        }
        return lista;
    }

    public void insertar(String nombre) throws SQLException {
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO Categorias (nombre) VALUES (?)")) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        }
    }

    /** Cambia el nombre y actualiza también la categoría de los productos. */
    public void actualizar(int id, String nombreNuevo) throws SQLException {
        try (Connection cn = Conexion.getConexion()) {
            cn.setAutoCommit(false);
            try {
                String anterior;
                try (PreparedStatement ps = cn.prepareStatement(
                        "SELECT nombre FROM Categorias WHERE id = ?")) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("La categoría ya no existe.");
                        }
                        anterior = rs.getString("nombre");
                    }
                }
                try (PreparedStatement ps = cn.prepareStatement(
                        "UPDATE Categorias SET nombre = ? WHERE id = ?")) {
                    ps.setString(1, nombreNuevo);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = cn.prepareStatement(
                        "UPDATE Productos SET categoria = ? WHERE categoria = ?")) {
                    ps.setString(1, nombreNuevo);
                    ps.setString(2, anterior);
                    ps.executeUpdate();
                }
                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    /** No permite borrar una categoría que todavía tiene productos. */
    public void eliminar(int id) throws SQLException {
        try (Connection cn = Conexion.getConexion()) {
            String nombre;
            try (PreparedStatement ps = cn.prepareStatement(
                    "SELECT nombre FROM Categorias WHERE id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return;
                    }
                    nombre = rs.getString("nombre");
                }
            }
            try (PreparedStatement ps = cn.prepareStatement(
                    "SELECT COUNT(*) FROM Productos WHERE categoria = ?")) {
                ps.setString(1, nombre);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("La categoría \"" + nombre + "\" tiene productos "
                                + "asociados. Cambia esos productos de categoría primero.");
                    }
                }
            }
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Categorias WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }
}
