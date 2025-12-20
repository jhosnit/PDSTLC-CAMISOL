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
    this.colorFondo = (colorFondo != null) ? colorFondo : new Color(55, 65, 81);
    this.colorHover = ajustarBrillo(this.colorFondo, 1.2f); // 20% más brillante

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

  public static Botón crearBotónMenu(String texto, Color color) {
    Botón botón = new Botón("  " + texto, color);
    botón.setHorizontalAlignment(SwingConstants.LEFT);
    botón.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    botón.setPreferredSize(new Dimension(210, 42));
    botón.setMaximumSize(new Dimension(210, 42));

    botón.addMouseListener(new java.awt.event.MouseAdapter() {
      Color colorOriginal = botón.getBackground();

      public void mouseEntered(java.awt.event.MouseEvent evt) {
        botón.setBackground(new Color(75, 85, 99));
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        botón.setBackground(colorOriginal);
      }
    });

    return botón;
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

