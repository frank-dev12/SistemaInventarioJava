package View;

import Model.ConexionUsuario;
import Model.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PanelUsuarios extends javax.swing.JPanel implements Modulo {

    private final ConexionUsuario dao = new ConexionUsuario();
    private final List<Usuario> usuarios = new ArrayList<>();

    private final PanelCrud crud = new PanelCrud(
            "Usuarios con acceso al sistema",
            "Nuevo usuario", "Eliminar",
            new String[]{"ID", "Usuario", "Nombre", "Rol", "Activo"}) {
        @Override
        protected Color[] colorFila(int fila) {
            Object activo = modelo.getValueAt(fila, 4);
            if ("No".equals(activo)) {
                return new Color[]{Estilo.FILA_INACTIVA, Estilo.GRIS};
            }
            return null;
        }
    };

    public PanelUsuarios() {
        setLayout(new BorderLayout());
        setOpaque(false);
        add(crud, BorderLayout.CENTER);

        crud.getTabla().getColumnModel().getColumn(0).setMaxWidth(60);
        crud.getTabla().getColumnModel().getColumn(2).setPreferredWidth(220);
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
            usuarios.clear();
            usuarios.addAll(dao.listar(crud.getBusqueda()));

            List<Object[]> filas = new ArrayList<>();
            for (Usuario u : usuarios) {
                filas.add(new Object[]{
                    u.getId(), u.getUsuario(), u.getNombre(), u.getRol(),
                    u.isActivo() ? "Sí" : "No"
                });
            }
            crud.setFilas(filas);
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los usuarios", e);
        }
    }

    private void nuevo() {
        JTextField usuario = new JTextField(20);
        JTextField nombre = new JTextField(25);
        JComboBox<String> rol = new JComboBox<>(new String[]{"ADMIN", "ALMACEN"});
        rol.setSelectedItem("ALMACEN");
        JPasswordField clave = new JPasswordField(15);
        JComboBox<String> activo = new JComboBox<>(new String[]{"Sí", "No"});
        estilo(usuario, nombre, clave);

        boolean ok = Formulario.pedir(this, "Nuevo usuario",
                new String[]{"Usuario", "Nombre", "Rol", "Contraseña", "Activo"},
                new JComponent[]{usuario, nombre, rol, clave, activo},
                () -> validar(usuario.getText(), nombre.getText(),
                        new String(clave.getPassword()), true));
        if (!ok) {
            return;
        }

        Usuario u = new Usuario(0, usuario.getText().trim(), nombre.getText().trim(),
                (String) rol.getSelectedItem(), "Sí".equals(activo.getSelectedItem()));
        try {
            dao.insertar(u, new String(clave.getPassword()));
            cargar();
        } catch (SQLException e) {
            mostrarError("No se pudo guardar el usuario", e);
        }
    }

    private void editar() {
        int fila = crud.getFilaSeleccionada();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla.");
            return;
        }
        Usuario base = usuarios.get(fila);

        JTextField usuario = new JTextField(base.getUsuario(), 20);
        JTextField nombre = new JTextField(base.getNombre(), 25);
        JComboBox<String> rol = new JComboBox<>(new String[]{"ADMIN", "ALMACEN"});
        rol.setSelectedItem(base.getRol());
        JPasswordField clave = new JPasswordField(15);
        JComboBox<String> activo = new JComboBox<>(new String[]{"Sí", "No"});
        activo.setSelectedItem(base.isActivo() ? "Sí" : "No");
        estilo(usuario, nombre, clave);

        boolean ok = Formulario.pedir(this, "Editar usuario",
                new String[]{"Usuario", "Nombre", "Rol",
                    "Nueva contraseña (opcional)", "Activo"},
                new JComponent[]{usuario, nombre, rol, clave, activo},
                () -> validar(usuario.getText(), nombre.getText(),
                        new String(clave.getPassword()), false));
        if (!ok) {
            return;
        }

        Usuario u = new Usuario(base.getId(), usuario.getText().trim(), nombre.getText().trim(),
                (String) rol.getSelectedItem(), "Sí".equals(activo.getSelectedItem()));
        try {
            dao.actualizar(u, new String(clave.getPassword()));
            cargar();
        } catch (SQLException e) {
            mostrarError("No se pudo actualizar el usuario", e);
        }
    }

    private void eliminar() {
        int fila = crud.getFilaSeleccionada();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla.");
            return;
        }
        Usuario u = usuarios.get(fila);
        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar definitivamente al usuario \"" + u.getUsuario() + "\"?\n"
                + "Si solo quieres quitarle el acceso, edítalo y ponlo como Inactivo.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.eliminar(u.getId());
            cargar();
        } catch (SQLException e) {
            mostrarError("No se pudo eliminar el usuario", e);
        }
    }

    private String validar(String usuario, String nombre, String clave, boolean esNuevo) {
        if (usuario.isBlank()) return "Ingresa el usuario.";
        if (usuario.trim().length() > 30) return "El usuario admite máximo 30 caracteres.";
        if (nombre.isBlank()) return "Ingresa el nombre.";
        if (nombre.trim().length() > 80) return "El nombre admite máximo 80 caracteres.";
        if (esNuevo && clave.isEmpty()) return "Ingresa una contraseña.";
        if (!clave.isEmpty() && clave.length() < 4) {
            return "La contraseña debe tener al menos 4 caracteres.";
        }
        return null;
    }

    private void estilo(JTextField... campos) {
        for (JTextField campo : campos) {
            Estilo.campo(campo);
        }
    }

    private void mostrarError(String mensaje, Exception e) {
        JOptionPane.showMessageDialog(this, mensaje + ":\n" + e.getMessage(),
                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }

}
