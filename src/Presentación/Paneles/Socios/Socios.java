package Presentación.Paneles.Socios;

import Presentación.Recursos.Botón;
import Presentación.Paneles.Auditoría.Auditoría;
import Logica.Gestores.GestorAlertas;
import Logica.Gestores.GestorSocios;
import Logica.Entidades.Socio;
import Logica.Entidades.Usuario;
import Logica.Utilidades.Validaciones;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Socios extends JPanel {

  private JTable tablaSocios;
  private DefaultTableModel modeloTabla;
  private GestorSocios gestorSocios;
  private Usuario usuarioActual;

  private Botón btnRegistrar, btnActualizar, btnCambiarEstado, btnConsultar, btnEliminar;

  public Socios(Usuario usuario) {
    this.usuarioActual = usuario;
    this.gestorSocios = new GestorSocios();
    inicializarComponentes();
    cargarSocios();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout());
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelBotones.setOpaque(false);

    btnRegistrar = new Botón("Registrar", new Color(40, 167, 69));
    btnActualizar = new Botón("Actualizar Datos", new Color(234, 177, 0));
    btnConsultar = new Botón("Consultar", new Color(70, 128, 139));
    btnCambiarEstado = new Botón("Cambiar Estado", new Color(147, 51, 234));
    btnEliminar = new Botón("Eliminar", new Color(239, 68, 68));

    Dimension dim = new Dimension(150, 40);
    btnRegistrar.setPreferredSize(dim);
    btnActualizar.setPreferredSize(dim);
    btnConsultar.setPreferredSize(dim);
    btnCambiarEstado.setPreferredSize(dim);
    btnEliminar.setPreferredSize(dim);

    panelBotones.add(btnRegistrar);
    panelBotones.add(btnActualizar);
    panelBotones.add(btnConsultar);
    panelBotones.add(btnCambiarEstado);
    panelBotones.add(btnEliminar);

    add(panelBotones, BorderLayout.NORTH);

    crearTabla();

    JScrollPane scroll = new JScrollPane(tablaSocios);
    scroll.getViewport().setBackground(new Color(31, 41, 55));
    scroll.setBorder(new LineBorder(new Color(55, 65, 81), 1));

    add(scroll, BorderLayout.CENTER);

    btnRegistrar.addActionListener(e -> registrarSocio());
    btnActualizar.addActionListener(e -> actualizarSocio());
    btnConsultar.addActionListener(e -> consultarSocio());
    btnCambiarEstado.addActionListener(e -> cambiarEstado());
    btnEliminar.addActionListener(e -> eliminarSocio());
  }

  private void crearTabla() {
    String[] columnas = {
      "ID", "RUC", "Razón Social", "Teléfono", "Correo", "Dirección", "Estado"
    };

    modeloTabla = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tablaSocios = new JTable(modeloTabla);
    tablaSocios.setBackground(new Color(31, 41, 55));
    tablaSocios.setForeground(Color.WHITE);
    tablaSocios.setGridColor(new Color(55, 65, 81));
    tablaSocios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tablaSocios.setRowHeight(30);
    tablaSocios.setSelectionBackground(new Color(75, 85, 99));
    tablaSocios.setSelectionForeground(Color.WHITE);

    JTableHeader header = tablaSocios.getTableHeader();
    header.setBackground(new Color(243, 244, 246));
    header.setForeground(new Color(31, 41, 55));
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setPreferredSize(new Dimension(0, 35));

    DefaultTableCellRenderer render = new DefaultTableCellRenderer();
    render.setBackground(new Color(31, 41, 55));
    render.setForeground(Color.WHITE);
    render.setHorizontalAlignment(SwingConstants.LEFT);

    for (int i = 0; i < tablaSocios.getColumnCount(); i++) {
      tablaSocios.getColumnModel().getColumn(i).setCellRenderer(render);
    }

    tablaSocios.getColumnModel().getColumn(0).setPreferredWidth(50);
    tablaSocios.getColumnModel().getColumn(2).setPreferredWidth(200);
  }

  private void cargarSocios() {
    modeloTabla.setRowCount(0);
    List<Socio> lista = gestorSocios.listarSocios();

    for (Socio socio : lista) {
      modeloTabla.addRow(new Object[]{
        socio.getIdSocio(),
        socio.getRuc(),
        socio.getRazonSocial(),
        socio.getTelefono(),
        socio.getCorreo(),
        socio.getDireccion(),
        socio.isEstado() ? "Activo" : "Inactivo"
      });
    }
  }

  private void registrarSocio() {
    JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblRuc = new JLabel("RUC:");
    JTextField txtRuc = new JTextField();

    JLabel lblRazonSocial = new JLabel("Razón Social:");
    JTextField txtRazonSocial = new JTextField();

    JLabel lblTelefono = new JLabel("Teléfono:");
    JTextField txtTelefono = new JTextField();

    JLabel lblCorreo = new JLabel("Correo Electrónico:");
    JTextField txtCorreo = new JTextField();

    JLabel lblDireccion = new JLabel("Dirección:");
    JTextField txtDireccion = new JTextField();

    panel.add(lblRuc);
    panel.add(txtRuc);
    panel.add(lblRazonSocial);
    panel.add(txtRazonSocial);
    panel.add(lblTelefono);
    panel.add(txtTelefono);
    panel.add(lblCorreo);
    panel.add(txtCorreo);
    panel.add(lblDireccion);
    panel.add(txtDireccion);

    int resultado = JOptionPane.showConfirmDialog(
      this,
      panel,
      "Registrar Nuevo Socio",
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (resultado == JOptionPane.OK_OPTION) {
      String ruc = txtRuc.getText().trim();
      String razonSocial = txtRazonSocial.getText().trim();
      String telefono = txtTelefono.getText().trim();
      String correo = txtCorreo.getText().trim();
      String direccion = txtDireccion.getText().trim();

      // Validaciones
      if (ruc.isEmpty() || razonSocial.isEmpty() || telefono.isEmpty() ||
        correo.isEmpty() || direccion.isEmpty()) {
        GestorAlertas.mostrarError(this, "Todos los campos son obligatorios");
        return;
      }

      if (!Validaciones.validarRUC(ruc)) {
        GestorAlertas.mostrarError(this, "RUC inválido. Debe tener 13 dígitos y terminar en 001");
        return;
      }

      if (gestorSocios.existeRuc(ruc)) {
        GestorAlertas.mostrarError(this, "El RUC ya está registrado en el sistema");
        return;
      }

      if (!Validaciones.validarTelefono(telefono)) {
        GestorAlertas.mostrarError(this, "Teléfono inválido. Debe tener 10 dígitos y empezar con 09");
        return;
      }

      if (!Validaciones.validarCorreo(correo)) {
        GestorAlertas.mostrarError(this, "Correo electrónico inválido");
        return;
      }

      if (razonSocial.length() > 150) {
        GestorAlertas.mostrarError(this, "Razón Social no puede exceder 150 caracteres");
        return;
      }

      if (direccion.length() > 200) {
        GestorAlertas.mostrarError(this, "Dirección no puede exceder 200 caracteres");
        return;
      }

      Socio nuevoSocio = new Socio(ruc, razonSocial, direccion, telefono, correo);
      nuevoSocio.setUsuarioRegistro(usuarioActual.getUsername());

      if (gestorSocios.crearSocio(nuevoSocio)) {
        GestorAlertas.mostrarExito(this, "Socio registrado exitosamente");
        /*
        Auditoría.registrarAccion(
          usuarioActual.getUsername(),
          usuarioActual.getNombre() + " " + usuarioActual.getApellido(),
          "Socios",
          "Registró nuevo socio"
        );
        */
        cargarSocios();
      } else {
        GestorAlertas.mostrarError(this, "Error al registrar el socio");
      }
    }
  }

  private void actualizarSocio() {
    int fila = tablaSocios.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un socio de la tabla");
      return;
    }

    int idSocio = (int) modeloTabla.getValueAt(fila, 0);
    String rucActual = (String) modeloTabla.getValueAt(fila, 1);

    Socio socio = gestorSocios.buscarPorRuc(rucActual);
    if (socio == null) {
      GestorAlertas.mostrarError(this, "Error al cargar los datos del socio");
      return;
    }

    JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblRuc = new JLabel("RUC:");
    JTextField txtRuc = new JTextField(socio.getRuc());
    txtRuc.setEditable(false);
    txtRuc.setBackground(Color.LIGHT_GRAY);

    JLabel lblRazonSocial = new JLabel("Razón Social:");
    JTextField txtRazonSocial = new JTextField(socio.getRazonSocial());

    JLabel lblTelefono = new JLabel("Teléfono:");
    JTextField txtTelefono = new JTextField(socio.getTelefono());

    JLabel lblCorreo = new JLabel("Correo:");
    JTextField txtCorreo = new JTextField(socio.getCorreo());

    JLabel lblDireccion = new JLabel("Dirección:");
    JTextField txtDireccion = new JTextField(socio.getDireccion());

    panel.add(lblRuc);
    panel.add(txtRuc);
    panel.add(lblRazonSocial);
    panel.add(txtRazonSocial);
    panel.add(lblTelefono);
    panel.add(txtTelefono);
    panel.add(lblCorreo);
    panel.add(txtCorreo);
    panel.add(lblDireccion);
    panel.add(txtDireccion);

    int resultado = JOptionPane.showConfirmDialog(
      this,
      panel,
      "Actualizar Datos del Socio",
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (resultado == JOptionPane.OK_OPTION) {
      String razonSocial = txtRazonSocial.getText().trim();
      String telefono = txtTelefono.getText().trim();
      String correo = txtCorreo.getText().trim();
      String direccion = txtDireccion.getText().trim();

      // Validaciones
      if (razonSocial.isEmpty() || telefono.isEmpty() || correo.isEmpty() || direccion.isEmpty()) {
        GestorAlertas.mostrarError(this, "Todos los campos son obligatorios");
        return;
      }

      if (!Validaciones.validarTelefono(telefono)) {
        GestorAlertas.mostrarError(this, "Teléfono inválido");
        return;
      }

      if (!Validaciones.validarCorreo(correo)) {
        GestorAlertas.mostrarError(this, "Correo electrónico inválido");
        return;
      }

      socio.setRazonSocial(razonSocial);
      socio.setTelefono(telefono);
      socio.setCorreo(correo);
      socio.setDireccion(direccion);

      if (gestorSocios.actualizarSocio(socio)) {
        GestorAlertas.mostrarExito(this, "Datos actualizados correctamente");

        /*
        Auditoría.registrarAccion(
          usuarioActual.getUsername(),
          usuarioActual.getNombre() + " " + usuarioActual.getApellido(),
          "Socios",
          "Actualizó datos del socio: " + socio.getRazonSocial()
        );
         */
        cargarSocios();
      } else {
        GestorAlertas.mostrarError(this, "Error al actualizar los datos");
      }
    }
  }

  private void consultarSocio() {
    String busqueda = JOptionPane.showInputDialog(
      this,
      "Ingrese RUC o nombre del socio:",
      "Buscar Socio",
      JOptionPane.PLAIN_MESSAGE
    );

    if (busqueda == null || busqueda.trim().isEmpty()) {
      return;
    }

    busqueda = busqueda.trim();

    if (busqueda.length() == 13 && busqueda.matches("^[0-9]+$")) {
      Socio socio = gestorSocios.buscarPorRuc(busqueda);
      if (socio != null) {
        mostrarDetallesSocio(socio);
      } else {
        GestorAlertas.mostrarError(this, "No se encontró un socio con ese RUC");
      }
    } else {

      List<Socio> resultados = gestorSocios.buscarPorNombre(busqueda);
      if (resultados.isEmpty()) {
        GestorAlertas.mostrarError(this, "No se encontraron socios con ese nombre");
      } else if (resultados.size() == 1) {
        mostrarDetallesSocio(resultados.get(0));
      } else {
        mostrarResultadosBusqueda(resultados);
      }
    }
  }

  private void mostrarDetallesSocio(Socio socio) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    StringBuilder detalles = new StringBuilder();
    detalles.append("RUC: ").append(socio.getRuc()).append("\n");
    detalles.append("Razón Social: ").append(socio.getRazonSocial()).append("\n");
    detalles.append("Teléfono: ").append(socio.getTelefono()).append("\n");
    detalles.append("Correo: ").append(socio.getCorreo()).append("\n");
    detalles.append("Dirección: ").append(socio.getDireccion()).append("\n");
    detalles.append("Estado: ").append(socio.isEstado() ? "Activo" : "Inactivo").append("\n\n");

    if (socio.getFechaRegistro() != null) {
      detalles.append("Fecha de Registro: ").append(socio.getFechaRegistro().format(fmt)).append("\n");
    }
    if (socio.getFechaModificacion() != null) {
      detalles.append("Última Modificación: ").append(socio.getFechaModificacion().format(fmt)).append("\n");
    }
    if (socio.getUsuarioRegistro() != null) {
      detalles.append("Registrado por: ").append(socio.getUsuarioRegistro()).append("\n");
    }

    JTextArea textArea = new JTextArea(detalles.toString());
    textArea.setEditable(false);
    textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(400, 250));

    JOptionPane.showMessageDialog(
      this,
      scrollPane,
      "Detalles del Socio",
      JOptionPane.PLAIN_MESSAGE
    );
  }

  private void mostrarResultadosBusqueda(List<Socio> resultados) {
    String[] opciones = new String[resultados.size()];
    for (int i = 0; i < resultados.size(); i++) {
      opciones[i] = resultados.get(i).getRazonSocial() + " (" + resultados.get(i).getRuc() + ")";
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
      for (Socio socio : resultados) {
        String opcion = socio.getRazonSocial() + " (" + socio.getRuc() + ")";
        if (opcion.equals(seleccion)) {
          mostrarDetallesSocio(socio);
          break;
        }
      }
    }
  }

  private void cambiarEstado() {
    int fila = tablaSocios.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un socio de la tabla");
      return;
    }

    int idSocio = (int) modeloTabla.getValueAt(fila, 0);
    String razonSocial = (String) modeloTabla.getValueAt(fila, 2);
    String estadoActual = (String) modeloTabla.getValueAt(fila, 6);

    boolean nuevoEstado = estadoActual.equals("Inactivo");
    String accion = nuevoEstado ? "activar" : "desactivar";

    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Está seguro de " + accion + " el socio " + razonSocial + "?",
      "Confirmar Cambio de Estado",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (confirmacion == JOptionPane.YES_OPTION) {
      if (gestorSocios.cambiarEstado(idSocio, nuevoEstado)) {
        GestorAlertas.mostrarExito(this, "Estado actualizado correctamente");

        // Registrar en auditoría
        Auditoría.registrarAccion(
          usuarioActual.getUsername(),
          usuarioActual.getNombre() + " " + usuarioActual.getApellido(),
          "Socios",
          "Cambió estado del socio: " + razonSocial + " a " + (nuevoEstado ? "Activo" : "Inactivo")
        );

        cargarSocios();
      } else {
        GestorAlertas.mostrarError(this, "Error al cambiar el estado");
      }
    }
  }

  // ==================== ELIMINAR SOCIO ====================
  private void eliminarSocio() {
    int fila = tablaSocios.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un socio de la tabla");
      return;
    }

    int idSocio = (int) modeloTabla.getValueAt(fila, 0);
    String razonSocial = (String) modeloTabla.getValueAt(fila, 2);

    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Está seguro de eliminar el socio " + razonSocial + "?\n" +
        "Esta acción no se puede deshacer.",
      "Confirmar Eliminación",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.WARNING_MESSAGE
    );

    if (confirmacion == JOptionPane.YES_OPTION) {
      if (gestorSocios.eliminarSocio(idSocio)) {
        GestorAlertas.mostrarExito(this, "Socio eliminado correctamente");

        // Registrar en auditoría
        Auditoría.registrarAccion(
          usuarioActual.getUsername(),
          usuarioActual.getNombre() + " " + usuarioActual.getApellido(),
          "Socios",
          "Eliminó el socio: " + razonSocial
        );

        cargarSocios();
      } else {
        GestorAlertas.mostrarError(this, "Error al eliminar el socio.\n" +
          "Puede que tenga servicios asociados.");
      }
    }
  }
}
