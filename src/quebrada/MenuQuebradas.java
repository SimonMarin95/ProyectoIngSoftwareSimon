/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Quebrada (pantalla / menú)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-01 a HU-06 / RF-01 a RF-09 / RN-01 / RNF-03
 * Criterios: CA-01.1 a CA-05.3 (buenos y de falla)
 * Estructuras: Usa GestorQuebradas (arreglo + pila + archivo)
 * Mockup         : Figura 9.2
 * =========================================================== */

package quebrada;

import java.util.Scanner;

/**
 * Pantalla de consola de Quebradas según el mockup de la Figura 9.2.
 */
public class MenuQuebradas {

    private GestorQuebradas gestor;
    private Scanner sc;
    private String mensaje;

    public MenuQuebradas(GestorQuebradas gestor, Scanner sc) {
        this.gestor = gestor;
        this.sc = sc;
        this.mensaje = "Datos disponibles: " + gestor.getCantidad() + " quebrada(s). (RNF-03)";
    }

    // HU-01 a HU-06 — ejecuta las operaciones del menú de Quebradas.
    public String iniciar() {
        boolean volver = false;
        while (!volver) {
            dibujarPantalla();
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
                    opcionGuardar();
                    break;
                case 2:
                    opcionBuscar();
                    break;
                case 3:
                    mensaje = gestor.getCantidad() == 0
                            ? "(!) Aún no hay quebradas registradas. (RF-08)"
                            : "Listado actualizado. Total de puntos: "
                                    + gestor.calcularTotalPuntos() + " (RF-08)";
                    break;
                case 4:
                    opcionCorregir();
                    break;
                case 5:
                    opcionEliminar();
                    break;
                case 6:
                    mensaje = gestor.deshacerEliminacion();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    mensaje = "(!) Opción no válida. Use 1 a 6 o 0.";
                    break;
            }
        }
        return "Regresó de Quebradas. Registros: " + gestor.getCantidad() + ".";
    }

    // RF-01 a RF-09 — dibuja mensaje, listado y opciones del mockup.
    private void dibujarPantalla() {
        System.out.println();
        System.out.println("CONSOLA · java Main · Monitoreo de calidad del agua");
        System.out.println("===============================================================================");
        System.out.println(" QUEBRADAS");
        System.out.println(" Realiza: HU-01 a HU-06 | CU-01 a CU-06 | RN-01 · RNF-03");
        System.out.println(" Requisitos: RF-01 a RF-09");
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("+-----------------------------------------------------------------------------+");
        System.out.println("| MENSAJE : " + mensaje);
        System.out.println("+-----------------------------------------------------------------------------+");
        System.out.println();
        System.out.println(" La opción 1 (Guardar) pide nombre_quebrada y numero_puntos (1 a 3).");
        System.out.println(" La opción 4 (Corregir) no cambia nombre_quebrada: es el identificador (RF-06).");
        mostrarListado();
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(" OPCIONES                                      REQUISITOS");
        System.out.println(" 1. Guardar   -> crea y guarda la quebrada    RF-01 · RF-02 · RF-03 · RF-04");
        System.out.println(" 2. Buscar    -> nombre completo o parcial   RF-05");
        System.out.println(" 3. Listar    -> lista y suma numero_puntos  RF-08");
        System.out.println(" 4. Corregir  -> actualiza numero_puntos     RF-06");
        System.out.println(" 5. Eliminar  -> quita la quebrada           RF-07");
        System.out.println(" 6. Deshacer  -> devuelve la última eliminada RF-09");
        System.out.println(" 0. Volver    -> regresa al menú principal (Figura 9.1)");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(" OTROS MENSAJES que aparecen en la línea MENSAJE:");
        System.out.println("  RF-02 Esa quebrada ya está registrada");
        System.out.println("  RF-03 El número de puntos debe estar entre 1 y 3");
        System.out.println("  RF-04 No hay espacio para más quebradas");
        System.out.println("  RF-05 Escriba un nombre / No se encontró ninguna coincidencia");
        System.out.println("  RF-06 Esa quebrada no existe / número no válido, se conserva el anterior");
        System.out.println("  RF-07 Escriba el nombre / Esa quebrada no existe");
        System.out.println("  RF-08 Aún no hay quebradas registradas");
        System.out.println("  RF-09 No hay nada que deshacer");
        System.out.println("-------------------------------------------------------------------------------");
    }

    // RF-08 — muestra la lista compacta y los cálculos del diccionario.
    private void mostrarListado() {
        System.out.println(" LISTADO");
        System.out.println("-------------------------------------------------------------------------------");
        Quebrada[] lista = gestor.obtenerCopiaCompacta();
        if (lista.length == 0) {
            System.out.println(" (vacío)");
        } else {
            System.out.printf("%-28s %s%n", "nombre_quebrada", "numero_puntos");
            for (int i = 0; i < lista.length; i++) {
                System.out.printf("%-28s %d%n",
                        lista[i].getNombreQuebrada(), lista[i].getNumeroPuntos());
            }
        }
        System.out.println(" Total de puntos: " + gestor.calcularTotalPuntos()
                + " (RF-08 · CA-05.1)");
        System.out.println(" Quebradas con menos de 3 puntos: "
                + gestor.contarMenosDeTresPuntos() + " (RF-08 · CA-05.3)");
    }

    // RF-01 · RF-02 · RF-03 · RF-04 — registra una quebrada.
    private void opcionGuardar() {
        String nombre = leerCampo("nombre_quebrada: ");
        String puntos = leerCampo("numero_puntos (1 a 3): ");
        mensaje = gestor.registrar(nombre, puntos);
    }

    // RF-05 — consulta por nombre completo o texto parcial.
    private void opcionBuscar() {
        String texto = leerCampo("nombre_quebrada o parte del nombre: ");
        if (texto.trim().isEmpty()) {
            mensaje = "(!) Escriba un nombre o parte de él. (RF-05)";
            return;
        }
        Quebrada[] encontradas = gestor.buscarPorTexto(texto);
        if (encontradas.length == 0) {
            mensaje = "(!) No se encontró ninguna coincidencia. (RF-05)";
            return;
        }
        mensaje = "Se encontraron " + encontradas.length + " coincidencia(s). (RF-05)";
        System.out.println();
        System.out.printf("%-28s %s%n", "nombre_quebrada", "numero_puntos");
        for (int i = 0; i < encontradas.length; i++) {
            System.out.printf("%-28s %d%n",
                    encontradas[i].getNombreQuebrada(), encontradas[i].getNumeroPuntos());
        }
    }

    // RF-06 — actualiza los puntos sin cambiar el nombre identificador.
    private void opcionCorregir() {
        String nombre = leerCampo("nombre_quebrada: ");
        String puntos = leerCampo("nuevo numero_puntos (1 a 3): ");
        mensaje = gestor.actualizar(nombre, puntos);
    }

    // RF-07 — elimina una quebrada por su identificador.
    private void opcionEliminar() {
        String nombre = leerCampo("nombre_quebrada: ");
        mensaje = gestor.eliminar(nombre);
    }

    // RF-01 a RF-07 — lee un campo de consola.
    private String leerCampo(String etiqueta) {
        System.out.print(etiqueta);
        return sc.nextLine();
    }
}
