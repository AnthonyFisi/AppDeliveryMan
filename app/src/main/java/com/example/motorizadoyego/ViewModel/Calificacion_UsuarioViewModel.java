package com.example.motorizadoyego.ViewModel;

import android.app.Application;

import com.example.motorizadoyego.Repository.Modelo.Calificacion_Usuario;
import com.example.motorizadoyego.Repository.Repositorio.Calificacion_UsuarioRepository;
import com.example.motorizadoyego.View.LoginUI.SessionPrefs;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class Calificacion_UsuarioViewModel extends AndroidViewModel {

    private Calificacion_UsuarioRepository mCalificacion_usuarioRepository;

    private LiveData<Calificacion_Usuario> mCalificacion_usuarioLiveData;


    public Calificacion_UsuarioViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        mCalificacion_usuarioRepository= new Calificacion_UsuarioRepository();

        mCalificacion_usuarioLiveData=mCalificacion_usuarioRepository.getCalificacion_UsuarioDataLiveData();
    }

    public void agregarCalificacion(Calificacion_Usuario calificacion){
        String token= SessionPrefs.get(getApplication()).getTokenPrefs();

        mCalificacion_usuarioRepository.agregarCalificacion(token,calificacion);
    }

    public LiveData<Calificacion_Usuario> getCalificacion_usuarioLiveData(){
        return mCalificacion_usuarioLiveData;
    }

}
