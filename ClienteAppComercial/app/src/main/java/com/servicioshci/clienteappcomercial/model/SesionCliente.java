package com.servicioshci.clienteappcomercial.model;

import com.google.gson.annotations.SerializedName;

public class SesionCliente {
    private String dni;
    private String email;
    @SerializedName("estado_cliente")
    private String estadoCliente;
    private int id;
    private String nombre;
    private String token;

    /*Declarar un atributo para almacenar los datos de la sesión*/
    public static SesionCliente DATOS_SESION;
    /*Declarar un atributo para almacenar los datos de la sesión*/

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstadoCliente() {
        return estadoCliente;
    }

    public void setEstadoCliente(String estadoCliente) {
        this.estadoCliente = estadoCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
