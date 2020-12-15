package com.example.motorizadoyego.Repository.Service;

import com.example.motorizadoyego.Repository.Modelo.JwtResponse;
import com.example.motorizadoyego.Repository.Modelo.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/api/auth/signin")
    Call<JwtResponse> authenticationUser(@Body LoginRequest loginRequest);
}
