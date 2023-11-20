package com.example.appcomercial.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class Producto {
    private String categoria;
    private String foto;
    private int id;
    private String nombre;
    private double precio;
    private int stock;

    //Atributo para que el vendedor ingrese la cantidad a vender
    private int cantidad;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(final String categoria) {
        this.categoria = categoria;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(final String foto) {
        this.foto = foto;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(final double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(final int stock) {
        this.stock = stock;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(final int cantidad) {
        this.cantidad = cantidad;
    }

    public JSONObject getJSONItemProducto() {
        final JSONObject json = new JSONObject();
        try {
            json.put("producto_id", this.getId());
            json.put("cantidad", this.getCantidad());
            json.put("precio", this.getPrecio());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return json;
    }

}
