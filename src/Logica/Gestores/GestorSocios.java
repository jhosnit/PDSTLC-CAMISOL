package Logica.Gestores;

import Logica.Conexión.ConexionBD;
import Logica.Entidades.Socio;
import Logica.Utilidades.Validaciones;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorSocios {


  public boolean crearSocio(Socio socio) {
    String sql = "INSERT INTO socios (ruc, razon_social, direccion, telefono, correo, usuario_registro) " +
      "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, socio.getRuc());
      pstmt.setString(2, socio.getRazonSocial());
      pstmt.setString(3, socio.getDireccion());
      pstmt.setString(4, socio.getTelefono());
      pstmt.setString(5, socio.getCorreo());
      pstmt.setString(6, socio.getUsuarioRegistro());

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
    String sql = "SELECT * FROM socios ORDER BY razon_social";

    try (Connection conn = ConexionBD.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Socio socio = new Socio();
        socio.setIdSocio(rs.getInt("id_socio"));
        socio.setRuc(rs.getString("ruc"));
        socio.setRazonSocial(rs.getString("razon_social"));
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

        lista.add(socio);
      }

    } catch (SQLException e) {
      System.err.println("Error al listar socios: " + e.getMessage());
      e.printStackTrace();
    }

    return lista;
  }

  public Socio buscarPorRuc(String ruc) {
    String sql = "SELECT * FROM socios WHERE ruc = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, ruc);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        Socio socio = new Socio();
        socio.setIdSocio(rs.getInt("id_socio"));
        socio.setRuc(rs.getString("ruc"));
        socio.setRazonSocial(rs.getString("razon_social"));
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

    } catch (SQLException e) {
      System.err.println("Error al buscar socio por RUC: " + e.getMessage());
      e.printStackTrace();
    }

    return null;
  }

  public List<Socio> buscarPorNombre(String nombre) {
    List<Socio> lista = new ArrayList<>();
    String sql = "SELECT * FROM socios WHERE LOWER(razon_social) LIKE LOWER(?) ORDER BY razon_social";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, "%" + nombre + "%");
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        Socio socio = new Socio();
        socio.setIdSocio(rs.getInt("id_socio"));
        socio.setRuc(rs.getString("ruc"));
        socio.setRazonSocial(rs.getString("razon_social"));
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

        lista.add(socio);
      }

    } catch (SQLException e) {
      System.err.println("Error al buscar socios por nombre: " + e.getMessage());
      e.printStackTrace();
    }

    return lista;
  }

  public boolean actualizarSocio(Socio socio) {
    String sql = "UPDATE socios SET razon_social = ?, direccion = ?, telefono = ?, correo = ?, " +
      "fecha_modificacion = CURRENT_TIMESTAMP WHERE id_socio = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, socio.getRazonSocial());
      pstmt.setString(2, socio.getDireccion());
      pstmt.setString(3, socio.getTelefono());
      pstmt.setString(4, socio.getCorreo());
      pstmt.setInt(5, socio.getIdSocio());

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

  public boolean existeRuc(String ruc) {
    String sql = "SELECT COUNT(*) as total FROM socios WHERE ruc = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, ruc);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return rs.getInt("total") > 0;
      }

    } catch (SQLException e) {
      System.err.println("Error al verificar RUC: " + e.getMessage());
      e.printStackTrace();
    }

    return false;
  }

}
