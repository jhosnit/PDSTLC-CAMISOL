package Presentación.Módulos;

import Presentación.Recursos.Botón;
import Presentación.Recursos.GestorAlertas;
import Presentación.Recursos.GestorUsuarios;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;

public class Administración extends JPanel {

  private JTable tablaUsuarios;
  private DefaultTableModel modeloTabla;
  private double iva = 15.0;

  public Administración() {
    inicializarComponentes();
    cargarUsuarios();
  }

  private void inicializarComponentes() {

    setLayout(new BorderLayout());
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel panelHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelHerramientas.setOpaque(false);

    Botón btnCambiarClave = new Botón("Cambiar Contraseña", new Color(70, 128, 139));
    Botón btnIVA = new Botón("Configurar IVA", new Color(59, 130, 246));
    Botón btnBackup = new Botón("Respaldar Sistema", new Color(249, 115, 22));
    Botón btnRestore = new Botón("Restaurar Sistema", new Color(239, 68, 68));
    Botón btnActualizar = new Botón("Actualizar Tabla", new Color(34, 197, 94));

    Dimension dim = new Dimension(180, 40);
    btnCambiarClave.setPreferredSize(dim);
    btnIVA.setPreferredSize(dim);
    btnBackup.setPreferredSize(dim);
    btnRestore.setPreferredSize(dim);
    btnActualizar.setPreferredSize(dim);

    panelHerramientas.add(btnCambiarClave);
    panelHerramientas.add(btnIVA);
    panelHerramientas.add(btnBackup);
    panelHerramientas.add(btnRestore);
    panelHerramientas.add(btnActualizar);

    crearTabla();

    JScrollPane scroll = new JScrollPane(tablaUsuarios);
    scroll.getViewport().setBackground(new Color(31, 41, 55));
    scroll.setBorder(new LineBorder(new Color(55, 65, 81), 1));

    add(panelHerramientas, BorderLayout.NORTH);
    add(scroll, BorderLayout.CENTER);

    btnIVA.addActionListener(e -> configurarIVA());
    btnCambiarClave.addActionListener(e -> cambiarContraseña());
    btnBackup.addActionListener(e -> respaldarSistema());
    btnRestore.addActionListener(e -> restaurarSistema());
    btnActualizar.addActionListener(e -> cargarUsuarios());
  }

  private void crearTabla() {

    String[] columnas = {
      "ID", "Usuario", "Rol", "Último Acceso", "Intentos Fallidos"
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
    // Limpiar tabla
    modeloTabla.setRowCount(0);

    // Cargar usuarios desde el GestorUsuarios
    for (GestorUsuarios.Usuario usuario : GestorUsuarios.obtenerInstancia().obtenerTodosLosUsuarios()) {
      modeloTabla.addRow(new Object[]{
        usuario.getId(),
        usuario.getUsuario(),
        usuario.getRol(),
        usuario.getUltimoAcceso(),
        usuario.getIntentosFallidos()
      });
    }
  }

  // ================= FUNCIONES =================

  private void configurarIVA() {
    String valor = JOptionPane.showInputDialog(
      this,
      "Ingrese el IVA (%)",
      iva
    );

    if (valor != null) {
      try {
        iva = Double.parseDouble(valor);
        GestorAlertas.mostrarExito(this, "IVA configurado en " + iva + "%");
      } catch (NumberFormatException e) {
        GestorAlertas.mostrarError(this, "Valor inválido");
      }
    }
  }

  private void cambiarContraseña() {
    int fila = tablaUsuarios.getSelectedRow();
    if (fila < 0) {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un usuario de la tabla");
      return;
    }

    // Obtener el usuario seleccionado
    String nombreUsuario = (String) modeloTabla.getValueAt(fila, 1);

    // Crear panel personalizado para el diálogo
    JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel lblNueva = new JLabel("Nueva contraseña:");
    JPasswordField txtNueva = new JPasswordField();

    JLabel lblConfirmar = new JLabel("Confirmar contraseña:");
    JPasswordField txtConfirmar = new JPasswordField();

    panel.add(lblNueva);
    panel.add(txtNueva);
    panel.add(lblConfirmar);
    panel.add(txtConfirmar);

    int resultado = JOptionPane.showConfirmDialog(
      this,
      panel,
      "Cambiar contraseña para: " + nombreUsuario,
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );

    if (resultado == JOptionPane.OK_OPTION) {
      String nuevaContraseña = new String(txtNueva.getPassword());
      String confirmarContraseña = new String(txtConfirmar.getPassword());

      // Validaciones
      if (nuevaContraseña.isEmpty()) {
        GestorAlertas.mostrarError(this, "La contraseña no puede estar vacía");
        return;
      }

      if (nuevaContraseña.length() < 4) {
        GestorAlertas.mostrarError(this, "La contraseña debe tener al menos 4 caracteres");
        return;
      }

      if (!nuevaContraseña.equals(confirmarContraseña)) {
        GestorAlertas.mostrarError(this, "Las contraseñas no coinciden");
        return;
      }

      // Cambiar la contraseña usando el GestorUsuarios
      if (GestorUsuarios.obtenerInstancia().cambiarContraseña(nombreUsuario, nuevaContraseña)) {
        GestorAlertas.mostrarExito(
          this,
          "Contraseña actualizada correctamente para: " + nombreUsuario
        );
      } else {
        GestorAlertas.mostrarError(this, "Error al actualizar la contraseña");
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
      JOptionPane.WARNING_MESSAGE
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
      // Recargar usuarios después de restaurar
      cargarUsuarios();
    }
  }
}
