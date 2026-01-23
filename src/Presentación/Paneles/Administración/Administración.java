package Presentación.Paneles.Administración;

import Presentación.Recursos.Botón;
import Logica.Gestores.GestorAlertas;
import Logica.Gestores.GestorUsuarios;
import Logica.Gestores.GestorParámetros;
import Logica.Entidades.Usuario;
import Logica.Entidades.Parámetros;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class Administración extends JPanel {

  private JTable tablaUsuarios;
  private DefaultTableModel modeloTabla;

  private GestorUsuarios gestorUsuarios;
  private GestorParámetros gestorParámetros;  // NUEVO   // NUEVO
  private Usuario usuarioActual;

  public Administración(Usuario usuario) {
    this.usuarioActual = usuario;
    gestorUsuarios = new GestorUsuarios();
    gestorParámetros = new GestorParámetros();    // NUEVO
    inicializarComponentes();
    cargarUsuarios();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout());
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel panelHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelHerramientas.setOpaque(false);

    Botón botónCrearUsuario = new Botón("Crear Usuario", new Color(34, 197, 94));
    Botón botónCambiarClave = new Botón("Cambiar Contraseña", new Color(70, 128, 139));
    Botón botónCambiarEstado = new Botón("Cambiar Estado", new Color(251, 146, 60));
    Botón botónParámetros = new Botón("Configurar Parámetros", new Color(59, 130, 246));
    Botón botónBackup = new Botón("Respaldar Sistema", new Color(249, 115, 22));
    Botón botónRestore = new Botón("Restaurar Sistema", new Color(239, 68, 68));
    Botón botónActualizar = new Botón("Actualizar Tabla", new Color(34, 197, 94));

    Dimension dim = new Dimension(180, 40);
    botónCrearUsuario.setPreferredSize(dim);
    botónCambiarClave.setPreferredSize(dim);
    botónCambiarEstado.setPreferredSize(dim);
    botónParámetros.setPreferredSize(dim);
    botónBackup.setPreferredSize(dim);
    botónRestore.setPreferredSize(dim);
    botónActualizar.setPreferredSize(dim);

    panelHerramientas.add(botónCrearUsuario);
    panelHerramientas.add(botónCambiarClave);
    panelHerramientas.add(botónCambiarEstado);
    panelHerramientas.add(botónParámetros);
    panelHerramientas.add(botónBackup);
    panelHerramientas.add(botónRestore);
    panelHerramientas.add(botónActualizar);


    crearTabla();

    JScrollPane scroll = new JScrollPane(tablaUsuarios);
    scroll.getViewport().setBackground(new Color(31, 41, 55));
    scroll.setBorder(new LineBorder(new Color(55, 65, 81), 1));

    add(panelHerramientas, BorderLayout.NORTH);
    add(scroll, BorderLayout.CENTER);

    botónCrearUsuario.addActionListener(e -> crearNuevoUsuario());
    botónCambiarClave.addActionListener(e -> cambiarContraseña());
    botónCambiarEstado.addActionListener(e -> cambiarEstadoUsuario());
    botónParámetros.addActionListener(e -> configurarParámetros());
    botónBackup.addActionListener(e -> respaldarSistema());
    botónRestore.addActionListener(e -> restaurarSistema());
    botónActualizar.addActionListener(e -> cargarUsuarios());
  }

  private void crearTabla() {
    String[] columnas = {
      "ID", "Usuario", "Nombre y Apellido", "Rol", "Estado", "Ultimo acceso"
    };

    modeloTabla = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tablaUsuarios = new JTable(modeloTabla);
    tablaUsuarios.setBackground(new Color(31, 41, 55));
    tablaUsuarios.setForeground(Color.WHITE);
    tablaUsuarios.setGridColor(new Color(55, 65, 81));
    tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tablaUsuarios.setRowHeight(30);
    tablaUsuarios.setSelectionBackground(new Color(75, 85, 99));
    tablaUsuarios.setSelectionForeground(Color.WHITE);

    JTableHeader header = tablaUsuarios.getTableHeader();
    header.setBackground(new Color(243, 244, 246));
    header.setForeground(new Color(31, 41, 55));
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setPreferredSize(new Dimension(0, 35));

    DefaultTableCellRenderer render = new DefaultTableCellRenderer();
    render.setBackground(new Color(31, 41, 55));
    render.setForeground(Color.WHITE);
    render.setHorizontalAlignment(SwingConstants.LEFT);

    for (int i = 0; i < tablaUsuarios.getColumnCount(); i++) {
      tablaUsuarios.getColumnModel().getColumn(i).setCellRenderer(render);
    }
    tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
  }

  private void cargarUsuarios() {
    modeloTabla.setRowCount(0);

    List<Usuario> lista = gestorUsuarios.listarUsuarios();

    for (Usuario usuario : lista) {
      modeloTabla.addRow(new Object[]{
        usuario.getId(),
        usuario.getUsername(),
        usuario.getNombre() + " " + usuario.getApellido(),
        usuario.getRol(),
        usuario.isEstado() ? "Activo" : "Inactivo",
        usuario.getUltimoAcceso()
      });
    }
  }

  private void crearNuevoUsuario() {
    JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblUsername = new JLabel("Usuario:");
    JTextField txtUsername = new JTextField();

    JLabel lblPassword = new JLabel("Contraseña:");
    JPasswordField txtPassword = new JPasswordField();

    JLabel lblNombre = new JLabel("Nombre:");
    JTextField txtNombre = new JTextField();

    JLabel lblApellido = new JLabel("Apellido:");
    JTextField txtApellido = new JTextField();

    JLabel lblRol = new JLabel("Rol:");
    JComboBox<String> cmbRol = new JComboBox<>(new String[]{"GERENTE", "SECRETARIA"});

    panel.add(lblUsername);
    panel.add(txtUsername);
    panel.add(lblPassword);
    panel.add(txtPassword);
    panel.add(lblNombre);
    panel.add(txtNombre);
    panel.add(lblApellido);
    panel.add(txtApellido);
    panel.add(lblRol);
    panel.add(cmbRol);

    int resultado = JOptionPane.showConfirmDialog(
      this,
      panel,
      "Crear Nuevo Usuario",
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (resultado == JOptionPane.OK_OPTION) {
      String username = txtUsername.getText().trim();
      String password = new String(txtPassword.getPassword()).trim();
      String nombre = txtNombre.getText().trim();
      String apellido = txtApellido.getText().trim();
      String rol = (String) cmbRol.getSelectedItem();

      // Validaciones
      if (username.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
        GestorAlertas.mostrarError(this, "Todos los campos son obligatorios");
        return;
      }

      if (password.length() < 4) {
        GestorAlertas.mostrarError(this, "La contraseña debe tener al menos 4 caracteres");
        return;
      }

      if (gestorUsuarios.existeUsername(username)) {
        GestorAlertas.mostrarError(this, "El nombre de usuario ya existe");
        return;
      }

      // Crear usuario
      Usuario nuevoUsuario = new Usuario();
      nuevoUsuario.setUsername(username);
      nuevoUsuario.setPassword(password);
      nuevoUsuario.setNombre(nombre);
      nuevoUsuario.setApellido(apellido);
      nuevoUsuario.setRol(rol);

      if (gestorUsuarios.crearUsuario(nuevoUsuario)) {
        GestorAlertas.mostrarExito(this, "Usuario creado exitosamente");
        cargarUsuarios();
      } else {
        GestorAlertas.mostrarError(this, "Error al crear el usuario");
      }
    }
  }

  private void cambiarContraseña() {
    int fila = tablaUsuarios.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un usuario de la tabla");
      return;
    }

    String nombreUsuario = (String) modeloTabla.getValueAt(fila, 1);

    JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
    JLabel lblNueva = new JLabel("Nueva contraseña:");
    JPasswordField txtNueva = new JPasswordField();
    JLabel lblConfirmar = new JLabel("Confirmar contraseña:");
    JPasswordField txtConfirmar = new JPasswordField();
    panel.add(lblNueva);
    panel.add(txtNueva);
    panel.add(lblConfirmar);
    panel.add(txtConfirmar);

    int resultado = JOptionPane.showConfirmDialog(this, panel,
      "Cambiar contraseña para: " + nombreUsuario,
      JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (resultado == JOptionPane.OK_OPTION) {
      String nuevaContrasena = new String(txtNueva.getPassword());
      String confirmarContrasena = new String(txtConfirmar.getPassword());

      if (nuevaContrasena.isEmpty() || nuevaContrasena.length() < 4 || !nuevaContrasena.equals(confirmarContrasena)) {
        GestorAlertas.mostrarError(this, "Contraseña inválida o no coinciden");
        return;
      }

      if (gestorUsuarios.cambiarContraseña(nombreUsuario, nuevaContrasena)) {
        GestorAlertas.mostrarExito(this, "Contraseña actualizada correctamente");
      } else {
        GestorAlertas.mostrarError(this, "Error al actualizar la contraseña");
      }
    }
  }

  private void cambiarEstadoUsuario() {
    int fila = tablaUsuarios.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un usuario de la tabla");
      return;
    }

    int idUsuario = (int) modeloTabla.getValueAt(fila, 0);
    String nombreUsuario = (String) modeloTabla.getValueAt(fila, 1);
    String estadoActual = (String) modeloTabla.getValueAt(fila, 4);

    boolean nuevoEstado = estadoActual.equals("Inactivo");
    String accion = nuevoEstado ? "activar" : "desactivar";

    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Está seguro de " + accion + " al usuario " + nombreUsuario + "?",
      "Confirmar acción",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (confirmacion == JOptionPane.YES_OPTION) {
      if (gestorUsuarios.cambiarEstadoUsuario(idUsuario, nuevoEstado)) {
        GestorAlertas.mostrarExito(this, "Estado actualizado correctamente");
        cargarUsuarios();
      } else {
        GestorAlertas.mostrarError(this, "Error al actualizar el estado");
      }
    }
  }

  private void configurarParámetros() {

    Parámetros params = gestorParámetros.obtenerParámetros();

    if (params == null) {
      GestorAlertas.mostrarError(this, "Error al cargar los parámetros del sistema");
      return;
    }

    // Crear panel con GridLayout igual que crear usuario
    JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblIVA = new JLabel("IVA (%):");
    JTextField txtIVA = new JTextField(String.format("%.2f", params.getIva()));

    JLabel lblUltimaModif = new JLabel("Última Modificación:");
    String textoModif = (params.getUltimaModificacion() != null)
      ? params.getUltimaModificacion().substring(0, 19)
      : "Sin modificaciones";
    JLabel lblModifValor = new JLabel(textoModif);
/*
    JLabel lblUsuarioModif = new JLabel("Modificado por:");
    String textoUsuario = params.getUsuarioModificacion();
    JLabel lblUsuarioValor = new JLabel(textoUsuario);
*/
    panel.add(lblIVA);
    panel.add(txtIVA);
    panel.add(lblUltimaModif);
    panel.add(lblModifValor);
//    panel.add(lblUsuarioModif);
//    panel.add(lblUsuarioValor);

    int resultado = JOptionPane.showConfirmDialog(this, panel, "Configurar Parámetros del Sistema", JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE);

    if (resultado == JOptionPane.OK_OPTION) {
      String valorIva = txtIVA.getText().trim();

      // Validaciones
      if (valorIva.isEmpty()) {
        GestorAlertas.mostrarError(this, "El IVA es obligatorio");
        return;
      }

      try {
        double iva = Double.parseDouble(valorIva);

        if (iva < 13 || iva > 20) {
          GestorAlertas.mostrarError(this, "El IVA debe estar entre 13 y 20");
          return;
        }

        Parámetros nuevosParams = new Parámetros();
        nuevosParams.setIva(iva);

        if (gestorParámetros.actualizarParámetros(nuevosParams, usuarioActual.getUsername())) {
          GestorAlertas.mostrarExito(this, "IVA actualizado correctamente a " + String.format("%.2f%%", iva));
        } else {
          GestorAlertas.mostrarError(this, "Error al actualizar el IVA");
        }

      } catch (NumberFormatException e) {
        GestorAlertas.mostrarError(this, "Valor inválido. Ingrese un número válido.");
      }
    }
  }

  private void respaldarSistema() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Guardar respaldo del sistema");
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      File archivo = chooser.getSelectedFile();
      GestorAlertas.mostrarExito(
        this,
        "Respaldo generado:\n" + archivo.getAbsolutePath()
      );
    }
  }

  private void restaurarSistema() {
    int confirmacion = JOptionPane.showConfirmDialog(
      this,
      "¿Está seguro de restaurar el sistema?\nEsto sobrescribirá los datos actuales.",
      "Confirmar restauración",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (confirmacion != JOptionPane.YES_OPTION) {
      return;
    }

    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Seleccionar archivo de respaldo");
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File archivo = chooser.getSelectedFile();
      GestorAlertas.mostrarAdvertencia(
        this,
        "Sistema restaurado desde:\n" + archivo.getAbsolutePath()
      );

      cargarUsuarios();
    }
  }
}
