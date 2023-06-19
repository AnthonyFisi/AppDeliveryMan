package com.mimiperla.motorizadoyego.Repository.Service;

import com.mimiperla.motorizadoyego.Repository.Modelo.Orden_estado_delivery;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Orden_estado_deliveryService {

    @POST("/Orden_Estado_DeliveryController/aceptar/{idUsuario}")
    Call<Orden_estado_delivery> updateEstadoDelivery(@Header("authorization")String auth, @Body Orden_estado_delivery estado, @Path("idUsuario") int idUsuario);

}