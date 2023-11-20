package com.example.appcomercial.response;

import com.example.appcomercial.model.Producto;

public class ProductoVentaResponse {
    private Producto data[];
    private String message;
    private boolean status;

    public Producto[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
