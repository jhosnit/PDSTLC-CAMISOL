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

  private Botón btnRegistrar, btnActualizar, btnCambiarEstado, btnConsultar, btnEliminar;

  public Cliente() {
    inicializarComponentes();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout());
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelBotones.setOpaque(false);

    btnRegistrar = new Botón("Registrar Cliente", new Color(40, 167, 69));
    btnActualizar = new Botón("Actualizar", new Color(234, 177, 0));
    btnConsultar  = new Botón("Consultar", new Color(70, 128, 139));
    btnCambiarEstado = new Botón("Cambiar Estado", new Color(147, 51, 234));
    btnEliminar   = new Botón("Eliminar", new Color(239, 68, 68));

    Dimension dimBoton = new Dimension(150, 40);
    btnRegistrar.setPreferredSize(dimBoton);
    btnActualizar.setPreferredSize(dimBoton);
    btnConsultar.setPreferredSize(dimBoton);
    btnCambiarEstado.setPreferredSize(dimBoton);
    btnEliminar.setPreferredSize(dimBoton);

    panelBotones.add(btnRegistrar);
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


    btnRegistrar.addActionListener(e -> GestorAlertas.mostrarExito(this, "registrar usuario"));
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
    tablaClientes.setGridColor(new Color(55, 65, 81));
    tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tablaClientes.setRowHeight(30);
    tablaClientes.setSelectionBackground(new Color(75, 85, 99));
    tablaClientes.setSelectionForeground(Color.WHITE);

    // Cabecera (Header)
    JTableHeader header = tablaClientes.getTableHeader();
    header.setBackground(new Color(243, 244, 246)); // Gris claro para contraste
    header.setForeground(new Color(31, 41, 55));   // Texto oscuro
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setPreferredSize(new Dimension(0, 35));

    // Renderizador para alinear texto y mantener colores
    DefaultTableCellRenderer render = new DefaultTableCellRenderer();
    render.setBackground(new Color(31, 41, 55));
    render.setForeground(Color.WHITE);
    render.setHorizontalAlignment(SwingConstants.LEFT);

    for (int i = 0; i < tablaClientes.getColumnCount(); i++) {
      tablaClientes.getColumnModel().getColumn(i).setCellRenderer(render);
    }
  }

}
