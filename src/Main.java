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

import integracion.AdaptadorQuebradaTemporal;
import integracion.BuscadorQuebrada;
import resultado.GestorResultados;
import resultado.MenuResultados;

/**
 * Pantalla principal del prototipo (Figura 9.1).
 */
public class Main {

    // RNF-03 · CA de persistencia — ruta del archivo de resultados
    private static final String RUTA_RESULTADOS = "datos" + File.separator + "resultados.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // RF-32 — contrato con Quebrada (adaptador temporal hasta integrar a Willin)
        AdaptadorQuebradaTemporal adaptadorQuebrada = new AdaptadorQuebradaTemporal();
        BuscadorQuebrada buscadorQuebrada = adaptadorQuebrada;

        GestorResultados gestorResultados = new GestorResultados(100, RUTA_RESULTADOS, buscadorQuebrada);

        // RNF-03 — recuperar registros al iniciar
        int cargados = gestorResultados.cargarDesdeArchivo();
        String mensaje = "Se cargaron " + cargados
                + " resultado(s) desde el archivo. (RNF-03)";

        boolean salir = false;
        while (!salir) {
            mostrarPantallaPrincipal(mensaje, gestorResultados.getCantidad());
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
                    // HU-01 a HU-06 — pantalla de Willin (aún no integrada)
                    mensaje = "(!) Módulo Quebradas pendiente de integrar (Willin). "
                            + "Hoy se valida con AdaptadorQuebradaTemporal: "
                            + adaptadorQuebrada.listarNombres();
                    break;
                case 2:
                    // HU-17 a HU-22 · CU-17 a CU-22 — Figura 9.3
                    MenuResultados menu = new MenuResultados(gestorResultados, sc, adaptadorQuebrada);
                    mensaje = menu.iniciar();
                    break;
                case 0:
                    // RNF-03 — grabar y cerrar
                    gestorResultados.guardarEnArchivo();
                    System.out.println();
                    System.out.println("MENSAJE : Datos guardados en " + RUTA_RESULTADOS + ". (RNF-03)");
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
    private static void mostrarPantallaPrincipal(String mensaje, int cantidadResultados) {
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
        System.out.println(" Resultados en memoria: " + cantidadResultados);
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
