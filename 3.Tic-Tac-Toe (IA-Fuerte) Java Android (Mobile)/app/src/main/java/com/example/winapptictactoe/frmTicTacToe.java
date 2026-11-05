package com.example.winapptictactoe;

import android.app.Activity;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla principal del juego Tic-Tac-Toe (equivalente en Android al formulario frmTicTacToe de WinForms).
 *
 * Esta clase se encarga de:
 *   - Vincular los elementos de la interfaz gráfica (botones, textos, imagen animada).
 *   - Escuchar los eventos de clic del usuario en las 9 casillas del tablero.
 *   - Comunicar cada jugada al controlador de inteligencia artificial (UnbeatableAIController).
 *   - Gestionar las opciones de "Nuevo Juego" y "Salir".
 */
public class frmTicTacToe extends Activity {

    // =========================================================================
    // ATRIBUTOS Y ELEMENTOS DE LA INTERFAZ GRÁFICA
    // =========================================================================

    /** Lista dinámica que almacena los 9 botones del tablero en orden (índices 0 al 8). */
    private List<Button> ListButtons = new ArrayList<Button>();

    /** Instancia del controlador de Inteligencia Artificial que gestiona la lógica del juego. */
    private UnbeatableAIController UnbeatableAI = new UnbeatableAIController();

    /** Variable booleana que indica si la jugada del usuario fue válida para luego dar paso al NPC. */
    private static boolean ControlPlayer;

    // Controles visuales (mismos nombres que en el proyecto original):
    private Button btnNew;           // Botón para reiniciar / nueva partida
    private Button btnExit;          // Botón para salir de la aplicación
    private Button btnTicTacToe1;    // Casilla [0] (Fila 1, Columna 1)
    private Button btnTicTacToe2;    // Casilla [1] (Fila 1, Columna 2)
    private Button btnTicTacToe3;    // Casilla [2] (Fila 1, Columna 3)
    private Button btnTicTacToe4;    // Casilla [3] (Fila 2, Columna 1)
    private Button btnTicTacToe5;    // Casilla [4] (Fila 2, Columna 2 - Centro)
    private Button btnTicTacToe6;    // Casilla [5] (Fila 2, Columna 3)
    private Button btnTicTacToe7;    // Casilla [6] (Fila 3, Columna 1)
    private Button btnTicTacToe8;    // Casilla [7] (Fila 3, Columna 2)
    private Button btnTicTacToe9;    // Casilla [8] (Fila 3, Columna 3)

    private ViewGroup grbTicTacToe;    // Contenedor del tablero de 3x3
    private ViewGroup grbOptions;      // Contenedor de botones de opciones (Nuevo / Salir)
    private ViewGroup grbInformation;  // Contenedor informativo inferior
    private TextView lblInformation;   // Etiqueta de texto informativo
    private ImageView picCharacter;    // Vista de imagen donde se muestra la animación (Pocoyó)

    // =========================================================================
    // CICLO DE VIDA DE LA ACTIVIDAD
    // =========================================================================

    /**
     * Punto de entrada de la pantalla al iniciar la aplicación (equivalente a Form_Load en C#).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitializeComponent();  // Vincular las vistas del archivo XML
        InsertDataList();       // Llenar la lista con los 9 botones
        frmTicTacToe_Load();    // Iniciar el estado del juego
    }

    /**
     * Inicializa los componentes de la interfaz gráfica asociando los IDs del diseño XML
     * y configurando los escuchadores de eventos (OnClickListeners).
     */
    private void InitializeComponent() {
        setContentView(R.layout.activity_frm_tic_tac_toe);

        // Enlazar botones del diseño con las variables Java
        btnNew = findViewById(R.id.btn_new);
        btnExit = findViewById(R.id.btn_exit);
        btnTicTacToe1 = findViewById(R.id.btn_tic_tac_toe_1);
        btnTicTacToe2 = findViewById(R.id.btn_tic_tac_toe_2);
        btnTicTacToe3 = findViewById(R.id.btn_tic_tac_toe_3);
        btnTicTacToe4 = findViewById(R.id.btn_tic_tac_toe_4);
        btnTicTacToe5 = findViewById(R.id.btn_tic_tac_toe_5);
        btnTicTacToe6 = findViewById(R.id.btn_tic_tac_toe_6);
        btnTicTacToe7 = findViewById(R.id.btn_tic_tac_toe_7);
        btnTicTacToe8 = findViewById(R.id.btn_tic_tac_toe_8);
        btnTicTacToe9 = findViewById(R.id.btn_tic_tac_toe_9);

        // Enlazar contenedores y vistas informativas
        grbTicTacToe = findViewById(R.id.grb_tic_tac_toe);
        grbOptions = findViewById(R.id.grb_options);
        grbInformation = findViewById(R.id.grb_information);
        lblInformation = findViewById(R.id.lbl_information);
        picCharacter = findViewById(R.id.pic_character);

        // Cargar y reproducir la animación del personaje
        LoadPicCharacter();

        // Asignar el evento OnClick a cada casilla del tablero
        btnTicTacToe1.setOnClickListener(v -> btnTicTacToe1_Click(v));
        btnTicTacToe2.setOnClickListener(v -> btnTicTacToe2_Click(v));
        btnTicTacToe3.setOnClickListener(v -> btnTicTacToe3_Click(v));
        btnTicTacToe4.setOnClickListener(v -> btnTicTacToe4_Click(v));
        btnTicTacToe5.setOnClickListener(v -> btnTicTacToe5_Click(v));
        btnTicTacToe6.setOnClickListener(v -> btnTicTacToe6_Click(v));
        btnTicTacToe7.setOnClickListener(v -> btnTicTacToe7_Click(v));
        btnTicTacToe8.setOnClickListener(v -> btnTicTacToe8_Click(v));
        btnTicTacToe9.setOnClickListener(v -> btnTicTacToe9_Click(v));

        // Asignar eventos a los botones de control
        btnNew.setOnClickListener(v -> btnNew_Click(v));
        btnExit.setOnClickListener(v -> btnExit_Click(v));
    }

    /**
     * Carga y anima el archivo GIF original (pocoyo_bailando) en el ImageView.
     * En Android 9+ (API 28) utiliza AnimatedImageDrawable para animación fluida infinita.
     */
    private void LoadPicCharacter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(
                        getResources(), R.drawable.pocoyo_bailando);
                Drawable drawable = ImageDecoder.decodeDrawable(source);
                picCharacter.setImageDrawable(drawable);
                if (drawable instanceof AnimatedImageDrawable) {
                    AnimatedImageDrawable animated = (AnimatedImageDrawable) drawable;
                    animated.setRepeatCount(AnimatedImageDrawable.REPEAT_INFINITE);
                    animated.start();
                }
            } catch (IOException e) {
                picCharacter.setImageResource(R.drawable.pocoyo_bailando);
            }
        } else {
            picCharacter.setImageResource(R.drawable.pocoyo_bailando);
        }
    }

    /**
     * Crea el menú superior de la aplicación (conservando la estructura del menú original).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mnu_tic_tac_toe, menu);
        return true;
    }

    /**
     * Agrega los 9 botones a la lista enlazada/dinámica en el orden exacto de casillas (0 a 8).
     */
    private void InsertDataList() {
        ListButtons.add(btnTicTacToe1); // Nodo 0: Esquina superior izquierda
        ListButtons.add(btnTicTacToe2); // Nodo 1: Borde superior central
        ListButtons.add(btnTicTacToe3); // Nodo 2: Esquina superior derecha
        ListButtons.add(btnTicTacToe4); // Nodo 3: Borde medio izquierdo
        ListButtons.add(btnTicTacToe5); // Nodo 4: Centro del tablero
        ListButtons.add(btnTicTacToe6); // Nodo 5: Borde medio derecho
        ListButtons.add(btnTicTacToe7); // Nodo 6: Esquina inferior izquierda
        ListButtons.add(btnTicTacToe8); // Nodo 7: Borde inferior central
        ListButtons.add(btnTicTacToe9); // Nodo 8: Esquina inferior derecha
    }

    /**
     * Inicializa o reinicia todos los datos de la partida y limpia los botones.
     */
    private void InitializeGame() {
        UnbeatableAI.InitializeGameData();
        UnbeatableAI.InitializeControls(ListButtons, grbTicTacToe);
    }

    /**
     * Evento ejecutado al cargar la actividad: inicia la primera partida.
     */
    private void frmTicTacToe_Load() {
        InitializeGame();
    }

    // =========================================================================
    // MANEJADORES DE EVENTOS DE CLIC (CASILLAS DEL TABLERO)
    // Cada clic sigue la misma secuencia:
    //   1. PlayUser: Se intenta colocar la "X" del jugador en la casilla presionada.
    //   2. Si la jugada fue válida (ControlPlayer == true), se llama a PlayNPC para
    //      que la Inteligencia Artificial responda con su jugada ("O").
    // =========================================================================

    private void btnTicTacToe1_Click(View view) {
        ControlPlayer = UnbeatableAI.PlayUser(btnTicTacToe1, ListButtons, grbTicTacToe);
        if (ControlPlayer == true) {
            UnbeatableAI.PlayNPC(ListButtons, grbTicTacToe);
        }
    }

    private void btnTicTacToe2_Click(View view) {
        ControlPlayer = UnbeatableAI.PlayUser(btnTicTacToe2, ListButtons, grbTicTacToe);
        if (ControlPlayer == true) {
            UnbeatableAI.PlayNPC(ListButtons, grbTicTacToe);
        }
    }

    private void btnTicTacToe3_Click(View view) {
        ControlPlayer = UnbeatableAI.PlayUser(btnTicTacToe3, ListButtons, grbTicTacToe);
        if (ControlPlayer == true) {
            UnbeatableAI.PlayNPC(ListButtons, grbTicTacToe);
        }
    }

    private void btnTicTacToe4_Click(View view) {
        ControlPlayer = UnbeatableAI.PlayUser(btnTicTacToe4, ListButtons, grbTicTacToe);
        if (ControlPlayer == true) {
            UnbeatableAI.PlayNPC(ListButtons, grbTicTacToe);
        }
    }

    private void btnTicTacToe5_Click(View view) {
        ControlPlayer = UnbeatableAI.PlayUser(btnTicTacToe5, ListButtons, grbTicTacToe);
        if (ControlPlayer == true) {
            UnbeatableAI.PlayNPC(ListButtons, grbTicTacToe);
        }
    }

    private void btnTicTacToe6_Click(View view) {
        ControlPlayer = UnbeatableAI.PlayUser(btnTicTacToe6, ListButtons, grbTicTacToe);
        if (ControlPlayer == true) {
            UnbeatableAI.PlayNPC(ListButtons, grbTicTacToe);
        }
    }

    private void btnTicTacToe7_Click(View view) {
        ControlPlayer = UnbeatableAI.PlayUser(btnTicTacToe7, ListButtons, grbTicTacToe);
        if (ControlPlayer == true) {
            UnbeatableAI.PlayNPC(ListButtons, grbTicTacToe);
        }
    }

    private void btnTicTacToe8_Click(View view) {
        ControlPlayer = UnbeatableAI.PlayUser(btnTicTacToe8, ListButtons, grbTicTacToe);
        if (ControlPlayer == true) {
            UnbeatableAI.PlayNPC(ListButtons, grbTicTacToe);
        }
    }

    private void btnTicTacToe9_Click(View view) {
        ControlPlayer = UnbeatableAI.PlayUser(btnTicTacToe9, ListButtons, grbTicTacToe);
        if (ControlPlayer == true) {
            UnbeatableAI.PlayNPC(ListButtons, grbTicTacToe);
        }
    }

    /**
     * Evento al presionar el botón "Nuevo": Reinicia el tablero y comienza una nueva partida.
     */
    private void btnNew_Click(View view) {
        InitializeGame();
    }

    /**
     * Evento al presionar el botón "Salir": Cierra la aplicación (equivalente a Close() en C#).
     */
    private void btnExit_Click(View view) {
        finish();
    }
}
