# Tabla de ubicación de funcionalidades

Líneas actuales del código. Las Figuras 10.1–10.6 corresponden a Quebradas y son las capturas descritas en `ESCENARIOS_PRUEBA.md`.

## Quebradas — capturas 10.1 a 10.6

| Evidencia / función | Archivo y línea | Qué demuestra |
|---|---|---|
| Menú, mensaje y pantalla de Quebradas | [`MenuQuebradas.java`](../src/quebrada/MenuQuebradas.java#L80) | Encabezado, caja MENSAJE y opciones |
| Listado y total de puntos — Fig. 10.1 | [`MenuQuebradas.java`](../src/quebrada/MenuQuebradas.java#L120) | `nombre_quebrada`, `numero_puntos`, total y quebradas con menos de 3 puntos |
| Crear — Fig. 10.1 | [`GestorQuebradas.java`](../src/quebrada/GestorQuebradas.java#L53) | Inserta en arreglo y persiste |
| Rechazar nombre repetido — Fig. 10.2 | [`GestorQuebradas.java`](../src/quebrada/GestorQuebradas.java#L61) | Mensaje RF-02 · CA-01.3; no aumenta la cantidad |
| Eliminar del medio y compactar — Fig. 10.3 | [`GestorQuebradas.java`](../src/quebrada/GestorQuebradas.java#L127) | Elimina desplazando el arreglo |
| Deshacer y mensaje de pila vacía — Fig. 10.4/10.5 | [`GestorQuebradas.java`](../src/quebrada/GestorQuebradas.java#L149) | Recupera la última eliminación; informa si no hay pendientes |
| Operaciones de pila LIFO | [`PilaDeshacerQuebradas.java`](../src/quebrada/PilaDeshacerQuebradas.java#L43) | Apilar/desapilar una quebrada sin `Stack` |
| Persistencia de Quebradas — Fig. 10.6 | [`PersistenciaQuebradas.java`](../src/quebrada/PersistenciaQuebradas.java#L35) | Cargar al iniciar y guardar al salir |
| Integración con Resultados | [`GestorQuebradas.java`](../src/quebrada/GestorQuebradas.java#L206) | `existeQuebrada` implementa el contrato de búsqueda |
| Inicio, carga y cierre de ambos módulos | [`Main.java`](../src/Main.java#L30) | Menú principal, persistencia y navegación |
| Prueba automatizada de las seis capturas | [`PruebaEscenariosQuebrada.java`](../src/PruebaEscenariosQuebrada.java#L22) | 6 escenarios y verificaciones adicionales |

## Resultados — pruebas del módulo existente

| Funcionalidad | Archivo y línea | Qué demuestra |
|---|---|---|
| Registro y validaciones | [`GestorResultados.java`](../src/resultado/GestorResultados.java#L72) | Inserta resultados y valida quebrada, punto, campaña e ICA |
| Rechazo de resultados duplicados | [`GestorResultados.java`](../src/resultado/GestorResultados.java#L129) | RF-29 · CA-17.3 |
| Listado y promedio ICA | [`MenuResultados.java`](../src/resultado/MenuResultados.java#L245) | Columnas del diccionario y promedio |
| Eliminar sin huecos | [`GestorResultados.java`](../src/resultado/GestorResultados.java#L216) | Compacta el arreglo |
| Deshacer y pila vacía | [`GestorResultados.java`](../src/resultado/GestorResultados.java#L242) | RF-39 · CA-22.1 a CA-22.3 |
| Persistencia | [`PersistenciaResultados.java`](../src/resultado/PersistenciaResultados.java#L37) | Lee y escribe `datos/resultados.txt` |
| Prueba automatizada | [`PruebaEscenarios.java`](../src/PruebaEscenarios.java#L24) | 6 escenarios de Resultados |

## Mockups y cabeceras

| Elemento | Archivo y línea |
|---|---|
| Menú principal, Figura 9.1 | [`Main.java`](../src/Main.java#L93) |
| Pantalla de Quebradas, Figura 9.2 | [`MenuQuebradas.java`](../src/quebrada/MenuQuebradas.java#L80) |
| Pantalla de Resultados, Figura 9.3 | [`MenuResultados.java`](../src/resultado/MenuResultados.java#L106) |
| Orden de categorías ICA | [`ValidadorResultado.java`](../src/resultado/ValidadorResultado.java#L59) |

## Entrega fuera del código

Las capturas reales, la predicción manuscrita y la bitácora deben agregarse manualmente al documento del curso. Consulta [`CHECKLIST_ENTREGA_DOCENTE.md`](CHECKLIST_ENTREGA_DOCENTE.md).
