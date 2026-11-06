# Escenarios de prueba y capturas

## Capturas de Quebradas — Figuras 10.1 a 10.6

La imagen del documento del proyecto ubica estos seis espacios en la sección **10.3, “Capturas del programa de Quebrada”**. La guía general del curso describe la misma entrega en **10.8, “Las seis capturas”**. Pega una captura real de consola en cada espacio, conserva el orden y los pies de figura.

### Preparación de una ejecución limpia

Desde la raíz del proyecto, si ya existe un archivo de quebradas, conserva primero una copia con fecha. Después deja solo la línea de comentario para que el escenario empiece sin registros:

```powershell
if (Test-Path datos\quebradas.txt) {
    $copia = "datos\quebradas-respaldo-" + (Get-Date -Format "yyyyMMdd-HHmmss") + ".txt"
    Copy-Item datos\quebradas.txt $copia
}
Set-Content -Path datos\quebradas.txt -Value "# quebradas — nombre_quebrada|numero_puntos" -Encoding ascii

javac -encoding UTF-8 -d bin src\Main.java src\PruebaEscenariosQuebrada.java src\integracion\*.java src\quebrada\*.java src\resultado\*.java
java -cp bin Main
```

En el menú principal, usa la opción **1 (Quebradas)**. Para la primera figura, registra en este orden:

1. `La Iguaná`, `3`
2. `La Presidenta`, `2`
3. `Santa Elena`, `3`

La lista debe mostrar `Total de puntos: 8`.

### Figura 10.1 — Crear tres quebradas y listarlas

Después de registrar las tres, selecciona **3 Listar**. Captura el encabezado `QUEBRADAS`, el listado con las tres filas y el total de puntos. El archivo debe tener tres registros.

### Figura 10.2 — Rechazar nombre repetido

Selecciona **1 Guardar** e intenta registrar `La Iguaná` otra vez, con cualquier número válido de puntos.

Captura el mensaje `Esa quebrada ya está registrada (RF-02 · CA-01.3)` y el listado que conserva solo tres quebradas.

### Figura 10.3 — Eliminar la quebrada del medio sin huecos

Selecciona **5 Eliminar** y escribe `La Presidenta`. Luego selecciona **3 Listar**.

Captura las filas `La Iguaná` y `Santa Elena` seguidas, sin espacio vacío en medio.

### Figura 10.4 — Deshacer la eliminación

Selecciona **6 Deshacer** y luego **3 Listar**.

Captura el mensaje de recuperación y las tres filas, con `La Presidenta` restaurada.

### Figura 10.5 — Deshacer cuando no hay nada pendiente

Selecciona **6 Deshacer** otra vez, sin eliminar nada entre ambos intentos.

Captura el mensaje `No hay nada que deshacer (RF-09)`. El programa debe seguir mostrando el menú, sin excepción ni cierre.

### Figura 10.6 — Cerrar, abrir y comprobar persistencia

1. Selecciona **0 Volver** para regresar al menú principal.
2. Selecciona **0 Salir** y espera el mensaje de guardado de ambos archivos.
3. Vuelve a ejecutar `java -cp bin Main`.
4. Selecciona **1 Quebradas**.

Captura el listado con las mismas tres quebradas después de reiniciar. En el menú principal se debe indicar la cantidad cargada desde los archivos (RNF-03).

## Ubicación de la implementación de Quebradas

| Comportamiento que se muestra | Archivo y método |
|---|---|
| Menú y listado que aparecen en las capturas | `src/quebrada/MenuQuebradas.java` — `dibujarPantalla`, `mostrarListado` |
| Crear y rechazar duplicados | `src/quebrada/GestorQuebradas.java` — `registrar` |
| Eliminar compactando el arreglo | `src/quebrada/GestorQuebradas.java` — `eliminar` |
| Pila LIFO y recuperación / pila vacía | `src/quebrada/PilaDeshacerQuebradas.java` y `GestorQuebradas.deshacerEliminacion` |
| Leer y guardar el archivo | `src/quebrada/PersistenciaQuebradas.java` — `cargar`, `guardar` |
| Inicio, menú integrado y cierre | `src/Main.java` — `main` |

Prueba automatizada equivalente, que usa un archivo temporal y no modifica `datos/`: `java -cp bin PruebaEscenariosQuebrada`.

## Escenarios de Resultados

Los escenarios de Resultados se mantienen aparte porque son otra entidad. Se ejecutan desde el menú principal con la opción **2**. Pasos detallados en el documento de checklist y en `src/PruebaEscenarios.java`; su prueba automatizada es `java -cp bin PruebaEscenarios`.
