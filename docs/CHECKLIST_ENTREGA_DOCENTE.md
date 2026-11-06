# Checklist fuera del código (guía 10.8 · 10.9 · 10.11 · Anexo B)

Esto **no lo resuelve el programa**: debe completarlo usted en el documento Word y en físico.

## 1. Seis capturas dentro del documento (10.8 / 10.4)

Pegue capturas **reales** de la consola, en este orden, en la sección 10.4:

| Figura | Qué capturar | Cómo obtenerla |
|---|---|---|
| 10.7 | Tres resultados + listado | Menú `2` → `1 Guardar` ×3 → `3 Listar` |
| 10.8 | Rechazo de duplicado | `1 Guardar` con el mismo punto+fecha |
| 10.9 | Eliminar el del medio sin huecos | `5 Eliminar` parte media → `3 Listar` |
| 10.10 | Deshacer | `6 Deshacer` → `3 Listar` |
| 10.11 | Nada que deshacer | `6 Deshacer` otra vez |
| 10.12 | Persistencia | `0 Volver` → `0 Salir` → abrir de nuevo → `2` → `3 Listar` |

Detalle paso a paso: `docs/ESCENARIOS_PRUEBA.md`.

La pantalla debe verse como el **mockup Figura 9.3**: cabecera `RESULTADOS DE CALIDAD`, caja `MENSAJE`, columnas `nombre_quebrada`, `fecha_campana`, `nombre_punto`, `valor_ica`, `categoria_ica`, `fuente`, opciones 1–6 y 0.

## 2. Hoja de predicción escrita a mano (10.9)

Entrégala **antes** de mostrar el programa. Plantilla sugerida (copiar a mano):

```
HOJA DE PREDICCIÓN — Resultado de calidad
Nombre: Simón Marín de los Ríos
Fecha: ___________

Escenario A — Guardar tres resultados
  Predigo: el listado mostrará 3 filas y promedio ICA = 0,60

Escenario B — Duplicado
  Predigo: MENSAJE con RF-29 · CA-17.3 y el total sigue en 3

Escenario C — Eliminar el del medio
  Predigo: quedan 2 filas seguidas, sin hueco (parte alta y parte baja)

Escenario D — Deshacer
  Predigo: vuelve parte media; total = 3

Escenario E — Deshacer vacío
  Predigo: MENSAJE "No hay nada que deshacer" (RF-39 · CA-22.2), sin error

Escenario F — Reinicio
  Predigo: al abrir de nuevo se cargan los 3 resultados (RNF-03)
```

## 3. Bitácora de IA — Anexo B

Lo que se califica es la columna **«qué le cambié y por qué»**. Ejemplo:

| Fecha | Qué pedí a la IA | Qué me entregó | Qué le cambié y por qué |
|---|---|---|---|
| 06/11/2026 | Módulo Resultado con CRUDL y pila | Código con menú ampliado | Ajusté el menú al mockup 9.3 (opciones Guardar/Buscar/Listar/Corregir/Eliminar/Deshacer/0) porque la guía exige coincidencia con el mockup |
| 06/11/2026 | Cabeceras de clase | Javadoc solamente | Agregué el bloque /* Proyecto / Entidad / HU-RF-CA / Estructuras */ que pide el punto 10.7 |
| … | … | … | … |

## 4. Enlace del repositorio en el documento

1. Cree el repo en GitHub.
2. Suba el proyecto (comandos en `README.md`).
3. Pegue el enlace real en la sección **10.2 Repositorio** del Word (donde dice «pegar aquí el enlace»).

## 5. Unión con el módulo de Willin

Estado actual: contrato listo (`BuscadorQuebrada`), menú principal con opción 1 Quebradas pendiente.

Cuando Willin entregue su código:

1. Copiar sus clases al mismo proyecto.
2. Implementar un adaptador que llame a su búsqueda.
3. En `Main`, opción 1 debe abrir su pantalla (Figura 9.2).
4. Probar: registrar resultado en quebrada inexistente → `RF-32 · CA-17.6`.

Guía técnica: `docs/INTEGRACION_QUEBRADA.md`.
