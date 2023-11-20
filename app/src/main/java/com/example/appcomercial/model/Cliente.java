package com.example.appcomercial.model;

import com.google.gson.annotations.SerializedName;

public class Cliente {
    private int id;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String email;
    @SerializedName("ciudad_id")
    private int ciudadId;

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(final String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(final String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public int getCiudadId() {
        return ciudadId;
    }

    public void setCiudadId(final int ciudadId) {
        this.ciudadId = ciudadId;
    }
}
