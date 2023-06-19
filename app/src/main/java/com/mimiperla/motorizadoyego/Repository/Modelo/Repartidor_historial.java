package com.mimiperla.motorizadoyego.Repository.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;

public class Repartidor_historial implements Serializable {

    @SerializedName("idventa")
    @Expose
    private int idventa;

    @SerializedName("idtipopago")
    @Expose
    private int idtipopago;

    @SerializedName("tipopago_nombre")
    @Expose
    private String tipopago_nombre;

    @SerializedName("idubicacion")
    @Expose
    private int idubicacion;

    @SerializedName("idpedido")
    @Expose
    private int idpedido;

    @SerializedName("idempresa")
    @Expose
    private int idempresa;

    @SerializedName("pedido_cantidadtotal")
    @Expose
    private int pedido_cantidadtotal;

    @SerializedName("idusuario")
    @Expose
    private int idusuario;

    @SerializedName("idusuariogeneral")
    @Expose
    private int idusuariogeneral;

    @SerializedName("venta_costodelivery")
    @Expose
    private float venta_costodelivery;

    @SerializedName("venta_costototal")
    @Expose
    private float venta_costototal;


    @SerializedName("idestado_pago")
    @Expose
    private int idestado_pago;

    @SerializedName("nombre_estadopago")
    @Expose
    private String nombre_estadopago;


    @SerializedName("ordendisponible")
    @Expose
    private boolean ordendisponible;

    @SerializedName("tiempo_espera")
    @Expose
    private String tiempo_espera;

    @SerializedName("idrepartidor")
    @Expose
    private int idrepartidor;

    @SerializedName("cancelar")
    @Expose
    private boolean cancelar;

    @SerializedName("comentario_cancelar")
    @Expose
    private boolean comentario_cancelar;

    @SerializedName("tiempo_aprox_delivery")
    @Expose
    private String tiempo_aprox_delivery;

    @SerializedName("distancia_delivery")
    @Expose
    private String distancia_delivery;

    @SerializedName("fechahistorial")
    @Expose
    private Timestamp fechahistorial;

    @SerializedName("idestadoedelivery")
    @Expose
    private int  idestadodelivery;

    @SerializedName("venta_distancia_delivery_total")
    @Expose
    private String venta_distancia_delivery_total;

    @SerializedName("ganancia_delivery")
    @Expose
    private float ganancia_delivery;


    public int getIdventa() {
        return idventa;
    }

    public void setIdventa(int idventa) {
        this.idventa = idventa;
    }

    public int getIdtipopago() {
        return idtipopago;
    }

    public void setIdtipopago(int idtipopago) {
        this.idtipopago = idtipopago;
    }

    public String getTipopago_nombre() {
        return tipopago_nombre;
    }

    public void setTipopago_nombre(String tipopago_nombre) {
        this.tipopago_nombre = tipopago_nombre;
    }

    public int getIdubicacion() {
        return idubicacion;
    }

    public void setIdubicacion(int idubicacion) {
        this.idubicacion = idubicacion;
    }

    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
    }

    public int getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(int idempresa) {
        this.idempresa = idempresa;
    }

    public int getPedido_cantidadtotal() {
        return pedido_cantidadtotal;
    }

    public void setPedido_cantidadtotal(int pedido_cantidadtotal) {
        this.pedido_cantidadtotal = pedido_cantidadtotal;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getIdusuariogeneral() {
        return idusuariogeneral;
    }

    public void setIdusuariogeneral(int idusuariogeneral) {
        this.idusuariogeneral = idusuariogeneral;
    }

    public float getVenta_costodelivery() {
        return venta_costodelivery;
    }

    public void setVenta_costodelivery(float venta_costodelivery) {
        this.venta_costodelivery = venta_costodelivery;
    }

    public float getVenta_costototal() {
        return venta_costototal;
    }

    public void setVenta_costototal(float venta_costototal) {
        this.venta_costototal = venta_costototal;
    }

    public int getIdestado_pago() {
        return idestado_pago;
    }

    public void setIdestado_pago(int idestado_pago) {
        this.idestado_pago = idestado_pago;
    }

    public String getNombre_estadopago() {
        return nombre_estadopago;
    }

    public void setNombre_estadopago(String nombre_estadopago) {
        this.nombre_estadopago = nombre_estadopago;
    }

    public boolean isOrdendisponible() {
        return ordendisponible;
    }

    public void setOrdendisponible(boolean ordendisponible) {
        this.ordendisponible = ordendisponible;
    }

    public String getTiempo_espera() {
        return tiempo_espera;
    }

    public void setTiempo_espera(String tiempo_espera) {
        this.tiempo_espera = tiempo_espera;
    }

    public int getIdrepartidor() {
        return idrepartidor;
    }

    public void setIdrepartidor(int idrepartidor) {
        this.idrepartidor = idrepartidor;
    }

    public boolean isCancelar() {
        return cancelar;
    }

    public void setCancelar(boolean cancelar) {
        this.cancelar = cancelar;
    }

    public boolean isComentario_cancelar() {
        return comentario_cancelar;
    }

    public void setComentario_cancelar(boolean comentario_cancelar) {
        this.comentario_cancelar = comentario_cancelar;
    }

    public String getTiempo_aprox_delivery() {
        return tiempo_aprox_delivery;
    }

    public void setTiempo_aprox_delivery(String tiempo_aprox_delivery) {
        this.tiempo_aprox_delivery = tiempo_aprox_delivery;
    }

    public String getDistancia_delivery() {
        return distancia_delivery;
    }

    public void setDistancia_delivery(String distancia_delivery) {
        this.distancia_delivery = distancia_delivery;
    }

    public Timestamp getFechahistorial() {
        return fechahistorial;
    }

    public void setFechahistorial(Timestamp fechahistorial) {
        this.fechahistorial = fechahistorial;
    }

    public int getIdestadodelivery() {
        return idestadodelivery;
    }

    public void setIdestadodelivery(int idestadodelivery) {
        this.idestadodelivery = idestadodelivery;
    }

    public String getVenta_distancia_delivery_total() {
        return venta_distancia_delivery_total;
    }

    public void setVenta_distancia_delivery_total(String venta_distancia_delivery_total) {
        this.venta_distancia_delivery_total = venta_distancia_delivery_total;
    }

    public float getGanancia_delivery() {
        return ganancia_delivery;
    }

    public void setGanancia_delivery(float ganancia_delivery) {
        this.ganancia_delivery = ganancia_delivery;
    }
}
