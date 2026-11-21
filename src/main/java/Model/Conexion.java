package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Configuración por defecto para MySQL en el puerto 3307 con usuario root
    private static final String URL = valor("inventario.db.url", "INVENTARIO_DB_URL",
            "jdbc:mysql://127.0.0.1:3307/InventarioDB"
            + "?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true");

    private static final String USUARIO = valor("inventario.db.usuario", "INVENTARIO_DB_USUARIO",
            "root");

    private static final String CLAVE = valor("inventario.db.clave", "INVENTARIO_DB_CLAVE",
            ""); // <-- Si tu usuario root tiene contraseña, escríbela entre estas comillas

    private static String valor(String propiedad, String variable, String porDefecto) {
        String v = System.getProperty(propiedad);
        if (v == null || v.isBlank()) {
            v = System.getenv(variable);
        }
        return (v == null || v.isBlank()) ? porDefecto : v;
    }

    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC de MySQL (mysql-connector-j).", e);
        }
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
