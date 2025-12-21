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
    private JTextField txtBuscar;
    private JComboBox<String> comboEstado, comboCliente;

    public Facturación() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabs.addTab("Secretaria", crearPanelSecretaria());
        tabs.addTab("Dueños de Tanqueros", crearPanelDueños());
        tabs.addTab("Gerente General", crearPanelGerente());

        add(tabs, BorderLayout.CENTER);
    }

    // ===================== SECRETARIA =====================
    private JPanel crearPanelSecretaria() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(31, 41, 55));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setOpaque(false);

        Botón btnViaje = new Botón("Registrar Viaje", new Color(40, 167, 69));
        Botón btnFactura = new Botón("Generar Factura", new Color(59, 130, 246));
        Botón btnPago = new Botón("Registrar Pago", new Color(234, 177, 0));
        Botón btnAnular = new Botón("Anular Factura", new Color(239, 68, 68));
        Botón btnContra = new Botón("Contra Factura", new Color(147, 51, 234));
        Botón btnCobro = new Botón("Gestión de Cobro", new Color(220, 38, 38));
        Botón btnReporte = new Botón("Reportes", new Color(16, 185, 129));

        Dimension d = new Dimension(170, 40);
        for (Botón b : new Botón[]{btnViaje, btnFactura, btnPago, btnAnular, btnContra, btnCobro, btnReporte}) {
            b.setPreferredSize(d);
            panelBotones.add(b);
        }

        panel.add(panelBotones, BorderLayout.NORTH);

        // Filtros 
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filtros.setOpaque(false);

        txtBuscar = new JTextField(15);
        comboEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "Pagada", "Anulada"});
        comboCliente = new JComboBox<>(new String[]{"Todos", "Cliente A", "Cliente B"});

        Botón btnFiltrar = new Botón("Filtrar", new Color(70, 128, 139));
        btnFiltrar.setPreferredSize(new Dimension(100, 35));

        filtros.add(crearLabel("Buscar:"));
        filtros.add(txtBuscar);
        filtros.add(crearLabel("Estado:"));
        filtros.add(comboEstado);
        filtros.add(crearLabel("Cliente:"));
        filtros.add(comboCliente);
        filtros.add(btnFiltrar);

        // Tabla 
        crearTablaFacturas();
        JScrollPane scroll = new JScrollPane(tablaFacturas);
        scroll.getViewport().setBackground(new Color(31, 41, 55));
        scroll.setBorder(new LineBorder(new Color(55, 65, 81)));

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.add(filtros, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        panel.add(centro, BorderLayout.CENTER);

        // Acciones 
        btnViaje.addActionListener(e -> GestorAlertas.mostrarExito(this, "Formulario de viaje (GUI)"));
        btnFactura.addActionListener(e -> GestorAlertas.mostrarExito(this, "Generar factura (GUI)"));
        btnPago.addActionListener(e -> GestorAlertas.mostrarExito(this, "Registrar pago (GUI)"));
        btnAnular.addActionListener(e -> GestorAlertas.mostrarAdvertencia(this, "Solicitud de anulación"));
        btnContra.addActionListener(e -> GestorAlertas.mostrarInfo(this, "Contra factura (GUI)"));
        btnCobro.addActionListener(e -> GestorAlertas.mostrarAdvertencia(this, "Gestión de cobro"));
        btnReporte.addActionListener(e -> GestorAlertas.mostrarExito(this, "Reportes financieros"));

        return panel;
    }

    // ===================== DUEÑOS =====================
    private JPanel crearPanelDueños() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(crearPanelInfo("Facturación Total", "$15,430.00", Color.GREEN));
        panel.add(crearPanelInfo("Pagos Pendientes", "$3,200.00", Color.ORANGE));
        panel.add(crearPanelInfo("Viajes Realizados", "24", Color.CYAN));
        panel.add(crearPanelInfo("Rendimiento", "87%", Color.MAGENTA));

        return panel;
    }

    // ===================== GERENTE =====================
    private JPanel crearPanelGerente() {
        JPanel panel = new JPanel(new GridLayout(3, 3, 15, 15));
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(crearPanelInfo("Facturación Total", "$45,890.75", Color.GREEN));
        panel.add(crearPanelInfo("Cuentas por Cobrar", "$12,340.20", Color.ORANGE));
        panel.add(crearPanelInfo("Clientes Activos", "18", Color.CYAN));
        panel.add(crearPanelInfo("Facturas Pendientes", "7", Color.YELLOW));
        panel.add(crearPanelInfo("Facturas Anuladas", "2", Color.RED));
        panel.add(crearPanelInfo("Morosidad", "8.5%", Color.MAGENTA));

        return panel;
    }

    // COMPONENTES 
    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }

    private JPanel crearPanelInfo(String titulo, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(55, 65, 81));
        panel.setBorder(new LineBorder(color, 2, true));

        JLabel t = new JLabel(titulo, SwingConstants.CENTER);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel v = new JLabel(valor, SwingConstants.CENTER);
        v.setForeground(color);
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));

        panel.add(t, BorderLayout.NORTH);
        panel.add(v, BorderLayout.CENTER);
        return panel;
    }

    private void crearTablaFacturas() {
        String[] columnas = {
                "Factura", "Fecha", "Cliente", "Monto", "Estado", "Acción"
        };

        Object[][] datos = {
                {"FAC-001", "20/12/2025", "Cliente A", "$1,250.00", "Pagada", "Ver"},
                {"FAC-002", "19/12/2025", "Cliente B", "$890.50", "Pendiente", "Ver"}
        };

        modeloTabla = new DefaultTableModel(datos, columnas) {
            public boolean isCellEditable(int r, int c) { return c == 5; }
        };

        tablaFacturas = new JTable(modeloTabla);
        tablaFacturas.setRowHeight(35);
        tablaFacturas.setBackground(new Color(31, 41, 55));
        tablaFacturas.setForeground(Color.WHITE);
        tablaFacturas.setSelectionBackground(new Color(75, 85, 99));

        tablaFacturas.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        tablaFacturas.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox()));

        JTableHeader h = tablaFacturas.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setBackground(new Color(243, 244, 246));
        h.setForeground(new Color(31, 41, 55));

        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        r.setBackground(new Color(31, 41, 55));
        r.setForeground(Color.WHITE);

        for (int i = 0; i < 5; i++) {
            tablaFacturas.getColumnModel().getColumn(i).setCellRenderer(r);
        }
    }

    // BOTÓN EN TABLA
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setText("Ver");
            setForeground(Color.WHITE);
            setBackground(new Color(59, 130, 246));
            setFocusPainted(false);
            setBorderPainted(false);
        }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button = new JButton();

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(59, 130, 246));
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                fireEditingStopped();
                GestorAlertas.mostrarInfo(Facturación.this, "Vista previa de factura ");
            });
        }

        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            button.setText("Ver");
            return button;
        }
    }
}
