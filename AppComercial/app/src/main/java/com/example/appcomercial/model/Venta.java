package com.example.appcomercial.model;

import com.google.gson.annotations.SerializedName;

public class Venta {
    @SerializedName("venta_id")
    private int ventaId;
    private int ndoc;
    private String serie;
    private double total;

    public double getTotal() {
        return total;
    }

    public void setTotal(final double total) {
        this.total = total;
    }

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

    public String getSerie() {
        return serie;
    }

    public void setSerie(final String serie) {
        this.serie = serie;
    }
}
