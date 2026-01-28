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

  // --- LÓGICA DE NEGOCIO ---

  /**
   * Calcula el factor de precio por litro según los kilómetros recorridos.
   * Basado en la Tabla del Anexo 8 del documento ERS.
   */
  public double obtenerFactorPrecio(double kilometros) {
    if (kilometros <= 100) return 0.013;
    if (kilometros <= 200) return 0.015;
    if (kilometros <= 250) return 0.017;
    if (kilometros <= 300) return 0.020;
    if (kilometros <= 350) return 0.023;
    if (kilometros <= 400) return 0.024;
    if (kilometros <= 450) return 0.025;
    if (kilometros <= 500) return 0.026;
    if (kilometros <= 550) return 0.027;
    if (kilometros <= 600) return 0.028;
    if (kilometros <= 650) return 0.029;
    return 0.030; // Más de 650km
  }

  public double calcularCostoFlete(double kilometros, double litros) {
    double factor = obtenerFactorPrecio(kilometros);
    return litros * factor;
  }

  public double calcularOcupacion(double litros, double capacidadMaxima) {
    if (capacidadMaxima <= 0) return 0;
    return (litros / capacidadMaxima) * 100;
  }

  // --- BASE DE DATOS ---

  public boolean registrarViaje(Transporte transporte) {
    String sql = "INSERT INTO transporte (id_tanquero, id_socio, id_cliente, fecha_asignacion, " +
      "hora_asignacion, ruta_origen, ruta_destino, kilometros, litros_transportados, " +
      "estado_viaje, observaciones, usuario_registro) " +
      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, transporte.getTanquero().getIdTanquero());
      pstmt.setInt(2, transporte.getSocio().getIdSocio());
      pstmt.setInt(3, transporte.getCliente().getIdCliente()); // ← NUEVO
      pstmt.setDate(4, Date.valueOf(transporte.getFechaAsignacion()));
      pstmt.setTime(5, Time.valueOf(transporte.getHoraAsignacion()));
      pstmt.setString(6, transporte.getRutaOrigen());
      pstmt.setString(7, transporte.getRutaDestino());
      pstmt.setDouble(8, transporte.getKilometros());
      pstmt.setDouble(9, transporte.getLitrosTransportados());
      pstmt.setString(10, transporte.getEstadoViaje());
      pstmt.setString(11, transporte.getObservaciones());
      pstmt.setString(12, transporte.getUsuarioRegistro());

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
      "ta.placa, ta.capacidad_litros, " +
      "s.cedula, s.nombres, s.apellidos, " +
      "c.ruc, c.razon_social " +
      "FROM transporte t " +
      "INNER JOIN tanqueros ta ON t.id_tanquero = ta.id_tanquero " +
      "INNER JOIN socios s ON t.id_socio = s.id_socio " +
      "LEFT JOIN clientes c ON t.id_cliente = c.id_cliente " +
      "ORDER BY t.fecha_asignacion DESC, t.hora_asignacion DESC";

    try (Connection conn = ConexionBD.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        lista.add(mapearTransporte(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lista;
  }

  private Transporte mapearTransporte(ResultSet rs) throws SQLException {
    Transporte t = new Transporte();
    t.setIdTransporte(rs.getInt("id_transporte"));

    // Tanquero
    Tanquero tanquero = new Tanquero();
    tanquero.setIdTanquero(rs.getInt("id_tanquero"));
    tanquero.setPlaca(rs.getString("placa"));
    tanquero.setCapacidadLitros(rs.getDouble("capacidad_litros"));
    t.setTanquero(tanquero);

    // Socio
    Socio socio = new Socio();
    socio.setIdSocio(rs.getInt("id_socio"));
    socio.setCedula(rs.getString("cedula"));
    socio.setNombres(rs.getString("nombres"));
    socio.setApellidos(rs.getString("apellidos"));
    t.setSocio(socio);

    // Cliente ← NUEVO
    Cliente cliente = new Cliente();
    cliente.setIdCliente(rs.getInt("id_cliente"));
    cliente.setRuc(rs.getString("ruc"));
    cliente.setRazonSocial(rs.getString("razon_social"));
    t.setCliente(cliente);

    // Resto de datos
    t.setFechaAsignacion(rs.getDate("fecha_asignacion").toLocalDate());
    t.setHoraAsignacion(rs.getTime("hora_asignacion").toLocalTime());
    t.setRutaOrigen(rs.getString("ruta_origen"));
    t.setRutaDestino(rs.getString("ruta_destino"));
    t.setKilometros(rs.getDouble("kilometros"));
    t.setLitrosTransportados(rs.getDouble("litros_transportados"));
    t.setEstadoViaje(rs.getString("estado_viaje"));
    t.setObservaciones(rs.getString("observaciones"));
    t.setUsuarioRegistro(rs.getString("usuario_registro"));

    return t;
  }

  public int contarViajesActivos() {
    String sql = "SELECT COUNT(*) as total FROM transporte WHERE estado_viaje = 'En Curso'";
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

  /**
   * Cambia el estado de un viaje
   */
  public boolean cambiarEstadoViaje(int idTransporte, String nuevoEstado) {
    String sql = "UPDATE transporte SET estado_viaje = ?, fecha_modificacion = CURRENT_TIMESTAMP " +
      "WHERE id_transporte = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, nuevoEstado);
      pstmt.setInt(2, idTransporte);

      return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
      System.err.println("Error al cambiar estado del viaje: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Elimina (lógicamente) un viaje
   */
  public boolean eliminarViaje(int idTransporte) {
    // Cambiar el estado a "Cancelado" en lugar de eliminar físicamente
    return cambiarEstadoViaje(idTransporte, "Cancelado");
  }


}
