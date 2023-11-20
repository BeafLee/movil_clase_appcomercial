package com.servicioshci.clienteappcomercial.response;

import com.servicioshci.clienteappcomercial.model.SesionCliente;

public class SesionClienteResponse {
    private SesionCliente data;
    private String message;
    private boolean status;

    public SesionCliente getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
