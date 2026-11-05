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

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla principal del juego Tic-Tac-Toe (nivel medio).
 *
 * Equivale a frmTicTacToe.cs del proyecto WinForms. Se conservan los nombres
 * de los campos (ListButtons, MediumAI, ControlPlayer, btnTicTacToe1..9,
 * btnNew, btnExit) y de los manejadores de eventos. La interfaz se construye
 * por código porque el proyecto Android no incluye un archivo de layout.
 */
public class MainActivity extends Activity {

    private List<Button> ListButtons = new ArrayList<Button>();
    private MediumAIController MediumAI = new MediumAIController();
    private static boolean ControlPlayer;

    private Button btnNew;
    private Button btnExit;
    private Button btnTicTacToe1;
    private Button btnTicTacToe2;
    private Button btnTicTacToe3;
    private Button btnTicTacToe4;
    private Button btnTicTacToe5;
    private Button btnTicTacToe6;
    private Button btnTicTacToe7;
    private Button btnTicTacToe8;
    private Button btnTicTacToe9;

    private ViewGroup grbTicTacToe;
    private TextView lblInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitializeComponent();
        InsertDataList();
        frmTicTacToe_Load();
    }

    /**
     * Construye la interfaz: título, etiqueta de estado, tablero 3x3 y botones
     * de Nuevo/Salir. Cada casilla guarda su índice en el tag (0 a 8).
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
        grbTicTacToe = grid;
        root.addView(grid, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        btnTicTacToe1 = createCell(0);
        btnTicTacToe2 = createCell(1);
        btnTicTacToe3 = createCell(2);
        btnTicTacToe4 = createCell(3);
        btnTicTacToe5 = createCell(4);
        btnTicTacToe6 = createCell(5);
        btnTicTacToe7 = createCell(6);
        btnTicTacToe8 = createCell(7);
        btnTicTacToe9 = createCell(8);

        LinearLayout options = new LinearLayout(this);
        options.setOrientation(LinearLayout.HORIZONTAL);
        options.setGravity(Gravity.CENTER);
        root.addView(options, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        btnNew = new Button(this);
        btnNew.setText("Nuevo");
        btnNew.setAllCaps(false);
        options.addView(btnNew, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        btnExit = new Button(this);
        btnExit.setText("Salir");
        btnExit.setAllCaps(false);
        options.addView(btnExit, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        btnTicTacToe1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnTicTacToe1_Click(v); }
        });
        btnTicTacToe2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnTicTacToe2_Click(v); }
        });
        btnTicTacToe3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnTicTacToe3_Click(v); }
        });
        btnTicTacToe4.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnTicTacToe4_Click(v); }
        });
        btnTicTacToe5.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnTicTacToe5_Click(v); }
        });
        btnTicTacToe6.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnTicTacToe6_Click(v); }
        });
        btnTicTacToe7.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnTicTacToe7_Click(v); }
        });
        btnTicTacToe8.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnTicTacToe8_Click(v); }
        });
        btnTicTacToe9.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnTicTacToe9_Click(v); }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnNew_Click(v); }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { btnExit_Click(v); }
        });

        setContentView(root);
    }

    private Button createCell(final int index) {
        Button cell = new Button(this);
        cell.setTextSize(38);
        cell.setTextColor(Color.rgb(42, 72, 150));
        cell.setAllCaps(false);
        cell.setTag(String.valueOf(index));

        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(index / 3, 1, 1f),
                GridLayout.spec(index % 3, 1, 1f));
        params.width = 0;
        params.height = 0;
        params.setMargins(dp(3), dp(3), dp(3), dp(3));
        ((GridLayout) grbTicTacToe).addView(cell, params);
        return cell;
    }

    private void InsertDataList() {
        ListButtons.add(btnTicTacToe1); // nodo 0
        ListButtons.add(btnTicTacToe2); // nodo 1
        ListButtons.add(btnTicTacToe3); // nodo 2
        ListButtons.add(btnTicTacToe4); // nodo 3
        ListButtons.add(btnTicTacToe5); // nodo 4
        ListButtons.add(btnTicTacToe6); // nodo 5
        ListButtons.add(btnTicTacToe7); // nodo 6
        ListButtons.add(btnTicTacToe8); // nodo 7
        ListButtons.add(btnTicTacToe9); // nodo 8
    }

    private void InitializeGame() {
        MediumAI.InitializeGameData();
        MediumAI.InitializeControls(ListButtons, grbTicTacToe);
        lblInformation.setText("Turno de X");
    }

    private void frmTicTacToe_Load() {
        InitializeGame();
    }

    private void playCell(Button button, View view) {
        ControlPlayer = MediumAI.PlayUser(button, ListButtons, grbTicTacToe);
        if (ControlPlayer == true) {
            MediumAI.PlayNPC(ListButtons, grbTicTacToe);
            if (MediumAI.getGameControl() == GameState.Active) {
                lblInformation.setText("Turno de X");
            }
        }
    }

    private void btnTicTacToe1_Click(View view) { playCell(btnTicTacToe1, view); }
    private void btnTicTacToe2_Click(View view) { playCell(btnTicTacToe2, view); }
    private void btnTicTacToe3_Click(View view) { playCell(btnTicTacToe3, view); }
    private void btnTicTacToe4_Click(View view) { playCell(btnTicTacToe4, view); }
    private void btnTicTacToe5_Click(View view) { playCell(btnTicTacToe5, view); }
    private void btnTicTacToe6_Click(View view) { playCell(btnTicTacToe6, view); }
    private void btnTicTacToe7_Click(View view) { playCell(btnTicTacToe7, view); }
    private void btnTicTacToe8_Click(View view) { playCell(btnTicTacToe8, view); }
    private void btnTicTacToe9_Click(View view) { playCell(btnTicTacToe9, view); }

    private void btnNew_Click(View view) {
        InitializeGame();
    }

    private void btnExit_Click(View view) {
        finish();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
