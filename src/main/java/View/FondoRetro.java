package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import javax.swing.JPanel;

// Fondo blanco liso, compartido por el login y el dashboard
public class FondoRetro extends JPanel {

    public FondoRetro(LayoutManager layout) {
        super(layout);
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
