package com.mimiperla.motorizadoyego.Repository.Service;

import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonDeliveryPedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface Delivery_PedidoService {


    @GET("/DeliveryPedido/Repartidor/{idRepartidor}")
    Call<GsonDelivery_Pedido> searchDelivery_PedidoById(@Header("authorization")String auth, @Path("idRepartidor") int idRepartidor);

    @GET("/DeliveryPedido/listaPedido/{idRepartidor}")
    Call<GsonDeliveryPedido> listDeliveryPedido(@Header("authorization")String auth, @Path("idRepartidor") int idRepartidor);


}
