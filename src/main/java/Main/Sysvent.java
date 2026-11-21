package Main;

import Controller.ControllerLogin;
import Model.ConexionUsuario;
import View.Estilo;
import View.Vista;
import javax.swing.SwingUtilities;

public class Sysvent {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Estilo.instalar();
            new ControllerLogin(new Vista(), new ConexionUsuario()).iniciar();
        });
    }
}
