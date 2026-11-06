/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Quebrada
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-01 a HU-06 / RF-01 a RF-09 / RN-01
 * Criterios: CA-01.1 a CA-05.3
 * Estructuras: Objeto de dominio (sin colecciones)
 * =========================================================== */

package quebrada;

import java.util.Locale;

/**
 * Registro de una quebrada según el mockup de la Figura 9.2.
 */
public class Quebrada {

    private String nombre_quebrada;
    private int numero_puntos;

    public Quebrada(String nombreQuebrada, int numeroPuntos) {
        this.nombre_quebrada = nombreQuebrada;
        this.numero_puntos = numeroPuntos;
    }

    // RF-09 — copia independiente para la pila de deshacer.
    public Quebrada copiar() {
        return new Quebrada(nombre_quebrada, numero_puntos);
    }

    public String getNombreQuebrada() {
        return nombre_quebrada;
    }

    public int getNumeroPuntos() {
        return numero_puntos;
    }

    public void setNumeroPuntos(int numeroPuntos) {
        this.numero_puntos = numeroPuntos;
    }

    // RF-02 — clave única sin distinguir mayúsculas ni espacios externos.
    public String claveUnica() {
        return nombre_quebrada == null ? "" : nombre_quebrada.trim().toLowerCase(Locale.ROOT);
    }
}
