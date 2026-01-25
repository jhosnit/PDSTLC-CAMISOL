package Presentación.Paneles.Tanqueros;

import Presentación.Recursos.Botón;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Tanquero extends JPanel {
    private static final String ADMIN_PASSWORD = "admin123";

    private JTable tabla;
    private DefaultTableModel modelo;
    private Botón botónRegistrar, botónConsultar, botónModificar, botónEliminar;
    private Botón botónMantenimiento, botónNovedades, botónDisponibilidad;
    private Botón botónAsignarChofer, botónHistorialMant;

    public Tanquero() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- PANEL DE BOTONES ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBotones.setOpaque(false);

        botónRegistrar = new Botón("Registrar Vehículo", new Color(40, 167, 69));
        botónConsultar = new Botón("Consultar Vehículo", new Color(70, 128, 139));
        botónModificar = new Botón("Modificar", new Color(234, 177, 0));
        botónEliminar = new Botón("Eliminar", new Color(239, 68, 68));
        botónMantenimiento = new Botón("Mantenimiento", new Color(147, 51, 234));
        botónNovedades = new Botón("Novedades", new Color(249, 115, 22));
        botónDisponibilidad = new Botón("Disponibilidad", new Color(20, 184, 166));
        botónAsignarChofer = new Botón("Asignar Chofer", new Color(99, 102, 241)); // NUEVO
        botónHistorialMant = new Botón("Historial Mant.", new Color(168, 85, 247)); // NUEVO

        Dimension dimBoton = new Dimension(150, 40);
        botónRegistrar.setPreferredSize(dimBoton);
        botónConsultar.setPreferredSize(dimBoton);
        botónModificar.setPreferredSize(dimBoton);
        botónEliminar.setPreferredSize(dimBoton);
        botónMantenimiento.setPreferredSize(dimBoton);
        botónNovedades.setPreferredSize(dimBoton);
        botónDisponibilidad.setPreferredSize(dimBoton);
        botónAsignarChofer.setPreferredSize(dimBoton); // NUEVO
        botónHistorialMant.setPreferredSize(dimBoton); // NUEVO

        panelBotones.add(botónRegistrar);
        panelBotones.add(botónConsultar);
        panelBotones.add(botónModificar);
        panelBotones.add(botónEliminar);
        panelBotones.add(botónMantenimiento);
        panelBotones.add(botónNovedades);
        panelBotones.add(botónDisponibilidad);
        panelBotones.add(botónAsignarChofer); // NUEVO
        panelBotones.add(botónHistorialMant); // NUEVO

        add(panelBotones, BorderLayout.NORTH);

        // --- CONFIGURACIÓN DE LA TABLA ---
        String[] columnas = { "Placa", "Marca", "Modelo", "Año", "Capacidad (Gal)", "Estado" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        configurarEstiloTabla();

        JScrollPane panelDesplazamiento = new JScrollPane(tabla);
        panelDesplazamiento.setOpaque(false);
        panelDesplazamiento.getViewport().setBackground(new Color(31, 41, 55));
        panelDesplazamiento.setBorder(new LineBorder(new Color(55, 65, 81), 1));

        add(panelDesplazamiento, BorderLayout.CENTER);

        // Datos de ejemplo
        cargarDatosEjemplo();

        // Listeners
        botónRegistrar.addActionListener(e -> abrirDialogoRegistrar());
        botónConsultar.addActionListener(e -> abrirDialogoConsultar());
        botónModificar.addActionListener(e -> abrirDialogoModificar());
        botónEliminar.addActionListener(e -> eliminar());
        botónMantenimiento.addActionListener(e -> abrirDialogoMantenimiento());
        botónNovedades.addActionListener(e -> abrirDialogoNovedades());
        botónDisponibilidad.addActionListener(e -> abrirDialogoDisponibilidad());
        botónAsignarChofer.addActionListener(e -> abrirDialogoAsignarChofer()); // NUEVO
        botónHistorialMant.addActionListener(e -> abrirDialogoHistorialMantenimientos()); // NUEVO

    }

    private void configurarEstiloTabla() {
        tabla.setBackground(new Color(31, 41, 55));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(55, 65, 81));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(30);
        tabla.setSelectionBackground(new Color(75, 85, 99));
        tabla.setSelectionForeground(Color.WHITE);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(243, 244, 246));
        header.setForeground(new Color(31, 41, 55));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(new Color(31, 41, 55));
        cellRenderer.setForeground(Color.WHITE);
        cellRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    private void cargarDatosEjemplo() {
        modelo.addRow(new Object[] { "ABC-123", "Volvo", "FH16", "2020", "2113.38", "Activo" });
        modelo.addRow(new Object[] { "XYZ-4567", "Mercedes", "Actros", "2019", " 2641.72", "Inactivo" });
        modelo.addRow(new Object[] { "DEF-7890", "Scania", "R450", "2021", "2510.05", "Activo" });
    }

    // ======================== DIÁLOGO REGISTRAR VEHÍCULO (rt1)
    // ========================
    private void abrirDialogoRegistrar() {
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Registrar Vehículo ",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField textoPlaca = new JTextField(15);
        JTextField textoMarca = new JTextField(15);
        JTextField textoModelo = new JTextField(15);
        JTextField textoAño = new JTextField(15);
        JTextField textoCapacidad = new JTextField(15);
        JComboBox<String> comboEstado = new JComboBox<>(new String[] { " Activo", "Inactivo" });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Placa (Ej: ABC-1234): "), gbc);
        gbc.gridx = 1;
        panel.add(textoPlaca, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Marca"), gbc);
        gbc.gridx = 1;
        panel.add(textoMarca, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Modelo"), gbc);
        gbc.gridx = 1;
        panel.add(textoModelo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Año (1980 - " + java.time.Year.now() + "): "), gbc);
        gbc.gridx = 1;
        panel.add(textoAño, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Capacidad en Galones: "), gbc);
        gbc.gridx = 1;
        panel.add(textoCapacidad, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Estado Operativo: "), gbc);
        gbc.gridx = 1;
        panel.add(comboEstado, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Botón botónGuardar = new Botón("Guardar", new Color(40, 167, 69));
        Botón botónLimpiar = new Botón("Limpiar", new Color(108, 117, 125));
        Botón botónCancelar = new Botón("Cancelar", new Color(220, 53, 69));

        botónGuardar.setPreferredSize(new Dimension(100, 35));
        botónLimpiar.setPreferredSize(new Dimension(100, 35));
        botónCancelar.setPreferredSize(new Dimension(100, 35));

        panelBotones.add(botónGuardar);
        panelBotones.add(botónLimpiar);
        panelBotones.add(botónCancelar);

        dialogo.getContentPane().add(panel, BorderLayout.CENTER);
        dialogo.getContentPane().add(panelBotones, BorderLayout.SOUTH);

        botónLimpiar.addActionListener(ev -> {
            textoPlaca.setText("");
            textoMarca.setText("");
            textoModelo.setText("");
            textoAño.setText("");
            textoCapacidad.setText("");
            comboEstado.setSelectedIndex(0);
        });

        botónCancelar.addActionListener(ev -> dialogo.dispose());

        botónGuardar.addActionListener(ev -> {
            // Validaciones
            String placa = textoPlaca.getText().trim().toUpperCase();
            String marca = textoMarca.getText().trim();
            String modeloStr = textoModelo.getText().trim();
            String añoStr = textoAño.getText().trim();
            String capacidadStr = textoCapacidad.getText().trim();
            String estado = (String) comboEstado.getSelectedItem();

            if (placa.isEmpty() || marca.isEmpty() || modeloStr.isEmpty() || añoStr.isEmpty()
                    || capacidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Todos los campos son obligatorios.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de placa ecuatoriana (ABC-1234 o ABC-123)
            if (!placa.matches("^[A-Z]{3}-\\d{3,4}$")) {
                JOptionPane.showMessageDialog(dialogo, "Formato de placa inválido. Use: ABC-1234", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar placa duplicada
            for (int i = 0; i < modelo.getRowCount(); i++) {
                if (modelo.getValueAt(i, 0).equals(placa)) {
                    JOptionPane.showMessageDialog(dialogo, "La placa ya está registrada en el sistema.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Validar marca (solo letras)
            if (marca.length() > 50 || !marca.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
                JOptionPane.showMessageDialog(dialogo, "Marca debe tener máximo 50 caracteres alfabéticos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar modelo (alfanumérico)
            if (modeloStr.length() > 50) {
                JOptionPane.showMessageDialog(dialogo, "Modelo debe tener máximo 50 caracteres.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar año
            try {
                int año = Integer.parseInt(añoStr);
                int añoActual = java.time.Year.now().getValue();
                if (año < 1980 || año > añoActual) {
                    JOptionPane.showMessageDialog(dialogo, "Año debe estar entre 1980 y " + añoActual, "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Año debe ser un número válido.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar capacidad
            try {
                double capacidad = Double.parseDouble(capacidadStr);
                if (capacidad <= 0) {
                    JOptionPane.showMessageDialog(dialogo, "Capacidad debe ser mayor a 0.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                capacidadStr = String.format("%.2f", capacidad);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Capacidad debe ser un número válido.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Agregar a la tabla
            modelo.addRow(new Object[] { placa, marca, modeloStr, añoStr, capacidadStr, estado });
            JOptionPane.showMessageDialog(dialogo, "Tanquero registrado exitosamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });

        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    // ======================== DIÁLOGO CONSULTAR VEHÍCULO (rt2)
    // ========================
    private void abrirDialogoConsultar() {
        String placa = JOptionPane.showInputDialog(this, "Ingrese la Placa del vehículo (Ej: ABC-1234):",
                "Consultar Vehículo (rt2)", JOptionPane.QUESTION_MESSAGE);
        if (placa == null || placa.trim().isEmpty())
            return;
        placa = placa.trim().toUpperCase();

        int foundIdx = -1;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).equals(placa)) {
                foundIdx = i;
                break;
            }
        }

        if (foundIdx < 0) {
            JOptionPane.showMessageDialog(this, "Vehículo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostrar información completa
        StringBuilder info = new StringBuilder();
        info.append("INFORMACIÓN DEL TANQUERO\n\n");
        info.append("Placa: ").append(modelo.getValueAt(foundIdx, 0)).append("\n");
        info.append("Marca: ").append(modelo.getValueAt(foundIdx, 1)).append("\n");
        info.append("Modelo: ").append(modelo.getValueAt(foundIdx, 2)).append("\n");
        info.append("Año: ").append(modelo.getValueAt(foundIdx, 3)).append("\n");
        info.append("Capacidad: ").append(modelo.getValueAt(foundIdx, 4)).append(" galones\n");
        info.append("Estado: ").append(modelo.getValueAt(foundIdx, 5)).append("\n");

        JOptionPane.showMessageDialog(this, info.toString(), "Información del Tanquero",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ======================== DIÁLOGO MODIFICAR VEHÍCULO ========================
    private void abrirDialogoModificar() {
        String placa = JOptionPane.showInputDialog(this, "Ingrese la Placa del vehículo:", "Modificar Vehículo",
                JOptionPane.QUESTION_MESSAGE);
        if (placa == null || placa.trim().isEmpty())
            return;
        placa = placa.trim().toUpperCase();

        int foundIdx = -1;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).equals(placa)) {
                foundIdx = i;
                break;
            }
        }

        if (foundIdx < 0) {
            JOptionPane.showMessageDialog(this, "Vehículo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final int rowIndex = foundIdx;

        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Modificar Tanquero",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField textoMarca = new JTextField(modelo.getValueAt(rowIndex, 1).toString(), 15);
        JTextField textoModelo = new JTextField(modelo.getValueAt(rowIndex, 2).toString(), 15);
        JTextField textoCapacidad = new JTextField(modelo.getValueAt(rowIndex, 4).toString(), 15);
        JComboBox<String> comboEstado = new JComboBox<>(new String[] { "Activo", "Inactivo" });
        comboEstado.setSelectedItem(modelo.getValueAt(rowIndex, 5));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Placa: " + placa), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1;
        panel.add(textoMarca, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1;
        panel.add(textoModelo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Capacidad (Gal):"), gbc);
        gbc.gridx = 1;
        panel.add(textoCapacidad, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        panel.add(comboEstado, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Botón botónActualizar = new Botón("Actualizar", new Color(40, 167, 69));
        Botón botónCancelar = new Botón("Cancelar", new Color(220, 53, 69));

        botónActualizar.setPreferredSize(new Dimension(100, 35));
        botónCancelar.setPreferredSize(new Dimension(100, 35));

        panelBotones.add(botónActualizar);
        panelBotones.add(botónCancelar);

        dialogo.getContentPane().add(panel, BorderLayout.CENTER);
        dialogo.getContentPane().add(panelBotones, BorderLayout.SOUTH);

        botónCancelar.addActionListener(ev -> dialogo.dispose());

        botónActualizar.addActionListener(ev -> {
            modelo.setValueAt(textoMarca.getText().trim(), rowIndex, 1);
            modelo.setValueAt(textoModelo.getText().trim(), rowIndex, 2);
            modelo.setValueAt(textoCapacidad.getText().trim(), rowIndex, 4);
            modelo.setValueAt(comboEstado.getSelectedItem(), rowIndex, 5);
            JOptionPane.showMessageDialog(dialogo, "Tanquero actualizado exitosamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });

        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    // ======================== ELIMINAR VEHÍCULO ========================
    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un tanquero para eliminar.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField contraseña = new JPasswordField();
        int opt = JOptionPane.showConfirmDialog(this, contraseña, "Contraseña admin", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (opt == JOptionPane.OK_OPTION && ADMIN_PASSWORD.equals(new String(contraseña.getPassword()))) {
            modelo.removeRow(fila);
            JOptionPane.showMessageDialog(this, "Tanquero eliminado correctamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (opt == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ======================== DIÁLOGO MANTENIMIENTO (rt12)
    // ========================
    private void abrirDialogoMantenimiento() {
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Registrar Mantenimiento ",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField textoPlaca = new JTextField(15);
        JComboBox<String> comboTipo = new JComboBox<>(new String[] { "Preventivo", "Correctivo" });
        JTextField textoFecha = new JTextField(15);
        JTextField textoProveedor = new JTextField(15);
        JTextField textoCosto = new JTextField(15);
        JTextField textoKilometraje = new JTextField(15);
        JTextArea textoDescripcion = new JTextArea(4, 15);
        JScrollPane scrollDesc = new JScrollPane(textoDescripcion);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Placa del Vehículo: "), gbc);
        gbc.gridx = 1;
        panel.add(textoPlaca, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Tipo: "), gbc);
        gbc.gridx = 1;
        panel.add(comboTipo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Fecha (dd/mm/aaaa): "), gbc);
        gbc.gridx = 1;
        panel.add(textoFecha, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Código Proveedor: "), gbc);
        gbc.gridx = 1;
        panel.add(textoProveedor, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Costo Estimado: "), gbc);
        gbc.gridx = 1;
        panel.add(textoCosto, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Kilometraje: "), gbc);
        gbc.gridx = 1;
        panel.add(textoKilometraje, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Descripción "), gbc);
        gbc.gridx = 1;
        panel.add(scrollDesc, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Botón botónGuardar = new Botón("Guardar", new Color(40, 167, 69));
        Botón botónCancelar = new Botón("Cancelar", new Color(220, 53, 69));

        botónGuardar.setPreferredSize(new Dimension(100, 35));
        botónCancelar.setPreferredSize(new Dimension(100, 35));

        panelBotones.add(botónGuardar);
        panelBotones.add(botónCancelar);

        dialogo.getContentPane().add(panel, BorderLayout.CENTER);
        dialogo.getContentPane().add(panelBotones, BorderLayout.SOUTH);

        botónCancelar.addActionListener(ev -> dialogo.dispose());

        botónGuardar.addActionListener(ev -> {
            JOptionPane.showMessageDialog(dialogo, "Mantenimiento registrado exitosamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });

        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    // ======================== DIÁLOGO NOVEDADES (rt11) ========================
    private void abrirDialogoNovedades() {
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Reportar Novedad ",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField textoPlaca = new JTextField(15);
        JComboBox<String> comboTipo = new JComboBox<>(
                new String[] { "Mecánica", "Accidente", "Robo", "Falla eléctrica", "Otra" });
        JTextField textoFecha = new JTextField(15);
        JTextField textoHora = new JTextField(15);
        JTextField textoUbicacion = new JTextField(15);
        JComboBox<String> comboGravedad = new JComboBox<>(new String[] { "Baja", "Media", "Alta" });
        JTextArea textoDescripcion = new JTextArea(4, 15);
        JScrollPane scrollDesc = new JScrollPane(textoDescripcion);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Placa: "), gbc);
        gbc.gridx = 1;
        panel.add(textoPlaca, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Tipo: "), gbc);
        gbc.gridx = 1;
        panel.add(comboTipo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Fecha (dd/mm/aaaa): "), gbc);
        gbc.gridx = 1;
        panel.add(textoFecha, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Hora (hh:mm): "), gbc);
        gbc.gridx = 1;
        panel.add(textoHora, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Ubicación: "), gbc);
        gbc.gridx = 1;
        panel.add(textoUbicacion, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Gravedad: "), gbc);
        gbc.gridx = 1;
        panel.add(comboGravedad, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Descripción: "), gbc);
        gbc.gridx = 1;
        panel.add(scrollDesc, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Botón botónReportar = new Botón("Reportar", new Color(40, 167, 69));
        Botón botónCancelar = new Botón("Cancelar", new Color(220, 53, 69));

        botónReportar.setPreferredSize(new Dimension(100, 35));
        botónCancelar.setPreferredSize(new Dimension(100, 35));

        panelBotones.add(botónReportar);
        panelBotones.add(botónCancelar);

        dialogo.getContentPane().add(panel, BorderLayout.CENTER);
        dialogo.getContentPane().add(panelBotones, BorderLayout.SOUTH);

        botónCancelar.addActionListener(ev -> dialogo.dispose());

        botónReportar.addActionListener(ev -> {
            JOptionPane.showMessageDialog(dialogo,
                    "Novedad reportada exitosamente. Se notificará al área de mantenimiento.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });

        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    // ======================== DIÁLOGO DISPONIBILIDAD (rt4, rt20)
    // ========================
    private void abrirDialogoDisponibilidad() {
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Verificar Disponibilidad",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setSize(700, 400);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel superior con búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Placa:"));
        JTextField textoPlaca = new JTextField(15);
        panelBusqueda.add(textoPlaca);
        Botón botónBuscar = new Botón("Buscar", new Color(70, 128, 139));
        botónBuscar.setPreferredSize(new Dimension(100, 30));
        panelBusqueda.add(botónBuscar);

        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de disponibilidad
        String[] columnas = { "Placa", "Estado", "Última Actualización", "Próxima Disponibilidad", "Chofer Asignado" };
        DefaultTableModel modeloDisp = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Datos de ejemplo
        modeloDisp.addRow(new Object[] { "ABC-123", "Disponible", "20/12/2024 14:30", "-", "-" });
        modeloDisp.addRow(new Object[] { "XYZ-4567", "En mantenimiento", "20/12/2024 08:00", "25/12/2024 16:00", "-" });
        modeloDisp.addRow(new Object[] { "DEF-7890", "En ruta", "20/12/2024 06:00", "20/12/2024 18:00", "Juan Pérez" });

        JTable tablaDisp = new JTable(modeloDisp);
        tablaDisp.setBackground(new Color(31, 41, 55));
        tablaDisp.setForeground(Color.WHITE);
        tablaDisp.setGridColor(new Color(55, 65, 81));
        tablaDisp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaDisp.setRowHeight(25);

        JTableHeader headerDisp = tablaDisp.getTableHeader();
        headerDisp.setBackground(new Color(243, 244, 246));
        headerDisp.setForeground(new Color(31, 41, 55));
        headerDisp.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tablaDisp);
        panel.add(scroll, BorderLayout.CENTER);

        // Panel de estadísticas
        JPanel panelEstadisticas = new JPanel(new GridLayout(1, 4, 10, 0));
        panelEstadisticas.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblTotal = new JLabel("Total: 24");
        JLabel lblDisponibles = new JLabel("Disponibles: 18");
        JLabel lblMantenimiento = new JLabel("Mantenimiento: 4");
        JLabel lblFueraServicio = new JLabel("Fuera de Servicio: 2");

        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDisponibles.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDisponibles.setForeground(new Color(40, 167, 69));
        lblMantenimiento.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMantenimiento.setForeground(new Color(234, 177, 0));
        lblFueraServicio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFueraServicio.setForeground(new Color(239, 68, 68));

        panelEstadisticas.add(lblTotal);
        panelEstadisticas.add(lblDisponibles);
        panelEstadisticas.add(lblMantenimiento);
        panelEstadisticas.add(lblFueraServicio);

        panel.add(panelEstadisticas, BorderLayout.SOUTH);

        dialogo.add(panel);

        botónBuscar.addActionListener(ev -> {
            String placa = textoPlaca.getText().trim().toUpperCase();
            if (placa.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Ingrese una placa para buscar.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean encontrado = false;
            for (int i = 0; i < modeloDisp.getRowCount(); i++) {
                if (modeloDisp.getValueAt(i, 0).equals(placa)) {
                    tablaDisp.setRowSelectionInterval(i, i);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                JOptionPane.showMessageDialog(dialogo, "Vehículo no encontrado.", "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    // ======================== DIÁLOGO ASIGNAR VEHÍCULO A CHOFER (rt3)
    // ========================
    private void abrirDialogoAsignarChofer() {
        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Asignar Vehículo a Chofer (rt3)",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField textoPlaca = new JTextField(15);
        JTextField textoCedula = new JTextField(15);
        JTextField textoFecha = new JTextField(15);
        JTextField textoHora = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Placa del Vehículo (ABC-1234): *"), gbc);
        gbc.gridx = 1;
        panel.add(textoPlaca, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Cédula del Chofer (10 dígitos): *"), gbc);
        gbc.gridx = 1;
        panel.add(textoCedula, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Fecha de Asignación (dd/mm/aaaa): *"), gbc);
        gbc.gridx = 1;
        panel.add(textoFecha, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Hora de Inicio (hh:mm): *"), gbc);
        gbc.gridx = 1;
        panel.add(textoHora, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Botón botónAsignar = new Botón("Asignar", new Color(40, 167, 69));
        Botón botónCancelar = new Botón("Cancelar", new Color(220, 53, 69));

        botónAsignar.setPreferredSize(new Dimension(100, 35));
        botónCancelar.setPreferredSize(new Dimension(100, 35));

        panelBotones.add(botónAsignar);
        panelBotones.add(botónCancelar);

        dialogo.getContentPane().add(panel, BorderLayout.CENTER);
        dialogo.getContentPane().add(panelBotones, BorderLayout.SOUTH);

        botónCancelar.addActionListener(ev -> dialogo.dispose());

        botónAsignar.addActionListener(ev -> {
            String placa = textoPlaca.getText().trim().toUpperCase();
            String cedula = textoCedula.getText().trim();
            String fecha = textoFecha.getText().trim();
            String hora = textoHora.getText().trim();

            // Validaciones
            if (placa.isEmpty() || cedula.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Todos los campos son obligatorios.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de placa
            if (!placa.matches("^[A-Z]{3}-\\d{3,4}$")) {
                JOptionPane.showMessageDialog(dialogo, "Formato de placa inválido. Use: ABC-1234", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar que el vehículo exista
            boolean vehiculoExiste = false;
            for (int i = 0; i < modelo.getRowCount(); i++) {
                if (modelo.getValueAt(i, 0).equals(placa)) {
                    vehiculoExiste = true;
                    break;
                }
            }

            if (!vehiculoExiste) {
                JOptionPane.showMessageDialog(dialogo, "El vehículo no está registrado en el sistema.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar cédula (10 dígitos)
            if (!cedula.matches("^\\d{10}$")) {
                JOptionPane.showMessageDialog(dialogo, "La cédula debe tener exactamente 10 dígitos numéricos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de fecha
            if (!fecha.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
                JOptionPane.showMessageDialog(dialogo, "Formato de fecha inválido. Use: dd/mm/aaaa", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de hora
            if (!hora.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
                JOptionPane.showMessageDialog(dialogo, "Formato de hora inválido. Use: hh:mm (00:00 - 23:59)", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Aquí iría la validación de que el vehículo no esté asignado a otro chofer
            // Por ahora solo mostramos mensaje de éxito

            JOptionPane.showMessageDialog(dialogo,
                    "Vehículo " + placa + " asignado exitosamente al chofer con cédula " + cedula +
                            "\nFecha: " + fecha + " - Hora: " + hora,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });

        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    // ======================== DIÁLOGO HISTORIAL DE MANTENIMIENTOS (rt13)
    // ========================
    private void abrirDialogoHistorialMantenimientos() {
        String placa = JOptionPane.showInputDialog(this, "Ingrese la Placa del vehículo:",
                "Historial de Mantenimientos (rt13)",
                JOptionPane.QUESTION_MESSAGE);
        if (placa == null || placa.trim().isEmpty())
            return;
        placa = placa.trim().toUpperCase();

        // Verificar que el vehículo exista
        boolean vehiculoExiste = false;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).equals(placa)) {
                vehiculoExiste = true;
                break;
            }
        }

        if (!vehiculoExiste) {
            JOptionPane.showMessageDialog(this, "Vehículo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Historial de Mantenimientos - " + placa,
                Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setSize(900, 500);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(18, 18, 18));

        // Título
        JLabel titulo = new JLabel("Historial de Mantenimientos - Vehículo: " + placa);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo, BorderLayout.NORTH);

        // Tabla de historial
        String[] columnas = { "Fecha", "Tipo", "Descripción", "Proveedor", "Costo", "Estado", "Observaciones" };
        DefaultTableModel modeloHistorial = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Datos de ejemplo (ordenados por fecha descendente)
        modeloHistorial.addRow(new Object[] { "15/12/2024", "P - Preventivo", "Cambio de aceite y filtros", "PROV-001",
                "$150.00", "Completado", "Sin novedades" });
        modeloHistorial.addRow(new Object[] { "28/11/2024", "C - Correctivo", "Reparación de frenos", "PROV-002",
                "$380.00", "Completado", "Cambio de pastillas" });
        modeloHistorial.addRow(new Object[] { "10/10/2024", "P - Preventivo", "Revisión general", "PROV-001", "$200.00",
                "Completado", "Todo en orden" });
        modeloHistorial.addRow(new Object[] { "05/09/2024", "C - Correctivo", "Cambio de neumáticos", "PROV-003",
                "$560.00", "Completado", "4 neumáticos nuevos" });
        modeloHistorial.addRow(new Object[] { "25/01/2025", "P - Preventivo", "Mantenimiento programado", "PROV-001",
                "$180.00", "Pendiente", "Próximo mes" });

        JTable tablaHistorial = new JTable(modeloHistorial);
        tablaHistorial.setBackground(new Color(31, 41, 55));
        tablaHistorial.setForeground(Color.WHITE);
        tablaHistorial.setGridColor(new Color(55, 65, 81));
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaHistorial.setRowHeight(30);

        JTableHeader header = tablaHistorial.getTableHeader();
        header.setBackground(new Color(243, 244, 246));
        header.setForeground(new Color(31, 41, 55));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(new Color(31, 41, 55));
        panel.add(scroll, BorderLayout.CENTER);

        // Panel de estadísticas
        JPanel panelStats = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelStats.setOpaque(false);

        JLabel lblTotal = new JLabel("Total Mantenimientos: 5");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotal.setForeground(Color.WHITE);

        JLabel lblCosto = new JLabel("Costo Total: $1,470.00");
        lblCosto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCosto.setForeground(new Color(234, 177, 0));

        JLabel lblPendientes = new JLabel("Pendientes: 1");
        lblPendientes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPendientes.setForeground(new Color(239, 68, 68));

        panelStats.add(lblTotal);
        panelStats.add(lblCosto);
        panelStats.add(lblPendientes);

        panel.add(panelStats, BorderLayout.SOUTH);

        dialogo.add(panel);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

}
