package com.example.tictactoekotlin

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private val board = CharArray(9) { EMPTY }
    private val cells = arrayOfNulls<Button>(9)
    private val ai = MediumAI()
    private lateinit var status: TextView
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createInterface()
        resetGame()
    }

    private fun createInterface() {
        val padding = dp(24)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(padding, padding, padding, padding)
            setBackgroundColor(Color.rgb(248, 249, 252))
        }

        val title = TextView(this).apply {
            text = "Tic-Tac-Toe"
            setTextColor(Color.rgb(32, 42, 68))
            textSize = 30f
            gravity = Gravity.CENTER
        }
        root.addView(
            title,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        status = TextView(this).apply {
            setTextColor(Color.rgb(74, 84, 105))
            textSize = 18f
            gravity = Gravity.CENTER
        }
        root.addView(status, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(64)
        ))

        val grid = GridLayout(this).apply {
            columnCount = 3
            rowCount = 3
        }
        root.addView(grid, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        for (i in cells.indices) {
            val cell = Button(this).apply {
                textSize = 38f
                setTextColor(Color.rgb(42, 72, 150))
                isAllCaps = false
                setOnClickListener { playHumanMove(i) }
            }
            val cellParams = GridLayout.LayoutParams(
                GridLayout.spec(i / 3, 1, 1f),
                GridLayout.spec(i % 3, 1, 1f)
            ).apply {
                width = 0
                height = 0
                setMargins(dp(4), dp(4), dp(4), dp(4))
            }
            grid.addView(cell, cellParams)
            cells[i] = cell
        }

        val restart = Button(this).apply {
            text = "Nuevo juego"
            isAllCaps = false
            setOnClickListener { resetGame() }
        }
        root.addView(restart, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(16)
        })

        setContentView(root)
    }

    private fun playHumanMove(index: Int) {
        if (gameOver || board[index] != EMPTY) return
        ai.PlayUser(index)
        if (ai.getArrayMoves()[index] == 1) {
            makeMove(index, 'X')
        }
        if (finishIfNeeded()) return

        val aiMove = ai.PlayNPC()
        if (aiMove >= 0) {
            makeMove(aiMove, '0')
            if (finishIfNeeded()) return
        }
        status.text = "Tu turno (X)"
    }

    private fun makeMove(index: Int, player: Char) {
        board[index] = player
        cells[index]?.apply {
            text = player.toString()
            isEnabled = false
        }
    }

    private fun finishIfNeeded(): Boolean {
        val message = when {
            hasWon('X') -> "Ganó X"
            hasWon('0') -> "Ganó 0"
            board.none { it == EMPTY } -> "Empate"
            else -> null
        } ?: return false

        status.text = message
        gameOver = true
        return true
    }

    private fun hasWon(player: Char): Boolean = WINNING_LINES.any { line ->
        board[line[0]] == player && board[line[1]] == player && board[line[2]] == player
    }

    private fun resetGame() {
        board.fill(EMPTY)
        cells.forEach { cell ->
            cell?.apply {
                text = ""
                isEnabled = true
            }
        }
        gameOver = false
        ai.InitializeData()
        status.text = "Tu turno (X) · La IA juega con 0"
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    private companion object {
        const val EMPTY = ' '
        val WINNING_LINES = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )
    }
}
