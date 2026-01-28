package Logica.Utilidades;

public class CalculadoraFlete {

  public double obtenerFactorPrecio(double kilometros) {
    // Lógica basada en Anexo 8 [cite: 1962-1994]
    if (kilometros >= 0 && kilometros <= 100) return 0.013;
    if (kilometros >= 101 && kilometros <= 200) return 0.015;
    if (kilometros >= 201 && kilometros <= 250) return 0.017;
    if (kilometros >= 251 && kilometros <= 300) return 0.020;
    if (kilometros >= 301 && kilometros <= 350) return 0.023;
    if (kilometros >= 351 && kilometros <= 400) return 0.024;
    if (kilometros >= 401 && kilometros <= 450) return 0.025;
    if (kilometros >= 451 && kilometros <= 500) return 0.026;
    if (kilometros >= 501 && kilometros <= 550) return 0.027;
    if (kilometros >= 551 && kilometros <= 600) return 0.028;
    if (kilometros >= 601 && kilometros <= 650) return 0.029;

    return 0.030; // Valor por defecto si supera 650km (o lo que definas)
  }

  public double calcularCostoTotal(double kilometros, double litros) {
    double factor = obtenerFactorPrecio(kilometros);
    return litros * factor;
  }
}
