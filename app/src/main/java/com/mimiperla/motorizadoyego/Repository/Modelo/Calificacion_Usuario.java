package com.mimiperla.motorizadoyego.Repository.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Calificacion_Usuario {

    @SerializedName("idcalificacion_usuario")
    @Expose
    private int idcalificacion_usuario;

    @SerializedName("idventa")
    @Expose
    private int idventa;


    @SerializedName("idusuario")
    @Expose
    private int idusuario;

    @SerializedName("calificacion")
    @Expose
    private float calificacion;



    public int getIdcalificacion_usuario() {
        return idcalificacion_usuario;
    }

    public void setIdcalificacion_usuario(int idcalificacion_usuario) {
        this.idcalificacion_usuario = idcalificacion_usuario;
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
