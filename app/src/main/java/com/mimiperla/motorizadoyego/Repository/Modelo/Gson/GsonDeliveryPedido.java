package com.mimiperla.motorizadoyego.Repository.Modelo.Gson;

import com.mimiperla.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GsonDeliveryPedido implements Serializable {

    @SerializedName("listaDeliveryPedido")
    @Expose
    private List<Delivery_Pedido> listaDeliveryPedido;

    public List<Delivery_Pedido> getListaDeliveryPedido() {
        return listaDeliveryPedido;
    }

    public void setListaDeliveryPedido(List<Delivery_Pedido> listaDeliveryPedido) {
        this.listaDeliveryPedido = listaDeliveryPedido;
    }

}
