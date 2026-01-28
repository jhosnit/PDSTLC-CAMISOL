package Presentación.Paneles.Socios;

import Presentación.Recursos.Botón;
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
    btnActualizar = new Botón("Actualizar", new Color(234, 177, 0));
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
      "Cédula", "Nombres", "Apellidos", "Teléfono", "Correo", "Dirección", "Estado"
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

    tablaSocios.getColumnModel().getColumn(0).setPreferredWidth(100);
    tablaSocios.getColumnModel().getColumn(1).setPreferredWidth(150);
    tablaSocios.getColumnModel().getColumn(2).setPreferredWidth(150);
    tablaSocios.getColumnModel().getColumn(3).setPreferredWidth(150);
  }

  private void cargarSocios() {
    modeloTabla.setRowCount(0);
    List<Socio> lista = gestorSocios.listarSocios();

    for (Socio socio : lista) {
      modeloTabla.addRow(new Object[]{
        socio.getCedula(),
        socio.getNombres(),
        socio.getApellidos(),
        socio.getTelefono(),
        socio.getCorreo(),
        socio.getDireccion(),
        socio.isEstado() ? "Activo" : "Inactivo"
      });
    }
  }

  private void registrarSocio() {
    JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblCedula = new JLabel("Cédula:");
    JTextField txtCedula = new JTextField();

    JLabel lblNombres = new JLabel("Nombres:");
    JTextField txtNombres = new JTextField();

    JLabel lblApellidos = new JLabel("Apellidos:");
    JTextField txtApellidos = new JTextField();

    JLabel lblTelefono = new JLabel("Teléfono:");
    JTextField txtTelefono = new JTextField();

    JLabel lblCorreo = new JLabel("Correo Electrónico:");
    JTextField txtCorreo = new JTextField();

    JLabel lblDireccion = new JLabel("Dirección:");
    JTextField txtDireccion = new JTextField();

    panel.add(lblCedula); panel.add(txtCedula);
    panel.add(lblNombres); panel.add(txtNombres);
    panel.add(lblApellidos); panel.add(txtApellidos);
    panel.add(lblTelefono); panel.add(txtTelefono);
    panel.add(lblCorreo); panel.add(txtCorreo);
    panel.add(lblDireccion); panel.add(txtDireccion);

    int resultado = JOptionPane.showConfirmDialog(
      this,
      panel,
      "Registrar Socio",
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (resultado == JOptionPane.OK_OPTION) {
      String cedula = txtCedula.getText().trim();
      String nombres = txtNombres.getText().trim();
      String apellidos = txtApellidos.getText().trim();
      String telefono = txtTelefono.getText().trim();
      String correo = txtCorreo.getText().trim();
      String direccion = txtDireccion.getText().trim();

      // Validaciones
      if (cedula.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() ||
        telefono.isEmpty() || correo.isEmpty() || direccion.isEmpty()) {
        GestorAlertas.mostrarError(this, "Todos los campos son obligatorios");
        return;
      }

      if (!Validaciones.validarCédula(cedula)) {
        GestorAlertas.mostrarError(this, "Cédula inválida");
        return;
      }

      if (gestorSocios.existeCedula(cedula)) {
        GestorAlertas.mostrarError(this, "Cédula ya está registrada en el sistema");
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

      Socio nuevoSocio = new Socio(cedula, nombres, apellidos, direccion, telefono, correo);
      nuevoSocio.setUsuarioRegistro(usuarioActual.getUsername());

      if (gestorSocios.crearSocio(nuevoSocio)) {
        GestorAlertas.mostrarExito(this, "Socio registrado exitosamente");

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

    String cedulaActual = (String) modeloTabla.getValueAt(fila, 1);
    Socio socio = gestorSocios.buscarPorCedula(cedulaActual);

    if (socio == null) {
      GestorAlertas.mostrarError(this, "Error al cargar los datos del socio");
      return;
    }

    JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblCedula = new JLabel("Cédula:");
    JTextField txtCedula = new JTextField(socio.getCedula());
    txtCedula.setEditable(false);
    txtCedula.setBackground(Color.LIGHT_GRAY);

    JLabel lblNombres = new JLabel("Nombres:");
    JTextField txtNombres = new JTextField(socio.getNombres());
    txtNombres.setEditable(false);
    txtNombres.setBackground(Color.LIGHT_GRAY);

    JLabel lblApellidos = new JLabel("Apellidos:");
    JTextField txtApellidos = new JTextField(socio.getApellidos());
    txtApellidos.setEditable(false);
    txtApellidos.setBackground(Color.LIGHT_GRAY);

    JLabel lblTelefono = new JLabel("Teléfono:");
    JTextField txtTelefono = new JTextField(socio.getTelefono());

    JLabel lblCorreo = new JLabel("Correo:");
    JTextField txtCorreo = new JTextField(socio.getCorreo());

    JLabel lblDireccion = new JLabel("Dirección:");
    JTextField txtDireccion = new JTextField(socio.getDireccion());

    panel.add(lblCedula); panel.add(txtCedula);
    panel.add(lblNombres); panel.add(txtNombres);
    panel.add(lblApellidos); panel.add(txtApellidos);
    panel.add(lblTelefono); panel.add(txtTelefono);
    panel.add(lblCorreo); panel.add(txtCorreo);
    panel.add(lblDireccion); panel.add(txtDireccion);

    int resultado = JOptionPane.showConfirmDialog(this, panel,
      "Actualizar Datos", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (resultado == JOptionPane.OK_OPTION) {
      String telefono = txtTelefono.getText().trim();
      String correo = txtCorreo.getText().trim();
      String direccion = txtDireccion.getText().trim();

      if (telefono.isEmpty() || correo.isEmpty() || direccion.isEmpty()) {
        GestorAlertas.mostrarError(this, "Los campos de contacto son obligatorios");
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

      socio.setTelefono(telefono);
      socio.setCorreo(correo);
      socio.setDireccion(direccion);

      if (gestorSocios.actualizarSocio(socio)) {
        GestorAlertas.mostrarExito(this, "Datos actualizados exitosamente");
        cargarSocios();
      } else {
        GestorAlertas.mostrarError(this, "Error al actualizar los datos");
      }
    }
  }

  private void consultarSocio() {
    String busqueda = JOptionPane.showInputDialog(
      this,
      "Ingrese Cédula o nombre del socio:",
      "Consultar Socio",
      JOptionPane.PLAIN_MESSAGE
    );

    if (busqueda == null || busqueda.trim().isEmpty()) {
      return;
    }

    busqueda = busqueda.trim();

    if (busqueda.length() == 10 && busqueda.matches("^[0-9]+$")) {
      Socio socio = gestorSocios.buscarPorCedula(busqueda);
      if (socio != null) {
        mostrarDetallesSocio(socio);
      } else {
        GestorAlertas.mostrarError(this, "No se encontró un socio con esa Cédula");
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
    detalles.append("Cédula: ").append(socio.getCedula()).append("\n");
    detalles.append("Nombres: ").append(socio.getNombres()).append("\n");
    detalles.append("Apellidos: ").append(socio.getApellidos()).append("\n");
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

    JTextArea textArea = new JTextArea(detalles.toString());
    textArea.setEditable(false);
    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(400, 300));

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
      opciones[i] = resultados.get(i).getNombres() + " " + resultados.get(i).getApellidos() +
        " (" + resultados.get(i).getCedula() + ")";
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
        String opcion = socio.getNombres() + " " + socio.getApellidos() +
          " (" + socio.getCedula() + ")";
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
    String nombres = (String) modeloTabla.getValueAt(fila, 2);
    String apellidos = (String) modeloTabla.getValueAt(fila, 3);

    String estadoActual = (String) modeloTabla.getValueAt(fila, 7);

    boolean nuevoEstado = estadoActual.equals("Inactivo");
    String accion = nuevoEstado ? "activar" : "desactivar";
    String nombreCompleto = nombres + " " + apellidos;

    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Está seguro de " + accion + " el socio " + nombreCompleto + "?",
      "Cambiar Estado",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (confirmacion == JOptionPane.YES_OPTION) {
      if (gestorSocios.cambiarEstado(idSocio, nuevoEstado)) {
        GestorAlertas.mostrarExito(this, "Estado actualizado exitosamente");
        cargarSocios();
      } else {
        GestorAlertas.mostrarError(this, "Error al cambiar el estado");
      }
    }
  }

  private void eliminarSocio() {
    int fila = tablaSocios.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un socio de la tabla");
      return;
    }

    int idSocio = (int) modeloTabla.getValueAt(fila, 0);
    String nombres = (String) modeloTabla.getValueAt(fila, 2);
    String apellidos = (String) modeloTabla.getValueAt(fila, 3);
    String nombreCompleto = nombres + " " + apellidos;

    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Está seguro de eliminar el socio " + nombreCompleto + " ?",
      "Eliminar Socio",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.PLAIN_MESSAGE //JOptionPane.WARNING_MESSAGE
    );

    if (confirmacion == JOptionPane.YES_OPTION) {
      if (gestorSocios.eliminarSocio(idSocio)) {
        GestorAlertas.mostrarExito(this, "Socio eliminado exitosamente");
        cargarSocios();
      } else {
        GestorAlertas.mostrarError(this, "Error al eliminar el socio");
      }
    }
  }
}
