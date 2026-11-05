# Migración C# (WinForms) → Java (Android)

Proyecto Android listo para abrir en Android Studio. El proyecto C# original
(`WinAppTicTacToe/`) **no fue modificado**.

- Carpeta Android: `TicTacToeAndroid/`
- Paquete Java: `com.example.winapptictactoe`
- Activity de entrada: `frmTicTacToe` (nombre original conservado)
- `minSdk 21`, `compileSdk/targetSdk 35`

## 1. Mapa de archivos

| Original C# | Destino Android | Tratamiento |
|---|---|---|
| `UnbeatableAIController.cs` | `app/src/main/java/com/example/winapptictactoe/UnbeatableAIController.java` | Lógica copiada fielmente; solo se adaptaron los tipos de UI. |
| `Player` (enum dentro del .cs) | `.../Player.java` | Enum independiente; ordinales 0, 1, 2 conservados. |
| `GameState` (enum dentro del .cs) | `.../GameState.java` | Enum independiente; ordinales 0, 1 conservados. |
| `frmTicTacToe.cs` | `.../frmTicTacToe.java` | Activity; nombres de clase, campos, métodos y eventos conservados. |
| `frmTicTacToe.Designer.cs` | `app/src/main/res/layout/activity_frm_tic_tac_toe.xml` + `InitializeComponent()` | Coordenadas, tamaños, textos y orden de los controles reproducidos. |
| `Program.cs` | `app/src/main/AndroidManifest.xml` | No existe `Main` en Android; el launcher arranca la Activity. |
| `frmTicTacToe.resx` (`picCharacter.Image`) | `app/src/main/res/drawable-nodpi/pocoyo_bailando.gif` | Imagen original extraída del `.resx` sin cambios. |
| `frmTicTacToe.resx` (`$this.Icon`) | `app/src/main/res/mipmap-*/ic_launcher.png` | Icono original convertido a PNG para Android. |
| `Properties/Resources.resx` | No aplica | No se usaba en el formulario. |
| `App.config`, `AssemblyInfo.cs`, `Settings` | No aplica | Equivalentes gestionados por Gradle/AndroidManifest. |

## 2. Qué se conservó exactamente

### 2.1 `UnbeatableAIController`

Se conservaron los nombres y la lógica:

- Campos: `mCurrentPlayerFlag`, `mMoveCount`, `mCurrentGameState`,
  `mBoardState`, `mHeuristicSum`, `mArrayMoves`.
- Constantes: `CENTER_INDEX`, `CORNER_INDICES`, `SIDE_INDICES`,
  `WinningLines` (mismo orden de filas, columnas y diagonales).
- Métodos: `InitializeGameData`, `InitializeControls`, `DisableControls`,
  `EnableControls`, `CheckWinner`, `ShowUserMove`, `PlayUser`,
  `GenerateRandomNPCMove`, `ShowNPCMove`, `HeuristicNumericalMethod`,
  `PlayNPC`, `FlagForPlayer`.
- La codificación original de `mArrayMoves` se mantiene:
  - celda vacía = `0`
  - jugada del usuario = `1`
  - jugada del NPC = `3`
  - las sumas 6, 2 y 5 del método heurístico se conservan.
- **No se reemplazó la IA por minimax ni por otro algoritmo.**
- **No se simplificó** la cadena de `if / else if` de
  `HeuristicNumericalMethod`.
- Se conservaron los bucles `do { ... } while (true)` y
  `while (mCurrentGameState == GameState.Active)`.
- Se conservó el orden exacto de las comprobaciones del NPC.

### 2.2 `frmTicTacToe`

- Clase `frmTicTacToe`.
- Campos `ListButtons`, `UnbeatableAI`, `ControlPlayer` y los nueve botones
  `btnTicTacToe1` ... `btnTicTacToe9`.
- Métodos `InsertDataList`, `InitializeGame`, `frmTicTacToe_Load`,
  `btnTicTacToe1_Click` ... `btnTicTacToe9_Click`, `btnNew_Click`,
  `btnExit_Click`.
- La secuencia de cada clic se conserva:
  1. `PlayUser(...)`
  2. si devuelve `true`, se llama `PlayNPC(...)`.
- `btnNew` reinicia con `InitializeGameData()` y `InitializeControls(...)`.
- `btnExit` usa `finish()` como equivalente de `Close()`.

## 3. Diferencias C# / Java / Android y adaptaciones necesarias

### 3.1 Propiedades C#

C# tiene propiedades (`Flag`, `MoveNumber`, `GameControl`, `ArrayMoves`).
Java no. Se migraron a getters/setters JavaBean:

- `Flag` → `getFlag()` / `setFlag(String)`
- `MoveNumber` → `getMoveNumber()` / `setMoveNumber(int)`
- `GameControl` → `getGameControl()` / `setGameControl(GameState)`
- `ArrayMoves` → `getArrayMoves()` / `setArrayMoves(Player[])`

La lógica de cada getter/setter es idéntica.

### 3.2 Inicialización del arreglo de enums (diferencia crítica)

En C#:

```csharp
mBoardState = new Player[9]; // todos los elementos quedan en Player.None (0)
```

En Java:

```java
mBoardState = new Player[9]; // todos los elementos quedan en null
```

Por eso se añadió el llenado explícito con `Player.None`. Sin este cambio,
el algoritmo funcionaría distinto. Es una adaptación obligatoria, no un cambio
de lógica.

### 3.3 Controles de UI

| WinForms | Android |
|---|---|
| `Button` | `android.widget.Button` |
| `GroupBox` | `android.view.ViewGroup` (`RelativeLayout` en el layout) |
| `MessageBox.Show` | `AlertDialog` |
| `Button.TabIndex` | `android:tag="0".."8"` en el layout |
| `Button.Text` | `Button.setText(...)` / `getText()` |
| `GroupBox.Enabled` | `setEnabledRecursive(...)` |
| `Form.Close()` | `Activity.finish()` |
| `Form.Load` | `Activity.onCreate(...)` |
| `MenuStrip` | menú de opciones Android |
| `Application.Run` | `AndroidManifest.xml` + Activity launcher |

### 3.4 `Button.TabIndex` → `tag`

El controlador original usa `objectButton.TabIndex` como índice del tablero.
Android no tiene `TabIndex`. Cada botón del layout tiene:

```xml
android:tag="0"
```

y `ShowUserMove` obtiene el índice con:

```java
int index = Integer.parseInt(objectButton.getTag().toString());
```

Los tags 0..8 coinciden con los `TabIndex` originales 0..8.

### 3.5 `GroupBox.Enabled` → deshabilitado recursivo

En WinForms, deshabilitar un `GroupBox` deshabilita también sus hijos. En
Android, `ViewGroup.setEnabled(false)` **no** deshabilita automáticamente a los
hijos. Para conservar el comportamiento original, `DisableControls` y
`EnableControls` recorren recursivamente el grupo y sus botones. Sin esto, el
tablero seguiría aceptando clics después de terminar la partida.

### 3.6 `MessageBox` → `AlertDialog`

`MessageBox.Show` es modal/bloqueante. `AlertDialog.show()` no bloquea. El
estado del juego se actualiza igual y el tablero se deshabilita inmediatamente
después de mostrar el diálogo. Los textos se conservan:

- Título: `Resultado`
- `¡Ganó X!` / `¡Ganó O!`
- `¡Empate!`

### 3.7 `Random`

- `new Random()` → `new java.util.Random()`
- `RandomNumber.Next(0, 9)` → `RandomNumber.nextInt(9)`

El rango es el mismo (0..8) y se conserva el uso de una instancia nueva de
`Random` en los mismos lugares que el original.

### 3.8 Eventos

Los `Click += new EventHandler(...)` del diseñador se reemplazaron por
`setOnClickListener(...)` en `InitializeComponent()`. Se conservaron los nueve
métodos `btnTicTacToeN_Click`, además de `btnNew_Click` y `btnExit_Click`.

### 3.9 Nombres de recursos Android

Android exige nombres de recursos en minúsculas (por ejemplo
`btn_tic_tac_toe_1`). Los **nombres de campos y métodos Java** sí conservan los
nombres originales (`btnTicTacToe1`, `ListButtons`, etc.).

### 3.10 Menú

El `MenuStrip` original tenía:

- Archivo → Nuevo, separador, Salir
- Ayuda → Acerca de..., separador, Contenido
- Ver → Puntajes, separador, Resetear puntajes

En el C# **ninguno de esos ítems tenía manejador de clic**. Por fidelidad, en
Android se creó un menú de opciones con los mismos textos y **sin acciones**.
No se inventó funcionalidad para ellos. Android no tiene un elemento de
separador en menús XML, por lo que los separadores no se representan.

### 3.11 Fuente e interfaz

- La fuente original `Ravie` no está disponible en Android; se usa
  `sans-serif` en negrita.
- Los tamaños en puntos se convirtieron a `sp`:
  - 20.25 pt ≈ 27 sp
  - 19.8 pt ≈ 26.4 sp
  - 12 pt ≈ 16 sp
- El borde del `GroupBox` se aproximó con un `shape` gris de 1 dp.
- Las coordenadas y tamaños del formulario original (279 × 420; tablero
  259 × 225; botones 60 × 60) se reprodujeron en `dp`.

### 3.12 Imagen animada

`picCharacter` era un GIF animado dentro de `frmTicTacToe.resx`. Se extrajo el
GIF original sin modificarlo. En API 28+ se carga con `ImageDecoder` como
`AnimatedImageDrawable`; en versiones anteriores se muestra el primer cuadro.
Esto es una adaptación de plataforma; la imagen es la original.

### 3.13 Orientación

La Activity se fija en vertical (`portrait`) porque el formulario original era
de tamaño fijo y no tenía comportamiento de rotación. Así se evita que Android
reconstruya la Activity y reinicie la partida al girar el dispositivo. Es una
adaptación de plataforma, no una regla nueva del juego.

## 4. Rarezas del original que se conservaron intencionalmente

Estas partes parecen errores, pero **no se corrigieron** porque la prioridad es
la fidelidad:

1. En `PlayNPC`, la condición de esquinas laterales repite
   `SIDE_INDICES[0]` y nunca comprueba `SIDE_INDICES[1]`:

   ```java
   (index == SIDE_INDICES[0] || index == SIDE_INDICES[0] ||
    index == SIDE_INDICES[2] || index == SIDE_INDICES[3])
   ```

   Como consecuencia, si el único espacio libre fuera el índice 3
   (`SIDE_INDICES[1]`), ese `do/while` podría no terminar. Se conserva tal cual
   porque así se comporta el original; no se corrigió.

2. `mHeuristicSum` no se reinicia en todas las rutas de
   `HeuristicNumericalMethod`; se conserva su valor anterior cuando el método
   cae en el `else` final.

3. `FlagForPlayer(Player.None)` devolvería `"O"`, igual que el original. Solo se
   invoca con celdas ganadoras o con `Player.NPC`.

4. El método se llama "Unbeatable", pero conserva los movimientos aleatorios y
   las condiciones originales; no se sustituyó por una IA perfecta.

5. La comprobación de empate sigue siendo `mMoveCount == 9`.

6. El orden de las condiciones 6 → 2 → 5 en `HeuristicNumericalMethod` es
   exactamente el original.

## 5. Verificación realizada

- `./gradlew assembleDebug`: **BUILD SUCCESSFUL**.
- `./gradlew lintDebug`: **0 errores**; solo advertencias intencionales
  (orientación fija, GIF original, puntos suspensivos originales).
- APK generado en:
  `TicTacToeAndroid/app/build/outputs/apk/debug/app-debug.apk`
- Simulación de la lógica migrada con 5000 partidas y movimientos aleatorios
  del usuario: sin excepciones, sin bloqueos, sin jugadas duplicadas y con
  consistencia entre el estado interno y el texto de los botones.
- La transcripción de `HeuristicNumericalMethod` se comparó con el original y
  es idéntica tras normalizar la sintaxis.
- `PlayNPC` conserva exactamente las mismas ramas y condiciones; solo cambian
  `Next(0,9)` por `nextInt(9)` y la propiedad `MoveNumber` por
  `getMoveNumber()`.

## 6. Cómo abrir y ejecutar

1. Abrir Android Studio.
2. `File > Open...` y seleccionar `TicTacToeAndroid/`.
3. Dejar que Gradle sincronice (Android Studio creará `local.properties`).
4. Ejecutar la configuración `app` en un emulador o dispositivo.

Requisitos: JDK 17 o superior y Android SDK 35. El proyecto usa únicamente APIs
de plataforma, sin dependencias externas.

## 7. Archivos que no se migraron

- `Program.cs`: Android no usa `Main`; el launcher está en el manifiesto.
- `App.config`, `AssemblyInfo.cs`, `Settings.settings`: configuración de .NET
  sin equivalente necesario en este proyecto.
- `Properties/Resources.resx`: no se usaba para el formulario.
- `bin/`, `obj/`: artefactos de compilación de Visual Studio.

No se añadieron puntajes, persistencia, ajustes, diálogos de "Acerca de" ni
ninguna otra funcionalidad inexistente en el original.
