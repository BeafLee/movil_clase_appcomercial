package com.example.appcomercial.response;
import com.example.appcomercial.model.Ciudad;
public class CiudadListadoResponse {
    private Ciudad data[];
    private String message;
    private boolean status;

    public Ciudad[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
