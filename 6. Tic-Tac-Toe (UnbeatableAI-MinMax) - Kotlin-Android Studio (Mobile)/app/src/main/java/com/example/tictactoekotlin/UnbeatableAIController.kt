package com.example.tictactoekotlin

/**
 * Controlador de Inteligencia Artificial basado en el algoritmo Minimax.
 *
 * Es la migración directa de UnbeatableAIController.cs del proyecto
 * Tic-Tac-Toe-Version-MiniMax. Se conservan los nombres de las constantes
 * (AI_PLAYER, HUMAN_PLAYER, EMPTY) y de los métodos (GetBestMove, Minimax,
 * EvaluateBoard, IsMovesLeft), el puntaje 10 / -10 y el ajuste por
 * profundidad. El tablero de C# char[3,3] se representa como Array<CharArray>.
 */
class UnbeatableAIController {

    private val AI_PLAYER = 'O'
    private val HUMAN_PLAYER = 'X'
    private val EMPTY = ' '

    /**
     * Recorre todas las casillas libres, simula la jugada de la IA y elige la
     * de mayor puntaje devuelto por Minimax.
     */
    fun GetBestMove(board: Array<CharArray>): Int {
        var bestScore = Int.MIN_VALUE
        var bestMove = -1

        for (i in 0 until 9) {
            val row = i / 3
            val col = i % 3

            if (board[row][col] == EMPTY) {
                board[row][col] = AI_PLAYER
                val score = Minimax(board, 0, false)
                board[row][col] = EMPTY

                if (score > bestScore) {
                    bestScore = score
                    bestMove = i
                }
            }
        }

        return bestMove
    }

    /**
     * Algoritmo Minimax con ajuste por profundidad (10 - depth / -10 + depth).
     */
    private fun Minimax(board: Array<CharArray>, depth: Int, isMaximizing: Boolean): Int {
        val score = EvaluateBoard(board)

        if (score == 10) return score - depth
        if (score == -10) return score + depth
        if (!IsMovesLeft(board)) return 0

        if (isMaximizing) {
            var best = Int.MIN_VALUE
            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = AI_PLAYER
                        best = Math.max(best, Minimax(board, depth + 1, false))
                        board[i][j] = EMPTY
                    }
                }
            }
            return best
        } else {
            var best = Int.MAX_VALUE
            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = HUMAN_PLAYER
                        best = Math.min(best, Minimax(board, depth + 1, true))
                        board[i][j] = EMPTY
                    }
                }
            }
            return best
        }
    }

    /**
     * Evalúa el tablero: 10 si gana la IA, -10 si gana el humano, 0 en otro caso.
     */
    private fun EvaluateBoard(board: Array<CharArray>): Int {
        // Filas
        for (row in 0 until 3) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == AI_PLAYER) return 10
                if (board[row][0] == HUMAN_PLAYER) return -10
            }
        }

        // Columnas
        for (col in 0 until 3) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == AI_PLAYER) return 10
                if (board[0][col] == HUMAN_PLAYER) return -10
            }
        }

        // Diagonal principal
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == AI_PLAYER) return 10
            if (board[0][0] == HUMAN_PLAYER) return -10
        }

        // Diagonal secundaria
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == AI_PLAYER) return 10
            if (board[0][2] == HUMAN_PLAYER) return -10
        }

        return 0
    }

    /**
     * Indica si todavía quedan casillas vacías.
     */
    private fun IsMovesLeft(board: Array<CharArray>): Boolean {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (board[i][j] == EMPTY) return true
            }
        }
        return false
    }
}
