package Presentación.Recursos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Botón extends JButton {
  private Color colorFondo;
  private Color colorHover;
  private boolean enHover = false;

  public Botón(String texto, Color colorFondo) {
    super(texto);
    this.colorFondo = colorFondo;
    this.colorHover = ajustarBrillo(colorFondo, 1.2f); // 20% más brillante

    setForeground(Color.WHITE);
    setFocusPainted(false);
    setBorderPainted(false);
    setContentAreaFilled(false);
    setOpaque(false);
    setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Detectar hover
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        enHover = true;
        repaint();
      }

      @Override
      public void mouseExited(MouseEvent e) {
        enHover = false;
        repaint();
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(enHover ? colorHover : colorFondo);
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
    super.paintComponent(g);
    g2.dispose();
  }

  // Método para aclarar el color
  private Color ajustarBrillo(Color color, float factor) {
    int r = Math.min(255, (int) (color.getRed() * factor));
    int g = Math.min(255, (int) (color.getGreen() * factor));
    int b = Math.min(255, (int) (color.getBlue() * factor));
    return new Color(r, g, b);
  }
}

