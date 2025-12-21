package Presentación.Módulos;

import Presentación.Recursos.Botón;
import Presentación.Recursos.GestorAlertas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Facturación extends JPanel {

    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;
    private JPanel panelFiltros;
    private JTextField txtBuscar;
    private JComboBox<String> comboEstado, comboCliente;

    public Facturación() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior con pestañas para roles
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(31, 41, 55));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("Secretaria", crearPanelSecretaria());
        tabbedPane.addTab("Dueños Tanqueros", crearPanelDueñosTanqueros());
        tabbedPane.addTab("Gerente General", crearPanelGerente());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelSecretaria() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setOpaque(false);

        Botón[] botones = {
            new Botón("Registrar Datos Viaje", new Color(40, 167, 69)),
            new Botón("Generar Factura", new Color(59, 130, 246)),
            new Botón("Consultar Factura", new Color(70, 128, 139)),
            new Botón("Anular Factura", new Color(239, 68, 68)),
            new Botón("Registrar Pago", new Color(147, 51, 234)),
            new Botón("Contra Factura", new Color(249, 115, 22)),
            new Botón("Recordatorio Pago", new Color(156, 163, 175)),
            new Botón("Gestión Cobro", new Color(220, 38, 38)),
            new Botón("Reportes Financieros", new Color(16, 185, 129))
        };

        Dimension dimBoton = new Dimension(180, 40);
        for (Botón b : botones) {
            b.setPreferredSize(dimBoton);
            panelBotones.add(b);
        }

        panel.add(panelBotones, BorderLayout.NORTH);

        // Filtros
        panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(Color.WHITE);
        txtBuscar = new JTextField(20);
        txtBuscar.setPreferredSize(new Dimension(200, 35));
        txtBuscar.setBackground(new Color(55, 65, 81));
        txtBuscar.setForeground(Color.WHITE);
        txtBuscar.setBorder(new LineBorder(new Color(75, 85, 99), 1));

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(Color.WHITE);
        comboEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "Pagada", "Anulada", "Vencida"});
        comboEstado.setPreferredSize(new Dimension(150, 35));
        comboEstado.setBackground(new Color(55, 65, 81));
        comboEstado.setForeground(Color.WHITE);

        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setForeground(Color.WHITE);
        comboCliente = new JComboBox<>(new String[]{"Todos", "Cliente A", "Cliente B", "Cliente C"});
        comboCliente.setPreferredSize(new Dimension(150, 35));
        comboCliente.setBackground(new Color(55, 65, 81));
        comboCliente.setForeground(Color.WHITE);

        Botón btnFiltrar = new Botón("Filtrar", new Color(70, 128, 139));
        btnFiltrar.setPreferredSize(new Dimension(100, 35));

        panelFiltros.add(lblBuscar);
        panelFiltros.add(txtBuscar);
        panelFiltros.add(lblEstado);
        panelFiltros.add(comboEstado);
        panelFiltros.add(lblCliente);
        panelFiltros.add(comboCliente);
        panelFiltros.add(btnFiltrar);

        panel.add(panelFiltros, BorderLayout.CENTER);

        // Tabla
        crearTablaFacturas();
        JScrollPane scrollTabla = new JScrollPane(tablaFacturas);
        scrollTabla.getViewport().setBackground(new Color(31, 41, 55));
        scrollTabla.setBorder(new LineBorder(new Color(55, 65, 81), 1));

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(new EmptyBorder(10, 0, 0, 0));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        panel.add(panelTabla, BorderLayout.SOUTH);

        // Action Listeners de botones (solo visual)
        botones[0].addActionListener(e -> GestorAlertas.mostrarExito(this, "Formulario de registro de viaje abierto"));
        botones[1].addActionListener(e -> GestorAlertas.mostrarExito(this, "Generando factura..."));
        botones[2].addActionListener(e -> GestorAlertas.mostrarInfo(this, "Mostrando detalles de la factura"));
        botones[3].addActionListener(e -> GestorAlertas.mostrarAdvertencia(this, "¿Está seguro de anular esta factura?"));
        botones[4].addActionListener(e -> GestorAlertas.mostrarExito(this, "Registro de pago completado"));
        btnFiltrar.addActionListener(e -> GestorAlertas.mostrarInfo(this, "Aplicando filtros..."));

        return panel;
    }

    private JPanel crearPanelDueñosTanqueros() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setOpaque(false);

        Botón[] botones = {
            new Botón("Consultar Facturación", new Color(70, 128, 139)),
            new Botón("Ver Rendimiento", new Color(16, 185, 129)),
            new Botón("Consultar Pagos", new Color(59, 130, 246)),
            new Botón("Descargar Comprobante", new Color(147, 51, 234))
        };

        Dimension dimBoton = new Dimension(200, 40);
        for (Botón b : botones) {
            b.setPreferredSize(dimBoton);
            panelBotones.add(b);
        }

        panel.add(panelBotones, BorderLayout.NORTH);

        JPanel panelInfo = new JPanel(new GridLayout(2, 2, 10, 10));
        panelInfo.setOpaque(false);
        panelInfo.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelInfo.add(crearPanelInfo("Facturación Total", "$15,430.50", Color.GREEN));
        panelInfo.add(crearPanelInfo("Pagos Pendientes", "$3,200.00", Color.ORANGE));
        panelInfo.add(crearPanelInfo("Viajes Realizados", "24", Color.CYAN));
        panelInfo.add(crearPanelInfo("Rendimiento Mensual", "87%", Color.MAGENTA));

        panel.add(panelInfo, BorderLayout.CENTER);

        // Action Listeners
        botones[0].addActionListener(e -> GestorAlertas.mostrarInfo(this, "Mostrando facturación de tanqueros"));
        botones[3].addActionListener(e -> GestorAlertas.mostrarExito(this, "Comprobante descargado exitosamente"));

        return panel;
    }

    private JPanel crearPanelGerente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setOpaque(false);

        Botón[] botones = {
            new Botón("Consultar Factura", new Color(70, 128, 139)),
            new Botón("Consultar Reportes", new Color(59, 130, 246)),
            new Botón("Revisar Detalle", new Color(16, 185, 129)),
            new Botón("Aprobar Anulaciones", new Color(234, 177, 0)),
            new Botón("Supervisar Cuentas", new Color(220, 38, 38)),
            new Botón("Configurar Condiciones", new Color(147, 51, 234)),
            new Botón("Generar Estados", new Color(249, 115, 22))
        };

        Dimension dimBoton = new Dimension(180, 40);
        for (Botón b : botones) {
            b.setPreferredSize(dimBoton);
            panelBotones.add(b);
        }

        panel.add(panelBotones, BorderLayout.NORTH);

        JPanel panelMetricas = new JPanel(new GridLayout(3, 3, 10, 10));
        panelMetricas.setOpaque(false);
        panelMetricas.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelMetricas.add(crearPanelInfo("Facturación Total", "$45,890.75", Color.GREEN));
        panelMetricas.add(crearPanelInfo("Cuentas por Cobrar", "$12,340.20", Color.ORANGE));
        panelMetricas.add(crearPanelInfo("Clientes Activos", "18", Color.CYAN));
        panelMetricas.add(crearPanelInfo("Facturas Pendientes", "7", Color.YELLOW));
        panelMetricas.add(crearPanelInfo("Facturas Anuladas", "2", Color.RED));
        panelMetricas.add(crearPanelInfo("Tasa de Morosidad", "8.5%", Color.MAGENTA));
        panelMetricas.add(crearPanelInfo("Promedio de Pago", "4.2 días", Color.BLUE));
        panelMetricas.add(crearPanelInfo("Facturación Promedio", "$2,550.60", Color.PINK));

        panel.add(panelMetricas, BorderLayout.CENTER);

        // Action Listeners
        botones[3].addActionListener(e -> GestorAlertas.mostrarAdvertencia(this, "Revisando solicitudes de anulación"));
        botones[5].addActionListener(e -> GestorAlertas.mostrarExito(this, "Configuración de condiciones guardada"));

        return panel;
    }

    private JPanel crearPanelInfo(String titulo, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(55, 65, 81));
        panel.setBorder(new LineBorder(color, 2, true));
        panel.setPreferredSize(new Dimension(200, 100));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setBorder(new EmptyBorder(10, 5, 5, 5));

        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setForeground(color);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValor.setBorder(new EmptyBorder(5, 5, 10, 5));

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);

        return panel;
    }

    private void crearTablaFacturas() {
        String[] columnas = {
            "N° Factura", "Fecha", "Cliente", "Monto",
            "Estado", "Fecha Vencimiento", "Acciones"
        };

        Object[][] datos = {
            {"FAC-001", "20/12/2025", "Cliente A", "$1,250.00", "Pagada", "25/12/2025", "Ver"},
            {"FAC-002", "19/12/2025", "Cliente B", "$890.50", "Pendiente", "24/12/2025", "Ver"},
            {"FAC-003", "18/12/2025", "Cliente C", "$2,340.00", "Anulada", "23/12/2025", "Ver"},
            {"FAC-004", "17/12/2025", "Cliente D", "$1,780.25", "Pagada", "22/12/2025", "Ver"}
        };

        modeloTabla = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // solo columna acciones
            }
        };

        tablaFacturas = new JTable(modeloTabla);
        tablaFacturas.setBackground(new Color(31, 41, 55));
        tablaFacturas.setForeground(Color.WHITE);
        tablaFacturas.setGridColor(new Color(55, 65, 81));
        tablaFacturas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaFacturas.setRowHeight(35);
        tablaFacturas.setSelectionBackground(new Color(75, 85, 99));
        tablaFacturas.setSelectionForeground(Color.WHITE);

        JTableHeader header = tablaFacturas.getTableHeader();
        header.setBackground(new Color(243, 244, 246));
        header.setForeground(new Color(31, 41, 55));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setBackground(new Color(31, 41, 55));
        render.setForeground(Color.WHITE);
        render.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tablaFacturas.getColumnCount(); i++) {
            tablaFacturas.getColumnModel().getColumn(i).setCellRenderer(render);
        }

        tablaFacturas.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaFacturas.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaFacturas.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaFacturas.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaFacturas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaFacturas.getColumnModel().getColumn(5).setPreferredWidth(120);
        tablaFacturas.getColumnModel().getColumn(6).setPreferredWidth(80);
    }
}
