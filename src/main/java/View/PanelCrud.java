package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

// Pantalla comun de Productos, Categorias y Usuarios: buscador, tres botones y una tabla
public class PanelCrud extends PanelModulo {
    protected final JTextField txtBuscar = new JTextField(20);
    protected final JButton btnNuevo = new JButton();
    protected final JButton btnEditar = new JButton("Editar");
    protected final JButton btnEliminar = new JButton();
    protected final DefaultTableModel modelo;
    protected final JTable tabla;

    public PanelCrud(String subtitulo, String textoNuevo, String textoEliminar, String[] columnas) {
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modelo) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int fila, int columna) {
                Component c = super.prepareRenderer(r, fila, columna);
                if (!isRowSelected(fila)) {
                    Color[] colores = colorFila(fila);
                    if (colores != null) {
                        c.setBackground(colores[0]);
                        c.setForeground(colores[1]);
                    }
                }
                return c;
            }
        };
        Estilo.tabla(tabla);

        btnNuevo.setText(textoNuevo);
        btnEliminar.setText(textoEliminar);
        Estilo.botonNuevo(btnNuevo);
        Estilo.botonGris(btnEditar);
        Estilo.botonRojo(btnEliminar);
        Estilo.campo(txtBuscar);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setForeground(Estilo.GRIS);

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        izquierda.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(Estilo.GRIS);
        izquierda.add(lblBuscar);
        izquierda.add(txtBuscar);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        derecha.setOpaque(false);
        derecha.add(btnNuevo);
        derecha.add(btnEditar);
        derecha.add(btnEliminar);

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);
        barra.add(izquierda, BorderLayout.WEST);
        barra.add(derecha, BorderLayout.EAST);

        JPanel arriba = new JPanel(new BorderLayout(0, 12));
        arriba.setOpaque(false);
        arriba.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        arriba.add(lblSub, BorderLayout.NORTH);
        arriba.add(barra, BorderLayout.CENTER);

        JScrollPane scroll = Estilo.scroll(tabla);

        add(arriba, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    // Las subclases devuelven {fondo, texto} para pintar una fila de otro color, o null
    protected Color[] colorFila(int fila) {
        return null;
    }

    protected void setFilas(List<Object[]> filas) {
        modelo.setRowCount(0);
        for (Object[] f : filas) {
            modelo.addRow(f);
        }
    }

    public int getFilaSeleccionada() {
        return tabla.getSelectedRow();
    }

    public Object getValor(int fila, int columna) {
        return modelo.getValueAt(fila, columna);
    }

    public JTable getTabla() {
        return tabla;
    }

    public String getBusqueda() {
        return txtBuscar.getText().trim();
    }

    public void setPuedeEliminar(boolean puede) {
        btnEliminar.setVisible(puede);
    }

    public void addBusquedaListener(Runnable accion) {
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { accion.run(); }
            public void removeUpdate(DocumentEvent e) { accion.run(); }
            public void changedUpdate(DocumentEvent e) { accion.run(); }
        });
    }

    public void addNuevoListener(Runnable accion) {
        btnNuevo.addActionListener(e -> accion.run());
    }

    // El boton Editar y el doble clic sobre una fila hacen lo mismo
    public void addEditarListener(Runnable accion) {
        btnEditar.addActionListener(e -> accion.run());
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabla.getSelectedRow() >= 0) {
                    accion.run();
                }
            }
        });
    }

    public void addEliminarListener(Runnable accion) {
        btnEliminar.addActionListener(e -> accion.run());
    }
}
