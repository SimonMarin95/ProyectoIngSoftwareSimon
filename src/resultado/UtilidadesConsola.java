/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Resultado de calidad (utilidades de consola — legado)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * Nota           : La pantalla oficial sigue Figura 9.3 en MenuResultados.
 *                  Esta clase queda como apoyo opcional; el mockup no depende de ella.
 * Estructuras: Sin colecciones prohibidas
 * =========================================================== */

package resultado;

import java.util.Scanner;

/**
 * Utilidades auxiliares de consola (el mockup se dibuja en MenuResultados / Main).
 */
public class UtilidadesConsola {

    private UtilidadesConsola() {
    }

    public static void pausa(Scanner sc) {
        System.out.print("ENTER para continuar...");
        sc.nextLine();
    }

    public static String leerTexto(Scanner sc, String etiqueta) {
        System.out.print(etiqueta);
        return sc.nextLine().trim();
    }

    public static int leerOpcion(Scanner sc) {
        String texto = sc.nextLine().trim();
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
