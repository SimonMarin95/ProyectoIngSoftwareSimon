/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Resultado de calidad (persistencia en archivo)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-17 / RNF-03 / CA-17.2
 * Criterios: CA-17.2 (bueno) — sobrevive al reinicio
 * Estructuras: Arreglo con contador (persistido línea a línea)
 * =========================================================== */

package resultado;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Guarda y recupera resultados en archivo de texto.
 * Formato: nombre_quebrada|fecha_campana|temporada|nombre_punto|valor_ica|categoria_ica|fuente
 */
public class PersistenciaResultados {

    private String rutaArchivo;

    public PersistenciaResultados(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    // RNF-03 · CA-17.2 — lee el archivo al iniciar; omite líneas mal formadas
    public int cargar(GestorResultados gestor) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            crearArchivoSiNoExiste();
            return 0;
        }
        if (archivo.length() == 0) {
            return 0;
        }

        int cargados = 0;
        int omitidos = 0;
        BufferedReader lector = null;
        try {
            lector = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                ResultadoCalidad r = parsearLinea(linea);
                if (r == null) {
                    omitidos++;
                    continue;
                }
                String mensaje = gestor.insertarDesdeArchivo(r);
                if (mensaje == null) {
                    cargados++;
                } else {
                    omitidos++;
                }
            }
        } catch (IOException e) {
            System.out.println("MENSAJE : no se pudo leer el archivo (" + e.getMessage() + ").");
        } finally {
            if (lector != null) {
                try {
                    lector.close();
                } catch (IOException e) {
                    // no tumba el programa
                }
            }
        }

        if (omitidos > 0) {
            System.out.println("MENSAJE : se omitieron " + omitidos
                    + " línea(s) mal formadas o duplicadas.");
        }
        return cargados;
    }

    // RNF-03 — sobrescribe el archivo con el estado actual
    public boolean guardar(GestorResultados gestor) {
        crearCarpetaPadre();
        BufferedWriter escritor = null;
        try {
            escritor = new BufferedWriter(new FileWriter(rutaArchivo, false));
            escritor.write("# resultados_calidad — nombres del diccionario de datos");
            escritor.newLine();
            escritor.write("# nombre_quebrada|fecha_campana|temporada|nombre_punto|valor_ica|categoria_ica|fuente");
            escritor.newLine();

            ResultadoCalidad[] lista = gestor.obtenerTodos();
            int n = gestor.getCantidad();
            for (int i = 0; i < n; i++) {
                escritor.write(formatearLinea(lista[i]));
                escritor.newLine();
            }
            escritor.flush();
            return true;
        } catch (IOException e) {
            System.out.println("MENSAJE : error al guardar (" + e.getMessage() + ").");
            return false;
        } finally {
            if (escritor != null) {
                try {
                    escritor.close();
                } catch (IOException e) {
                    // ignorado
                }
            }
        }
    }

    // RNF-03 — convierte un resultado a una línea de texto
    private String formatearLinea(ResultadoCalidad r) {
        return r.getNombreQuebrada() + "|"
                + r.getFechaCampana() + "|"
                + r.getTemporada() + "|"
                + r.getNombrePunto() + "|"
                + String.format(java.util.Locale.US, "%.2f", r.getValorIca()) + "|"
                + r.getCategoriaIca() + "|"
                + r.getFuente();
    }

    // RNF-03 — parsea una línea; null si está mal formada
    private ResultadoCalidad parsearLinea(String linea) {
        String[] partes = linea.split("\\|", -1);
        if (partes.length < 7) {
            return null;
        }
        Double ica = ValidadorResultado.parsearIca(partes[4]);
        if (ica == null) {
            return null;
        }
        if (!ValidadorResultado.esCategoriaValida(partes[5])) {
            return null;
        }
        if (!ValidadorResultado.esPuntoValido(partes[3])) {
            return null;
        }
        if (!ValidadorResultado.esTemporadaValida(partes[2])) {
            return null;
        }
        ResultadoCalidad r = new ResultadoCalidad(
                partes[0].trim(),
                partes[1].trim(),
                partes[2].trim().toLowerCase(),
                partes[3].trim().toLowerCase(),
                ica.doubleValue(),
                partes[5].trim().toLowerCase()
        );
        String fuente = partes[6].trim();
        if (!fuente.isEmpty()) {
            r.setFuente(fuente);
        }
        return r;
    }

    // RNF-03 — crea el archivo si no existe
    private void crearArchivoSiNoExiste() {
        crearCarpetaPadre();
        File archivo = new File(rutaArchivo);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
                BufferedWriter w = new BufferedWriter(new FileWriter(archivo));
                w.write("# Archivo de resultados de calidad — RedRío / La Iguaná");
                w.newLine();
                w.write("# nombre_quebrada|fecha_campana|temporada|nombre_punto|valor_ica|categoria_ica|fuente");
                w.newLine();
                w.close();
            }
        } catch (IOException e) {
            System.out.println("MENSAJE : no se pudo crear el archivo (" + e.getMessage() + ").");
        }
    }

    private void crearCarpetaPadre() {
        File archivo = new File(rutaArchivo);
        File padre = archivo.getParentFile();
        if (padre != null && !padre.exists()) {
            padre.mkdirs();
        }
    }
}
