package com.example.proyecto_palabrasdesordenadas;

public class Trofeo {
    private String titulo;
    private String objeto3DId;
    private String descripcion;

    public Trofeo(String titulo, String objeto3DId, String descripcion) {
        this.titulo = titulo;
        this.objeto3DId = objeto3DId;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getObjeto3DId() {
        return objeto3DId;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
