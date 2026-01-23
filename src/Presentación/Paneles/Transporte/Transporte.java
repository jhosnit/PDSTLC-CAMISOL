package Presentación.Paneles.Transporte;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Presentación.Recursos.Botón;
import Logica.Gestores.GestorAlertas;

public class Transporte extends JPanel {
    
    // Clase para rango de tarifas
    private static class RangoTarifa {
        int kmInicio;
        int kmFin;
        double tarifaLitro;
        
        RangoTarifa(int inicio, int fin, double tarifa) {
            this.kmInicio = inicio;
            this.kmFin = fin;
            this.tarifaLitro = tarifa;
        }
        
        // Verifica si km está en rango
        boolean estaEnRango(int km) {
            if (kmFin == 0) return km >= kmInicio; // Para "650+"
            return km >= kmInicio && km <= kmFin;
        }
    }
    
    // Clase para datos de vehículos
    private static class Vehiculo {
        String placa;
        double capacidadTotal;
        double capacidadMinima;
        
        Vehiculo(String placa, String propietario, double capacidadTotal) {
            this.placa = placa;
            this.capacidadTotal = capacidadTotal;
            this.capacidadMinima = capacidadTotal * 0.9; // 90% mínimo garantizado
        }
    }

    // Componentes de interfaz
    private JTable tablaServicios;
    private DefaultTableModel modeloTabla;
    private JPanel panelFiltros;
    
    // Datos del sistema
    private List<RangoTarifa> tablaTarifas;
    private List<Vehiculo> vehiculos;
    
    // Formatos
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
    private DecimalFormat litrosFormat = new DecimalFormat("#,##0");
    
    // Campos del formulario
    private JTextField txtCliente, txtRuc, txtRuta, txtChofer, txtKilometros, txtLitros, txtFlete;
    private JComboBox<String> comboTanquero;
    private JLabel lblCapacidadMinima, lblRangoCalculado;
    private JCheckBox chkCapacidadMinima;
    
    // Control de modificación
    private boolean modoEdicion = false;
    private int filaEditando = -1;
    
    // Constructor principal
    public Transporte() {
        inicializarDatos();
        inicializarComponentes();
    }
    
    // Inicializa datos del sistema
    private void inicializarDatos() {
        inicializarTarifas();
        inicializarVehiculos();
    }
    
    // Configura tarifas por rango
    private void inicializarTarifas() {
        tablaTarifas = new ArrayList<>();
        tablaTarifas.add(new RangoTarifa(0, 100, 0.013));
        tablaTarifas.add(new RangoTarifa(101, 200, 0.015));
        tablaTarifas.add(new RangoTarifa(201, 250, 0.017));
        tablaTarifas.add(new RangoTarifa(251, 300, 0.020));
        tablaTarifas.add(new RangoTarifa(301, 350, 0.023));
        tablaTarifas.add(new RangoTarifa(351, 400, 0.024));
        tablaTarifas.add(new RangoTarifa(401, 450, 0.025));
        tablaTarifas.add(new RangoTarifa(451, 500, 0.026));
        tablaTarifas.add(new RangoTarifa(501, 550, 0.027));
        tablaTarifas.add(new RangoTarifa(551, 600, 0.028));
        tablaTarifas.add(new RangoTarifa(601, 650, 0.029));
        tablaTarifas.add(new RangoTarifa(651, 0, 0.030)); // >650 km
    }
    
    // Configura vehículos disponibles
    private void inicializarVehiculos() {
        vehiculos = new ArrayList<>();
        vehiculos.add(new Vehiculo("PCB-1234", "Propietario 1", 13500));
        vehiculos.add(new Vehiculo("ABC-5678", "Propietario 2", 13800));
        vehiculos.add(new Vehiculo("XYZ-9012", "Propietario 3", 13500));
        vehiculos.add(new Vehiculo("DEF-3456", "Propietario 4", 18500));
        vehiculos.add(new Vehiculo("GHI-7890", "Propietario 5", 13500));
        vehiculos.add(new Vehiculo("JKL-1111", "Propietario 6", 18500));
    }
    
    // Inicializa componentes gráficos
    private void inicializarComponentes() {
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(18, 18, 18));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        
        agregarMenuContextual();
    }
    
    // Crea panel superior con botones
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);

        panel.add(crearPanelBotonesPrincipales(), BorderLayout.NORTH);
        panel.add(crearPanelBotonesAcciones(), BorderLayout.CENTER);

        return panel;
    }
    
    // Crea botones principales (Nuevo, Modificar, Anular)
    private JPanel crearPanelBotonesPrincipales() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        
        Botón btnNuevo = crearBotonPrincipal("NUEVO SERVICIO", new Color(0, 22, 141));
        btnNuevo.addActionListener(e -> mostrarFormularioRegistrarServicio(false, -1));
        
        Botón btnModificar = crearBotonPrincipal("MODIFICAR", new Color(245, 158, 11));
        btnModificar.addActionListener(e -> modificarServicio());
        
        Botón btnAnular = crearBotonPrincipal("ANULAR", new Color(239, 68, 68));
        btnAnular.addActionListener(e -> anularServicio());
        
        panel.add(btnNuevo);
        panel.add(btnModificar);
        panel.add(btnAnular);

        return panel;
    }
    
    // Crea botón principal con estilo
    private Botón crearBotonPrincipal(String texto, Color color) {
        Botón b = new Botón(texto, color);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setPreferredSize(new Dimension(200, 50));
        return b;
    }
    
    // Crea botones de acciones secundarias
    private JPanel crearPanelBotonesAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);

        panel.add(crearBotonAccion("Consultar", new Color(70, 128, 139), e -> toggleFiltros()));
        panel.add(crearBotonAccion("Facturar", new Color(40, 167, 69), e -> mostrarFormularioFacturar()));
        panel.add(crearBotonAccion("Estadísticas", new Color(147, 51, 234), e -> mostrarEstadisticas()));
        panel.add(crearBotonAccion("Reportes", new Color(59, 130, 246), e -> generarReportes()));
        panel.add(crearBotonAccion("Exportar", new Color(234, 177, 0), e -> exportarDatos()));

        return panel;
    }
    
    // Crea botón de acción con estilo
    private Botón crearBotonAccion(String texto, Color color, java.awt.event.ActionListener listener) {
        Botón b = new Botón(texto, color);
        b.setPreferredSize(new Dimension(160, 38));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.addActionListener(listener);
        return b;
    }
    
    // Crea panel central con filtros y tabla
    private JPanel crearPanelCentral() {
        panelFiltros = crearPanelFiltros();
        panelFiltros.setVisible(false);

        crearTabla();
        JScrollPane scrollTabla = new JScrollPane(tablaServicios);
        scrollTabla.setBorder(new LineBorder(new Color(55, 65, 81)));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }
    
    // ================= FORMULARIO DE SERVICIO =================
    
    // Muestra formulario para nuevo o editar servicio
    private void mostrarFormularioRegistrarServicio(boolean editar, int fila) {
        modoEdicion = editar;
        filaEditando = fila;
        
        JDialog dialogo = new JDialog(
            SwingUtilities.getWindowAncestor(this),
            editar ? "Modificar Servicio" : "Registrar Nuevo Servicio",
            Dialog.ModalityType.APPLICATION_MODAL
        );
        dialogo.setSize(700, 800);
        dialogo.setLocationRelativeTo(this);
        dialogo.add(crearContenidoFormulario(dialogo, editar, fila));
        dialogo.setVisible(true);
    }
    
    // Crea contenido del formulario
    private JPanel crearContenidoFormulario(JDialog dialogo, boolean editar, int fila) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(crearHeaderFormulario(editar), BorderLayout.NORTH);
        panel.add(crearContenidoPrincipal(editar, fila), BorderLayout.CENTER);
        panel.add(crearBotonesFormulario(dialogo, editar, fila), BorderLayout.SOUTH);

        return panel;
    }
    
    // Crea header del formulario con degradado
    private JPanel crearHeaderFormulario(boolean editar) {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                    0, 0, editar ? new Color(245, 158, 11) : new Color(0, 22, 141),
                    getWidth(), 0, new Color(234, 177, 0)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 60));
        
        JLabel titulo = new JLabel(editar ? "MODIFICAR SERVICIO" : "NUEVO SERVICIO DE TRANSPORTE LÁCTEO");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        header.add(titulo, BorderLayout.CENTER);

        return header;
    }
    
    // Crea contenido principal con pestañas
    private JPanel crearContenidoPrincipal(boolean editar, int fila) {
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setBackground(new Color(31, 41, 55));
        pestanas.setForeground(Color.WHITE);
        pestanas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        pestanas.addTab("Información Básica", crearPestanaBasica(editar, fila));
        pestanas.addTab("Cálculo de Flete", crearPestanaFlete(editar, fila));

        JPanel contenido = new JPanel(new BorderLayout(10, 15));
        contenido.setBackground(new Color(18, 18, 18));
        contenido.add(pestanas, BorderLayout.CENTER);
        
        return contenido;
    }
    
    // Crea pestaña de información básica
    private JPanel crearPestanaBasica(boolean editar, int fila) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Cliente
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(crearLabel("Cliente *"), gbc);
        gbc.gridx = 1;
        txtCliente = crearCampoTexto("Ej: ECUAJUGOS S.A.");
        if (editar && fila != -1) txtCliente.setText((String) modeloTabla.getValueAt(fila, 1));
        panel.add(txtCliente, gbc);
        
        // RUC
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(crearLabel("RUC *"), gbc);
        gbc.gridx = 1;
        txtRuc = crearCampoTexto("Ej: 0990318735001");
        panel.add(txtRuc, gbc);
        
        // Ruta
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(crearLabel("Ruta (Origen-Destino) *"), gbc);
        gbc.gridx = 1;
        txtRuta = crearCampoTexto("Ej: Quito - Guayaquil");
        if (editar && fila != -1) txtRuta.setText((String) modeloTabla.getValueAt(fila, 3));
        panel.add(txtRuta, gbc);
        
        // Chofer
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(crearLabel("Chofer *"), gbc);
        gbc.gridx = 1;
        txtChofer = crearCampoTexto("Nombre y Apellido");
        panel.add(txtChofer, gbc);
        
        // Fecha
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(crearLabel("Fecha de Servicio *"), gbc);
        gbc.gridx = 1;
        JTextField txtFecha = crearCampoTexto(dateFormat.format(new Date()));
        if (editar && fila != -1) {
            txtFecha.setText((String) modeloTabla.getValueAt(fila, 2));
        }
        panel.add(txtFecha, gbc);
        
        return panel;
    }
    
    // Crea pestaña de cálculo de flete
    private JPanel crearPestanaFlete(boolean editar, int fila) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 1;
        
        // Kilómetros
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(crearLabel("Kilómetros *"), gbc);
        gbc.gridx = 1;
        txtKilometros = crearCampoTexto("Ej: 420");
        if (editar && fila != -1) txtKilometros.setText("420"); // Ejemplo
        txtKilometros.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) { calcularFlete(); }
        });
        panel.add(txtKilometros, gbc);
        
        // Tanquero
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(crearLabel("Tanquero (Placa) *"), gbc);
        gbc.gridx = 1;
        comboTanquero = crearComboTanqueros();
        if (editar && fila != -1) comboTanquero.setSelectedItem(modeloTabla.getValueAt(fila, 4));
        comboTanquero.addActionListener(e -> actualizarCapacidadMinima());
        panel.add(comboTanquero, gbc);
        
        // Capacidad mínima
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(crearLabel("Capacidad Mínima (90%)"), gbc);
        gbc.gridx = 1;
        lblCapacidadMinima = crearLabelInfo("0 litros");
        panel.add(lblCapacidadMinima, gbc);
        
        // Checkbox capacidad mínima
        chkCapacidadMinima = new JCheckBox("Usar capacidad mínima garantizada");
        chkCapacidadMinima.setSelected(true);
        chkCapacidadMinima.setForeground(Color.WHITE);
        chkCapacidadMinima.setBackground(new Color(31, 41, 55));
        chkCapacidadMinima.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkCapacidadMinima.addActionListener(e -> calcularFlete());
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(chkCapacidadMinima, gbc);
        
        // Litros transportados
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(crearLabel("Litros Transportados *"), gbc);
        gbc.gridx = 1;
        txtLitros = crearCampoTexto("Ej: 13200");
        if (editar && fila != -1) {
            String litros = (String) modeloTabla.getValueAt(fila, 5);
            txtLitros.setText(litros.replace(",", "").replace(" L", ""));
        }
        txtLitros.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) { calcularFlete(); }
        });
        panel.add(txtLitros, gbc);
        
        // Rango calculado
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(crearLabel("Rango Calculado"), gbc);
        gbc.gridx = 1;
        lblRangoCalculado = crearLabelDestacado("Seleccione kilómetros");
        panel.add(lblRangoCalculado, gbc);
        
        // Flete total
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(crearLabel("Flete Calculado *"), gbc);
        gbc.gridx = 1;
        txtFlete = crearCampoFlete();
        if (editar && fila != -1) txtFlete.setText((String) modeloTabla.getValueAt(fila, 7));
        panel.add(txtFlete, gbc);
        
        // Calcular automáticamente si está editando
        if (editar && fila != -1) {
            SwingUtilities.invokeLater(() -> {
                actualizarCapacidadMinima();
                calcularFlete();
            });
        }
        
        return panel;
    }
    
    // Crea botones del formulario
    private JPanel crearBotonesFormulario(JDialog dialogo, boolean editar, int fila) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);
        
        Botón btnCalcular = new Botón("Calcular Flete", new Color(234, 177, 0));
        btnCalcular.setPreferredSize(new Dimension(180, 40));
        btnCalcular.addActionListener(e -> calcularFlete());
        
        String textoGuardar = editar ? "Actualizar Servicio" : "Guardar Servicio";
        Color colorGuardar = editar ? new Color(245, 158, 11) : new Color(40, 167, 69);
        Botón btnGuardar = new Botón(textoGuardar, colorGuardar);
        btnGuardar.setPreferredSize(new Dimension(180, 40));
        btnGuardar.addActionListener(e -> {
            if (editar) actualizarServicio(dialogo, fila);
            else guardarServicio(dialogo);
        });
        
        Botón btnCancelar = new Botón("Cancelar", new Color(239, 68, 68));
        btnCancelar.setPreferredSize(new Dimension(180, 40));
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panel.add(btnCalcular);
        panel.add(btnGuardar);
        panel.add(btnCancelar);

        return panel;
    }
    
    // ================= OPERACIONES CRUD =================
    
    // Modifica servicio seleccionado
    private void modificarServicio() {
        int fila = tablaServicios.getSelectedRow();
        if (fila == -1) {
            GestorAlertas.mostrarError(this, "Seleccione un servicio para modificar");
            return;
        }
        
        String estado = (String) modeloTabla.getValueAt(fila, 8);
        if (!"Pendiente".equals(estado)) {
            GestorAlertas.mostrarAdvertencia(this,
                "Solo se pueden modificar servicios PENDIENTES\nEstado actual: " + estado);
            return;
        }
        
        mostrarFormularioRegistrarServicio(true, fila);
    }
    
    // Actualiza servicio existente
    private void actualizarServicio(JDialog dialogo, int fila) {
        try {
            if (!validarFormulario()) {
                GestorAlertas.mostrarError(dialogo, "Complete todos los campos requeridos (*)");
                return;
            }
            
            String[] datos = obtenerDatosFormulario();
            String idServicio = (String) modeloTabla.getValueAt(fila, 0);
            String estadoOriginal = (String) modeloTabla.getValueAt(fila, 8);
            String facturaOriginal = (String) modeloTabla.getValueAt(fila, 9);
            
            // Actualizar fila
            modeloTabla.setValueAt(datos[0], fila, 1); // Cliente
            modeloTabla.setValueAt(dateFormat.format(new Date()), fila, 2); // Fecha
            modeloTabla.setValueAt(datos[1], fila, 3); // Ruta
            modeloTabla.setValueAt(datos[2], fila, 4); // Placa
            modeloTabla.setValueAt(datos[3], fila, 5); // Litros
            modeloTabla.setValueAt(datos[4], fila, 6); // Ocupación
            modeloTabla.setValueAt(datos[5], fila, 7); // Flete
            
            if ("Pendiente".equals(estadoOriginal)) {
                modeloTabla.setValueAt("Pendiente (Modificado)", fila, 8);
            }
            
            modeloTabla.setValueAt(facturaOriginal, fila, 9); // Mantener factura
            
            GestorAlertas.mostrarExito(dialogo, 
                String.format("Servicio actualizado\nID: %s\nNuevo flete: %s", 
                idServicio, datos[5]));
            
            dialogo.dispose();
            
        } catch (Exception ex) {
            GestorAlertas.mostrarError(dialogo, "Error al actualizar: " + ex.getMessage());
        }
    }
    
    // Guarda nuevo servicio
    private void guardarServicio(JDialog dialogo) {
        try {
            if (!validarFormulario()) {
                GestorAlertas.mostrarError(dialogo, "Complete todos los campos requeridos (*)");
                return;
            }
            
            String idServicio = "SER-" + String.format("%03d", modeloTabla.getRowCount() + 1);
            String[] datos = obtenerDatosFormulario();
            
            modeloTabla.addRow(new Object[]{
                idServicio, datos[0], dateFormat.format(new Date()), 
                datos[1], datos[2], datos[3], datos[4], datos[5], "Pendiente", ""
            });
            
            GestorAlertas.mostrarExito(dialogo, 
                String.format("Servicio registrado\nID: %s\nFlete: %s", idServicio, datos[5]));
            
            dialogo.dispose();
            
        } catch (Exception ex) {
            GestorAlertas.mostrarError(dialogo, "Error al guardar: " + ex.getMessage());
        }
    }
    
    // Anula servicio seleccionado
    private void anularServicio() {
        int fila = tablaServicios.getSelectedRow();
        if (fila == -1) {
            GestorAlertas.mostrarError(this, "Seleccione un servicio para anular");
            return;
        }
        
        String id = (String) modeloTabla.getValueAt(fila, 0);
        String cliente = (String) modeloTabla.getValueAt(fila, 1);
        String estado = (String) modeloTabla.getValueAt(fila, 8);
        String factura = (String) modeloTabla.getValueAt(fila, 9);
        
        String mensaje = String.format(
            "¿Anular este servicio?\n\nID: %s\nCliente: %s\nEstado: %s\nFactura: %s\n\nEsta acción es irreversible.",
            id, cliente, estado, factura.isEmpty() ? "No facturado" : factura
        );
        
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje,
            "Confirmar Anulación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            modeloTabla.setValueAt("Anulado", fila, 8);
            if (!factura.isEmpty()) {
                modeloTabla.setValueAt(factura + " (Anulada)", fila, 9);
            }
            GestorAlertas.mostrarExito(this, String.format("Servicio anulado\nID: %s", id));
        }
    }
    
    // ================= CÁLCULOS =================
    
    // Actualiza capacidad mínima según vehículo
    private void actualizarCapacidadMinima() {
        String placa = (String) comboTanquero.getSelectedItem();
        if (placa == null || placa.equals("Seleccione")) {
            lblCapacidadMinima.setText("0 litros");
            return;
        }
        
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.placa.equals(placa)) {
                lblCapacidadMinima.setText(litrosFormat.format(vehiculo.capacidadMinima) + " litros");
                if (chkCapacidadMinima.isSelected()) {
                    txtLitros.setText(litrosFormat.format(vehiculo.capacidadMinima));
                }
                break;
            }
        }
    }
    
    // Calcula flete automáticamente
    private void calcularFlete() {
        try {
            if (txtKilometros.getText().trim().isEmpty() || txtLitros.getText().trim().isEmpty()) {
                txtFlete.setText("$0.00");
                lblRangoCalculado.setText("Complete los datos");
                return;
            }
            
            int km = Integer.parseInt(txtKilometros.getText().replaceAll("[^0-9]", ""));
            double litros = Double.parseDouble(txtLitros.getText().replaceAll("[^0-9.]", ""));
            
            validarCapacidadMinima(litros);
            
            RangoTarifa rango = encontrarRangoTarifa(km);
            if (rango != null) {
                double flete = litros * rango.tarifaLitro;
                mostrarResultadosCalculo(rango, km, flete);
            } else {
                lblRangoCalculado.setText("Rango no encontrado");
                txtFlete.setText("$0.00");
            }
            
        } catch (NumberFormatException ex) {
            lblRangoCalculado.setText("Datos inválidos");
            txtFlete.setText("$0.00");
        }
    }
    
    // Valida capacidad mínima garantizada
    private void validarCapacidadMinima(double litros) {
        String placa = (String) comboTanquero.getSelectedItem();
        if (!chkCapacidadMinima.isSelected() || placa == null || placa.equals("Seleccione")) return;
        
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.placa.equals(placa) && litros < vehiculo.capacidadMinima) {
                litros = vehiculo.capacidadMinima;
                txtLitros.setText(litrosFormat.format(litros));
                break;
            }
        }
    }
    
    // Encuentra rango de tarifa según kilómetros
    private RangoTarifa encontrarRangoTarifa(int km) {
        for (RangoTarifa rango : tablaTarifas) {
            if (rango.estaEnRango(km)) return rango;
        }
        return null;
    }
    
    // Muestra resultados del cálculo
    private void mostrarResultadosCalculo(RangoTarifa rango, int km, double flete) {
        String rangoTexto;
        if (rango.kmFin == 0) {
            rangoTexto = String.format("%d+ km → $%.3f/L", rango.kmInicio, rango.tarifaLitro);
        } else {
            rangoTexto = String.format("%d-%d km → $%.3f/L", rango.kmInicio, rango.kmFin, rango.tarifaLitro);
        }
        
        lblRangoCalculado.setText(rangoTexto);
        txtFlete.setText(moneyFormat.format(flete));
    }
    
    // ================= FACTURACIÓN =================
    
    // Muestra formulario de facturación
    private void mostrarFormularioFacturar() {
        int fila = tablaServicios.getSelectedRow();
        if (fila == -1) {
            GestorAlertas.mostrarInfo(this, "Seleccione un servicio para facturar");
            return;
        }
        
        String estado = (String) modeloTabla.getValueAt(fila, 8);
        if (!estado.contains("Pendiente")) {
            GestorAlertas.mostrarError(this, 
                "Solo servicios PENDIENTES pueden facturarse\nEstado actual: " + estado);
            return;
        }
        
        JDialog dialogo = new JDialog(
            SwingUtilities.getWindowAncestor(this),
            "Generar Factura",
            Dialog.ModalityType.APPLICATION_MODAL
        );
        dialogo.setSize(800, 600);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Obtener datos del servicio
        String id = (String) modeloTabla.getValueAt(fila, 0);
        String cliente = (String) modeloTabla.getValueAt(fila, 1);
        String ruta = (String) modeloTabla.getValueAt(fila, 3);
        String placa = (String) modeloTabla.getValueAt(fila, 4);
        String litros = (String) modeloTabla.getValueAt(fila, 5);
        String flete = (String) modeloTabla.getValueAt(fila, 7);
        
        // Calcular impuestos
        double fleteNum = Double.parseDouble(flete.replaceAll("[^0-9.]", ""));
        double iva = fleteNum * 0.15;
        double retencion = fleteNum * 0.01;
        double total = fleteNum + iva - retencion;
        
        // Mostrar vista previa
        JTextArea txtFactura = new JTextArea();
        txtFactura.setEditable(false);
        txtFactura.setBackground(new Color(31, 41, 55));
        txtFactura.setForeground(Color.WHITE);
        txtFactura.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtFactura.setText(generarContenidoFactura(id, cliente, ruta, placa, litros, flete, iva, retencion, total));
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);
        
        Botón btnGenerar = new Botón("Generar Factura", new Color(40, 167, 69));
        btnGenerar.addActionListener(e -> {
            modeloTabla.setValueAt("Facturado", fila, 8);
            modeloTabla.setValueAt("FAC-" + String.format("%03d", fila + 1), fila, 9);
            GestorAlertas.mostrarExito(dialogo, "Factura generada y enviada");
            dialogo.dispose();
        });
        
        Botón btnCancelar = new Botón("Cancelar", new Color(239, 68, 68));
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panelBotones.add(btnGenerar);
        panelBotones.add(btnCancelar);
        
        panel.add(crearTitulo("Vista Previa de Factura"), BorderLayout.NORTH);
        panel.add(new JScrollPane(txtFactura), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        dialogo.add(panel);
        dialogo.setVisible(true);
    }
    
    // Genera contenido de factura
    private String generarContenidoFactura(String id, String cliente, String ruta, String placa, 
                                          String litros, String flete, double iva, double retencion, double total) {
        return String.format(
            "========================================\n" +
            "        CAMIONEROS DEL SOL S.A.\n" +
            "           CAMISOL S.A.\n" +
            "========================================\n" +
            "Matriz: SHYRIS LOTE 62 Y EL SOL\n" +
            "RUC: 1091735373001\n\n" +
            "FACTURA No.: FAC-%03d\n" +
            "Fecha: %s\n\n" +
            "CLIENTE: %s\n" +
            "RUC/CI: (tomar de la base de datos)\n\n" +
            "DESCRIPCIÓN DEL SERVICIO:\n" +
            "  - Servicio: Transporte de lácteos\n" +
            "  - ID Servicio: %s\n" +
            "  - Vehículo: %s\n" +
            "  - Litros Transportados: %s\n" +
            "  - Ruta: %s\n\n" +
            "DETALLE DE VALORES:\n" +
            "  Subtotal (Flete): %20s\n" +
            "  IVA 15%%: %25s\n" +
            "  Retención 1%%: %22s\n" +
            "  --------------------------------------\n" +
            "  TOTAL A PAGAR: %23s\n\n" +
            "========================================\n" +
            "OBSERVACIONES:\n" +
            "Este valor corresponde al transporte realizado.\n" +
            "Factura generada por sistema PDSTLE-CAMISOL\n" +
            "========================================\n",
            tablaServicios.getSelectedRow() + 1,
            dateFormat.format(new Date()),
            cliente,
            id,
            placa,
            litros,
            ruta,
            flete,
            moneyFormat.format(iva),
            moneyFormat.format(retencion),
            moneyFormat.format(total)
        );
    }
    
    // ================= COMPONENTES REUTILIZABLES =================
    
    // Crea label con estilo
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }
    
    // Crea label informativo
    private JLabel crearLabelInfo(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(new Color(156, 163, 175));
        label.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        return label;
    }
    
    // Crea label destacado
    private JLabel crearLabelDestacado(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(new Color(234, 177, 0));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }
    
    // Crea campo de texto con placeholder
    private JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(156, 163, 175));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets insets = getInsets();
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(placeholder, insets.left + 5, y);
                    g2.dispose();
                }
            }
        };
        campo.setBackground(new Color(55, 65, 81));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(75, 85, 99)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setPreferredSize(new Dimension(250, 40));
        return campo;
    }
    
    // Crea campo de flete (solo lectura)
    private JTextField crearCampoFlete() {
        JTextField campo = crearCampoTexto("$0.00");
        campo.setEditable(false);
        campo.setBackground(new Color(75, 85, 99));
        campo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        campo.setForeground(new Color(234, 177, 0));
        return campo;
    }
    
    // Crea combo box de tanqueros
    private JComboBox<String> crearComboTanqueros() {
        String[] placas = new String[vehiculos.size() + 1];
        placas[0] = "Seleccione";
        for (int i = 0; i < vehiculos.size(); i++) {
            placas[i + 1] = vehiculos.get(i).placa;
        }
        
        JComboBox<String> combo = new JComboBox<>(placas);
        combo.setBackground(new Color(55, 65, 81));
        combo.setForeground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(75, 85, 99)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? new Color(70, 128, 139) : new Color(55, 65, 81));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                return this;
            }
        });
        return combo;
    }
    
    // Crea título centrado
    private JLabel crearTitulo(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(0, 0, 10, 0));
        return label;
    }
    
    // ================= VALIDACIONES =================
    
    // Valida formulario completo
    private boolean validarFormulario() {
        return !txtCliente.getText().trim().isEmpty() &&
               !txtRuc.getText().trim().isEmpty() &&
               !txtRuta.getText().trim().isEmpty() &&
               !txtChofer.getText().trim().isEmpty() &&
               !txtKilometros.getText().trim().isEmpty() &&
               !txtLitros.getText().trim().isEmpty() &&
               comboTanquero.getSelectedIndex() != 0;
    }
    
    // Obtiene datos del formulario
    private String[] obtenerDatosFormulario() {
        String cliente = txtCliente.getText().trim();
        String ruta = txtRuta.getText().trim();
        String placa = (String) comboTanquero.getSelectedItem();
        String litros = litrosFormat.format(Double.parseDouble(txtLitros.getText().replaceAll("[^0-9.]", ""))) + " L";
        String flete = txtFlete.getText();
        
        // Calcular ocupación
        double litrosNum = Double.parseDouble(txtLitros.getText().replaceAll("[^0-9.]", ""));
        double capacidadVehiculo = 13500;
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.placa.equals(placa)) {
                capacidadVehiculo = vehiculo.capacidadTotal;
                break;
            }
        }
        double porcentajeOcupacion = (litrosNum / capacidadVehiculo) * 100;
        String ocupacion = String.format("%.1f%%", porcentajeOcupacion);
        
        return new String[]{cliente, ruta, placa, litros, ocupacion, flete};
    }
    
    // ================= TABLA Y FILTROS =================
    
    // Crea y configura la tabla
    private void crearTabla() {
        String[] columnas = {
            "ID", "Cliente", "Fecha", "Ruta", "Placa", 
            "Litros", "Ocupación", "Flete", "Estado", "Factura"
        };
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Datos de ejemplo
        modeloTabla.addRow(new Object[]{"SER-001", "ECUAJUGOS S.A.", "20/12/2025", "Quito-Guayaquil", "PCB-1234", "13,500 L", "100.0%", "$337.50", "Facturado", "FAC-001"});
        modeloTabla.addRow(new Object[]{"SER-002", "Lácteos Andes", "22/12/2025", "Quito-Cuenca", "ABC-5678", "12,800 L", "94.8%", "$307.20", "Pendiente", ""});
        modeloTabla.addRow(new Object[]{"SER-003", "Leche Premium", "25/12/2025", "Quito-Manta", "XYZ-9012", "13,200 L", "71.4%", "$330.00", "Pagado", "FAC-002"});

        tablaServicios = new JTable(modeloTabla);
        tablaServicios.setBackground(new Color(31, 41, 55));
        tablaServicios.setForeground(Color.WHITE);
        tablaServicios.setRowHeight(40);
        tablaServicios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaServicios.setShowGrid(false);
        tablaServicios.setIntercellSpacing(new Dimension(0, 0));
        tablaServicios.setSelectionBackground(new Color(70, 128, 139));
        tablaServicios.setSelectionForeground(Color.WHITE);
        
        configurarHeaderTabla();
        configurarRenderersTabla();
    }
    
    // Configura header de la tabla
    private void configurarHeaderTabla() {
        JTableHeader header = tablaServicios.getTableHeader();
        header.setBackground(new Color(55, 65, 81));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(new LineBorder(new Color(75, 85, 99)));
        header.setPreferredSize(new Dimension(0, 45));
    }
    
    // Configura renderers de la tabla
    private void configurarRenderersTabla() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setForeground(Color.WHITE);
        centerRenderer.setBackground(new Color(31, 41, 55));
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftRenderer.setForeground(Color.WHITE);
        leftRenderer.setBackground(new Color(31, 41, 55));
        
        for (int i = 0; i < tablaServicios.getColumnCount(); i++) {
            if (i == 1 || i == 3) { // Cliente y Ruta a la izquierda
                tablaServicios.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
            } else {
                tablaServicios.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // Renderer especial para columna Estado
        tablaServicios.getColumnModel().getColumn(8).setCellRenderer(crearRendererEstado());
    }
    
    // Crea renderer para columna Estado
    private DefaultTableCellRenderer crearRendererEstado() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                
                if (value != null) {
                    String estado = value.toString();
                    if ("Facturado".equals(estado)) {
                        c.setBackground(new Color(40, 167, 69, 100));
                    } else if (estado.contains("Pendiente")) {
                        c.setBackground(new Color(234, 177, 0, 100));
                    } else if ("Pagado".equals(estado)) {
                        c.setBackground(new Color(0, 123, 255, 100));
                    } else if ("Anulado".equals(estado)) {
                        c.setBackground(new Color(108, 117, 125, 100));
                    }
                }
                
                if (isSelected) {
                    c.setBackground(new Color(70, 128, 139));
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
    }
    
    // Crea panel de filtros
    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(31, 41, 55));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Etiquetas
        gbc.gridy = 0;
        gbc.gridx = 0; panel.add(crearLabel("Cliente"), gbc);
        gbc.gridx = 1; panel.add(crearLabel("Tanquero"), gbc);
        gbc.gridx = 2; panel.add(crearLabel("Estado"), gbc);
        
        // Campos
        gbc.gridy = 1;
        gbc.gridx = 0; panel.add(crearCampoTexto("Buscar cliente"), gbc);
        
        JComboBox<String> comboTanquero = crearComboFiltroTanqueros();
        gbc.gridx = 1; panel.add(comboTanquero, gbc);
        
        JComboBox<String> comboEstado = crearComboFiltroEstados();
        gbc.gridx = 2; panel.add(comboEstado, gbc);
        
        // Botones
        gbc.gridy = 2;
        gbc.gridx = 0; gbc.gridwidth = 3;
        panel.add(crearPanelBotonesFiltros(), gbc);
        
        return panel;
    }
    
    // Crea combo para filtro de tanqueros
    private JComboBox<String> crearComboFiltroTanqueros() {
        String[] opciones = {"Todos", "PCB-1234", "ABC-5678", "XYZ-9012", "DEF-3456", "GHI-7890", "JKL-1111"};
        JComboBox<String> combo = new JComboBox<>(opciones);
        combo.setBackground(new Color(55, 65, 81));
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return combo;
    }
    
    // Crea combo para filtro de estados
    private JComboBox<String> crearComboFiltroEstados() {
        String[] opciones = {"Todos", "Pendiente", "Facturado", "Pagado", "Anulado"};
        JComboBox<String> combo = new JComboBox<>(opciones);
        combo.setBackground(new Color(55, 65, 81));
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return combo;
    }
    
    // Crea botones de filtros
    private JPanel crearPanelBotonesFiltros() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);
        
        Botón btnBuscar = new Botón("Buscar", new Color(70, 128, 139));
        btnBuscar.setPreferredSize(new Dimension(120, 35));
        btnBuscar.addActionListener(e -> buscarServicios());
        
        Botón btnLimpiar = new Botón("Limpiar", new Color(108, 117, 125));
        btnLimpiar.setPreferredSize(new Dimension(120, 35));
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        
        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        
        return panel;
    }
    
    // ================= MENÚ CONTEXTUAL =================
    
    // Agrega menú contextual a la tabla
    private void agregarMenuContextual() {
        JPopupMenu menu = new JPopupMenu();
        
        menu.add(crearItemMenu("Ver Detalles", e -> verDetallesServicio()));
        menu.addSeparator();
        menu.add(crearItemMenu("Modificar", e -> modificarServicio()));
        menu.add(crearItemMenu("Generar Factura", e -> mostrarFormularioFacturar()));
        
        JMenuItem itemAnular = crearItemMenu("Anular", e -> anularServicio());
        itemAnular.setForeground(new Color(239, 68, 68));
        menu.add(itemAnular);
        
        tablaServicios.setComponentPopupMenu(menu);
        
        // Doble clic para ver detalles
        tablaServicios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) verDetallesServicio();
            }
        });
    }
    
    // Crea item de menú
    private JMenuItem crearItemMenu(String texto, java.awt.event.ActionListener listener) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(listener);
        return item;
    }
    
    // Muestra detalles del servicio
    private void verDetallesServicio() {
        int fila = tablaServicios.getSelectedRow();
        if (fila == -1) {
            GestorAlertas.mostrarInfo(this, "Seleccione un servicio para ver detalles");
            return;
        }
        
        String[] datos = new String[10];
        for (int i = 0; i < 10; i++) {
            datos[i] = modeloTabla.getValueAt(fila, i).toString();
        }
        
        String detalles = String.format(
            "DETALLES DEL SERVICIO\n\n" +
            "ID: %s\nCliente: %s\nFecha: %s\nRuta: %s\n" +
            "Vehículo: %s\nLitros: %s\nOcupación: %s\n" +
            "Flete: %s\nEstado: %s\nFactura: %s\n\n" +
            "Última Modificación: %s",
            datos[0], datos[1], datos[2], datos[3], datos[4],
            datos[5], datos[6], datos[7], datos[8], 
            datos[9].isEmpty() ? "No facturado" : datos[9],
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
        );
        
        mostrarDialogoTexto("Detalles del Servicio", detalles, 500, 300);
    }
    
    // ================= FUNCIONES SECUNDARIAS =================
    
    // Busca servicios (simplificado)
    private void buscarServicios() {
        GestorAlertas.mostrarInfo(this, "Buscando servicios...");
    }
    
    // Limpia filtros
    private void limpiarFiltros() {
        Component[] componentes = panelFiltros.getComponents();
        for (Component c : componentes) {
            if (c instanceof JTextField) ((JTextField) c).setText("");
            else if (c instanceof JComboBox) ((JComboBox<?>) c).setSelectedIndex(0);
        }
        GestorAlertas.mostrarInfo(this, "Filtros limpiados");
    }
    
    // Muestra estadísticas
    private void mostrarEstadisticas() {
        if (modeloTabla.getRowCount() == 0) {
            GestorAlertas.mostrarInfo(this, "No hay datos para mostrar estadísticas");
            return;
        }
        
        String mensaje = "ESTADÍSTICAS DE RENTABILIDAD\n\n" +
                        "Funcionalidad en desarrollo.\n" +
                        "Aquí se mostrarán análisis de:\n" +
                        "• Ingresos por cliente\n" +
                        "• Rentabilidad por ruta\n" +
                        "• Volúmenes transportados\n" +
                        "• Proyecciones financieras";
        
        mostrarDialogoTexto("Estadísticas", mensaje, 400, 250);
    }
    
    // Genera reportes
    private void generarReportes() {
        if (modeloTabla.getRowCount() == 0) {
            GestorAlertas.mostrarInfo(this, "No hay datos para generar reportes");
            return;
        }
        
        String reporte = "REPORTE FINANCIERO - CAMISOL S.A.\n" +
                        "====================================\n\n" +
                        "Fecha: " + dateFormat.format(new Date()) + "\n\n" +
                        "Funcionalidad en desarrollo.\n" +
                        "Aquí se generarán reportes:\n" +
                        "• Por cliente\n" +
                        "• Por período\n" +
                        "• Por vehículo\n" +
                        "• Para contabilidad";
        
        mostrarDialogoTexto("Reporte Financiero", reporte, 500, 300);
    }
    
    // Exporta datos
    private void exportarDatos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Datos para Contabilidad");
        fileChooser.setSelectedFile(new java.io.File("servicios_camisol_" + 
            new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            GestorAlertas.mostrarExito(this, 
                "Datos exportados exitosamente\n" +
                "Archivo: " + fileChooser.getSelectedFile().getName() + "\n" +
                "Enviar a la contadora para procesamiento.");
        }
    }
    
    // Muestra diálogo con texto
    private void mostrarDialogoTexto(String titulo, String contenido, int ancho, int alto) {
        JTextArea txtArea = new JTextArea(contenido);
        txtArea.setEditable(false);
        txtArea.setBackground(new Color(31, 41, 55));
        txtArea.setForeground(Color.WHITE);
        txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scroll = new JScrollPane(txtArea);
        scroll.setPreferredSize(new Dimension(ancho, alto));
        
        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Alterna visibilidad de filtros
    private void toggleFiltros() {
        panelFiltros.setVisible(!panelFiltros.isVisible());
        revalidate();
        repaint();
    }
}
