package com.example.winapptictactoe;

/**
 * Enumeración que representa el estado actual de la partida.
 *
 * Valores y ordinales:
 *   - GameOver = 0: Indica que la partida ha finalizado (alguien ganó o hubo empate).
 *   - Active   = 1: Indica que la partida sigue en curso y se pueden seguir haciendo jugadas.
 */
public enum GameState {
    GameOver, // Fin del juego (victoria o empate)
    Active    // Juego activo en desarrollo
}
