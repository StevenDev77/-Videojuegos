# -Videojuegos

Proyectos de Tic-Tac-Toe migrados de Java Swing a Android Studio.

## Estructura

1. `1. Tic-Tac-Toe (UnbeatableAI) - Java-Android Studio (Mobile)`
2. `2. Tic-Tac-Toe (MediumAI) - Java-Android Studio (Mobile)`
3. `3. Tic-Tac-Toe (UnbeatableAI) - Kotlin-Android Studio (Mobile)`
4. `4. Tic-Tac-Toe (MediumAI) - Kotlin-Android Studio (Mobile)`
5. `5. Tic-Tac-Toe (UnbeatableAI-MinMax) - Java-Android Studio (Mobile)`
6. `6. Tic-Tac-Toe (UnbeatableAI-MinMax) - Kotlin-Android Studio (Mobile)`

Las carpetas 1 y 3 usan búsqueda óptima para evitar derrotas; las carpetas 5 y 6
documentan explícitamente la variante MinMax. Las carpetas 2 y 4 usan la
heurística MediumAI con prioridad de ataque, bloqueo y selección aleatoria.

## Requisitos

- Android Studio, JDK 17
- Android SDK 35
- API 23 o superior

## Uso

1. Abre la carpeta del proyecto en Android Studio
2. Espera sincronización de Gradle
3. Ejecuta en emulador o dispositivo Android

## Notas

- Los APKs no están versionados (están en `.gitignore`). Se generan con `./gradlew assembleDebug` dentro de cada proyecto.
- El informe completo se encuentra en [`Informe.tex`](Informe.tex).
