package View;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.function.Supplier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// Ventana sencilla con etiquetas y campos. Se repite hasta que los datos sean validos o se cancele.
public class Formulario {

    // validar devuelve null si todo esta bien, o el mensaje de error a mostrar
    public static boolean pedir(Component padre, String titulo, String[] etiquetas,
                                JComponent[] campos, Supplier<String> validar) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < etiquetas.length; i++) {
            c.gridy = i;
            c.gridx = 0;
            c.weightx = 0;
            c.fill = GridBagConstraints.NONE;
            panel.add(new JLabel(etiquetas[i]), c);
            c.gridx = 1;
            c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(campos[i], c);
        }
        while (true) {
            int r = JOptionPane.showConfirmDialog(padre, panel, titulo,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (r != JOptionPane.OK_OPTION) {
                return false;
            }
            String error = validar.get();
            if (error == null) {
                return true;
            }
            JOptionPane.showMessageDialog(padre, error, titulo, JOptionPane.WARNING_MESSAGE);
        }
    }

    public static Integer entero(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal decimal(String texto) {
        try {
            return new BigDecimal(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
