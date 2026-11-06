# Integración de Quebradas y Resultados

## Estado

El proyecto contiene los dos módulos en el mismo menú. La opción **1** abre el CRUDL de Quebradas y la opción **2** abre Resultados. El módulo Quebradas ya no usa el adaptador temporal de demostración.

## Contrato de validación

`quebrada.GestorQuebradas` implementa `integracion.BuscadorQuebrada`:

```java
boolean existeQuebrada(String nombreQuebrada)
```

`Main` inyecta el gestor real al crear `resultado.GestorResultados`. Antes de registrar un resultado, `GestorResultados.registrar(...)` consulta esa interfaz y rechaza el registro si el nombre no existe (RF-32 · CA-17.6).

## Operaciones de Quebradas

| Opción | Operación | Requisito |
|---|---|---|
| 1 | Guardar `nombre_quebrada` y `numero_puntos` (1 a 3); rechazar identificadores repetidos | RF-01 a RF-04 |
| 2 | Buscar por nombre completo o texto parcial | RF-05 |
| 3 | Listar y sumar `numero_puntos` | RF-08 |
| 4 | Corregir `numero_puntos`; el nombre, por ser identificador, no se cambia | RF-06 |
| 5 | Eliminar compactando el arreglo | RF-07 |
| 6 | Deshacer la última eliminación con una pila LIFO propia | RF-09 |
| 0 | Volver al menú principal | — |

La entidad usa arreglo fijo con contador, pila propia y archivo de texto. No usa `ArrayList`, `LinkedList` ni `java.util.Stack`.

## Persistencia

- Quebradas: `datos/quebradas.txt`, una línea por registro: `nombre_quebrada|numero_puntos`.
- Resultados: `datos/resultados.txt`, administrado por el módulo Resultado.
- Ambos módulos se cargan al iniciar y se guardan al salir.

Si luego se integra el código original de otro integrante, debe conservarse el contrato `BuscadorQuebrada` y comprobar que la búsqueda use los mismos nombres persistidos por Quebradas.
