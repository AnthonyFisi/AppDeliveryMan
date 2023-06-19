package com.mimiperla.motorizadoyego.Repository.Modelo.Gson;

import com.mimiperla.motorizadoyego.Repository.Modelo.ProductoJOINregistroPedidoJOINpedido;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonProductoJOINregistroPedidoJOINpedido {

    @SerializedName("listaProductoJOINregistroPedidoJOINpedido")
    @Expose
    List<ProductoJOINregistroPedidoJOINpedido> listaProductoJOINregistroPedidoJOINpedido;

    public List<ProductoJOINregistroPedidoJOINpedido> getListaProductoJOINregistroPedidoJOINpedido() {
        return listaProductoJOINregistroPedidoJOINpedido;
    }

    public void setListaProductoJOINregistroPedidoJOINpedido(List<ProductoJOINregistroPedidoJOINpedido> listaProductoJOINregistroPedidoJOINpedido) {
        this.listaProductoJOINregistroPedidoJOINpedido = listaProductoJOINregistroPedidoJOINpedido;
    }
}
