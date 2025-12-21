package Presentación.Módulos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import Presentación.Recursos.Botón;
import Presentación.Recursos.GestorAlertas;

public class Cliente extends JPanel {

  private JTable tablaClientes;
  private DefaultTableModel modeloTabla;

  private Botón btnAgregar, btnActualizar, btnCambiarEstado, btnConsultar, btnEliminar;

  public Cliente() {
    inicializarComponentes();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout(0, 20));
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(20, 20, 20, 20));

    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
    panelBotones.setOpaque(false);

    btnAgregar    = new Botón("Agregar", new Color(37, 99, 235));
    btnActualizar = new Botón("Actualizar",        new Color(234, 177, 0));
    btnConsultar  = new Botón("Consultar",         new Color(16, 185, 129));
    btnCambiarEstado = new Botón("Cambiar Estado",      new Color(59, 130, 246));
    btnEliminar   = new Botón("Eliminar",      new Color(239, 68, 68));

    panelBotones.add(btnAgregar);
    panelBotones.add(btnActualizar);
    panelBotones.add(btnConsultar);
    panelBotones.add(btnCambiarEstado);
    panelBotones.add(btnEliminar);

    add(panelBotones, BorderLayout.NORTH);

    crearTabla();

    JScrollPane scroll = new JScrollPane(tablaClientes);
    scroll.getViewport().setBackground(new Color(31, 41, 55));
    scroll.setBorder(new LineBorder(new Color(55, 65, 81), 1));

    add(scroll, BorderLayout.CENTER);


    btnAgregar.addActionListener(e -> GestorAlertas.mostrarExito(this, "registrar usuario"));
    btnActualizar.addActionListener(e -> GestorAlertas.mostrarExito(this, "Actualizar usuario"));
    btnCambiarEstado.addActionListener(e -> GestorAlertas.mostrarExito(this, "Cambiar Estado usuario"));
  }

  private void crearTabla() {

    String[] columnas = {
      "Cédula/RUC", "Razón Social/Nombre",
      "Teléfono", "Correo", "Dirección", "Estado"
    };

    modeloTabla = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tablaClientes = new JTable(modeloTabla);
    tablaClientes.setBackground(new Color(31, 41, 55));
    tablaClientes.setForeground(Color.WHITE);
    tablaClientes.setRowHeight(30);
    tablaClientes.setShowVerticalLines(false);
    tablaClientes.setGridColor(new Color(55, 65, 81));

    JTableHeader header = tablaClientes.getTableHeader();
    header.setBackground(new Color(0, 22, 141));
    header.setForeground(Color.WHITE);
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));

    DefaultTableCellRenderer render = new DefaultTableCellRenderer();
    render.setHorizontalAlignment(JLabel.CENTER);
    render.setBackground(new Color(31, 41, 55));
    render.setForeground(Color.WHITE);

    for (int i = 0; i < tablaClientes.getColumnCount(); i++) {
      tablaClientes.getColumnModel().getColumn(i).setCellRenderer(render);
    }
  }

}
