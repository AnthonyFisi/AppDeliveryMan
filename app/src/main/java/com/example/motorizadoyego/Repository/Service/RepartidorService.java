package com.example.motorizadoyego.Repository.Service;

import com.example.motorizadoyego.Repository.Modelo.Repartidor;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RepartidorService {

    @POST("/repartidorController/disponibilidad/{idRepartidor}/{state}")
    Call<Repartidor> updateRepartidorState(@Header("authorization") String token, @Path("idRepartidor")int idRepartidor,@Path("state") boolean state);

}
