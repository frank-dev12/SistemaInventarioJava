package View;

import Model.ConexionMovimiento;
import Model.ConexionProducto;
import Model.Movimiento;
import Model.Producto;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PanelEntradasSalidas extends javax.swing.JPanel implements Modulo {

    private final ConexionMovimiento dao = new ConexionMovimiento();
    private final ConexionProducto daoProducto = new ConexionProducto();
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final JComboBox<Producto> cbxProducto = new JComboBox<>();
    private final JComboBox<String> cbxTipo = new JComboBox<>(new String[]{"Entrada", "Salida"});
    private final JTextField txtCantidad = new JTextField(6);
    private final JTextField txtObservacion = new JTextField(22);
    private final JButton btnRegistrar = new JButton("Registrar movimiento");

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Producto", "Tipo", "Cantidad", "Fecha", "Observación"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    public PanelEntradasSalidas() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 14, 6, 14));

        cbxProducto.setPreferredSize(new Dimension(280, 32));
        cbxTipo.setPreferredSize(new Dimension(100, 32));
        txtCantidad.setPreferredSize(new Dimension(70, 32));
        txtObservacion.setPreferredSize(new Dimension(220, 32));
        Estilo.campo(txtCantidad);
        Estilo.campo(txtObservacion);

        Estilo.botonNuevo(btnRegistrar);
        btnRegistrar.addActionListener(e -> registrar());

        Estilo.tabla(tabla);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(260);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(220);
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
        JLabel titulo = Estilo.titulo("Entradas y salidas de inventario");

        JLabel subtitulo = new JLabel(
                "Registra un movimiento; el stock del producto se actualiza automáticamente.");
        subtitulo.setForeground(Estilo.GRIS);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 4));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);

        // Dos filas fijas: FlowLayout no reserva alto para una fila que se desborda
        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        fila1.setOpaque(false);
        fila1.add(etiqueta("Producto:"));
        fila1.add(cbxProducto);
        fila1.add(etiqueta("Tipo:"));
        fila1.add(cbxTipo);
        fila1.add(etiqueta("Cantidad:"));
        fila1.add(txtCantidad);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        fila2.setOpaque(false);
        fila2.add(etiqueta("Observación:"));
        fila2.add(txtObservacion);
        fila2.add(btnRegistrar);

        JPanel formulario = new JPanel(new GridLayout(2, 1));
        formulario.setOpaque(false);
        formulario.add(fila1);
        formulario.add(fila2);

        JPanel cabecera = new JPanel(new BorderLayout(0, 8));
        cabecera.setOpaque(false);
        cabecera.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        cabecera.add(textos, BorderLayout.NORTH);
        cabecera.add(formulario, BorderLayout.CENTER);
        return cabecera;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Estilo.GRIS);
        return l;
    }

    private void cargar() {
        try {
            Producto seleccionado = (Producto) cbxProducto.getSelectedItem();
            int idSeleccionado = seleccionado == null ? -1 : seleccionado.getId();

            List<Producto> productos = daoProducto.listar("");
            cbxProducto.removeAllItems();
            for (Producto p : productos) {
                cbxProducto.addItem(p);
                if (p.getId() == idSeleccionado) {
                    cbxProducto.setSelectedItem(p);
                }
            }

            modelo.setRowCount(0);
            for (Movimiento m : dao.listar()) {
                modelo.addRow(new Object[]{
                    m.id(), m.codigoProducto() + " - " + m.descripcionProducto(),
                    m.getTipoTexto(), m.cantidad(),
                    formatoFecha.format(m.fecha()), m.observacion()
                });
            }
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los movimientos", e);
        }
    }

    private void registrar() {
        Producto producto = (Producto) cbxProducto.getSelectedItem();
        if (producto == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero registra un producto en la sección Productos.");
            return;
        }

        Integer cantidad = Formulario.entero(txtCantidad.getText());
        if (cantidad == null || cantidad <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La cantidad debe ser un número entero mayor que cero.");
            return;
        }

        String tipo = "Entrada".equals(cbxTipo.getSelectedItem()) ? "E" : "S";
        try {
            dao.registrar(producto.getId(), tipo, cantidad, txtObservacion.getText().trim());
            txtCantidad.setText("");
            txtObservacion.setText("");
            cargar();
            JOptionPane.showMessageDialog(this, "Movimiento registrado correctamente.");
        } catch (SQLException e) {
            mostrarError("No se pudo registrar el movimiento", e);
        }
    }

    private void mostrarError(String mensaje, Exception e) {
        JOptionPane.showMessageDialog(this, mensaje + ":\n" + e.getMessage(),
                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }

}
