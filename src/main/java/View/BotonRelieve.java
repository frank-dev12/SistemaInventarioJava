package View;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicButtonUI;

// Boton con relieve: degradado, reflejo de cristal, borde luminoso y sombra que se hunde al presionar
public class BotonRelieve extends BasicButtonUI {
    public static final String MENU = "menu";
    public static final String ACTIVO = "activo";
    public static final String HOVER = "hover";

    private static final int ARCO = 14;
    private static final int RELIEVE = 4;

    public static ComponentUI createUI(JComponent c) {
        // Las flechas de combos y barras se dibujan solas
        if (c instanceof BasicArrowButton) {
            return BasicButtonUI.createUI(c);
        }
        return new BotonRelieve();
    }

    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        LookAndFeel.installProperty(b, "opaque", false);
        LookAndFeel.installProperty(b, "rolloverEnabled", true);
        b.setFocusPainted(false);
        if (b.getBorder() == null || b.getBorder() instanceof UIResource) {
            b.setBorder(new BorderUIResource.EmptyBorderUIResource(7, 18, 7 + RELIEVE, 18));
        }
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!b.isEnabled()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
        }
        if (esMenu(b)) {
            pintarMenu(g2, b);
        } else {
            pintarRelieve(g2, b);
        }
        g2.dispose();
        super.paint(g, c);
    }

    private void pintarRelieve(Graphics2D g2, AbstractButton b) {
        ButtonModel m = b.getModel();
        boolean hundido = m.isArmed() && m.isPressed();
        Color base = b.getBackground();
        if (m.isRollover() && b.getClientProperty(HOVER) instanceof Color hover) {
            base = hover;
        }

        int w = b.getWidth();
        int h = b.getHeight() - RELIEVE;
        int y = hundido ? RELIEVE - 1 : 0;

        if (!hundido) {
            for (int i = RELIEVE; i >= 1; i--) {
                g2.setColor(new Color(0, 4, 16, 30));
                g2.fillRoundRect(0, i, w, h, ARCO, ARCO);
            }
        }

        g2.setColor(Estilo.mezclar(base, Color.BLACK, 0.45f));
        g2.fillRoundRect(0, y + 2, w - 1, h - 2, ARCO, ARCO);

        RoundRectangle2D cuerpo = new RoundRectangle2D.Float(0, y, w - 1, h - 2, ARCO, ARCO);
        g2.setPaint(new GradientPaint(0, y, Estilo.mezclar(base, Color.WHITE, 0.22f),
                0, y + h, Estilo.mezclar(base, Color.BLACK, 0.12f)));
        g2.fill(cuerpo);

        g2.clip(cuerpo);
        g2.setPaint(new GradientPaint(0, y, new Color(255, 255, 255, 85),
                0, y + h / 2f, new Color(255, 255, 255, 0)));
        g2.fillRect(0, y, w, h / 2);
        g2.setClip(null);

        Color borde = Estilo.mezclar(base, Color.WHITE, 0.55f);
        g2.setColor(new Color(borde.getRed(), borde.getGreen(), borde.getBlue(),
                m.isRollover() ? 235 : 150));
        g2.drawRoundRect(0, y, w - 1, h - 3, ARCO, ARCO);
    }

    private void pintarMenu(Graphics2D g2, AbstractButton b) {
        int w = b.getWidth();
        int h = b.getHeight();
        if (Boolean.TRUE.equals(b.getClientProperty(ACTIVO))) {
            RoundRectangle2D forma = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, ARCO, ARCO);
            g2.setPaint(new GradientPaint(0, 0, new Color(40, 140, 230, 215),
                    w, 0, new Color(110, 100, 250, 160)));
            g2.fill(forma);
            g2.clip(forma);
            g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 60),
                    0, h / 2f, new Color(255, 255, 255, 0)));
            g2.fillRect(0, 0, w, h / 2);
            g2.setClip(null);
            g2.setColor(new Color(170, 225, 255, 210));
            g2.draw(forma);
            g2.setColor(new Color(255, 255, 255, 200));
            g2.fillRoundRect(5, h / 4, 3, h / 2, 3, 3);
        } else if (b.getModel().isRollover()) {
            g2.setColor(new Color(15, 23, 42, 12));
            g2.fillRoundRect(0, 0, w - 1, h - 1, ARCO, ARCO);
            g2.setColor(new Color(37, 99, 235, 70));
            g2.drawRoundRect(0, 0, w - 1, h - 1, ARCO, ARCO);
        }
    }

    @Override
    protected void paintText(Graphics g, AbstractButton b, Rectangle r, String text) {
        ButtonModel m = b.getModel();
        if (!esMenu(b) && m.isArmed() && m.isPressed()) {
            r = new Rectangle(r.x, r.y + RELIEVE - 1, r.width, r.height);
        }
        super.paintText(g, b, r, text);
    }

    private static boolean esMenu(AbstractButton b) {
        return Boolean.TRUE.equals(b.getClientProperty(MENU));
    }
}
