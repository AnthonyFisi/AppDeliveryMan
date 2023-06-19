package com.mimiperla.motorizadoyego.Repository.Service;

import com.mimiperla.motorizadoyego.Repository.Modelo.Calificacion_Servicio;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Calificacion_ServicioService {

    @POST("/CalificacionServicioController/agregar")
    Call<Calificacion_Servicio> agregarCalificacion(@Header("authorization")String auth, @Body Calificacion_Servicio calificacion);
}
