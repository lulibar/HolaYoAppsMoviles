# HolaYo - Laboratorios 1 y 2 (Aplicaciones Móviles)

Este repositorio contiene la resolución de los **Laboratorios 1 y 2** de la materia Aplicaciones Móviles, entregados juntos sobre el mismo proyecto: el Laboratorio 2 se construye encima del Laboratorio 1.

## Cómo ver cada laboratorio por separado

Como el proyecto es continuo, cada laboratorio quedó marcado en el historial de Git para poder revisarlos de forma independiente:

- **Laboratorio 1:** tag [`laboratorio-1`](../../tree/laboratorio-1). Corresponde al proyecto HolaYo recién armado: una sola pantalla (`MainActivity`) con el saludo, el dato y el botón que alterna el mensaje.
- **Laboratorio 2:** rama `main` (estado final del repositorio). Agrega `SegundaActivity`, la navegación con Intent explícito, la instrumentación del ciclo de vida en Logcat, el experimento de rotación y la muerte del proceso con `adb`, y el uso del depurador.

Para abrir el proyecto tal como estaba al terminar el Laboratorio 1, se puede:

1. Entrar a la pestaña **"Tags"** de este repositorio en GitHub y elegir `laboratorio-1`, o
2. Clonar el repositorio y ejecutar `git checkout laboratorio-1`.

El detalle completo de ambos laboratorios (qué se hizo, qué se observó, y las conclusiones de cada experimento) está documentado en [`REGISTRO.md`](./REGISTRO.md).

## Resumen de la app

HolaYo es una carta de presentación digital con dos pantallas:

- **Pantalla principal:** muestra un saludo y un dato personal, con un botón que alterna el estilo del saludo.
- **Sala de experimentos** (segunda pantalla, a la que se llega con un botón): recibe el nombre por un Intent explícito, tiene un contador y un campo de notas, y sirve para observar en vivo el ciclo de vida de una Activity (pausa, detención, recreación por rotación, muerte del proceso y recuperación).


Aclaracion sobre carpeta *apk lab 1*, quedo desactualizado el nombre.
