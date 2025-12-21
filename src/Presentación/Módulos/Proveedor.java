package Presentación.Módulos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import Presentación.Recursos.Botón;

public class Proveedor extends JPanel {
  private static final String ADMIN_PASSWORD = "admin123";

  private JTable tabla;
  private DefaultTableModel modelo;
  private Botón botónAgregar, botónActualizar, botónEliminar, botónConsultar;

  public Proveedor() {
    inicializarComponentes();
  }

  private void inicializarComponentes() {
    setLayout(new BorderLayout());
    // Color de fondo consistente con VentanaPrincipal (Panel de contenido)
    setBackground(new Color(18, 18, 18));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    // --- PANEL DE BOTONES ---
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    panelBotones.setOpaque(false); // Para que se vea el fondo oscuro del padre

    // Colores basados en tu imagen de referencia y VentanaPrincipal
    botónAgregar = new Botón("Registrar Proveedor", new Color(40, 167, 69)); // Verde
    botónActualizar = new Botón("Actualizar ", new Color(234, 177, 0)); // Amarillo/Oro
    botónConsultar = new Botón("Consultar ", new Color(70, 128, 139)); // Azul Grisáceo
    botónEliminar = new Botón("Eliminar", new Color(239, 68, 68)); // Rojo

    Dimension dimBoton = new Dimension(150, 40);
    botónAgregar.setPreferredSize(dimBoton);
    botónActualizar.setPreferredSize(dimBoton);
    botónConsultar.setPreferredSize(dimBoton);
    botónEliminar.setPreferredSize(dimBoton);

    panelBotones.add(botónAgregar);
    panelBotones.add(botónActualizar);
    panelBotones.add(botónConsultar);
    panelBotones.add(botónEliminar);

    add(panelBotones, BorderLayout.NORTH);

    // --- CONFIGURACIÓN DE LA TABLA ---
    String[] columnas = {"Cédula/RUC", "Nombre", "Apellido", "Celular", "Email", "Descripción", "Dirección", "Activo"};
    modelo = new DefaultTableModel(columnas, 0) {
      @Override
      public Class<?> getColumnClass(int column) {
        return column == 7 ? Boolean.class : String.class;
      }
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tabla = new JTable(modelo);
    configurarEstiloTabla();

    JScrollPane panelDesplazamiento = new JScrollPane(tabla);
    panelDesplazamiento.setOpaque(false);
    panelDesplazamiento.getViewport().setBackground(new Color(31, 41, 55)); // Fondo tabla
    panelDesplazamiento.setBorder(new LineBorder(new Color(55, 65, 81), 1));

    add(panelDesplazamiento, BorderLayout.CENTER);

    // Listeners
    botónAgregar.addActionListener(e -> abrirDiálogoAgregar());
    botónActualizar.addActionListener(e -> abrirDialogoActualizar());
    botónEliminar.addActionListener(e -> eliminar());
  }

  private void configurarEstiloTabla() {

    tabla.setBackground(new Color(31, 41, 55));
    tabla.setForeground(Color.WHITE);
    tabla.setGridColor(new Color(55, 65, 81));
    tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tabla.setRowHeight(30);
    tabla.setSelectionBackground(new Color(75, 85, 99));
    tabla.setSelectionForeground(Color.WHITE);

    // Cabecera (Header)
    JTableHeader header = tabla.getTableHeader();
    header.setBackground(new Color(243, 244, 246)); // Gris claro para contraste
    header.setForeground(new Color(31, 41, 55));   // Texto oscuro
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setPreferredSize(new Dimension(0, 35));

    // Renderizador para alinear texto y mantener colores
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
    cellRenderer.setBackground(new Color(31, 41, 55));
    cellRenderer.setForeground(Color.WHITE);
    cellRenderer.setHorizontalAlignment(SwingConstants.LEFT);

    for (int i = 0; i < tabla.getColumnCount(); i++) {
      if(i != 7) { // No aplicar a la columna de Checkbox
        tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
      }
    }
  }

  private void abrirDiálogoAgregar() {
    JDialog diálogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Agregar nuevo proveedor", Dialog.ModalityType.APPLICATION_MODAL);
    diálogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 4, 4, 4);
    gbc.anchor = GridBagConstraints.WEST;

    JTextField textoCédula      = new JTextField(15);
    JTextField textoNombre      = new JTextField(15);
    JTextField textoApellido    = new JTextField(15);
    JTextField textoCelular     = new JTextField(15);
    JTextField textoEmail       = new JTextField(15);
    JTextField textoDescripción = new JTextField(15);
    JTextField textoDirección   = new JTextField(15);

    gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Cédula:"),   gbc);
    gbc.gridx = 1; panel.add(textoCédula, gbc);
    gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nombre:"),   gbc);
    gbc.gridx = 1; panel.add(textoNombre, gbc);
    gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Apellido:"), gbc);
    gbc.gridx = 1; panel.add(textoApellido, gbc);
    gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Celular:"),  gbc);
    gbc.gridx = 1; panel.add(textoCelular, gbc);
    gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Email:"),    gbc);
    gbc.gridx = 1; panel.add(textoEmail, gbc);
    gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Descripción (opcional):"), gbc);
    gbc.gridx = 1; panel.add(textoDescripción, gbc);
    gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Dirección (opcional):"),   gbc);
    gbc.gridx = 1; panel.add(textoDirección, gbc);

    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    Botón botónAgregar     = new Botón("Agregar",  new Color(40, 167, 69));
    Botón botónLimpiar  = new Botón("Limpiar",   new Color(108, 117, 125));
    Botón botónCancel = new Botón("Cancelar",  new Color(220, 53, 69));

    botónAgregar.setPreferredSize(new Dimension(100, 35)); botónAgregar.setFont(new Font("Arial", Font.BOLD, 14));
    botónLimpiar.setPreferredSize(new Dimension(100, 35)); botónLimpiar.setFont(new Font("Arial", Font.BOLD, 14));
    botónCancel.setPreferredSize(new Dimension(100, 35)); botónCancel.setFont(new Font("Arial", Font.BOLD, 14));

    panelBotones.add(botónAgregar);
    panelBotones.add(botónLimpiar);
    panelBotones.add(botónCancel);

    diálogo.getContentPane().add(panel, BorderLayout.CENTER);
    diálogo.getContentPane().add(panelBotones, BorderLayout.SOUTH);

    botónLimpiar.addActionListener(ev -> {
      textoCédula.setText(""); textoNombre.setText(""); textoApellido.setText("");
      textoCelular.setText(""); textoEmail.setText(""); textoDescripción.setText(""); textoDirección.setText("");
    });
    botónCancel.addActionListener(ev -> diálogo.dispose());

    botónAgregar.addActionListener(ev -> {
      if (textoCédula.getText().trim().isEmpty() || textoNombre.getText().trim().isEmpty() ||
        textoApellido.getText().trim().isEmpty() || textoCelular.getText().trim().isEmpty() ||
        textoEmail.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(diálogo, "Los campos obligatorios no pueden quedar vacíos.", "Error al agregar", JOptionPane.PLAIN_MESSAGE);
      } else {
        modelo.addRow(new Object[]{
          textoCédula.getText().trim(),
          textoNombre.getText().trim(),
          textoApellido.getText().trim(),
          textoCelular.getText().trim(),
          textoEmail.getText().trim(),
          textoDescripción.getText().trim(),
          textoDirección.getText().trim(),
          true // Activo
        });
        JOptionPane.showMessageDialog(diálogo, "Proveedor agregado exitosamente.", "Éxito", JOptionPane.PLAIN_MESSAGE);
      }
    });

    diálogo.pack();
    diálogo.setLocationRelativeTo(this);
    diálogo.setVisible(true);
  }

  private void abrirDialogoActualizar() {
    String cedula = JOptionPane.showInputDialog(this, "Ingrese la Cédula del proveedor:", "Actualizar proveedor", JOptionPane.PLAIN_MESSAGE);
    if (cedula == null || cedula.trim().isEmpty()) return;
    cedula = cedula.trim();

    int foundIdx = -1;
    for (int i = 0; i < modelo.getRowCount(); i++) {
      if (modelo.getValueAt(i, 0).equals(cedula)) {
        foundIdx = i;
        break;
      }
    }
    if (foundIdx < 0) {
      JOptionPane.showMessageDialog(this, "Proveedor no encontrado.", "Error", JOptionPane.PLAIN_MESSAGE);
      return;
    }
    final int rowIndexLocal = foundIdx;

    JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Actualizar proveedor", Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 4, 4, 4);
    gbc.anchor = GridBagConstraints.WEST;

    JTextField textoDireccion   = new JTextField(modelo.getValueAt(rowIndexLocal, 6).toString(), 15);
    JTextField textoDescripcion = new JTextField(modelo.getValueAt(rowIndexLocal, 5).toString(), 15);
    JCheckBox chkActivo       = new JCheckBox("Activo", (Boolean) modelo.getValueAt(rowIndexLocal, 7));

    gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Dirección:"), gbc);
    gbc.gridx = 1; panel.add(textoDireccion, gbc);
    gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Descripción:"), gbc);
    gbc.gridx = 1; panel.add(textoDescripcion, gbc);
    gbc.gridx = 0; gbc.gridy = 2; panel.add(chkActivo, gbc);

    JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    Botón botónActualizar     = new Botón("Actualizar", new Color(40, 167, 69));
    Botón botónLimpiar  = new Botón("Limpiar",   new Color(108, 117, 125));
    Botón botónCancelar = new Botón("Cancelar",  new Color(220, 53, 69));

    botónActualizar.setPreferredSize(new Dimension(100, 35)); botónActualizar.setFont(new Font("Arial", Font.BOLD, 14));
    botónLimpiar.setPreferredSize(new Dimension(100, 35)); botónLimpiar.setFont(new Font("Arial", Font.BOLD, 14));
    botónCancelar.setPreferredSize(new Dimension(100, 35)); botónCancelar.setFont(new Font("Arial", Font.BOLD, 14));

    botones.add(botónActualizar);
    botones.add(botónLimpiar);
    botones.add(botónCancelar);

    dialog.getContentPane().add(panel, BorderLayout.CENTER);
    dialog.getContentPane().add(botones, BorderLayout.SOUTH);

    botónLimpiar.addActionListener(ev -> {
      textoDireccion.setText(""); textoDescripcion.setText(""); chkActivo.setSelected(false);
    });
    botónCancelar.addActionListener(ev -> dialog.dispose());

    botónActualizar.addActionListener(ev -> {
      modelo.setValueAt(textoDescripcion.getText().trim(), rowIndexLocal, 5);
      modelo.setValueAt(textoDireccion.getText().trim(),    rowIndexLocal, 6);
      modelo.setValueAt(chkActivo.isSelected(),          rowIndexLocal, 7);
      JOptionPane.showMessageDialog(dialog, "Proveedor actualizado exitosamente.", "Éxito", JOptionPane.PLAIN_MESSAGE);
      dialog.dispose();
    });

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  private void eliminar() {
    int fila = tabla.getSelectedRow();
    if (fila < 0) {
      JOptionPane.showMessageDialog(this, "Seleccione un proveedor para eliminar.", "Error", JOptionPane.PLAIN_MESSAGE);
      return;
    }
    JPasswordField contraseña = new JPasswordField();
    int opt = JOptionPane.showConfirmDialog(this, contraseña, "Contraseña admin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (opt == JOptionPane.OK_OPTION && ADMIN_PASSWORD.equals(new String(contraseña.getPassword()))) {
      modelo.removeRow(fila);
      JOptionPane.showMessageDialog(this, "Proveedor eliminado correctamente.", "Éxito", JOptionPane.PLAIN_MESSAGE);
    } else if (opt == JOptionPane.OK_OPTION) {
      JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error autenticación", JOptionPane.PLAIN_MESSAGE);
    }
  }
}
