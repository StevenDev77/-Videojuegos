package com.example.winapptictactoe;

/**
 * Enumeración que representa a los participantes del juego y las celdas del tablero.
 *
 * Valores y ordinales:
 *   - None = 0: Representa una casilla vacía en el tablero.
 *   - User = 1: Representa al jugador humano (utiliza el símbolo "X").
 *   - NPC  = 2: Representa a la inteligencia artificial / máquina (utiliza el símbolo "O").
 */
public enum Player {
    None, // Casilla vacía (sin jugada)
    User, // Jugador humano (X)
    NPC   // Jugador controlado por la máquina (O)
}
