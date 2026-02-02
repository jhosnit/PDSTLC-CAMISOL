package Logica.Entidades;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transporte {
  private int idTransporte;

  // Relaciones (Objetos completos para poder acceder a sus datos como Placa o Nombre)
  private Tanquero tanquero;
  private Socio socio;
  private Cliente cliente;
  private LocalDate fechaAsignacion;
  private LocalTime horaAsignacion;
  private LocalDate fechaFin;
  private LocalTime horaFin;
  private String rutaOrigen;
  private String rutaDestino;
  private double kilometros;
  private double litrosTransportados;
  private String estadoViaje; // "En Curso", "Finalizado", etc.
  private String observaciones;
  private String usuarioRegistro;


  private double valorFlete;
  private double porcentajeOcupacion;

  public Transporte() {
  }

  public Transporte(Tanquero tanquero, Socio socio, LocalDate fecha, LocalTime hora,
                    String origen, String destino, double kms, double litros) {
    this.tanquero = tanquero;
    this.socio = socio;
    this.fechaAsignacion = fecha;
    this.horaAsignacion = hora;
    this.rutaOrigen = origen;
    this.rutaDestino = destino;
    this.kilometros = kms;
    this.litrosTransportados = litros;
    this.estadoViaje = "En Curso";
  }

  public int getIdTransporte() {
    return idTransporte;
  }

  public void setIdTransporte(int idTransporte) {
    this.idTransporte = idTransporte;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public Tanquero getTanquero() {
    return tanquero;
  }

  public void setTanquero(Tanquero tanquero) {
    this.tanquero = tanquero;
  }

  public Socio getSocio() {
    return socio;
  }

  public void setSocio(Socio socio) {
    this.socio = socio;
  }

  public LocalDate getFechaAsignacion() {
    return fechaAsignacion;
  }

  public void setFechaAsignacion(LocalDate fechaAsignacion) {
    this.fechaAsignacion = fechaAsignacion;
  }

  public LocalTime getHoraAsignacion() {
    return horaAsignacion;
  }

  public void setHoraAsignacion(LocalTime horaAsignacion) {
    this.horaAsignacion = horaAsignacion;
  }

  public String getRutaOrigen() {
    return rutaOrigen;
  }

  public void setRutaOrigen(String rutaOrigen) {
    this.rutaOrigen = rutaOrigen;
  }

  public String getRutaDestino() {
    return rutaDestino;
  }

  public void setRutaDestino(String rutaDestino) {
    this.rutaDestino = rutaDestino;
  }

  public double getKilometros() {
    return kilometros;
  }

  public void setKilometros(double kilometros) {
    this.kilometros = kilometros;
  }

  public double getLitrosTransportados() {
    return litrosTransportados;
  }

  public void setLitrosTransportados(double litrosTransportados) {
    this.litrosTransportados = litrosTransportados;
  }

  public String getEstadoViaje() {
    return estadoViaje;
  }

  public void setEstadoViaje(String estadoViaje) {
    this.estadoViaje = estadoViaje;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getUsuarioRegistro() {
    return usuarioRegistro;
  }

  public void setUsuarioRegistro(String usuarioRegistro) {
    this.usuarioRegistro = usuarioRegistro;
  }

  public double getValorFlete() {
    return valorFlete;
  }

  public void setValorFlete(double valorFlete) {
    this.valorFlete = valorFlete;
  }

  public double getPorcentajeOcupacion() {
    return porcentajeOcupacion;
  }

  public void setPorcentajeOcupacion(double porcentajeOcupacion) {
    this.porcentajeOcupacion = porcentajeOcupacion;
  }

  public LocalDate getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(LocalDate fechaFin) {
    this.fechaFin = fechaFin;
  }

  public LocalTime getHoraFin() {
    return horaFin;
  }

  public void setHoraFin(LocalTime horaFin) {
    this.horaFin = horaFin;
  }

}