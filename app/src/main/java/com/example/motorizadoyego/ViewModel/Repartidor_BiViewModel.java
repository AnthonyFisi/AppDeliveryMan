package com.example.motorizadoyego.ViewModel;

import android.app.Application;

import com.example.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.example.motorizadoyego.Repository.Repositorio.Repartidor_BiRepository;
import com.example.motorizadoyego.View.LoginUI.SessionPrefs;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class Repartidor_BiViewModel extends AndroidViewModel {

    private Repartidor_BiRepository mRepartidor_biRepository;

    private LiveData<Repartidor_Bi> mRepartidor_biLiveData;

    public Repartidor_BiViewModel(@NonNull Application application) {
        super(application);
    }

   // private String token;

    public void init(){

       // token= SessionPrefs.get(getApplication()).getTokenPrefs();

        mRepartidor_biRepository= new Repartidor_BiRepository();
        mRepartidor_biLiveData=mRepartidor_biRepository.getRepartidorLiveData();
    }

    public void informacionBasica(String token,int idusuario){
        mRepartidor_biRepository.informacionBasica(token,idusuario);
    }

    public LiveData<Repartidor_Bi> getRepartidor_biLiveData(){
        return mRepartidor_biLiveData;
    }
}
