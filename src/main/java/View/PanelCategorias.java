package View;

import Model.Categoria;
import Model.ConexionCategoria;
import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PanelCategorias extends javax.swing.JPanel implements Modulo {

    private final ConexionCategoria dao = new ConexionCategoria();
    private final List<Categoria> categorias = new ArrayList<>();

    private final PanelCrud crud = new PanelCrud(
            "Administra las categorías de productos",
            "Nueva categoría", "Eliminar",
            new String[]{"ID", "Nombre", "Productos"});

    public PanelCategorias() {
        setLayout(new BorderLayout());
        setOpaque(false);
        add(crud, BorderLayout.CENTER);

        crud.getTabla().getColumnModel().getColumn(0).setMaxWidth(60);
        crud.getTabla().getColumnModel().getColumn(1).setPreferredWidth(300);
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
            List<Categoria> todas = dao.listar();
            String filtro = crud.getBusqueda().toLowerCase();

            categorias.clear();
            List<Object[]> filas = new ArrayList<>();
            for (Categoria c : todas) {
                if (filtro.isEmpty() || c.getNombre().toLowerCase().contains(filtro)) {
                    categorias.add(c);
                    filas.add(new Object[]{c.getId(), c.getNombre(), c.getCantidadProductos()});
                }
            }
            crud.setFilas(filas);
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar las categorías", e);
        }
    }

    private void nuevo() {
        JTextField nombre = new JTextField(25);
        Estilo.campo(nombre);

        boolean ok = Formulario.pedir(this, "Nueva categoría",
                new String[]{"Nombre"}, new JComponent[]{nombre},
                () -> validar(nombre.getText()));
        if (!ok) {
            return;
        }

        try {
            dao.insertar(nombre.getText().trim());
            cargar();
        } catch (SQLException e) {
            mostrarError("No se pudo guardar la categoría", e);
        }
    }

    private void editar() {
        int fila = crud.getFilaSeleccionada();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría de la tabla.");
            return;
        }
        Categoria c = categorias.get(fila);

        JTextField nombre = new JTextField(c.getNombre(), 25);
        Estilo.campo(nombre);

        boolean ok = Formulario.pedir(this, "Editar categoría",
                new String[]{"Nombre"}, new JComponent[]{nombre},
                () -> validar(nombre.getText()));
        if (!ok) {
            return;
        }

        try {
            dao.actualizar(c.getId(), nombre.getText().trim());
            cargar();
        } catch (SQLException e) {
            mostrarError("No se pudo actualizar la categoría", e);
        }
    }

    private void eliminar() {
        int fila = crud.getFilaSeleccionada();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría de la tabla.");
            return;
        }
        Categoria c = categorias.get(fila);
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la categoría \"" + c.getNombre() + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.eliminar(c.getId());
            cargar();
        } catch (SQLException e) {
            mostrarError("No se pudo eliminar la categoría", e);
        }
    }

    private String validar(String nombre) {
        if (nombre.isBlank()) return "Ingresa el nombre de la categoría.";
        if (nombre.trim().length() > 40) return "El nombre admite máximo 40 caracteres.";
        return null;
    }

    private void mostrarError(String mensaje, Exception e) {
        JOptionPane.showMessageDialog(this, mensaje + ":\n" + e.getMessage(),
                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }

}
