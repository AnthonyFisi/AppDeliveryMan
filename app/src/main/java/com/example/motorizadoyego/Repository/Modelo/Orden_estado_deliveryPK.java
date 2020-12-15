package com.example.motorizadoyego.Repository.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Orden_estado_deliveryPK {

    @SerializedName("idventa")
    @Expose
    private int idventa;

    @SerializedName("idestado_delivery")
    @Expose
    private int idestado_delivery;


    public int getIdventa() {
        return idventa;
    }

    public void setIdventa(int idventa) {
        this.idventa = idventa;
    }

    public int getIdestado_delivery() {
        return idestado_delivery;
    }

    public void setIdestado_delivery(int idestado_delivery) {
        this.idestado_delivery = idestado_delivery;
    }
}
