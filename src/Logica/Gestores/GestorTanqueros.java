package Logica.Gestores;

import Logica.Conexión.ConexionBD;
import Logica.Entidades.Tanquero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorTanqueros {

  public boolean crearTanquero(Tanquero tanquero) {
    String sql = "INSERT INTO tanqueros (placa, marca, modelo, anio_fabricacion, capacidad_litros, usuario_registro) " +
      "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, tanquero.getPlaca().toUpperCase());
      pstmt.setString(2, tanquero.getMarca());
      pstmt.setString(3, tanquero.getModelo());
      pstmt.setInt(4, tanquero.getAnioFabricacion());
      pstmt.setDouble(5, tanquero.getCapacidadLitros());
      pstmt.setString(6, tanquero.getUsuarioRegistro());

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Error al crear tanquero: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public List<Tanquero> listarTanqueros() {
    List<Tanquero> lista = new ArrayList<>();
    String sql = "SELECT * FROM tanqueros ORDER BY placa";

    try (Connection conn = ConexionBD.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        lista.add(mapearTanquero(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lista;
  }

  public Tanquero buscarPorPlaca(String placa) {
    String sql = "SELECT * FROM tanqueros WHERE placa = ?";
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, placa.toUpperCase());
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return mapearTanquero(rs);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean actualizarTanquero(Tanquero tanquero) {
    String sql = "UPDATE tanqueros SET marca=?, modelo=?, anio_fabricacion=?, capacidad_litros=?, " +
      "fecha_modificacion=CURRENT_TIMESTAMP WHERE id_tanquero=?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, tanquero.getMarca());
      pstmt.setString(2, tanquero.getModelo());
      pstmt.setInt(3, tanquero.getAnioFabricacion());
      pstmt.setDouble(4, tanquero.getCapacidadLitros());
      pstmt.setInt(5, tanquero.getIdTanquero());

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean cambiarEstado(int idTanquero, boolean nuevoEstado) {
    String sql = "UPDATE tanqueros SET estado=?, fecha_modificacion=CURRENT_TIMESTAMP WHERE id_tanquero=?";
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setBoolean(1, nuevoEstado);
      pstmt.setInt(2, idTanquero);
      return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean existePlaca(String placa) {
    String sql = "SELECT COUNT(*) as total FROM tanqueros WHERE placa = ?";
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, placa.toUpperCase());
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) return rs.getInt("total") > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private Tanquero mapearTanquero(ResultSet rs) throws SQLException {
    Tanquero t = new Tanquero();
    t.setIdTanquero(rs.getInt("id_tanquero"));
    t.setPlaca(rs.getString("placa"));
    t.setMarca(rs.getString("marca"));
    t.setModelo(rs.getString("modelo"));
    t.setAnioFabricacion(rs.getInt("anio_fabricacion"));
    t.setCapacidadLitros(rs.getDouble("capacidad_litros"));
    t.setEstado(rs.getBoolean("estado"));
    t.setUsuarioRegistro(rs.getString("usuario_registro"));
    return t;
  }

  public int contarTanquerosActivos() {
    String sql = "SELECT COUNT(*) as total FROM tanqueros WHERE estado = true";
    try (Connection conn = ConexionBD.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      if (rs.next()) {
        return rs.getInt("total");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public boolean eliminarTanquero(int idTanquero) {
    String sql = "DELETE FROM tanqueros WHERE id_tanquero = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, idTanquero);
      int filasAfectadas = pstmt.executeUpdate();
      return filasAfectadas > 0;

    } catch (SQLException e) {
      System.err.println("Error al eliminar tanquero: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public int obtenerIdPorPlaca(String placa) {
    if (placa == null || placa.trim().isEmpty()) return -1;
    Tanquero t = buscarPorPlaca(placa.trim().toUpperCase());
    return (t != null) ? t.getIdTanquero() : -1;
  }

  public boolean asignarChofer(int idTanquero, int idSocio) {
    String sql = "UPDATE tanqueros SET id_socio = ? WHERE id_tanquero = ?";
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, idSocio);
      pstmt.setInt(2, idTanquero);
      return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean desasignarChofer(int idTanquero) {
    String sql = "UPDATE tanqueros SET id_socio = NULL, fecha_modificacion = CURRENT_TIMESTAMP " +
      "WHERE id_tanquero = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, idTanquero);
      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Error al desasignar chofer: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

}