/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Resultado de calidad (pila de deshacer)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-22 / RF-39 / RN-04
 * Criterios: CA-22.1 (bueno) CA-22.2 CA-22.3 (falla)
 * Estructuras: Pila propia con arreglo y contador (sin Stack)
 * =========================================================== */

package resultado;

/**
 * Pila LIFO propia para recuperar el último resultado eliminado.
 */
public class PilaDeshacer {

    private ResultadoCalidad[] elementos;
    private int cima;
    private int capacidad;

    public PilaDeshacer(int capacidad) {
        if (capacidad < 1) {
            capacidad = 20;
        }
        this.capacidad = capacidad;
        this.elementos = new ResultadoCalidad[capacidad];
        this.cima = 0;
    }

    // RF-39 · CA-22.2 — indica si no hay eliminaciones pendientes
    public boolean estaVacia() {
        return cima == 0;
    }

    public boolean estaLlena() {
        return cima >= capacidad;
    }

    public int tamanio() {
        return cima;
    }

    // RF-39 · CA-22.1 — apila una copia del resultado eliminado
    public void apilar(ResultadoCalidad resultado) {
        if (resultado == null) {
            return;
        }
        if (estaLlena()) {
            for (int i = 0; i < capacidad - 1; i++) {
                elementos[i] = elementos[i + 1];
            }
            cima = capacidad - 1;
        }
        elementos[cima] = resultado.copiar();
        cima++;
    }

    // RF-39 · CA-22.1 · CA-22.3 — desapila solo el último (LIFO)
    public ResultadoCalidad desapilar() {
        if (estaVacia()) {
            return null;
        }
        cima--;
        ResultadoCalidad sacado = elementos[cima];
        elementos[cima] = null;
        return sacado;
    }

    // RF-39 — consulta la cima sin quitar
    public ResultadoCalidad verCima() {
        if (estaVacia()) {
            return null;
        }
        return elementos[cima - 1];
    }
}
