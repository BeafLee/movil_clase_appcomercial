package com.example.appcomercial.response;

import com.example.appcomercial.model.Categoria;

public class CategoriaListadoResponse {
    private Categoria data[];
    private String message;
    private boolean status;

    public Categoria[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
