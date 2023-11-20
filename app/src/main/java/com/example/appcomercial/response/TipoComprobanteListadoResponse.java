package com.example.appcomercial.response;

import com.example.appcomercial.model.TipoComprobante;

public class TipoComprobanteListadoResponse {
    private TipoComprobante data[];
    private String message;
    private boolean status;

    public TipoComprobante[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
