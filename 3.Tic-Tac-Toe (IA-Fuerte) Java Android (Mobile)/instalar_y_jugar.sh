#!/usr/bin/env bash
# Script para instalar y abrir la app en el emulador
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SDK_DIR="$HOME/.cache/opencode-android-sdk"
export ANDROID_HOME="$SDK_DIR"
export PATH="$SDK_DIR/platform-tools:$PATH"

APK_PATH="$DIR/app/build/outputs/apk/debug/app-debug.apk"

if [ ! -f "$APK_PATH" ]; then
    echo "Compilando APK..."
    (cd "$DIR" && ./gradlew assembleDebug --no-daemon)
fi

echo "Esperando que el emulador esté encendido..."
adb wait-for-device

echo "Esperando a que Android termine de iniciar (boot completed)..."
while [ "$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" != "1" ]; do
    sleep 1
done

echo "Instalando la app Tic-Tac-Toe..."
adb install -r "$APK_PATH"

echo "Abriendo el juego..."
adb shell am start -n com.example.winapptictactoe/.frmTicTacToe

echo ""
echo "============================================="
echo "  ¡Juego iniciado en el emulador con éxito!  "
echo "============================================="

