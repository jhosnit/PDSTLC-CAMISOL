package Logica.Utilidades;

public class Validaciones {

  public static boolean validarCédula(String cédulaAVerificar) {
    return validarLongitud(cédulaAVerificar)
      && validarDígitosIniciales(cédulaAVerificar)
      && validarDígitoVerificador(cédulaAVerificar)
      && validarDígitosNuméricos(cédulaAVerificar)
      && validarTercerDígito(cédulaAVerificar);
  }

  private static boolean validarTercerDígito(String cédulaAVerificar) {
    return Integer.parseInt(String.valueOf(cédulaAVerificar.charAt(2))) >= 0
      && Integer.parseInt(String.valueOf(cédulaAVerificar.charAt(2))) <= 5;
  }

  private static boolean validarDígitosNuméricos(String cédulaAVerificar) {
    return cédulaAVerificar.matches("^[0-9]*$");
  }

  private static boolean validarDígitoVerificador(String cédulaAVerificar) {
    int númeroVerificador = 0;
    for(int i = 0; i < cédulaAVerificar.length() - 1 ; i++) {
      int valorAux;
      if (i % 2 == 0) {
        valorAux = Integer.parseInt(String.valueOf(cédulaAVerificar.charAt(i))) * 2;
      } else {
        valorAux = Integer.parseInt(String.valueOf(cédulaAVerificar.charAt(i)));
      }
      if (valorAux >= 10) {
        valorAux = valorAux - 9;
      }
      númeroVerificador = númeroVerificador + valorAux;
    }
    if(númeroVerificador != 0){
      númeroVerificador = númeroVerificador % 10;
      númeroVerificador = 10 - númeroVerificador;
    }
    return(númeroVerificador == (Integer.parseInt(String.valueOf(cédulaAVerificar.charAt(9)))));
  }

  private static boolean validarDígitosIniciales(String cédulaAVerificar) {
    return Integer.parseInt(cédulaAVerificar.substring(0,2)) <= 24
      && Integer.parseInt(cédulaAVerificar.substring(0,2)) >= 1;
  }

  private static boolean validarLongitud(String cédulaAVerificar) {
    return cédulaAVerificar.length() == 10;
  }

  public static boolean validarRUC(String ruc) {
    if (ruc == null || ruc.length() != 13) {
      return false;
    }

    if (!ruc.endsWith("001")) {
      return false;
    }

    String cedulaParte = ruc.substring(0, 10);
    return validarCédula(cedulaParte);
  }

  public static boolean validarCorreo(String correo) {
    if (correo == null || correo.trim().isEmpty()) {
      return false;
    }

    if (correo.indexOf('@') != correo.lastIndexOf('@')) {
      return false;
    }

    String[] partes = correo.split("@");
    if (partes.length != 2) {
      return false;
    }

    String nombreUsuario = partes[0];
    String dominio = partes[1];

    if (!validarNombreUsuario(nombreUsuario)) {
      return false;
    }

    if (!validarDominio(dominio)) {
      return false;
    }

    return true;
  }

  private static boolean validarNombreUsuario(String nombreUsuario) {

    if (nombreUsuario.isEmpty()) {
      return false;
    }

    if (nombreUsuario.startsWith(".") || nombreUsuario.endsWith(".")) {
      return false;
    }

    if (nombreUsuario.contains("..")) {
      return false;
    }

    if (nombreUsuario.contains(" ")) {
      return false;
    }

    String regex = "^[a-zA-Z0-9._%-+]+$";
    if (!nombreUsuario.matches(regex)) {
      return false;
    }

    return true;
  }

  private static boolean validarDominio(String dominio) {
    if (dominio.isEmpty()) {
      return false;
    }

    if (!dominio.contains(".")) {
      return false;
    }

    if (dominio.startsWith(".") || dominio.endsWith(".")
      || dominio.startsWith("-") || dominio.endsWith("-")) {
      return false;
    }

    if (dominio.contains(" ")) {
      return false;
    }

    String regex = "^[a-zA-Z0-9.-]+$";
    if (!dominio.matches(regex)) {
      return false;
    }

    String[] partesDominio = dominio.split("\\.");
    String tld = partesDominio[partesDominio.length - 1];
    if (tld.length() < 2) {
      return false;
    }

    return true;
  }

  public static boolean validarTelefono(String telefono) {
    if (telefono == null || telefono.length() != 10) {
      return false;
    }

    if (!telefono.startsWith("09")) {
      return false;
    }

    return telefono.matches("^[0-9]+$");
  }

  public static boolean validarPlaca(String placa) {
    if (placa == null || (placa.length() != 7 && placa.length() != 8)) {
      return false;
    }

    // Formato: ABC-1234 o ABC-123
    if (!placa.matches("^[A-Z]{3}-[0-9]{3,4}$")) {
      return false;
    }

    // Validar primera letra (provincias válidas - Anexo B)
    char primeraLetra = placa.charAt(0);
    String provinciasValidas = "ABCEGHIJKLMNOPQRSTUVWXYZ";

    return provinciasValidas.indexOf(primeraLetra) != -1;
  }
}
