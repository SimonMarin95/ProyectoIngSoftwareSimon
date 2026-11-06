# Calidad del agua — Quebrada La Iguaná (RedRío / SIATA)

Proyecto académico universitario — **Grupo 8**  
Medellín, Antioquia, Colombia

## Integrantes

| Integrante | Entidad | Estado en este repositorio |
|---|---|---|
| **Simón Marín de los Ríos** | Resultado de calidad | Implementado |
| Willin Stewen Gutiérrez Santos | Quebrada | Pendiente de integrar |

## ¿Qué hace este programa?

Aplicación de **consola en Java** alineada a los **mockups de la sección 9** (Figuras 9.1 y 9.3): cabecera con HU/CU/RF, caja `MENSAJE`, campos con nombres del diccionario de datos y opciones Guardar / Buscar / Listar / Corregir / Eliminar / Deshacer / Volver.

Permite gestionar y comparar resultados de calidad del agua publicados por **RedRío** (quebrada **La Iguaná**). **No se conecta automáticamente** a RedRío ni al SIATA.

## Requisitos

- **Java JDK 17** o superior (probado con Eclipse Temurin 17)
- Consola / terminal (PowerShell, CMD o terminal del IDE)

Verificar instalación:

```bash
javac -version
java -version
```

## Estructura del proyecto

```
SimonProyect/
├── src/
│   ├── Main.java
│   ├── integracion/
│   │   ├── BuscadorQuebrada.java          ← contrato con el módulo de Willin
│   │   └── AdaptadorQuebradaTemporal.java ← stub temporal para pruebas
│   └── resultado/
│       ├── ResultadoCalidad.java
│       ├── ValidadorResultado.java
│       ├── PilaDeshacer.java
│       ├── PersistenciaResultados.java
│       ├── GestorResultados.java
│       ├── ComparadorCalidad.java
│       ├── UtilidadesConsola.java
│       └── MenuResultados.java
├── datos/
│   ├── resultados.txt                    ← persistencia real del programa
│   └── resultados_demostracion_ejemplo.txt
├── docs/
│   ├── ESCENARIOS_PRUEBA.md
│   ├── INTEGRACION_QUEBRADA.md
│   └── TABLA_FUNCIONALIDADES.md
├── bin/                                  ← .class (se genera al compilar)
├── README.md
└── .gitignore
```

## Compilar y ejecutar

Desde la carpeta raíz del proyecto (`SimonProyect`):

### Windows (PowerShell)

```powershell
javac -encoding UTF-8 -d bin src\Main.java src\integracion\*.java src\resultado\*.java
java -cp bin Main
```

Verificación automática de los 6 escenarios (opcional, archivo de apoyo):

```powershell
javac -encoding UTF-8 -d bin src\PruebaEscenarios.java src\Main.java src\integracion\*.java src\resultado\*.java
java -cp bin PruebaEscenarios
```

### Alternativa con rutas absolutas al JDK

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.21.9-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
javac -encoding UTF-8 -d bin src\Main.java src\integracion\*.java src\resultado\*.java
java -cp bin Main
```

## Funcionalidades (Figura 9.3)

1. Guardar (RF-28 a RF-34)  
2. Buscar (RF-35)  
3. Listar + promedio ICA (RF-38)  
4. Corregir valor_ica / categoria_ica (RF-36)  
5. Eliminar sin huecos (RF-37)  
6. Deshacer (RF-39)  
0. Volver al menú principal  

Consultas adicionales (HU-34): 7 comparar puntos · 8 temporadas · 9 evolución · 10 datos de demostración  

Checklist fuera del código (capturas, predicción, bitácora IA): `docs/CHECKLIST_ENTREGA_DOCENTE.md` 

## Restricciones técnicas respetadas

**No se utilizan:**

- `ArrayList`, `LinkedList`, `HashSet`, `TreeSet`
- Frameworks ni bibliotecas externas de colecciones

**Sí se utilizan (implementación manual):**

- Arreglos tradicionales de objetos + contador
- Pila propia (`PilaDeshacer`) con arreglo y contador
- Desplazamiento de elementos al eliminar
- Control de capacidad
- Persistencia en archivo de texto (`datos/resultados.txt`)

## Datos de demostración

La opción **11** del menú carga datos ilustrativos basados en el diagnóstico del documento:

- Parte alta: categoría **aceptable**
- Parte media: categoría **regular**
- Parte baja: **mala** / **muy mala**

Los **valores numéricos del ICA y las fechas exactas son de prueba**.  
No deben presentarse ante el docente como mediciones oficiales de RedRío.

Fuentes de referencia del proyecto:

- RedRío: https://www.metropol.gov.co/ambiental/recurso-hidrico/Paginas/aguas-superficiales/red-rio.aspx  
- SIATA: https://geoportal.siata.gov.co/

## Pruebas y capturas (sección 10.4)

Instrucciones paso a paso: [`docs/ESCENARIOS_PRUEBA.md`](docs/ESCENARIOS_PRUEBA.md)

Tabla de ubicación del código: [`docs/TABLA_FUNCIONALIDADES.md`](docs/TABLA_FUNCIONALIDADES.md)

## Integración con Quebrada

Ver: [`docs/INTEGRACION_QUEBRADA.md`](docs/INTEGRACION_QUEBRADA.md)

Contrato requerido:

```java
public interface BuscadorQuebrada {
    boolean existeQuebrada(String nombreQuebrada);
}
```

## Subir a GitHub

```powershell
cd "C:\Users\Web Develop\Desktop\SimonProyect"

git init
git add src datos docs README.md .gitignore
git commit -m "Módulo Resultado de calidad — Simón Marín de los Ríos"

# Crear el repositorio vacío en GitHub (en la web) y luego:
git branch -M main
git remote add origin https://github.com/USUARIO/NOMBRE-REPO.git
git push -u origin main
```

> No se incluye un enlace de repositorio inventado. Debe crear el repo en su cuenta de GitHub y reemplazar `USUARIO/NOMBRE-REPO`.

## Notas para la sustentación

- Explique el **arreglo + contador** en `GestorResultados`.
- Explique el **desplazamiento** al eliminar (no quedan huecos).
- Explique la **pila LIFO** en `PilaDeshacer`.
- Explique por qué el orden de categorías **no es alfabético** (`ValidadorResultado.ordenCategoria`).
- Explique la validación de quebrada vía `BuscadorQuebrada` (RF-32).
