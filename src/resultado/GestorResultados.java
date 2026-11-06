/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Resultado de calidad (gestor / CRUDL)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-17 a HU-22 / RF-28 a RF-39 / RN-02 · RN-04 · RN-11
 * Criterios: CA-17.1 a CA-22.3 (bueno y falla)
 * Estructuras: Arreglo con contador · Pila propia
 * =========================================================== */

package resultado;

import integracion.BuscadorQuebrada;

/**
 * Gestiona el arreglo de resultados con contador (sin ArrayList).
 */
public class GestorResultados {

    private ResultadoCalidad[] resultados;
    private int cantidad;
    private int capacidad;
    private PilaDeshacer pilaDeshacer;
    private PersistenciaResultados persistencia;
    private BuscadorQuebrada buscadorQuebrada;

    public GestorResultados(int capacidad, String rutaArchivo, BuscadorQuebrada buscadorQuebrada) {
        if (capacidad < 5) {
            capacidad = 50;
        }
        this.capacidad = capacidad;
        this.resultados = new ResultadoCalidad[capacidad];
        this.cantidad = 0;
        this.pilaDeshacer = new PilaDeshacer(30);
        this.persistencia = new PersistenciaResultados(rutaArchivo);
        this.buscadorQuebrada = buscadorQuebrada;
    }

    public int getCantidad() {
        return cantidad;
    }

    public PersistenciaResultados getPersistencia() {
        return persistencia;
    }

    public BuscadorQuebrada getBuscadorQuebrada() {
        return buscadorQuebrada;
    }

    // RNF-03 — permite cambiar el buscador al integrar el módulo de Willin
    public void setBuscadorQuebrada(BuscadorQuebrada buscadorQuebrada) {
        this.buscadorQuebrada = buscadorQuebrada;
    }

    // RNF-03 — recupera registros al iniciar
    public int cargarDesdeArchivo() {
        return persistencia.cargar(this);
    }

    // RNF-03 — graba el arreglo completo en el archivo
    public boolean guardarEnArchivo() {
        return persistencia.guardar(this);
    }

    // RF-28 · CA-17.1 — registra ICA y categoría con fuente RedRío
    // RF-29 · CA-17.3 — rechaza si el punto ya tiene resultado en esa campaña
    // RF-30 · CA-17.4 — rechaza categoría inválida
    // RF-32 · CA-17.6 — rechaza si la quebrada no existe
    // RF-34 · CA-17.8 — rechaza si valor_ica no es número
    // RN-02 · RN-04 · RN-11
    public String registrar(String nombreQuebrada, String fechaCampana, String temporada,
                            String nombrePunto, String textoIca, String categoriaIca) {

        if (nombreQuebrada == null || nombreQuebrada.trim().isEmpty()) {
            return "(!) Debe escribir nombre_quebrada.";
        }
        nombreQuebrada = nombreQuebrada.trim();

        // RF-32 · CA-17.6 — rechaza si la quebrada no está registrada
        if (buscadorQuebrada == null || !buscadorQuebrada.existeQuebrada(nombreQuebrada)) {
            return "(!) Esa quebrada no existe. (RF-32 · CA-17.6)";
        }

        if (fechaCampana == null || fechaCampana.trim().isEmpty()) {
            return "(!) Debe escribir fecha_campana (dd/MM/yyyy).";
        }
        fechaCampana = fechaCampana.trim();
        if (!ValidadorResultado.esFechaValida(fechaCampana)) {
            return "(!) fecha_campana no válida o posterior a hoy. Use dd/MM/yyyy.";
        }

        // Temporada pertenece a Campaña; se pide aquí mientras ese módulo no está integrado
        if (!ValidadorResultado.esTemporadaValida(temporada)) {
            return "(!) temporada debe ser seca o lluvias. (RN-03)";
        }
        temporada = temporada.trim().toLowerCase();

        if (nombrePunto == null || nombrePunto.trim().isEmpty()) {
            return "(!) Debe escribir nombre_punto.";
        }
        // RF-33 — puntos válidos del diccionario (parte alta/media/baja)
        if (!ValidadorResultado.esPuntoValido(nombrePunto)) {
            return "(!) Ese punto no existe. Use: parte alta, parte media, parte baja. (RF-33 · CA-17.7)";
        }
        nombrePunto = nombrePunto.trim().toLowerCase();

        // RF-34 · CA-17.8 — el valor del ICA solo admite números
        Double valorIca = ValidadorResultado.parsearIca(textoIca);
        if (valorIca == null) {
            return "(!) El valor del ICA solo admite números. (RF-34 · CA-17.8)";
        }

        // RF-30 · CA-17.4 — categoría inválida
        if (!ValidadorResultado.esCategoriaValida(categoriaIca)) {
            return "(!) Categoría no válida. Use: muy mala, mala, regular, aceptable, buena. (RF-30 · CA-17.4)";
        }
        categoriaIca = categoriaIca.trim().toLowerCase();

        if (cantidad >= capacidad) {
            return "(!) No hay espacio para más resultados.";
        }

        ResultadoCalidad nuevo = new ResultadoCalidad(
                nombreQuebrada, fechaCampana, temporada, nombrePunto,
                valorIca.doubleValue(), categoriaIca
        );

        // RF-29 · CA-17.3 — un solo resultado por punto y campaña (RN-04)
        if (buscarIndicePorClave(nuevo.claveUnica()) >= 0) {
            return "(!) Ese punto ya tiene resultado en esa campaña. (RF-29 · CA-17.3)";
        }

        resultados[cantidad] = nuevo;
        cantidad++;
        guardarEnArchivo();
        return "OK: resultado registrado (fuente RedRío). Total: " + cantidad + ". (RF-28 · CA-17.1)";
    }

    // RNF-03 — inserción interna al cargar archivo (sin revalidar quebrada)
    String insertarDesdeArchivo(ResultadoCalidad r) {
        if (r == null) {
            return "nulo";
        }
        if (cantidad >= capacidad) {
            return "sin cupo";
        }
        if (buscarIndicePorClave(r.claveUnica()) >= 0) {
            return "duplicado";
        }
        resultados[cantidad] = r;
        cantidad++;
        return null;
    }

    // RF-35 · CA-18.1 · CA-18.3 — consulta por punto y campaña
    public ResultadoCalidad consultar(String nombreQuebrada, String fechaCampana, String nombrePunto) {
        int i = buscarIndice(nombreQuebrada, fechaCampana, nombrePunto);
        if (i < 0) {
            return null;
        }
        return resultados[i];
    }

    // RF-35 · CA-18.2 · CA-18.4 — busca texto parcial en categoria_ica
    public ResultadoCalidad[] buscarPorCategoria(String textoParcial) {
        if (textoParcial == null || textoParcial.trim().isEmpty()) {
            return new ResultadoCalidad[0];
        }
        String filtro = textoParcial.trim().toLowerCase();
        int coincidencias = 0;
        for (int i = 0; i < cantidad; i++) {
            if (resultados[i].getCategoriaIca().contains(filtro)) {
                coincidencias++;
            }
        }
        ResultadoCalidad[] hallados = new ResultadoCalidad[coincidencias];
        int pos = 0;
        for (int i = 0; i < cantidad; i++) {
            if (resultados[i].getCategoriaIca().contains(filtro)) {
                hallados[pos] = resultados[i];
                pos++;
            }
        }
        return hallados;
    }

    // RF-36 · CA-19.1 — corrige valor_ica y categoria_ica
    // RF-36 · CA-19.2 — rechaza categoría inválida y conserva la anterior
    // RF-36 · CA-19.3 — no ofrece cambiar punto ni campaña
    // RF-36 · CA-19.4 — avisa si el resultado no existe
    public String actualizar(String nombreQuebrada, String fechaCampana, String nombrePunto,
                             String textoIca, String categoriaIca) {
        int i = buscarIndice(nombreQuebrada, fechaCampana, nombrePunto);
        if (i < 0) {
            return "(!) Ese resultado no existe. (RF-36 · CA-19.4)";
        }

        Double valorIca = ValidadorResultado.parsearIca(textoIca);
        if (valorIca == null) {
            return "(!) El valor del ICA solo admite números. (RF-34 · CA-17.8)";
        }
        if (!ValidadorResultado.esCategoriaValida(categoriaIca)) {
            return "(!) Categoría no válida. Use: muy mala, mala, regular, aceptable, buena. (RF-36 · CA-19.2)";
        }

        resultados[i].setValorIca(valorIca.doubleValue());
        resultados[i].setCategoriaIca(categoriaIca.trim().toLowerCase());
        guardarEnArchivo();
        return "OK: resultado corregido. (RF-36 · CA-19.1)";
    }

    // RF-37 · CA-20.1 — elimina sin dejar huecos (desplazamiento)
    // RF-37 · CA-20.2 — avisa si no existe
    // RF-37 · CA-20.3 — pide nombre_punto si viene vacío
    public String eliminar(String nombreQuebrada, String fechaCampana, String nombrePunto) {
        if (nombrePunto == null || nombrePunto.trim().isEmpty()) {
            return "(!) Debe escribir nombre_punto. (RF-37 · CA-20.3)";
        }
        int i = buscarIndice(nombreQuebrada, fechaCampana, nombrePunto);
        if (i < 0) {
            return "(!) Ese resultado no existe. (RF-37 · CA-20.2)";
        }

        ResultadoCalidad eliminado = resultados[i].copiar();
        // Desplazamiento: compacta el arreglo para no dejar espacios vacíos
        for (int j = i; j < cantidad - 1; j++) {
            resultados[j] = resultados[j + 1];
        }
        resultados[cantidad - 1] = null;
        cantidad--;

        // RF-39 — guarda en la pila para poder deshacer
        pilaDeshacer.apilar(eliminado);
        guardarEnArchivo();
        return "OK: resultado eliminado. Puede usar Deshacer. (RF-37 · CA-20.1)";
    }

    // RF-39 · CA-22.1 — deshace la última eliminación
    // RF-39 · CA-22.2 — avisa si no hay nada que deshacer
    // RF-39 · CA-22.3 — solo recupera el último (LIFO)
    public String deshacerEliminacion() {
        if (pilaDeshacer.estaVacia()) {
            return "(!) No hay nada que deshacer. (RF-39 · CA-22.2)";
        }
        ResultadoCalidad recuperado = pilaDeshacer.desapilar();

        if (buscarIndicePorClave(recuperado.claveUnica()) >= 0) {
            return "(!) No se pudo deshacer: ya existe un resultado igual (punto y campaña).";
        }
        if (cantidad >= capacidad) {
            pilaDeshacer.apilar(recuperado);
            return "(!) No hay espacio para recuperar el resultado.";
        }

        resultados[cantidad] = recuperado;
        cantidad++;
        guardarEnArchivo();
        return "OK: se recuperó el resultado de \"" + recuperado.getNombrePunto()
                + "\" (" + recuperado.getFechaCampana() + "). (RF-39 · CA-22.1)";
    }

    public ResultadoCalidad[] obtenerTodos() {
        return resultados;
    }

    // RF-38 — copia compacta sin nulos para listar
    public ResultadoCalidad[] obtenerCopiaCompacta() {
        ResultadoCalidad[] copia = new ResultadoCalidad[cantidad];
        for (int i = 0; i < cantidad; i++) {
            copia[i] = resultados[i];
        }
        return copia;
    }

    // RF-38 · CA-21.3 — filtra por nombre_quebrada
    public ResultadoCalidad[] filtrarPorQuebrada(String nombreQuebrada) {
        if (nombreQuebrada == null) {
            return new ResultadoCalidad[0];
        }
        String q = nombreQuebrada.trim().toLowerCase();
        int n = 0;
        for (int i = 0; i < cantidad; i++) {
            if (resultados[i].getNombreQuebrada().trim().toLowerCase().equals(q)) {
                n++;
            }
        }
        ResultadoCalidad[] filtrados = new ResultadoCalidad[n];
        int pos = 0;
        for (int i = 0; i < cantidad; i++) {
            if (resultados[i].getNombreQuebrada().trim().toLowerCase().equals(q)) {
                filtrados[pos] = resultados[i];
                pos++;
            }
        }
        return filtrados;
    }

    // RF-38 · CA-21.1 — promedio del ICA
    public double calcularPromedioIca() {
        return calcularPromedioIca(obtenerCopiaCompacta());
    }

    // RF-38 · CA-21.1 — promedio del ICA sobre una lista
    public double calcularPromedioIca(ResultadoCalidad[] lista) {
        if (lista == null || lista.length == 0) {
            return 0.0;
        }
        double suma = 0.0;
        for (int i = 0; i < lista.length; i++) {
            suma += lista[i].getValorIca();
        }
        return suma / lista.length;
    }

    private int buscarIndice(String nombreQuebrada, String fechaCampana, String nombrePunto) {
        if (nombreQuebrada == null || fechaCampana == null || nombrePunto == null) {
            return -1;
        }
        ResultadoCalidad temp = new ResultadoCalidad();
        temp.setNombreQuebrada(nombreQuebrada.trim());
        temp.setFechaCampana(fechaCampana.trim());
        temp.setNombrePunto(nombrePunto.trim().toLowerCase());
        return buscarIndicePorClave(temp.claveUnica());
    }

    // RN-04 — busca por clave lógica (quebrada + fecha + punto)
    private int buscarIndicePorClave(String clave) {
        for (int i = 0; i < cantidad; i++) {
            if (resultados[i].claveUnica().equals(clave)) {
                return i;
            }
        }
        return -1;
    }
}
