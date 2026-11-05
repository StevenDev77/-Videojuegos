package com.example.tictactoejava;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Pantalla principal del juego Tic-Tac-Toe (UnbeatableAI con Minimax).
 *
 * Equivale a frmTicTacToe.cs del proyecto Tic-Tac-Toe-Version-MiniMax. Se
 * conservan los nombres de los campos (buttons, board, aiController,
 * isHumanTurn, isGameOver) y de los métodos (InitializeGame, ResetBoard,
 * OnButtonClick, AIMove, MakeMove, CheckGameEnd, CheckWin, IsBoardFull,
 * btnNew_Click, btnExit_Click). El tablero char[3,3] de C# se representa como
 * char[3][3].
 */
public class MainActivity extends Activity {

    private Button[] buttons = new Button[9];
    private char[][] board = new char[3][3];
    private UnbeatableAIController aiController;
    private boolean isHumanTurn = true;
    private boolean isGameOver = false;

    private TextView lblInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitializeComponent();
        aiController = new UnbeatableAIController();
        InitializeGame();
    }

    /**
     * Construye la interfaz: título, etiqueta de estado, tablero 3x3 y botones
     * de Nuevo/Salir.
     */
    private void InitializeComponent() {
        int padding = dp(20);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(padding, padding, padding, padding);
        root.setBackgroundColor(Color.rgb(248, 249, 252));

        TextView title = new TextView(this);
        title.setText("Tic-Tac-Toe");
        title.setTextColor(Color.rgb(32, 42, 68));
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        lblInformation = new TextView(this);
        lblInformation.setText("Turno de X");
        lblInformation.setTextColor(Color.rgb(74, 84, 105));
        lblInformation.setTextSize(16);
        lblInformation.setGravity(Gravity.CENTER);
        root.addView(lblInformation, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(48)));

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(3);
        grid.setRowCount(3);
        root.addView(grid, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        for (int i = 0; i < 9; i++) {
            Button cell = new Button(this);
            cell.setTextSize(38);
            cell.setTextColor(Color.rgb(42, 72, 150));
            cell.setAllCaps(false);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(i / 3, 1, 1f),
                    GridLayout.spec(i % 3, 1, 1f));
            params.width = 0;
            params.height = 0;
            params.setMargins(dp(3), dp(3), dp(3), dp(3));
            grid.addView(cell, params);
            buttons[i] = cell;
        }

        LinearLayout options = new LinearLayout(this);
        options.setOrientation(LinearLayout.HORIZONTAL);
        options.setGravity(Gravity.CENTER);
        root.addView(options, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button btnNew = new Button(this);
        btnNew.setText("Nuevo");
        btnNew.setAllCaps(false);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnNew_Click(v); }
        });
        options.addView(btnNew, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        Button btnExit = new Button(this);
        btnExit.setText("Salir");
        btnExit.setAllCaps(false);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnExit_Click(v); }
        });
        options.addView(btnExit, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        setContentView(root);
    }

    /**
     * Asigna los 9 botones al arreglo, registra el evento de clic y reinicia
     * el tablero.
     */
    private void InitializeGame() {
        for (int i = 0; i < 9; i++) {
            final int index = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { OnButtonClick(index); }
            });
        }

        ResetBoard();
    }

    /**
     * Limpia el tablero lógico y visual, y restaura los turnos.
     */
    private void ResetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }

        for (Button btn : buttons) {
            if (btn != null) {
                btn.setText("");
                btn.setEnabled(true);
            }
        }

        isHumanTurn = true;
        isGameOver = false;
        lblInformation.setText("Turno de X");
    }

    /**
     * Atiende el clic de una casilla: jugada humana, comprobación de fin y
     * respuesta de la IA.
     */
    private void OnButtonClick(int index) {
        if (isGameOver || !isHumanTurn) return;

        int row = index / 3;
        int col = index % 3;

        if (board[row][col] == ' ') {
            MakeMove(index, 'X');

            if (CheckGameEnd()) return;

            isHumanTurn = false;
            AIMove();
        }
    }

    /**
     * Pide a la IA su mejor jugada y la aplica.
     */
    private void AIMove() {
        if (isGameOver) return;

        int bestMove = aiController.GetBestMove(board);
        if (bestMove != -1) {
            MakeMove(bestMove, 'O');
            CheckGameEnd();
        }

        isHumanTurn = true;
    }

    /**
     * Coloca la ficha en el tablero lógico y en el botón correspondiente.
     */
    private void MakeMove(int index, char player) {
        int row = index / 3;
        int col = index % 3;

        board[row][col] = player;
        buttons[index].setText(String.valueOf(player));
        buttons[index].setEnabled(false);
    }

    /**
     * Comprueba el final de la partida y muestra el resultado.
     */
    private boolean CheckGameEnd() {
        if (CheckWin('X')) {
            showResult("You Win!");
            isGameOver = true;
            return true;
        }

        if (CheckWin('O')) {
            showResult("AI Wins!");
            isGameOver = true;
            return true;
        }

        if (IsBoardFull()) {
            showResult("It's a Draw!");
            isGameOver = true;
            return true;
        }

        return false;
    }

    private void showResult(String message) {
        lblInformation.setText(message);
        new android.app.AlertDialog.Builder(this)
                .setTitle("Tic Tac Toe")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Comprueba si el jugador indicado tiene tres en línea.
     */
    private boolean CheckWin(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;

        return false;
    }

    /**
     * Indica si el tablero está lleno.
     */
    private boolean IsBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    private void btnNew_Click(View view) {
        ResetBoard();
    }

    private void btnExit_Click(View view) {
        finish();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
