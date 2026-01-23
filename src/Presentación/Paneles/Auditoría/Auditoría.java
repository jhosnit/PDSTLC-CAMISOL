package Presentación.Paneles.Auditoría;

import Presentación.Recursos.Botón;
import Logica.Gestores.GestorAlertas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Auditoría extends JPanel {

  private static JTable tablaLogs;
  private static DefaultTableModel modeloTabla;
  private TableRowSorter<DefaultTableModel> sorter;
  private static int intentosFallidos = 0;
  private static Auditoría auditoría;

  public static Auditoría obtenerInstancia() {
    if (auditoría == null) {
      auditoría = new Auditoría();
    }
    return auditoría;
  }

  public Auditoría() {
    inicializarComponentes();
    cargarEjemplos();
  }

  private void inicializarComponentes() {

    setLayout(new BorderLayout());
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel panelHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelHerramientas.setOpaque(false);

    Botón btnConsultar = new Botón("Consultar Actividades", new Color(70, 128, 139));
    Botón btnAlertas = new Botón("Alertas de Seguridad", new Color(239, 68, 68));
    Botón btnExportar = new Botón("Exportar Informe", new Color(249, 115, 22));

    btnConsultar.setPreferredSize(new Dimension(170, 40));
    btnAlertas.setPreferredSize(new Dimension(170, 40));
    btnExportar.setPreferredSize(new Dimension(170, 40));

    panelHerramientas.add(btnConsultar);
    panelHerramientas.add(btnAlertas);
    panelHerramientas.add(btnExportar);

    crearTabla();

    JScrollPane scrollTabla = new JScrollPane(tablaLogs);
    scrollTabla.getViewport().setBackground(new Color(31, 41, 55));
    scrollTabla.setBorder(new LineBorder(new Color(55, 65, 81), 1));

    add(panelHerramientas, BorderLayout.NORTH);
    add(scrollTabla, BorderLayout.CENTER);

    sorter = new TableRowSorter<>(modeloTabla);
    tablaLogs.setRowSorter(sorter);

    btnConsultar.addActionListener(e -> {
      if (solicitarContraseña()) {
        abrirDialogoConsulta();
      }
    });

    btnAlertas.addActionListener(e -> {
      if (solicitarContraseña()) {
        mostrarAlertas();
      }
    });

    btnExportar.addActionListener(e -> {
      if (solicitarContraseña()) {
        exportarCSV();
      }
    });

    tablaLogs.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          mostrarDetalle();
        }
      }
    });
  }

  private void crearTabla() {

    String[] columnas = {
      "ID", "Fecha - Hora", "Usuario", "Módulo", "Acción"
    };

    modeloTabla = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tablaLogs = new JTable(modeloTabla);
    tablaLogs.setBackground(new Color(31, 41, 55));
    tablaLogs.setForeground(Color.WHITE);
    tablaLogs.setGridColor(new Color(55, 65, 81));
    tablaLogs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tablaLogs.setRowHeight(30);
    tablaLogs.setSelectionBackground(new Color(75, 85, 99));
    tablaLogs.setSelectionForeground(Color.WHITE);

    JTableHeader header = tablaLogs.getTableHeader();
    header.setBackground(new Color(243, 244, 246));
    header.setForeground(new Color(31, 41, 55));
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setPreferredSize(new Dimension(0, 35));

    DefaultTableCellRenderer render = new DefaultTableCellRenderer();
    render.setBackground(new Color(31, 41, 55));
    render.setForeground(Color.WHITE);
    render.setHorizontalAlignment(SwingConstants.LEFT);

    for (int i = 0; i < tablaLogs.getColumnCount(); i++) {
      tablaLogs.getColumnModel().getColumn(i).setCellRenderer(render);
    }

    tablaLogs.getColumnModel().getColumn(0).setPreferredWidth(50);
    tablaLogs.getColumnModel().getColumn(1).setPreferredWidth(150);
  }

  private void abrirDialogoConsulta() {

    JDialog dialogo = new JDialog(
      (JFrame) SwingUtilities.getWindowAncestor(this),
      "Consultar Actividades",
      true
    );

    dialogo.setSize(500, 140);
    dialogo.setLocationRelativeTo(this);
    dialogo.setLayout(new BorderLayout());

    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JComboBox<String> comboFiltro = new JComboBox<>(new String[]{
      "Fecha", "Usuario", "Módulo"
    });

    JTextField txtValor = new JTextField();
    txtValor.setPreferredSize(new Dimension(180, 30));

    Botón btnBuscar = new Botón("Buscar", new Color(59, 130, 246));
    btnBuscar.setPreferredSize(new Dimension(100, 30));

    panel.add(new JLabel("Buscar por:"));
    panel.add(comboFiltro);
    panel.add(txtValor);
    panel.add(btnBuscar);

    dialogo.add(panel, BorderLayout.CENTER);

    btnBuscar.addActionListener(e -> {

      String valor = txtValor.getText().trim();

      if (valor.isEmpty()) {
        sorter.setRowFilter(null);
        dialogo.dispose();
        return;
      }

      int columna = switch (comboFiltro.getSelectedIndex()) {
        case 0 -> 1;
        case 1 -> 2;
        case 2 -> 3;
        default -> -1;
      };

      sorter.setRowFilter(RowFilter.regexFilter(valor, columna));
      dialogo.dispose();
    });

    dialogo.setVisible(true);
  }

  private void mostrarDetalle() {

    int fila = tablaLogs.getSelectedRow();
    if (fila < 0) return;

    fila = tablaLogs.convertRowIndexToModel(fila);

    String mensaje =
      "Fecha - Hora: " + modeloTabla.getValueAt(fila, 1) + "\n" +
        "Usuario: " + modeloTabla.getValueAt(fila, 2) + "\n" +
        "Módulo: " + modeloTabla.getValueAt(fila, 3) + "\n" +
        "Acción: " + modeloTabla.getValueAt(fila, 4);

    JOptionPane.showMessageDialog(
      this,
      mensaje,
      "Detalle de Auditoría",
      JOptionPane.INFORMATION_MESSAGE
    );
  }

  private void mostrarAlertas() {

    StringBuilder alertas = new StringBuilder();

    for (int i = 0; i < modeloTabla.getRowCount(); i++) {
      String accion = modeloTabla.getValueAt(i, 4).toString();
      if (accion.equals("Inicio de sesión fallido")) {
        alertas.append(modeloTabla.getValueAt(i, 1))
          .append(" - ")
          .append(modeloTabla.getValueAt(i, 2))
          .append("\n");
      }
    }

    JOptionPane.showMessageDialog(
      this,
      alertas.length() == 0 ? "No hay alertas registradas" : alertas.toString(),
      "Alertas de Seguridad",
      JOptionPane.WARNING_MESSAGE
    );
  }

  private void exportarCSV() {

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    JFileChooser chooser = new JFileChooser();
    chooser.setSelectedFile(
      new File("informe_auditoria_" + LocalDateTime.now().format(fmt) + ".csv")
    );

    if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

      try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {

        for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
          writer.append(modeloTabla.getColumnName(i)).append(",");
        }
        writer.append("\n");

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
          for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
            writer.append(modeloTabla.getValueAt(i, j).toString()).append(",");
          }
          writer.append("\n");
        }

        GestorAlertas.mostrarExito(this, "Informe exportado correctamente");

      } catch (IOException e) {
        GestorAlertas.mostrarError(this, "Error al exportar informe");
      }
    }
  }

  public static void registrarAccion(
    DefaultTableModel modelo,
    String usuario,
    String modulo,
    String accion
  ) {

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");

    modelo.addRow(new Object[]{
      modelo.getRowCount() + 1,
      LocalDateTime.now().format(fmt),
      usuario,
      modulo,
      accion
    });
  }

  public void registrarAccionPublica(String usuario, String modulo, String accion) {
    registrarAccion(modeloTabla, usuario, modulo, accion);
  }

  public void loginFallido(String usuario) {

    intentosFallidos++;

    registrarAccion(modeloTabla, usuario, "Auditoría", "Inicio de sesión fallido"
    );

    if (intentosFallidos >= 3) {
      GestorAlertas.mostrarAdvertencia(
        this,
        "3 intentos fallidos consecutivos"
      );
    }
  }

  public void loginExitoso() {
    intentosFallidos = 0;
  }

  private void cargarEjemplos() {

    registrarAccion(modeloTabla, "admin", "Servicios", "CREAR");
    registrarAccion(modeloTabla, "admin", "Clientes", "CREAR");
    registrarAccion(modeloTabla, "secre", "Clientes", "MODIFICAR");

    //registrarAccion(modeloTabla, "usua", "Auditoría", "LOGIN_FALLIDO");
    //registrarAccion(modeloTabla, "usua", "Auditoría", "LOGIN_FALLIDO");
    //registrarAccion(modeloTabla, "usua", "Auditoría", "LOGIN_FALLIDO");

  }

  public static boolean solicitarContraseña() {

    JPasswordField pf = new JPasswordField();
    int okCxl = JOptionPane.showConfirmDialog(
      auditoría, pf, "Ingrese contraseña de auditoría", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
    );

    if (okCxl == JOptionPane.OK_OPTION) {
      String password = new String(pf.getPassword());
      if (password.equals("admin")) {
        return true;
      } else {
        GestorAlertas.mostrarError(auditoría, "Contraseña incorrecta");
        return false;
      }
    }
    return false;
  }

}
