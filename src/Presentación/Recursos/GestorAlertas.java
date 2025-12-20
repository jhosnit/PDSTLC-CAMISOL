package Presentación.Recursos;

import Presentación.Ventanas.VentanaInicio;
import Presentación.Ventanas.VentanaPrincipal;

import javax.swing.*;
import java.awt.*;

public class GestorAlertas {

  public static void mostrarErrorLogin(VentanaInicio ventanaInicio, String mensaje) {
    JOptionPane.showMessageDialog(ventanaInicio, mensaje, "Error de Ingreso", JOptionPane.WARNING_MESSAGE);
  }

  public static boolean confirmarCerrarSesión(VentanaPrincipal ventanaPrincipal, String s) {
    int opción = JOptionPane.showConfirmDialog(ventanaPrincipal,
            s,
            "Confirmar Cierre de Sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
    return opción == JOptionPane.YES_OPTION;
  }
}
