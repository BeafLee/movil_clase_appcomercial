package com.example.appcomercial.response;

public class ClienteInsertarReponse {
    private boolean status;
    private String data; //El WS devuelve "None"
    private String message;

    public boolean isStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
