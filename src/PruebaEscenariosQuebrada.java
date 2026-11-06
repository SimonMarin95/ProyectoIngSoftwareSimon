/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Apoyo de verificación (no es entrega al docente)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-01 a HU-06 / RF-01 a RF-09 / RNF-03
 * Criterios: escenarios 10.1 a 10.6 de Quebradas
 * Estructuras: Usa el gestor real (arreglo + pila + archivo)
 * =========================================================== */

import java.io.File;
import java.io.IOException;

import quebrada.GestorQuebradas;
import quebrada.Quebrada;

/**
 * Ejecuta los seis escenarios de Quebradas sin tocar los datos del proyecto.
 */
public class PruebaEscenariosQuebrada {

    public static void main(String[] args) throws IOException {
        File archivo = crearArchivoTemporal();
        int ok = 0;
        int total = 6;

        try {
            GestorQuebradas gestor = new GestorQuebradas(50, archivo.getAbsolutePath());
            gestor.cargarDesdeArchivo();

            // Fig. 10.1 — crear tres quebradas y listarlas.
            String m1 = gestor.registrar("La Iguaná", "3");
            String m2 = gestor.registrar("La Presidenta", "2");
            String m3 = gestor.registrar("Santa Elena", "3");
            boolean e1 = m1.startsWith("OK") && m2.startsWith("OK") && m3.startsWith("OK")
                    && gestor.getCantidad() == 3 && gestor.calcularTotalPuntos() == 8;
            System.out.println((e1 ? "[OK] " : "[FALLÓ] ")
                    + "Fig. 10.1 Crear tres quebradas y listarlas");
            if (e1) {
                ok++;
            }

            // Fig. 10.2 — identificador repetido.
            String duplicada = gestor.registrar("la iguaná", "2");
            boolean e2 = duplicada.contains("RF-02") && gestor.getCantidad() == 3;
            System.out.println((e2 ? "[OK] " : "[FALLÓ] ")
                    + "Fig. 10.2 Rechazo de nombre repetido");
            if (e2) {
                ok++;
            }

            // Fig. 10.3 — eliminar el registro del medio sin dejar huecos.
            String eliminada = gestor.eliminar("La Presidenta");
            Quebrada[] lista = gestor.obtenerCopiaCompacta();
            boolean compacta = lista.length == 2
                    && lista[0].getNombreQuebrada().equals("La Iguaná")
                    && lista[1].getNombreQuebrada().equals("Santa Elena");
            boolean e3 = eliminada.startsWith("OK") && compacta;
            System.out.println((e3 ? "[OK] " : "[FALLÓ] ")
                    + "Fig. 10.3 Eliminar del medio sin huecos");
            if (e3) {
                ok++;
            }

            // Fig. 10.4 — recuperar la última quebrada eliminada.
            String deshacer = gestor.deshacerEliminacion();
            boolean e4 = deshacer.startsWith("OK") && gestor.getCantidad() == 3
                    && gestor.consultar("La Presidenta") != null;
            System.out.println((e4 ? "[OK] " : "[FALLÓ] ")
                    + "Fig. 10.4 Deshacer eliminación");
            if (e4) {
                ok++;
            }

            // Fig. 10.5 — informar que la pila no tiene eliminaciones pendientes.
            String vacio = gestor.deshacerEliminacion();
            boolean e5 = vacio.contains("No hay nada que deshacer")
                    && gestor.getCantidad() == 3;
            System.out.println((e5 ? "[OK] " : "[FALLÓ] ")
                    + "Fig. 10.5 Deshacer sin pendientes");
            if (e5) {
                ok++;
            }

            // Fig. 10.6 — cerrar y volver a cargar los mismos registros.
            gestor.guardarEnArchivo();
            GestorQuebradas reiniciado = new GestorQuebradas(50, archivo.getAbsolutePath());
            int cargadas = reiniciado.cargarDesdeArchivo();
            boolean e6 = cargadas == 3 && reiniciado.getCantidad() == 3
                    && reiniciado.consultar("La Presidenta") != null;
            System.out.println((e6 ? "[OK] " : "[FALLÓ] ")
                    + "Fig. 10.6 Persistencia al reiniciar (" + cargadas + " cargadas)");
            if (e6) {
                ok++;
            }

            boolean busqueda = reiniciado.buscarPorTexto("iguan").length == 1;
            String actualizada = reiniciado.actualizar("Santa Elena", "2");
            boolean edicion = actualizada.startsWith("OK")
                    && reiniciado.consultar("Santa Elena").getNumeroPuntos() == 2;
            boolean integracion = reiniciado.existeQuebrada("la iguaná");
            System.out.println((busqueda && edicion && integracion ? "[OK] " : "[FALLÓ] ")
                    + "Extra búsqueda parcial, actualización e integración");
            System.out.println("Resultado: " + ok + "/" + total + " escenarios principales");
        } finally {
            eliminarArchivoTemporal(archivo);
        }

        if (ok != total) {
            System.exit(1);
        }
    }

    private static File crearArchivoTemporal() throws IOException {
        File archivo = File.createTempFile("quebradas-prueba-", ".txt");
        eliminarArchivoTemporal(archivo);
        return archivo;
    }

    private static void eliminarArchivoTemporal(File archivo) throws IOException {
        if (archivo.exists() && !archivo.delete()) {
            throw new IOException("No se pudo eliminar el archivo temporal: " + archivo);
        }
    }
}
