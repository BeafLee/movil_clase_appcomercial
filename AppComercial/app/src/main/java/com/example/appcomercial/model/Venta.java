package com.example.appcomercial.model;

import com.google.gson.annotations.SerializedName;

public class Venta {
    @SerializedName("venta_id")
    private int ventaId;
    private int ndoc;
    private int serie;

    public int getVentaId() {
        return ventaId;
    }

    public void setVentaId(final int ventaId) {
        this.ventaId = ventaId;
    }

    public int getNdoc() {
        return ndoc;
    }

    public void setNdoc(final int ndoc) {
        this.ndoc = ndoc;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(final int serie) {
        this.serie = serie;
    }
}
