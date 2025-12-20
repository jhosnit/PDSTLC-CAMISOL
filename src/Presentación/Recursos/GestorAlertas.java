package Presentación.Recursos;

import Presentación.Ventanas.VentanaInicio;
import Presentación.Ventanas.VentanaPrincipal;

import javax.swing.*;
import java.awt.*;

public class GestorAlertas {

  public static void mostrarErrorLogin(VentanaInicio ventanaInicio, String mensaje) {
    JLabel label = new JLabel(mensaje);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    JOptionPane.showMessageDialog(ventanaInicio, label, "Error de Ingreso", JOptionPane.PLAIN_MESSAGE);
  }

  public static boolean confirmarCerrarSesión(VentanaPrincipal ventanaPrincipal, String mensaje) {
    JLabel label = new JLabel(mensaje);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    int opción = JOptionPane.showConfirmDialog(ventanaPrincipal, label, "Confirmar Cierre de Sesión",
      JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
    return opción == JOptionPane.YES_OPTION;
  }

}
