package Presentación.Paneles;

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
import java.util.ArrayList;
import java.util.List;

public class Tanqueros extends JPanel {

  private JTable tabla;
  private DefaultTableModel modelo;

  private GestorTanqueros gestorTanqueros;
  private GestorSocios gestorSocios;
  private Usuario usuarioActual;

  private Botón botónRegistrar, botónConsultar, botónModificar, botónEliminar;
  private Botón botónAsignarChofer, botónCambiarEstado, botónActualizar;

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
    botónCambiarEstado = new Botón("Cambiar Estado", new Color(147, 51, 234));
    botónActualizar = new Botón("\uD83D\uDD04", new Color(253, 253, 253));
    botónActualizar.setForeground(Color.BLACK);

    Dimension dimBoton = new Dimension(160, 40);
    botónRegistrar.setPreferredSize(dimBoton);
    botónConsultar.setPreferredSize(dimBoton);
    botónModificar.setPreferredSize(dimBoton);
    botónEliminar.setPreferredSize(dimBoton);
    botónAsignarChofer.setPreferredSize(dimBoton);
    botónCambiarEstado.setPreferredSize(dimBoton);
    botónActualizar.setPreferredSize(new Dimension(50, 40));

    panelBotones.add(botónRegistrar);
    panelBotones.add(botónModificar);
    panelBotones.add(botónConsultar);
    panelBotones.add(botónCambiarEstado);
    panelBotones.add(botónAsignarChofer);
    panelBotones.add(botónEliminar);
    panelBotones.add(botónActualizar);

    add(panelBotones, BorderLayout.NORTH);

    String[] columnas = {"Placa", "Marca", "Modelo", "Año de fabricación", "Capacidad (Litros)", "Estado"};
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
    botónCambiarEstado.addActionListener(e -> cambiarEstado());
    botónActualizar.addActionListener(e -> cargarDatosBD());
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

    JLabel lblAnio = new JLabel("Año de fabricación:");
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
        GestorAlertas.mostrarError(this, "Placa inválida");
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
        GestorAlertas.mostrarError(this, "Año de fabricación o Capacidad inválidos");
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

    JLabel lblAnio = new JLabel("Año de fabricación:");
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
    // Crear un diálogo con opciones de filtro
    JPanel panelFiltro = new JPanel(new GridLayout(3, 2, 10, 10));
    panelFiltro.setBorder(new EmptyBorder(10, 10, 10, 10));
    
    // Campo para seleccionar criterio de búsqueda
    JLabel lblCriterio = new JLabel("Buscar por:");
    String[] criterios = {"Placa", "Marca", "Modelo", "Año de fabricación", "Estado"};
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
        "Buscar Tanqueros - Filtros",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
    );
    
    if (resultado == JOptionPane.OK_OPTION) {
        String criterio = (String) comboCriterio.getSelectedItem();
        String valor = txtValor.getText().trim();
        
        if (valor.isEmpty()) {
            // Si no hay valor, mostrar todos los tanqueros
            mostrarTodosTanqueros();
            return;
        }
        
        List<Tanquero> resultados = buscarTanquerosPorCriterio(criterio, valor);
        
        if (resultados.isEmpty()) {
            GestorAlertas.mostrarInfo(this, "No se encontraron tanqueros con esos criterios");
        } else if (resultados.size() == 1) {
            // Si solo hay un resultado, mostrar directamente sus detalles
            mostrarDetallesTanquero(resultados.get(0));
        } else {
            // Si hay múltiples resultados, mostrar lista para seleccionar
            mostrarResultadosBusquedaTanqueros(resultados);
        }
    }
}

private List<Tanquero> buscarTanquerosPorCriterio(String criterio, String valor) {
    List<Tanquero> todosTanqueros = gestorTanqueros.listarTanqueros();
    List<Tanquero> resultados = new ArrayList<>();

    String valorBusqueda = valor.toLowerCase();

    for (Tanquero tanquero : todosTanqueros) {
        boolean coincide = false;

        switch (criterio) {
            case "Placa":
                coincide = tanquero.getPlaca().toLowerCase().contains(valorBusqueda);
                break;
            case "Marca":
                coincide = tanquero.getMarca().toLowerCase().contains(valorBusqueda);
                break;
            case "Modelo":
                coincide = tanquero.getModelo().toLowerCase().contains(valorBusqueda);
                break;
            case "Año de fabricación":
                coincide = String.valueOf(tanquero.getAnioFabricacion()).contains(valorBusqueda);
                break;
            case "Estado":
                String estadoTanquero = tanquero.isEstado() ? "activo" : "inactivo";
                coincide = estadoTanquero.contains(valorBusqueda);
                break;
        }

        if (coincide) {
            resultados.add(tanquero);
        }
    }

    return resultados;
}

private void mostrarTodosTanqueros() {
    StringBuilder mensaje = new StringBuilder();
    mensaje.append("=== LISTADO COMPLETO DE TANQUEROS ===\n\n");
    
    List<Tanquero> tanqueros = gestorTanqueros.listarTanqueros();
    
    for (Tanquero tanquero : tanqueros) {
        mensaje.append("Placa: ").append(tanquero.getPlaca()).append("\n");
        mensaje.append("Marca: ").append(tanquero.getMarca()).append("\n");
        mensaje.append("Modelo: ").append(tanquero.getModelo()).append("\n");
        mensaje.append("Año de fabricación: ").append(tanquero.getAnioFabricacion()).append("\n");
        mensaje.append("Capacidad: ").append(tanquero.getCapacidadLitros()).append(" litros\n");
        mensaje.append("Estado: ").append(tanquero.isEstado() ? "Activo" : "Inactivo").append("\n");
        
        // Mostrar información del socio asignado
        Socio socioAsignado = gestorSocios.obtenerSocioAsignadoATanquero(tanquero.getIdTanquero());
        if (socioAsignado != null) {
            mensaje.append("Socio Asignado: ").append(socioAsignado.getNombreCompleto())
                   .append(" (").append(socioAsignado.getCedula()).append(")\n");
        } else {
            mensaje.append("Socio Asignado: Sin asignar\n");
        }
        mensaje.append("-----------------------------------\n");
    }
    
    mensaje.append("\nTotal de tanqueros: ").append(tanqueros.size());
    
    JTextArea textArea = new JTextArea(mensaje.toString());
    textArea.setEditable(false);
    textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(500, 400));
    
    JOptionPane.showMessageDialog(
        this,
        scrollPane,
        "Listado de Tanqueros",
        JOptionPane.PLAIN_MESSAGE
    );
}

private void mostrarResultadosBusquedaTanqueros(List<Tanquero> resultados) {
    String[] opciones = new String[resultados.size()];
    for (int i = 0; i < resultados.size(); i++) {
        opciones[i] = resultados.get(i).getPlaca() + " - " + 
                      resultados.get(i).getMarca() + " " + 
                      resultados.get(i).getModelo();
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
        for (Tanquero tanquero : resultados) {
            String opcion = tanquero.getPlaca() + " - " + 
                           tanquero.getMarca() + " " + 
                           tanquero.getModelo();
            if (opcion.equals(seleccion)) {
                mostrarDetallesTanquero(tanquero);
                break;
            }
        }
    }
}

  private void mostrarDetallesTanquero(Tanquero t) {
    StringBuilder detalles = new StringBuilder();

    Socio socioAsignado = gestorSocios.obtenerSocioAsignadoATanquero(t.getIdTanquero());

    detalles.append("Vehículo\n");
    detalles.append("Placa:      ").append(t.getPlaca()).append("\n");
    detalles.append("Marca:      ").append(t.getMarca()).append("\n");
    detalles.append("Modelo:     ").append(t.getModelo()).append("\n");
    detalles.append("Año de fabricación:        ").append(t.getAnioFabricacion()).append("\n");
    detalles.append("Capacidad:  ").append(t.getCapacidadLitros()).append(" Litros\n");
    detalles.append("Estado:     ").append(t.isEstado() ? "Activo" : "Inactivo").append("\n\n");

    detalles.append("Socio Asignado\n");
    if (socioAsignado != null) {
      detalles.append("Socio:      ").append(socioAsignado.getNombreCompleto()).append("\n");
      detalles.append("Cédula:      ").append(socioAsignado.getCedula()).append("\n\n");
    } else {
      detalles.append("Socio: Sin asignar\n\n");
    }

    JTextArea textArea = new JTextArea(detalles.toString());
    textArea.setEditable(false);
    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(400, 300));

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

    int idTanquero = gestorTanqueros.obtenerIdPorPlaca((String) modelo.getValueAt(fila, 0));
    String placa = (String) modelo.getValueAt(fila, 0);

    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Está seguro de eliminar el vehículo " + placa + " ?\n",
      "Confirmar Eliminación",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (confirmacion == JOptionPane.YES_OPTION) {

      if (gestorTanqueros.eliminarTanquero(idTanquero)) {
        GestorAlertas.mostrarExito(this, "Vehículo eliminado exitosamente");
        cargarDatosBD();
      }
    }
  }

  private void asignarChofer() {
    int fila = tabla.getSelectedRow();
    String placaPreseleccionada = (fila >= 0) ? (String) modelo.getValueAt(fila, 0) : "";

    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un vehículo de la tabla");
      return;
    }

    JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblPlaca = new JLabel("Placa Tanquero:");
    JTextField txtPlaca = new JTextField(placaPreseleccionada);

    // Verificar si ya tiene socio asignado
    int idTanquero = gestorTanqueros.obtenerIdPorPlaca(placaPreseleccionada);
    Socio socioActual = null;
    if (idTanquero != -1) {
      socioActual = gestorSocios.obtenerSocioAsignadoATanquero(idTanquero);
    }

    JLabel lblSocioActual = new JLabel("Socio Actual:");
    JLabel lblResultadoActual = new JLabel(
      socioActual != null ?
        socioActual.getNombres() + " " + socioActual.getApellidos() :
        "Sin asignar"
    );
    lblResultadoActual.setForeground(socioActual != null ? new Color(40, 167, 69) : Color.GRAY);

    JLabel lblCedula = new JLabel("Cédula Socio:");
    JTextField txtCedula = new JTextField();

    JLabel lblNombre = new JLabel("Nombre Socio:");
    JLabel lblResultadoNombre = new JLabel("---------------------------------------------");
    lblResultadoNombre.setForeground(Color.GRAY);

    JLabel lblFecha = new JLabel("Fecha Asignación:");
    JTextField txtFecha = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    txtFecha.setEditable(false);
    txtFecha.setBackground(Color.LIGHT_GRAY);

    JPanel panelBusqueda = new JPanel(new BorderLayout(5, 0));
    JButton btnBuscar = new JButton("Buscar");
    btnBuscar.setPreferredSize(new Dimension(75, 20));
    panelBusqueda.add(txtCedula, BorderLayout.CENTER);
    panelBusqueda.add(btnBuscar, BorderLayout.EAST);

    panel.add(lblPlaca);
    panel.add(txtPlaca);
    panel.add(lblSocioActual);
    panel.add(lblResultadoActual);
    panel.add(lblCedula);
    panel.add(panelBusqueda);
    panel.add(lblNombre);
    panel.add(lblResultadoNombre);
    panel.add(lblFecha);
    panel.add(txtFecha);

    final int[] idSocioEncontrado = {-1};

    btnBuscar.addActionListener(e -> {
      String cedula = txtCedula.getText().trim();
      if (!cedula.isEmpty()) {
        Socio s = gestorSocios.buscarPorCedula(cedula);
        if (s != null) {
          lblResultadoNombre.setText(s.getNombres() + " " + s.getApellidos());
          lblResultadoNombre.setForeground(new Color(40, 167, 69));
          idSocioEncontrado[0] = s.getIdSocio();
        } else {
          lblResultadoNombre.setText("No encontrado");
          lblResultadoNombre.setForeground(Color.RED);
          idSocioEncontrado[0] = -1;
        }
      }
    });

    Object[] opciones = socioActual != null ?
      new Object[]{"Asignar", "Desasignar", "Cancelar"} :
      new Object[]{"Asignar", "Cancelar"};

    int res = JOptionPane.showOptionDialog(
      this,
      panel,
      "Asignar Socio",
      JOptionPane.DEFAULT_OPTION,
      JOptionPane.PLAIN_MESSAGE,
      null,
      opciones,
      opciones[0]
    );

    String placa = txtPlaca.getText().trim().toUpperCase();
    int idTanqueroFinal = gestorTanqueros.obtenerIdPorPlaca(placa);

    if (!gestorTanqueros.existePlaca(placa)) {
      GestorAlertas.mostrarError(this, "La placa ingresada no existe");
      return;
    }

    if (res == 0) {
      String nombreSocio = lblResultadoNombre.getText();

      if (idSocioEncontrado[0] == -1 || nombreSocio.equals("---------------------------------------------admin") || nombreSocio.equals("No encontrado")) {
        GestorAlertas.mostrarError(this, "Buscar y seleccionar un socio válido");
        return;
      }

      if (gestorTanqueros.asignarChofer(idTanqueroFinal, idSocioEncontrado[0])) {
        GestorAlertas.mostrarExito(this, "Socio asignado exitosamente");
        cargarDatosBD();
      } else {
        GestorAlertas.mostrarError(this, "Error al asignar el socio");
      }

    } else if (res == 1 && socioActual != null) {
      int confirmacion = JOptionPane.showConfirmDialog(
        this,
        "¿Desasignar el socio " + socioActual.getNombreCompleto() + " del vehículo " + placa + " ?",
        "Confirmar Desasignación",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.PLAIN_MESSAGE
      );

      if (confirmacion == JOptionPane.YES_OPTION) {
        if (gestorTanqueros.desasignarChofer(idTanqueroFinal)) {
          GestorAlertas.mostrarExito(this, "Socio desasignado exitosamente");
          cargarDatosBD();
        } else {
          GestorAlertas.mostrarError(this, "Error al desasignar el socio");
        }
      }
    }
  }

  private void cambiarEstado() {
    int fila = tabla.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un vehículo de la tabla");
      return;
    }

    String placa = (String) modelo.getValueAt(fila, 0);
    String estadoActual = (String) modelo.getValueAt(fila, 5);

    Tanquero t = gestorTanqueros.buscarPorPlaca(placa);
    if (t == null) {
      GestorAlertas.mostrarError(this, "Error al cargar los datos del vehículo");
      return;
    }

    boolean nuevoEstado = estadoActual.equals("Inactivo");
    String accion = nuevoEstado ? "activar" : "desactivar";

    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Está seguro de " + accion + " el vehículo " + placa + "?",
      "Cambiar Estado",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (confirmacion == JOptionPane.YES_OPTION) {
      if (gestorTanqueros.cambiarEstado(t.getIdTanquero(), nuevoEstado)) {
        GestorAlertas.mostrarExito(this, "Estado actualizado exitosamente");
        cargarDatosBD();
      } else {
        GestorAlertas.mostrarError(this, "Error al cambiar el estado");
      }
    }
  }

}