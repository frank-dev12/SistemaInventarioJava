package Model;

import java.sql.*;

public class Conexion {

    // Valores por defecto para MariaDB/MySQL local (puerto 3306).
    // Se pueden sobreescribir con variables de entorno o con -Dpropiedad=valor.
    private static final String URL = valor("inventario.db.url", "INVENTARIO_DB_URL",
            "jdbc:mysql://127.0.0.1:3306/InventarioDB"
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");

    private static final String USUARIO = valor("inventario.db.usuario", "INVENTARIO_DB_USUARIO",
            "inventario");

    private static final String CLAVE = valor("inventario.db.clave", "INVENTARIO_DB_CLAVE",
            "inventario123");

    private static String valor(String propiedad, String variable, String porDefecto) {
        String v = System.getProperty(propiedad);
        if (v == null || v.isBlank()) {
            v = System.getenv(variable);
        }
        return (v == null || v.isBlank()) ? porDefecto : v;
    }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
