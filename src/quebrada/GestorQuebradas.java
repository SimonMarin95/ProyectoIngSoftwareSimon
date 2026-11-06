/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Quebrada (gestor / CRUDL)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-01 a HU-06 / RF-01 a RF-09 / RN-01 / RNF-03
 * Criterios: crear, buscar, actualizar, eliminar, listar y deshacer
 * Estructuras: Arreglo con contador · Pila propia · Archivo de texto
 * =========================================================== */

package quebrada;

import java.util.Locale;

import integracion.BuscadorQuebrada;

/**
 * Gestiona quebradas con arreglo fijo y contador, sin colecciones.
 */
public class GestorQuebradas implements BuscadorQuebrada {

    private Quebrada[] quebradas;
    private int cantidad;
    private PilaDeshacerQuebradas pilaDeshacer;
    private PersistenciaQuebradas persistencia;

    public GestorQuebradas(int capacidad, String rutaArchivo) {
        int capacidadReal = capacidad < 5 ? 50 : capacidad;
        quebradas = new Quebrada[capacidadReal];
        cantidad = 0;
        pilaDeshacer = new PilaDeshacerQuebradas(30);
        persistencia = new PersistenciaQuebradas(rutaArchivo);
    }

    public int getCantidad() {
        return cantidad;
    }

    // RNF-03 — carga quebradas al iniciar.
    public int cargarDesdeArchivo() {
        return persistencia.cargar(this);
    }

    // RNF-03 — guarda el estado actual en archivo.
    public boolean guardarEnArchivo() {
        return persistencia.guardar(this);
    }

    // RF-01 · CA-01.1 — crea quebrada con nombre único y entre uno y tres puntos.
    // RF-02 · CA-01.3 — rechaza nombres repetidos.
    // RF-03 — valida el número de puntos.
    // RF-04 — rechaza registros cuando el arreglo está lleno.
    public String registrar(String nombreQuebrada, String textoNumeroPuntos) {
        if (nombreQuebrada == null || nombreQuebrada.trim().isEmpty()) {
            return "(!) Escriba el nombre de la quebrada. (RF-01)";
        }
        nombreQuebrada = nombreQuebrada.trim();
        if (nombreQuebrada.indexOf('|') >= 0) {
            return "(!) El nombre no puede contener el carácter |. (RF-01)";
        }
        if (existeQuebrada(nombreQuebrada)) {
            return "(!) Esa quebrada ya está registrada. (RF-02 · CA-01.3)";
        }
        Integer numeroPuntos = parsearNumeroPuntos(textoNumeroPuntos);
        if (numeroPuntos == null) {
            return "(!) El número de puntos debe estar entre 1 y 3. (RF-03)";
        }
        if (cantidad >= quebradas.length) {
            return "(!) No hay espacio para más quebradas. (RF-04)";
        }

        quebradas[cantidad] = new Quebrada(nombreQuebrada, numeroPuntos.intValue());
        cantidad++;
        if (!guardarEnArchivo()) {
            return "(!) Se registró en memoria, pero no se pudo guardar el archivo.";
        }
        return "OK: quebrada registrada. (RF-01 · CA-01.1)";
    }

    // RF-05 — busca por nombre completo o por texto parcial.
    public Quebrada[] buscarPorTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return new Quebrada[0];
        }
        String filtro = texto.trim().toLowerCase(Locale.ROOT);
        int coincidencias = 0;
        for (int i = 0; i < cantidad; i++) {
            if (quebradas[i].getNombreQuebrada().toLowerCase(Locale.ROOT).contains(filtro)) {
                coincidencias++;
            }
        }
        Quebrada[] encontradas = new Quebrada[coincidencias];
        int indice = 0;
        for (int i = 0; i < cantidad; i++) {
            if (quebradas[i].getNombreQuebrada().toLowerCase(Locale.ROOT).contains(filtro)) {
                encontradas[indice] = quebradas[i].copiar();
                indice++;
            }
        }
        return encontradas;
    }

    // RF-05 — consulta una quebrada por su identificador.
    public Quebrada consultar(String nombreQuebrada) {
        int indice = buscarIndice(nombreQuebrada);
        return indice < 0 ? null : quebradas[indice].copiar();
    }

    // RF-06 — actualiza numero_puntos y conserva el identificador nombre_quebrada.
    public String actualizar(String nombreQuebrada, String textoNumeroPuntos) {
        int indice = buscarIndice(nombreQuebrada);
        if (indice < 0) {
            return "(!) Esa quebrada no existe. (RF-06)";
        }
        Integer numeroPuntos = parsearNumeroPuntos(textoNumeroPuntos);
        if (numeroPuntos == null) {
            return "(!) Número de puntos no válido; se conserva el anterior. (RF-06)";
        }
        quebradas[indice].setNumeroPuntos(numeroPuntos.intValue());
        if (!guardarEnArchivo()) {
            return "(!) Se actualizó en memoria, pero no se pudo guardar el archivo.";
        }
        return "OK: número de puntos actualizado. El nombre no se puede cambiar. (RF-06)";
    }

    // RF-07 — elimina desplazando el arreglo para no dejar huecos.
    public String eliminar(String nombreQuebrada) {
        if (nombreQuebrada == null || nombreQuebrada.trim().isEmpty()) {
            return "(!) Escriba el nombre de la quebrada. (RF-07)";
        }
        int indice = buscarIndice(nombreQuebrada);
        if (indice < 0) {
            return "(!) Esa quebrada no existe. (RF-07)";
        }

        pilaDeshacer.apilar(quebradas[indice]);
        for (int i = indice; i < cantidad - 1; i++) {
            quebradas[i] = quebradas[i + 1];
        }
        cantidad--;
        quebradas[cantidad] = null;
        if (!guardarEnArchivo()) {
            return "(!) Se eliminó en memoria, pero no se pudo guardar el archivo.";
        }
        return "OK: quebrada eliminada. Puede usar Deshacer. (RF-07)";
    }

    // RF-09 — recupera la última quebrada eliminada o avisa si la pila está vacía.
    public String deshacerEliminacion() {
        if (pilaDeshacer.estaVacia()) {
            return "(!) No hay nada que deshacer. (RF-09)";
        }
        Quebrada recuperada = pilaDeshacer.desapilar();
        if (buscarIndice(recuperada.getNombreQuebrada()) >= 0) {
            pilaDeshacer.apilar(recuperada);
            return "(!) No se pudo deshacer: ya existe una quebrada con ese nombre. (RF-09)";
        }
        if (cantidad >= quebradas.length) {
            pilaDeshacer.apilar(recuperada);
            return "(!) No hay espacio para recuperar la quebrada. (RF-09)";
        }
        quebradas[cantidad] = recuperada;
        cantidad++;
        if (!guardarEnArchivo()) {
            return "(!) Se recuperó en memoria, pero no se pudo guardar el archivo.";
        }
        return "OK: se recuperó la última quebrada eliminada. (RF-09)";
    }

    // RF-08 — devuelve una copia compacta para mostrar la lista.
    public Quebrada[] obtenerCopiaCompacta() {
        Quebrada[] copia = new Quebrada[cantidad];
        for (int i = 0; i < cantidad; i++) {
            copia[i] = quebradas[i].copiar();
        }
        return copia;
    }

    // RF-08 — expone el arreglo ocupado a la persistencia.
    Quebrada[] obtenerTodos() {
        return quebradas;
    }

    // RF-08 · CA-05.1 — suma el número de puntos de todas las quebradas.
    public int calcularTotalPuntos() {
        int total = 0;
        for (int i = 0; i < cantidad; i++) {
            total += quebradas[i].getNumeroPuntos();
        }
        return total;
    }

    // RF-08 · CA-05.3 — cuenta quebradas con menos de tres puntos.
    public int contarMenosDeTresPuntos() {
        int total = 0;
        for (int i = 0; i < cantidad; i++) {
            if (quebradas[i].getNumeroPuntos() < 3) {
                total++;
            }
        }
        return total;
    }

    // RF-32 — permite que Resultado valide la existencia de una quebrada.
    @Override
    public boolean existeQuebrada(String nombreQuebrada) {
        return buscarIndice(nombreQuebrada) >= 0;
    }

    // RNF-03 — inserta un registro válido al recuperar el archivo.
    boolean insertarDesdeArchivo(Quebrada quebrada) {
        if (quebrada == null || quebrada.getNombreQuebrada() == null
                || quebrada.getNombreQuebrada().trim().isEmpty()
                || quebrada.getNumeroPuntos() < 1 || quebrada.getNumeroPuntos() > 3
                || cantidad >= quebradas.length
                || existeQuebrada(quebrada.getNombreQuebrada())) {
            return false;
        }
        quebradas[cantidad] = quebrada.copiar();
        cantidad++;
        return true;
    }

    // RF-02 · RF-05 — busca el identificador sin distinguir mayúsculas.
    private int buscarIndice(String nombreQuebrada) {
        if (nombreQuebrada == null || nombreQuebrada.trim().isEmpty()) {
            return -1;
        }
        String clave = nombreQuebrada.trim().toLowerCase(Locale.ROOT);
        for (int i = 0; i < cantidad; i++) {
            if (quebradas[i].claveUnica().equals(clave)) {
                return i;
            }
        }
        return -1;
    }

    // RF-03 · RF-06 — valida el dominio permitido del número de puntos.
    private Integer parsearNumeroPuntos(String texto) {
        if (texto == null) {
            return null;
        }
        try {
            int puntos = Integer.parseInt(texto.trim());
            return puntos >= 1 && puntos <= 3 ? Integer.valueOf(puntos) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
