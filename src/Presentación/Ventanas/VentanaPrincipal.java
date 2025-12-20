package Presentación.Ventanas;

import Presentación.Módulos.*;
import Presentación.Recursos.Botón;
import Presentación.Recursos.GestorAlertas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentanaPrincipal extends JFrame {

  private JLabel etiquetaUsuario;
  private JLabel etiquetaFecha;
  private JLabel etiquetaTítuloSección;
  private Timer timer;
  private JPanel panelDeContenido;
  private JPanel panelMenú;

  private Botón btnInicio;
  private Botón btnAdmin;
  private Botón btnAuditoria;
  private Botón btnClientes;
  private Botón btnFacturacion;
  private Botón btnProveedores;
  private Botón btnTanqueros;
  private Botón btnSalir;

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

    URL urlIcono = getClass().getResource("/Presentación/Recursos/Icono.png");
    if(urlIcono != null) setIconImage(new ImageIcon(urlIcono).getImage());

    // PANEL PRINCIPAL
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(new Color(18, 18, 18));

    JPanel panelEncabezado = new JPanel(new BorderLayout()) {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Degradado horizontal
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


    JPanel panelInformación = new JPanel(new GridLayout(2, 1));
    panelInformación.setOpaque(false);

    etiquetaUsuario = new JLabel("Usuario: Administrador");
    etiquetaUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
    etiquetaUsuario.setForeground(Color.WHITE);
    etiquetaUsuario.setHorizontalAlignment(SwingConstants.RIGHT);

    etiquetaFecha = new JLabel();
    etiquetaFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    etiquetaFecha.setForeground(new Color(255, 255, 255, 220));
    etiquetaFecha.setHorizontalAlignment(SwingConstants.RIGHT);

    panelInformación.add(etiquetaUsuario);
    panelInformación.add(etiquetaFecha);

    panelEncabezado.add(etiquetaTítulo, BorderLayout.WEST);
    panelEncabezado.add(panelInformación, BorderLayout.EAST);

    // CONTENEDOR CON MENÚ Y CONTENIDO
    JPanel contenedorPrincipal = new JPanel(new BorderLayout());
    contenedorPrincipal.setBackground(new Color(18, 18, 18));

    // MENÚ LATERAL MODERNO
    panelMenú = crearMenuLateral();

    // ÁREA DE CONTENIDO
    JPanel areaContenido = new JPanel(new BorderLayout());
    areaContenido.setBackground(new Color(18, 18, 18));

    // Breadcrumb / Título de sección
    JPanel panelBreadcrumb = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
    panelBreadcrumb.setBackground(new Color(18, 18, 18));

    etiquetaTítuloSección = new JLabel("INICIO");
    etiquetaTítuloSección.setFont(new Font("Segoe UI", Font.BOLD, 20));
    etiquetaTítuloSección.setForeground(new Color(229, 231, 235));
    panelBreadcrumb.add(etiquetaTítuloSección);

    // Panel de contenido dinámico
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

    // Sección: Principal
    menu.add(crearEtiquetaSeccion("PRINCIPAL"));
    btnInicio = crearBotónMenuModerno("Inicio", new Color(0, 22, 141));
    btnClientes = crearBotónMenuModerno("Clientes", new Color(40, 167, 69));
    btnTanqueros = crearBotónMenuModerno("Tanqueros", new Color(70, 128, 139));
    btnProveedores = crearBotónMenuModerno("Proveedores", new Color(108, 117, 125));
    btnFacturacion = crearBotónMenuModerno("Facturación", new Color(234, 177, 0));
    menu.add(btnInicio);
    menu.add(Box.createVerticalStrut(5));
    menu.add(btnClientes);
    menu.add(Box.createVerticalStrut(5));
    menu.add(btnTanqueros);
    menu.add(Box.createVerticalStrut(5));
    menu.add(btnProveedores);
    menu.add(Box.createVerticalStrut(5));
    menu.add(btnFacturacion);
    menu.add(Box.createVerticalStrut(5));

    btnInicio.setAlignmentX(Component.CENTER_ALIGNMENT);
    btnClientes.setAlignmentX(Component.CENTER_ALIGNMENT);
    btnTanqueros.setAlignmentX(Component.CENTER_ALIGNMENT);
    btnProveedores.setAlignmentX(Component.CENTER_ALIGNMENT);
    btnFacturacion.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Sistema
    menu.add(crearEtiquetaSeccion("SISTEMA"));
    btnAdmin = crearBotónMenuModerno("Administración", new Color(21, 128, 61));
    btnAuditoria = crearBotónMenuModerno("Auditoría", new Color(168, 85, 247));

    btnAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);
    btnAuditoria.setAlignmentX(Component.CENTER_ALIGNMENT);

    menu.add(btnAdmin);
    menu.add(Box.createVerticalStrut(5));
    menu.add(btnAuditoria);

    menu.add(Box.createVerticalGlue());

    // Botón Cerrar Sesión
    btnSalir = new Botón("Cerrar Sesión", new Color(239, 68, 68));
    btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 12));
    btnSalir.setPreferredSize(new Dimension(210, 40));
    btnSalir.setMaximumSize(new Dimension(210, 40));
    btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
    menu.add(btnSalir);

    return menu;
  }

  private JLabel crearEtiquetaSeccion(String texto) {
    JLabel lbl = new JLabel(texto);
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
    lbl.setForeground(new Color(107, 114, 128));
    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
    lbl.setBorder(new EmptyBorder(0, 5, 10, 0));
    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    return lbl;
  }

  private Botón crearBotónMenuModerno(String texto, Color colorIndicador) {
    Botón btn = new Botón("  " + texto, new Color(55, 65, 81));
    btn.setHorizontalAlignment(SwingConstants.LEFT);
    btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    btn.setPreferredSize(new Dimension(210, 42));
    btn.setMaximumSize(new Dimension(210, 42));

    // Efecto hover personalizado
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
      Color colorOriginal = btn.getBackground();

      public void mouseEntered(java.awt.event.MouseEvent evt) {
        btn.setBackground(new Color(75, 85, 99));
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        btn.setBackground(colorOriginal);
      }
    });

    return btn;
  }

  private void asignarEventos() {
    btnInicio.addActionListener(e -> mostrarInicio());
    btnClientes.addActionListener(e -> cambiarPanel("CLIENTES", new JPanel()));
    btnAdmin.addActionListener(e -> cambiarPanel("ADMINISTRACIÓN", new JPanel()));
    btnAuditoria.addActionListener(e -> cambiarPanel("AUDITORÍA", new JPanel()));
    btnFacturacion.addActionListener(e -> cambiarPanel("FACTURACIÓN", new JPanel()));
    btnProveedores.addActionListener(e -> cambiarPanel("PROVEEDORES", new Proveedor()));
    btnTanqueros.addActionListener(e -> cambiarPanel("TANQUEROS", new JPanel()));

    btnSalir.addActionListener(e -> {
      if(GestorAlertas.confirmarCerrarSesión(this, "¿Seguro que desea cerrar sesión?")) {
        dispose();
        VentanaInicio.obtenerVentana().setVisible(true);
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

    // Tarjetas con estadísticas
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
      new EmptyBorder(25, 20, 25, 20)
    ));

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
      etiquetaFecha.setText(now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss")));
    });
    timer.start();
  }
}
