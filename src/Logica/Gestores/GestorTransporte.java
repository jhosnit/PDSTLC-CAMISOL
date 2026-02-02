package Logica.Gestores;

import Logica.Conexión.ConexionBD;
import Logica.Entidades.Cliente;
import Logica.Entidades.Socio;
import Logica.Entidades.Tanquero;
import Logica.Entidades.Transporte;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorTransporte {

  public boolean registrarViaje(Transporte t) {
    String sql = "INSERT INTO transporte " +
      "(id_tanquero, id_socio, id_cliente, fecha_asignacion, hora_asignacion, " +
      "ruta_origen, ruta_destino, kilometros, litros_transportados, " +
      "porcentaje_ocupacion, valor_flete, " + // <--- NUEVAS COLUMNAS
      "estado_viaje, observaciones, usuario_registro) " +
      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, t.getTanquero().getIdTanquero());
      pstmt.setInt(2, t.getSocio().getIdSocio());
      pstmt.setInt(3, t.getCliente().getIdCliente());
      pstmt.setDate(4, java.sql.Date.valueOf(t.getFechaAsignacion()));
      pstmt.setTime(5, java.sql.Time.valueOf(t.getHoraAsignacion()));
      pstmt.setString(6, t.getRutaOrigen());
      pstmt.setString(7, t.getRutaDestino());
      pstmt.setDouble(8, t.getKilometros());
      pstmt.setDouble(9, t.getLitrosTransportados());
      pstmt.setDouble(10, t.getPorcentajeOcupacion());
      pstmt.setDouble(11, t.getValorFlete());
      pstmt.setString(12, t.getEstadoViaje());
      pstmt.setString(13, t.getObservaciones());
      pstmt.setString(14, t.getUsuarioRegistro());

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Error al registrar viaje: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public List<Transporte> listarViajes() {
    List<Transporte> lista = new ArrayList<>();
    String sql = "SELECT t.*, " +
      "tan.placa, tan.capacidad_litros, " +
      "s.nombres, s.apellidos, s.cedula, " +
      "c.razon_social, c.ruc " +
      "FROM transporte t " +
      "JOIN tanqueros tan ON t.id_tanquero = tan.id_tanquero " +
      "JOIN socios s ON t.id_socio = s.id_socio " +
      "LEFT JOIN clientes c ON t.id_cliente = c.id_cliente " +
      "ORDER BY t.fecha_asignacion DESC, t.hora_asignacion DESC";

    try (Connection conn = ConexionBD.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Transporte t = new Transporte();
        t.setIdTransporte(rs.getInt("id_transporte"));

        Tanquero tan = new Tanquero();
        tan.setIdTanquero(rs.getInt("id_tanquero"));
        tan.setPlaca(rs.getString("placa"));
        tan.setCapacidadLitros(rs.getDouble("capacidad_litros"));
        t.setTanquero(tan);

        Socio s = new Socio();
        s.setIdSocio(rs.getInt("id_socio"));
        s.setNombres(rs.getString("nombres"));
        s.setApellidos(rs.getString("apellidos"));
        s.setCedula(rs.getString("cedula"));
        t.setSocio(s);

        if (rs.getInt("id_cliente") > 0) {
          Cliente c = new Cliente();
          c.setIdCliente(rs.getInt("id_cliente"));
          c.setRazonSocial(rs.getString("razon_social"));
          t.setCliente(c);
        }

        java.sql.Date fFin = rs.getDate("fecha_fin");
        if (fFin != null) {
          t.setFechaFin(fFin.toLocalDate());
        }

        java.sql.Time hFin = rs.getTime("hora_fin");
        if (hFin != null) {
          t.setHoraFin(hFin.toLocalTime());
        }

        t.setFechaAsignacion(rs.getDate("fecha_asignacion").toLocalDate());
        t.setHoraAsignacion(rs.getTime("hora_asignacion").toLocalTime());
        t.setRutaOrigen(rs.getString("ruta_origen"));
        t.setRutaDestino(rs.getString("ruta_destino"));
        t.setKilometros(rs.getDouble("kilometros"));
        t.setLitrosTransportados(rs.getDouble("litros_transportados"));

        t.setPorcentajeOcupacion(rs.getDouble("porcentaje_ocupacion"));
        t.setValorFlete(rs.getDouble("valor_flete"));


        t.setEstadoViaje(rs.getString("estado_viaje"));
        t.setObservaciones(rs.getString("observaciones"));

        lista.add(t);
      }

    } catch (SQLException e) {
      System.err.println("Error al listar viajes: " + e.getMessage());
      e.printStackTrace();
    }
    return lista;
  }

  public boolean cambiarEstadoViaje(int idTransporte, String nuevoEstado) {
    String sql;

    if (nuevoEstado.equals("Finalizado") || nuevoEstado.equals("Cancelado")) {
      sql = "UPDATE transporte SET estado_viaje = ?, fecha_fin = CURRENT_DATE, hora_fin = CURRENT_TIME WHERE id_transporte = ?";
    } else {
      sql = "UPDATE transporte SET estado_viaje = ?, fecha_fin = NULL, hora_fin = NULL WHERE id_transporte = ?";
    }

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, nuevoEstado);
      pstmt.setInt(2, idTransporte);

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Error al cambiar estado: " + e.getMessage());
      return false;
    }
  }

  public boolean eliminarViaje(int idTransporte) {
    String sql = "DELETE FROM transporte WHERE id_transporte = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, idTransporte);

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Error al eliminar viaje: " + e.getMessage());
      return false;
    }
  }

}