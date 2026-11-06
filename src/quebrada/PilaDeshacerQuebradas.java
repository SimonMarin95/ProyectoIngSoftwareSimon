/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Quebrada (pila de deshacer)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-06 / RF-09
 * Criterios: deshacer la última quebrada eliminada
 * Estructuras: Pila LIFO propia con arreglo y contador (sin Stack)
 * =========================================================== */

package quebrada;

/**
 * Pila LIFO de quebradas eliminadas.
 */
public class PilaDeshacerQuebradas {

    private Quebrada[] elementos;
    private int cima;

    public PilaDeshacerQuebradas(int capacidad) {
        int capacidadReal = capacidad < 1 ? 20 : capacidad;
        elementos = new Quebrada[capacidadReal];
        cima = 0;
    }

    // RF-09 — indica si no hay eliminaciones pendientes.
    public boolean estaVacia() {
        return cima == 0;
    }

    // RF-09 — indica si se alcanzó la capacidad fija de la pila.
    public boolean estaLlena() {
        return cima >= elementos.length;
    }

    // RF-09 — devuelve la cantidad de elementos apilados.
    public int tamanio() {
        return cima;
    }

    // RF-09 — apila una copia de la quebrada eliminada.
    public void apilar(Quebrada quebrada) {
        if (quebrada == null) {
            return;
        }
        if (estaLlena()) {
            for (int i = 0; i < elementos.length - 1; i++) {
                elementos[i] = elementos[i + 1];
            }
            cima = elementos.length - 1;
        }
        elementos[cima] = quebrada.copiar();
        cima++;
    }

    // RF-09 — desapila y devuelve solo el último elemento insertado.
    public Quebrada desapilar() {
        if (estaVacia()) {
            return null;
        }
        cima--;
        Quebrada recuperada = elementos[cima];
        elementos[cima] = null;
        return recuperada;
    }

    // RF-09 — consulta el último elemento sin retirarlo.
    public Quebrada verCima() {
        if (estaVacia()) {
            return null;
        }
        return elementos[cima - 1];
    }
}
