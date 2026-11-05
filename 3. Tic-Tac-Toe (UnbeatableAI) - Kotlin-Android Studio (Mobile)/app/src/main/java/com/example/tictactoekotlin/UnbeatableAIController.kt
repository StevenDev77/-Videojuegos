package com.example.tictactoekotlin

import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import java.util.Random

/**
 * Controlador de Inteligencia Artificial del nivel "UnbeatableAI".
 *
 * Es la migración directa de UnbeatableAIController.cs (WinForms). Se conservan
 * los nombres de los campos (mCurrentPlayerFlag, mMoveCount, mCurrentGameState,
 * mBoardState, mHeuristicSum, mArrayMoves), las constantes, las propiedades
 * (Flag, MoveNumber, GameControl, ArrayMoves), los métodos y el orden exacto de
 * las comprobaciones del método heurístico numérico.
 *
 * Codificación de mArrayMoves:
 *   0 = casilla vacía, 1 = jugada del usuario, 3 = jugada del NPC.
 * Con ella se obtienen las sumas 6 (victoria de la IA), 2 (bloqueo) y 5
 * (defensa de diagonales) que usa el algoritmo original.
 */
class UnbeatableAIController {

    // =========================================================================
    // 1. Miembros de la clase (atributos)
    // =========================================================================

    private var mCurrentPlayerFlag: Player = Player.User
    private var mMoveCount: Int = 0
    private var mCurrentGameState: GameState = GameState.Active
    private var mBoardState: Array<Player> = Array(9) { Player.None }
    private var mHeuristicSum: Int = 0
    private var mArrayMoves: IntArray = IntArray(9)

    // Índices del tablero:
    //  [0] [1] [2]
    //  [3] [4] [5]
    //  [6] [7] [8]
    private val CENTER_INDEX = 4
    private val CORNER_INDICES = intArrayOf(0, 2, 6, 8)
    private val SIDE_INDICES = intArrayOf(1, 3, 5, 7)

    // Las 8 líneas ganadoras: 3 filas, 3 columnas y 2 diagonales.
    private val WinningLines: Array<IntArray> = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
    )

    // =========================================================================
    // 2. Propiedades (equivalentes a las propiedades de C#)
    // =========================================================================

    var Flag: String
        get() = if (mCurrentPlayerFlag == Player.User) "X" else "O"
        set(value) {
            mCurrentPlayerFlag = if (value == "X") Player.User else Player.NPC
        }

    var MoveNumber: Int
        get() = mMoveCount
        set(value) {
            mMoveCount = value
        }

    var GameControl: GameState
        get() = mCurrentGameState
        set(value) {
            mCurrentGameState = value
        }

    var ArrayMoves: Array<Player>
        get() = mBoardState
        set(value) {
            mBoardState = value
        }

    // =========================================================================
    // 3. Constructor
    // =========================================================================

    init {
        InitializeGameData()
    }

    // =========================================================================
    // 4. Métodos de control del juego
    // =========================================================================

    /**
     * Inicializa/reinicia todos los datos del juego a su estado inicial.
     */
    fun InitializeGameData() {
        mArrayMoves = IntArray(9)
        for (i in mArrayMoves.indices) {
            mArrayMoves[i] = 0
        }

        mBoardState = Array(9) { Player.None }
        mCurrentPlayerFlag = Player.User // El usuario (X) empieza
        mMoveCount = 0
        mCurrentGameState = GameState.Active
        mHeuristicSum = 0
    }

    /**
     * Limpia el texto de todos los botones del tablero y lo habilita.
     */
    fun InitializeControls(listButtons: List<Button>, grbTicTacToe: ViewGroup) {
        for (button in listButtons) {
            button.text = ""
        }
        EnableControls(grbTicTacToe)
    }

    /**
     * Deshabilita el tablero para impedir clics posteriores.
     */
    fun DisableControls(grbTicTacToe: ViewGroup) {
        setEnabledRecursive(grbTicTacToe, false)
    }

    /**
     * Habilita todos los controles del tablero.
     */
    fun EnableControls(grbTicTacToe: ViewGroup) {
        setEnabledRecursive(grbTicTacToe, true)
    }

    private fun setEnabledRecursive(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                setEnabledRecursive(view.getChildAt(i), enabled)
            }
        }
    }

    /**
     * Comprueba si hay ganador o empate y actualiza el estado de la partida.
     */
    fun CheckWinner(listButtons: List<Button>, grbTicTacToe: ViewGroup): GameState {
        for (line in WinningLines) {
            val cell1 = mBoardState[line[0]]
            val cell2 = mBoardState[line[1]]
            val cell3 = mBoardState[line[2]]

            if (cell1 != Player.None && cell1 == cell2 && cell2 == cell3) {
                AlertDialog.Builder(grbTicTacToe.context)
                        .setTitle("Resultado")
                        .setMessage("¡Ganó ${FlagForPlayer(cell1)}!")
                        .setPositiveButton("OK", null)
                        .show()
                DisableControls(grbTicTacToe)
                return GameState.GameOver
            }
        }

        if (mMoveCount == 9) {
            AlertDialog.Builder(grbTicTacToe.context)
                    .setTitle("Resultado")
                    .setMessage("¡Empate!")
                    .setPositiveButton("OK", null)
                    .show()
            DisableControls(grbTicTacToe)
            return GameState.GameOver
        }

        return GameState.Active
    }

    /**
     * Convierte el enum Player al símbolo visible ("X" u "O").
     */
    private fun FlagForPlayer(player: Player): String {
        return if (player == Player.User) "X" else "O"
    }

    // =========================================================================
    // 5. Gestión de jugadas
    // =========================================================================

    /**
     * Refleja la jugada del usuario en la interfaz y en el estado lógico.
     */
    private fun ShowUserMove(objectButton: Button) {
        objectButton.text = FlagForPlayer(Player.User)

        val index = objectButton.tag.toString().toInt()

        mBoardState[index] = Player.User
        mMoveCount++
        mCurrentPlayerFlag = Player.NPC
        mArrayMoves[index] = 1
    }

    /**
     * Permite al usuario realizar una jugada.
     */
    fun PlayUser(objectButton: Button, listButtons: List<Button>, grbTicTacToe: ViewGroup): Boolean {
        if (objectButton.text.toString() == "" && mCurrentGameState == GameState.Active) {
            if (mCurrentPlayerFlag == Player.User) {
                ShowUserMove(objectButton)
                mCurrentGameState = CheckWinner(listButtons, grbTicTacToe)
                return true
            }
        }
        return false
    }

    /**
     * Genera una jugada aleatoria en una casilla vacía.
     */
    private fun GenerateRandomNPCMove(ListButtons: List<Button>): Int {
        var index: Int
        val RandomNumber = Random()

        do {
            index = RandomNumber.nextInt(9)
            if (mArrayMoves[index] == 0 && index < mArrayMoves.size) {
                return index
            }
        } while (true)
    }

    /**
     * Refleja la jugada del NPC en la interfaz y en el estado lógico.
     */
    private fun ShowNPCMove(listButtons: List<Button>, index: Int) {
        listButtons[index].text = FlagForPlayer(Player.NPC)
        mBoardState[index] = Player.NPC
        mMoveCount++
        mCurrentPlayerFlag = Player.User
        mArrayMoves[index] = 3
    }

    /**
     * Método heurístico numérico: prioridad 1 victoria (suma 6), prioridad 2
     * bloqueo (suma 2), prioridad 3 defensa de diagonales (suma 5), prioridad 4
     * control de bordes laterales y respaldo aleatorio.
     */
    private fun HeuristicNumericalMethod(ListButtons: List<Button>): Int {
        var index = 0

        // ---------------------------------------------------------------------
        // PASO 1: la IA puede ganar inmediatamente (dos 3 + un 0 = 6)
        // ---------------------------------------------------------------------
        if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 6) {
            if (mArrayMoves[0] == 0) index = 0
            else if (mArrayMoves[1] == 0) index = 1
            else if (mArrayMoves[2] == 0) index = 2
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2]
            return index
        } else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 6) {
            if (mArrayMoves[3] == 0) index = 3
            else if (mArrayMoves[4] == 0) index = 4
            else if (mArrayMoves[5] == 0) index = 5
            mHeuristicSum = mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5]
            return index
        } else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 6) {
            if (mArrayMoves[6] == 0) index = 6
            else if (mArrayMoves[7] == 0) index = 7
            else if (mArrayMoves[8] == 0) index = 8
            mHeuristicSum = mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8]
            return index
        } else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 6) {
            if (mArrayMoves[0] == 0) index = 0
            else if (mArrayMoves[3] == 0) index = 3
            else if (mArrayMoves[6] == 0) index = 6
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6]
            return index
        } else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 6) {
            if (mArrayMoves[1] == 0) index = 1
            else if (mArrayMoves[4] == 0) index = 4
            else if (mArrayMoves[7] == 0) index = 7
            mHeuristicSum = mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7]
            return index
        } else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 6) {
            if (mArrayMoves[2] == 0) index = 2
            else if (mArrayMoves[5] == 0) index = 5
            else if (mArrayMoves[8] == 0) index = 8
            mHeuristicSum = mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8]
            return index
        } else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 6) {
            if (mArrayMoves[0] == 0) index = 0
            else if (mArrayMoves[4] == 0) index = 4
            else if (mArrayMoves[8] == 0) index = 8
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8]
            return index
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 6) {
            if (mArrayMoves[2] == 0) index = 2
            else if (mArrayMoves[4] == 0) index = 4
            else if (mArrayMoves[6] == 0) index = 6
            mHeuristicSum = mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6]
            return index
        }

        // ---------------------------------------------------------------------
        // PASO 2: la IA debe bloquear al usuario (dos 1 + un 0 = 2)
        // ---------------------------------------------------------------------
        else if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 2) {
            if (mArrayMoves[0] == 0) index = 0
            else if (mArrayMoves[1] == 0) index = 1
            else if (mArrayMoves[2] == 0) index = 2
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2]
            return index
        } else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 2) {
            if (mArrayMoves[3] == 0) index = 3
            else if (mArrayMoves[4] == 0) index = 4
            else if (mArrayMoves[5] == 0) index = 5
            mHeuristicSum = mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5]
            return index
        } else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 2) {
            if (mArrayMoves[6] == 0) index = 6
            else if (mArrayMoves[7] == 0) index = 7
            else if (mArrayMoves[8] == 0) index = 8
            mHeuristicSum = mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8]
            return index
        } else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 2) {
            if (mArrayMoves[0] == 0) index = 0
            else if (mArrayMoves[3] == 0) index = 3
            else if (mArrayMoves[6] == 0) index = 6
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6]
            return index
        } else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 2) {
            if (mArrayMoves[1] == 0) index = 1
            else if (mArrayMoves[4] == 0) index = 4
            else if (mArrayMoves[7] == 0) index = 7
            mHeuristicSum = mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7]
            return index
        } else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 2) {
            if (mArrayMoves[2] == 0) index = 2
            else if (mArrayMoves[5] == 0) index = 5
            else if (mArrayMoves[8] == 0) index = 8
            mHeuristicSum = mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8]
            return index
        } else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 2) {
            if (mArrayMoves[0] == 0) index = 0
            else if (mArrayMoves[4] == 0) index = 4
            else if (mArrayMoves[8] == 0) index = 8
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8]
            return index
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 2) {
            if (mArrayMoves[2] == 0) index = 2
            else if (mArrayMoves[4] == 0) index = 4
            else if (mArrayMoves[6] == 0) index = 6
            mHeuristicSum = mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6]
            return index
        }

        // ---------------------------------------------------------------------
        // PASO 3: defensa contra bifurcaciones en diagonales (suma 5)
        // ---------------------------------------------------------------------
        else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 5) {
            if (mArrayMoves[2] == 0) index = 2
            else if (mArrayMoves[6] == 0) index = 6
            else index = GenerateRandomNPCMove(ListButtons)
            mHeuristicSum = 5
            return index
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 5) {
            if (mArrayMoves[0] == 0) index = 0
            else if (mArrayMoves[8] == 0) index = 8
            else index = GenerateRandomNPCMove(ListButtons)
            mHeuristicSum = 5
            return index
        }

        // ---------------------------------------------------------------------
        // PASO 4: control de bordes laterales (el usuario ocupa dos lados)
        // ---------------------------------------------------------------------
        else if ((mArrayMoves[1] + mArrayMoves[3] == 2) && (mArrayMoves[0] == 0)) {
            mHeuristicSum = 2
            index = CORNER_INDICES[0]
            return index
        } else if ((mArrayMoves[1] + mArrayMoves[5] == 2) && (mArrayMoves[2] == 0)) {
            mHeuristicSum = 2
            index = CORNER_INDICES[1]
            return index
        } else if ((mArrayMoves[3] + mArrayMoves[7] == 2) && (mArrayMoves[6] == 0)) {
            mHeuristicSum = 2
            index = CORNER_INDICES[2]
            return index
        } else if ((mArrayMoves[5] + mArrayMoves[7] == 2) && (mArrayMoves[8] == 0)) {
            mHeuristicSum = 2
            index = CORNER_INDICES[3]
            return index
        }

        // ---------------------------------------------------------------------
        // PASO 5: jugada aleatoria por defecto
        // ---------------------------------------------------------------------
        else {
            index = GenerateRandomNPCMove(ListButtons)
            return index
        }
    }

    /**
     * Orquesta el turno completo del NPC conservando las mismas ramas del
     * original: primer movimiento (centro o esquina) y movimientos posteriores
     * (centro, heurístico y caso especial de suma 5 con centro ocupado por la IA).
     */
    fun PlayNPC(ListButtons: List<Button>, grbTicTacToe: ViewGroup) {
        var index: Int
        val RandomNumber = Random()

        if (mCurrentGameState == GameState.Active) {
            if (MoveNumber == 1) {
                if (mArrayMoves[4] == 0) {
                    index = CENTER_INDEX
                    ShowNPCMove(ListButtons, index)
                    mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe)
                    return
                }

                do {
                    index = RandomNumber.nextInt(9)
                    if (mArrayMoves[index] == 0 &&
                            (index == CORNER_INDICES[0] || index == CORNER_INDICES[1] ||
                                    index == CORNER_INDICES[2] || index == CORNER_INDICES[3])) {
                        ShowNPCMove(ListButtons, index)
                        mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe)
                        return
                    }
                } while (true)
            } else {
                if (mArrayMoves[4] == 0) {
                    index = CENTER_INDEX
                    ShowNPCMove(ListButtons, index)
                    mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe)
                    return
                } else {
                    index = HeuristicNumericalMethod(ListButtons)

                    if (mArrayMoves[4] == 3 && mHeuristicSum == 5) {
                        do {
                            index = RandomNumber.nextInt(9)
                            // Se corrige el índice repetido del original (SIDE_INDICES[1])
                            // para garantizar que el bucle siempre encuentre una casilla libre.
                            if (mArrayMoves[index] == 0 &&
                                    (index == SIDE_INDICES[0] || index == SIDE_INDICES[1] ||
                                            index == SIDE_INDICES[2] || index == SIDE_INDICES[3])) {
                                ShowNPCMove(ListButtons, index)
                                mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe)
                                return
                            }
                        } while (true)
                    }

                    ShowNPCMove(ListButtons, index)
                    mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe)
                    return
                }
            }
        } else {
            return
        }
    }
}
