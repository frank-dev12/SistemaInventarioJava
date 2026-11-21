package View;

import Model.Cliente;
import Model.ConexionCliente;
import Model.ConsultarRuc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelClientes extends JPanel {

    private final JTextField txtId = new JTextField();
    private final JTextField txtRuc = new JTextField();
    private final JTextField txtRazonSocial = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtCorreo = new JTextField();

    private final JLabel lblEstadoSunat = new JLabel("SUNAT: -");

    private final JButton btnBuscarSunat = new JButton("Buscar Ruc ");
    private final JButton btnRegistrar = new JButton("Registrar");
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnLimpiar = new JButton("Limpiar");

    private DefaultTableModel modeloTabla = new DefaultTableModel();
    private JTable tablaClientes = new JTable();

    private final ConexionCliente conexionCliente = new ConexionCliente();
    private final ConsultarRuc consultarRuc = new ConsultarRuc();

    public PanelClientes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("CLIENTES");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout(10, 15));
        panelCentro.add(crearPanelFormulario(), BorderLayout.NORTH);
        panelCentro.add(crearPanelTabla(), BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        configurarEventos();
        cargarTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(BorderFactory.createTitledBorder("Datos del cliente"));

        txtId.setEditable(false);

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: ID | RUC + Buscar en SUNAT
        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0;
        grid.add(new JLabel("ID"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.25;
        grid.add(txtId, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        grid.add(new JLabel("RUC"), gbc);

        JPanel panelRuc = new JPanel(new BorderLayout(6, 0));
        panelRuc.add(txtRuc, BorderLayout.CENTER);
        panelRuc.add(btnBuscarSunat, BorderLayout.EAST);

        gbc.gridx = 3; gbc.weightx = 0.75;
        grid.add(panelRuc, gbc);

        // Fila 1: Razón Social
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0;
        grid.add(new JLabel("Razón Social"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        grid.add(txtRazonSocial, gbc);
        gbc.gridwidth = 1;

        // Fila 2: Dirección
        gbc.gridy = 2;
        gbc.gridx = 0; gbc.weightx = 0;
        grid.add(new JLabel("Dirección"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        grid.add(txtDireccion, gbc);
        gbc.gridwidth = 1;

        // Fila 3: Teléfono | Correo
        gbc.gridy = 3;
        gbc.gridx = 0; gbc.weightx = 0;
        grid.add(new JLabel("Teléfono"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.35;
        grid.add(txtTelefono, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        grid.add(new JLabel("Correo"), gbc);

        gbc.gridx = 3; gbc.weightx = 0.65;
        grid.add(txtCorreo, gbc);

        // Fila 4: Estado SUNAT
        gbc.gridy = 4;
        gbc.gridx = 1; gbc.gridwidth = 3;
        lblEstadoSunat.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblEstadoSunat.setForeground(new Color(90, 100, 110));
        grid.add(lblEstadoSunat, gbc);

        // Fila 5: Botones CRUD
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        contenedor.add(grid, BorderLayout.CENTER);
        contenedor.add(panelBotones, BorderLayout.SOUTH);
        return contenedor;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Lista de clientes"));

        String[] columnas = {"ID", "RUC", "Razón Social", "Dirección", "Teléfono", "Correo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
        return panel;
    }

    private void configurarEventos() {
        btnBuscarSunat.addActionListener(e -> buscarEnSunat());
        btnRegistrar.addActionListener(e -> registrarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaClientes.getSelectedRow();
                if (fila != -1) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtRuc.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtRazonSocial.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtDireccion.setText(valOrEmpty(modeloTabla.getValueAt(fila, 3)));
                    txtTelefono.setText(valOrEmpty(modeloTabla.getValueAt(fila, 4)));
                    txtCorreo.setText(valOrEmpty(modeloTabla.getValueAt(fila, 5)));
                }
            }
        });
    }

    private void buscarEnSunat() {
        String doc = txtRuc.getText().trim();
        btnBuscarSunat.setEnabled(false);
        lblEstadoSunat.setText("SUNAT: Consultando...");

        new SwingWorker<Cliente, Void>() {
            @Override
            protected Cliente doInBackground() throws Exception {
                return consultarRuc.consultar(doc);
            }

            @Override
            protected void done() {
                btnBuscarSunat.setEnabled(true);
                try {
                    Cliente res = get();
                    txtRuc.setText(res.getRuc());
                    txtRazonSocial.setText(res.getRazonSocial());
                    if (res.getDireccion() != null && !res.getDireccion().isEmpty()) {
                        txtDireccion.setText(res.getDireccion());
                    }
                    String estado = res.getEstadoSunat() == null ? "-" : res.getEstadoSunat();
                    String condicion = res.getCondicionSunat() == null ? "-" : res.getCondicionSunat();
                    lblEstadoSunat.setText("SUNAT: " + estado + " - " + condicion);
                } catch (Exception ex) {
                    lblEstadoSunat.setText("SUNAT: Error en consulta");
                    JOptionPane.showMessageDialog(
                            PanelClientes.this,
                            ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(),
                            "Consulta SUNAT",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        }.execute();
    }

    private void registrarCliente() {
        if (!validarCampos()) return;
        try {
            Cliente c = obtenerClienteFormulario(0);
            if (conexionCliente.registrar(c)) {
                JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
                cargarTabla();
                limpiarCampos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCliente() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la lista para actualizar.");
            return;
        }
        if (!validarCampos()) return;
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            Cliente c = obtenerClienteFormulario(id);
            if (conexionCliente.actualizar(c)) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
                cargarTabla();
                limpiarCampos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la lista para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar este cliente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                if (conexionCliente.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado.");
                    cargarTabla();
                    limpiarCampos();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Cliente> lista = conexionCliente.listar();
            for (Cliente c : lista) {
                modeloTabla.addRow(new Object[]{
                        c.getId(),
                        c.getRuc(),
                        c.getRazonSocial(),
                        c.getDireccion(),
                        c.getTelefono(),
                        c.getCorreo()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        String ruc = txtRuc.getText().trim();
        String razon = txtRazonSocial.getText().trim();
        if (!ruc.matches("\\d{8}|\\d{11}")) {
            JOptionPane.showMessageDialog(this, "El RUC/DNI debe tener 8 u 11 dígitos numéricos.");
            return false;
        }
        if (razon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La Razón Social es obligatoria.");
            return false;
        }
        return true;
    }

    private Cliente obtenerClienteFormulario(int id) {
        return new Cliente(
                id,
                txtRuc.getText().trim(),
                txtRazonSocial.getText().trim(),
                txtDireccion.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim()
        );
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtRuc.setText("");
        txtRazonSocial.setText("");
        txtDireccion.setText("");
        txtTelefono.getText();
        txtTelefono.setText("");
        txtCorreo.setText("");
        lblEstadoSunat.setText("SUNAT: -");
        tablaClientes.clearSelection();
    }

    private String valOrEmpty(Object o) {
        return o == null ? "" : o.toString();
    }
}