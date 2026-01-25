package Logica.Entidades;

import java.time.LocalDateTime;

public class Socio {
  private int idSocio;
  private String ruc;
  private String razonSocial;
  private String direccion;
  private String telefono;
  private String correo;
  private boolean estado;
  private LocalDateTime fechaRegistro;
  private LocalDateTime fechaModificacion;
  private String usuarioRegistro;

  public Socio() {
  }

  public Socio(String ruc, String razonSocial, String direccion, String telefono, String correo) {
    this.ruc = ruc;
    this.razonSocial = razonSocial;
    this.direccion = direccion;
    this.telefono = telefono;
    this.correo = correo;
    this.estado = true;
  }

  public int getIdSocio() {
    return idSocio;
  }

  public void setIdSocio(int idSocio) {
    this.idSocio = idSocio;
  }

  public String getRuc() {
    return ruc;
  }

  public void setRuc(String ruc) {
    this.ruc = ruc;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
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
}
