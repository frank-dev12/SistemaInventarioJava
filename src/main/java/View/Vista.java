package View;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Ventana de inicio de sesión.
 *
 * Toda la interfaz está construida por código: no hay archivo .form ni código
 * generado por el diseñador de NetBeans.
 */
public class Vista extends JFrame {

    private final JLabel titulo = new JLabel("Iniciar Sesión");
    private final JLabel subtitulo = new JLabel("SISTEMA DE INVENTARIO");
    private final JLabel etiquetaUsuario = new JLabel("Usuario");
    private final JLabel etiquetaClave = new JLabel("Contraseña");

    private final JTextField txtnom = new JTextField();
    private final JPasswordField jcontra = new JPasswordField();
    private final JButton jingresar = new JButton("Ingresar");

    public Vista() {
        construirComponentes();
        armarDiseno();
    }

    /** Crea los componentes y les da el estilo base. */
    private void construirComponentes() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Inventario");

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Estilo.TITULO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        subtitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subtitulo.setForeground(Estilo.NEON);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        for (JLabel etiqueta : new JLabel[]{etiquetaUsuario, etiquetaClave}) {
            etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 13));
            etiqueta.setForeground(Estilo.GRIS);
        }

        Dimension campo = new Dimension(300, 40);
        for (JTextField campoTexto : new JTextField[]{txtnom, jcontra}) {
            Estilo.campo(campoTexto);
            campoTexto.setPreferredSize(campo);
        }

        Estilo.botonNuevo(jingresar);
        jingresar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        jingresar.setPreferredSize(new Dimension(300, 48));
    }

    /** Tarjeta de cristal centrada sobre el fondo retro. */
    private void armarDiseno() {
        PanelCristal tarjeta = new PanelCristal(new GridBagLayout(), 28, 28);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.insets = new Insets(0, 0, 4, 0);
        tarjeta.add(titulo, c);
        c.insets = new Insets(0, 0, 30, 0);
        tarjeta.add(subtitulo, c);
        c.insets = new Insets(0, 0, 6, 0);
        tarjeta.add(etiquetaUsuario, c);
        c.insets = new Insets(0, 0, 18, 0);
        tarjeta.add(txtnom, c);
        c.insets = new Insets(0, 0, 6, 0);
        tarjeta.add(etiquetaClave, c);
        c.insets = new Insets(0, 0, 28, 0);
        tarjeta.add(jcontra, c);
        c.insets = new Insets(0, 0, 0, 0);
        tarjeta.add(jingresar, c);

        FondoRetro fondo = new FondoRetro(new GridBagLayout());
        fondo.setPreferredSize(new Dimension(560, 640));
        fondo.add(tarjeta);
        setContentPane(fondo);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    public void addLoginListener(java.awt.event.ActionListener listener) {
        jingresar.addActionListener(listener);
        jcontra.addActionListener(listener);
    }

    public String getUsuario() {
        return txtnom.getText().trim();
    }

    public String getClave() {
        return new String(jcontra.getPassword());
    }

    public void limpiar() {
        txtnom.setText("");
        jcontra.setText("");
        txtnom.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Estilo.instalar();
            new Vista().setVisible(true);
        });
    }
}
