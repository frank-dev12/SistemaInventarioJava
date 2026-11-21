package Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductoPerecible extends Producto {
    private static final int DIAS_POR_VENCER = 7;
    private static final BigDecimal DESCUENTO_POR_VENCER = new BigDecimal("0.70");

    private LocalDate fechaVencimiento;
    private String lote;

    public ProductoPerecible() {
        super();
    }

    public ProductoPerecible(int id, String codigo, String descripcion, String categoria,
                             BigDecimal precio, int stock, int stockMinimo,
                             LocalDate fechaVencimiento, String lote) {
        super(id, codigo, descripcion, categoria, precio, stock, stockMinimo);
        this.fechaVencimiento = fechaVencimiento;
        this.lote = lote;
    }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }

    public long getDiasParaVencer() {
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    public boolean isVencido() {
        return fechaVencimiento.isBefore(LocalDate.now());
    }

    @Override
    public BigDecimal calcularPrecioVenta() {
        if (isVencido()) {
            return BigDecimal.ZERO;
        }
        if (getDiasParaVencer() <= DIAS_POR_VENCER) {
            return precio.multiply(DESCUENTO_POR_VENCER).setScale(2, RoundingMode.HALF_UP);
        }
        return precio;
    }

    @Override
    public String toString() {
        return super.toString() + " (lote " + lote + ", vence " + fechaVencimiento + ")";
    }
}
