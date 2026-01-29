package Presentación.Paneles.Transporte;

import Logica.Entidades.*;
import Logica.Gestores.*;
import Presentación.Recursos.Botón;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransportePanel extends JPanel {

  private JTable tablaViajes;
  private DefaultTableModel modeloTabla;
  private List<Transporte> listaViajes;

  private GestorTransporte gestorTransporte;
  private GestorTanqueros gestorTanqueros;
  private GestorSocios gestorSocios;
  private Usuario usuarioActual;

  private Botón btnNuevoViaje, btnActualizar, btnConsultar, btnCambiarEstado, btnEliminar;

  public TransportePanel(Usuario usuario) {
    this.usuarioActual = usuario;
    this.gestorTransporte = new GestorTransporte();
    this.gestorTanqueros = new GestorTanqueros();
    this.gestorSocios = new GestorSocios();

    inicializarComponentes();
    cargarViajes();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout());
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelBotones.setOpaque(false);

    btnNuevoViaje = new Botón("Nuevo Destino", new Color(40, 167, 69));
    btnCambiarEstado = new Botón("Cambiar Estado", new Color(234, 177, 0));
    btnConsultar = new Botón("Consultar", new Color(70, 128, 139));
    btnEliminar = new Botón("Eliminar", new Color(239, 68, 68));
    btnActualizar = new Botón("Actualizar Tabla", new Color(147, 51, 234));

    Dimension dim = new Dimension(150, 40);
    btnNuevoViaje.setPreferredSize(dim);
    btnCambiarEstado.setPreferredSize(dim);
    btnConsultar.setPreferredSize(dim);
    btnEliminar.setPreferredSize(dim);
    btnActualizar.setPreferredSize(dim);

    panelBotones.add(btnNuevoViaje);
    panelBotones.add(btnCambiarEstado);
    panelBotones.add(btnConsultar);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnActualizar);
    add(panelBotones, BorderLayout.NORTH);

    String[] columnas = {
      "Fecha", "Tanquero", "Chofer", "Cliente", "Destino", "Litros", "Ocupación", "Flete ($)", "Estado"
    };

    modeloTabla = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tablaViajes = new JTable(modeloTabla);
    configurarEstiloTabla();

    JScrollPane scroll = new JScrollPane(tablaViajes);
    scroll.getViewport().setBackground(new Color(31, 41, 55));
    scroll.setBorder(new LineBorder(new Color(55, 65, 81), 1));
    add(scroll, BorderLayout.CENTER);

    btnNuevoViaje.addActionListener(e -> abrirDialogoNuevoViaje());
    btnCambiarEstado.addActionListener(e -> cambiarEstadoViaje());
    btnConsultar.addActionListener(e -> consultarViaje());
    btnEliminar.addActionListener(e -> eliminarViaje());
    btnActualizar.addActionListener(e -> cargarViajes());
  }

  private void configurarEstiloTabla() {
    tablaViajes.setBackground(new Color(31, 41, 55));
    tablaViajes.setForeground(Color.WHITE);
    tablaViajes.setGridColor(new Color(55, 65, 81));
    tablaViajes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tablaViajes.setRowHeight(30);
    tablaViajes.setSelectionBackground(new Color(75, 85, 99));
    tablaViajes.setSelectionForeground(Color.WHITE);

    JTableHeader header = tablaViajes.getTableHeader();
    header.setBackground(new Color(243, 244, 246));
    header.setForeground(new Color(31, 41, 55));
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setPreferredSize(new Dimension(0, 35));

    DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
    centerRender.setHorizontalAlignment(SwingConstants.CENTER);
    centerRender.setBackground(new Color(31, 41, 55));
    centerRender.setForeground(Color.WHITE);

    for (int i = 0; i < tablaViajes.getColumnCount(); i++) {
      tablaViajes.getColumnModel().getColumn(i).setCellRenderer(centerRender);
    }

    // Ajustar anchos
    tablaViajes.getColumnModel().getColumn(0).setPreferredWidth(90); // ID
    tablaViajes.getColumnModel().getColumn(1).setPreferredWidth(100); // Fecha
    tablaViajes.getColumnModel().getColumn(2).setPreferredWidth(100); // Tanquero
  }

  private void cargarViajes() {
    modeloTabla.setRowCount(0);
    listaViajes = gestorTransporte.listarViajes();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    for (Transporte t : listaViajes) {
      String cliente = (t.getCliente() != null) ? t.getCliente().getRazonSocial() : "N/A";

      modeloTabla.addRow(new Object[]{
        t.getFechaAsignacion().format(fmt),
        t.getTanquero().getPlaca(),
        t.getSocio().getNombres() + " " + t.getSocio().getApellidos(),
        cliente,
        t.getRutaDestino(),
        String.format("%.2f", t.getLitrosTransportados()),
        String.format("%.1f%%", t.getPorcentajeOcupacion()),
        String.format("$ %.2f", t.getValorFlete()),
        t.getEstadoViaje()
      });
    }
  }

  private void abrirDialogoNuevoViaje() {
    GestorClientes gestorClientes = new GestorClientes();
    Cliente clienteDefecto = gestorClientes.obtenerClientePorDefecto();

    if (clienteDefecto == null) {
      GestorAlertas.mostrarError(this, "No se encontró el cliente ECUAJUGOS");
      return;
    }

    JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblTanquero = new JLabel("Tanquero:");
    JComboBox<String> comboTanqueros = new JComboBox<>();
    List<Tanquero> listaTanqueros = gestorTanqueros.listarTanqueros();
    for (Tanquero t : listaTanqueros) {
      if (t.isEstado()) {
        comboTanqueros.addItem(t.getPlaca() + " - " + t.getCapacidadLitros() + "L");
      }
    }


    JLabel lblChofer = new JLabel("Socio:");
    JComboBox<String> comboSocios = new JComboBox<>();
    List<Socio> listaSocios = gestorSocios.listarSocios();
    for (Socio s : listaSocios) {
      if (s.isEstado()) {
        comboSocios.addItem(s.getCedula() + " - " + s.getNombres() + " " + s.getApellidos());
      }
    }

    JLabel lblCliente = new JLabel("Cliente:");
    JTextField txtCliente = new JTextField(clienteDefecto.getRazonSocial());
    txtCliente.setEditable(false);
    txtCliente.setBackground(Color.LIGHT_GRAY);

    JLabel lblOrigen = new JLabel("Ruta Origen:");
    JTextField txtOrigen = new JTextField();

    JLabel lblDestino = new JLabel("Ruta Destino:");
    JTextField txtDestino = new JTextField();

    JLabel lblKms = new JLabel("Distancia (Km):");
    JTextField txtKms = new JTextField();

    JLabel lblLitros = new JLabel("Carga (Litros):");
    JTextField txtLitros = new JTextField();

    JLabel lblObservaciones = new JLabel("Observaciones:");
    JTextField txtObservaciones = new JTextField();


    panel.add(lblTanquero);
    panel.add(comboTanqueros);
    panel.add(lblChofer);
    panel.add(comboSocios);
    panel.add(lblCliente);
    panel.add(txtCliente);
    panel.add(lblOrigen);
    panel.add(txtOrigen);
    panel.add(lblDestino);
    panel.add(txtDestino);
    panel.add(lblKms);
    panel.add(txtKms);
    panel.add(lblLitros);
    panel.add(txtLitros);
    panel.add(lblObservaciones);
    panel.add(txtObservaciones);

    int res = JOptionPane.showConfirmDialog(
      this,
      panel,
      "Registrar Nuevo Viaje",
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (res == JOptionPane.OK_OPTION) {
      try {
        // Validar selecciones
        int idxTanquero = comboTanqueros.getSelectedIndex();
        int idxSocio = comboSocios.getSelectedIndex();

        if (idxTanquero < 0 || idxSocio < 0) {
          GestorAlertas.mostrarError(this, "Debe seleccionar un Tanquero y un Socio");
          return;
        }

        // Validar campos vacíos
        if (txtDestino.getText().trim().isEmpty() || txtKms.getText().trim().isEmpty() ||
          txtLitros.getText().trim().isEmpty()) {
          GestorAlertas.mostrarError(this, "Todos los campos son obligatorios");
          return;
        }

        // Obtener tanquero seleccionado
        Tanquero tanqueroSel = null;
        int countT = 0;
        for (Tanquero t : listaTanqueros) {
          if (t.isEstado()) {
            if (countT == idxTanquero) {
              tanqueroSel = t;
              break;
            }
            countT++;
          }
        }

        // Obtener socio seleccionado
        Socio socioSel = null;
        int countS = 0;
        for (Socio s : listaSocios) {
          if (s.isEstado()) {
            if (countS == idxSocio) {
              socioSel = s;
              break;
            }
            countS++;
          }
        }

        // Validaciones numéricas
        double kms = Double.parseDouble(txtKms.getText().trim());
        double litros = Double.parseDouble(txtLitros.getText().trim());

        if (kms < 0 || litros <= 0) {
          GestorAlertas.mostrarError(this, "Kilómetros y Litros deben ser valores positivos");
          return;
        }

        if (litros > tanqueroSel.getCapacidadLitros()) {
          GestorAlertas.mostrarError(this,
            String.format("La carga (%.2f L) excede la capacidad del tanquero (%.2f L)",
              litros, tanqueroSel.getCapacidadLitros()));
          return;
        }

        // Crear objeto Transporte
        Transporte t = new Transporte();
        t.setTanquero(tanqueroSel);
        t.setSocio(socioSel);
        t.setCliente(clienteDefecto);
        t.setFechaAsignacion(LocalDate.now());
        t.setHoraAsignacion(LocalTime.now());
        t.setRutaOrigen(txtOrigen.getText().trim());
        t.setRutaDestino(txtDestino.getText().trim());
        t.setKilometros(kms);
        t.setLitrosTransportados(litros);
        t.setEstadoViaje("En Curso");
        t.setUsuarioRegistro(usuarioActual.getUsername());
        t.setObservaciones(txtObservaciones.getText().trim());

        // Guardar en BD
        if (gestorTransporte.registrarViaje(t)) {
          GestorAlertas.mostrarExito(this, "Viaje registrado exitosamente");
          cargarViajes();
        } else {
          GestorAlertas.mostrarError(this, "Error al guardar");
        }

      } catch (NumberFormatException ex) {
        GestorAlertas.mostrarError(this, "Kilómetros y Litros deben ser números válidos");
      } catch (Exception ex) {
        GestorAlertas.mostrarError(this, "Error " + ex.getMessage());
        ex.printStackTrace();
      }
    }
  }

  private void cambiarEstadoViaje() {
    int fila = tablaViajes.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un viaje de la tabla");
      return;
    }

    // Recuperamos el objeto real desde la lista oculta
    Transporte viaje = listaViajes.get(fila);

    int idTransporte = viaje.getIdTransporte();
    String estadoActual = (String) modeloTabla.getValueAt(fila, 8);
    String tanquero = (String) modeloTabla.getValueAt(fila, 1);

    // Opciones de estado
    String[] opciones = {"En Curso", "Finalizado", "Cancelado"};

    String nuevoEstado = (String) JOptionPane.showInputDialog(
      this,
      "Seleccione el nuevo estado para el viaje:\n" +
        "Tanquero: " + tanquero + "\n" +
        "Estado actual: " + estadoActual,
      "Cambiar Estado del Viaje",
      JOptionPane.PLAIN_MESSAGE,
      null,
      opciones,
      estadoActual
    );

    if (nuevoEstado != null && !nuevoEstado.equals(estadoActual)) {
      int confirmacion = JOptionPane.showConfirmDialog(
        this,
        "¿Está seguro de cambiar el estado del viaje a \"" + nuevoEstado + "\"?",
        "Cambiar Estado",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.PLAIN_MESSAGE
      );

      if (confirmacion == JOptionPane.YES_OPTION) {
        if (gestorTransporte.cambiarEstadoViaje(idTransporte, nuevoEstado)) {
          GestorAlertas.mostrarExito(this, "Estado actualizado exitosamente");
          cargarViajes();
        } else {
          GestorAlertas.mostrarError(this, "Error al cambiar el estado");
        }
      }
    }
  }

  private void eliminarViaje() {
    int fila = tablaViajes.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un viaje de la tabla");
      return;
    }


    Transporte viaje = listaViajes.get(fila);

    int idTransporte = viaje.getIdTransporte();
    String tanquero = (String) modeloTabla.getValueAt(fila, 1);
    String destino = (String) modeloTabla.getValueAt(fila, 4);

    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Está seguro de eliminar el viaje?\n\n" +
        "Tanquero: " + tanquero + "\n" +
        "Destino: " + destino + "\n\n" +
        "Esta acción marcará el viaje como CANCELADO.",
      "Confirmar Eliminación",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.WARNING_MESSAGE
    );

    if (confirmacion == JOptionPane.YES_OPTION) {
      if (gestorTransporte.eliminarViaje(idTransporte)) {
        GestorAlertas.mostrarExito(this, "Viaje eliminado correctamente (Estado: Cancelado)");
        cargarViajes();
      } else {
        GestorAlertas.mostrarError(this, "Error al eliminar el viaje");
      }
    }
  }

  private void consultarViaje() {
    int fila = tablaViajes.getSelectedRow();

    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un viaje de la tabla");
      return;
    }

    Transporte viaje = listaViajes.get(fila);


    if (viaje == null) {
      GestorAlertas.mostrarError(this, "Error al cargar los datos del viaje");
      return;
    }

    mostrarDetallesViaje(viaje);
  }

  private void mostrarDetallesViaje(Transporte viaje) {
    DateTimeFormatter fmtFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH:mm:ss");

    StringBuilder detalles = new StringBuilder();
    detalles.append("ID Viaje:       ").append(viaje.getIdTransporte()).append("\n");
    detalles.append("Fecha:          ").append(viaje.getFechaAsignacion().format(fmtFecha)).append("\n");
    detalles.append("Hora:           ").append(viaje.getHoraAsignacion().format(fmtHora)).append("\n");
    detalles.append("Estado:         ").append(viaje.getEstadoViaje()).append("\n\n");

    detalles.append("TANQUERO\n");
    detalles.append("Placa:          ").append(viaje.getTanquero().getPlaca()).append("\n");
    detalles.append("Marca:          ").append(viaje.getTanquero().getMarca()).append("\n");
    detalles.append("Capacidad:      ").append(viaje.getTanquero().getCapacidadLitros()).append(" L\n\n");

    detalles.append("SOCIO\n");
    detalles.append("Nombre:         ").append(viaje.getSocio().getNombres()).append(" ").append(viaje.getSocio().getApellidos()).append("\n");
    detalles.append("Cédula:         ").append(viaje.getSocio().getCedula()).append("\n");
    detalles.append("Teléfono:       ").append(viaje.getSocio().getTelefono()).append("\n\n");

    if (viaje.getCliente() != null) {
      detalles.append("CLIENTE\n");
      detalles.append("Razón Social:   ").append(viaje.getCliente().getRazonSocial()).append("\n");
      detalles.append("RUC:            ").append(viaje.getCliente().getRuc()).append("\n\n");
    }

    detalles.append("RUTA Y CARGA\n");
    detalles.append("Origen:         ").append(viaje.getRutaOrigen()).append("\n");
    detalles.append("Destino:        ").append(viaje.getRutaDestino()).append("\n");
    detalles.append("Distancia:      ").append(String.format("%.2f", viaje.getKilometros())).append(" Km\n");
    detalles.append("Litros:         ").append(String.format("%.2f", viaje.getLitrosTransportados())).append(" L\n");
    detalles.append("Ocupación:      ").append(String.format("%.1f", viaje.getPorcentajeOcupacion())).append(" %\n");
    detalles.append("Flete:          $ ").append(String.format("%.2f", viaje.getValorFlete())).append("\n\n");

    if (viaje.getObservaciones() != null && !viaje.getObservaciones().isEmpty()) {
      detalles.append("OBSERVACIONES\n");
      detalles.append(viaje.getObservaciones()).append("\n\n");
    }

    JTextArea textArea = new JTextArea(detalles.toString());
    textArea.setEditable(false);
    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(450, 500));

    JOptionPane.showMessageDialog(
      this,
      scrollPane,
      "Detalles del Viaje",
      JOptionPane.PLAIN_MESSAGE
    );
  }
}
