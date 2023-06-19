package com.mimiperla.motorizadoyego.Repository.Modelo.Gson;

import com.mimiperla.motorizadoyego.Repository.Modelo.Orden_estado_delivery;
import com.mimiperla.motorizadoyego.Repository.Modelo.ProductoJOINregistroPedidoJOINpedido;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistorialDetailGson {


    @SerializedName("listaProducto")
    @Expose
    private List<ProductoJOINregistroPedidoJOINpedido> listaProducto;


    @SerializedName("listaEstados")
    @Expose
    private List<Orden_estado_delivery> listaEstados;


    @SerializedName("ubicacionEmpresa")
    @Expose
    private String ubicacionEmpresa;

    @SerializedName("ubicacionUsuario")
    @Expose
    private String ubicacionUsuario;

    public List<ProductoJOINregistroPedidoJOINpedido> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(List<ProductoJOINregistroPedidoJOINpedido> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public List<Orden_estado_delivery> getListaEstados() {
        return listaEstados;
    }

    public void setListaEstados(List<Orden_estado_delivery> listaEstados) {
        this.listaEstados = listaEstados;
    }

    public String getUbicacionEmpresa() {
        return ubicacionEmpresa;
    }

    public void setUbicacionEmpresa(String ubicacionEmpresa) {
        this.ubicacionEmpresa = ubicacionEmpresa;
    }

    public String getUbicacionUsuario() {
        return ubicacionUsuario;
    }

    public void setUbicacionUsuario(String ubicacionUsuario) {
        this.ubicacionUsuario = ubicacionUsuario;
    }
}
