package com.example.tictactoejava;

import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * Controlador de Inteligencia Artificial de nivel medio para Tic-Tac-Toe.
 *
 * Es la migración directa de MediumAIController.cs (WinForms). Se conservan
 * los nombres de los campos, las constantes, los métodos y el orden exacto de
 * las comprobaciones del método heurístico numérico. La única diferencia es la
 * adaptación de los tipos de interfaz (Button/ViewGroup de Android en lugar de
 * Button/GroupBox de WinForms) y el uso de RandomGroup para generar números
 * aleatorios en el mismo rango 0..8 que Random.Next(0, 9).
 */
public class MediumAIController {

    // =========================================================================
    // Miembros de la clase (atributos)
    // =========================================================================

    private Player mCurrentPlayerFlag;   // Turno actual ("X" o "O")
    private int mMoveCount;              // Número de jugadas realizadas
    private GameState mCurrentGameState; // Partida activa o terminada
    private Player[] mBoardState;        // Tablero lógico con el enum Player
    private int[] mArrayMoves;           // Tablero numérico: 0 vacío, 1 usuario, 3 NPC
    private RandomGroup mRandomNumber;   // Generador aleatorio auxiliar

    private static final int CENTER_INDEX = 4;

    private static final int[][] WinningLines = new int[][] {
            // Filas
            new int[] { 0, 1, 2 },
            new int[] { 3, 4, 5 },
            new int[] { 6, 7, 8 },
            // Columnas
            new int[] { 0, 3, 6 },
            new int[] { 1, 4, 7 },
            new int[] { 2, 5, 8 },
            // Diagonales
            new int[] { 0, 4, 8 },
            new int[] { 2, 4, 6 }
    };

    // =========================================================================
    // Propiedades (getters y setters)
    // =========================================================================

    public String getFlag() {
        return mCurrentPlayerFlag == Player.User ? "X" : "O";
    }

    public void setFlag(String value) {
        mCurrentPlayerFlag = "X".equals(value) ? Player.User : Player.NPC;
    }

    public int getMoveNumber() {
        return mMoveCount;
    }

    public void setMoveNumber(int value) {
        mMoveCount = value;
    }

    public GameState getGameControl() {
        return mCurrentGameState;
    }

    public void setGameControl(GameState value) {
        mCurrentGameState = value;
    }

    public Player[] getArrayMoves() {
        return mBoardState;
    }

    public void setArrayMoves(Player[] value) {
        mBoardState = value;
    }

    public RandomGroup getRandomGroup() {
        return mRandomNumber;
    }

    public void setRandomGroup(RandomGroup value) {
        mRandomNumber = value;
    }

    // =========================================================================
    // Constructor
    // =========================================================================

    public MediumAIController() {
        InitializeGameData();
    }

    // =========================================================================
    // Métodos de control del juego
    // =========================================================================

    /**
     * Inicializa/reinicia todos los datos del juego a su estado inicial.
     */
    public void InitializeGameData() {
        mArrayMoves = new int[9];
        for (int i = 0; i < mArrayMoves.length; i++) {
            mArrayMoves[i] = 0;
        }

        mBoardState = new Player[9];
        for (int i = 0; i < mBoardState.length; i++) {
            mBoardState[i] = Player.None;
        }

        mCurrentPlayerFlag = Player.User; // El usuario (X) empieza
        mMoveCount = 0;
        mCurrentGameState = GameState.Active;
        mRandomNumber = new RandomGroup();
    }

    /**
     * Limpia el texto de los botones del tablero y lo habilita.
     */
    public void InitializeControls(List<Button> listButtons, ViewGroup grbTicTacToe) {
        for (Button button : listButtons) {
            button.setText("");
        }
        EnableControls(grbTicTacToe);
    }

    /**
     * Deshabilita el tablero para que no se acepten más clics.
     */
    public void DisableControls(ViewGroup grbTicTacToe) {
        setEnabledRecursive(grbTicTacToe, false);
    }

    /**
     * Habilita el tablero.
     */
    public void EnableControls(ViewGroup grbTicTacToe) {
        setEnabledRecursive(grbTicTacToe, true);
    }

    private void setEnabledRecursive(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                setEnabledRecursive(group.getChildAt(i), enabled);
            }
        }
    }

    /**
     * Comprueba si hay ganador o empate y actualiza el estado de la partida.
     */
    public GameState CheckWinner(List<Button> listButtons, ViewGroup grbTicTacToe) {
        for (int[] line : WinningLines) {
            Player cell1 = mBoardState[line[0]];
            Player cell2 = mBoardState[line[1]];
            Player cell3 = mBoardState[line[2]];

            if (cell1 != Player.None && cell1 == cell2 && cell2 == cell3) {
                new AlertDialog.Builder(grbTicTacToe.getContext())
                        .setTitle("Resultado")
                        .setMessage("¡Ganó " + FlagForPlayer(cell1) + "!")
                        .setPositiveButton("OK", null)
                        .show();
                DisableControls(grbTicTacToe);
                return GameState.GameOver;
            }
        }

        if (mMoveCount == 9) {
            new AlertDialog.Builder(grbTicTacToe.getContext())
                    .setTitle("Resultado")
                    .setMessage("¡Empate!")
                    .setPositiveButton("OK", null)
                    .show();
            DisableControls(grbTicTacToe);
            return GameState.GameOver;
        }

        return GameState.Active;
    }

    private String FlagForPlayer(Player player) {
        return player == Player.User ? "X" : "O";
    }

    // =========================================================================
    // Gestión de jugadas
    // =========================================================================

    /**
     * Refleja la jugada del usuario en la interfaz y en el estado lógico.
     */
    private void ShowUserMove(Button objectButton) {
        objectButton.setText(FlagForPlayer(Player.User));

        int index = Integer.parseInt(objectButton.getTag().toString());

        mBoardState[index] = Player.User;
        mMoveCount++;
        mCurrentPlayerFlag = Player.NPC;
        mArrayMoves[index] = 1;
    }

    /**
     * Permite al usuario realizar una jugada.
     */
    public boolean PlayUser(Button objectButton, List<Button> listButtons, ViewGroup grbTicTacToe) {
        if (objectButton.getText().toString().equals("")
                && mCurrentGameState == GameState.Active) {
            if (mCurrentPlayerFlag == Player.User) {
                ShowUserMove(objectButton);
                mCurrentGameState = CheckWinner(listButtons, grbTicTacToe);
                return true;
            }
        }
        return false;
    }

    /**
     * Genera una jugada aleatoria en una casilla vacía.
     */
    private int GenerateRandomNPCMove(List<Button> ListButtons) {
        int index = 0;

        do {
            index = getRandomGroup().getRandomNumber(0, 8);
            if (mArrayMoves[index] == 0 && index < mArrayMoves.length) {
                return index;
            }
        } while (true);
    }

    /**
     * Refleja la jugada del NPC en la interfaz y en el estado lógico.
     */
    private void ShowNPCMove(List<Button> listButtons, int index) {
        listButtons.get(index).setText(FlagForPlayer(Player.NPC));
        mBoardState[index] = Player.NPC;
        mMoveCount++;
        mCurrentPlayerFlag = Player.User;
        mArrayMoves[index] = 3;
    }

    /**
     * Método heurístico numérico: busca primero una victoria (suma 6) y luego
     * un bloqueo (suma 2). Conserva exactamente el orden original de las
     * dieciséis comprobaciones y el respaldo aleatorio.
     */
    private int HeuristicNumericalMethod(List<Button> ListButtons) {
        int index = 0;
        if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[1] == 0) {
                index = 1;
            } else if (mArrayMoves[2] == 0) {
                index = 2;
            }
            return index;
        } else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 6) {
            if (mArrayMoves[3] == 0) {
                index = 3;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[5] == 0) {
                index = 5;
            }
            return index;
        } else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 6) {
            if (mArrayMoves[6] == 0) {
                index = 6;
            } else if (mArrayMoves[7] == 0) {
                index = 7;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[3] == 0) {
                index = 3;
            } else if (mArrayMoves[6] == 0) {
                index = 6;
            }
            return index;
        } else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 6) {
            if (mArrayMoves[1] == 0) {
                index = 1;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[7] == 0) {
                index = 7;
            }
            return index;
        } else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 6) {
            if (mArrayMoves[2] == 0) {
                index = 2;
            } else if (mArrayMoves[5] == 0) {
                index = 5;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 6) {
            if (mArrayMoves[2] == 0) {
                index = 2;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[6] == 0) {
                index = 6;
            }
            return index;
        } else if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[1] == 0) {
                index = 1;
            } else if (mArrayMoves[2] == 0) {
                index = 2;
            }
            return index;
        } else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 2) {
            if (mArrayMoves[3] == 0) {
                index = 3;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[5] == 0) {
                index = 5;
            }
            return index;
        } else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 2) {
            if (mArrayMoves[6] == 0) {
                index = 6;
            } else if (mArrayMoves[7] == 0) {
                index = 7;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[3] == 0) {
                index = 3;
            } else if (mArrayMoves[6] == 0) {
                index = 6;
            }
            return index;
        } else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 2) {
            if (mArrayMoves[1] == 0) {
                index = 1;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[7] == 0) {
                index = 7;
            }
            return index;
        } else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 2) {
            if (mArrayMoves[2] == 0) {
                index = 2;
            } else if (mArrayMoves[5] == 0) {
                index = 5;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 2) {
            if (mArrayMoves[2] == 0) {
                index = 2;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[6] == 0) {
                index = 6;
            }
            return index;
        } else {
            index = GenerateRandomNPCMove(ListButtons);
            return index;
        }
    }

    /**
     * Orquesta el turno del NPC: ocupa el centro si está libre y, en caso
     * contrario, usa el método heurístico.
     */
    public void PlayNPC(List<Button> ListButtons, ViewGroup grbTicTacToe) {
        int index;

        if (mCurrentGameState == GameState.Active) {
            if (mArrayMoves[4] == 0) {
                index = CENTER_INDEX;
                ShowNPCMove(ListButtons, index);
                mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe);
                return;
            } else {
                index = HeuristicNumericalMethod(ListButtons);
                ShowNPCMove(ListButtons, index);
                mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe);
                return;
            }
        } else {
            return;
        }
    }
}
