# Integración con el módulo de Quebrada (Willin)

## Estado actual

El módulo de **Resultado de calidad** (Simón) está funcional de forma independiente.  
La existencia de una quebrada se valida con la interfaz `BuscadorQuebrada`.

Mientras el código de Willin no está unido, se usa:

- `integracion.AdaptadorQuebradaTemporal`  
  Contiene solo nombres de demostración (`La Iguaná`, `La Presidenta`, `Santa Elena`).  
  **No es** el CRUD de Quebrada.

## Qué método necesita Resultado de calidad

| Elemento | Detalle |
|---|---|
| Interfaz | `integracion.BuscadorQuebrada` |
| Método | `boolean existeQuebrada(String nombreQuebrada)` |
| Parámetro | Nombre exacto de la quebrada (ej. `"La Iguaná"`) |
| Retorno | `true` si existe; `false` si no existe o el nombre está vacío |
| Requisito | RF-32 · CA-17.6 |

## Cómo se usa hoy

En `Main.java` se crea el adaptador temporal y se inyecta en el gestor:

```java
AdaptadorQuebradaTemporal adaptadorTemporal = new AdaptadorQuebradaTemporal();
BuscadorQuebrada buscadorQuebrada = adaptadorTemporal;
GestorResultados gestor = new GestorResultados(100, rutaDatos, buscadorQuebrada);
```

En `GestorResultados.registrar(...)` se llama:

```java
if (!buscadorQuebrada.existeQuebrada(nombreQuebrada)) {
    // rechaza el registro
}
```

## Cómo integrar cuando Willin entregue su módulo

### Opción recomendada (adaptador)

1. Willin expone un método de búsqueda, por ejemplo:  
   `public boolean buscarPorNombre(String nombre)` en su `GestorQuebradas`.
2. Crear una clase `AdaptadorQuebradaWillin` que implemente `BuscadorQuebrada` y delegue:

```java
package integracion;

public class AdaptadorQuebradaWillin implements BuscadorQuebrada {
    private GestorQuebradas gestorQuebradas; // clase de Willin

    public AdaptadorQuebradaWillin(GestorQuebradas gestorQuebradas) {
        this.gestorQuebradas = gestorQuebradas;
    }

    @Override
    public boolean existeQuebrada(String nombreQuebrada) {
        return gestorQuebradas.buscarPorNombre(nombreQuebrada); // ajustar al nombre real
    }
}
```

3. En `Main.java`, reemplazar el adaptador temporal por el de Willin.
4. Unificar el menú principal: opción 1 = Resultados, opción 2 = Quebradas.

### Archivos a modificar al unir

| Archivo | Cambio |
|---|---|
| `src/Main.java` | Instanciar el gestor de Willin y el nuevo adaptador |
| `src/integracion/AdaptadorQuebradaWillin.java` | Crear (nuevo) |
| Menú principal | Activar la opción real de Quebrada |
| (Opcional) | Eliminar o dejar de usar `AdaptadorQuebradaTemporal` |

### Archivos que NO se deben duplicar

- No reimplementar el CRUD de Quebrada dentro del paquete `resultado`.
- No copiar la pila/archivo de Willin dentro de este módulo.

## Prueba de integración sugerida

1. En el módulo de Willin, crear la quebrada `La Iguaná`.
2. En Resultados, registrar un ICA para `La Iguaná` → debe aceptar.
3. Intentar registrar en `Quebrada Inexistente` → debe rechazar con mensaje RF-32.

## Importante para el documento

No afirmar que la integración está completa hasta probar ambos módulos juntos.  
Hoy el contrato está listo; la unión real depende del código de Willin.
