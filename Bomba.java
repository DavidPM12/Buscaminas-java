package es.riberadeltajo.buscaminas_davidpascual;

public class Bomba {
    private String nombre;
    private int imagenbomba;

    public Bomba(String nombre, int imagenbomba) {
        this.nombre = nombre;
        this.imagenbomba = imagenbomba;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImageResource() {
        return imagenbomba;
    }
}