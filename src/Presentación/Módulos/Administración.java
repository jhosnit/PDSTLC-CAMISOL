package Presentación.Módulos;

import Presentación.Recursos.Botón;
import Presentación.Recursos.GestorAlertas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Administración extends JPanel {

  private JTable tablaUsuarios;
  private DefaultTableModel modeloTabla;

  public Administración() {
    inicializarComponentes();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout(0, 20));
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(20, 20, 20, 20));

    JPanel panelHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
    panelHerramientas.setOpaque(false);

    Botón btnNuevo = new Botón("Registrar Usuario", new Color(37, 99, 235));
    Botón btnEditar = new Botón("Actualizar Usuario", new Color(234, 177, 0));
    Botón btnEstado = new Botón("Cambiar Estado", new Color(16, 185, 129));
    Botón btnBackup = new Botón("Respaldar Sistema", new Color(59, 130, 246));
    Botón btnRestore = new Botón("Restaurar Sistema", new Color(239, 68, 68));

    panelHerramientas.add(btnNuevo);
    panelHerramientas.add(btnEditar);
    panelHerramientas.add(btnEstado);
    panelHerramientas.add(btnBackup);
    panelHerramientas.add(btnRestore);

    crearTabla();
    JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);
    scrollTabla.getViewport().setBackground(new Color(31, 41, 55));
    scrollTabla.setBorder(new LineBorder(new Color(55, 65, 81), 1));

    add(panelHerramientas, BorderLayout.NORTH);
    add(scrollTabla, BorderLayout.CENTER);

    btnNuevo.addActionListener(e ->
      GestorAlertas.mostrarExito(this, "Formulario de registro de usuario abierto")
    );

    btnEditar.addActionListener(e ->
      GestorAlertas.mostrarExito(this, "Formulario de actualización de usuario abierto")
    );

    btnEstado.addActionListener(e -> cambiarEstadoUsuario());

    btnBackup.addActionListener(e ->
      GestorAlertas.mostrarExito(this, "Respaldo del sistema generado correctamente")
    );

    btnRestore.addActionListener(e ->
      GestorAlertas.mostrarAdvertencia(this, "Restauración del sistema iniciada")
    );
  }

  private void crearTabla() {
    String[] columnas = {
      "ID", "Cédula", "Nombre Completo", "Usuario",
      "Rol", "Estado", "Último Acceso"
    };

    Object[][] datos = {
      {"1", "1727964197", "Ángel Quishpe", "admin", "Gerente", "Activo", "20/12/2025 - 10:00"},
      {"2", "1712345678", "Evelyn Cumbal", "secre", "Secretaria", "Activo", "20/12/2025 - 08:30"},
      {"3", "1798765432", "Luis Cárdenas", "cond01", "Conductor", "Inactivo", "19/12/2025 - 14:00"}
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
    tablaUsuarios.setRowHeight(30);
    tablaUsuarios.setShowVerticalLines(false);
    tablaUsuarios.setGridColor(new Color(55, 65, 81));

    JTableHeader header = tablaUsuarios.getTableHeader();
    header.setBackground(new Color(0, 22, 141));
    header.setForeground(Color.WHITE);
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));

    DefaultTableCellRenderer center = new DefaultTableCellRenderer();
    center.setHorizontalAlignment(JLabel.CENTER);
    center.setBackground(new Color(31, 41, 55));
    center.setForeground(Color.WHITE);

    for (int i = 0; i < tablaUsuarios.getColumnCount(); i++) {
      tablaUsuarios.getColumnModel().getColumn(i).setCellRenderer(center);
    }
  }

  private void cambiarEstadoUsuario() {
    int fila = tablaUsuarios.getSelectedRow();
    if (fila >= 0) {
      String estadoActual = modeloTabla.getValueAt(fila, 5).toString();
      String nuevoEstado = estadoActual.equals("Activo") ? "Inactivo" : "Activo";
      modeloTabla.setValueAt(nuevoEstado, fila, 5);
      GestorAlertas.mostrarExito(this, "Estado del usuario actualizado correctamente");
    } else {
      GestorAlertas.mostrarAdvertencia(this, "Seleccione un usuario primero");
    }
  }
}
