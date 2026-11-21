package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

// Colores y detalles de diseño que se repiten en las ventanas (tema claro)
public class Estilo {
    public static final Color FONDO_ARRIBA = new Color(255, 255, 255);
    public static final Color FONDO_ABAJO = new Color(255, 255, 255);
    public static final Color NEON = new Color(37, 99, 235);
    public static final Color TITULO = new Color(15, 23, 42);
    public static final Color TEXTO = new Color(30, 41, 59);
    public static final Color GRIS = new Color(100, 116, 139);

    public static final Color TURQUESA = new Color(24, 150, 222);
    public static final Color TURQUESA_CLARO = new Color(52, 178, 246);
    public static final Color PIZARRA = new Color(52, 72, 118);
    public static final Color PIZARRA_CLARO = new Color(72, 98, 152);
    public static final Color ROJO = new Color(196, 52, 80);
    public static final Color ROJO_CLARO = new Color(224, 72, 102);

    public static final Color CAMPO = new Color(255, 255, 255);
    public static final Color SELECCION = new Color(37, 99, 235);
    public static final Color FILA = new Color(255, 255, 255);
    public static final Color FILA_ALTERNA = new Color(248, 250, 252);
    public static final Color FILA_ROJA = new Color(254, 226, 226);
    public static final Color TEXTO_ROJO = new Color(185, 28, 28);
    public static final Color FILA_INACTIVA = new Color(241, 245, 249);
    public static final Color CABECERA = new Color(241, 245, 249);
    public static final Color BORDE = new Color(203, 213, 225);
    public static final String MONEDA = "$";

    // Paleta del modulo Productos (crema y taupe)
    public static final Color CREMA = new Color(245, 242, 236);
    public static final Color TAUPE = new Color(143, 134, 119);
    public static final Color TAUPE_OSCURO = new Color(117, 109, 95);
    public static final Color TAUPE_CLARO = new Color(230, 224, 213);
    public static final Color TEXTO_TAUPE = new Color(111, 103, 91);

    // Se llama una vez al iniciar, antes de crear ventanas: aplica el tema a dialogos, combos y botones
    public static void instalar() {
        ColorUIResource panel = new ColorUIResource(255, 255, 255);
        UIManager.put("ButtonUI", BotonRelieve.class.getName());
        UIManager.put("ComboBoxUI", "javax.swing.plaf.basic.BasicComboBoxUI");

        UIManager.put("Panel.background", panel);
        UIManager.put("OptionPane.background", panel);
        UIManager.put("Viewport.background", new ColorUIResource(FILA));
        UIManager.put("ScrollPane.background", new ColorUIResource(FILA));
        UIManager.put("Label.foreground", new ColorUIResource(TEXTO));
        UIManager.put("Label.font", new FontUIResource("Segoe UI", Font.PLAIN, 13));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(TEXTO));
        UIManager.put("OptionPane.messageFont", new FontUIResource("Segoe UI", Font.PLAIN, 13));
        UIManager.put("OptionPane.buttonFont", new FontUIResource("Segoe UI", Font.BOLD, 13));

        UIManager.put("Button.background", new ColorUIResource(PIZARRA));
        UIManager.put("Button.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("Button.font", new FontUIResource("Segoe UI", Font.BOLD, 13));

        UIManager.put("ComboBox.background", new ColorUIResource(CAMPO));
        UIManager.put("ComboBox.foreground", new ColorUIResource(TEXTO));
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(SELECCION));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(Color.WHITE));
        UIManager.put("ComboBox.font", new FontUIResource("Segoe UI", Font.PLAIN, 13));
        UIManager.put("ComboBox.buttonBackground", new ColorUIResource(CABECERA));
        UIManager.put("ComboBox.buttonShadow", new ColorUIResource(BORDE));
        UIManager.put("ComboBox.buttonDarkShadow", new ColorUIResource(TEXTO));
        UIManager.put("ComboBox.buttonHighlight", new ColorUIResource(Color.WHITE));
        UIManager.put("ComboBox.border", new BorderUIResource(BorderFactory.createLineBorder(BORDE)));
    }

    public static ImageIcon logo(int tam) {
        java.net.URL url = Estilo.class.getResource("/img/logo.png");
        if (url == null) {
            return null;
        }
        Image img = new ImageIcon(url).getImage().getScaledInstance(tam, tam, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static Color mezclar(Color a, Color b, float t) {
        return new Color(
                Math.round(a.getRed() + (b.getRed() - a.getRed()) * t),
                Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * t),
                a.getAlpha());
    }

    // Sombra difusa: capas redondeadas cada vez mas grandes y transparentes
    public static void sombra(Graphics2D g2, int x, int y, int w, int h, int arco, int tam) {
        for (int i = tam; i >= 1; i--) {
            g2.setColor(new Color(0, 4, 16, 9));
            g2.fill(new RoundRectangle2D.Float(x - i, y - i, w + 2 * i, h + 2 * i,
                    arco + 2 * i, arco + 2 * i));
        }
    }

    // Boton de color solido con relieve (login y cerrar sesion)
    public static void boton(JButton b, Color color, Color hover) {
        if (!(b.getUI() instanceof BotonRelieve)) {
            b.setUI(new BotonRelieve());
        }
        b.setBackground(color);
        b.putClientProperty(BotonRelieve.HOVER, hover);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 20, 12, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // Botones del menu lateral: transparentes, con brillo al pasar y resaltado de cristal si estan activos
    public static void botonMenu(JButton b) {
        if (!(b.getUI() instanceof BotonRelieve)) {
            b.setUI(new BotonRelieve());
        }
        b.putClientProperty(BotonRelieve.MENU, true);
        b.setForeground(TEXTO);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 16));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        marcar(b, false);
    }

    public static void marcar(JButton b, boolean activo) {
        b.putClientProperty(BotonRelieve.ACTIVO, activo);
        b.setForeground(activo ? Color.WHITE : TEXTO);
        b.setFont(b.getFont().deriveFont(activo ? Font.BOLD : Font.PLAIN));
        b.repaint();
    }

    // Botones de accion de cada modulo (Nuevo / Editar / Eliminar)
    public static void botonAccion(JButton b, Color color, Color hover, Color texto) {
        boton(b, color, hover);
        b.setForeground(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(7, 18, 11, 18));
    }

    public static void botonNuevo(JButton b) { botonAccion(b, TURQUESA, TURQUESA_CLARO, Color.WHITE); }

    public static void botonGris(JButton b) { botonAccion(b, PIZARRA, PIZARRA_CLARO, Color.WHITE); }

    public static void botonRojo(JButton b) { botonAccion(b, ROJO, ROJO_CLARO, Color.WHITE); }

    public static void botonTaupe(JButton b) { botonAccion(b, TAUPE, TAUPE_OSCURO, Color.WHITE); }

    public static void botonTaupeClaro(JButton b) {
        botonAccion(b, TAUPE_CLARO, new Color(216, 208, 194), TEXTO_TAUPE);
    }

    // Campo blanco con borde suave, para el formulario del modulo Productos
    public static void campoTaupe(JTextField t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setForeground(TEXTO);
        t.setBackground(Color.WHITE);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 207, 195)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    }

    // Campo oscuro con borde que se ilumina al enfocarlo
    public static void campo(JTextField t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setForeground(TEXTO);
        t.setBackground(CAMPO);
        t.setCaretColor(NEON);
        t.setSelectionColor(SELECCION);
        t.setSelectedTextColor(Color.WHITE);
        Border normal = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(6, 10, 6, 10));
        Border enfocado = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NEON),
                BorderFactory.createEmptyBorder(6, 10, 6, 10));
        t.setBorder(t.hasFocus() ? enfocado : normal);
        t.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { t.setBorder(enfocado); }
            public void focusLost(FocusEvent e) { t.setBorder(normal); }
        });
    }

    public static void tabla(JTable t) {
        t.setRowHeight(32);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setBackground(FILA);
        t.setForeground(TEXTO);
        t.setShowVerticalLines(false);
        t.setGridColor(new Color(226, 232, 240));
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(SELECCION);
        t.setSelectionForeground(Color.WHITE);
        t.setFillsViewportHeight(true);
        t.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t.setDefaultRenderer(Object.class, new CeldaTabla());
        JTableHeader h = t.getTableHeader();
        h.setDefaultRenderer(new CabeceraTabla());
        h.setBackground(CABECERA);
        h.setPreferredSize(new Dimension(0, 38));
        h.setReorderingAllowed(false);
    }

    // Contenedor de tabla con borde luminoso y barras de desplazamiento finas
    public static JScrollPane scroll(JTable t) {
        JScrollPane s = new JScrollPane(t);
        s.setBorder(BorderFactory.createLineBorder(BORDE));
        s.getViewport().setBackground(FILA);
        s.getVerticalScrollBar().setUI(new BarraDesplazamiento());
        s.getHorizontalScrollBar().setUI(new BarraDesplazamiento());
        s.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        s.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        JPanel esquina = new JPanel();
        esquina.setBackground(CABECERA);
        s.setCorner(JScrollPane.UPPER_RIGHT_CORNER, esquina);
        return s;
    }

    public static JLabel titulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(TITULO);
        return l;
    }

    public static String dinero(java.math.BigDecimal v) {
        return MONEDA + String.format(java.util.Locale.US, "%,.2f", v);
    }

    private static class CeldaTabla extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean seleccionada,
                                                       boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(t, valor, seleccionada, false, fila, columna);
            setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
            if (!seleccionada) {
                setBackground(fila % 2 == 0 ? FILA : FILA_ALTERNA);
                setForeground(TEXTO);
            }
            return this;
        }
    }

    private static class CabeceraTabla extends JLabel implements TableCellRenderer {
        CabeceraTabla() {
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(new Color(51, 65, 85));
            setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean seleccionada,
                                                       boolean foco, int fila, int columna) {
            setText(valor == null ? "" : valor.toString());
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth();
            int h = getHeight();
            g2.setPaint(new GradientPaint(0, 0, new Color(248, 250, 252), 0, h, CABECERA));
            g2.fillRect(0, 0, w, h);
            g2.setColor(new Color(NEON.getRed(), NEON.getGreen(), NEON.getBlue(), 170));
            g2.fillRect(0, h - 2, w, 2);
            g2.setColor(new Color(15, 23, 42, 20));
            g2.fillRect(w - 1, 9, 1, h - 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class BarraDesplazamiento extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            trackColor = FILA;
            thumbColor = new Color(148, 163, 184, 180);
        }

        @Override
        protected JButton createDecreaseButton(int orientacion) { return sinBoton(); }

        @Override
        protected JButton createIncreaseButton(int orientacion) { return sinBoton(); }

        private static JButton sinBoton() {
            JButton b = new JButton();
            Dimension cero = new Dimension(0, 0);
            b.setPreferredSize(cero);
            b.setMinimumSize(cero);
            b.setMaximumSize(cero);
            return b;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(FILA);
            g.fillRect(r.x, r.y, r.width, r.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            if (r.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isThumbRollover() ? new Color(37, 99, 235, 200) : thumbColor);
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
            g2.dispose();
        }
    }
}
