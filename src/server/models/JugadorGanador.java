package server.models;

import java.io.Serializable;

public class JugadorGanador implements Serializable {
    private String nombre;
    private int puntos;

    public JugadorGanador(String nombre, int puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntos() {
        return puntos;
    }
}
