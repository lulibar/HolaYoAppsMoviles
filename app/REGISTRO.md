# Registro de laboratorios 1 y 2 - HolaYo

**Materia:** Aplicaciones Móviles
**Alumna:** Lourdes Barrientos
**Proyecto:** HolaYo (`com.example.holayo`)

---


##  Laboratorio 1 - El taller y el primer proyecto

###  Dónde vive cada cosa en el proyecto

| Qué | Dónde está |
|---|---|
| El código de la primera pantalla | `app → kotlin+java → com.example.holayo → MainActivity.kt` |
| La estructura de la pantalla (donde estaba declarado el "Hello World!") | `app → res → layout → activity_main.xml` |
| Los textos de la app | `app → res → values → strings.xml` |
| El registro de la Activity ante el sistema (con `intent-filter` MAIN + LAUNCHER) | `app → manifests → AndroidManifest.xml` |
| La decisión de `minSdk` y las dependencias | `Gradle Scripts → build.gradle.kts (Module :app)`, línea `minSdk = 26` |

###  La app HolaYo

- Muestra un saludo con mi nombre y un dato mío ("Estoy cursando Aplicaciones Móviles").
- El modelo de datos es una `data class Perfil(nombre, dato, apodo: String?)`. 
- El botón "Saludar distinto" alterna el saludo entre "Hola, soy ..." y "¡Buenas! Acá ..." en cada toque, mediante una lambda en `setOnClickListener`.
- Los textos fijos viven en `strings.xml`, no en el código ni en el layout, para que después la app se pueda traducir sin tocar código.
- Es el modelo tradicional: la estructura se declara en XML, el código encuentra las vistas con `findViewById` y las muta (`tvSaludo.text = ...`).

Verificado en el **emulador** y en mi **teléfono físico**: muestra mis datos y el botón responde.

### El APK

- **Ruta:** `C:\Users\lulii\AndroidStudioProjects\HolaYo\app\build\outputs\apk\debug\app-debug.apk`
- **Peso:** `5,55 MB (5.823.921 bytes)`

**¿Por qué pesa varios megabytes si la app tiene dos textos y un botón?** Porque adentro del APK viajan las bibliotecas de compatibilidad, los recursos para todas las variantes de pantalla y el empaquetado universal, no solo el código que escribí.

**Sobre la firma:** para instalarse en el teléfono, el paquete tuvo que ir firmado. No firmé nada a mano: Android Studio lo firmó con una clave de debug generada automáticamente. Sirve para desarrollar pero no para publicar. Esa firma no acredita quién soy, y la firma de verdad, la identidad criptográfica del producto, llega con el trabajo práctico.

---

## Laboratorio 2 - Una pantalla que vive y muere

###  La segunda pantalla

- Creé `SegundaActivity` con su layout `activity_segunda.xml`. 
- Desde `MainActivity`, el botón "Ir a la sala de experimentos" arma un **Intent explícito** (`Intent(this, SegundaActivity::class.java)`, que nombra al destino por su clase) y le agrega un extra: `putExtra("nombre", perfil.apodo ?: perfil.nombre)`.
- No creé ni mostré la pantalla yo misma: **le pedí al sistema** que lo hiciera. El Intent es un mensaje al sistema, y es el sistema quien crea la Activity, la apila sobre la actual y la pone en escena.
- `SegundaActivity` recibe el dato con `intent.getStringExtra("nombre") ?: "misterioso visitante"`. El `?:` es necesario porque el extra podría no venir (por ejemplo, si alguien activara esa pantalla sin el dato), y Kotlin obliga a definir el plan B: es la nulabilidad del Laboratorio 1, ahora protegiendo la comunicación entre componentes.
- El botón "atrás" devuelve a la primera pantalla tal como la dejé: uso de la pila de retroceso.

###  El ciclo de vida instrumentado (Logcat)

Ambas Activities escriben en Logcat, con la etiqueta `VIDA`, cada aviso del ciclo de vida: `onCreate`, `onStart`, `onResume`, `onPause`, `onStop` y `onDestroy`. Filtré Logcat con `tag:VIDA`.

**Al abrir la app:** `Main → onCreate`, `Main → onStart`, `Main → onResume`. Es la secuencia de nacimiento completa.

**Ir de la primera a la segunda pantalla:**

```
Main → onPause
Segunda → onCreate
Segunda → onStart
Segunda → onResume
Main → onStop
```

La primera pantalla se **pausa** antes de que la segunda exista, pero no se **detiene** (`onStop`) hasta que la segunda ya está en escena e interactiva. El sistema nunca deja un hueco: siempre hay una pantalla lista antes de retirar del todo a la anterior.

**Volver atrás:**

```
Segunda → onPause
Main → onStart
Main → onResume
Segunda → onStop
Segunda → onDestroy
```

La primera **revive** (no hay `onCreate`: estaba detenida, no muerta) y la segunda **se destruye**. En la pila de retroceso, salir de una pantalla es morir.

### 3.3 La tabla del experimentador

Con la segunda pantalla abierta, limpié Logcat entre acción y acción. Escribí mi predicción **antes** de ejecutar cada acción.

| # | Acción | Mi predicción (antes de ejecutar) | Lo observado                                             | Por qué |
|---|---|---|----------------------------------------------------------|---|
| 1 | Apretar **Home** (la app queda en fondo) | `Creo que no va a pasar nada en el log, porque la app sigue abierta, solo que no la veo. Como mucho capaz aparece algo de 'pause'` | `Segunda → onPause`, `Segunda → onStop`                  | Al fondo la Activity queda detenida, no destruida: el proceso sigue vivo. |
| 2 | **Volver** a la app desde recientes | `Supongo que va a aparecer onCreate de nuevo, porque para mí es como si la abriera de cero otra vez.` | `Segunda → onStart`, `Segunda → onResume`, sin `onCreate` | Revivir no es renacer: la Activity estaba detenida, no muerta. |
| 3 | **Apagar la pantalla** con el botón de encendido | `Me parece que va a pasar lo mismo que con Home, porque la app deja de estar visible igual.` | `Segunda → onPause`, `Segunda → onStop`                  | Para la Activity, la pantalla apagada y estar en fondo son casi lo mismo: no se la ve. |
| 4 | **Encenderla** y desbloquear | `Debería volver a mostrar onResume, sin pasar por onCreate, porque la app nunca se cerró, solo estaba la pantalla apagada.` | `Segunda → onStart`, `Segunda → onResume`                | El mismo par start/resume que al volver de Home. |
| 5 | Abrir la **cortina de notificaciones** completa y cerrarla | `No tengo muy claro qué va a pasar acá. Mi primera idea es que no debería aparecer nada porque la app sigue viéndose atrás, pero por ahí cuenta como que me tapa algo y hace pause."` | Ningún aviso                                             | La cortina no siempre cuenta como "taparte" para la Activity; el comportamiento fino varía entre versiones y fabricantes. |
| 6 | Recibir algo **encima** (abrir otra app desde una notificación) y volver | `Creo que va a ser parecido a ir a la segunda pantalla: se pausa, se detiene, y cuando vuelvo se reanuda. Como si la otra app fuera 'la segunda pantalla' por un momento.` | `onPause` → `onStop` → `onStart` → `onResume`            | Ciclo completo de ser tapada y descubierta. |


### 3.4 Lectura de conjunto

1. **Detenerse no es morir.** Ninguna de las seis acciones produjo un `onDestroy`: la Activity fue pausada y detenida, pero siguió existiendo, con todas sus variables.
2. **Mi código no decidió nada de lo que pasó.** El usuario y el sistema movieron todos los hilos y mi Activity solo fue notificada. 

### 3.5 El giro fatal

Agregué a `SegundaActivity` un contador (`private var contador = 0`, con un botón "Sumar") y un `EditText` de notas con `android:id`. Sumé hasta 7 y escribí una frase en las notas. Al rotar el dispositivo, Logcat mostró:

```
Segunda → onPause
Segunda → onStop
Segunda → onDestroy
Segunda → onCreate
Segunda → onStart
Segunda → onResume
```

**Resultado:** el sistema destruyó la Activity y creó otra (mismo nombre de clase, objeto nuevo, vida nueva). El **contador volvió a 0** y la **frase de las notas siguió ahí**.

- **Por qué se perdió el contador:** era una variable de la instancia destruida. La instancia nueva nació con el valor inicial. El giro se llevó el 7.
- **Por qué sobrevivió la frase:** antes de destruir por un cambio de configuración, el sistema guarda automáticamente el estado de las **vistas que tienen `android:id`** y se lo repone a la instancia nueva. El `EditText` tiene id, por eso su texto se conservó. Sin id, el sistema no puede asociar el estado guardado a la vista.

### 3.6 La muerte por memoria, provocada

Con la app en la segunda pantalla (contador sumado y frase escrita), verifiqué el PID del proceso con `adb`. Como tenía el emulador y el teléfono conectados a la vez, usé el flag `-d` para apuntar al dispositivo físico:

```
& $adb -d shell pidof com.example.holayo
```

Después apreté **Home** (la app queda en fondo, proceso vivo) y la maté como lo haría el sistema por presión de memoria:

```
& $adb -d shell am kill com.example.holayo
```

Verifiqué con `pidof` que el proceso ya no existía, y volví a la app **desde recientes**.

| Dato | Valor |
|---|---|
| PID antes del kill | `10571` |
| PID después de volver desde recientes | `13274` |

Los PID son distintos: el proceso murió y nació uno nuevo.

### 3.7 El depurador

Puse un breakpoint en `SegundaActivity.kt`, en la línea `contador++` (dentro del listener del botón "Sumar"), y ejecuté con **Debug** (el ícono del bichito), no con Play. Al tocar "Sumar" en la app, la ejecución se detuvo en esa línea y el teléfono quedó congelado.

### 3.8 Logcat vs. depurador

- **Logcat** muestra que pasa. Sirve para entender el comportamiento.
- El **depurador** muestra que está pasando excatamente. Sirve para agarrar el error que no entiendo.


### 3.9 La pregunta que el laboratorio deja abierta

Vi dónde muere el estado: en las variables de una pantalla que el sistema destruye cuando quiere (el giro y la muerte por memoria se llevaron el contador). La pregunta es dónde debería vivir el estado de una pantalla para que ni el giro ni la memoria se lo lleven, sin tener que guardarlo a mano, valor por valor. 

