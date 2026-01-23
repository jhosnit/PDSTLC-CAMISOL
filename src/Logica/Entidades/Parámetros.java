package Logica.Entidades;

public class Parámetros {

  private int id;

  private double iva;
  private String ultimaModificacion;
  private String usuarioModificacion;

  public Parámetros() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getIva() {
    return iva;
  }

  public void setIva(double iva) {
    this.iva = iva;
  }

  public String getUltimaModificacion() {
    return ultimaModificacion;
  }

  public void setUltimaModificacion(String ultimaModificacion) {
    this.ultimaModificacion = ultimaModificacion;
  }

  public String getUsuarioModificacion() {
    return usuarioModificacion;
  }

  public void setUsuarioModificacion(String usuarioModificacion) {
    this.usuarioModificacion = usuarioModificacion;
  }

}
