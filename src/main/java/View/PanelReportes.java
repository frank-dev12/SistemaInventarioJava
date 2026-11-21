package View;

import Model.ConexionReporte;
import Model.Producto;
import Model.Resumen;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelReportes extends javax.swing.JPanel implements Modulo {

    private final ConexionReporte dao = new ConexionReporte();

    private final JLabel lblProductos = valor();
    private final JLabel lblStockBajo = valor();
    private final JLabel lblCategorias = valor();
    private final JLabel lblMovimientos = valor();
    private final JLabel lblValor = valor();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Código", "Descripción", "Categoría", "Stock", "Stock mínimo"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    public PanelReportes() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 14, 6, 14));

        Estilo.tabla(tabla);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(260);
        JScrollPane scroll = Estilo.scroll(tabla);

        add(crearCabecera(), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        cargar();
    }

    @Override
    public void recargar() {
        cargar();
    }

    private JPanel crearCabecera() {
        JLabel titulo = Estilo.titulo("Reportes de inventario");

        JLabel subtitulo = new JLabel(
                "Resumen general y productos que llegaron a su stock mínimo.");
        subtitulo.setForeground(Estilo.GRIS);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 4));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);

        JPanel tarjetas = new JPanel(new GridLayout(1, 5, 4, 4));
        tarjetas.setOpaque(false);
        tarjetas.add(tarjeta("Productos", lblProductos));
        tarjetas.add(tarjeta("Con stock bajo", lblStockBajo));
        tarjetas.add(tarjeta("Categorías", lblCategorias));
        tarjetas.add(tarjeta("Movimientos hoy", lblMovimientos));
        tarjetas.add(tarjeta("Valor inventario", lblValor));

        JButton btnActualizar = new JButton("Actualizar reporte");
        Estilo.botonNuevo(btnActualizar);
        btnActualizar.addActionListener(e -> cargar());

        JPanel acciones = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));
        acciones.setOpaque(false);
        acciones.add(btnActualizar);

        JPanel cabecera = new JPanel(new BorderLayout(0, 12));
        cabecera.setOpaque(false);
        cabecera.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        cabecera.add(textos, BorderLayout.NORTH);
        cabecera.add(tarjetas, BorderLayout.CENTER);
        cabecera.add(acciones, BorderLayout.SOUTH);
        return cabecera;
    }

    private JPanel tarjeta(String titulo, JLabel valor) {
        PanelCristal p = new PanelCristal(new BorderLayout(0, 4), 18, 8);
        JLabel l = new JLabel(titulo);
        l.setForeground(Estilo.GRIS);
        p.add(valor, BorderLayout.CENTER);
        p.add(l, BorderLayout.SOUTH);
        return p;
    }

    private static JLabel valor() {
        JLabel l = new JLabel("-");
        l.setFont(new Font("Segoe UI", Font.BOLD, 20));
        l.setForeground(Estilo.TITULO);
        return l;
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

            modelo.setRowCount(0);
            List<Producto> bajos = dao.stockBajo();
            for (Producto p : bajos) {
                modelo.addRow(new Object[]{
                    p.getCodigo(), p.getDescripcion(), p.getCategoria(),
                    p.getStock(), p.getStockMinimo()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los reportes:\n" + e.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
