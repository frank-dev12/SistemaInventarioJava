package Model;

import java.sql.Timestamp;

public record Movimiento(int id, String codigoProducto, String descripcionProducto,
                         String tipo, int cantidad, Timestamp fecha, String observacion) {

    public String getTipoTexto() {
        return "E".equals(tipo) ? "Entrada" : "Salida";
    }
}
