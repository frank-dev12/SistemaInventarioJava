package View;

import Model.ConexionReporte;
import Model.Resumen;
import Model.Usuario;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelInicio extends javax.swing.JPanel implements Modulo {

    private final ConexionReporte dao = new ConexionReporte();

    private final JLabel lblBienvenida = new JLabel("Bienvenido");
    private final JLabel lblProductos = valor();
    private final JLabel lblStockBajo = valor();
    private final JLabel lblCategorias = valor();
    private final JLabel lblMovimientos = valor();
    private final JLabel lblValor = valor();
    private final JLabel lblUsuarios = valor();

    public PanelInicio() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 10, 14));

        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblBienvenida.setForeground(Estilo.TITULO);
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(0, 6, 14, 0));

        JPanel rejilla = new JPanel(new GridLayout(2, 3, 6, 6));
        rejilla.setOpaque(false);
        rejilla.add(tarjeta("Productos registrados", lblProductos));
        rejilla.add(tarjeta("Productos con stock bajo", lblStockBajo));
        rejilla.add(tarjeta("Categorías", lblCategorias));
        rejilla.add(tarjeta("Movimientos de hoy", lblMovimientos));
        rejilla.add(tarjeta("Valor del inventario", lblValor));
        rejilla.add(tarjeta("Usuarios activos", lblUsuarios));

        JLabel ayuda = new JLabel(
                "Usa el menú de la izquierda para abrir Productos, Categorías, "
                + "Entradas/Salidas, Reportes o Usuarios.");
        ayuda.setForeground(Estilo.GRIS);
        ayuda.setBorder(BorderFactory.createEmptyBorder(14, 6, 0, 0));

        add(lblBienvenida, BorderLayout.NORTH);
        add(rejilla, BorderLayout.CENTER);
        add(ayuda, BorderLayout.SOUTH);

        cargar();
    }

    public void setUsuario(Usuario u) {
        lblBienvenida.setText("Bienvenido, " + u.getNombre());
    }

    @Override
    public void recargar() {
        cargar();
    }

    private void cargar() {
        try {
            Resumen r = dao.resumen();
            lblProductos.setText(String.valueOf(r.productos()));
            lblStockBajo.setText(String.valueOf(r.stockBajo()));
            lblStockBajo.setForeground(r.stockBajo() > 0 ? Estilo.TEXTO_ROJO : Estilo.TITULO);
            lblCategorias.setText(String.valueOf(r.categorias()));
            lblMovimientos.setText(String.valueOf(r.movimientosHoy()));
            lblValor.setText(Estilo.dinero(r.valorInventario()));
            lblUsuarios.setText(String.valueOf(r.usuariosActivos()));
        } catch (SQLException e) {
            lblProductos.setText("-");
            lblStockBajo.setText("-");
            lblCategorias.setText("-");
            lblMovimientos.setText("-");
            lblValor.setText("-");
            lblUsuarios.setText("-");
        }
    }

    private JPanel tarjeta(String titulo, JLabel valor) {
        PanelCristal p = new PanelCristal(new BorderLayout(0, 6), 20, 16);
        JLabel l = new JLabel(titulo);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(Estilo.GRIS);
        p.add(valor, BorderLayout.CENTER);
        p.add(l, BorderLayout.SOUTH);
        return p;
    }

    private static JLabel valor() {
        JLabel l = new JLabel("-");
        l.setFont(new Font("Segoe UI", Font.BOLD, 32));
        l.setForeground(Estilo.TITULO);
        return l;
    }
}
