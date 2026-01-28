package Presentación.Ventanas;

import Logica.Entidades.Usuario;
import Logica.Gestores.GestorSocios;
import Logica.Gestores.GestorTanqueros;
import Logica.Gestores.GestorTransporte;
import Presentación.Paneles.Administración.Administración;
import Presentación.Paneles.Auditoría.Auditoría;
import Presentación.Paneles.Tanqueros.Tanqueros;
import Presentación.Paneles.Socios.Socios;
import Presentación.Paneles.Transporte.TransportePanel;
import Presentación.Recursos.Botón;
import Logica.Gestores.GestorAlertas;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VentanaPrincipal extends JFrame {

  private Usuario usuario;
  private JLabel etiquetaUsuario;
  private JLabel etiquetaFecha;
  private JLabel etiquetaTítulo;
  private Timer timer;
  private JPanel panelContenido;
  private JPanel panelMenú;

  private GestorSocios gestorSocios;
  private GestorTanqueros gestorTanqueros;
  private GestorTransporte gestorTransporte;

  private Botón botónInicio;
  private Botón botónAdmin;
  private Botón botónAuditoría;
  private Botón botónSocios;
  private Botón botónTanqueros;
  private Botón botónTransporte;
  private Botón botónSalir;

  public VentanaPrincipal(Usuario usuario) {
    this.usuario = usuario;
    this.gestorSocios = new GestorSocios();
    this.gestorTanqueros = new GestorTanqueros();
    this.gestorTransporte = new GestorTransporte();
    inicializarComponentes();
    configurarVentana();
    iniciarReloj();
    mostrarInicio();
  }

  private void inicializarComponentes() {
    setTitle("Sistema de Transporte de Lácteos CAMISOL S.A.");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setIconImage(new ImageIcon("src/Presentación/Recursos/Icono.png").getImage());

    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(18, 18, 18));

    // Encabezado
    JPanel panelEncabezado = new JPanel(new BorderLayout()) {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(
          0, 0, new Color(234, 177, 0),
          getWidth(), 0, new Color(0, 22, 141));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
      }
    };

    panelEncabezado.setOpaque(false);
    panelEncabezado.setBorder(new EmptyBorder(15, 20, 15, 20));
    panelEncabezado.setPreferredSize(new Dimension(0, 80));

    JLabel etiquetaTítulo = new JLabel("CAMISOL S.A. - SISTEMA DE TRANSPORTE");
    etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
    etiquetaTítulo.setForeground(Color.WHITE);

    JPanel panelInformación = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
    panelInformación.setOpaque(false);

    JPanel cuadroUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
    cuadroUsuario.setBackground(new Color(255, 255, 255, 25));
    cuadroUsuario.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1));
    JLabel lblIconoUsuario = new JLabel("●");
    lblIconoUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
    lblIconoUsuario.setForeground(new Color(31, 234, 0));
    etiquetaUsuario = new JLabel(usuario.getRol());
    etiquetaUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
    etiquetaUsuario.setForeground(Color.WHITE);

    cuadroUsuario.add(lblIconoUsuario);
    cuadroUsuario.add(etiquetaUsuario);

    etiquetaFecha = new JLabel();
    etiquetaFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
    etiquetaFecha.setForeground(new Color(255, 255, 255, 220));
    etiquetaFecha.setHorizontalAlignment(SwingConstants.RIGHT);

    panelInformación.add(cuadroUsuario);
    panelInformación.add(etiquetaFecha);

    panelEncabezado.add(etiquetaTítulo, BorderLayout.WEST);
    panelEncabezado.add(panelInformación, BorderLayout.EAST);

    JPanel contenedorPrincipal = new JPanel(new BorderLayout());
    contenedorPrincipal.setBackground(new Color(18, 18, 18));

    panelMenú = crearMenuLateral();

    JPanel areaContenido = new JPanel(new BorderLayout());
    areaContenido.setBackground(new Color(18, 18, 18));

    JPanel panelBreadcrumb = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
    panelBreadcrumb.setBackground(new Color(18, 18, 18));

    this.etiquetaTítulo = new JLabel();
    this.etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
    this.etiquetaTítulo.setForeground(new Color(229, 231, 235));
    panelBreadcrumb.add(this.etiquetaTítulo);

    panelContenido = new JPanel(new BorderLayout());
    panelContenido.setBackground(new Color(18, 18, 18));
    panelContenido.setBorder(new EmptyBorder(10, 20, 20, 20));

    areaContenido.add(panelBreadcrumb, BorderLayout.NORTH);
    areaContenido.add(panelContenido, BorderLayout.CENTER);

    contenedorPrincipal.add(panelMenú, BorderLayout.WEST);
    contenedorPrincipal.add(areaContenido, BorderLayout.CENTER);

    panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
    panelPrincipal.add(contenedorPrincipal, BorderLayout.CENTER);

    add(panelPrincipal);
    asignarEventos();
  }

  private JPanel crearMenuLateral() {
    JPanel menu = new JPanel();
    menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
    menu.setBackground(new Color(31, 41, 55));
    menu.setBorder(new EmptyBorder(20, 15, 20, 15));
    menu.setPreferredSize(new Dimension(240, 0));

    // PRINCIPAL
    JLabel lbl = new JLabel("PRINCIPAL");
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
    lbl.setForeground(new Color(107, 114, 128));
    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    lbl.setBorder(new EmptyBorder(0, 5, 10, 0));
    menu.add(lbl);

    botónInicio = Botón.crearBotónMenu("Inicio", null);
    botónSocios = Botón.crearBotónMenu("Socios", null);
    botónTanqueros = Botón.crearBotónMenu("Tanqueros", null);
    botónTransporte = Botón.crearBotónMenu("Transporte", null);

    botónInicio.setAlignmentX(Component.CENTER_ALIGNMENT);
    botónSocios.setAlignmentX(Component.CENTER_ALIGNMENT);
    botónTanqueros.setAlignmentX(Component.CENTER_ALIGNMENT);
    botónTransporte.setAlignmentX(Component.CENTER_ALIGNMENT);

    menu.add(botónInicio);
    menu.add(Box.createVerticalStrut(5));
    menu.add(botónSocios);
    menu.add(Box.createVerticalStrut(5));
    menu.add(botónTanqueros);
    menu.add(Box.createVerticalStrut(5));
    menu.add(botónTransporte);
    menu.add(Box.createVerticalStrut(15));

    // Solo para administrador
    if (usuario.getRol().equalsIgnoreCase("ADMINISTRADOR")) {
      JLabel lbs = new JLabel("SISTEMA");
      lbs.setFont(new Font("Segoe UI", Font.BOLD, 11));
      lbs.setForeground(new Color(107, 114, 128));
      lbs.setAlignmentX(Component.CENTER_ALIGNMENT);
      lbs.setBorder(new EmptyBorder(0, 5, 10, 0));
      menu.add(lbs);

      botónAdmin = Botón.crearBotónMenu("Administración", null);
      botónAuditoría = Botón.crearBotónMenu("Auditoría", null);
      botónAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);
      botónAuditoría.setAlignmentX(Component.CENTER_ALIGNMENT);

      menu.add(botónAdmin);
      menu.add(Box.createVerticalStrut(5));
      menu.add(botónAuditoría);
    }

    menu.add(Box.createVerticalGlue());

    // Cerrar Sesión
    botónSalir = Botón.crearBotónMenu("Cerrar Sesión", new Color(239, 68, 68));
    botónSalir.setFont(new Font("Segoe UI", Font.BOLD, 12));
    botónSalir.setPreferredSize(new Dimension(210, 40));
    botónSalir.setMaximumSize(new Dimension(210, 40));
    botónSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
    menu.add(botónSalir);

    return menu;
  }

  private void asignarEventos() {
    botónInicio.addActionListener(e -> mostrarInicio());
    botónSocios.addActionListener(e -> cambiarPanel("SOCIOS", new Socios(usuario)));
    botónTanqueros.addActionListener(e -> cambiarPanel("TANQUEROS", new Tanqueros(usuario)));
    botónTransporte.addActionListener(e -> cambiarPanel("TRANSPORTE",new TransportePanel(usuario)));

    if (usuario.getRol().equalsIgnoreCase("ADMINISTRADOR")) {
      botónAdmin.addActionListener(e -> cambiarPanel("ADMINISTRACIÓN", new Administración(usuario)));
      botónAuditoría.addActionListener(e -> {
        if (Auditoría.solicitarContraseña()) {
          cambiarPanel("AUDITORÍA", Auditoría.obtenerInstancia());
        }
      });
    }

    botónSalir.addActionListener(e -> {
      if (GestorAlertas.confirmarCerrarSesión(this, "¿Seguro que desea cerrar sesión?")) {
        dispose();
        SwingUtilities.invokeLater(() -> {
          new VentanaInicio().setVisible(true);
        });
      }
    });
  }

  private void cambiarPanel(String titulo, Component panel) {
    etiquetaTítulo.setText(titulo);
    panelContenido.removeAll();

    JPanel contenedor = new JPanel(new BorderLayout());
    contenedor.setBackground(new Color(18, 18, 18));
    contenedor.add(panel, BorderLayout.CENTER);

    panelContenido.add(contenedor, BorderLayout.CENTER);
    panelContenido.revalidate();
    panelContenido.repaint();
  }

  private void mostrarInicio() {
    etiquetaTítulo.setText("INICIO");
    panelContenido.removeAll();

    // Obtener datos reales de la base de datos
    int viajesActivos = gestorTransporte.contarViajesActivos();
    int totalSocios = gestorSocios.contarSociosActivos();
    int totalTanqueros = gestorTanqueros.contarTanquerosActivos();
    int alertas = 2;

    // Dashboard con 4 tarjetas en 2x2
    JPanel dashboard = new JPanel(new GridLayout(2, 2, 20, 20));
    dashboard.setBackground(new Color(18, 18, 18));

    dashboard.add(crearTarjetaEstadistica(
      String.valueOf(viajesActivos),
      "Viajes Activos",
      new Color(234, 177, 0)
    ));

    dashboard.add(crearTarjetaEstadistica(
      String.valueOf(totalSocios),
      "Socios Activos",
      new Color(40, 167, 69)
    ));

    dashboard.add(crearTarjetaEstadistica(
      String.valueOf(totalTanqueros),
      "Tanqueros Activos",
      new Color(70, 128, 139)
    ));

    dashboard.add(crearTarjetaEstadistica(
      String.valueOf(alertas),
      "Alertas",
      alertas > 0 ? new Color(239, 68, 68) : new Color(40, 167, 69)
    ));

    panelContenido.add(dashboard, BorderLayout.CENTER);
    panelContenido.revalidate();
    panelContenido.repaint();
  }

  private JPanel crearTarjetaEstadistica(String valor, String titulo, Color colorBorde) {
    JPanel tarjeta = new JPanel();
    tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
    tarjeta.setBackground(new Color(31, 41, 55));
    tarjeta.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createMatteBorder(3, 0, 0, 0, colorBorde),
      new EmptyBorder(25, 20, 25, 20)));

    JLabel lblValor = new JLabel(valor);
    lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
    lblValor.setForeground(Color.WHITE);
    lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel lblTitulo = new JLabel(titulo);
    lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    lblTitulo.setForeground(new Color(156, 163, 175));
    lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

    tarjeta.add(Box.createVerticalGlue());
    tarjeta.add(lblValor);
    tarjeta.add(Box.createVerticalStrut(10));
    tarjeta.add(lblTitulo);
    tarjeta.add(Box.createVerticalGlue());

    return tarjeta;
  }

  private void configurarVentana() {
    setLocationRelativeTo(null);
  }

  private void iniciarReloj() {
    timer = new Timer(1000, e -> {
      LocalDateTime now = LocalDateTime.now();
      etiquetaFecha.setText(now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")));
    });
    timer.start();
  }
}
