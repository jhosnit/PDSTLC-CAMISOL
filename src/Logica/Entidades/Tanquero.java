package Logica.Entidades;

import java.time.LocalDateTime;

public class Tanquero {
  private int idTanquero;
  private String placa;
  private String marca;
  private String modelo;
  private int anioFabricacion;
  private double capacidadLitros;
  private boolean estado;
  private LocalDateTime fechaRegistro;
  private LocalDateTime fechaModificacion;
  private String usuarioRegistro;


  public Tanquero() {
  }

  public Tanquero(String placa, String marca, String modelo, int anioFabricacion, double capacidadLitros) {
    this.placa = placa;
    this.marca = marca;
    this.modelo = modelo;
    this.anioFabricacion = anioFabricacion;
    this.capacidadLitros = capacidadLitros;
    this.estado = true;
  }

  public int getIdTanquero() {
    return idTanquero;
  }

  public void setIdTanquero(int idTanquero) {
    this.idTanquero = idTanquero;
  }

  public String getPlaca() {
    return placa;
  }

  public void setPlaca(String placa) {
    this.placa = placa;
  }

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getModelo() {
    return modelo;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public int getAnioFabricacion() {
    return anioFabricacion;
  }

  public void setAnioFabricacion(int anioFabricacion) {
    this.anioFabricacion = anioFabricacion;
  }

  public double getCapacidadLitros() {
    return capacidadLitros;
  }

  public void setCapacidadLitros(double capacidadLitros) {
    this.capacidadLitros = capacidadLitros;
  }

  public boolean isEstado() {
    return estado;
  }

  public void setEstado(boolean estado) {
    this.estado = estado;
  }

  public LocalDateTime getFechaRegistro() {
    return fechaRegistro;
  }

  public void setFechaRegistro(LocalDateTime fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

  public LocalDateTime getFechaModificacion() {
    return fechaModificacion;
  }

  public void setFechaModificacion(LocalDateTime fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
  }

  public String getUsuarioRegistro() {
    return usuarioRegistro;
  }

  public void setUsuarioRegistro(String usuarioRegistro) {
    this.usuarioRegistro = usuarioRegistro;
  }

  @Override
  public String toString() {
    return placa + " (" + capacidadLitros + "L)";
  }

}
