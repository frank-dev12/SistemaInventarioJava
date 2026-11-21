package View;

import Model.Usuario;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Ventana principal con el menú lateral y las tarjetas de cada módulo.
 */
public class Dashboard extends JFrame {

    private final CardLayout contenido = new CardLayout();
    private final JPanel tarjetas = new JPanel(contenido);

    private final JLabel titulo = new JLabel("Inventario");
    private final JLabel nombreUsuario = new JLabel("Administrador");
    private final JLabel rolUsuario = new JLabel("ADMIN");

    private final JButton btnInicio = new JButton("Inicio");
    private final JButton btnProductos = new JButton("Productos");
    private final JButton btnCategorias = new JButton("Categorias");
    private final JButton btnEntradasSalidas = new JButton("Entradas / Salidas");
    private final JButton btnReportes = new JButton("Reportes");
    private final JButton btnUsuarios = new JButton("Usuarios");
    private final JButton btnCerrar = new JButton("Cerrar Sesion");

    private final PanelInicio panelInicio = new PanelInicio();
    private final PanelProductos panelProductos = new PanelProductos();
    private final PanelCategorias panelCategorias = new PanelCategorias();
    private final PanelEntradasSalidas panelEntradasSalidas = new PanelEntradasSalidas();
    private final PanelReportes panelReportes = new PanelReportes();
    private final PanelUsuarios panelUsuarios = new PanelUsuarios();

    public Dashboard() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        tarjetas.setOpaque(false);
        tarjetas.setPreferredSize(new Dimension(980, 620));

        estilizarMenu();
        armarDiseno();

        agregarModulo(btnInicio, "Inicio", panelInicio);
        agregarModulo(btnProductos, "Productos", panelProductos);
        agregarModulo(btnCategorias, "Categorias", panelCategorias);
        agregarModulo(btnEntradasSalidas, "EntradasSalidas", panelEntradasSalidas);
        agregarModulo(btnReportes, "Reportes", panelReportes);
        agregarModulo(btnUsuarios, "Usuarios", panelUsuarios);

        contenido.show(tarjetas, "Inicio");
        marcarActivo(btnInicio);
    }

    /** Botones del menú lateral, en orden. */
    private JButton[] botones() {
        return new JButton[]{btnInicio, btnProductos, btnCategorias,
            btnEntradasSalidas, btnReportes, btnUsuarios};
    }

    private void estilizarMenu() {
        for (JButton boton : botones()) {
            Estilo.botonMenu(boton);
        }
        Estilo.botonRojo(btnCerrar);

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Estilo.TITULO);
        nombreUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nombreUsuario.setForeground(Estilo.TITULO);
        rolUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rolUsuario.setForeground(Estilo.NEON);
    }

    /** Menú lateral y contenido como tarjetas de cristal sobre el fondo retro. */
    private void armarDiseno() {
        JLabel subtitulo = new JLabel("PANEL DE CONTROL");
        subtitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        subtitulo.setForeground(Estilo.NEON);

        PanelCristal menu = new PanelCristal(null, 24, 14);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setPreferredSize(new Dimension(260, 0));
        agregarAlMenu(menu, titulo);
        agregarAlMenu(menu, subtitulo);
        menu.add(Box.createVerticalStrut(26));
        for (JButton boton : botones()) {
            agregarAlMenu(menu, boton);
            menu.add(Box.createVerticalStrut(6));
        }
        menu.add(Box.createVerticalGlue());

        JPanel perfil = new JPanel();
        perfil.setLayout(new BoxLayout(perfil, BoxLayout.Y_AXIS));
        perfil.setOpaque(false);
        perfil.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(14, 4, 14, 4)));
        agregarAlMenu(perfil, nombreUsuario);
        perfil.add(Box.createVerticalStrut(2));
        agregarAlMenu(perfil, rolUsuario);
        agregarAlMenu(menu, perfil);
        agregarAlMenu(menu, btnCerrar);

        PanelCristal contenedor = new PanelCristal(new BorderLayout(), 24, 10);
        contenedor.add(tarjetas, BorderLayout.CENTER);

        FondoRetro fondo = new FondoRetro(new BorderLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        fondo.add(menu, BorderLayout.WEST);
        fondo.add(contenedor, BorderLayout.CENTER);
        setContentPane(fondo);
        setTitle("Sistema de Inventario");
        pack();
        setMinimumSize(new Dimension(1100, 680));
    }

    private static void agregarAlMenu(JPanel menu, JComponent componente) {
        componente.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        if (componente instanceof JButton || componente instanceof JPanel) {
            componente.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                    componente.getPreferredSize().height));
        }
        menu.add(componente);
    }

    /** Conecta un botón del menú con su tarjeta del CardLayout. */
    private void agregarModulo(JButton boton, String nombre, JPanel panel) {
        tarjetas.add(panel, nombre);
        boton.addActionListener(e -> {
            if (panel instanceof Modulo modulo) {
                modulo.recargar();
            }
            contenido.show(tarjetas, nombre);
            marcarActivo(boton);
        });
    }

    private void marcarActivo(JButton activo) {
        for (JButton boton : botones()) {
            Estilo.marcar(boton, boton == activo);
        }
    }

    public void setUsuario(Usuario u) {
        nombreUsuario.setText(u.getNombre());
        rolUsuario.setText(u.getRol());
        panelInicio.setUsuario(u);
    }

    public void addCerrarListener(java.awt.event.ActionListener listener) {
        btnCerrar.addActionListener(listener);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Estilo.instalar();
            new Dashboard().setVisible(true);
        });
    }
}
