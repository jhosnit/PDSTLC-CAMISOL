package Presentación.Ventanas;

import Presentación.Módulos.*;
import Presentación.Recursos.Botón;
import Presentación.Recursos.GestorAlertas;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VentanaPrincipal extends JFrame {

  private JLabel etiquetaUsuario;
  private JLabel etiquetaFecha;
  private JLabel etiquetaTítuloSección;
  private Timer timer;
  private JPanel panelDeContenido;
  private JPanel panelMenú;

  private Botón botónInicio;
  private Botón botónAdmin;
  private Botón botónAuditoria;
  private Botón botónClientes;
  private Botón botónFacturacion;
  private Botón botónProveedores;
  private Botón botónTanqueros;
  private Botón botónSalir;

  public VentanaPrincipal() {
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

        // Degradado
        GradientPaint gp = new GradientPaint(
            0, 0, new Color(234, 177, 0),
            getWidth(), 0, new Color(0, 22, 141));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
      }
    };

    // Panel encabezado
    panelEncabezado.setOpaque(false);
    panelEncabezado.setBorder(new EmptyBorder(15, 20, 15, 20));
    panelEncabezado.setPreferredSize(new Dimension(0, 80));

    // Titulo
    JLabel etiquetaTítulo = new JLabel("CAMISOL S.A. - SISTEMA DE TRANSPORTE");
    etiquetaTítulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
    etiquetaTítulo.setForeground(Color.WHITE);

    // Información usuario y fecha
    JPanel panelInformación = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
    panelInformación.setOpaque(false);

    JPanel cuadroUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
    cuadroUsuario.setBackground(new Color(255, 255, 255, 25));
    cuadroUsuario.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1));
    JLabel lblIconoUsuario = new JLabel("●");
    lblIconoUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
    lblIconoUsuario.setForeground(new Color(31, 234, 0));
    etiquetaUsuario = new JLabel("Administrador");
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

    // Panel principal
    JPanel contenedorPrincipal = new JPanel(new BorderLayout());
    contenedorPrincipal.setBackground(new Color(18, 18, 18));

    // Panel de menú lateral
    panelMenú = crearMenuLateral();

    // Panel de contenido
    JPanel areaContenido = new JPanel(new BorderLayout());
    areaContenido.setBackground(new Color(18, 18, 18));

    // Título de sección
    JPanel panelBreadcrumb = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
    panelBreadcrumb.setBackground(new Color(18, 18, 18));

    etiquetaTítuloSección = new JLabel();
    etiquetaTítuloSección.setFont(new Font("Segoe UI", Font.BOLD, 20));
    etiquetaTítuloSección.setForeground(new Color(229, 231, 235));
    panelBreadcrumb.add(etiquetaTítuloSección);

    // Panel de contenido
    panelDeContenido = new JPanel(new BorderLayout());
    panelDeContenido.setBackground(new Color(18, 18, 18));
    panelDeContenido.setBorder(new EmptyBorder(10, 20, 20, 20));

    areaContenido.add(panelBreadcrumb, BorderLayout.NORTH);
    areaContenido.add(panelDeContenido, BorderLayout.CENTER);

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

    // Principal
    JLabel lbl = new JLabel("PRINCIPAL");
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
    lbl.setForeground(new Color(107, 114, 128));
    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
    lbl.setBorder(new EmptyBorder(0, 5, 10, 0));
    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    menu.add(lbl);
    botónInicio = Botón.crearBotónMenu("Inicio", null);
    botónClientes = Botón.crearBotónMenu("Clientes", null);
    botónTanqueros = Botón.crearBotónMenu("Tanqueros", null);
    botónProveedores = Botón.crearBotónMenu("Proveedores", null);
    botónFacturacion = Botón.crearBotónMenu("Facturación", null);
    botónInicio.setAlignmentX(Component.CENTER_ALIGNMENT);
    botónClientes.setAlignmentX(Component.CENTER_ALIGNMENT);
    botónTanqueros.setAlignmentX(Component.CENTER_ALIGNMENT);
    botónProveedores.setAlignmentX(Component.CENTER_ALIGNMENT);
    botónFacturacion.setAlignmentX(Component.CENTER_ALIGNMENT);
    menu.add(botónInicio);
    menu.add(Box.createVerticalStrut(5));
    menu.add(botónClientes);
    menu.add(Box.createVerticalStrut(5));
    menu.add(botónTanqueros);
    menu.add(Box.createVerticalStrut(5));
    menu.add(botónProveedores);
    menu.add(Box.createVerticalStrut(5));
    menu.add(botónFacturacion);
    menu.add(Box.createVerticalStrut(15));

    // Sistema
    JLabel lbs = new JLabel("SISTEMA");
    lbs.setFont(new Font("Segoe UI", Font.BOLD, 11));
    lbs.setForeground(new Color(107, 114, 128));
    lbs.setAlignmentX(Component.LEFT_ALIGNMENT);
    lbs.setBorder(new EmptyBorder(0, 5, 10, 0));
    lbs.setAlignmentX(Component.CENTER_ALIGNMENT);
    menu.add(lbs);
    botónAdmin = Botón.crearBotónMenu("Administración", null);
    botónAuditoria = Botón.crearBotónMenu("Auditoría", null);
    botónAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);
    botónAuditoria.setAlignmentX(Component.CENTER_ALIGNMENT);

    menu.add(botónAdmin);
    menu.add(Box.createVerticalStrut(5));
    menu.add(botónAuditoria);

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
    botónClientes.addActionListener(e -> cambiarPanel("CLIENTES", new Cliente()));
    botónAdmin.addActionListener(e -> cambiarPanel("ADMINISTRACIÓN", new Administración()));
    botónAuditoria.addActionListener(e -> cambiarPanel("AUDITORÍA", new Auditoría()));
    botónFacturacion.addActionListener(e -> cambiarPanel("FACTURACIÓN", new JPanel()));
    botónProveedores.addActionListener(e -> cambiarPanel("PROVEEDORES", new Proveedor()));
    botónTanqueros.addActionListener(e -> cambiarPanel("TANQUEROS", new Tanquero()));

    botónSalir.addActionListener(e -> {
      if (GestorAlertas.confirmarCerrarSesión(this, "¿Seguro que desea cerrar sesión?")) {

        dispose();
        System.exit(0);
      }
    });
  }

  private void cambiarPanel(String titulo, Component panel) {
    etiquetaTítuloSección.setText(titulo);
    panelDeContenido.removeAll();

    JPanel contenedor = new JPanel(new BorderLayout());
    contenedor.setBackground(new Color(18, 18, 18));
    contenedor.add(panel, BorderLayout.CENTER);

    panelDeContenido.add(contenedor, BorderLayout.CENTER);
    panelDeContenido.revalidate();
    panelDeContenido.repaint();
  }

  private void mostrarInicio() {
    etiquetaTítuloSección.setText("INICIO");
    panelDeContenido.removeAll();

    JPanel dashboard = new JPanel(new GridLayout(2, 3, 20, 20));
    dashboard.setBackground(new Color(18, 18, 18));

    dashboard.add(crearTarjetaEstadistica("15,400", "Litros Hoy", new Color(0, 22, 141)));
    dashboard.add(crearTarjetaEstadistica("8", "Viajes Activos", new Color(234, 177, 0)));
    dashboard.add(crearTarjetaEstadistica("124", "Clientes", new Color(40, 167, 69)));
    dashboard.add(crearTarjetaEstadistica("23", "Tanqueros", new Color(70, 128, 139)));
    dashboard.add(crearTarjetaEstadistica("$12,450", "Facturación Hoy", new Color(168, 85, 247)));
    dashboard.add(crearTarjetaEstadistica("0", "Alertas", new Color(239, 68, 68)));

    panelDeContenido.add(dashboard, BorderLayout.CENTER);
    panelDeContenido.revalidate();
    panelDeContenido.repaint();
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
