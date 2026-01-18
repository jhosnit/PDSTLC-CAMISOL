package Presentación.Recursos;


import java.io.*;
import java.util.*;

public class GestorUsuarios {
  private static GestorUsuarios instancia;
  private Map<String, Usuario> usuarios;
  private static final String ARCHIVO_USUARIOS = "usuarios.dat";

  private GestorUsuarios() {
    usuarios = new HashMap<>();
    cargarUsuarios();
  }

  public static GestorUsuarios obtenerInstancia() {
    if (instancia == null) {
      instancia = new GestorUsuarios();
    }
    return instancia;
  }

  private void cargarUsuarios() {
    File archivo = new File(ARCHIVO_USUARIOS);

    if (!archivo.exists()) {
      // Cargar usuarios por defecto
      usuarios.put("admin", new Usuario(1, "admin", "admin", "Gerente"));
      usuarios.put("secre", new Usuario(2, "secretaria", "1234", "Secretaria"));
      guardarUsuarios();
    } else {
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
        usuarios = (Map<String, Usuario>) ois.readObject();
      } catch (Exception e) {
        System.err.println("Error al cargar usuarios: " + e.getMessage());
        usuarios.put("admin", new Usuario(1, "admin", "admin", "Gerente"));
        usuarios.put("secre", new Usuario(2, "secretaria", "1234", "Secretaria"));
      }
    }
  }

  private void guardarUsuarios() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
      oos.writeObject(usuarios);
    } catch (IOException e) {
      System.err.println("Error al guardar usuarios: " + e.getMessage());
    }
  }

  public boolean validarCredenciales(String usuario, String contraseña) {
    Usuario u = usuarios.get(usuario);
    if (u != null) {
      boolean valido = u.getContraseña().equals(contraseña);
      if (valido) {
        u.actualizarUltimoAcceso();
        u.resetearIntentosFallidos();
        guardarUsuarios();
      } else {
        u.incrementarIntentosFallidos();
        guardarUsuarios();
      }
      return valido;
    }
    return false;
  }

  public boolean cambiarContraseña(String usuario, String nuevaContraseña) {
    Usuario u = usuarios.get(usuario);
    if (u != null) {
      u.setContraseña(nuevaContraseña);
      guardarUsuarios();
      return true;
    }
    return false;
  }

  public List<Usuario> obtenerTodosLosUsuarios() {
    return new ArrayList<>(usuarios.values());
  }

  public Usuario obtenerUsuario(String usuario) {
    return usuarios.get(usuario);
  }

  public static class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String usuario;
    private String contraseña;
    private String rol;
    private String ultimoAcceso;
    private int intentosFallidos;

    public Usuario(int id, String usuario, String contraseña, String rol) {
      this.id = id;
      this.usuario = usuario;
      this.contraseña = contraseña;
      this.rol = rol;
      this.ultimoAcceso = "Nunca";
      this.intentosFallidos = 0;
    }

    public void actualizarUltimoAcceso() {
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
      this.ultimoAcceso = sdf.format(new java.util.Date());
    }

    public void incrementarIntentosFallidos() {
      this.intentosFallidos++;
    }

    public void resetearIntentosFallidos() {
      this.intentosFallidos = 0;
    }

    public int getId() {
      return id;
    }

    public String getUsuario() {
      return usuario;
    }

    public String getContraseña() {
      return contraseña;
    }

    public String getRol() {
      return rol;
    }

    public String getUltimoAcceso() {
      return ultimoAcceso;
    }

    public int getIntentosFallidos() {
      return intentosFallidos;
    }

    public void setContraseña(String contraseña) {
      this.contraseña = contraseña;
    }
  }
}
