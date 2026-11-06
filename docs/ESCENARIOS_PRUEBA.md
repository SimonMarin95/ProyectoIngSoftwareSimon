# Escenarios de captura — Resultado de calidad (10.4 / 10.8)

Alineados al **mockup Figura 9.3** (opciones Guardar, Buscar, Listar, Corregir, Eliminar, Deshacer, Volver).

## Preparación

```powershell
cd "C:\Users\Web Develop\Desktop\SimonProyect"
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.21.9-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# Deje datos/resultados.txt vacío (solo líneas #)
javac -encoding UTF-8 -d bin src\Main.java src\integracion\*.java src\resultado\*.java
java -cp bin Main
```

En el menú principal (Figura 9.1): opción **2** (Resultados).

Use quebrada **La Iguaná**.

---

## Figura 10.7 — Guardar tres y listar

1. Opción **1 Guardar**
   - `nombre_quebrada`: La Iguaná
   - `fecha_campana`: 15/02/2024
   - `temporada`: seca
   - `nombre_punto`: parte alta
   - `valor_ica`: 0,80
   - `categoria_ica`: aceptable
2. Opción **1** otra vez → `parte media` / `0,60` / `regular` (misma fecha)
3. Opción **1** otra vez → `parte baja` / `0,40` / `mala`
4. Opción **3 Listar** → ENTER

**Captura:** listado con columnas del diccionario + `Promedio del ICA: 0,60`.

---

## Figura 10.8 — Duplicado rechazado

1. Opción **1 Guardar** con los mismos datos de `parte media` / 15/02/2024.

**Captura:** caja MENSAJE  
`(!) Ese punto ya tiene resultado en esa campaña. (RF-29 · CA-17.3)`

---

## Figura 10.9 — Eliminar el del medio sin huecos

1. Opción **5 Eliminar** → La Iguaná / 15/02/2024 / parte media
2. Opción **3 Listar**

**Captura:** solo parte alta y parte baja, seguidas, sin fila vacía.

---

## Figura 10.10 — Deshacer

1. Opción **6 Deshacer**
2. Opción **3 Listar**

**Captura:** MENSAJE de recuperación + otra vez tres filas.

---

## Figura 10.11 — Nada que deshacer

1. Opción **6 Deshacer** otra vez.

**Captura:**  
`(!) No hay nada que deshacer. (RF-39 · CA-22.2)`  
(sin excepción ni cierre).

---

## Figura 10.12 — Persistencia

1. Opción **0 Volver**
2. En el principal: **0 Salir**
3. Ejecutar otra vez `java -cp bin Main`
4. Opción **2** → **3 Listar**

**Captura:** MENSAJE de carga al iniciar + listado con los 3 resultados.

---

## Extra para sustentación (comparaciones)

Con opción **10** (demostración) o con los tres de 15/02/2024:

- **7** Comparar puntos → fecha 15/02/2024  
- **8** Comparar temporadas → parte baja, año 2024 (tras cargar demostración)  
- **9** Evolución histórica → parte baja  
