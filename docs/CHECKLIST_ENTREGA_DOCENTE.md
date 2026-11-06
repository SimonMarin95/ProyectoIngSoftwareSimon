# Checklist de entrega docente

## 1. Seis capturas del programa de Quebradas

En el documento del proyecto, pega capturas **reales de la consola** en los seis espacios de la sección **10.3, “Capturas del programa de Quebrada”**, en este orden. La guía general del curso describe este requisito en **10.8, “Las seis capturas”**.

| Figura | Evidencia que debe verse | Operación |
|---|---|---|
| 10.1 | Tres quebradas y total de puntos | Guardar La Iguaná (3), La Presidenta (2), Santa Elena (3) → Listar |
| 10.2 | Mensaje al repetir un identificador; la cantidad no aumenta | Guardar otra vez La Iguaná |
| 10.3 | Dos registros contiguos, sin hueco | Eliminar La Presidenta → Listar |
| 10.4 | Registro eliminado restaurado | Deshacer → Listar |
| 10.5 | Aviso de pila vacía, sin error ni cierre | Deshacer otra vez |
| 10.6 | Las tres quebradas siguen después de reiniciar | Volver → Salir → abrir de nuevo → Quebradas |

Pasos de preparación y ejecución: [`ESCENARIOS_PRUEBA.md`](ESCENARIOS_PRUEBA.md). Toma las capturas cuando el listado y el mensaje queden visibles; no uses la salida de una prueba automatizada como evidencia de la interfaz.

## 2. Hoja de predicción escrita a mano (10.9)

Escríbela antes de ejecutar el programa y entrégala antes de mostrarlo:

```text
HOJA DE PREDICCIÓN — Quebradas
Nombre: Simón Marín de los Ríos
Fecha: ___________

Escenario A — Crear tres quebradas
  Predigo: se listan La Iguaná, La Presidenta y Santa Elena; total de puntos = 8

Escenario B — Nombre repetido
  Predigo: se muestra RF-02 · CA-01.3 y el total sigue en 3 quebradas

Escenario C — Eliminar la quebrada del medio
  Predigo: quedan La Iguaná y Santa Elena seguidas, sin hueco

Escenario D — Deshacer
  Predigo: La Presidenta vuelve al listado y la cantidad queda en 3

Escenario E — Deshacer vacío
  Predigo: aparece "No hay nada que deshacer" (RF-09), sin error

Escenario F — Reinicio
  Predigo: al abrir de nuevo se cargan las tres quebradas (RNF-03)
```

## 3. Bitácora de IA — Anexo B

Registra las solicitudes y, especialmente, qué revisaste o cambiaste y por qué. Ejemplo:

| Fecha | Qué pedí a la IA | Qué me entregó | Qué le cambié y por qué |
|---|---|---|---|
| 06/11/2026 | CRUDL de Quebradas con arreglo, pila y archivo | Gestor, menú, persistencia y pruebas | Contrasté los campos con el diccionario (`nombre_quebrada`, `numero_puntos`) y los mensajes con las figuras 9.2 y 10.1–10.6 |
| 06/11/2026 | Integrar Quebradas con Resultados | `GestorQuebradas` implementa `BuscadorQuebrada` | Verifiqué que Resultado rechace una quebrada inexistente (RF-32) |

## 4. Enlace del repositorio

Sube el proyecto a GitHub y pega el enlace real en la sección **10.2 Repositorio** del documento. No dejes un URL de ejemplo.

## 5. Integración entre entidades

La opción 1 abre Quebradas; la opción 2 abre Resultados. `GestorQuebradas` implementa `BuscadorQuebrada`, que Resultado consulta al guardar un registro. Detalle técnico: [`INTEGRACION_QUEBRADA.md`](INTEGRACION_QUEBRADA.md).

## 6. Antes de entregar

- Compilar desde la raíz con los comandos del [`README.md`](../README.md).
- Ejecutar `PruebaEscenariosQuebrada` y `PruebaEscenarios`.
- Revisar que los mensajes visibles incluyan los requisitos que menciona el documento.
- Completar manualmente las capturas, la hoja de predicción y la bitácora; esos documentos no se generan desde el código.
