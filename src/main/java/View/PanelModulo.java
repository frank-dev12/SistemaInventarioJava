package View;

import java.awt.BorderLayout;
import java.awt.event.HierarchyEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Base de todos los modulos: fondo transparente sobre el cristal, margenes y un aviso cuando el panel se muestra
public class PanelModulo extends JPanel {

    public PanelModulo() {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 14, 6, 14));
    }

    // Si es true, el Dashboard no agrega el titulo de arriba (el modulo dibuja el suyo)
    public boolean tieneTituloPropio() {
        return false;
    }

    // Se ejecuta cada vez que el usuario abre este modulo (para recargar los datos)
    public void setAlMostrar(Runnable accion) {
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                accion.run();
            }
        });
    }
}
