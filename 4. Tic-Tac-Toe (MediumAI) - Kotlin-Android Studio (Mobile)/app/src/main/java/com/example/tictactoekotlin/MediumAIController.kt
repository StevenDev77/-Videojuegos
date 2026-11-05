package com.example.tictactoekotlin

import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * Controlador de Inteligencia Artificial de nivel medio.
 *
 * Es la migración directa de MediumAIController.cs (WinForms). Se conservan
 * los nombres de los campos (mCurrentPlayerFlag, mMoveCount, mCurrentGameState,
 * mBoardState, mArrayMoves), las propiedades, los métodos y el orden exacto de
 * las dieciséis comprobaciones del método heurístico numérico.
 *
 * Codificación de mArrayMoves:
 *   0 = casilla vacía, 1 = jugada del usuario, 3 = jugada del NPC.
 */
class MediumAIController {

    // =========================================================================
    // Miembros de la clase (atributos)
    // =========================================================================

    private var mCurrentPlayerFlag: Player = Player.User
    private var mMoveCount: Int = 0
    private var mCurrentGameState: GameState = GameState.Active
    private var mBoardState: Array<Player> = Array(9) { Player.None }
    private var mArrayMoves: IntArray = IntArray(9)
    private var mRandomNumber: RandomGroup = RandomGroup()

    private val CENTER_INDEX = 4

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
    // Propiedades (equivalentes a las propiedades de C#)
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

    var RandomGroup: RandomGroup
        get() = mRandomNumber
        set(value) {
            mRandomNumber = value
        }

    // =========================================================================
    // Constructor
    // =========================================================================

    init {
        InitializeGameData()
    }

    // =========================================================================
    // Métodos de control del juego
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
        mRandomNumber = RandomGroup()
    }

    /**
     * Limpia el texto de los botones del tablero y lo habilita.
     */
    fun InitializeControls(listButtons: List<Button>, grbTicTacToe: ViewGroup) {
        for (button in listButtons) {
            button.text = ""
        }
        EnableControls(grbTicTacToe)
    }

    /**
     * Deshabilita el tablero para que no se acepten más clics.
     */
    fun DisableControls(grbTicTacToe: ViewGroup) {
        setEnabledRecursive(grbTicTacToe, false)
    }

    /**
     * Habilita el tablero.
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

    private fun FlagForPlayer(player: Player): String {
        return if (player == Player.User) "X" else "O"
    }

    // =========================================================================
    // Gestión de jugadas
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

        do {
            index = RandomGroup.getRandomNumber(0, 8)
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
     * Método heurístico numérico: primero busca ganar (suma 6) y luego bloquear
     * (suma 2). Conserva el orden original de las comprobaciones y el respaldo
     * aleatorio.
     */
    private fun HeuristicNumericalMethod(ListButtons: List<Button>): Int {
        var index = 0
        if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[1] == 0) {
                index = 1
            } else if (mArrayMoves[2] == 0) {
                index = 2
            }
            return index
        } else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 6) {
            if (mArrayMoves[3] == 0) {
                index = 3
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[5] == 0) {
                index = 5
            }
            return index
        } else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 6) {
            if (mArrayMoves[6] == 0) {
                index = 6
            } else if (mArrayMoves[7] == 0) {
                index = 7
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[3] == 0) {
                index = 3
            } else if (mArrayMoves[6] == 0) {
                index = 6
            }
            return index
        } else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 6) {
            if (mArrayMoves[1] == 0) {
                index = 1
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[7] == 0) {
                index = 7
            }
            return index
        } else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 6) {
            if (mArrayMoves[2] == 0) {
                index = 2
            } else if (mArrayMoves[5] == 0) {
                index = 5
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 6) {
            if (mArrayMoves[2] == 0) {
                index = 2
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[6] == 0) {
                index = 6
            }
            return index
        } else if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[1] == 0) {
                index = 1
            } else if (mArrayMoves[2] == 0) {
                index = 2
            }
            return index
        } else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 2) {
            if (mArrayMoves[3] == 0) {
                index = 3
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[5] == 0) {
                index = 5
            }
            return index
        } else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 2) {
            if (mArrayMoves[6] == 0) {
                index = 6
            } else if (mArrayMoves[7] == 0) {
                index = 7
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[3] == 0) {
                index = 3
            } else if (mArrayMoves[6] == 0) {
                index = 6
            }
            return index
        } else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 2) {
            if (mArrayMoves[1] == 0) {
                index = 1
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[7] == 0) {
                index = 7
            }
            return index
        } else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 2) {
            if (mArrayMoves[2] == 0) {
                index = 2
            } else if (mArrayMoves[5] == 0) {
                index = 5
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 2) {
            if (mArrayMoves[2] == 0) {
                index = 2
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[6] == 0) {
                index = 6
            }
            return index
        } else {
            index = GenerateRandomNPCMove(ListButtons)
            return index
        }
    }

    /**
     * Orquesta el turno del NPC: ocupa el centro si está libre y, en caso
     * contrario, usa el método heurístico.
     */
    fun PlayNPC(ListButtons: List<Button>, grbTicTacToe: ViewGroup) {
        var index: Int

        if (mCurrentGameState == GameState.Active) {
            if (mArrayMoves[4] == 0) {
                index = CENTER_INDEX
                ShowNPCMove(ListButtons, index)
                mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe)
                return
            } else {
                index = HeuristicNumericalMethod(ListButtons)
                ShowNPCMove(ListButtons, index)
                mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe)
                return
            }
        } else {
            return
        }
    }
}
