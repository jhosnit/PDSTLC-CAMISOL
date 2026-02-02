package Presentación.Paneles;

import Logica.Entidades.*;
import Logica.Gestores.*;
import Logica.Utilidades.CalculadoraFlete;
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
import java.util.ArrayList;
import java.util.List;

public class Transportes extends JPanel {

  private JTable tablaViajes;
  private DefaultTableModel modeloTabla;
  private List<Transporte> listaViajes;

  private GestorTransporte gestorTransporte;
  private GestorTanqueros gestorTanqueros;
  private GestorSocios gestorSocios;
  private Usuario usuarioActual;

  private Botón btnNuevoViaje, btnActualizar, btnConsultar, btnCambiarEstado, btnEliminar;

  public Transportes(Usuario usuario) {
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
    btnConsultar = new Botón("Consultar", new Color(70, 128, 139));
    btnCambiarEstado = new Botón("Cambiar Estado", new Color(147, 51, 234));
    btnEliminar = new Botón("Eliminar", new Color(239, 68, 68));
    btnActualizar = new Botón("\uD83D\uDD04", new Color(253, 253, 253));
    btnActualizar.setForeground(Color.BLACK);

    Dimension dim = new Dimension(150, 40);
    btnNuevoViaje.setPreferredSize(dim);
    btnConsultar.setPreferredSize(dim);
    btnCambiarEstado.setPreferredSize(dim);
    btnEliminar.setPreferredSize(dim);
    btnActualizar.setPreferredSize(new Dimension(50, 40));

    panelBotones.add(btnNuevoViaje);
    panelBotones.add(btnConsultar);
    panelBotones.add(btnCambiarEstado);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnActualizar);
    add(panelBotones, BorderLayout.NORTH);

    String[] columnas = {
      "Fecha del viaje", "Tanquero", "Socio", "Cliente", "Ruta destino", "Litros", "Ocupación", "Flete ($)", "Estado"
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

    tablaViajes.getColumnModel().getColumn(0).setPreferredWidth(90);
    tablaViajes.getColumnModel().getColumn(1).setPreferredWidth(100);
    tablaViajes.getColumnModel().getColumn(2).setPreferredWidth(100);
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
        comboTanqueros.addItem(t.getPlaca() + " - " + t.getCapacidadLitros() + " L");
      }
    }

    JLabel lblChofer = new JLabel("Socio:");
    JTextField txtSocio = new JTextField();
    txtSocio.setEditable(false);
    txtSocio.setBackground(Color.LIGHT_GRAY);

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

    comboTanqueros.addActionListener(e -> {
      int idxTanquero = comboTanqueros.getSelectedIndex();
      if (idxTanquero >= 0) {
        String item = (String) comboTanqueros.getSelectedItem();
        String placaSeleccionada = item.split(" - ")[0];

        Tanquero tSeleccionado = null;
        for (Tanquero t : listaTanqueros) {
          if (t.getPlaca().equals(placaSeleccionada)) {
            tSeleccionado = t;
            break;
          }
        }

        if (tSeleccionado != null) {
          Socio socioAsignado = gestorSocios.obtenerSocioAsignadoATanquero(tSeleccionado.getIdTanquero());
          if (socioAsignado != null) {
            txtSocio.setText(socioAsignado.getNombreCompleto());
          } else {
            txtSocio.setText("Sin socio asignado");
          }
        }
      }
    });

    panel.add(lblTanquero);
    panel.add(comboTanqueros);
    panel.add(lblChofer);
    panel.add(txtSocio);
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
      "Registrar Viaje",
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (res == JOptionPane.OK_OPTION) {
      try {
        if (comboTanqueros.getSelectedIndex() < 0) {
          GestorAlertas.mostrarError(this, "Debe seleccionar un Tanquero");
          return;
        }

        if (txtDestino.getText().trim().isEmpty() || txtKms.getText().trim().isEmpty() ||
          txtLitros.getText().trim().isEmpty()) {
          GestorAlertas.mostrarError(this, "Todos los campos son obligatorios");
          return;
        }

        String item = (String) comboTanqueros.getSelectedItem();
        String placaSeleccionada = item.split(" - ")[0];
        Tanquero tanqueroSel = null;
        for (Tanquero t : listaTanqueros) {
          if (t.getPlaca().equals(placaSeleccionada)) {
            tanqueroSel = t;
            break;
          }
        }

        Socio socioSel = gestorSocios.obtenerSocioAsignadoATanquero(tanqueroSel.getIdTanquero());
        if (socioSel == null) {
          GestorAlertas.mostrarError(this, "El tanquero no tiene un socio asignado");
          return;
        }

        double kms = Double.parseDouble(txtKms.getText().trim());
        double litros = Double.parseDouble(txtLitros.getText().trim());

        if (kms < 0 || litros <= 0) {
          GestorAlertas.mostrarError(this, "Kilómetros o Litros inválidos");
          return;
        }

        if (litros > tanqueroSel.getCapacidadLitros()) {
          GestorAlertas.mostrarError(this, "La carga excede la capacidad del tanquero");
          return;
        }

        double porcentajeOcupacion = (litros / tanqueroSel.getCapacidadLitros()) * 100.0;
        CalculadoraFlete calcu = new CalculadoraFlete();
        double costoFlete = calcu.calcularCostoTotal(kms, litros);

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
        t.setPorcentajeOcupacion(porcentajeOcupacion);
        t.setValorFlete(costoFlete);
        t.setEstadoViaje("En Curso");
        t.setUsuarioRegistro(usuarioActual.getUsername());
        t.setObservaciones(txtObservaciones.getText().trim());

        if (gestorTransporte.registrarViaje(t)) {
          GestorAlertas.mostrarExito(this, "Viaje registrado exitosamente");
          cargarViajes();
        } else {
          GestorAlertas.mostrarError(this, "Error al guardar");
        }
      } catch (NumberFormatException ex) {
        GestorAlertas.mostrarError(this, "Kilómetros o Litros inválidos");
      } catch (Exception ex) {
        GestorAlertas.mostrarError(this, "Error: " + ex.getMessage());
      }
    }
  }

  private void cambiarEstadoViaje() {
    int fila = tablaViajes.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un viaje de la tabla");
      return;
    }

    Transporte viaje = listaViajes.get(fila);
    String estadoActual = viaje.getEstadoViaje();


    if (estadoActual.equals("Finalizado")) {
      GestorAlertas.mostrarAdvertencia(this, "El viaje ya finalizó, no se puede modificar");
      return;
    }
    if (estadoActual.equals("Cancelado")) {
      GestorAlertas.mostrarAdvertencia(this, "El viaje fue cancelado, no se puede modificar");
      return;
    }

    int idTransporte = viaje.getIdTransporte();
    String tanquero = viaje.getTanquero().getPlaca();

    String[] opciones = {"Cancelado", "Finalizado"};

    String nuevoEstado = (String) JOptionPane.showInputDialog(
      this,
      "Tanquero: " + tanquero + "\n" +
        "Estado actual: " + estadoActual,
      "Cambiar Estado del Viaje",
      JOptionPane.PLAIN_MESSAGE,
      null,
      opciones,
      estadoActual
    );

    if (nuevoEstado != null && !nuevoEstado.equals(estadoActual)) {

      if (gestorTransporte.cambiarEstadoViaje(idTransporte, nuevoEstado)) {
        GestorAlertas.mostrarExito(this, "Estado actualizado exitosamente");
        cargarViajes();
      } else {
        GestorAlertas.mostrarError(this, "Error al cambiar el estado");
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
    String estadoViaje = viaje.getEstadoViaje();

    if (!estadoViaje.equals("Cancelado") && !estadoViaje.equals("Finalizado")) {
      GestorAlertas.mostrarAdvertencia(this,
        "Solo se pueden eliminar viajes en estado 'Cancelado' o 'Finalizado'");
      return;
    }

    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Desea eliminar definitivamente este viaje del historial?",
      "Confirmar Eliminación",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (confirmacion == JOptionPane.YES_OPTION) {
      if (gestorTransporte.eliminarViaje(viaje.getIdTransporte())) {
        GestorAlertas.mostrarExito(this, "Viaje eliminado exitosamente");
        cargarViajes();
      } else {
        GestorAlertas.mostrarError(this, "Error al eliminar el viaje");
      }
    }
  }

  private void consultarViaje() {
        // Crear un diálogo con opciones de filtro - IGUAL QUE EN SOCIOS
        JPanel panelFiltro = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFiltro.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Campo para seleccionar criterio de búsqueda
        JLabel lblCriterio = new JLabel("Buscar por:");
        String[] criterios = {"Fecha del viaje", "Tanquero", "Socio", "Ruta destino", "Estado"};
        JComboBox<String> comboCriterio = new JComboBox<>(criterios);
        
        // Campo para ingresar el valor a buscar
        JLabel lblValor = new JLabel("Valor:");
        JTextField txtValor = new JTextField();
        
        panelFiltro.add(lblCriterio);
        panelFiltro.add(comboCriterio);
        panelFiltro.add(lblValor);
        panelFiltro.add(txtValor);
        
        int resultado = JOptionPane.showConfirmDialog(
                this,
                panelFiltro,
                "Buscar Viajes - Filtros",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        
        if (resultado == JOptionPane.OK_OPTION) {
            String criterio = (String) comboCriterio.getSelectedItem();
            String valor = txtValor.getText().trim();
            
            if (valor.isEmpty()) {
                // Si no hay valor, mostrar todos los viajes
                mostrarTodosViajes();
                return;
            }
            
            List<Transporte> resultados = buscarViajesPorCriterio(criterio, valor);
            
            if (resultados.isEmpty()) {
                GestorAlertas.mostrarInfo(this, "No se encontraron viajes con esos criterios");
            } else if (resultados.size() == 1) {
                // Si solo hay un resultado, mostrar directamente sus detalles
                mostrarDetallesViaje(resultados.get(0));
            } else {
                // Si hay múltiples resultados, mostrar lista para seleccionar
                mostrarResultadosBusquedaViajes(resultados);
            }
        }
    }

    private List<Transporte> buscarViajesPorCriterio(String criterio, String valor) {
        List<Transporte> todosViajes = gestorTransporte.listarViajes();
        List<Transporte> resultados = new ArrayList<>();

        String valorBusqueda = valor.toLowerCase();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Transporte viaje : todosViajes) {
            boolean coincide = false;

            switch (criterio) {
                case "Fecha del viaje":
                    String fechaStr = viaje.getFechaAsignacion().format(fmt);
                    coincide = fechaStr.toLowerCase().contains(valorBusqueda);
                    break;
                case "Tanquero":
                    coincide = viaje.getTanquero().getPlaca().toLowerCase().contains(valorBusqueda);
                    break;
                case "Socio":
                    String nombreSocio = viaje.getSocio().getNombres() + " " + viaje.getSocio().getApellidos();
                    coincide = nombreSocio.toLowerCase().contains(valorBusqueda);
                    break;
                case "Ruta destino":
                    coincide = viaje.getRutaDestino().toLowerCase().contains(valorBusqueda);
                    break;
                case "Estado":
                    coincide = viaje.getEstadoViaje().toLowerCase().contains(valorBusqueda);
                    break;
            }

            if (coincide) {
                resultados.add(viaje);
            }
        }

        return resultados;
    }

    private void mostrarTodosViajes() {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("=== LISTADO COMPLETO DE VIAJES ===\n\n");
        
        List<Transporte> viajes = gestorTransporte.listarViajes();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Transporte viaje : viajes) {
            mensaje.append("Fecha del viaje: ").append(viaje.getFechaAsignacion().format(fmt)).append("\n");
            mensaje.append("Tanquero: ").append(viaje.getTanquero().getPlaca()).append("\n");
            mensaje.append("Socio: ").append(viaje.getSocio().getNombres()).append(" ")
                   .append(viaje.getSocio().getApellidos()).append("\n");
            mensaje.append("Ruta destino: ").append(viaje.getRutaDestino()).append("\n");
            mensaje.append("Litros: ").append(String.format("%.2f", viaje.getLitrosTransportados())).append(" L\n");
            mensaje.append("Ocupación: ").append(String.format("%.1f", viaje.getPorcentajeOcupacion())).append("%\n");
            mensaje.append("Flete: $ ").append(String.format("%.2f", viaje.getValorFlete())).append("\n");
            mensaje.append("Estado: ").append(viaje.getEstadoViaje()).append("\n");
            mensaje.append("-----------------------------------\n");
        }
        
        mensaje.append("\nTotal de viajes: ").append(viajes.size());
        
        JTextArea textArea = new JTextArea(mensaje.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Listado de Viajes",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void mostrarResultadosBusquedaViajes(List<Transporte> resultados) {
        String[] opciones = new String[resultados.size()];
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (int i = 0; i < resultados.size(); i++) {
            Transporte viaje = resultados.get(i);
            opciones[i] = viaje.getFechaAsignacion().format(fmt) + " - " +
                         viaje.getTanquero().getPlaca() + " - " +
                         viaje.getRutaDestino() + " - " +
                         viaje.getSocio().getNombres();
        }

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Se encontraron " + resultados.size() + " resultados. Seleccione uno:",
                "Resultados de Búsqueda",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion != null) {
            for (Transporte viaje : resultados) {
                String opcion = viaje.getFechaAsignacion().format(fmt) + " - " +
                              viaje.getTanquero().getPlaca() + " - " +
                              viaje.getRutaDestino() + " - " +
                              viaje.getSocio().getNombres();
                if (opcion.equals(seleccion)) {
                    mostrarDetallesViaje(viaje);
                    break;
                }
            }
        }
    }

  private void mostrarDetallesViaje(Transporte viaje) {
    DateTimeFormatter fmtFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH:mm:ss");

    StringBuilder detalles = new StringBuilder();
    detalles.append("VIAJE\n");
    detalles.append("Inicio:         ").append(viaje.getFechaAsignacion().format(fmtFecha))
      .append(" ").append(viaje.getHoraAsignacion().format(fmtHora)).append("\n");

    if (viaje.getFechaFin() != null && viaje.getHoraFin() != null) {
      detalles.append("Fin:            ").append(viaje.getFechaFin().format(fmtFecha))
        .append(" ").append(viaje.getHoraFin().format(fmtHora)).append("\n\n");
    } else {
      detalles.append("Fin:            En Curso\n\n");
    }

    detalles.append("TANQUERO\n");
    detalles.append("Placa:          ").append(viaje.getTanquero().getPlaca()).append("\n");
    detalles.append("Capacidad:      ").append(viaje.getTanquero().getCapacidadLitros()).append(" L\n\n");

    detalles.append("SOCIO\n");
    if (viaje.getSocio() != null) {
      detalles.append("Nombre:         ").append(viaje.getSocio().getNombres()).append(" ").append(viaje.getSocio().getApellidos()).append("\n");
      detalles.append("Cédula:         ").append(viaje.getSocio().getCedula()).append("\n\n");
    }

    if (viaje.getCliente() != null) {
      detalles.append("CLIENTE\n");
      detalles.append("RUC:          0990318735001\n");
      detalles.append("Razón Social:   ").append(viaje.getCliente().getRazonSocial()).append("\n\n");
    }

    detalles.append("RUTA Y CARGA\n");
    detalles.append("Origen:         ").append(viaje.getRutaOrigen()).append("\n");
    detalles.append("Ruta destino:        ").append(viaje.getRutaDestino()).append("\n");
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

    JOptionPane.showMessageDialog(this, scrollPane, "Detalles del Viaje", JOptionPane.PLAIN_MESSAGE);
  }
}