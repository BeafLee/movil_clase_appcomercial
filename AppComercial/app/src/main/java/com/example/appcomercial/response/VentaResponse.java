package com.example.appcomercial.response;

import com.example.appcomercial.model.Venta;

public class VentaResponse {
    private Venta data;
    private String message;
    private boolean status;

    public Venta getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
