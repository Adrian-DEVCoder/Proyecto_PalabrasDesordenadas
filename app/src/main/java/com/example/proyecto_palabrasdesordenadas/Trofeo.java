package com.example.proyecto_palabrasdesordenadas;

public class Trofeo {
    private String titulo;
    private String recurso;
    private String descripcion;

    public Trofeo() {
    }

    public Trofeo(String titulo, String recurso, String descripcion) {
        this.titulo = titulo;
        this.recurso = recurso;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getRecurso() {
        return recurso;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
