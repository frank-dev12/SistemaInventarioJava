package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexionCliente {

    public boolean registrar(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes (ruc, razon_social, direccion, telefono, correo) VALUES (?, ?, ?, ?, ?)";
        try (@SuppressWarnings("static-access")
        Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getRuc());
            ps.setString(2, c.getRazonSocial());
            ps.setString(3, c.getDireccion());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getCorreo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Cliente c) throws SQLException {
        String sql = "UPDATE clientes SET ruc = ?, razon_social = ?, direccion = ?, telefono = ?, correo = ? WHERE id = ?";
        new Conexion();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getRuc());
            ps.setString(2, c.getRazonSocial());
            ps.setString(3, c.getDireccion());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getCorreo());
            ps.setInt(6, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";
        new Conexion();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Cliente> listar() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, ruc, razon_social, direccion, telefono, correo FROM clientes ORDER BY id ASC";
        new Conexion();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("ruc"),
                        rs.getString("razon_social"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("correo")
                ));
            }
        }
        return lista;
    }
}