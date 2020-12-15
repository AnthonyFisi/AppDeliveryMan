package com.example.motorizadoyego.Repository.Service;

import com.example.motorizadoyego.Repository.Modelo.Usuario;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioService {

    @POST("/UsuarioController/updateCelular/{idusuario}/{celular}")
    Call<Usuario> updateCelular(@Header("Authorization") String auth, @Path("idusuario") int idusuario, @Path("celular") String celular);
}
