package com.example.motorizadoyego.ViewModel;

import android.app.Application;

import com.example.motorizadoyego.Repository.Modelo.JwtResponse;
import com.example.motorizadoyego.Repository.Modelo.LoginRequest;
import com.example.motorizadoyego.Repository.Repositorio.AuthRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepository mAuthRepository;

    private LiveData<JwtResponse> mJwtResponseLiveData;

    public AuthViewModel(@NonNull Application application) {
        super(application);
    }


    public void init(){
        mAuthRepository= new AuthRepository();
        mJwtResponseLiveData= mAuthRepository.getJwtResponse();

    }

    public void authenticationUser (LoginRequest loginRequest){
        mAuthRepository.authenticationUser(loginRequest);
    }

    public LiveData<JwtResponse> getJwtResponseLiveData(){
        return mJwtResponseLiveData;
    }
}
