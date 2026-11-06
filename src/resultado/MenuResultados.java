/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Resultado de calidad (pantalla / menú)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-17 a HU-22 / RF-28 a RF-39 / RN-02 · RN-04 · RN-11
 * Criterios: CA-17.1 a CA-22.3 (buenos y falla)
 * Estructuras: Arreglo con contador · Pila propia
 * Mockup         : Figura 9.3
 * =========================================================== */

package resultado;

import java.util.Locale;
import java.util.Scanner;

/**
 * Pantalla de Resultados de calidad según el mockup de la Figura 9.3.
 */
public class MenuResultados {

    private GestorResultados gestor;
    private ComparadorCalidad comparador;
    private Scanner sc;
    private String mensaje;

    public MenuResultados(GestorResultados gestor, Scanner sc) {
        this.gestor = gestor;
        this.comparador = new ComparadorCalidad(gestor);
        this.sc = sc;
        this.mensaje = "Listo. Elija una opción.";
    }

    /**
     * Ciclo de la pantalla Figura 9.3.
     * @return mensaje para devolver al menú principal
     */
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
                    // RF-28 a RF-34 — Guardar
                    opcionGuardar();
                    break;
                case 2:
                    // RF-35 — Buscar
                    opcionBuscar();
                    break;
                case 3:
                    // RF-38 — Listar
                    opcionListar();
                    break;
                case 4:
                    // RF-36 — Corregir
                    opcionCorregir();
                    break;
                case 5:
                    // RF-37 — Eliminar
                    opcionEliminar();
                    break;
                case 6:
                    // RF-39 — Deshacer
                    opcionDeshacer();
                    break;
                case 7:
                    // HU-34 · RF-64 — comparación entre puntos (consulta)
                    opcionCompararPuntos();
                    break;
                case 8:
                    // HU-34 · RF-63 · RF-65 — comparación entre temporadas
                    opcionCompararTemporadas();
                    break;
                case 9:
                    // Evolución histórica (consulta)
                    opcionEvolucion();
                    break;
                case 10:
                    // Datos de demostración (apoyo a pruebas, no está en el mockup CRUD)
                    opcionCargarDemostracion();
                    break;
                case 0:
                    // Volver → Figura 9.1
                    volver = true;
                    break;
                default:
                    mensaje = "(!) Opción no válida.";
                    break;
            }
        }
        return "Regresó de Resultados. Registros: " + gestor.getCantidad() + ".";
    }

    // Dibuja la pantalla completa según Figura 9.3
    private void dibujarPantalla() {
        System.out.println();
        System.out.println("CONSOLA · java Main · Monitoreo de calidad del agua");
        System.out.println("===============================================================================");
        System.out.println(" RESULTADOS DE CALIDAD");
        System.out.println(" Realiza: HU-17 a HU-22 | CU-17 a CU-22 | RN-02 · RN-04 · RN-11");
        System.out.println(" RF-28 a RF-39");
        System.out.println(" La quebrada, la campaña y el punto se validan contra las otras pantallas.");
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("+-----------------------------------------------------------------------------+");
        System.out.println("| MENSAJE : " + formatearMensajeCaja(mensaje));
        System.out.println("+-----------------------------------------------------------------------------+");
        System.out.println();
        System.out.println(" La opción 1 (Guardar) pide: nombre_quebrada, fecha_campana, temporada");
        System.out.println(" (de la campaña), nombre_punto, valor_ica, categoria_ica");
        System.out.println(" (fuente se llena sola: RedRío).");
        System.out.println(" La opción 4 (Corregir) NO cambia nombre_punto ni fecha_campana");
        System.out.println(" (identifican el resultado) (RF-36 · CA-19.3).");
        System.out.println();
        mostrarListadoResumen();
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(" OPCIONES");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(" 1. Guardar   Pide los campos, valida y graba (RF-28 a RF-34)");
        System.out.println(" 2. Buscar    Texto en categoria_ica o por punto+campaña (RF-35)");
        System.out.println(" 3. Listar    Muestra todos los resultados y el promedio del ICA (RF-38)");
        System.out.println(" 4. Corregir  Cambia valor_ica y categoria_ica (RF-36)");
        System.out.println(" 5. Eliminar  Quita el resultado del listado (RF-37)");
        System.out.println(" 6. Deshacer  Devuelve el último resultado eliminado (RF-39)");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(" Consultas de calidad (HU-34) — apoyo a la comparación del diagnóstico");
        System.out.println(" 7. Comparar puntos (alta/media/baja)     (RF-64)");
        System.out.println(" 8. Comparar temporadas (seca/lluvias)    (RF-63 · RF-65)");
        System.out.println(" 9. Evolución histórica");
        System.out.println("10. Cargar datos de demostración (La Iguaná — datos de PRUEBA)");
        System.out.println(" 0. Volver    Regresa al menú principal (Figura 9.1)");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(" OTROS MENSAJES que aparecen en la línea MENSAJE:");
        System.out.println("  RF-30/36 Categoría no válida. Use: muy mala, mala, regular, aceptable, buena");
        System.out.println("  RF-31/32/33 Esa campaña / quebrada / punto no existe");
        System.out.println("  RF-34 El valor del ICA solo admite números");
        System.out.println("  RF-35/36/37 No hay coincidencias / Ese resultado no existe");
        System.out.println("  RF-38 No hay resultados registrados");
        System.out.println("  RF-39 No hay nada que deshacer");
        System.out.println("-------------------------------------------------------------------------------");
    }

    // RF-38 · CA-21.1 / CA-21.2 — listado con nombres del diccionario de datos
    private void mostrarListadoResumen() {
        System.out.println(" LISTADO");
        System.out.println("-------------------------------------------------------------------------------");
        ResultadoCalidad[] lista = gestor.obtenerCopiaCompacta();
        if (lista.length == 0) {
            System.out.println(" (vacío)");
            return;
        }
        System.out.println(String.format("%-14s %-12s %-12s %8s %-10s %-8s",
                "nombre_quebrada", "fecha_campana", "nombre_punto",
                "valor_ica", "categoria_ica", "fuente"));
        System.out.println("-------------- ------------ ------------ -------- ------------ --------");
        int max = lista.length;
        if (max > 8) {
            max = 8; // vista resumida; el Listar completo está en opción 3
        }
        for (int i = 0; i < max; i++) {
            ResultadoCalidad r = lista[i];
            System.out.println(String.format("%-14s %-12s %-12s %8s %-10s %-8s",
                    recortar(r.getNombreQuebrada(), 14),
                    r.getFechaCampana(),
                    r.getNombrePunto(),
                    formatearIca(r.getValorIca()),
                    r.getCategoriaIca(),
                    r.getFuente()));
        }
        if (lista.length > 8) {
            System.out.println(" ... (" + (lista.length - 8)
                    + " más; use opción 3 Listar para ver todos)");
        }
        System.out.println(" Promedio del ICA: " + formatearIca(gestor.calcularPromedioIca(lista))
                + "  (RF-38 · CA-21.1)   Total: " + lista.length);
    }

    // RF-28 a RF-34 · CA-17.1 a CA-17.8 — Guardar
    private void opcionGuardar() {
        System.out.println();
        System.out.println("--- Guardar resultado (RF-28 a RF-34) ---");
        String nombreQuebrada = leerCampo("nombre_quebrada: ");
        String fechaCampana = leerCampo("fecha_campana (dd/MM/yyyy): ");
        String temporada = leerCampo("temporada (seca/lluvias) [de la campaña]: ");
        String nombrePunto = leerCampo("nombre_punto (parte alta/media/baja): ");
        String valorIca = leerCampo("valor_ica: ");
        String categoriaIca = leerCampo("categoria_ica: ");
        System.out.println("fuente: RedRío (se llena sola)");

        mensaje = gestor.registrar(nombreQuebrada, fechaCampana, temporada,
                nombrePunto, valorIca, categoriaIca);
    }

    // RF-35 · CA-18.1 a CA-18.4 — Buscar
    private void opcionBuscar() {
        System.out.println();
        System.out.println("--- Buscar (RF-35) ---");
        System.out.println(" a) Por texto en categoria_ica");
        System.out.println(" b) Por nombre_quebrada + fecha_campana + nombre_punto");
        String modo = leerCampo("Elija a/b: ");

        if (modo.equalsIgnoreCase("b")) {
            String nombreQuebrada = leerCampo("nombre_quebrada: ");
            String fechaCampana = leerCampo("fecha_campana: ");
            String nombrePunto = leerCampo("nombre_punto: ");
            ResultadoCalidad r = gestor.consultar(nombreQuebrada, fechaCampana, nombrePunto);
            if (r == null) {
                // RF-35 · CA-18.3
                mensaje = "(!) Ese punto no tiene resultado en esa campaña. (RF-35 · CA-18.3)";
            } else {
                System.out.println();
                imprimirUno(r);
                mensaje = "Consulta OK. (RF-35 · CA-18.1)";
            }
        } else {
            String texto = leerCampo("texto en categoria_ica: ");
            ResultadoCalidad[] hallados = gestor.buscarPorCategoria(texto);
            if (hallados.length == 0) {
                // RF-35 · CA-18.4
                mensaje = "(!) No hay coincidencias. (RF-35 · CA-18.4)";
            } else {
                System.out.println();
                for (int i = 0; i < hallados.length; i++) {
                    imprimirUno(hallados[i]);
                }
                mensaje = "Se encontraron " + hallados.length + " coincidencia(s). (RF-35 · CA-18.2)";
            }
        }
        pausa();
    }

    // RF-38 · CA-21.1 · CA-21.2 · CA-21.3 — Listar
    private void opcionListar() {
        System.out.println();
        System.out.println("--- Listar (RF-38) ---");
        String filtro = leerCampo("nombre_quebrada (ENTER = todas): ");
        ResultadoCalidad[] lista;
        if (filtro.isEmpty()) {
            lista = gestor.obtenerCopiaCompacta();
        } else {
            lista = gestor.filtrarPorQuebrada(filtro);
        }
        if (lista.length == 0) {
            if (filtro.isEmpty()) {
                mensaje = "(!) No hay resultados registrados. (RF-38 · CA-21.2)";
            } else {
                mensaje = "(!) Esa quebrada no tiene resultados. (RF-38 · CA-21.3)";
            }
        } else {
            System.out.println(String.format("%-14s %-12s %-12s %8s %-10s %-8s",
                    "nombre_quebrada", "fecha_campana", "nombre_punto",
                    "valor_ica", "categoria_ica", "fuente"));
            for (int i = 0; i < lista.length; i++) {
                ResultadoCalidad r = lista[i];
                System.out.println(String.format("%-14s %-12s %-12s %8s %-10s %-8s",
                        r.getNombreQuebrada(), r.getFechaCampana(), r.getNombrePunto(),
                        formatearIca(r.getValorIca()), r.getCategoriaIca(), r.getFuente()));
            }
            System.out.println("Promedio del ICA: " + formatearIca(gestor.calcularPromedioIca(lista))
                    + " (RF-38 · CA-21.1)");
            mensaje = "Listado: " + lista.length + " resultado(s). (RF-38 · CA-21.1)";
        }
        pausa();
    }

    // RF-36 · CA-19.1 a CA-19.4 — Corregir
    private void opcionCorregir() {
        System.out.println();
        System.out.println("--- Corregir (RF-36) ---");
        System.out.println("No se ofrecen nombre_punto ni fecha_campana para modificar (CA-19.3).");
        String nombreQuebrada = leerCampo("nombre_quebrada: ");
        String fechaCampana = leerCampo("fecha_campana: ");
        String nombrePunto = leerCampo("nombre_punto: ");
        String valorIca = leerCampo("nuevo valor_ica: ");
        String categoriaIca = leerCampo("nueva categoria_ica: ");
        mensaje = gestor.actualizar(nombreQuebrada, fechaCampana, nombrePunto, valorIca, categoriaIca);
    }

    // RF-37 · CA-20.1 a CA-20.3 — Eliminar
    private void opcionEliminar() {
        System.out.println();
        System.out.println("--- Eliminar (RF-37) ---");
        String nombreQuebrada = leerCampo("nombre_quebrada: ");
        String fechaCampana = leerCampo("fecha_campana: ");
        String nombrePunto = leerCampo("nombre_punto: ");
        mensaje = gestor.eliminar(nombreQuebrada, fechaCampana, nombrePunto);
    }

    // RF-39 · CA-22.1 · CA-22.2 · CA-22.3 — Deshacer
    private void opcionDeshacer() {
        mensaje = gestor.deshacerEliminacion();
    }

    // RF-64 · CA-34.2 — comparar puntos
    private void opcionCompararPuntos() {
        System.out.println();
        String nombreQuebrada = leerCampo("nombre_quebrada: ");
        String fechaCampana = leerCampo("fecha_campana: ");
        System.out.println(comparador.compararEntrePuntos(nombreQuebrada, fechaCampana));
        mensaje = "Comparación entre puntos mostrada. (RF-64)";
        pausa();
    }

    // RF-63 · RF-65 · CA-34.1 · CA-34.3 — comparar temporadas
    private void opcionCompararTemporadas() {
        System.out.println();
        String nombreQuebrada = leerCampo("nombre_quebrada: ");
        String nombrePunto = leerCampo("nombre_punto: ");
        String anio = leerCampo("año (ej. 2024) o ENTER: ");
        System.out.println(comparador.compararEntreTemporadas(nombreQuebrada, nombrePunto, anio));
        mensaje = "Comparación entre temporadas mostrada. (RF-63)";
        pausa();
    }

    private void opcionEvolucion() {
        System.out.println();
        String nombreQuebrada = leerCampo("nombre_quebrada: ");
        String nombrePunto = leerCampo("nombre_punto (ENTER = todos): ");
        System.out.println(comparador.evolucionHistorica(nombreQuebrada, nombrePunto));
        mensaje = "Evolución histórica mostrada.";
        pausa();
    }

    // Datos ilustrativos del diagnóstico (NO oficiales)
    private void opcionCargarDemostracion() {
        System.out.println();
        System.out.println("ADVERTENCIA: datos de PRUEBA / ILUSTRATIVOS (no cifras oficiales RedRío).");
        String conf = leerCampo("¿Cargar demostración La Iguaná? (S/N): ");
        if (!conf.equalsIgnoreCase("S") && !conf.equalsIgnoreCase("SI")
                && !conf.equalsIgnoreCase("SÍ")) {
            mensaje = "Carga de demostración cancelada.";
            return;
        }
        String[][] filas = {
                {"La Iguaná", "15/02/2023", "seca", "parte alta", "0.72", "aceptable"},
                {"La Iguaná", "15/02/2023", "seca", "parte media", "0.55", "regular"},
                {"La Iguaná", "15/02/2023", "seca", "parte baja", "0.38", "mala"},
                {"La Iguaná", "15/10/2023", "lluvias", "parte alta", "0.70", "aceptable"},
                {"La Iguaná", "15/10/2023", "lluvias", "parte media", "0.52", "regular"},
                {"La Iguaná", "15/10/2023", "lluvias", "parte baja", "0.35", "mala"},
                {"La Iguaná", "15/02/2024", "seca", "parte alta", "0.71", "aceptable"},
                {"La Iguaná", "15/02/2024", "seca", "parte media", "0.54", "regular"},
                {"La Iguaná", "15/02/2024", "seca", "parte baja", "0.40", "mala"},
                {"La Iguaná", "15/10/2024", "lluvias", "parte alta", "0.69", "aceptable"},
                {"La Iguaná", "15/10/2024", "lluvias", "parte media", "0.50", "regular"},
                {"La Iguaná", "15/10/2024", "lluvias", "parte baja", "0.22", "muy mala"},
                {"La Iguaná", "15/02/2025", "seca", "parte alta", "0.73", "aceptable"},
                {"La Iguaná", "15/02/2025", "seca", "parte media", "0.56", "regular"},
                {"La Iguaná", "15/02/2025", "seca", "parte baja", "0.36", "mala"},
                {"La Iguaná", "15/10/2025", "lluvias", "parte alta", "0.70", "aceptable"},
                {"La Iguaná", "15/10/2025", "lluvias", "parte media", "0.51", "regular"},
                {"La Iguaná", "15/10/2025", "lluvias", "parte baja", "0.20", "muy mala"}
        };
        int ok = 0;
        for (int i = 0; i < filas.length; i++) {
            String m = gestor.registrar(filas[i][0], filas[i][1], filas[i][2],
                    filas[i][3], filas[i][4], filas[i][5]);
            if (m.startsWith("OK") || m.contains("registrado")) {
                ok++;
            }
        }
        mensaje = "Demostración: " + ok + " registro(s) nuevos (datos de PRUEBA).";
    }

    private void imprimirUno(ResultadoCalidad r) {
        System.out.println(String.format("%-14s | %-12s | %-12s | %s | %-10s | %s",
                r.getNombreQuebrada(), r.getFechaCampana(), r.getNombrePunto(),
                formatearIca(r.getValorIca()), r.getCategoriaIca(), r.getFuente()));
    }

    private String leerCampo(String etiqueta) {
        System.out.print(etiqueta);
        return sc.nextLine().trim();
    }

    private void pausa() {
        System.out.print("ENTER para continuar...");
        sc.nextLine();
    }

    private String formatearIca(double valor) {
        // Mockup usa coma decimal (0,60)
        return String.format(Locale.forLanguageTag("es-CO"), "%.2f", valor);
    }

    private String formatearMensajeCaja(String texto) {
        if (texto == null) {
            return "";
        }
        if (texto.length() > 70) {
            return texto.substring(0, 67) + "...";
        }
        return texto;
    }

    private String recortar(String texto, int max) {
        if (texto == null) {
            return "";
        }
        if (texto.length() <= max) {
            return texto;
        }
        return texto.substring(0, max);
    }
}
