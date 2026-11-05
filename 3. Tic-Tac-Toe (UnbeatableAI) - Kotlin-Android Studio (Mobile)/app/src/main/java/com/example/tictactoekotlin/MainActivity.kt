package com.example.tictactoekotlin

import android.app.Activity
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
 * Pantalla principal del juego Tic-Tac-Toe (nivel UnbeatableAI).
 *
 * Equivale a frmTicTacToe.cs del proyecto WinForms. Se conservan los nombres
 * de los campos (ListButtons, UnbeatableAI, ControlPlayer, btnTicTacToe1..9,
 * btnNew, btnExit) y de los manejadores de eventos. La interfaz se construye
 * por código porque el proyecto Android no incluye un archivo de layout.
 */
class MainActivity : Activity() {

    private val ListButtons: MutableList<Button> = ArrayList<Button>()
    private val UnbeatableAI = UnbeatableAIController()
    private var ControlPlayer: Boolean = false

    private lateinit var btnNew: Button
    private lateinit var btnExit: Button
    private lateinit var btnTicTacToe1: Button
    private lateinit var btnTicTacToe2: Button
    private lateinit var btnTicTacToe3: Button
    private lateinit var btnTicTacToe4: Button
    private lateinit var btnTicTacToe5: Button
    private lateinit var btnTicTacToe6: Button
    private lateinit var btnTicTacToe7: Button
    private lateinit var btnTicTacToe8: Button
    private lateinit var btnTicTacToe9: Button

    private lateinit var grbTicTacToe: ViewGroup
    private lateinit var lblInformation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InitializeComponent()
        InsertDataList()
        frmTicTacToe_Load()
    }

    /**
     * Construye la interfaz: título, etiqueta de estado, tablero 3x3 y botones
     * de Nuevo/Salir. Cada casilla guarda su índice en el tag (0 a 8).
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
        grbTicTacToe = grid
        root.addView(grid, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        btnTicTacToe1 = createCell(0)
        btnTicTacToe2 = createCell(1)
        btnTicTacToe3 = createCell(2)
        btnTicTacToe4 = createCell(3)
        btnTicTacToe5 = createCell(4)
        btnTicTacToe6 = createCell(5)
        btnTicTacToe7 = createCell(6)
        btnTicTacToe8 = createCell(7)
        btnTicTacToe9 = createCell(8)

        val options = LinearLayout(this)
        options.orientation = LinearLayout.HORIZONTAL
        options.gravity = Gravity.CENTER
        root.addView(options, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        btnNew = Button(this)
        btnNew.text = "Nuevo"
        btnNew.isAllCaps = false
        options.addView(btnNew, LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        btnExit = Button(this)
        btnExit.text = "Salir"
        btnExit.isAllCaps = false
        options.addView(btnExit, LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        btnTicTacToe1.setOnClickListener { btnTicTacToe1_Click(it) }
        btnTicTacToe2.setOnClickListener { btnTicTacToe2_Click(it) }
        btnTicTacToe3.setOnClickListener { btnTicTacToe3_Click(it) }
        btnTicTacToe4.setOnClickListener { btnTicTacToe4_Click(it) }
        btnTicTacToe5.setOnClickListener { btnTicTacToe5_Click(it) }
        btnTicTacToe6.setOnClickListener { btnTicTacToe6_Click(it) }
        btnTicTacToe7.setOnClickListener { btnTicTacToe7_Click(it) }
        btnTicTacToe8.setOnClickListener { btnTicTacToe8_Click(it) }
        btnTicTacToe9.setOnClickListener { btnTicTacToe9_Click(it) }

        btnNew.setOnClickListener { btnNew_Click(it) }
        btnExit.setOnClickListener { btnExit_Click(it) }

        setContentView(root)
    }

    private fun createCell(index: Int): Button {
        val cell = Button(this)
        cell.textSize = 38f
        cell.setTextColor(Color.rgb(42, 72, 150))
        cell.isAllCaps = false
        cell.tag = index.toString()

        val params = GridLayout.LayoutParams(
                GridLayout.spec(index / 3, 1, 1f),
                GridLayout.spec(index % 3, 1, 1f))
        params.width = 0
        params.height = 0
        params.setMargins(dp(3), dp(3), dp(3), dp(3))
        (grbTicTacToe as GridLayout).addView(cell, params)
        return cell
    }

    private fun InsertDataList() {
        ListButtons.add(btnTicTacToe1) // nodo 0
        ListButtons.add(btnTicTacToe2) // nodo 1
        ListButtons.add(btnTicTacToe3) // nodo 2
        ListButtons.add(btnTicTacToe4) // nodo 3
        ListButtons.add(btnTicTacToe5) // nodo 4
        ListButtons.add(btnTicTacToe6) // nodo 5
        ListButtons.add(btnTicTacToe7) // nodo 6
        ListButtons.add(btnTicTacToe8) // nodo 7
        ListButtons.add(btnTicTacToe9) // nodo 8
    }

    private fun InitializeGame() {
        UnbeatableAI.InitializeGameData()
        UnbeatableAI.InitializeControls(ListButtons, grbTicTacToe)
        lblInformation.text = "Turno de X"
    }

    private fun frmTicTacToe_Load() {
        InitializeGame()
    }

    private fun playCell(button: Button, view: View) {
        ControlPlayer = UnbeatableAI.PlayUser(button, ListButtons, grbTicTacToe)
        if (ControlPlayer == true) {
            UnbeatableAI.PlayNPC(ListButtons, grbTicTacToe)
            if (UnbeatableAI.GameControl == GameState.Active) {
                lblInformation.text = "Turno de X"
            }
        }
    }

    private fun btnTicTacToe1_Click(view: View) = playCell(btnTicTacToe1, view)
    private fun btnTicTacToe2_Click(view: View) = playCell(btnTicTacToe2, view)
    private fun btnTicTacToe3_Click(view: View) = playCell(btnTicTacToe3, view)
    private fun btnTicTacToe4_Click(view: View) = playCell(btnTicTacToe4, view)
    private fun btnTicTacToe5_Click(view: View) = playCell(btnTicTacToe5, view)
    private fun btnTicTacToe6_Click(view: View) = playCell(btnTicTacToe6, view)
    private fun btnTicTacToe7_Click(view: View) = playCell(btnTicTacToe7, view)
    private fun btnTicTacToe8_Click(view: View) = playCell(btnTicTacToe8, view)
    private fun btnTicTacToe9_Click(view: View) = playCell(btnTicTacToe9, view)

    private fun btnNew_Click(view: View) {
        InitializeGame()
    }

    private fun btnExit_Click(view: View) {
        finish()
    }

    private fun dp(value: Int): Int =
            Math.round(value * resources.displayMetrics.density)
}
