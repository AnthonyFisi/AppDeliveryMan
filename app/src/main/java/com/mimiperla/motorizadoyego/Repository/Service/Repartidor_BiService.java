package com.mimiperla.motorizadoyego.Repository.Service;

import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_Bi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface Repartidor_BiService {

    @GET("/RepartidorBiController/informacion/{idusuario}")
    Call<Repartidor_Bi> informacionBasica(@Header("authorization")String auth, @Path("idusuario")int idusuario);


}
