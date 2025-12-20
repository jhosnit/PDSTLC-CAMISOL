package Presentación.Ventanas;

import Presentación.Recursos.Botón;
import Presentación.Recursos.GestorAlertas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaInicio extends JFrame {
  private JTextField textoUsuario;
  private JPasswordField textoContraseña;
  private Botón botónIngresar;
  private Botón botónSalir;
  private JCheckBox mostrarContraseña;
  private static VentanaInicio inicio;

  public VentanaInicio() {
    inicializarComponentes();
    configurarVentana();
  }

  public static VentanaInicio obtenerVentana() {
    if (inicio == null) {
      inicio = new VentanaInicio();
    }
    inicio.setVisible(true);
    return inicio;
  }

  private void inicializarComponentes() {
    setTitle("Sistema de Transporte Lácteos CAMISOL.S.A - Iniciar Sesión");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setIconImage(new ImageIcon("src/Presentación/Recursos/Icono.png").getImage());

    // Panel principal
    JPanel panelPrincipal = new JPanel(new GridBagLayout());
    panelPrincipal.setBackground(new Color(18, 18, 18));
    GridBagConstraints gbc = new GridBagConstraints();

    // Panel izquierdo decorativo
    JPanel panelIzquierdo = crearPanelDecorativo();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.4;
    gbc.weighty = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    panelPrincipal.add(panelIzquierdo, gbc);

    // Panel derecho con formulario
    JPanel panelDerecho = crearPanelFormulario();
    gbc.gridx = 1;
    gbc.weightx = 0.6;
    panelPrincipal.add(panelDerecho, gbc);

    add(panelPrincipal);
  }

  private void configurarVentana() {
    setSize(1000, 600);
    setLocationRelativeTo(null);
    setResizable(false);
  }

  private JPanel crearPanelDecorativo() {
    JPanel panel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Degradado
        GradientPaint gp = new GradientPaint(
          0, 0, new Color(234, 177, 0),
          getWidth(), getHeight(), new Color(0, 22, 141));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
      }
    };
    panel.setLayout(new GridBagLayout());

    JPanel contenido = new JPanel();
    contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
    contenido.setOpaque(false);

    // Logo
    JLabel etiquetaLogo = new JLabel();
    ImageIcon íconoImagen = new ImageIcon("src/Presentación/Recursos/Icono.png");
    Image logo = íconoImagen.getImage().getScaledInstance(280, 280, Image.SCALE_SMOOTH);
    etiquetaLogo.setIcon(new ImageIcon(logo));
    etiquetaLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
    contenido.add(etiquetaLogo);
    contenido.add(Box.createRigidArea(new Dimension(0, 40)));

    // Texto de la empresa
    JLabel lblBienvenida = new JLabel("<html><center>Sistema de Transporte<br>de Lácteos CAMISOL S.A.</center></html>");
    lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 28));
    lblBienvenida.setForeground(Color.WHITE);
    lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
    contenido.add(lblBienvenida);
    contenido.add(Box.createRigidArea(new Dimension(0, 15)));

    panel.add(contenido);
    return panel;
  }

  private JPanel crearPanelFormulario() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    panel.setBackground(new Color(18, 18, 18));

    JPanel formulario = new JPanel();
    formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
    formulario.setOpaque(false);
    formulario.setBorder(new EmptyBorder(60, 50, 60, 50));

    // Título del formulario
    JLabel lblTitulo = new JLabel("Iniciar Sesión");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
    formulario.add(lblTitulo);

    formulario.add(Box.createRigidArea(new Dimension(0, 20)));

    // Usuario
    JLabel lblUsuario = new JLabel("Usuario");
    lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
    lblUsuario.setForeground(new Color(229, 231, 235));
    lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
    formulario.add(lblUsuario);

    formulario.add(Box.createRigidArea(new Dimension(0, 8)));

    textoUsuario = new JTextField();
    textoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
    textoUsuario.setPreferredSize(new Dimension(350, 45));
    textoUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    textoUsuario.setForeground(Color.WHITE);
    textoUsuario.setCaretColor(Color.WHITE);
    textoUsuario.setBackground(new Color(31, 41, 55));
    textoUsuario.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(55, 65, 81), 1),
      BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    textoUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
    formulario.add(textoUsuario);

    formulario.add(Box.createRigidArea(new Dimension(0, 20)));

    // Contraseña
    JLabel lblContraseña = new JLabel("Contraseña");
    lblContraseña.setFont(new Font("Segoe UI", Font.BOLD, 13));
    lblContraseña.setForeground(new Color(229, 231, 235));
    lblContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
    formulario.add(lblContraseña);

    formulario.add(Box.createRigidArea(new Dimension(0, 8)));

    textoContraseña = new JPasswordField();
    textoContraseña.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
    textoContraseña.setPreferredSize(new Dimension(350, 45));
    textoContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    textoContraseña.setForeground(Color.WHITE);
    textoContraseña.setCaretColor(Color.WHITE);
    textoContraseña.setBackground(new Color(31, 41, 55));
    textoContraseña.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(55, 65, 81), 1),
      BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    textoContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
    formulario.add(textoContraseña);

    formulario.add(Box.createRigidArea(new Dimension(0, 15)));

    // Checkbox mostrar contraseña
    mostrarContraseña = new JCheckBox("Mostrar contraseña");
    mostrarContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    mostrarContraseña.setForeground(new Color(156, 163, 175));
    mostrarContraseña.setOpaque(false);
    mostrarContraseña.setFocusPainted(false);
    mostrarContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
    mostrarContraseña.addActionListener(e -> {
      if (mostrarContraseña.isSelected()) {
        textoContraseña.setEchoChar((char) 0);
      } else {
        textoContraseña.setEchoChar('•');
      }
    });
    formulario.add(mostrarContraseña);

    formulario.add(Box.createRigidArea(new Dimension(0, 30)));

    // Botón Ingresar
    botónIngresar = new Botón("Ingresar", new Color(0, 22, 141));
    botónIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
    botónIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
    botónIngresar.setPreferredSize(new Dimension(350, 45));
    botónIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
    botónIngresar.addActionListener(e -> ingresar());
    formulario.add(botónIngresar);

    formulario.add(Box.createRigidArea(new Dimension(0, 15)));

    // Botón Salir
    botónSalir = new Botón("Salir", new Color(239, 68, 68));
    botónSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
    botónSalir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
    botónSalir.setPreferredSize(new Dimension(350, 45));
    botónSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
    botónSalir.addActionListener(e -> System.exit(0));
    formulario.add(botónSalir);

    formulario.add(Box.createRigidArea(new Dimension(0, 15)));

    panel.add(formulario);

    textoContraseña.addActionListener(e -> ingresar());

    return panel;
  }

  private void ingresar() {
    String usuario = textoUsuario.getText().trim();
    String contraseña = new String(textoContraseña.getPassword());

    if (usuario.isEmpty() || contraseña.isEmpty()) {
      GestorAlertas.mostrarErrorLogin(this, "Por favor ingrese usuario y contraseña");
      return;
    }

    if (usuario.equals("admin") && contraseña.equals("admin")) {
      dispose();
      new VentanaPrincipal().setVisible(true);
    } else {
      GestorAlertas.mostrarErrorLogin(this, "Usuario o contraseña incorrecto");
      textoUsuario.setText("");
      textoContraseña.setText("");
    }
  }

}
