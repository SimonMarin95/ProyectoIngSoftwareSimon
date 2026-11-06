/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Resultado de calidad (validaciones)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-17 · HU-19 · HU-34 / RF-30 · RF-34 · RF-36 / RN-02 · RN-03
 * Criterios: CA-17.4 CA-17.8 CA-19.2 (falla) · orden de categorías no alfabético
 * Estructuras: Arreglos de constantes (sin colecciones prohibidas)
 * =========================================================== */

package resultado;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Validaciones del diccionario de datos y orden de calidad del ICA.
 */
public class ValidadorResultado {

    public static final String[] PUNTOS_VALIDOS = {
            "parte alta", "parte media", "parte baja"
    };

    // RN-02 — cinco categorías oficiales
    public static final String[] CATEGORIAS_VALIDAS = {
            "muy mala", "mala", "regular", "aceptable", "buena"
    };

    // RN-03
    public static final String[] TEMPORADAS_VALIDAS = {
            "seca", "lluvias"
    };

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private ValidadorResultado() {
    }

    // RF-33 — nombre_punto válido
    public static boolean esPuntoValido(String punto) {
        return estaEnLista(punto, PUNTOS_VALIDOS);
    }

    // RF-30 · CA-17.4 — categoria_ica válida
    public static boolean esCategoriaValida(String categoria) {
        return estaEnLista(categoria, CATEGORIAS_VALIDAS);
    }

    // RN-03 — temporada válida
    public static boolean esTemporadaValida(String temporada) {
        return estaEnLista(temporada, TEMPORADAS_VALIDAS);
    }

    // HU-34 — orden de calidad (1=peor … 5=mejor); NO alfabético
    public static int ordenCategoria(String categoria) {
        if (categoria == null) {
            return 0;
        }
        String c = categoria.trim().toLowerCase();
        if (c.equals("muy mala")) {
            return 1;
        }
        if (c.equals("mala")) {
            return 2;
        }
        if (c.equals("regular")) {
            return 3;
        }
        if (c.equals("aceptable")) {
            return 4;
        }
        if (c.equals("buena")) {
            return 5;
        }
        return 0;
    }

    // RF-64 — orden de tramos: alta → media → baja
    public static int ordenPunto(String punto) {
        if (punto == null) {
            return 0;
        }
        String p = punto.trim().toLowerCase();
        if (p.equals("parte alta")) {
            return 1;
        }
        if (p.equals("parte media")) {
            return 2;
        }
        if (p.equals("parte baja")) {
            return 3;
        }
        return 0;
    }

    public static boolean esFechaValida(String fechaTexto) {
        LocalDate fecha = parsearFecha(fechaTexto);
        if (fecha == null) {
            return false;
        }
        return !fecha.isAfter(LocalDate.now());
    }

    public static LocalDate parsearFecha(String fechaTexto) {
        if (fechaTexto == null || fechaTexto.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(fechaTexto.trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // RF-34 · CA-17.8 — valor_ica numérico (acepta coma o punto)
    public static Double parsearIca(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        String limpio = texto.trim().replace(',', '.');
        try {
            return Double.parseDouble(limpio);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String textoCategoriasValidas() {
        return "muy mala, mala, regular, aceptable y buena";
    }

    public static String textoPuntosValidos() {
        return "parte alta, parte media y parte baja";
    }

    private static boolean estaEnLista(String valor, String[] lista) {
        if (valor == null) {
            return false;
        }
        String v = valor.trim().toLowerCase();
        for (int i = 0; i < lista.length; i++) {
            if (lista[i].equals(v)) {
                return true;
            }
        }
        return false;
    }
}
