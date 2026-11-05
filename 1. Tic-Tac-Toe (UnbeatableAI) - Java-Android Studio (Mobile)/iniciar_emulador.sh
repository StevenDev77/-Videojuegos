#!/usr/bin/env bash
# Script para iniciar el emulador de Android (TicTacToe)
set -e

SDK_DIR="$HOME/.cache/opencode-android-sdk"
export ANDROID_HOME="$SDK_DIR"
export ANDROID_AVD_HOME="$HOME/.config/.android/avd"
export PATH="$SDK_DIR/emulator:$SDK_DIR/platform-tools:$PATH"

echo "============================================="
echo "  Iniciando Emulador Android (TicTacToe)     "
echo "  Imagen limpia AOSP (Android 14 x86_64)     "
echo "============================================="

emulator -avd TicTacToe -accel on -gpu auto

