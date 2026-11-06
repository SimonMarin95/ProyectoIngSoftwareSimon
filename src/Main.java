/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Menú principal (integración)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-01 a HU-06 · HU-17 a HU-22 / RNF-03
 * Criterios: CA de carga desde archivo (RNF-03)
 * Estructuras: Arreglo con contador · Pila propia (en módulos de entidad)
 * Mockup         : Figura 9.1
 * =========================================================== */

import java.io.File;
import java.util.Scanner;

import integracion.BuscadorQuebrada;
import quebrada.GestorQuebradas;
import quebrada.MenuQuebradas;
import resultado.GestorResultados;
import resultado.MenuResultados;

/**
 * Pantalla principal del prototipo (Figura 9.1).
 */
public class Main {

    // RNF-03 · CA de persistencia — ruta del archivo de resultados
    private static final String RUTA_RESULTADOS = "datos" + File.separator + "resultados.txt";
    private static final String RUTA_QUEBRADAS = "datos" + File.separator + "quebradas.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // HU-01 a HU-06 — Quebrada implementa el contrato que consulta Resultado.
        GestorQuebradas gestorQuebradas = new GestorQuebradas(100, RUTA_QUEBRADAS);
        BuscadorQuebrada buscadorQuebrada = gestorQuebradas;

        GestorResultados gestorResultados = new GestorResultados(100, RUTA_RESULTADOS, buscadorQuebrada);

        // RNF-03 — recuperar ambas entidades al iniciar.
        int quebradasCargadas = gestorQuebradas.cargarDesdeArchivo();
        int cargados = gestorResultados.cargarDesdeArchivo();
        String mensaje = "Cargadas: " + quebradasCargadas + " quebradas y " + cargados
                + " resultados. (RNF-03)";

        boolean salir = false;
        while (!salir) {
            mostrarPantallaPrincipal(mensaje, gestorQuebradas.getCantidad(),
                    gestorResultados.getCantidad());
            System.out.print("Opción: ");
            String texto = sc.nextLine().trim();
            int opcion;
            try {
                opcion = Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                mensaje = "(!) Opción no válida.";
                continue;
            }

            switch (opcion) {
                case 1:
                    // HU-01 a HU-06 — Figura 9.2.
                    mensaje = new MenuQuebradas(gestorQuebradas, sc).iniciar();
                    break;
                case 2:
                    // HU-17 a HU-22 · CU-17 a CU-22 — Figura 9.3
                    MenuResultados menu = new MenuResultados(gestorResultados, sc);
                    mensaje = menu.iniciar();
                    break;
                case 0:
                    // RNF-03 — grabar ambas entidades y cerrar.
                    boolean quebradasGuardadas = gestorQuebradas.guardarEnArchivo();
                    boolean resultadosGuardados = gestorResultados.guardarEnArchivo();
                    System.out.println();
                    if (quebradasGuardadas && resultadosGuardados) {
                        System.out.println("MENSAJE : Datos guardados en " + RUTA_QUEBRADAS
                                + " y " + RUTA_RESULTADOS + ". (RNF-03)");
                    } else {
                        System.out.println("MENSAJE : No se pudieron guardar todos los datos. "
                                + "Revise los mensajes de error anteriores.");
                    }
                    System.out.println("Programa cerrado.");
                    salir = true;
                    break;
                default:
                    mensaje = "(!) Opción no válida. Use 1, 2 o 0.";
                    break;
            }
        }
        sc.close();
    }

    // RNF-03 — dibuja la pantalla principal según Figura 9.1
    private static void mostrarPantallaPrincipal(String mensaje, int cantidadQuebradas,
                                                  int cantidadResultados) {
        System.out.println();
        System.out.println("CONSOLA · java Main · Monitoreo de calidad del agua");
        System.out.println("===============================================================================");
        System.out.println(" MONITOREO DE CALIDAD DEL AGUA · QUEBRADA LA IGUANÁ (pantalla principal)");
        System.out.println(" Realiza: HU-01 a HU-06 · HU-17 a HU-22 | CU-01 a CU-06 · CU-17 a CU-22 | RNF-03");
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("+-----------------------------------------------------------------------------+");
        System.out.println("| MENSAJE : " + ajustarMensaje(mensaje));
        System.out.println("+-----------------------------------------------------------------------------+");
        System.out.println();
        System.out.println(" Quebradas en memoria: " + cantidadQuebradas
                + " | Resultados en memoria: " + cantidadResultados);
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(" OPCIONES");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(" 1. Quebradas   -> abre la pantalla de quebradas (Figura 9.2)");
        System.out.println("                 HU-01 a HU-06 · CU-01 a CU-06");
        System.out.println(" 2. Resultados  -> abre la pantalla de resultados de calidad (Figura 9.3)");
        System.out.println("                 HU-17 a HU-22 · CU-17 a CU-22");
        System.out.println(" 0. Salir       -> graba en el archivo y cierra el programa (RNF-03)");
        System.out.println("-------------------------------------------------------------------------------");
    }

    private static String ajustarMensaje(String mensaje) {
        if (mensaje == null) {
            return "";
        }
        if (mensaje.length() > 70) {
            return mensaje.substring(0, 67) + "...";
        }
        return mensaje;
    }
}
