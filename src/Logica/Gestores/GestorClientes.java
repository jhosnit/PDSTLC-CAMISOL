package Logica.Gestores;

import Logica.Conexión.ConexionBD;
import Logica.Entidades.Cliente;

import java.sql.*;

public class GestorClientes {

  public Cliente obtenerClientePorDefecto() {
    String sql = "SELECT * FROM clientes WHERE ruc = '0990318735001' LIMIT 1";

    try (Connection conn = ConexionBD.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      if (rs.next()) {
        return mapearCliente(rs);
      }
    } catch (SQLException e) {
      System.err.println("Error al obtener cliente por defecto: " + e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  public Cliente buscarPorId(int idCliente) {
    String sql = "SELECT * FROM clientes WHERE id_cliente = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, idCliente);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return mapearCliente(rs);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Cliente mapearCliente(ResultSet rs) throws SQLException {
    Cliente c = new Cliente();
    c.setIdCliente(rs.getInt("id_cliente"));
    c.setRuc(rs.getString("ruc"));
    c.setRazonSocial(rs.getString("razon_social"));
    c.setDireccion(rs.getString("direccion"));
    c.setTelefono(rs.getString("telefono"));
    c.setCorreo(rs.getString("correo"));
    c.setEstado(rs.getBoolean("estado"));
    return c;
  }
}