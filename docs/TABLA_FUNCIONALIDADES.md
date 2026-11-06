# Tabla de ubicación exacta de funcionalidades

Líneas del código **actual** en `src/` (después del ajuste al mockup 9.3 y cabeceras 10.7).

| # | Funcionalidad | Archivo | Método | Línea inicial | Línea final | Qué demuestra | Escenario / captura |
|---|---|---|---|---|---|---|---|
| 1 | Registro (Guardar) | `resultado/GestorResultados.java` | `registrar` | 72 | 138 | Inserta en arreglo, valida y guarda | Fig. 10.7 — opción **1** |
| 2 | Validación de duplicados | `resultado/GestorResultados.java` | bloque RF-29 dentro de `registrar` | 129 | 131 | Mensaje mockup `(RF-29 · CA-17.3)` | Fig. 10.8 |
| 3 | Listado | `resultado/MenuResultados.java` | `opcionListar` | 254 | 285 | Columnas del diccionario + promedio | Fig. 10.7 / 10.9 — opción **3** |
| 4 | Eliminación sin huecos | `resultado/GestorResultados.java` | `eliminar` (desplazamiento 226–228) | 216 | 237 | Compacta el arreglo | Fig. 10.9 — opción **5** |
| 5 | Pila para deshacer | `resultado/PilaDeshacer.java` | `apilar` / `desapilar` | 45 / 60 | 57 / 68 | Pila LIFO propia | Fig. 10.10 — opción **6** |
| 6 | Mensaje si no hay nada que deshacer | `resultado/GestorResultados.java` | `deshacerEliminacion` | 242 | 244 | `(!) No hay nada que deshacer. (RF-39 · CA-22.2)` | Fig. 10.11 |
| 7 | Guardado en archivos | `resultado/PersistenciaResultados.java` | `guardar` | 90 | 120 | Escribe `datos/resultados.txt` | Tras cada cambio; Fig. 10.12 |
| 8 | Recuperación al iniciar | `resultado/PersistenciaResultados.java` | `cargar` | 37 | 87 | Lee al arrancar (RNF-03 · CA-17.2) | Fig. 10.12 |
| 9 | Comparación entre puntos | `resultado/ComparadorCalidad.java` | `compararEntrePuntos` | 27 | 55 | Alta → media → baja (RF-64) | Opción **7** |
| 10 | Comparación entre temporadas | `resultado/ComparadorCalidad.java` | `compararEntreTemporadas` | 60 | 148 | Seca vs lluvias (RF-63/65) | Opción **8** |
| 11 | Evolución histórica | `resultado/ComparadorCalidad.java` | `evolucionHistorica` | 151 | 227 | Orden cronológico + MEJORA/EMPEORA | Opción **9** |
| 12 | Validación de quebradas | `GestorResultados.java` + `BuscadorQuebrada.java` | RF-32 en `registrar` / `existeQuebrada` | 80–82 / 20 | 82 / 20 | `(!) Esa quebrada no existe. (RF-32 · CA-17.6)` | Guardar quebrada inventada |

## Mockup y cabeceras (guía 10.7)

| Elemento | Archivo | Líneas / zona |
|---|---|---|
| Pantalla Figura 9.3 | `MenuResultados.java` | `dibujarPantalla` — título, MENSAJE, opciones 1–6 y 0 (~111–160) |
| Pantalla Figura 9.1 | `Main.java` | `mostrarPantallaPrincipal` |
| Cabecera de clase 10.7 | Todas las clases en `src/` | Bloque `/* Proyecto … Estructuras */` al inicio |
| Orden ICA no alfabético | `ValidadorResultado.java` | `ordenCategoria` 59–80 |

## Fuera del código

Ver `docs/CHECKLIST_ENTREGA_DOCENTE.md` (capturas, predicción a mano, bitácora IA, enlace GitHub, unión con Willin).
