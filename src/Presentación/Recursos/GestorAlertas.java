package Presentación.Recursos;

import javax.swing.*;
import java.awt.*;

public class GestorAlertas {

  public static void mostrarErrorLogin(Component component, String mensaje) {
    JLabel label = new JLabel(mensaje);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    JOptionPane.showMessageDialog(component, label, "Error de Ingreso", JOptionPane.PLAIN_MESSAGE);
  }

  public static boolean confirmarCerrarSesión(Component component, String mensaje) {
    JLabel label = new JLabel(mensaje);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    int opción = JOptionPane.showConfirmDialog(component, label, "Confirmar Cierre de Sesión",
      JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
    return opción == JOptionPane.YES_OPTION;
  }

  public static void mostrarAdvertencia(Component component, String s) {
    JLabel label = new JLabel(s);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    JOptionPane.showMessageDialog(component, label, "Advertencia", JOptionPane.PLAIN_MESSAGE);
  }

  public static void mostrarExito(Component component, String s) {
    JLabel label = new JLabel(s);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    JOptionPane.showMessageDialog(component, label, "Operación Exitosa", JOptionPane.PLAIN_MESSAGE);
  }

}
