package com.example.tictactoekotlin

class UnbeatableAI {
    fun getBestMove(board: CharArray): Int {
        require(board.size == 9) { "The board must contain exactly 9 cells." }

        var bestScore = Int.MIN_VALUE
        var bestMove = -1
        for (i in board.indices) {
            if (board[i] == EMPTY) {
                board[i] = AI_PLAYER
                val score = minimax(board, 0, maximizing = false)
                board[i] = EMPTY
                if (score > bestScore) {
                    bestScore = score
                    bestMove = i
                }
            }
        }
        return bestMove
    }

    private fun minimax(board: CharArray, depth: Int, maximizing: Boolean): Int {
        when (winner(board)) {
            AI_PLAYER -> return 10 - depth
            HUMAN_PLAYER -> return depth - 10
        }
        if (board.none { it == EMPTY }) return 0

        var bestScore = if (maximizing) Int.MIN_VALUE else Int.MAX_VALUE
        for (i in board.indices) {
            if (board[i] == EMPTY) {
                board[i] = if (maximizing) AI_PLAYER else HUMAN_PLAYER
                val score = minimax(board, depth + 1, maximizing = !maximizing)
                board[i] = EMPTY
                bestScore = if (maximizing) maxOf(bestScore, score) else minOf(bestScore, score)
            }
        }
        return bestScore
    }

    private fun winner(board: CharArray): Char {
        for (line in WINNING_LINES) {
            val mark = board[line[0]]
            if (mark != EMPTY && mark == board[line[1]] && mark == board[line[2]]) {
                return mark
            }
        }
        return EMPTY
    }

    private companion object {
        const val AI_PLAYER = 'O'
        const val HUMAN_PLAYER = 'X'
        const val EMPTY = ' '
        val WINNING_LINES = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )
    }
}
