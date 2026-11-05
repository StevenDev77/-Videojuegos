package com.example.tictactoejava;

/**
 * Controlador de Inteligencia Artificial basado en el algoritmo Minimax.
 *
 * Es la migración directa de UnbeatableAIController.cs del proyecto
 * Tic-Tac-Toe-Version-MiniMax. Se conservan los nombres de las constantes
 * (AI_PLAYER, HUMAN_PLAYER, EMPTY) y de los métodos (GetBestMove, Minimax,
 * EvaluateBoard, IsMovesLeft), el puntaje 10 / -10 y el ajuste por
 * profundidad. El tablero de C# char[3,3] se representa como char[3][3].
 */
public class UnbeatableAIController {

    private static final char AI_PLAYER = 'O';
    private static final char HUMAN_PLAYER = 'X';
    private static final char EMPTY = ' ';

    /**
     * Recorre todas las casillas libres, simula la jugada de la IA y elige la
     * de mayor puntaje devuelto por Minimax.
     */
    public int GetBestMove(char[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int col = i % 3;

            if (board[row][col] == EMPTY) {
                board[row][col] = AI_PLAYER;
                int score = Minimax(board, 0, false);
                board[row][col] = EMPTY;

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        return bestMove;
    }

    /**
     * Algoritmo Minimax con poda por profundidad (10 - depth / -10 + depth).
     */
    private int Minimax(char[][] board, int depth, boolean isMaximizing) {
        int score = EvaluateBoard(board);

        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (!IsMovesLeft(board)) return 0;

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = AI_PLAYER;
                        best = Math.max(best, Minimax(board, depth + 1, false));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = HUMAN_PLAYER;
                        best = Math.min(best, Minimax(board, depth + 1, true));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        }
    }

    /**
     * Evalúa el tablero: 10 si gana la IA, -10 si gana el humano, 0 en otro caso.
     */
    private int EvaluateBoard(char[][] board) {
        // Filas
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == AI_PLAYER) return 10;
                if (board[row][0] == HUMAN_PLAYER) return -10;
            }
        }

        // Columnas
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == AI_PLAYER) return 10;
                if (board[0][col] == HUMAN_PLAYER) return -10;
            }
        }

        // Diagonal principal
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == AI_PLAYER) return 10;
            if (board[0][0] == HUMAN_PLAYER) return -10;
        }

        // Diagonal secundaria
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == AI_PLAYER) return 10;
            if (board[0][2] == HUMAN_PLAYER) return -10;
        }

        return 0;
    }

    /**
     * Indica si todavía quedan casillas vacías.
     */
    private boolean IsMovesLeft(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) return true;
            }
        }
        return false;
    }
}
