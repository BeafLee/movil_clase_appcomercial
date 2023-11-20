package com.example.appcomercial.model;

public class Categoria {
    private int id;
    private String nombre;

    public Categoria() {
    }

    public Categoria(final int id, final String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }
}
