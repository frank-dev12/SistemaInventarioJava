package View;

import Model.ConexionProducto;
import Model.Producto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PanelProductos extends javax.swing.JPanel implements Modulo {

    private final ConexionProducto dao = new ConexionProducto();
    private final List<Producto> productos = new ArrayList<>();

    private final PanelCrud crud = new PanelCrud(
            "Administra el catálogo de productos",
            "Nuevo Producto", "Eliminar",
            new String[]{"ID", "Código", "Descripción", "Categoría",
                "Precio", "Stock", "Stock mínimo"}) {
        @Override
        protected Color[] colorFila(int fila) {
            Object stock = modelo.getValueAt(fila, 5);
            Object minimo = modelo.getValueAt(fila, 6);
            if (stock instanceof Number s && minimo instanceof Number m
                    && s.intValue() <= m.intValue()) {
                return new Color[]{Estilo.FILA_ROJA, Estilo.TEXTO_ROJO};
            }
            return null;
        }
    };

    public PanelProductos() {
        setLayout(new BorderLayout());
        setOpaque(false);
        add(crud, BorderLayout.CENTER);

        crud.getTabla().getColumnModel().getColumn(0).setMaxWidth(50);
        crud.getTabla().getColumnModel().getColumn(2).setPreferredWidth(240);
        crud.addBusquedaListener(this::cargar);
        crud.addNuevoListener(this::nuevo);
        crud.addEditarListener(this::editar);
        crud.addEliminarListener(this::eliminar);

        cargar();
    }

    @Override
    public void recargar() {
        cargar();
    }

    private void cargar() {
        try {
            productos.clear();
            productos.addAll(dao.listar(crud.getBusqueda()));

            List<Object[]> filas = new ArrayList<>();
            for (Producto p : productos) {
                filas.add(new Object[]{
                    p.getId(), p.getCodigo(), p.getDescripcion(), p.getCategoria(),
                    Estilo.dinero(p.getPrecio()), p.getStock(), p.getStockMinimo()
                });
            }
            crud.setFilas(filas);
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los productos", e);
        }
    }
    //Nuevo Producto 
    private void nuevo() {
        List<String> categorias = categorias();
        if (categorias.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Primero crea al menos una categoría en la sección Categorías.",
                    "Sin categorías", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField codigo = new JTextField(10);
        JTextField descripcion = new JTextField(25);
        JComboBox<String> categoria = new JComboBox<>(categorias.toArray(new String[0]));
        JTextField precio = new JTextField(10);
        JTextField stock = new JTextField(6);
        JTextField minimo = new JTextField(6);
        aplicarEstilo(codigo, descripcion, precio, stock, minimo);

        boolean ok = Formulario.pedir(this, "Nuevo producto",
                new String[]{"Código", "Descripción", "Categoría", "Precio",
                    "Stock", "Stock mínimo"},
                new JComponent[]{codigo, descripcion, categoria, precio, stock, minimo},
                () -> validar(codigo.getText(), descripcion.getText(),
                        (String) categoria.getSelectedItem(), precio.getText(),
                        stock.getText(), minimo.getText()));
        if (!ok) {
            return;
        }

        try {
            dao.insertar(new Producto(0,
                    codigo.getText().trim(),
                    descripcion.getText().trim(),
                    (String) categoria.getSelectedItem(),
                    Formulario.decimal(precio.getText()),
                    Formulario.entero(stock.getText()),
                    Formulario.entero(minimo.getText())));
            cargar();
        } catch (SQLException e) {
            mostrarError("No se pudo guardar el producto", e);
        }
    }

    private void editar() {
        int fila = crud.getFilaSeleccionada();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.");
            return;
        }
        Producto p = productos.get(fila);

        List<String> categorias = categorias();
        if (!categorias.contains(p.getCategoria())) {
            categorias.add(p.getCategoria());
        }

        JTextField codigo = new JTextField(p.getCodigo(), 10);
        JTextField descripcion = new JTextField(p.getDescripcion(), 25);
        JComboBox<String> categoria = new JComboBox<>(categorias.toArray(new String[0]));
        categoria.setSelectedItem(p.getCategoria());
        JTextField precio = new JTextField(p.getPrecio().toPlainString(), 10);
        JTextField stock = new JTextField(String.valueOf(p.getStock()), 6);
        JTextField minimo = new JTextField(String.valueOf(p.getStockMinimo()), 6);
        aplicarEstilo(codigo, descripcion, precio, stock, minimo);

        boolean ok = Formulario.pedir(this, "Editar producto",
                new String[]{"Código", "Descripción", "Categoría", "Precio",
                    "Stock", "Stock mínimo"},
                new JComponent[]{codigo, descripcion, categoria, precio, stock, minimo},
                () -> validar(codigo.getText(), descripcion.getText(),
                        (String) categoria.getSelectedItem(), precio.getText(),
                        stock.getText(), minimo.getText()));
        if (!ok) {
            return;
        }

        try {
            dao.actualizar(new Producto(p.getId(),
                    codigo.getText().trim(),
                    descripcion.getText().trim(),
                    (String) categoria.getSelectedItem(),
                    Formulario.decimal(precio.getText()),
                    Formulario.entero(stock.getText()),
                    Formulario.entero(minimo.getText())));
            cargar();
        } catch (SQLException e) {
            mostrarError("No se pudo actualizar el producto", e);
        }
    }

    private void eliminar() {
        int fila = crud.getFilaSeleccionada();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.");
            return;
        }
        Producto p = productos.get(fila);
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el producto " + p.getCodigo() + " - " + p.getDescripcion() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.eliminar(p.getId());
            cargar();
        } catch (SQLException e) {
            mostrarError("No se pudo eliminar el producto", e);
        }
    }

    private List<String> categorias() {
        try {
            return dao.listarCategorias();
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar las categorías", e);
            return new ArrayList<>();
        }
    }

    private String validar(String codigo, String descripcion, String categoria,
                           String precio, String stock, String minimo) {
        if (codigo.isBlank()) return "Ingresa el código del producto.";
        if (codigo.trim().length() > 10) return "El código admite máximo 10 caracteres.";
        if (descripcion.isBlank()) return "Ingresa la descripción.";
        if (descripcion.trim().length() > 80) return "La descripción admite máximo 80 caracteres.";
        if (categoria == null || categoria.isBlank()) return "Selecciona una categoría.";
        BigDecimal p = Formulario.decimal(precio);
        if (p == null || p.signum() < 0) return "El precio debe ser un número mayor o igual a 0.";
        Integer s = Formulario.entero(stock);
        if (s == null || s < 0) return "El stock debe ser un número entero mayor o igual a 0.";
        Integer m = Formulario.entero(minimo);
        if (m == null || m < 0) return "El stock mínimo debe ser un número entero mayor o igual a 0.";
        return null;
    }

    private void aplicarEstilo(JTextField... campos) {
        for (JTextField campo : campos) {
            Estilo.campo(campo);
        }
    }

    private void mostrarError(String mensaje, Exception e) {
        JOptionPane.showMessageDialog(this, mensaje + ":\n" + e.getMessage(),
                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }

}
