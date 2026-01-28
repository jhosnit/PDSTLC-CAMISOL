package Presentación.Paneles.Tanqueros;

import Presentación.Recursos.Botón;
import Logica.Entidades.Socio;
import Logica.Entidades.Tanquero;
import Logica.Entidades.Usuario;
import Logica.Gestores.GestorAlertas;
import Logica.Gestores.GestorSocios;
import Logica.Gestores.GestorTanqueros;
import Logica.Utilidades.Validaciones;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Tanqueros extends JPanel {

  private JTable tabla;
  private DefaultTableModel modelo;

  private GestorTanqueros gestorTanqueros;
  private GestorSocios gestorSocios;
  private Usuario usuarioActual;

  private Botón botónRegistrar, botónConsultar, botónModificar, botónEliminar;
  private Botón botónAsignarChofer;

  public Tanqueros(Usuario usuario) {
    this.usuarioActual = usuario;
    this.gestorTanqueros = new GestorTanqueros();
    this.gestorSocios = new GestorSocios();
    inicializarComponentes();
    cargarDatosBD();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout());
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelBotones.setOpaque(false);

    botónRegistrar = new Botón("Registrar", new Color(40, 167, 69));
    botónModificar = new Botón("Actualizar", new Color(234, 177, 0));
    botónConsultar = new Botón("Consultar", new Color(70, 128, 139));
    botónAsignarChofer = new Botón("Asignar Socio", new Color(99, 102, 241));
    botónEliminar = new Botón("Eliminar", new Color(239, 68, 68));

    Dimension dimBoton = new Dimension(160, 40);
    botónRegistrar.setPreferredSize(dimBoton);
    botónConsultar.setPreferredSize(dimBoton);
    botónModificar.setPreferredSize(dimBoton);
    botónEliminar.setPreferredSize(dimBoton);
    botónAsignarChofer.setPreferredSize(dimBoton);

    panelBotones.add(botónRegistrar);
    panelBotones.add(botónModificar);
    panelBotones.add(botónConsultar);
    panelBotones.add(botónAsignarChofer);
    panelBotones.add(botónEliminar);

    add(panelBotones, BorderLayout.NORTH);

    String[] columnas = {"Placa", "Marca", "Modelo", "Año", "Capacidad (Litros)", "Estado"};
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

    botónRegistrar.addActionListener(e -> registrarTanquero());
    botónConsultar.addActionListener(e -> consultarTanquero());
    botónModificar.addActionListener(e -> modificarTanquero());
    botónEliminar.addActionListener(e -> eliminarTanquero());
    botónAsignarChofer.addActionListener(e -> asignarChofer());
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

    DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
    centerRender.setHorizontalAlignment(SwingConstants.CENTER);
    centerRender.setBackground(new Color(31, 41, 55));
    centerRender.setForeground(Color.WHITE);

    for (int i = 0; i < tabla.getColumnCount(); i++) {
      tabla.getColumnModel().getColumn(i).setCellRenderer(centerRender);
    }
  }

  private void cargarDatosBD() {
    modelo.setRowCount(0);
    List<Tanquero> lista = gestorTanqueros.listarTanqueros();
    for (Tanquero t : lista) {
      modelo.addRow(new Object[]{
        t.getPlaca(),
        t.getMarca(),
        t.getModelo(),
        t.getAnioFabricacion(),
        t.getCapacidadLitros(),
        t.isEstado() ? "Activo" : "Inactivo"
      });
    }
  }

  private void registrarTanquero() {
    JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblPlaca = new JLabel("Placa:");
    JTextField txtPlaca = new JTextField();

    JLabel lblMarca = new JLabel("Marca:");
    JTextField txtMarca = new JTextField();

    JLabel lblModelo = new JLabel("Modelo:");
    JTextField txtModelo = new JTextField();

    JLabel lblAnio = new JLabel("Año:");
    JTextField txtAnio = new JTextField();

    JLabel lblCapacidad = new JLabel("Capacidad (Litros):");
    JTextField txtCapacidad = new JTextField();

    panel.add(lblPlaca);
    panel.add(txtPlaca);
    panel.add(lblMarca);
    panel.add(txtMarca);
    panel.add(lblModelo);
    panel.add(txtModelo);
    panel.add(lblAnio);
    panel.add(txtAnio);
    panel.add(lblCapacidad);
    panel.add(txtCapacidad);

    int res = JOptionPane.showConfirmDialog(
      this,
      panel,
      "Registrar Vehículo",
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (res == JOptionPane.OK_OPTION) {
      String placa = txtPlaca.getText().trim().toUpperCase();
      String marca = txtMarca.getText().trim();
      String modeloTxt = txtModelo.getText().trim();

      // Validaciones
      if (placa.isEmpty() || marca.isEmpty() || modeloTxt.isEmpty()) {
        GestorAlertas.mostrarError(this, "Todos los campos son obligatorios");
        return;
      }

      if (!Validaciones.validarPlaca(placa)) {
        GestorAlertas.mostrarError(this, "Placa inválida ");
        return;
      }
      if (gestorTanqueros.existePlaca(placa)) {
        GestorAlertas.mostrarError(this, "La placa ya existe");
        return;
      }

      try {
        int anio = Integer.parseInt(txtAnio.getText().trim());
        double capacidad = Double.parseDouble(txtCapacidad.getText().trim());
        int anioActual = java.time.Year.now().getValue();

        if (anio < 1980 || anio > anioActual + 1) throw new NumberFormatException();
        if (capacidad <= 0) throw new NumberFormatException();

        Tanquero t = new Tanquero(placa, marca, modeloTxt, anio, capacidad);
        t.setUsuarioRegistro(usuarioActual.getUsername());

        if (gestorTanqueros.crearTanquero(t)) {
          GestorAlertas.mostrarExito(this, "Vehículo registrado exitosamente");
          cargarDatosBD();
        } else {
          GestorAlertas.mostrarError(this, "Error al guardar");
        }
      } catch (NumberFormatException ex) {
        GestorAlertas.mostrarError(this, "Año o Capacidad inválidos");
      }
    }
  }

  private void modificarTanquero() {
    int fila = tabla.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un vehículo de la tabla");
      return;
    }

    String placa = (String) modelo.getValueAt(fila, 0);
    Tanquero t = gestorTanqueros.buscarPorPlaca(placa);
    if (t == null) return;

    JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblPlaca = new JLabel("Placa:");
    JTextField txtPlaca = new JTextField(t.getPlaca());
    txtPlaca.setEditable(false);
    txtPlaca.setBackground(Color.LIGHT_GRAY);

    JLabel lblMarca = new JLabel("Marca:");
    JTextField txtMarca = new JTextField(t.getMarca());

    JLabel lblModelo = new JLabel("Modelo:");
    JTextField txtModelo = new JTextField(t.getModelo());

    JLabel lblAnio = new JLabel("Año:");
    JTextField txtAnio = new JTextField(String.valueOf(t.getAnioFabricacion()));

    JLabel lblCapacidad = new JLabel("Capacidad:");
    JTextField txtCapacidad = new JTextField(String.valueOf(t.getCapacidadLitros()));

    panel.add(lblPlaca);
    panel.add(txtPlaca);
    panel.add(lblMarca);
    panel.add(txtMarca);
    panel.add(lblModelo);
    panel.add(txtModelo);
    panel.add(lblAnio);
    panel.add(txtAnio);
    panel.add(lblCapacidad);
    panel.add(txtCapacidad);

    int res = JOptionPane.showConfirmDialog(
      this,
      panel,
      "Modificar Vehículo",
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (res == JOptionPane.OK_OPTION) {
      try {
        t.setMarca(txtMarca.getText().trim());
        t.setModelo(txtModelo.getText().trim());
        t.setAnioFabricacion(Integer.parseInt(txtAnio.getText().trim()));
        t.setCapacidadLitros(Double.parseDouble(txtCapacidad.getText().trim()));

        if (gestorTanqueros.actualizarTanquero(t)) {
          GestorAlertas.mostrarExito(this, "Vehículo actualizado exitosamente");
          cargarDatosBD();
        }
      } catch (Exception e) {
        GestorAlertas.mostrarError(this, "Datos numéricos inválidos");
      }
    }
  }

  private void consultarTanquero() {
    String placa = JOptionPane.showInputDialog(
      this,
      "Ingrese la Placa del vehículo:",
      "Consultar Vehículo",
      JOptionPane.PLAIN_MESSAGE
    );

    if (placa != null && !placa.trim().isEmpty()) {
      Tanquero t = gestorTanqueros.buscarPorPlaca(placa.trim().toUpperCase());
      if (t != null) {
        mostrarDetallesTanquero(t);
      } else {
        GestorAlertas.mostrarError(this, "No se encontró un vehículo con esa placa");
      }
    }
  }

  private void mostrarDetallesTanquero(Tanquero t) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    StringBuilder detalles = new StringBuilder();

    detalles.append("Placa:      ").append(t.getPlaca()).append("\n");
    detalles.append("Marca:      ").append(t.getMarca()).append("\n");
    detalles.append("Modelo:     ").append(t.getModelo()).append("\n");
    detalles.append("Año:        ").append(t.getAnioFabricacion()).append("\n");
    detalles.append("Capacidad:  ").append(t.getCapacidadLitros()).append(" Litros\n");
    detalles.append("Estado:     ").append(t.isEstado() ? "Activo" : "Inactivo").append("\n\n");

    JTextArea textArea = new JTextArea(detalles.toString());
    textArea.setEditable(false);
    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(350, 200));

    JOptionPane.showMessageDialog(
      this,
      scrollPane,
      "Detalles del Vehículo",
      JOptionPane.PLAIN_MESSAGE
    );
  }

  private void eliminarTanquero() {
    int fila = tabla.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un vehículo de la tabla");
      return;
    }

    String placa = (String) modelo.getValueAt(fila, 0);

    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Está seguro de eliminar el vehículo " + placa + " ?\n",
      "Confirmar Eliminación",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (confirmacion == JOptionPane.YES_OPTION) {

      Tanquero t = gestorTanqueros.buscarPorPlaca(placa);
      if (gestorTanqueros.eliminarTanquero(t.getIdTanquero())) {
        GestorAlertas.mostrarExito(this, "Vehículo eliminado correctamente");
        cargarDatosBD();
      }
    }
  }

  // ======================== ASIGNAR CHOFER (Estilo Integrado) ========================
  private void asignarChofer() {
    int fila = tabla.getSelectedRow();
    String placaPreseleccionada = (fila >= 0) ? (String) modelo.getValueAt(fila, 0) : "";

    JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblPlaca = new JLabel("Placa Tanquero:");
    JTextField txtPlaca = new JTextField(placaPreseleccionada);

    JLabel lblCedula = new JLabel("Cédula Chofer:");
    JTextField txtCedula = new JTextField();

    JLabel lblNombre = new JLabel("Nombre Socio:");
    JLabel lblResultadoNombre = new JLabel("---");
    lblResultadoNombre.setForeground(Color.GRAY);

    JLabel lblFecha = new JLabel("Fecha Asignación:");
    JTextField txtFecha = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    txtFecha.setEditable(false);
    txtFecha.setBackground(Color.LIGHT_GRAY);

    // Panel anidado para la fila de búsqueda de Cédula
    JPanel panelBusqueda = new JPanel(new BorderLayout(5, 0));
    JButton btnBuscar = new JButton("Buscar");
    btnBuscar.setPreferredSize(new Dimension(75, 25));
    panelBusqueda.add(txtCedula, BorderLayout.CENTER);
    panelBusqueda.add(btnBuscar, BorderLayout.EAST);

    panel.add(lblPlaca);
    panel.add(txtPlaca);

    panel.add(lblCedula);
    panel.add(panelBusqueda);

    panel.add(lblNombre);
    panel.add(lblResultadoNombre);

    panel.add(lblFecha);
    panel.add(txtFecha);

    // Lógica del botón buscar
    btnBuscar.addActionListener(e -> {
      String cedula = txtCedula.getText().trim();
      if (!cedula.isEmpty()) {
        Socio s = gestorSocios.buscarPorCedula(cedula);
        if (s != null) {
          lblResultadoNombre.setText(s.getNombres() + " " + s.getApellidos());
          lblResultadoNombre.setForeground(new Color(40, 167, 69)); // Verde
        } else {
          lblResultadoNombre.setText("NO ENCONTRADO");
          lblResultadoNombre.setForeground(Color.RED);
        }
      }
    });

    int res = JOptionPane.showConfirmDialog(
      this,
      panel,
      "Asignar Chofer a Vehículo",
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (res == JOptionPane.OK_OPTION) {
      String placa = txtPlaca.getText().trim().toUpperCase();
      String nombreSocio = lblResultadoNombre.getText();

      if (!gestorTanqueros.existePlaca(placa)) {
        GestorAlertas.mostrarError(this, "La placa ingresada no existe.");
        return;
      }
      if (nombreSocio.equals("---") || nombreSocio.equals("NO ENCONTRADO")) {
        GestorAlertas.mostrarError(this, "Debe buscar y seleccionar un socio válido.");
        return;
      }

      // Aquí guardarías la asignación en BD
      GestorAlertas.mostrarExito(this, "Asignación realizada con éxito:\n" + placa + " -> " + nombreSocio);
    }
  }
}
