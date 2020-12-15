package com.example.motorizadoyego.Repository.Service;

import com.example.motorizadoyego.Repository.Modelo.Gson.HistorialDetailGson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface HistorialDetailService {

    @GET("/HistorialDetailController/lista/{idempresa}/{idpedido}/{idventa}/{idubicacion}")
    Call<HistorialDetailGson> detailHistorial(@Header("authorization")String auth,
                                              @Path("idempresa") int idempresa,
                                              @Path("idpedido") int idpedido,
                                              @Path("idventa") int idventa,
                                              @Path("idubicacion") int idubicacion);
}
