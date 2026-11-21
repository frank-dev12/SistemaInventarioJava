package Controller;

import Model.ConexionUsuario;
import Model.Usuario;
import View.Dashboard;
import View.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ControllerLogin implements ActionListener {
    private final Vista vista;
    private final ConexionUsuario modelo;

    public ControllerLogin(Vista vista, ConexionUsuario modelo) {
        this.vista = vista;
        this.modelo = modelo;
        vista.addLoginListener(this);
    }

    public void iniciar() {
        vista.limpiar();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String usuario = vista.getUsuario();
        String clave = vista.getClave();

        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Completa usuario y contraseña");
            return;
        }
        try {
            Usuario u = modelo.validar(usuario, clave);
            if (u == null) {
                JOptionPane.showMessageDialog(vista, "Usuario o contraseña incorrectos");
            } else {
                abrirDashboard(u);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error de base de datos: " + ex.getMessage());
        }
    }

    private void abrirDashboard(Usuario u) {
        Dashboard dashboard = new Dashboard();
        dashboard.setUsuario(u);
        dashboard.addCerrarListener(e -> {
            dashboard.dispose();
            iniciar();
        });
        vista.setVisible(false);
        dashboard.setLocationRelativeTo(null);
        dashboard.setVisible(true);
    }
}
