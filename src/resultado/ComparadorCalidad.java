/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Resultado de calidad (comparaciones)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-34 / RF-63 · RF-64 · RF-65 / RN-02
 * Criterios: CA-34.1 CA-34.2 (bueno) CA-34.3 CA-34.4 (falla)
 * Estructuras: Arreglos tradicionales · orden de categorías no alfabético
 * =========================================================== */

package resultado;

import java.time.LocalDate;

/**
 * Comparaciones de calidad: entre puntos, entre temporadas y evolución histórica.
 */
public class ComparadorCalidad {

    private GestorResultados gestor;

    public ComparadorCalidad(GestorResultados gestor) {
        this.gestor = gestor;
    }

    // RF-64 · CA-34.2 — compara parte alta, media y baja en una misma campaña
    public String compararEntrePuntos(String nombreQuebrada, String fechaCampana) {
        StringBuilder sb = new StringBuilder();
        sb.append("===============================================================================\n");
        sb.append(" COMPARACIÓN ENTRE PUNTOS (misma campaña) (RF-64 · CA-34.2)\n");
        sb.append(" nombre_quebrada: ").append(nombreQuebrada).append("\n");
        sb.append(" fecha_campana: ").append(fechaCampana).append("\n");
        sb.append("===============================================================================\n");

        ResultadoCalidad alta = gestor.consultar(nombreQuebrada, fechaCampana, "parte alta");
        ResultadoCalidad media = gestor.consultar(nombreQuebrada, fechaCampana, "parte media");
        ResultadoCalidad baja = gestor.consultar(nombreQuebrada, fechaCampana, "parte baja");

        boolean faltanDatos = false;
        sb.append(String.format("%-14s | %-12s | %8s | %-8s%n",
                "nombre_punto", "categoria_ica", "valor_ica", "fuente"));
        sb.append("---------------+--------------+----------+---------\n");

        faltanDatos = agregarFilaPunto(sb, "parte alta", alta) || faltanDatos;
        faltanDatos = agregarFilaPunto(sb, "parte media", media) || faltanDatos;
        faltanDatos = agregarFilaPunto(sb, "parte baja", baja) || faltanDatos;

        sb.append("===============================================================================\n");
        if (faltanDatos) {
            sb.append(" (!) Faltan datos para una comparación completa.\n");
        } else {
            sb.append(interpretarDeterioroTramos(alta, media, baja));
        }
        return sb.toString();
    }

    // RF-63 · CA-34.1 — compara seca vs lluvias de un mismo punto
    // RF-65 · CA-34.3 — avisa si falta una temporada
    // RF-66 · CA-34.4 — avisa si el punto no es válido
    public String compararEntreTemporadas(String nombreQuebrada, String nombrePunto, String anio) {
        StringBuilder sb = new StringBuilder();
        sb.append("===============================================================================\n");
        sb.append(" COMPARACIÓN ENTRE TEMPORADAS (RF-63 · CA-34.1)\n");
        sb.append(" nombre_quebrada: ").append(nombreQuebrada).append("\n");
        sb.append(" nombre_punto: ").append(nombrePunto).append("\n");
        sb.append("===============================================================================\n");

        if (!ValidadorResultado.esPuntoValido(nombrePunto)) {
            return "(!) Ese punto no existe. (RF-66 · CA-34.4)\n";
        }
        nombrePunto = nombrePunto.trim().toLowerCase();

        Integer anioFiltro = null;
        if (anio != null && !anio.trim().isEmpty()) {
            try {
                anioFiltro = Integer.parseInt(anio.trim());
            } catch (NumberFormatException e) {
                return "(!) El año debe ser un número (ejemplo: 2024).\n";
            }
        }

        ResultadoCalidad[] deQuebrada = gestor.filtrarPorQuebrada(nombreQuebrada);
        ResultadoCalidad seca = null;
        ResultadoCalidad lluvias = null;

        for (int i = 0; i < deQuebrada.length; i++) {
            ResultadoCalidad r = deQuebrada[i];
            if (!r.getNombrePunto().equalsIgnoreCase(nombrePunto)) {
                continue;
            }
            if (anioFiltro != null) {
                LocalDate f = ValidadorResultado.parsearFecha(r.getFechaCampana());
                if (f == null || f.getYear() != anioFiltro.intValue()) {
                    continue;
                }
            }
            if (r.getTemporada().equalsIgnoreCase("seca")) {
                if (seca == null || fechaEsPosterior(r.getFechaCampana(), seca.getFechaCampana())) {
                    seca = r;
                }
            } else if (r.getTemporada().equalsIgnoreCase("lluvias")) {
                if (lluvias == null || fechaEsPosterior(r.getFechaCampana(), lluvias.getFechaCampana())) {
                    lluvias = r;
                }
            }
        }

        sb.append(String.format("%-10s | %-12s | %-12s | %8s%n",
                "temporada", "fecha_campana", "categoria_ica", "valor_ica"));
        sb.append("-----------+--------------+--------------+---------\n");

        if (seca == null) {
            sb.append(String.format("%-10s | %-12s | %-12s | %8s%n", "seca", "(sin dato)", "—", "—"));
        } else {
            sb.append(String.format("%-10s | %-12s | %-12s | %8.2f%n",
                    "seca", seca.getFechaCampana(), seca.getCategoriaIca(), seca.getValorIca()));
        }
        if (lluvias == null) {
            sb.append(String.format("%-10s | %-12s | %-12s | %8s%n", "lluvias", "(sin dato)", "—", "—"));
        } else {
            sb.append(String.format("%-10s | %-12s | %-12s | %8.2f%n",
                    "lluvias", lluvias.getFechaCampana(), lluvias.getCategoriaIca(), lluvias.getValorIca()));
        }

        sb.append("===============================================================================\n");
        if (seca == null || lluvias == null) {
            // RF-65 · CA-34.3
            sb.append(" (!) Faltan datos para comparar temporadas. (RF-65 · CA-34.3)\n");
            if (seca == null) {
                sb.append(" Falta campaña de temporada seca.\n");
            }
            if (lluvias == null) {
                sb.append(" Falta campaña de temporada de lluvias.\n");
            }
        } else {
            int ordenSeca = ValidadorResultado.ordenCategoria(seca.getCategoriaIca());
            int ordenLluvia = ValidadorResultado.ordenCategoria(lluvias.getCategoriaIca());
            if (ordenSeca == ordenLluvia) {
                sb.append(" Interpretación: la categoría se mantiene igual entre temporadas.\n");
            } else if (ordenLluvia < ordenSeca) {
                sb.append(" Interpretación: en lluvias la calidad es PEOR que en temporada seca.\n");
            } else {
                sb.append(" Interpretación: en lluvias la calidad es MEJOR que en temporada seca.\n");
            }
            sb.append(" (Orden: muy mala < mala < regular < aceptable < buena)\n");
        }
        return sb.toString();
    }

    // Evolución histórica ordenada por fecha_campana
    public String evolucionHistorica(String nombreQuebrada, String nombrePunto) {
        StringBuilder sb = new StringBuilder();
        sb.append("===============================================================================\n");
        sb.append(" EVOLUCIÓN HISTÓRICA\n");
        sb.append(" nombre_quebrada: ").append(nombreQuebrada).append("\n");
        if (nombrePunto != null && !nombrePunto.trim().isEmpty()) {
            sb.append(" nombre_punto: ").append(nombrePunto.trim().toLowerCase()).append("\n");
        } else {
            sb.append(" nombre_punto: (todos)\n");
        }
        sb.append("===============================================================================\n");

        ResultadoCalidad[] base = gestor.filtrarPorQuebrada(nombreQuebrada);
        if (base.length == 0) {
            sb.append(" (!) Esa quebrada no tiene resultados. (RF-38 · CA-21.3)\n");
            return sb.toString();
        }

        ResultadoCalidad[] filtrados;
        if (nombrePunto != null && !nombrePunto.trim().isEmpty()) {
            if (!ValidadorResultado.esPuntoValido(nombrePunto)) {
                return "(!) Ese punto no existe. Use: parte alta, parte media, parte baja.\n";
            }
            String p = nombrePunto.trim().toLowerCase();
            int n = 0;
            for (int i = 0; i < base.length; i++) {
                if (base[i].getNombrePunto().equals(p)) {
                    n++;
                }
            }
            filtrados = new ResultadoCalidad[n];
            int pos = 0;
            for (int i = 0; i < base.length; i++) {
                if (base[i].getNombrePunto().equals(p)) {
                    filtrados[pos] = base[i];
                    pos++;
                }
            }
        } else {
            filtrados = base;
        }

        if (filtrados.length == 0) {
            sb.append(" (!) No hay resultados para ese punto.\n");
            return sb.toString();
        }

        ordenarPorFechaAscendente(filtrados);

        sb.append(String.format("%-12s | %-8s | %-12s | %-12s | %6s | %-8s%n",
                "fecha_campana", "temporada", "nombre_punto", "categoria_ica", "ICA", "cambio"));
        sb.append("-------------+----------+--------------+--------------+--------+---------\n");

        ResultadoCalidad anteriorMismoPunto = null;
        for (int i = 0; i < filtrados.length; i++) {
            ResultadoCalidad actual = filtrados[i];
            String cambio = "—";
            if (anteriorMismoPunto != null
                    && anteriorMismoPunto.getNombrePunto().equals(actual.getNombrePunto())) {
                int oAnt = ValidadorResultado.ordenCategoria(anteriorMismoPunto.getCategoriaIca());
                int oAct = ValidadorResultado.ordenCategoria(actual.getCategoriaIca());
                if (oAct > oAnt) {
                    cambio = "MEJORA";
                } else if (oAct < oAnt) {
                    cambio = "EMPEORA";
                } else {
                    cambio = "IGUAL";
                }
            }
            sb.append(String.format("%-12s | %-8s | %-12s | %-12s | %6.2f | %-8s%n",
                    actual.getFechaCampana(), actual.getTemporada(), actual.getNombrePunto(),
                    actual.getCategoriaIca(), actual.getValorIca(), cambio));
            anteriorMismoPunto = actual;
        }
        sb.append(" Nota: MEJORA/EMPEORA usa orden de categorias ICA (no alfabético).\n");
        return sb.toString();
    }

    private boolean agregarFilaPunto(StringBuilder sb, String etiqueta, ResultadoCalidad r) {
        if (r == null) {
            sb.append(String.format("%-14s | %-12s | %8s | %-8s%n", etiqueta, "(faltante)", "—", "—"));
            return true;
        }
        sb.append(String.format("%-14s | %-12s | %8.2f | %-8s%n",
                etiqueta, r.getCategoriaIca(), r.getValorIca(), r.getFuente()));
        return false;
    }

    private String interpretarDeterioroTramos(ResultadoCalidad alta, ResultadoCalidad media,
                                             ResultadoCalidad baja) {
        int oA = ValidadorResultado.ordenCategoria(alta.getCategoriaIca());
        int oM = ValidadorResultado.ordenCategoria(media.getCategoriaIca());
        int oB = ValidadorResultado.ordenCategoria(baja.getCategoriaIca());
        StringBuilder t = new StringBuilder();
        t.append(" parte alta : ").append(alta.getCategoriaIca()).append("\n");
        t.append(" parte media: ").append(media.getCategoriaIca()).append("\n");
        t.append(" parte baja : ").append(baja.getCategoriaIca()).append("\n");
        if (oA >= oM && oM >= oB && (oA > oB)) {
            t.append(" Conclusión: deterioro desde la parte alta hacia la baja.\n");
        } else if (oA == oM && oM == oB) {
            t.append(" Conclusión: las tres partes tienen la misma categoría.\n");
        } else {
            t.append(" Conclusión: el patrón no es un deterioro uniforme aguas abajo.\n");
        }
        return t.toString();
    }

    private void ordenarPorFechaAscendente(ResultadoCalidad[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                LocalDate f1 = ValidadorResultado.parsearFecha(arr[j].getFechaCampana());
                LocalDate f2 = ValidadorResultado.parsearFecha(arr[j + 1].getFechaCampana());
                int cmp;
                if (f1 == null && f2 == null) {
                    cmp = 0;
                } else if (f1 == null) {
                    cmp = 1;
                } else if (f2 == null) {
                    cmp = -1;
                } else {
                    cmp = f1.compareTo(f2);
                }
                if (cmp == 0) {
                    cmp = ValidadorResultado.ordenPunto(arr[j].getNombrePunto())
                            - ValidadorResultado.ordenPunto(arr[j + 1].getNombrePunto());
                }
                if (cmp > 0) {
                    ResultadoCalidad tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
    }

    private boolean fechaEsPosterior(String fechaNueva, String fechaActual) {
        LocalDate n = ValidadorResultado.parsearFecha(fechaNueva);
        LocalDate a = ValidadorResultado.parsearFecha(fechaActual);
        if (n == null || a == null) {
            return false;
        }
        return n.isAfter(a);
    }
}
