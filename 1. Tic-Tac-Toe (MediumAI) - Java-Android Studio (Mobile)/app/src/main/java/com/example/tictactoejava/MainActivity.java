package com.example.tictactoejava;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class MainActivity extends Activity {
    private static final char EMPTY = ' ';
    private final char[] board = new char[9];
    private final Button[] cells = new Button[9];
    private final MediumAI ai = new MediumAI();
    private TextView status;
    private boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createInterface();
        resetGame();
    }

    private void createInterface() {
        int padding = dp(24);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(padding, padding, padding, padding);
        root.setBackgroundColor(Color.rgb(248, 249, 252));

        TextView title = new TextView(this);
        title.setText("Tic-Tac-Toe");
        title.setTextColor(Color.rgb(32, 42, 68));
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        status = new TextView(this);
        status.setTextColor(Color.rgb(74, 84, 105));
        status.setTextSize(18);
        status.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams statusParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(64));
        root.addView(status, statusParams);

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(3);
        grid.setRowCount(3);
        LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        root.addView(grid, gridParams);

        for (int i = 0; i < cells.length; i++) {
            final int index = i;
            Button cell = new Button(this);
            cell.setTextSize(38);
            cell.setTextColor(Color.rgb(42, 72, 150));
            cell.setAllCaps(false);
            cell.setOnClickListener(view -> playHumanMove(index));
            GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams(
                    GridLayout.spec(i / 3, 1, 1f), GridLayout.spec(i % 3, 1, 1f));
            cellParams.width = 0;
            cellParams.height = 0;
            cellParams.setMargins(dp(4), dp(4), dp(4), dp(4));
            grid.addView(cell, cellParams);
            cells[i] = cell;
        }

        Button restart = new Button(this);
        restart.setText("Nuevo juego");
        restart.setAllCaps(false);
        restart.setOnClickListener(view -> resetGame());
        LinearLayout.LayoutParams restartParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        restartParams.topMargin = dp(16);
        root.addView(restart, restartParams);

        setContentView(root);
    }

    private void playHumanMove(int index) {
        if (gameOver || board[index] != EMPTY) return;
        ai.PlayUser(index);
        if (ai.getArrayMoves()[index] == 1) {
            makeMove(index, 'X');
        }
        if (finishIfNeeded()) return;

        int aiMove = ai.PlayNPC();
        if (aiMove >= 0) {
            makeMove(aiMove, '0');
            if (finishIfNeeded()) return;
        }
        status.setText("Tu turno (X)");
    }

    private void makeMove(int index, char player) {
        board[index] = player;
        cells[index].setText(String.valueOf(player));
        cells[index].setEnabled(false);
    }

    private boolean finishIfNeeded() {
        if (hasWon('X')) {
            status.setText("Ganó X");
        } else if (hasWon('0')) {
            status.setText("Ganó 0");
        } else if (isBoardFull()) {
            status.setText("Empate");
        } else {
            return false;
        }
        gameOver = true;
        return true;
    }

    private boolean hasWon(char player) {
        int[][] lines = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] line : lines) {
            if (board[line[0]] == player
                    && board[line[1]] == player
                    && board[line[2]] == player) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (char cell : board) {
            if (cell == EMPTY) return false;
        }
        return true;
    }

    private void resetGame() {
        for (int i = 0; i < board.length; i++) {
            board[i] = EMPTY;
            if (cells[i] != null) {
                cells[i].setText("");
                cells[i].setEnabled(true);
            }
        }
        gameOver = false;
        ai.InitializeData();
        status.setText("Tu turno (X) · La IA juega con 0");
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
