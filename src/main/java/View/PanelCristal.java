package View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Tarjeta blanca con sombra suave y borde gris que flota sobre el fondo
public class PanelCristal extends JPanel {
    // Espacio reservado alrededor de la tarjeta para dibujar la sombra
    private static final int MARGEN = 10;
    private static final int CAIDA = 6;

    private final int arco;

    public PanelCristal(LayoutManager layout, int arco, int relleno) {
        super(layout);
        this.arco = arco;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(MARGEN + relleno, MARGEN + relleno,
                MARGEN + CAIDA + relleno, MARGEN + relleno));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = MARGEN;
        int y = MARGEN;
        int w = getWidth() - 2 * MARGEN;
        int h = getHeight() - 2 * MARGEN - CAIDA;
        RoundRectangle2D forma = new RoundRectangle2D.Float(x, y, w, h, arco, arco);

        // La sombra solo se ve por fuera, para que la tarjeta no se ensucie
        Area fuera = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
        fuera.subtract(new Area(forma));
        g2.setClip(fuera);
        Estilo.sombra(g2, x, y + CAIDA, w, h, arco, MARGEN);
        g2.setClip(null);

        g2.setColor(Color.WHITE);
        g2.fill(forma);

        g2.setStroke(new BasicStroke(1f));
        g2.setColor(Estilo.BORDE);
        g2.draw(new RoundRectangle2D.Float(x + 0.5f, y + 0.5f, w - 1, h - 1, arco, arco));

        g2.dispose();
    }
}
