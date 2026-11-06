/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Quebrada (persistencia en archivo)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * RNF-03
 * Criterios: los registros permanecen después de cerrar y abrir el programa
 * Estructuras: Arreglo con contador (persistido línea a línea)
 * =========================================================== */

package quebrada;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Guarda y recupera quebradas en formato nombre_quebrada|numero_puntos.
 */
public class PersistenciaQuebradas {

    private String rutaArchivo;

    public PersistenciaQuebradas(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // RNF-03 — recupera los registros al iniciar.
    public int cargar(GestorQuebradas gestor) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            crearArchivoSiNoExiste();
            return 0;
        }
        if (archivo.length() == 0) {
            return 0;
        }

        int cargadas = 0;
        int omitidas = 0;
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(
                new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.replace("\uFEFF", "").trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                String[] partes = linea.split("\\|", -1);
                if (partes.length != 2) {
                    omitidas++;
                    continue;
                }
                try {
                    Quebrada quebrada = new Quebrada(partes[0].trim(),
                            Integer.parseInt(partes[1].trim()));
                    if (gestor.insertarDesdeArchivo(quebrada)) {
                        cargadas++;
                    } else {
                        omitidas++;
                    }
                } catch (NumberFormatException e) {
                    omitidas++;
                }
            }
        } catch (IOException e) {
            System.out.println("MENSAJE : no se pudo leer el archivo de quebradas ("
                    + e.getMessage() + ").");
            return cargadas;
        }

        if (omitidas > 0) {
            System.out.println("MENSAJE : se omitieron " + omitidas
                    + " línea(s) de quebradas mal formadas o duplicadas.");
        }
        return cargadas;
    }

    // RNF-03 — sobrescribe el archivo con el estado actual.
    public boolean guardar(GestorQuebradas gestor) {
        if (!crearCarpetaPadre()) {
            return false;
        }
        try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(rutaArchivo, false), StandardCharsets.UTF_8))) {
            escritor.write("# quebradas — nombre_quebrada|numero_puntos");
            escritor.newLine();
            Quebrada[] quebradas = gestor.obtenerTodos();
            for (int i = 0; i < gestor.getCantidad(); i++) {
                escritor.write(quebradas[i].getNombreQuebrada());
                escritor.write("|");
                escritor.write(Integer.toString(quebradas[i].getNumeroPuntos()));
                escritor.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("MENSAJE : error al guardar quebradas (" + e.getMessage() + ").");
            return false;
        }
    }

    // RNF-03 — crea el archivo vacío al primer inicio.
    private boolean crearArchivoSiNoExiste() {
        if (!crearCarpetaPadre()) {
            return false;
        }
        File archivo = new File(rutaArchivo);
        try {
            if (archivo.exists()) {
                return true;
            }
            try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
                escritor.write("# quebradas — nombre_quebrada|numero_puntos");
                escritor.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("MENSAJE : no se pudo crear el archivo de quebradas ("
                    + e.getMessage() + ").");
            return false;
        }
    }

    // RNF-03 — crea la carpeta de datos si todavía no existe.
    private boolean crearCarpetaPadre() {
        File padre = new File(rutaArchivo).getParentFile();
        if (padre == null || padre.exists()) {
            return true;
        }
        if (padre.mkdirs()) {
            return true;
        }
        System.out.println("MENSAJE : no se pudo crear la carpeta de datos para quebradas.");
        return false;
    }
}
