package Presentación.Módulos;

import Presentación.Recursos.Botón;
import Presentación.Recursos.GestorAlertas;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Facturación extends JPanel {
    
    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;
    private JPanel panelFiltros;
    private JTextField txtBuscarCliente, txtBuscarRUC, txtFechaInicio, txtFechaFin;
    private JComboBox<String> comboEstado;
    
    public Facturación() {
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(18, 18, 18));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // ========== PANEL SUPERIOR - BOTÓN PRINCIPAL ==========
        JPanel panelSuperior = new JPanel(new BorderLayout(0, 15));
        panelSuperior.setOpaque(false);
        
        // BOTÓN PRINCIPAL GRANDE
        Botón btnGenerarFactura = new Botón("GENERAR FACTURA", new Color(0, 22, 141));
        btnGenerarFactura.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGenerarFactura.setPreferredSize(new Dimension(300, 50));
        btnGenerarFactura.addActionListener(e -> mostrarFormularioGenerarFactura());
        
        JPanel panelBtnPrincipal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBtnPrincipal.setOpaque(false);
        panelBtnPrincipal.add(btnGenerarFactura);
        
        // BOTONES SECUNDARIOS HORIZONTALES
        JPanel panelBotonesSecundarios = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotonesSecundarios.setOpaque(false);
        
        Botón btnConsultar = crearBotonSecundario("Consultar Facturas", new Color(70, 128, 139));
        Botón btnAnular = crearBotonSecundario("Anular Factura", new Color(239, 68, 68));
        Botón btnRegistrarPago = crearBotonSecundario("Registrar Pago", new Color(40, 167, 69));
        Botón btnEstadoCuenta = crearBotonSecundario("Estado de Cuenta", new Color(147, 51, 234));
        Botón btnRecordatorio = crearBotonSecundario("Programar Recordatorio", new Color(234, 177, 0));
        
        panelBotonesSecundarios.add(btnConsultar);
        panelBotonesSecundarios.add(btnAnular);
        panelBotonesSecundarios.add(btnRegistrarPago);
        panelBotonesSecundarios.add(btnEstadoCuenta);
        panelBotonesSecundarios.add(btnRecordatorio);
        
        panelSuperior.add(panelBtnPrincipal, BorderLayout.NORTH);
        panelSuperior.add(panelBotonesSecundarios, BorderLayout.CENTER);
        
        // ========== PANEL FILTROS ==========
        panelFiltros = crearPanelFiltros();
        
        // ========== TABLA CENTRAL ==========
        crearTabla();
        JScrollPane scrollTabla = new JScrollPane(tablaFacturas);
        scrollTabla.getViewport().setBackground(new Color(31, 41, 55));
        scrollTabla.setBorder(new LineBorder(new Color(55, 65, 81), 1));
        
        // ========== ENSAMBLADO FINAL ==========
        add(panelSuperior, BorderLayout.NORTH);
        add(panelFiltros, BorderLayout.CENTER);
        add(scrollTabla, BorderLayout.SOUTH);
        
        // Asignar eventos a botones secundarios
        btnConsultar.addActionListener(e -> activarFiltros());
        btnAnular.addActionListener(e -> solicitarAnulacion());
        btnRegistrarPago.addActionListener(e -> registrarPago());
        btnEstadoCuenta.addActionListener(e -> generarEstadoCuenta());
        btnRecordatorio.addActionListener(e -> programarRecordatorio());
    }
    
    private Botón crearBotonSecundario(String texto, Color color) {
        Botón boton = new Botón(texto, color);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        boton.setPreferredSize(new Dimension(160, 35));
        return boton;
    }
    
    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 10, 10));
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(55, 65, 81), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setVisible(false); // Inicialmente oculto
        
        // Etiquetas blancas
        JLabel lblCliente = new JLabel("Cliente:");
        JLabel lblRUC = new JLabel("RUC/Cédula:");
        JLabel lblFechaInicio = new JLabel("Fecha Inicio:");
        JLabel lblFechaFin = new JLabel("Fecha Fin:");
        JLabel lblEstado = new JLabel("Estado:");
        
        lblCliente.setForeground(Color.WHITE);
        lblRUC.setForeground(Color.WHITE);
        lblFechaInicio.setForeground(Color.WHITE);
        lblFechaFin.setForeground(Color.WHITE);
        lblEstado.setForeground(Color.WHITE);
        
        // Campos de búsqueda
        txtBuscarCliente = new JTextField();
        txtBuscarRUC = new JTextField();
        txtFechaInicio = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        txtFechaFin = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        comboEstado = new JComboBox<>(new String[]{"Todos", "Emitida", "Pagada", "Pendiente", "Anulada"});
        
        // Configurar campos
        configurarCampoBusqueda(txtBuscarCliente);
        configurarCampoBusqueda(txtBuscarRUC);
        configurarCampoBusqueda(txtFechaInicio);
        configurarCampoBusqueda(txtFechaFin);
        
        // Botones de acción filtros
        Botón btnBuscar = new Botón("Buscar", new Color(70, 128, 139));
        Botón btnLimpiar = new Botón("Limpiar", new Color(108, 117, 125));
        btnBuscar.setPreferredSize(new Dimension(100, 30));
        btnLimpiar.setPreferredSize(new Dimension(100, 30));
        
        btnBuscar.addActionListener(e -> buscarFacturas());
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        
        // Agregar componentes al panel
        panel.add(lblCliente);
        panel.add(txtBuscarCliente);
        panel.add(lblRUC);
        panel.add(txtBuscarRUC);
        panel.add(lblFechaInicio);
        panel.add(txtFechaInicio);
        panel.add(lblFechaFin);
        panel.add(txtFechaFin);
        panel.add(lblEstado);
        panel.add(comboEstado);
        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        
        return panel;
    }
    
    private void configurarCampoBusqueda(JTextField campo) {
        campo.setBackground(new Color(55, 65, 81));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(new CompoundBorder(
            new LineBorder(new Color(75, 85, 99), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        campo.setPreferredSize(new Dimension(150, 30));
    }
    
    private void crearTabla() {
        String[] columnas = {
            "No. Factura", "Cliente", "RUC/Cédula", "Fecha Emisión",
            "Monto Total", "Estado", "Vehículo", "Producto"
        };
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Datos de ejemplo para la cooperativa de leche
        modeloTabla.addRow(new Object[]{
            "FAC-2025-001", "LECHE DEL VALLE S.A.", "0991234567001",
            "15/12/2025", "$3,450.00", "Pagada", "ABC-123", "Leche Entera"
        });
        
        modeloTabla.addRow(new Object[]{
            "FAC-2025-002", "QUESOS ANDINOS C.A.", "0992345678001",
            "16/12/2025", "$2,800.50", "Pendiente", "XYZ-456", "Queso Fresco"
        });
        
        modeloTabla.addRow(new Object[]{
            "FAC-2025-003", "YOGURT NATURAL LTDA", "0993456789001",
            "17/12/2025", "$4,200.75", "Emitida", "DEF-789", "Yogurt"
        });
        
        modeloTabla.addRow(new Object[]{
            "FAC-2025-004", "LACTEOS PREMIUM S.A.", "0994567890001",
            "18/12/2025", "$5,600.00", "Pagada", "GHI-012", "Crema de Leche"
        });
        
        tablaFacturas = new JTable(modeloTabla);
        tablaFacturas.setBackground(new Color(31, 41, 55));
        tablaFacturas.setForeground(Color.WHITE);
        tablaFacturas.setGridColor(new Color(55, 65, 81));
        tablaFacturas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaFacturas.setRowHeight(35);
        tablaFacturas.setSelectionBackground(new Color(75, 85, 99));
        tablaFacturas.setSelectionForeground(Color.WHITE);
        
        // Cabecera
        JTableHeader header = tablaFacturas.getTableHeader();
        header.setBackground(new Color(243, 244, 246));
        header.setForeground(new Color(31, 41, 55));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 40));
        
        // Renderizador personalizado
        DefaultTableCellRenderer render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                
                c.setBackground(new Color(31, 41, 55));
                c.setForeground(Color.WHITE);
                
                // Colorear estado
                if (column == 5) {
                    String estado = value.toString();
                    if (estado.equals("Pagada")) {
                        c.setForeground(new Color(40, 167, 69)); // Verde
                    } else if (estado.equals("Pendiente")) {
                        c.setForeground(new Color(239, 68, 68)); // Rojo
                    } else if (estado.equals("Emitida")) {
                        c.setForeground(new Color(234, 177, 0)); // Amarillo
                    }
                }
                
                // Centrar columnas numéricas
                if (column == 4 || column == 6) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                }
                
                return c;
            }
        };
        
        for (int i = 0; i < tablaFacturas.getColumnCount(); i++) {
            tablaFacturas.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        
        // Ajustar anchos
        tablaFacturas.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaFacturas.getColumnModel().getColumn(1).setPreferredWidth(180);
        tablaFacturas.getColumnModel().getColumn(2).setPreferredWidth(130);
        tablaFacturas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaFacturas.getColumnModel().getColumn(5).setPreferredWidth(90);
        tablaFacturas.getColumnModel().getColumn(6).setPreferredWidth(80);
        tablaFacturas.getColumnModel().getColumn(7).setPreferredWidth(120);
    }
    
    // ========== FUNCIONALIDADES ==========
    
    private void mostrarFormularioGenerarFactura() {
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), 
                                     "Generar Factura - Cooperativa de Leche", 
                                     Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setSize(500, 400);
        
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("NUEVA FACTURA DE TRANSPORTE");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 15));
        panelFormulario.setOpaque(false);
        panelFormulario.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Etiquetas
        JLabel lblCliente = new JLabel("Cliente:");
        JLabel lblFecha = new JLabel("Fecha:");
        JLabel lblCantidad = new JLabel("Cantidad (Litros):");
        JLabel lblTarifa = new JLabel("Tarifa por Litro:");
        JLabel lblVehiculo = new JLabel("Vehículo:");
        
        lblCliente.setForeground(Color.WHITE);
        lblFecha.setForeground(Color.WHITE);
        lblCantidad.setForeground(Color.WHITE);
        lblTarifa.setForeground(Color.WHITE);
        lblVehiculo.setForeground(Color.WHITE);
        
        // Campos
        JTextField txtCliente = new JTextField();
        JTextField txtFecha = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        JTextField txtCantidad = new JTextField();
        JTextField txtTarifa = new JTextField("0.13"); // Tarifa base
        JComboBox<String> comboVehiculo = new JComboBox<>(new String[]{"ABC-123", "XYZ-456", "DEF-789", "GHI-012"});
        
        configurarCampoFormulario(txtCliente);
        configurarCampoFormulario(txtFecha);
        configurarCampoFormulario(txtCantidad);
        configurarCampoFormulario(txtTarifa);
        comboVehiculo.setBackground(new Color(55, 65, 81));
        comboVehiculo.setForeground(Color.WHITE);
        comboVehiculo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        panelFormulario.add(lblCliente);
        panelFormulario.add(txtCliente);
        panelFormulario.add(lblFecha);
        panelFormulario.add(txtFecha);
        panelFormulario.add(lblCantidad);
        panelFormulario.add(txtCantidad);
        panelFormulario.add(lblTarifa);
        panelFormulario.add(txtTarifa);
        panelFormulario.add(lblVehiculo);
        panelFormulario.add(comboVehiculo);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setOpaque(false);
        
        Botón btnGenerar = new Botón("Generar", new Color(40, 167, 69));
        Botón btnLimpiar = new Botón("Limpiar", new Color(108, 117, 125));
        Botón btnCancelar = new Botón("Cancelar", new Color(239, 68, 68));
        
        btnGenerar.setPreferredSize(new Dimension(100, 35));
        btnLimpiar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        
        panelBotones.add(btnGenerar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCancelar);
        
        // Ensamblar
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelFormulario, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        btnLimpiar.addActionListener(e -> {
            txtCliente.setText("");
            txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            txtCantidad.setText("");
            txtTarifa.setText("0.13");
            comboVehiculo.setSelectedIndex(0);
        });
        
        btnGenerar.addActionListener(e -> {
            if (validarFormulario(txtCliente, txtCantidad, txtTarifa)) {
                // Generar número de factura
                String numeroFactura = "FAC-" + new SimpleDateFormat("yyyy").format(new Date()) + 
                                      "-" + String.format("%03d", modeloTabla.getRowCount() + 1);
                
                // Agregar a tabla
                modeloTabla.addRow(new Object[]{
                    numeroFactura,
                    txtCliente.getText().trim(),
                    "0999999999001", // RUC de ejemplo
                    txtFecha.getText().trim(),
                    "$" + calcularMonto(txtCantidad.getText(), txtTarifa.getText()),
                    "Emitida",
                    comboVehiculo.getSelectedItem(),
                    "Leche"
                });
                
                GestorAlertas.mostrarExito(dialogo, "Factura " + numeroFactura + " generada exitosamente");
                dialogo.dispose();
            }
        });
        
        dialogo.add(panel);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
    
    private void configurarCampoFormulario(JTextField campo) {
        campo.setBackground(new Color(55, 65, 81));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(new CompoundBorder(
            new LineBorder(new Color(75, 85, 99), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }
    
    private boolean validarFormulario(JTextField cliente, JTextField cantidad, JTextField tarifa) {
        if (cliente.getText().trim().isEmpty()) {
            GestorAlertas.mostrarAdvertencia(this, "Ingrese el nombre del cliente");
            return false;
        }
        
        try {
            double litros = Double.parseDouble(cantidad.getText());
            if (litros <= 0) {
                GestorAlertas.mostrarAdvertencia(this, "La cantidad debe ser mayor a 0");
                return false;
            }
            
            double precio = Double.parseDouble(tarifa.getText());
            if (precio <= 0) {
                GestorAlertas.mostrarAdvertencia(this, "La tarifa debe ser mayor a 0");
                return false;
            }
            
        } catch (NumberFormatException e) {
            GestorAlertas.mostrarAdvertencia(this, "Cantidad y Tarifa deben ser números válidos");
            return false;
        }
        
        return true;
    }
    
    private String calcularMonto(String cantidad, String tarifa) {
        try {
            double litros = Double.parseDouble(cantidad);
            double precio = Double.parseDouble(tarifa);
            double monto = litros * precio;
            return String.format("%,.2f", monto);
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }
    
    private void activarFiltros() {
        panelFiltros.setVisible(!panelFiltros.isVisible());
        revalidate();
        repaint();
    }
    
    private void buscarFacturas() {
        // Aquí iría la lógica real de búsqueda en base de datos
        GestorAlertas.mostrarExito(this, "Buscando facturas con los filtros aplicados");
    }
    
    private void limpiarFiltros() {
        txtBuscarCliente.setText("");
        txtBuscarRUC.setText("");
        txtFechaInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        txtFechaFin.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        comboEstado.setSelectedIndex(0);
    }
    
    private void solicitarAnulacion() {
        int fila = tablaFacturas.getSelectedRow();
        if (fila < 0) {
            GestorAlertas.mostrarAdvertencia(this, "Seleccione una factura para anular");
            return;
        }
        
        String numeroFactura = modeloTabla.getValueAt(fila, 0).toString();
        String motivo = JOptionPane.showInputDialog(this, 
            "Ingrese motivo de anulación para factura " + numeroFactura + ":",
            "Solicitar Anulación",
            JOptionPane.QUESTION_MESSAGE);
        
        if (motivo != null && !motivo.trim().isEmpty()) {
            modeloTabla.setValueAt("Anulada", fila, 5);
            GestorAlertas.mostrarExito(this, "Solicitud de anulación enviada al gerente");
        }
    }
    
    private void registrarPago() {
        int fila = tablaFacturas.getSelectedRow();
        if (fila < 0) {
            GestorAlertas.mostrarAdvertencia(this, "Seleccione una factura para registrar pago");
            return;
        }
        
        String numeroFactura = modeloTabla.getValueAt(fila, 0).toString();
        
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), 
                                     "Registrar Pago", 
                                     Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(400, 300);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 15));
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblFactura = new JLabel("Factura:");
        JLabel lblMonto = new JLabel("Monto Pagado:");
        JLabel lblFecha = new JLabel("Fecha Pago:");
        JLabel lblForma = new JLabel("Forma de Pago:");
        
        lblFactura.setForeground(Color.WHITE);
        lblMonto.setForeground(Color.WHITE);
        lblFecha.setForeground(Color.WHITE);
        lblForma.setForeground(Color.WHITE);
        
        JTextField txtFactura = new JTextField(numeroFactura);
        txtFactura.setEditable(false);
        JTextField txtMonto = new JTextField();
        JTextField txtFecha = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        JComboBox<String> comboForma = new JComboBox<>(new String[]{"Efectivo", "Transferencia", "Cheque", "Tarjeta"});
        
        configurarCampoFormulario(txtFactura);
        configurarCampoFormulario(txtMonto);
        configurarCampoFormulario(txtFecha);
        comboForma.setBackground(new Color(55, 65, 81));
        comboForma.setForeground(Color.WHITE);
        
        panel.add(lblFactura);
        panel.add(txtFactura);
        panel.add(lblMonto);
        panel.add(txtMonto);
        panel.add(lblFecha);
        panel.add(txtFecha);
        panel.add(lblForma);
        panel.add(comboForma);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setOpaque(false);
        
        Botón btnRegistrar = new Botón("Registrar", new Color(40, 167, 69));
        Botón btnCancelar = new Botón("Cancelar", new Color(239, 68, 68));
        
        btnRegistrar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        
        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);
        
        btnCancelar.addActionListener(e -> dialogo.dispose());
        btnRegistrar.addActionListener(e -> {
            modeloTabla.setValueAt("Pagada", fila, 5);
            GestorAlertas.mostrarExito(dialogo, "Pago registrado exitosamente");
            dialogo.dispose();
        });
        
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
    
    private void generarEstadoCuenta() {
        // Dialogo para seleccionar cliente y fechas
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this),
                                     "Generar Estado de Cuenta",
                                     Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(500, 200);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblCliente = new JLabel("Cliente:");
        JLabel lblInicio = new JLabel("Fecha Inicio:");
        JLabel lblFin = new JLabel("Fecha Fin:");
        
        lblCliente.setForeground(Color.WHITE);
        lblInicio.setForeground(Color.WHITE);
        lblFin.setForeground(Color.WHITE);
        
        JTextField txtCliente = new JTextField();
        JTextField txtInicio = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        JTextField txtFin = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        
        configurarCampoFormulario(txtCliente);
        configurarCampoFormulario(txtInicio);
        configurarCampoFormulario(txtFin);
        
        panel.add(lblCliente);
        panel.add(txtCliente);
        panel.add(lblInicio);
        panel.add(txtInicio);
        panel.add(lblFin);
        panel.add(txtFin);
        
        Botón btnGenerar = new Botón("Generar Reporte", new Color(147, 51, 234));
        btnGenerar.setPreferredSize(new Dimension(200, 35));
        btnGenerar.addActionListener(e -> {
            mostrarReporteEstadoCuenta(txtCliente.getText(), txtInicio.getText(), txtFin.getText());
            dialogo.dispose();
        });
        
        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(btnGenerar, BorderLayout.SOUTH);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
    
    private void mostrarReporteEstadoCuenta(String cliente, String inicio, String fin) {
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this),
                                     "Estado de Cuenta - " + cliente,
                                     Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setSize(700, 500);
        
        JTextArea areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setBackground(new Color(31, 41, 55));
        areaReporte.setForeground(Color.WHITE);
        areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaReporte.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Generar reporte de ejemplo
        StringBuilder reporte = new StringBuilder();
        reporte.append("COOPERATIVA DE TRANSPORTE DE LECHE CAMISOL S.A.\n");
        reporte.append("================================================\n");
        reporte.append("ESTADO DE CUENTA\n");
        reporte.append("Cliente: ").append(cliente).append("\n");
        reporte.append("Período: ").append(inicio).append(" al ").append(fin).append("\n");
        reporte.append("================================================\n");
        reporte.append("Fecha        Factura        Monto       Estado\n");
        reporte.append("------------------------------------------------\n");
        reporte.append("15/12/2025   FAC-2025-001   $3,450.00   Pagada\n");
        reporte.append("16/12/2025   FAC-2025-002   $2,800.50   Pendiente\n");
        reporte.append("------------------------------------------------\n");
        reporte.append("Total Facturado:           $6,250.50\n");
        reporte.append("Total Pagado:              $3,450.00\n");
        reporte.append("Saldo Pendiente:           $2,800.50\n");
        reporte.append("================================================\n");
        
        areaReporte.setText(reporte.toString());
        
        JScrollPane scroll = new JScrollPane(areaReporte);
        dialogo.add(scroll);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
    
    private void programarRecordatorio() {
        String fechaRecordatorio = JOptionPane.showInputDialog(this,
            "Ingrese fecha para el recordatorio (dd/mm/aaaa):",
            "Programar Recordatorio",
            JOptionPane.QUESTION_MESSAGE);
        
        if (fechaRecordatorio != null && !fechaRecordatorio.trim().isEmpty()) {
            String mensaje = JOptionPane.showInputDialog(this,
                "Ingrese mensaje del recordatorio:",
                "Mensaje de Recordatorio",
                JOptionPane.QUESTION_MESSAGE);
            
            if (mensaje != null && !mensaje.trim().isEmpty()) {
                GestorAlertas.mostrarExito(this, "Recordatorio programado para " + fechaRecordatorio);
            }
        }
    }
}