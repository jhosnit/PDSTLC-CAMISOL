package Logica.Utilidades;

public class Validaciones {

  // ================= VALIDACIÓN DE CÉDULA (ANEXO 3) =================

  public static boolean validarCédula(String cédulaAVerificar) {
    return validarLongitud(cédulaAVerificar)
      && validarDígitosIniciales(cédulaAVerificar)
      && validarDígitosNuméricos(cédulaAVerificar) // Validación básica de números
      && validarTercerDígito(cédulaAVerificar)
      && validarDígitoVerificador(cédulaAVerificar);
  }

  private static boolean validarLongitud(String cédulaAVerificar) {
    return cédulaAVerificar != null && cédulaAVerificar.length() == 10;
  }

  private static boolean validarDígitosIniciales(String cédulaAVerificar) {
    try {
      int provincia = Integer.parseInt(cédulaAVerificar.substring(0, 2));
      // Las provincias en Ecuador van del 01 al 24 (más 30 para extranjeros, si aplica)
      return provincia >= 1 && provincia <= 24;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private static boolean validarDígitosNuméricos(String cédulaAVerificar) {
    return cédulaAVerificar.matches("^[0-9]+$");
  }

  private static boolean validarTercerDígito(String cédulaAVerificar) {
    // Para personas naturales (cédula), el tercer dígito debe ser menor a 6 (0-5)
    int tercerDigito = Integer.parseInt(String.valueOf(cédulaAVerificar.charAt(2)));
    return tercerDigito >= 0 && tercerDigito < 6;
  }

  private static boolean validarDígitoVerificador(String cédulaAVerificar) {
    int suma = 0;
    int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2}; // Coeficientes para los primeros 9 dígitos

    for (int i = 0; i < coeficientes.length; i++) {
      int valor = Integer.parseInt(String.valueOf(cédulaAVerificar.charAt(i))) * coeficientes[i];

      if (valor >= 10) {
        valor = valor - 9;
      }
      suma += valor;
    }

    // Lógica corregida para el dígito verificador
    int residuo = suma % 10;
    int digitoVerificadorCalculado = (residuo == 0) ? 0 : (10 - residuo);

    int digitoVerificadorReal = Integer.parseInt(String.valueOf(cédulaAVerificar.charAt(9)));

    return digitoVerificadorCalculado == digitoVerificadorReal;
  }

  // ================= VALIDACIÓN DE RUC (ANEXO 1) =================

  public static boolean validarRUC(String ruc) {
    if (ruc == null || ruc.length() != 13) {
      return false;
    }

    if (!ruc.endsWith("001")) {
      return false;
    }

    // Para Personas Naturales, el RUC es la Cédula + 001
    String cedulaParte = ruc.substring(0, 10);
    return validarCédula(cedulaParte);
  }

  // ================= VALIDACIÓN DE CORREO (ANEXO 4) =================

  public static boolean validarCorreo(String correo) {
    if (correo == null || correo.trim().isEmpty()) {
      return false;
    }

    // Uso de Regex más robusto para simplificar y asegurar compatibilidad
    String regexCorreo = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    return correo.matches(regexCorreo);
  }

  // ================= VALIDACIÓN DE TELÉFONO =================

  public static boolean validarTelefono(String telefono) {
    if (telefono == null || telefono.length() != 10) {
      return false;
    }

    if (!telefono.startsWith("09")) {
      return false;
    }

    return telefono.matches("^[0-9]+$");
  }

  // ================= VALIDACIÓN DE PLACA (ANEXO 6) =================

  public static boolean validarPlaca(String placa) {
    if (placa == null) {
      return false;
    }

    // Formato: ABC-1234 o ABC-123 (3 letras, guion, 3 o 4 números)
    if (!placa.matches("^[A-Z]{3}-[0-9]{3,4}$")) {
      return false;
    }

    // Validar primera letra según provincias (Anexo 6 del PDF)
    char primeraLetra = placa.charAt(0);
    String provinciasValidas = "ABCEGHIJKLMNOPQRSTUVWXYZ"; // Lista de códigos de provincias

    return provinciasValidas.indexOf(primeraLetra) != -1;
  }
}
