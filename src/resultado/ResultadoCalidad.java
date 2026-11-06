/* ===========================================================
 * Proyecto       : Monitoreo de calidad del agua — Quebrada La Iguaná (RedRío / SIATA)
 * Entidad        : Resultado de calidad (Tabla 7.5)
 * Programado por : Simón Marín de los Ríos
 * Fecha          : 06/11/2026
 * HU-17 / RF-28 / RN-02 · RN-04 · RN-11
 * Criterios: CA-17.1 (bueno)
 * Estructuras: Objeto de dominio (sin colecciones)
 * =========================================================== */

package resultado;

/**
 * Un resultado de calidad del agua según el diccionario de datos (Tabla 7.5).
 * Campos: nombre_quebrada, fecha_campana, nombre_punto, valor_ica, categoria_ica, fuente.
 * temporada se guarda como apoyo desde Campaña (Tabla 7.4) para comparaciones.
 */
public class ResultadoCalidad {

    private String nombreQuebrada;
    private String fechaCampana;
    private String temporada;
    private String nombrePunto;
    private double valorIca;
    private String categoriaIca;
    private String fuente;

    public ResultadoCalidad() {
        this.fuente = "RedRío";
    }

    // RF-28 · CA-17.1 — construye un resultado con fuente RedRío (RN-11)
    public ResultadoCalidad(String nombreQuebrada, String fechaCampana, String temporada,
                            String nombrePunto, double valorIca, String categoriaIca) {
        this.nombreQuebrada = nombreQuebrada;
        this.fechaCampana = fechaCampana;
        this.temporada = temporada;
        this.nombrePunto = nombrePunto;
        this.valorIca = valorIca;
        this.categoriaIca = categoriaIca;
        this.fuente = "RedRío";
    }

    // RF-39 — copia independiente para la pila de deshacer
    public ResultadoCalidad copiar() {
        ResultadoCalidad copia = new ResultadoCalidad();
        copia.nombreQuebrada = this.nombreQuebrada;
        copia.fechaCampana = this.fechaCampana;
        copia.temporada = this.temporada;
        copia.nombrePunto = this.nombrePunto;
        copia.valorIca = this.valorIca;
        copia.categoriaIca = this.categoriaIca;
        copia.fuente = this.fuente;
        return copia;
    }

    public String getNombreQuebrada() {
        return nombreQuebrada;
    }

    public void setNombreQuebrada(String nombreQuebrada) {
        this.nombreQuebrada = nombreQuebrada;
    }

    public String getFechaCampana() {
        return fechaCampana;
    }

    public void setFechaCampana(String fechaCampana) {
        this.fechaCampana = fechaCampana;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public String getNombrePunto() {
        return nombrePunto;
    }

    public void setNombrePunto(String nombrePunto) {
        this.nombrePunto = nombrePunto;
    }

    public double getValorIca() {
        return valorIca;
    }

    public void setValorIca(double valorIca) {
        this.valorIca = valorIca;
    }

    public String getCategoriaIca() {
        return categoriaIca;
    }

    public void setCategoriaIca(String categoriaIca) {
        this.categoriaIca = categoriaIca;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    // RN-04 — clave lógica: un solo resultado por punto y campaña
    public String claveUnica() {
        return normalizar(nombreQuebrada) + "|" + normalizar(fechaCampana) + "|" + normalizar(nombrePunto);
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().toLowerCase();
    }

    @Override
    public String toString() {
        return nombreQuebrada + " | " + fechaCampana + " | " + nombrePunto
                + " | " + valorIca + " | " + categoriaIca + " | " + fuente;
    }
}
