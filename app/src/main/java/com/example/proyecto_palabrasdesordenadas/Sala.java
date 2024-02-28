package com.example.proyecto_palabrasdesordenadas;

import java.util.HashMap;
import java.util.List;

public class Sala {
    private String nombre;
    private String contrasena;
    private HashMap<String, Object> jugadores;
    public Sala() {
    }

    public Sala(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public HashMap<String, Object> getJugadores() {
        return jugadores;
    }

    public void setJugadores(HashMap<String, Object> jugadores) {
        this.jugadores = jugadores;
    }
}
