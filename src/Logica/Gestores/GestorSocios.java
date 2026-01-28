package Logica.Gestores;

import Logica.Conexión.ConexionBD;
import Logica.Entidades.Socio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorSocios {

  public boolean crearSocio(Socio socio) {
    String sql = "INSERT INTO socios (cedula, nombres, apellidos, direccion, telefono, correo, usuario_registro) " +
      "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, socio.getCedula());
      pstmt.setString(2, socio.getNombres());
      pstmt.setString(3, socio.getApellidos());
      pstmt.setString(4, socio.getDireccion());
      pstmt.setString(5, socio.getTelefono());
      pstmt.setString(6, socio.getCorreo());
      pstmt.setString(7, socio.getUsuarioRegistro());

      int filasAfectadas = pstmt.executeUpdate();
      return filasAfectadas > 0;

    } catch (SQLException e) {
      System.err.println("Error al crear socio: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public List<Socio> listarSocios() {
    List<Socio> lista = new ArrayList<>();
    String sql = "SELECT * FROM socios ORDER BY apellidos, nombres";

    try (Connection conn = ConexionBD.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Socio socio = mapearSocio(rs);
        lista.add(socio);
      }

    } catch (SQLException e) {
      System.err.println("Error al listar socios: " + e.getMessage());
      e.printStackTrace();
    }

    return lista;
  }

  public Socio buscarPorCedula(String cedula) {
    String sql = "SELECT * FROM socios WHERE cedula = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, cedula);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return mapearSocio(rs);
      }

    } catch (SQLException e) {
      System.err.println("Error al buscar socio por Cédula: " + e.getMessage());
      e.printStackTrace();
    }

    return null;
  }

  public List<Socio> buscarPorNombre(String busqueda) {
    List<Socio> lista = new ArrayList<>();

    String sql = "SELECT * FROM socios WHERE LOWER(nombres) LIKE LOWER(?) OR LOWER(apellidos) LIKE LOWER(?) ORDER BY apellidos";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, "%" + busqueda + "%");
      pstmt.setString(2, "%" + busqueda + "%");
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        lista.add(mapearSocio(rs));
      }

    } catch (SQLException e) {
      System.err.println("Error al buscar socios por nombre: " + e.getMessage());
      e.printStackTrace();
    }

    return lista;
  }

  public boolean actualizarSocio(Socio socio) {
    String sql = "UPDATE socios SET nombres = ?, apellidos = ?, direccion = ?, telefono = ?, correo = ?, " +
      "fecha_modificacion = CURRENT_TIMESTAMP WHERE id_socio = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, socio.getNombres());
      pstmt.setString(2, socio.getApellidos());
      pstmt.setString(3, socio.getDireccion());
      pstmt.setString(4, socio.getTelefono());
      pstmt.setString(5, socio.getCorreo());
      pstmt.setInt(6, socio.getIdSocio());

      int filasAfectadas = pstmt.executeUpdate();
      return filasAfectadas > 0;

    } catch (SQLException e) {
      System.err.println("Error al actualizar socio: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public boolean cambiarEstado(int idSocio, boolean nuevoEstado) {
    String sql = "UPDATE socios SET estado = ?, fecha_modificacion = CURRENT_TIMESTAMP WHERE id_socio = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setBoolean(1, nuevoEstado);
      pstmt.setInt(2, idSocio);

      int filasAfectadas = pstmt.executeUpdate();
      return filasAfectadas > 0;

    } catch (SQLException e) {
      System.err.println("Error al cambiar estado: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public boolean eliminarSocio(int idSocio) {
    String sql = "DELETE FROM socios WHERE id_socio = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, idSocio);
      int filasAfectadas = pstmt.executeUpdate();
      return filasAfectadas > 0;

    } catch (SQLException e) {
      System.err.println("Error al eliminar socio: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public boolean existeCedula(String cedula) {
    String sql = "SELECT COUNT(*) as total FROM socios WHERE cedula = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, cedula);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return rs.getInt("total") > 0;
      }

    } catch (SQLException e) {
      System.err.println("Error al verificar Cédula: " + e.getMessage());
      e.printStackTrace();
    }

    return false;
  }

  private Socio mapearSocio(ResultSet rs) throws SQLException {
    Socio socio = new Socio();
    socio.setIdSocio(rs.getInt("id_socio"));
    socio.setCedula(rs.getString("cedula"));
    socio.setNombres(rs.getString("nombres"));
    socio.setApellidos(rs.getString("apellidos"));
    socio.setDireccion(rs.getString("direccion"));
    socio.setTelefono(rs.getString("telefono"));
    socio.setCorreo(rs.getString("correo"));
    socio.setEstado(rs.getBoolean("estado"));

    Timestamp tsRegistro = rs.getTimestamp("fecha_registro");
    if (tsRegistro != null) {
      socio.setFechaRegistro(tsRegistro.toLocalDateTime());
    }

    Timestamp tsModificacion = rs.getTimestamp("fecha_modificacion");
    if (tsModificacion != null) {
      socio.setFechaModificacion(tsModificacion.toLocalDateTime());
    }

    socio.setUsuarioRegistro(rs.getString("usuario_registro"));
    return socio;
  }

  public int contarSociosActivos() {
    String sql = "SELECT COUNT(*) as total FROM socios WHERE estado = true";
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

}
