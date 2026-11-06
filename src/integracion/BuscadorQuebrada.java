/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Contrato de validación entre Quebrada y Resultado
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-17 / RF-32 / CA-17.6
 * Criterios: CA-17.6 (falla) — rechaza si la quebrada no existe
 * Estructuras: Interfaz (sin CRUD de Quebrada)
 * =========================================================== */

package integracion;

/**
 * Contrato que el módulo de Resultado necesita del módulo de Quebrada.
 * No implementa el CRUD de quebradas: solo consulta existencia.
 */
public interface BuscadorQuebrada {

    // RF-32 · CA-17.6 — true si la quebrada está registrada
    boolean existeQuebrada(String nombreQuebrada);
}
