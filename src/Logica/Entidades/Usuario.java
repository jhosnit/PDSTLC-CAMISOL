package Logica.Entidades;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Usuario {
  private int id;
  private String username;
  private String password;
  private String nombre;
  private String apellido;
  private String rol;
  private boolean estado;
  private Timestamp ultimoAcceso;

  public Usuario() {

  }

  public Usuario(int id, String username, String password, String nombre, String apellido, String rol, boolean estado) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.nombre = nombre;
    this.apellido = apellido;
    this.rol = rol;
    this.estado = estado;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public boolean isEstado() {
    return estado;
  }

  public void setEstado(boolean estado) {
    this.estado = estado;
  }

  public String getUltimoAcceso() {
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    return formato.format(this.ultimoAcceso);
  }

  public void setUltimoAcceso(Timestamp ultimoAcceso) {
    this.ultimoAcceso = ultimoAcceso;
  }

  @Override
  public String toString() {
    return nombre + " " + apellido;
  }

}
