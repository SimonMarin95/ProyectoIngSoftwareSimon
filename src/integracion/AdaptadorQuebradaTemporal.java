/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Adaptador TEMPORAL de Quebrada (no es el CRUD de Willin)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-17 / RF-32 / CA-17.6
 * Criterios: CA-17.6 — permite probar Resultados mientras se integra Quebrada
 * Estructuras: Arreglo con contador
 * =========================================================== */

package integracion;

/**
 * Stub temporal con quebradas de demostración.
 * Al integrar, reemplazar por un adaptador que llame al gestor de Willin.
 */
public class AdaptadorQuebradaTemporal implements BuscadorQuebrada {

    private String[] quebradasDemo;
    private int cantidad;

    public AdaptadorQuebradaTemporal() {
        quebradasDemo = new String[10];
        cantidad = 0;
        agregar("La Iguaná");
        agregar("La Presidenta");
        agregar("Santa Elena");
    }

    private void agregar(String nombre) {
        if (cantidad < quebradasDemo.length) {
            quebradasDemo[cantidad] = nombre;
            cantidad++;
        }
    }

    // RF-32 · CA-17.6 — búsqueda por nombre (arreglo + contador)
    @Override
    public boolean existeQuebrada(String nombreQuebrada) {
        if (nombreQuebrada == null || nombreQuebrada.trim().isEmpty()) {
            return false;
        }
        String buscado = nombreQuebrada.trim();
        for (int i = 0; i < cantidad; i++) {
            if (quebradasDemo[i].equalsIgnoreCase(buscado)) {
                return true;
            }
        }
        return false;
    }

    // Ayuda en consola — lista nombres del adaptador temporal
    public String listarNombres() {
        if (cantidad == 0) {
            return "(sin quebradas de demostración)";
        }
        String texto = "";
        for (int i = 0; i < cantidad; i++) {
            if (i > 0) {
                texto = texto + ", ";
            }
            texto = texto + quebradasDemo[i];
        }
        return texto;
    }
}
