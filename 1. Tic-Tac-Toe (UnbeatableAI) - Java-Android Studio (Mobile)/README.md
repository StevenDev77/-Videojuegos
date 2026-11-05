# Tic-Tac-Toe Android

Migración fiel del proyecto C# WinForms `WinAppTicTacToe` a Java/Android.

## Abrir en Android Studio

1. `File > Open...`
2. Seleccionar esta carpeta (`TicTacToeAndroid`).
3. Sincronizar Gradle y ejecutar el módulo `app`.

## Documentación de la migración

Ver [`MIGRACION.md`](MIGRACION.md) para:

- mapa de archivos C# → Java/Android,
- qué se conservó y qué se adaptó,
- diferencias C#/Java,
- rarezas del original conservadas intencionalmente,
- resultados de compilación y pruebas.

## Build por consola

```bash
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

Requiere JDK 17+ y Android SDK 35. Si compilas por consola sin Android
Studio, define `ANDROID_HOME` o crea `local.properties` con:

```properties
sdk.dir=/ruta/a/tu/Android/Sdk
```


Si solo quieres entrar a jugar (sin tocar código):

./iniciar_emulador.sh

Si modificas algo en el código y quieres probar los cambios:


./instalar_y_jugar.sh



