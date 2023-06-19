package com.mimiperla.motorizadoyego.Repository.Modelo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Calificacion_Servicio {

    @SerializedName("idcalificacion_servicio")
    @Expose
    private int idcalificacion_servicio;

    @SerializedName("idventa")
    @Expose
    private int idventa;

    @SerializedName("idusuario")
    @Expose
    private int idusuario;

    @SerializedName("calificacion")
    @Expose
    private float calificacion;



    public int getIdcalificacion_servicio() {
        return idcalificacion_servicio;
    }

    public void setIdcalificacion_servicio(int idcalificacion_servicio) {
        this.idcalificacion_servicio = idcalificacion_servicio;
    }

    public int getIdventa() {
        return idventa;
    }

    public void setIdventa(int idventa) {
        this.idventa = idventa;
    }


    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }





}
