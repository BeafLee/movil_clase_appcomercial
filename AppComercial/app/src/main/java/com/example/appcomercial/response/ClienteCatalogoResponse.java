package com.example.appcomercial.response;

import com.example.appcomercial.model.Cliente;

public class ClienteCatalogoResponse {
    private Cliente data[];
    private String message;
    private boolean status;

    public Cliente[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
