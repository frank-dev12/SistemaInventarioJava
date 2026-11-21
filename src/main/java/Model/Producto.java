package Model;

import java.math.BigDecimal;

public class Producto {
    protected int id;
    protected String codigo;
    protected String descripcion;
    private String categoria;
    protected BigDecimal precio;
    private int stock;
    protected int stockMinimo;

    public Producto() {
    }

    public Producto(int id, String codigo, String descripcion, String categoria,
                    BigDecimal precio, int stock, int stockMinimo) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public boolean isStockBajo() {
        return stock <= stockMinimo;
    }

    public BigDecimal calcularPrecioVenta() {
        return precio;
    }

    @Override
    public String toString() {
        return codigo + " - " + descripcion;
    }
}
