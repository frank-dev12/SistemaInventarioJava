package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexionUsuario {

    public Usuario validar(String usuario, String clave) throws SQLException {
        String sql = "SELECT id, usuario, nombre, rol, activo FROM Usuarios "
                + "WHERE usuario = ? AND clave = SHA2(?, 256) AND activo = 1";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("usuario"),
                            rs.getString("nombre"), rs.getString("rol"), rs.getBoolean("activo"));
                }
            }
        }
        return null;
    }

    /** Lista usuarios; si el filtro está vacío devuelve todos. */
    public List<Usuario> listar(String filtro) throws SQLException {
        String sql = "SELECT id, usuario, nombre, rol, activo FROM Usuarios "
                + "WHERE usuario LIKE ? OR nombre LIKE ? OR rol LIKE ? "
                + "ORDER BY usuario";
        String patron = "%" + (filtro == null ? "" : filtro.trim()) + "%";

        List<Usuario> lista = new ArrayList<>();
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Usuario(rs.getInt("id"), rs.getString("usuario"),
                            rs.getString("nombre"), rs.getString("rol"), rs.getBoolean("activo")));
                }
            }
        }
        return lista;
    }

    /** Inserta un usuario nuevo. La clave se guarda como SHA-256 hexadecimal. */
    public void insertar(Usuario u, String clave) throws SQLException {
        String sql = "INSERT INTO Usuarios (usuario, clave, nombre, rol, activo) "
                + "VALUES (?, SHA2(?, 256), ?, ?, ?)";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, clave);
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getRol());
            ps.setBoolean(5, u.isActivo());
            ps.executeUpdate();
        }
    }

    /** Actualiza un usuario. Si clave viene vacía, conserva la contraseña actual. */
    public void actualizar(Usuario u, String clave) throws SQLException {
        boolean cambiaClave = clave != null && !clave.isBlank();
        String sql = cambiaClave
                ? "UPDATE Usuarios SET usuario=?, clave=SHA2(?,256), nombre=?, rol=?, activo=? WHERE id=?"
                : "UPDATE Usuarios SET usuario=?, nombre=?, rol=?, activo=? WHERE id=?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, u.getUsuario());
            if (cambiaClave) {
                ps.setString(i++, clave);
            }
            ps.setString(i++, u.getNombre());
            ps.setString(i++, u.getRol());
            ps.setBoolean(i++, u.isActivo());
            ps.setInt(i, u.getId());
            ps.executeUpdate();
        }
    }

    /** Elimina definitivamente un usuario. */
    public void eliminar(int id) throws SQLException {
        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM Usuarios WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
