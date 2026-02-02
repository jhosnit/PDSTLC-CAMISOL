package Logica.Gestores;

import Logica.Conexión.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorAuditoria {

  public int registrarEvento(String usuario, String nombre, String accion) {
    String sql = "INSERT INTO auditoria (usuario, nombre, accion, fecha) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, usuario);
      pstmt.setString(2, nombre);
      pstmt.setString(3, accion);

      int filasAfectadas = pstmt.executeUpdate();

      if (filasAfectadas > 0) {
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
          }
        }
      }
      return -1;

    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public List<Object[]> listarEventos() {
    List<Object[]> lista = new ArrayList<>();

    String sql = "SELECT * FROM auditoria ORDER BY id_auditoria ASC";

    try (Connection conn = ConexionBD.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        String fechaStr = "";
        Timestamp ts = rs.getTimestamp("fecha");
        if (ts != null) {
          fechaStr = ts.toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }

        lista.add(new Object[]{
          rs.getInt("id_auditoria"),
          rs.getString("usuario"),
          rs.getString("nombre"),
          rs.getString("accion"),
          fechaStr
        });
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lista;
  }
}