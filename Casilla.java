package es.riberadeltajo.buscaminas_davidpascual;

import android.util.Log;

public class Casilla {
    private int filas;

    private int minas;
    private int columnas;
    private int[][] tablero;
    private int tipoImagen;

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public Casilla(int minas, int columnas, int filas) {
        this.minas = minas;
        this.columnas = columnas;
        this.filas = filas;
        this.tipoImagen = R.drawable.bomba;
    }

    public void iniciar() {
        tablero = new int[this.filas][this.columnas];
        for (int i = 0; i < this.filas; i++) {
            for (int j = 0; j < this.columnas; j++) {
                tablero[i][j] = 0;
            }
        }
        ponerMinas();
        asignarValores();
    }

    public void ponerMinas() {
        for (int i = 0; i < this.minas; i++) {
            int f = (int) (Math.random() * this.getFilas());
            int c = (int) (Math.random() * this.getColumnas());
            if (tablero[f][c] == -1) {
                i--;
            } else {
                tablero[f][c] = -1;
            }
        }
    }

    public void asignarValores() {
        for (int fila = 0; fila < this.filas; fila++) {
            for (int columna = 0; columna < this.columnas; columna++) {
                if (tablero[fila][columna] == 0) {
                    for (int filaAux = -1; filaAux < 2; filaAux++) {
                        for (int columnaAux = -1; columnaAux < 2; columnaAux++) {
                            try {
                                if (tablero[fila + filaAux][columna + columnaAux] == -1) {
                                    tablero[fila][columna]++;
                                }
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                Log.i("Log info", "Posición fuera de rango");
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean esMina(int i, int j) {
        if (tablero[i][j] == -1) {
            return true;
        }
        return false;
    }
}