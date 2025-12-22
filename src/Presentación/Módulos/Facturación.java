package Presentación.Módulos;

import Presentación.Recursos.Botón;
import Presentación.Recursos.GestorAlertas;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class Facturación extends JPanel {

    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;
    private JPanel panelFiltros;

    public Facturación() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(18, 18, 18));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        /* ================= PANEL SUPERIOR ================= */
        JPanel panelSuperior = new JPanel(new BorderLayout(0, 15));
        panelSuperior.setOpaque(false);

        Botón btnGenerarFactura = new Botón("GENERAR FACTURA", new Color(0, 22, 141));
        btnGenerarFactura.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGenerarFactura.setPreferredSize(new Dimension(300, 50));
        btnGenerarFactura.addActionListener(e -> mostrarFormularioGenerarFactura());

        JPanel panelBtnPrincipal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBtnPrincipal.setOpaque(false);
        panelBtnPrincipal.add(btnGenerarFactura);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotones.setOpaque(false);

        Botón btnConsultar = crearBoton("Consultar Facturas", new Color(70, 128, 139));
        Botón btnAnular = crearBoton("Anular Factura", new Color(239, 68, 68));
        Botón btnPago = crearBoton("Registrar Pago", new Color(40, 167, 69));
        Botón btnEstado = crearBoton("Estado de Cuenta", new Color(147, 51, 234));
        Botón btnRecordatorio = crearBoton("Programar Recordatorio", new Color(234, 177, 0));

        btnConsultar.addActionListener(e -> toggleFiltros());
        btnAnular.addActionListener(e -> GestorAlertas.mostrarInfo(this, "Solicitud de anulación"));
        btnPago.addActionListener(e -> GestorAlertas.mostrarInfo(this, "Registro de pago"));
        btnEstado.addActionListener(e -> GestorAlertas.mostrarInfo(this, "Estado de cuenta"));
        btnRecordatorio.addActionListener(e -> GestorAlertas.mostrarInfo(this, "Programar recordatorio"));

        panelBotones.add(btnConsultar);
        panelBotones.add(btnAnular);
        panelBotones.add(btnPago);
        panelBotones.add(btnEstado);
        panelBotones.add(btnRecordatorio);

        panelSuperior.add(panelBtnPrincipal, BorderLayout.NORTH);
        panelSuperior.add(panelBotones, BorderLayout.CENTER);

        /* ================= FILTROS ================= */
        panelFiltros = crearPanelFiltros();
        panelFiltros.setVisible(false);

        /* ================= TABLA ================= */
        crearTabla();
        JScrollPane scrollTabla = new JScrollPane(tablaFacturas);
        scrollTabla.setBorder(new LineBorder(new Color(55, 65, 81)));

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setOpaque(false);
        panelCentral.add(panelFiltros, BorderLayout.NORTH);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    private Botón crearBoton(String texto, Color color) {
        Botón b = new Botón(texto, color);
        b.setPreferredSize(new Dimension(170, 35));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return b;
    }

    /* ================= FORMULARIO GENERAR FACTURA ================= */
    private void mostrarFormularioGenerarFactura() {
        JDialog dialogo = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "Generar Factura",
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialogo.setSize(500, 400);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("NUEVA FACTURA");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel formulario = new JPanel(new GridLayout(5, 2, 10, 15));
        formulario.setOpaque(false);

        formulario.add(crearLabel("Cliente"));
        formulario.add(crearCampo());

        formulario.add(crearLabel("Fecha"));
        formulario.add(crearCampo());

        formulario.add(crearLabel("Vehículo"));
        formulario.add(new JComboBox<>(new String[]{"ABC-123", "XYZ-456"}));

        formulario.add(crearLabel("Producto"));
        formulario.add(crearCampo());

        formulario.add(crearLabel("Observación"));
        formulario.add(crearCampo());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botones.setOpaque(false);

        Botón btnGenerar = new Botón("Generar", new Color(40, 167, 69));
        Botón btnCancelar = new Botón("Cancelar", new Color(239, 68, 68));

        btnGenerar.addActionListener(e -> {
            GestorAlertas.mostrarExito(dialogo, "Factura generada (visual)");
            dialogo.dispose();
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        botones.add(btnGenerar);
        botones.add(btnCancelar);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(formulario, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    /* ================= FILTROS ================= */
    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new GridLayout(2, 5, 10, 10));
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(55, 65, 81)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        panel.add(crearLabel("Cliente"));
        panel.add(crearLabel("RUC"));
        panel.add(crearLabel("Desde"));
        panel.add(crearLabel("Hasta"));
        panel.add(crearLabel("Estado"));

        panel.add(crearCampo());
        panel.add(crearCampo());
        panel.add(crearCampo());
        panel.add(crearCampo());
        panel.add(new JComboBox<>(new String[]{"Todos", "Emitida", "Pagada", "Anulada"}));

        panel.add(new Botón("Buscar", new Color(70, 128, 139)));
        panel.add(new Botón("Limpiar", new Color(108, 117, 125)));

        return panel;
    }

    private JLabel crearLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Color.WHITE);
        return l;
    }

    private JTextField crearCampo() {
        JTextField t = new JTextField();
        t.setBackground(new Color(55, 65, 81));
        t.setForeground(Color.WHITE);
        t.setBorder(new LineBorder(new Color(75, 85, 99)));
        return t;
    }

    /* ================= TABLA ================= */
    private void crearTabla() {
        modeloTabla = new DefaultTableModel(
                new String[]{"Factura", "Cliente", "Fecha", "Monto", "Estado"},
                0
        );

        modeloTabla.addRow(new Object[]{"FAC-001", "Lácteos Andes", "20/12/2025", "$1200", "Pagada"});
        modeloTabla.addRow(new Object[]{"FAC-002", "Leche Premium", "22/12/2025", "$980", "Emitida"});

        tablaFacturas = new JTable(modeloTabla);
        tablaFacturas.setBackground(new Color(31, 41, 55));
        tablaFacturas.setForeground(Color.WHITE);
        tablaFacturas.setRowHeight(35);
    }

    private void toggleFiltros() {
        panelFiltros.setVisible(!panelFiltros.isVisible());
        revalidate();
        repaint();
    }
}
