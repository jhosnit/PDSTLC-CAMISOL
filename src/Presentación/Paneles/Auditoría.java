package Presentación.Paneles;

import Presentación.Recursos.Botón;
import Logica.Gestores.GestorAlertas;
import Logica.Gestores.GestorAuditoria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Auditoría extends JPanel {

  private static JTable tablaLogs;
  private static DefaultTableModel modeloTabla;
  private static Auditoría instancia;

  public static Auditoría obtenerInstancia() {
    if (instancia == null) {
      instancia = new Auditoría();
    }
    return instancia;
  }

  public Auditoría() {
    inicializarComponentes();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout());
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel panelHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelHerramientas.setOpaque(false);

    Botón btnExportar = new Botón("Exportar Informe", new Color(249, 115, 22));
    btnExportar.setPreferredSize(new Dimension(170, 40));
    panelHerramientas.add(btnExportar);

    crearTabla();
    cargarDatosDesdeBD();

    JScrollPane scrollTabla = new JScrollPane(tablaLogs);
    scrollTabla.getViewport().setBackground(new Color(31, 41, 55));
    scrollTabla.setBorder(new LineBorder(new Color(55, 65, 81), 1));

    add(panelHerramientas, BorderLayout.NORTH);
    add(scrollTabla, BorderLayout.CENTER);

    btnExportar.addActionListener(e -> exportarCSV());
  }

  private void crearTabla() {
    String[] columnas = {
      "ID", "Usuario", "Nombre y Apellido", "Acción", "Fecha"
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

  private void cargarDatosDesdeBD() {
    modeloTabla.setRowCount(0);
    GestorAuditoria gestor = new Logica.Gestores.GestorAuditoria();
    java.util.List<Object[]> eventos = gestor.listarEventos();

    for (Object[] fila : eventos) {
      modeloTabla.addRow(fila);
    }
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
          writer.append(modeloTabla.getColumnName(i));
          if (i < modeloTabla.getColumnCount() - 1) {
            writer.append(",");
          }
        }
        writer.append("\n");

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
          for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
            writer.append(modeloTabla.getValueAt(i, j).toString());
            if (j < modeloTabla.getColumnCount() - 1) {
              writer.append(",");
            }
          }
          writer.append("\n");
        }

        GestorAlertas.mostrarExito(this, "Informe exportado exitosamente");

      } catch (IOException e) {
        GestorAlertas.mostrarError(this, "Error al exportar informe: " + e.getMessage());
      }
    }
  }

  public static void registrarAccion(String usuario, String nombre, String accion) {
    if (instancia == null) {
      instancia = new Auditoría();
    }

    GestorAuditoria gestor = new GestorAuditoria();

    int idGenerado = gestor.registrarEvento(usuario, nombre, accion);

    if (idGenerado > 0) {
      DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

      modeloTabla.insertRow(0, new Object[]{
        idGenerado,
        usuario,
        nombre,
        accion,
        LocalDateTime.now().format(fmt)
      });
    }
  }

  public static boolean solicitarContraseña() {

    JPasswordField pf = new JPasswordField();
    int okCxl = JOptionPane.showConfirmDialog(
      instancia, pf, "Contraseña", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
    );

    if (okCxl == JOptionPane.OK_OPTION) {
      String password = new String(pf.getPassword());
      if (password.equals("admin123")) {
        return true;
      } else {
        GestorAlertas.mostrarError(instancia, "Contraseña incorrecta");
        return false;
      }
    }
    return false;
  }

}
