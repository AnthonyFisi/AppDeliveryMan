package com.example.motorizadoyego.Repository.Service;

import com.example.motorizadoyego.Repository.Modelo.Gson.GsonRepartidor_historial;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Repartidor_historialService {

    @GET("/RepartidorHistorialController/lista/{idrepartidor}/{idestadodelivery}")
    Call<GsonRepartidor_historial> listaHistorial(@Header("authorization") String token, @Path("idrepartidor")int idrepartidor, @Path("idestadodelivery") int idestadodelivery);

}
