package Logica.Gestores;

import Logica.Conexión.ConexionBD;
import Logica.Entidades.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {

  public List<Usuario> listarUsuarios() {
    List<Usuario> lista = new ArrayList<>();
    String sql = "SELECT * FROM usuarios ORDER BY id_usuario";

    try (Connection conn = ConexionBD.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setRol(rs.getString("rol"));
        u.setEstado(rs.getBoolean("estado"));
        u.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));
        lista.add(u);
      }
    } catch (SQLException e) {
      System.out.println("Error al listar usuarios: " + e.getMessage());
    }
    return lista;
  }

  public boolean cambiarContraseña(String username, String nuevaPassword) {
    String sql = "UPDATE usuarios SET password = ? WHERE username = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, nuevaPassword);
      pstmt.setString(2, username);

      int filasAfectadas = pstmt.executeUpdate();
      return filasAfectadas > 0;

    } catch (SQLException e) {
      System.out.println("Error al cambiar contraseña: " + e.getMessage());
      return false;
    }
  }

  public boolean validarCredenciales(String username, String password) {
    String sql = "SELECT id_usuario FROM usuarios WHERE username = ? AND password = ? AND estado = true";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, username);
      pstmt.setString(2, password);

      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        actualizarUltimoAcceso(username);
        return true;
      }
    } catch (SQLException e) {
      System.out.println("Error al validar credenciales: " + e.getMessage());
    }
    return false;
  }

  private void actualizarUltimoAcceso(String username) {
    String sql = "UPDATE usuarios SET ultimo_acceso = CURRENT_TIMESTAMP WHERE username = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, username);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      System.out.println("No se pudo actualizar la fecha de acceso: " + e.getMessage());
    }
  }

  public static Usuario obtenerUsuarioPorUsername(String username) {
    Usuario u = null;
    String sql = "SELECT * FROM usuarios WHERE username = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, username);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setRol(rs.getString("rol"));
        u.setEstado(rs.getBoolean("estado"));
        u.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return u;
  }

  public boolean crearUsuario(Usuario usuario) {
    String sql = "INSERT INTO usuarios (username, password, nombre, apellido, rol, estado) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, usuario.getUsername());
      pstmt.setString(2, usuario.getPassword());
      pstmt.setString(3, usuario.getNombre());
      pstmt.setString(4, usuario.getApellido());
      pstmt.setString(5, usuario.getRol());
      pstmt.setBoolean(6, true);

      int filasAfectadas = pstmt.executeUpdate();
      return filasAfectadas > 0;

    } catch (SQLException e) {
      System.out.println("Error al crear usuario: " + e.getMessage());
      return false;
    }
  }


  public boolean existeUsername(String username) {
    String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, username);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return rs.getInt(1) > 0;
      }
    } catch (SQLException e) {
      System.out.println("Error al verificar username: " + e.getMessage());
    }
    return false;
  }

  public boolean cambiarEstadoUsuario(int idUsuario, boolean estado) {
    String sql = "UPDATE usuarios SET estado = ? WHERE id_usuario = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setBoolean(1, estado);
      pstmt.setInt(2, idUsuario);

      int filasAfectadas = pstmt.executeUpdate();
      return filasAfectadas > 0;

    } catch (SQLException e) {
      System.out.println("Error al cambiar estado: " + e.getMessage());
      return false;
    }
  }
}
