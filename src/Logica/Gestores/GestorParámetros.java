package Logica.Gestores;

import Logica.Conexión.ConexionBD;
import Logica.Entidades.Parámetros;
import java.sql.*;

public class GestorParámetros {

  public Parámetros obtenerParámetros() {
    String sql = "SELECT * FROM parametros WHERE id = 1";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

      if (rs.next()) {
        Parámetros params = new Parámetros();
        params.setId(rs.getInt("id"));
        params.setIva(rs.getDouble("iva"));

        Timestamp timestamp = rs.getTimestamp("ultima_modificacion");
        if (timestamp != null) {
          params.setUltimaModificacion(timestamp.toString());
        }

        params.setUsuarioModificacion(rs.getString("usuario_modificacion"));
        return params;
      } else {
        return null;
      }

    } catch (SQLException e) {
      System.out.println("Error al obtener parámetros: " + e.getMessage());
      return null;
    }
  }

  public boolean actualizarParámetros(Parámetros parametros, String usuario) {
    String sql = "UPDATE parametros SET iva = ?, ultima_modificacion = CURRENT_TIMESTAMP, " +
      "usuario_modificacion = ? WHERE id = 1";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setDouble(1, parametros.getIva());
      pstmt.setString(2, usuario);

      int filasActualizadas = pstmt.executeUpdate();
      if (filasActualizadas == 0) {
        return insertarParámetros(parametros, usuario);
      }
      return filasActualizadas > 0;

    } catch (SQLException e) {
      System.out.println("Error al actualizar parámetros: " + e.getMessage());
      return false;
    }
  }

  private boolean insertarParámetros(Parámetros parametros, String usuario) {
    String sql = "INSERT INTO parametros (id, iva, ultima_modificacion, usuario_modificacion) " +
      "VALUES (1, ?, CURRENT_TIMESTAMP, ?)";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setDouble(1, parametros.getIva());
      pstmt.setString(2, usuario);
      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.out.println("Error al insertar parámetros: " + e.getMessage());
      return false;
    }
  }

}
