package com.example.appcomercial.response;

import com.example.appcomercial.model.Serie;

public class SerieListarResponse {
    private Serie data[];
    private String message;
    private boolean status;

    public Serie[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
