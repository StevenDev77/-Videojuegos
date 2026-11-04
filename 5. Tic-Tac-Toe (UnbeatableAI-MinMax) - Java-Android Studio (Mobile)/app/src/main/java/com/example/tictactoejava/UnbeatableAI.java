package com.example.tictactoejava;

public final class UnbeatableAI {
    private static final char AI_PLAYER = 'O';
    private static final char HUMAN_PLAYER = 'X';
    private static final char EMPTY = ' ';
    private static final int[][] WINNING_LINES = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    public int getBestMove(char[] board) {
        if (board == null || board.length != 9) {
            throw new IllegalArgumentException("The board must contain exactly 9 cells.");
        }

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == EMPTY) {
                board[i] = AI_PLAYER;
                int score = minimax(board, 0, false);
                board[i] = EMPTY;
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    private int minimax(char[] board, int depth, boolean maximizing) {
        int winner = getWinner(board);
        if (winner == AI_PLAYER) return 10 - depth;
        if (winner == HUMAN_PLAYER) return depth - 10;
        if (isBoardFull(board)) return 0;

        int bestScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == EMPTY) {
                board[i] = maximizing ? AI_PLAYER : HUMAN_PLAYER;
                int score = minimax(board, depth + 1, !maximizing);
                board[i] = EMPTY;
                bestScore = maximizing
                        ? Math.max(bestScore, score)
                        : Math.min(bestScore, score);
            }
        }
        return bestScore;
    }

    private char getWinner(char[] board) {
        for (int[] line : WINNING_LINES) {
            char mark = board[line[0]];
            if (mark != EMPTY && mark == board[line[1]] && mark == board[line[2]]) {
                return mark;
            }
        }
        return EMPTY;
    }

    private boolean isBoardFull(char[] board) {
        for (char cell : board) {
            if (cell == EMPTY) return false;
        }
        return true;
    }
}
