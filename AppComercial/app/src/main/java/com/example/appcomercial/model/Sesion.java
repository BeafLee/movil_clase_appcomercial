package com.example.appcomercial.model;

import com.google.gson.annotations.SerializedName;

public class Sesion {
    private int almacen_id;
    private String email;
    private String estado_usuario;
    private int id;
    private String imagen;
    private String nombre;
    private String token;
    @SerializedName("porcentaje_igv")
    private double porcentajeIgv;

    /*Declarar un atributo para almacenar los datos de la sesión*/
    public static Sesion DATOS_SESION;
    /*Declarar un atributo para almacenar los datos de la sesión*/


    public double getPorcentajeIgv() {
        return porcentajeIgv;
    }

    public void setPorcentajeIgv(final double porcentajeIgv) {
        this.porcentajeIgv = porcentajeIgv;
    }

    public int getAlmacen_id() {
        return almacen_id;
    }

    public void setAlmacen_id(final int almacen_id) {
        this.almacen_id = almacen_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getEstado_usuario() {
        return estado_usuario;
    }

    public void setEstado_usuario(final String estado_usuario) {
        this.estado_usuario = estado_usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(final String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}

