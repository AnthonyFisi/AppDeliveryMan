package com.example.motorizadoyego.Repository.Service;

import com.example.motorizadoyego.Repository.Modelo.Gson.GsonProductoJOINregistroPedidoJOINpedido;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ProductoJOINregistroPedidoJOINpedidoService {

    @GET("/ProductoJOINregistroPedidoJOINpedidoController/listaProductosAfterSell/{idPedido}")
    Call<GsonProductoJOINregistroPedidoJOINpedido> listaCarrito(@Header("authorization")String auth, @Path("idPedido") int idPedido);

}
