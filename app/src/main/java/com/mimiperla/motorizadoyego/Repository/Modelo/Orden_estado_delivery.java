package com.mimiperla.motorizadoyego.Repository.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Orden_estado_delivery {

    @SerializedName("id")
    @Expose
    Orden_estado_deliveryPK id;

    @SerializedName("idrepartidor")
    @Expose
    private int idrepartidor;

    @SerializedName("detalle")
    @Expose
    private String detalle;

    @SerializedName("fecha")
    @Expose
    private Timestamp fecha;

    public Orden_estado_deliveryPK getId() {
        return id;
    }

    public void setId(Orden_estado_deliveryPK id) {
        this.id = id;
    }

    public int getIdrepartidor() {
        return idrepartidor;
    }

    public void setIdrepartidor(int idrepartidor) {
        this.idrepartidor = idrepartidor;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
