package Model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductoElectronicos extends Producto {
    private static final BigDecimal RECARGO_GARANTIA = new BigDecimal("1.10");

    private String marca;
    private String modelo;
    private String voltaje;

    public ProductoElectronicos() {
        super();
    }

    public ProductoElectronicos(int id, String codigo, String descripcion, String categoria,
                                BigDecimal precio, int stock, int stockMinimo,
                                String marca, String modelo, String voltaje) {
        super(id, codigo, descripcion, categoria, precio, stock, stockMinimo);
        this.marca = marca;
        this.modelo = modelo;
        this.voltaje = voltaje;
    }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getVoltaje() { return voltaje; }
    public void setVoltaje(String voltaje) { this.voltaje = voltaje; }

    @Override
    public BigDecimal calcularPrecioVenta() {
        return precio.multiply(RECARGO_GARANTIA).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return super.toString() + " (" + marca + " " + modelo + ", " + voltaje + ")";
    }
}
