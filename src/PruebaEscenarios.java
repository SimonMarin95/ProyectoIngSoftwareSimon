/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Apoyo de verificación (no es entrega al docente)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-17 a HU-22 / RF-28 a RF-39
 * Criterios: CA equivalentes a Fig. 10.7–10.12
 * Estructuras: Usa el gestor (arreglo + pila + archivo)
 * =========================================================== */

import java.io.File;

import integracion.AdaptadorQuebradaTemporal;
import resultado.GestorResultados;
import resultado.ResultadoCalidad;

/**
 * Ejecuta automáticamente los 6 escenarios de la sección 10.4.
 * Uso: java -cp bin PruebaEscenarios
 */
public class PruebaEscenarios {

    public static void main(String[] args) {
        String ruta = "datos" + File.separator + "resultados_prueba_auto.txt";
        File f = new File(ruta);
        if (f.exists()) {
            f.delete();
        }

        AdaptadorQuebradaTemporal adaptador = new AdaptadorQuebradaTemporal();
        GestorResultados g = new GestorResultados(50, ruta, adaptador);
        g.cargarDesdeArchivo();

        int ok = 0;
        int total = 6;

        // Fig 10.7 — registrar 3 y listar
        String m1 = g.registrar("La Iguaná", "15/02/2024", "seca", "parte alta", "0.80", "aceptable");
        String m2 = g.registrar("La Iguaná", "15/02/2024", "seca", "parte media", "0.60", "regular");
        String m3 = g.registrar("La Iguaná", "15/02/2024", "seca", "parte baja", "0.40", "mala");
        boolean e1 = m1.startsWith("OK") && m2.startsWith("OK") && m3.startsWith("OK")
                && g.getCantidad() == 3
                && Math.abs(g.calcularPromedioIca() - 0.60) < 0.001;
        System.out.println((e1 ? "[OK] " : "[FALLÓ] ") + "Fig 10.7 Registrar tres y listar (promedio 0.60)");
        if (e1) {
            ok++;
        }

        // Fig 10.8 — duplicado
        String dup = g.registrar("La Iguaná", "15/02/2024", "seca", "parte media", "0.60", "regular");
        boolean e2 = dup.contains("RF-29") && dup.contains("ya tiene resultado") && g.getCantidad() == 3;
        System.out.println((e2 ? "[OK] " : "[FALLÓ] ") + "Fig 10.8 Rechazo de duplicado");
        if (e2) {
            ok++;
        }

        // Fig 10.9 — eliminar medio sin huecos
        String el = g.eliminar("La Iguaná", "15/02/2024", "parte media");
        ResultadoCalidad[] lista = g.obtenerCopiaCompacta();
        boolean sinHuecos = lista.length == 2
                && lista[0] != null && lista[1] != null
                && lista[0].getNombrePunto().equals("parte alta")
                && lista[1].getNombrePunto().equals("parte baja");
        boolean e3 = el.startsWith("OK") && sinHuecos;
        System.out.println((e3 ? "[OK] " : "[FALLÓ] ") + "Fig 10.9 Eliminar del medio sin huecos");
        if (e3) {
            ok++;
        }

        // Fig 10.10 — deshacer
        String undo = g.deshacerEliminacion();
        boolean e4 = undo.startsWith("OK") && g.getCantidad() == 3
                && g.consultar("La Iguaná", "15/02/2024", "parte media") != null;
        System.out.println((e4 ? "[OK] " : "[FALLÓ] ") + "Fig 10.10 Deshacer eliminación");
        if (e4) {
            ok++;
        }

        // Fig 10.11 — deshacer vacío
        String vacio = g.deshacerEliminacion();
        boolean e5 = vacio.toLowerCase().contains("no hay nada que deshacer") && g.getCantidad() == 3;
        System.out.println((e5 ? "[OK] " : "[FALLÓ] ") + "Fig 10.11 Deshacer sin pendientes");
        if (e5) {
            ok++;
        }

        // Fig 10.12 — persistencia
        g.guardarEnArchivo();
        GestorResultados g2 = new GestorResultados(50, ruta, adaptador);
        int cargados = g2.cargarDesdeArchivo();
        boolean e6 = cargados == 3 && g2.getCantidad() == 3
                && g2.consultar("La Iguaná", "15/02/2024", "parte baja") != null;
        System.out.println((e6 ? "[OK] " : "[FALLÓ] ") + "Fig 10.12 Persistencia al reiniciar (" + cargados + " cargados)");
        if (e6) {
            ok++;
        }

        // Extra: quebrada inexistente
        String q = g.registrar("Quebrada Seca", "15/02/2024", "seca", "parte alta", "0.5", "regular");
        System.out.println((q.contains("RF-32") ? "[OK] " : "[FALLÓ] ") + "Extra RF-32 quebrada inexistente");

        // Extra: comparación categorías orden
        System.out.println("Promedio final: " + g.calcularPromedioIca());
        System.out.println("Resultado: " + ok + "/" + total + " escenarios principales");
        if (ok != total) {
            System.exit(1);
        }
    }
}
