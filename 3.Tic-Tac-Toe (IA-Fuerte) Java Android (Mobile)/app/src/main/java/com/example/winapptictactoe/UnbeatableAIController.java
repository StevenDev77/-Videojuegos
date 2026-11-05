package com.example.winapptictactoe;

import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;
import java.util.Random;

/**
 * Controlador de Inteligencia Artificial para el juego Tic-Tac-Toe (Tres en Raya).
 *
 * Esta clase contiene toda la lógica del juego y el algoritmo de toma de decisiones
 * de la máquina (NPC). Utiliza un método numérico heurístico basado en sumas para
 * evaluar rápidamente el tablero y determinar:
 *   1. Si puede ganar en este turno (completar 3 en línea).
 *   2. Si debe bloquear al jugador humano para evitar que gane.
 *   3. Estrategias posicionales en centro, esquinas y bordes laterales.
 */
public class UnbeatableAIController {

    // =========================================================================
    // 1. ATRIBUTOS DE LA CLASE (Variables de estado)
    // =========================================================================

    /** Indica de quién es el turno actual: Player.User ("X") o Player.NPC ("O"). */
    private Player mCurrentPlayerFlag;

    /** Contador de movimientos realizados en la partida (de 0 a 9). */
    private int mMoveCount;

    /** Estado actual de la partida: GameState.Active (en curso) o GameState.GameOver (terminada). */
    private GameState mCurrentGameState;

    /** Tablero lógico representado con el enum Player (None, User o NPC). */
    private Player[] mBoardState;

    /**
     * Almacena el resultado de la suma heurística de la última línea evaluada.
     * Se usa para detectar situaciones estratégicas especiales (como la suma 5 o 2).
     */
    private int mHeuristicSum;

    /**
     * Representación numérica del tablero para el algoritmo matemático:
     *   - 0: Casilla vacía.
     *   - 1: Casilla ocupada por el Usuario humano.
     *   - 3: Casilla ocupada por la Inteligencia Artificial (NPC).
     *
     * Con estos valores:
     *   - Suma 6 (3 + 3 + 0): ¡La IA tiene 2 fichas y puede ganar!
     *   - Suma 2 (1 + 1 + 0): ¡El usuario tiene 2 fichas y la IA debe bloquearlo!
     */
    private int[] mArrayMoves;

    // Constantes de índices del tablero (0 al 8, distribuidos en una cuadrícula de 3x3):
    //  [0] [1] [2]
    //  [3] [4] [5]
    //  [6] [7] [8]

    /** Índice de la casilla central del tablero (la posición más estratégica). */
    private static final int CENTER_INDEX = 4;

    /** Índices correspondientes a las cuatro esquinas del tablero. */
    private static final int[] CORNER_INDICES = { 0, 2, 6, 8 };

    /** Índices correspondientes a los cuatro lados/bordes exteriores (en cruz). */
    private static final int[] SIDE_INDICES = { 1, 3, 5, 7 };

    /**
     * Definición de las 8 líneas posibles para ganar la partida:
     *   - 3 Filas horizontales: {0,1,2}, {3,4,5}, {6,7,8}
     *   - 3 Columnas verticales: {0,3,6}, {1,4,7}, {2,5,8}
     *   - 2 Diagonales: {0,4,8}, {2,4,6}
     */
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
    // 2. PROPIEDADES (Getters y Setters)
    // =========================================================================

    /** Obtiene el símbolo textual del turno actual ("X" u "O"). */
    public String getFlag() {
        return mCurrentPlayerFlag == Player.User ? "X" : "O";
    }

    /** Establece el jugador actual según el símbolo recibido. */
    public void setFlag(String value) {
        mCurrentPlayerFlag = "X".equals(value) ? Player.User : Player.NPC;
    }

    /** Obtiene el número total de movimientos realizados hasta el momento. */
    public int getMoveNumber() {
        return mMoveCount;
    }

    /** Establece el número de movimientos realizados. */
    public void setMoveNumber(int value) {
        mMoveCount = value;
    }

    /** Obtiene el estado actual del juego (Activo o Terminado). */
    public GameState getGameControl() {
        return mCurrentGameState;
    }

    /** Establece el estado del juego. */
    public void setGameControl(GameState value) {
        mCurrentGameState = value;
    }

    /** Obtiene el estado lógico del tablero como arreglo de Player. */
    public Player[] getArrayMoves() {
        return mBoardState;
    }

    /** Establece el estado del tablero. */
    public void setArrayMoves(Player[] value) {
        mBoardState = value;
    }

    // =========================================================================
    // 3. CONSTRUCTOR
    // =========================================================================

    /** Constructor por defecto: Inicializa los datos y estructuras para una nueva partida. */
    public UnbeatableAIController() {
        InitializeGameData();
    }

    // =========================================================================
    // 4. MÉTODOS DE CONTROL DEL JUEGO
    // =========================================================================

    /**
     * Reinicia todas las variables de datos y del tablero a su estado inicial.
     * El jugador humano (X) siempre inicia primero y el contador de jugadas empieza en 0.
     */
    public void InitializeGameData() {
        // Inicializar el arreglo numérico con ceros (todas las celdas vacías)
        mArrayMoves = new int[9];
        for (int i = 0; i < mArrayMoves.length; i++) {
            mArrayMoves[i] = 0;
        }

        // Inicializar el arreglo de estados lógicos con Player.None
        mBoardState = new Player[9];
        for (int i = 0; i < mBoardState.length; i++) {
            mBoardState[i] = Player.None;
        }

        mCurrentPlayerFlag = Player.User; // El usuario (X) inicia la partida
        mMoveCount = 0;                  // Contador en cero
        mCurrentGameState = GameState.Active; // Partida activa
        mHeuristicSum = 0;
    }

    /**
     * Limpia el texto visible de los 9 botones en la pantalla y los vuelve a habilitar.
     *
     * @param listButtons Lista con los 9 botones de la interfaz gráfica.
     * @param grbTicTacToe Contenedor visual que agrupa los botones del tablero.
     */
    public void InitializeControls(List<Button> listButtons, ViewGroup grbTicTacToe) {
        for (Button button : listButtons) {
            button.setText("");
        }
        EnableControls(grbTicTacToe);
    }

    /**
     * Deshabilita los botones del tablero para impedir que se sigan haciendo clics
     * una vez que la partida ha concluido.
     *
     * @param grbTicTacToe Contenedor de los botones del tablero.
     */
    public void DisableControls(ViewGroup grbTicTacToe) {
        setEnabledRecursive(grbTicTacToe, false);
    }

    /**
     * Habilita todos los controles del tablero para permitir la interacción del usuario.
     *
     * @param grbTicTacToe Contenedor de los botones del tablero.
     */
    public void EnableControls(ViewGroup grbTicTacToe) {
        setEnabledRecursive(grbTicTacToe, true);
    }

    /**
     * Método auxiliar recursivo para habilitar o deshabilitar una vista y todos sus elementos hijos.
     *
     * @param view Vista o contenedor a modificar.
     * @param enabled true para habilitar, false para deshabilitar.
     */
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
     * Verifica si se ha producido una victoria o un empate en el tablero.
     *
     * Recorre las 8 combinaciones ganadoras: si encuentra tres casillas iguales y no vacías,
     * muestra un diálogo con el ganador y bloquea el tablero. Si se alcanzaron 9 movimientos
     * sin ganador, declara empate.
     *
     * @param listButtons Lista de los 9 botones de la UI.
     * @param grbTicTacToe Contenedor visual del tablero.
     * @return GameState.GameOver si la partida terminó; GameState.Active si aún continúa.
     */
    public GameState CheckWinner(List<Button> listButtons, ViewGroup grbTicTacToe) {
        // 1. Revisar cada una de las 8 líneas ganadoras posibles
        for (int[] line : WinningLines) {
            Player cell1 = mBoardState[line[0]];
            Player cell2 = mBoardState[line[1]];
            Player cell3 = mBoardState[line[2]];

            // Si las tres casillas están ocupadas por el mismo jugador y no están vacías
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

        // 2. Comprobar si hay empate (tablero lleno con 9 movimientos sin ganador)
        if (mMoveCount == 9) {
            new AlertDialog.Builder(grbTicTacToe.getContext())
                    .setTitle("Resultado")
                    .setMessage("¡Empate!")
                    .setPositiveButton("OK", null)
                    .show();
            DisableControls(grbTicTacToe);
            return GameState.GameOver;
        }

        // 3. El juego sigue activo si no se cumplió ninguna de las condiciones anteriores
        return GameState.Active;
    }

    /**
     * Convierte el valor del enum Player a su símbolo visible ("X" para el usuario, "O" para la IA).
     */
    private String FlagForPlayer(Player player) {
        return player == Player.User ? "X" : "O";
    }

    // =========================================================================
    // 5. GESTIÓN DE JUGADAS (USUARIO Y MÁQUINA)
    // =========================================================================

    /**
     * Refleja en pantalla y en la memoria lógica la jugada realizada por el usuario.
     *
     * @param objectButton Botón sobre el cual el usuario hizo clic.
     */
    private void ShowUserMove(Button objectButton) {
        // Colocar la "X" en el texto del botón presionado
        objectButton.setText(FlagForPlayer(Player.User));

        // El tag del botón (0 al 8) indica la posición en la cuadrícula
        int index = Integer.parseInt(objectButton.getTag().toString());

        mBoardState[index] = Player.User; // Marcar en el tablero de estados
        mMoveCount++;                     // Incrementar el contador de jugadas
        mCurrentPlayerFlag = Player.NPC;  // Ceder el turno a la máquina
        mArrayMoves[index] = 1;           // Asignar el valor numérico 1 (jugador)
    }

    /**
     * Valida y procesa la jugada del usuario cuando presiona una casilla.
     * Solo permite la jugada si la casilla está vacía y la partida está activa.
     *
     * @param objectButton El botón presionado.
     * @param listButtons Lista de los 9 botones.
     * @param grbTicTacToe Contenedor del tablero.
     * @return true si la jugada fue válida y se realizó; false en caso contrario.
     */
    public boolean PlayUser(Button objectButton, List<Button> listButtons, ViewGroup grbTicTacToe) {
        if (objectButton.getText().toString().equals("") && mCurrentGameState == GameState.Active) {
            if (mCurrentPlayerFlag == Player.User) {
                ShowUserMove(objectButton);
                // Comprobar si el usuario ganó con esta jugada o hubo empate
                mCurrentGameState = CheckWinner(listButtons, grbTicTacToe);
                return true;
            }
        }
        return false;
    }

    /**
     * Genera una jugada aleatoria seleccionando cualquier casilla vacía disponible.
     * Se utiliza como respaldo cuando no hay ninguna regla heurística aplicable.
     *
     * @param ListButtons Lista de botones de la interfaz.
     * @return Índice de la casilla vacía elegida al azar.
     */
    private int GenerateRandomNPCMove(List<Button> ListButtons) {
        int index = 0;
        Random RandomNumber = new Random();

        // Bucle do-while que continúa generando números aleatorios hasta hallar una celda vacía (valor 0)
        do {
            index = RandomNumber.nextInt(9);
            if (mArrayMoves[index] == 0 && index < mArrayMoves.length) {
                return index;
            }
        } while (true);
    }

    /**
     * Registra y dibuja en pantalla la jugada realizada por la máquina (NPC).
     *
     * @param listButtons Lista de los 9 botones del tablero.
     * @param index Posición (0 a 8) donde la máquina coloca su ficha.
     */
    private void ShowNPCMove(List<Button> listButtons, int index) {
        listButtons.get(index).setText(FlagForPlayer(Player.NPC)); // Colocar "O" en el botón
        mBoardState[index] = Player.NPC;                          // Marcar en el tablero de estados
        mMoveCount++;                                             // Incrementar contador de jugadas
        mCurrentPlayerFlag = Player.User;                         // Regresar el turno al usuario
        mArrayMoves[index] = 3;                                   // Asignar valor numérico 3 (máquina)
    }

    /**
     * Método Heurístico Numérico: Núcleo inteligente de la toma de decisiones del NPC.
     *
     * Evalúa las 8 líneas sumando los valores numéricos de sus tres celdas:
     *   - Prioridad 1 (Suma = 6): La IA tiene dos fichas (3 + 3 + 0 = 6). ¡JUGADA GANADORA!
     *     La IA coloca su ficha en la casilla vacía restante para ganar la partida.
     *
     *   - Prioridad 2 (Suma = 2): El usuario tiene dos fichas (1 + 1 + 0 = 2). ¡JUGADA DE BLOQUEO!
     *     La IA coloca su ficha en la casilla vacía restante para impedir que el usuario gane.
     *
     *   - Prioridad 3 (Suma = 5): Situación de bifurcación o trampa en diagonales (1 + 3 + 1 = 5).
     *     La IA responde tomando una esquina o lado para neutralizar el ataque doble.
     *
     *   - Prioridad 4 (Suma = 2 en bordes): El usuario jugó en dos lados adyacentes; la IA
     *     toma la esquina correspondiente para frustrar la combinación.
     *
     *   - Respaldo: Si ninguna regla se activa, realiza una jugada aleatoria en casilla libre.
     *
     * @param ListButtons Lista de botones de la interfaz.
     * @return El índice del tablero donde la máquina debe jugar.
     */
    private int HeuristicNumericalMethod(List<Button> ListButtons) {
        int index = 0;

        // ---------------------------------------------------------------------
        // PASO 1: VERIFICAR SI LA MÁQUINA PUEDE GANAR INMEDIATAMENTE (SUMA == 6)
        // Dos '3's suman 6, lo que significa que la tercera celda vacía da la victoria.
        // ---------------------------------------------------------------------

        // Fila 1: [0, 1, 2]
        if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 6) {
            if (mArrayMoves[0] == 0) index = 0;
            else if (mArrayMoves[1] == 0) index = 1;
            else if (mArrayMoves[2] == 0) index = 2;
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2];
            return index;
        }
        // Fila 2: [3, 4, 5]
        else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 6) {
            if (mArrayMoves[3] == 0) index = 3;
            else if (mArrayMoves[4] == 0) index = 4;
            else if (mArrayMoves[5] == 0) index = 5;
            mHeuristicSum = mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5];
            return index;
        }
        // Fila 3: [6, 7, 8]
        else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 6) {
            if (mArrayMoves[6] == 0) index = 6;
            else if (mArrayMoves[7] == 0) index = 7;
            else if (mArrayMoves[8] == 0) index = 8;
            mHeuristicSum = mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8];
            return index;
        }
        // Columna 1: [0, 3, 6]
        else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 6) {
            if (mArrayMoves[0] == 0) index = 0;
            else if (mArrayMoves[3] == 0) index = 3;
            else if (mArrayMoves[6] == 0) index = 6;
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6];
            return index;
        }
        // Columna 2: [1, 4, 7]
        else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 6) {
            if (mArrayMoves[1] == 0) index = 1;
            else if (mArrayMoves[4] == 0) index = 4;
            else if (mArrayMoves[7] == 0) index = 7;
            mHeuristicSum = mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7];
            return index;
        }
        // Columna 3: [2, 5, 8]
        else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 6) {
            if (mArrayMoves[2] == 0) index = 2;
            else if (mArrayMoves[5] == 0) index = 5;
            else if (mArrayMoves[8] == 0) index = 8;
            mHeuristicSum = mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8];
            return index;
        }
        // Diagonal principal: [0, 4, 8]
        else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 6) {
            if (mArrayMoves[0] == 0) index = 0;
            else if (mArrayMoves[4] == 0) index = 4;
            else if (mArrayMoves[8] == 0) index = 8;
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8];
            return index;
        }
        // Diagonal secundaria: [2, 4, 6]
        else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 6) {
            if (mArrayMoves[2] == 0) index = 2;
            else if (mArrayMoves[4] == 0) index = 4;
            else if (mArrayMoves[6] == 0) index = 6;
            mHeuristicSum = mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6];
            return index;
        }

        // ---------------------------------------------------------------------
        // PASO 2: VERIFICAR SI DEBE BLOQUEAR AL JUGADOR HUMANO (SUMA == 2)
        // Dos '1's del usuario suman 2. ¡Si no bloqueamos esa casilla, el usuario gana!
        // ---------------------------------------------------------------------

        // Fila 1: [0, 1, 2]
        else if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 2) {
            if (mArrayMoves[0] == 0) index = 0;
            else if (mArrayMoves[1] == 0) index = 1;
            else if (mArrayMoves[2] == 0) index = 2;
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2];
            return index;
        }
        // Fila 2: [3, 4, 5]
        else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 2) {
            if (mArrayMoves[3] == 0) index = 3;
            else if (mArrayMoves[4] == 0) index = 4;
            else if (mArrayMoves[5] == 0) index = 5;
            mHeuristicSum = mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5];
            return index;
        }
        // Fila 3: [6, 7, 8]
        else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 2) {
            if (mArrayMoves[6] == 0) index = 6;
            else if (mArrayMoves[7] == 0) index = 7;
            else if (mArrayMoves[8] == 0) index = 8;
            mHeuristicSum = mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8];
            return index;
        }
        // Columna 1: [0, 3, 6]
        else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 2) {
            if (mArrayMoves[0] == 0) index = 0;
            else if (mArrayMoves[3] == 0) index = 3;
            else if (mArrayMoves[6] == 0) index = 6;
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6];
            return index;
        }
        // Columna 2: [1, 4, 7]
        else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 2) {
            if (mArrayMoves[1] == 0) index = 1;
            else if (mArrayMoves[4] == 0) index = 4;
            else if (mArrayMoves[7] == 0) index = 7;
            mHeuristicSum = mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7];
            return index;
        }
        // Columna 3: [2, 5, 8]
        else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 2) {
            if (mArrayMoves[2] == 0) index = 2;
            else if (mArrayMoves[5] == 0) index = 5;
            else if (mArrayMoves[8] == 0) index = 8;
            mHeuristicSum = mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8];
            return index;
        }
        // Diagonal principal: [0, 4, 8]
        else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 2) {
            if (mArrayMoves[0] == 0) index = 0;
            else if (mArrayMoves[4] == 0) index = 4;
            else if (mArrayMoves[8] == 0) index = 8;
            mHeuristicSum = mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8];
            return index;
        }
        // Diagonal secundaria: [2, 4, 6]
        else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 2) {
            if (mArrayMoves[2] == 0) index = 2;
            else if (mArrayMoves[4] == 0) index = 4;
            else if (mArrayMoves[6] == 0) index = 6;
            mHeuristicSum = mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6];
            return index;
        }

        // ---------------------------------------------------------------------
        // PASO 3: DEFENSAS CONTRA BIFURCACIONES / DIAGONALES (SUMA == 5)
        // Ejemplo: Usuario en esquinas opuestas (1 + 1) y la IA en el centro (3).
        // ---------------------------------------------------------------------
        else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 5) {
            if (mArrayMoves[2] == 0) index = 2;
            else if (mArrayMoves[6] == 0) index = 6;
            else index = GenerateRandomNPCMove(ListButtons);
            mHeuristicSum = 5;
            return index;
        }
        else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 5) {
            if (mArrayMoves[0] == 0) index = 0;
            else if (mArrayMoves[8] == 0) index = 8;
            else index = GenerateRandomNPCMove(ListButtons);
            mHeuristicSum = 5;
            return index;
        }

        // ---------------------------------------------------------------------
        // PASO 4: CONTROL DE BORDES LATERALES
        // Si el usuario jugó en dos bordes adyacentes, la IA ocupa la esquina entre ellos.
        // ---------------------------------------------------------------------
        else if ((mArrayMoves[1] + mArrayMoves[3] == 2) && (mArrayMoves[0] == 0)) {
            mHeuristicSum = 2;
            index = CORNER_INDICES[0]; // Esquina superior izquierda (0)
            return index;
        } else if ((mArrayMoves[1] + mArrayMoves[5] == 2) && (mArrayMoves[2] == 0)) {
            mHeuristicSum = 2;
            index = CORNER_INDICES[1]; // Esquina superior derecha (2)
            return index;
        } else if ((mArrayMoves[3] + mArrayMoves[7] == 2) && (mArrayMoves[6] == 0)) {
            mHeuristicSum = 2;
            index = CORNER_INDICES[2]; // Esquina inferior izquierda (6)
            return index;
        } else if ((mArrayMoves[5] + mArrayMoves[7] == 2) && (mArrayMoves[8] == 0)) {
            mHeuristicSum = 2;
            index = CORNER_INDICES[3]; // Esquina inferior derecha (8)
            return index;
        }

        // ---------------------------------------------------------------------
        // PASO 5: JUGADA POR DEFECTO
        // Si no hay ninguna condición de victoria, bloqueo o patrón especial,
        // se selecciona una casilla libre al azar.
        // ---------------------------------------------------------------------
        else {
            index = GenerateRandomNPCMove(ListButtons);
            return index;
        }
    }

    /**
     * Orquesta el turno completo de la máquina (NPC):
     *
     *   - Movimiento 1 de la IA:
     *       * Si el centro (índice 4) está vacío, lo toma de inmediato (es la mejor posición).
     *       * Si el usuario ya tomó el centro, la IA responde tomando una de las cuatro esquinas.
     *
     *   - Movimientos posteriores de la IA:
     *       * Si por alguna razón el centro sigue vacío, lo toma.
     *       * De lo contrario, ejecuta el método heurístico para buscar victorias o bloqueos.
     *       * Si se detecta un patrón de suma 5 con centro ocupado por la máquina,
     *         intenta tomar uno de los lados.
     *
     * @param ListButtons Lista de los 9 botones del tablero en la interfaz.
     * @param grbTicTacToe Contenedor visual del tablero.
     */
    public void PlayNPC(List<Button> ListButtons, ViewGroup grbTicTacToe) {
        int index;
        Random RandomNumber = new Random();

        // Solo juega si la partida continúa activa
        if (mCurrentGameState == GameState.Active) {

            // Caso A: Es el primer movimiento de la máquina (mMoveCount == 1)
            if (getMoveNumber() == 1) {
                // Si el centro está libre, la máquina lo toma inmediatamente
                if (mArrayMoves[4] == 0) {
                    index = CENTER_INDEX;
                    ShowNPCMove(ListButtons, index);
                    mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe);
                    return;
                }

                // Si el usuario tomó el centro, la máquina toma una esquina al azar
                do {
                    index = RandomNumber.nextInt(9);
                    if (mArrayMoves[index] == 0 &&
                            (index == CORNER_INDICES[0] || index == CORNER_INDICES[1] ||
                                    index == CORNER_INDICES[2] || index == CORNER_INDICES[3])) {
                        ShowNPCMove(ListButtons, index);
                        mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe);
                        return;
                    }
                } while (true);

            } else {
                // Caso B: Movimientos posteriores de la máquina (mMoveCount > 1)

                // Si el centro aún no ha sido ocupado, lo toma
                if (mArrayMoves[4] == 0) {
                    index = CENTER_INDEX;
                    ShowNPCMove(ListButtons, index);
                    mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe);
                    return;
                } else {
                    // Evaluar el tablero con el método heurístico numérico
                    index = HeuristicNumericalMethod(ListButtons);

                    // Caso especial: La IA tiene el centro y la suma heurística fue 5
                    if (mArrayMoves[4] == 3 && mHeuristicSum == 5) {
                        do {
                            index = RandomNumber.nextInt(9);
                            // Se busca jugar en uno de los lados exteriores (SIDE_INDICES)
                            // (Nota: se preserva la lógica fiel al código C# original)
                            if (mArrayMoves[index] == 0 &&
                                    (index == SIDE_INDICES[0] || index == SIDE_INDICES[0] ||
                                            index == SIDE_INDICES[2] || index == SIDE_INDICES[3])) {
                                ShowNPCMove(ListButtons, index);
                                mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe);
                                return;
                            }
                        } while (true);
                    }

                    // Realizar la jugada seleccionada por la heurística
                    ShowNPCMove(ListButtons, index);
                    mCurrentGameState = CheckWinner(ListButtons, grbTicTacToe);
                    return;
                }
            }
        } else {
            return;
        }
    }
}
