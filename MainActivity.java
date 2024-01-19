package es.riberadeltajo.buscaminas_davidpascual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.os.CountDownTimer;
import android.widget.GridLayout;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SpinnerBomba.OnRespuestaSkin {
    private int filas = 0;
    private int columnas = 0;
    private int minas = 0;
    private int dificultad = 0;
    private int[][] casillas;
    public int bomba = R.drawable.bomba;
    private boolean[][] casillasExploradas;
    private int ganar;
    private int minasRestantes = ganar;
    private MenuItem minasRestantesMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout c = findViewById(R.id.ConstLayout);
        c.post(new Runnable() {
            @Override
            public void run() {
                iniciar(c);
            }
        });
    }

    public void iniciar(ConstraintLayout c) {
        casillasExploradas = new boolean[filas][columnas];
        int[] ajustesDificultad = Dificultad();
        filas = ajustesDificultad[0];
        columnas = ajustesDificultad[1];
        minas = ajustesDificultad[2];

        Casilla ca = new Casilla(minas, filas, columnas);
        ca.iniciar();
        casillas = ca.getTablero();
        ganar = minas;

        casillasExploradas = new boolean[filas][columnas];
        minasRestantes = ganar;
        actualizarTituloMenu();

        GridLayout grid = createGrid(ca);
        c.addView(grid);

        if (bomba == 0) {
            bomba = R.drawable.bomba;
        }
    }

    private int[] Dificultad() {
        int[] valores = new int[3];
        switch (dificultad) {
            case 0:
                valores[0] = 8;
                valores[1] = 8;
                valores[2] = 10;
                break;
            case 1:
                valores[0] = 12;
                valores[1] = 12;
                valores[2] = 30;
                break;
            case 2:
                valores[0] = 16;
                valores[1] = 16;
                valores[2] = 60;
                break;
        }
        return valores;
    }

    private GridLayout createGrid(Casilla ca) {
        GridLayout grid = new GridLayout(this);

        ConstraintLayout parentLayout = findViewById(R.id.ConstLayout);

        int parentWidth = parentLayout.getWidth();
        int parentHeight = parentLayout.getHeight();

        GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams();
        gridParams.width = parentWidth;
        gridParams.height = parentHeight;
        grid.setLayoutParams(gridParams);

        grid.setRowCount(filas);
        grid.setColumnCount(columnas);

        int buttonWidth = parentWidth / columnas;
        int buttonHeight = parentHeight / filas;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                final int fila = i;
                final int columna = j;
                final Button button = new Button(this);
                GridLayout.LayoutParams buttonParams = new GridLayout.LayoutParams();
                buttonParams.width = buttonWidth;
                buttonParams.height = buttonHeight;
                button.setLayoutParams(buttonParams);
                button.setBackgroundResource(R.drawable.casilla);
                grid.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ca.esMina(fila, columna)) {
                            descubrirTodasMinas(grid, ca, fila, columna);
                            partidaPerdida();
                        } else {
                            casillasExploradas[fila][columna] = true;
                            int minasAdyacentes = calcularMinasAdyacentes(fila, columna);
                            if (minasAdyacentes == 0) {
                                descubrirCasillasSinMinasAdyacentes(grid, fila, columna);
                            } else {
                                button.setBackgroundResource(R.drawable.casilla_desbloqueada);
                                button.setText(String.valueOf(minasAdyacentes));
                            }
                            actualizarMinasRestantes();
                        }
                    }
                });

                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (ca.esMina(fila, columna)) {
                            button.setBackgroundResource(R.drawable.bandera);
                            ganar--;
                            if (ganar == 0) {
                                partidaGanada();
                            }
                        } else {
                            partidaPerdida();
                        }
                        actualizarMinasRestantes();
                        return true;
                    }
                });
            }
        }
        return grid;
    }

    private void descubrirTodasMinas(GridLayout grid, Casilla ca, int fila, int columna) {
        for (fila = 0; fila < filas; fila++) {
            for (columna = 0; columna < columnas; columna++) {
                if (ca.esMina(fila, columna)) {
                    Button button = (Button) grid.getChildAt(fila * columnas + columna);
                    button.setBackgroundResource(bomba);
                    casillasExploradas[fila][columna] = true;
                }
            }
        }
    }

    private void partidaGanada() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Has ganado!");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConstraintLayout c = findViewById(R.id.ConstLayout);
                c.removeAllViews();
                iniciar(c);
            }
        });
        builder.create().show();
        actualizarMinasRestantes();
    }

    private void descubrirCasillasSinMinasAdyacentes(GridLayout grid, int fila, int columna) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    Button button = (Button) grid.getChildAt(fila * columnas + columna);
                    button.setBackgroundResource(R.drawable.casilla_desbloqueada);
                }
                int nuevaFila = fila + i;
                int nuevaColumna = columna + j;
                if (posicionValida(nuevaFila, nuevaColumna) && !casillasExploradas[nuevaFila][nuevaColumna]) {
                    int minasAdyacentes = calcularMinasAdyacentes(nuevaFila, nuevaColumna);
                    casillasExploradas[nuevaFila][nuevaColumna] = true;
                    Button button = (Button) grid.getChildAt(nuevaFila * columnas + nuevaColumna);
                    if (minasAdyacentes == 0) {
                        descubrirCasillasSinMinasAdyacentes(grid, nuevaFila, nuevaColumna);
                        button.setBackgroundResource(R.drawable.casilla_desbloqueada);
                    } else {
                        button.setBackgroundResource(R.drawable.casilla_desbloqueada);
                        button.setText(String.valueOf(minasAdyacentes));
                    }
                }
            }
        }
        actualizarMinasRestantes();
    }

    private int calcularMinasAdyacentes(int row, int column) {
        int minasAdyacentes = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nuevaFila = row + i;
                int nuevaColumna = column + j;

                if (posicionValida(nuevaFila, nuevaColumna) && casillas[nuevaFila][nuevaColumna] == -1) {
                    minasAdyacentes++;
                }
            }
        }
        return minasAdyacentes;
    }

    private boolean posicionValida(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    private void partidaPerdida() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Has perdido!");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConstraintLayout c = findViewById(R.id.ConstLayout);
                c.removeAllViews();
                iniciar(c);
            }
        });
        builder.create().show();
    }

    private void actualizarMinasRestantes() {
        minasRestantes = ganar;
        actualizarTituloMenu();
    }

    private void actualizarTituloMenu() {
        if (minasRestantesMenuItem != null) {
            minasRestantesMenuItem.setTitle("" + minasRestantes);
        }
    }

    private void mostrarMinasRestantes() {
        minasRestantes = ganar;
        actualizarTituloMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        minasRestantesMenuItem = menu.findItem(R.id.minasrestantes);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bombas) {
            SpinnerBomba spinnerBombDialog = new SpinnerBomba();
            spinnerBombDialog.show(getSupportFragmentManager(), "Dialogo Spinner");
        } else if (item.getItemId() == R.id.configurar) {
            final String[] difficulties = {"Nivel Principiante", "Nivel Amateur", "Nivel Avanzado"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleccionar Dificultad");
            builder.setSingleChoiceItems(difficulties, dificultad, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dificultad = which;
                    Toast.makeText(MainActivity.this, "Dificultad seleccionada: " + difficulties[which], Toast.LENGTH_SHORT).show();
                }
            });
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Dificultad seleccionada: " + difficulties[dificultad], Toast.LENGTH_SHORT).show();
                    ConstraintLayout c = findViewById(R.id.ConstLayout);
                    c.removeAllViews();
                    iniciar(c);
                }
            });
            builder.create().show();
        } else if (item.getItemId() == R.id.instrucciones) {
            DialogInstruccion dialogIns = new DialogInstruccion();
            dialogIns.show(getSupportFragmentManager(), "Dialogo Instrucciones");
        } else if (item.getItemId() == R.id.reiniciar) {
            ConstraintLayout c = findViewById(R.id.ConstLayout);
            iniciar(c);
        } else if (item.getItemId() == R.id.minasrestantes) {
            mostrarMinasRestantes();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSkin(String mensaje) {
        if (mensaje.equals("bomba")) {
            bomba = R.drawable.bomba;
        } else if (mensaje.equals("azul")) {
            bomba = R.drawable.bomba_azul;
        } else if (mensaje.equals("granada")) {
            bomba = R.drawable.granada;
        } else if (mensaje.equals("molotov")) {
            bomba = R.drawable.molotov;
        } else if (mensaje.equals("atomica")) {
            bomba = R.drawable.bomba_atomica;
        }
        ConstraintLayout c = findViewById(R.id.ConstLayout);
        iniciar(c);
    }
}