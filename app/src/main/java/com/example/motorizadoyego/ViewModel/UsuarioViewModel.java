package com.example.motorizadoyego.ViewModel;

import android.app.Application;

import com.example.motorizadoyego.Repository.Modelo.Usuario;
import com.example.motorizadoyego.Repository.Repositorio.UsuarioRepository;
import com.example.motorizadoyego.View.LoginUI.SessionPrefs;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UsuarioViewModel extends AndroidViewModel {

    private UsuarioRepository mUsuarioRepository;

    private LiveData<Usuario> mUsuarioLiveData;

    private String token;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
    }


    public void init(){
        token= SessionPrefs.get(getApplication()).getTokenPrefs();;
        mUsuarioRepository=new UsuarioRepository();
        mUsuarioLiveData=mUsuarioRepository.getUsuarioMutableLiveData();
    }

    public void updateCelular(int idusuario,String celular){
        mUsuarioRepository.updateCelular(token, idusuario, celular);
    }


    public LiveData<Usuario> getUsuarioLiveData(){
        return mUsuarioLiveData;
    }

    }
