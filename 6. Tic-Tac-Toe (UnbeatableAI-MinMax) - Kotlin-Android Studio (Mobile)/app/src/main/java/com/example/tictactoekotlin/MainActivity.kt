package com.example.tictactoekotlin

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Pantalla principal del juego Tic-Tac-Toe (UnbeatableAI con Minimax).
 *
 * Equivale a frmTicTacToe.cs del proyecto Tic-Tac-Toe-Version-MiniMax. Se
 * conservan los nombres de los campos (buttons, board, aiController,
 * isHumanTurn, isGameOver) y de los métodos (InitializeGame, ResetBoard,
 * OnButtonClick, AIMove, MakeMove, CheckGameEnd, CheckWin, IsBoardFull,
 * btnNew_Click, btnExit_Click). El tablero char[3,3] de C# se representa como
 * Array<CharArray>.
 */
class MainActivity : Activity() {

    private val buttons = arrayOfNulls<Button>(9)
    private val board = Array(3) { CharArray(3) { ' ' } }
    private lateinit var aiController: UnbeatableAIController
    private var isHumanTurn = true
    private var isGameOver = false

    private lateinit var lblInformation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InitializeComponent()
        aiController = UnbeatableAIController()
        InitializeGame()
    }

    /**
     * Construye la interfaz: título, etiqueta de estado, tablero 3x3 y botones
     * de Nuevo/Salir.
     */
    private fun InitializeComponent() {
        val padding = dp(20)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.gravity = Gravity.CENTER
        root.setPadding(padding, padding, padding, padding)
        root.setBackgroundColor(Color.rgb(248, 249, 252))

        val title = TextView(this)
        title.text = "Tic-Tac-Toe"
        title.setTextColor(Color.rgb(32, 42, 68))
        title.textSize = 28f
        title.gravity = Gravity.CENTER
        root.addView(title, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        lblInformation = TextView(this)
        lblInformation.text = "Turno de X"
        lblInformation.setTextColor(Color.rgb(74, 84, 105))
        lblInformation.textSize = 16f
        lblInformation.gravity = Gravity.CENTER
        root.addView(lblInformation, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(48)))

        val grid = GridLayout(this)
        grid.columnCount = 3
        grid.rowCount = 3
        root.addView(grid, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        for (i in 0 until 9) {
            val cell = Button(this)
            cell.textSize = 38f
            cell.setTextColor(Color.rgb(42, 72, 150))
            cell.isAllCaps = false

            val params = GridLayout.LayoutParams(
                    GridLayout.spec(i / 3, 1, 1f),
                    GridLayout.spec(i % 3, 1, 1f))
            params.width = 0
            params.height = 0
            params.setMargins(dp(3), dp(3), dp(3), dp(3))
            grid.addView(cell, params)
            buttons[i] = cell
        }

        val options = LinearLayout(this)
        options.orientation = LinearLayout.HORIZONTAL
        options.gravity = Gravity.CENTER
        root.addView(options, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        val btnNew = Button(this)
        btnNew.text = "Nuevo"
        btnNew.isAllCaps = false
        btnNew.setOnClickListener { btnNew_Click(it) }
        options.addView(btnNew, LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        val btnExit = Button(this)
        btnExit.text = "Salir"
        btnExit.isAllCaps = false
        btnExit.setOnClickListener { btnExit_Click(it) }
        options.addView(btnExit, LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        setContentView(root)
    }

    /**
     * Asigna los 9 botones al arreglo, registra el evento de clic y reinicia
     * el tablero.
     */
    private fun InitializeGame() {
        for (i in 0 until 9) {
            val index = i
            buttons[i]!!.setOnClickListener { OnButtonClick(index) }
        }

        ResetBoard()
    }

    /**
     * Limpia el tablero lógico y visual, y restaura los turnos.
     */
    private fun ResetBoard() {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                board[i][j] = ' '
            }
        }

        for (btn in buttons) {
            if (btn != null) {
                btn.text = ""
                btn.isEnabled = true
            }
        }

        isHumanTurn = true
        isGameOver = false
        lblInformation.text = "Turno de X"
    }

    /**
     * Atiende el clic de una casilla: jugada humana, comprobación de fin y
     * respuesta de la IA.
     */
    private fun OnButtonClick(index: Int) {
        if (isGameOver || !isHumanTurn) return

        val row = index / 3
        val col = index % 3

        if (board[row][col] == ' ') {
            MakeMove(index, 'X')

            if (CheckGameEnd()) return

            isHumanTurn = false
            AIMove()
        }
    }

    /**
     * Pide a la IA su mejor jugada y la aplica.
     */
    private fun AIMove() {
        if (isGameOver) return

        val bestMove = aiController.GetBestMove(board)
        if (bestMove != -1) {
            MakeMove(bestMove, 'O')
            CheckGameEnd()
        }

        isHumanTurn = true
    }

    /**
     * Coloca la ficha en el tablero lógico y en el botón correspondiente.
     */
    private fun MakeMove(index: Int, player: Char) {
        val row = index / 3
        val col = index % 3

        board[row][col] = player
        buttons[index]!!.text = player.toString()
        buttons[index]!!.isEnabled = false
    }

    /**
     * Comprueba el final de la partida y muestra el resultado.
     */
    private fun CheckGameEnd(): Boolean {
        if (CheckWin('X')) {
            showResult("You Win!")
            isGameOver = true
            return true
        }

        if (CheckWin('O')) {
            showResult("AI Wins!")
            isGameOver = true
            return true
        }

        if (IsBoardFull()) {
            showResult("It's a Draw!")
            isGameOver = true
            return true
        }

        return false
    }

    private fun showResult(message: String) {
        lblInformation.text = message
        AlertDialog.Builder(this)
                .setTitle("Tic Tac Toe")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
    }

    /**
     * Comprueba si el jugador indicado tiene tres en línea.
     */
    private fun CheckWin(player: Char): Boolean {
        for (i in 0 until 3) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true
        }

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true

        return false
    }

    /**
     * Indica si el tablero está lleno.
     */
    private fun IsBoardFull(): Boolean {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (board[i][j] == ' ') return false
            }
        }
        return true
    }

    private fun btnNew_Click(view: View) {
        ResetBoard()
    }

    private fun btnExit_Click(view: View) {
        finish()
    }

    private fun dp(value: Int): Int =
            Math.round(value * resources.displayMetrics.density)
}
