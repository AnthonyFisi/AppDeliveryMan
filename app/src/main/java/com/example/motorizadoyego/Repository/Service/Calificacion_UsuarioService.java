package com.example.motorizadoyego.Repository.Service;

import com.example.motorizadoyego.Repository.Modelo.Calificacion_Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Calificacion_UsuarioService {

    @POST("/CalificacionUsuarioController/agregar")
    Call<Calificacion_Usuario> agregarCalificacion(@Header("authorization")String auth, @Body Calificacion_Usuario calificacion);
}
