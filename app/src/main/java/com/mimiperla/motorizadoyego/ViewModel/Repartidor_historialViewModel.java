package com.mimiperla.motorizadoyego.ViewModel;

import android.app.Application;

import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonRepartidor_historial;
import com.mimiperla.motorizadoyego.Repository.Repositorio.Repartidor_historialRepository;
import com.mimiperla.motorizadoyego.View.LoginUI.SessionPrefs;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class Repartidor_historialViewModel extends AndroidViewModel {

    private Repartidor_historialRepository mRepartidor_historialRepository;

    private LiveData<GsonRepartidor_historial> mRepartidor_historialLiveData;

    private String token;

    public Repartidor_historialViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        token= SessionPrefs.get(getApplication()).getTokenPrefs();
        mRepartidor_historialRepository=new Repartidor_historialRepository();
        mRepartidor_historialLiveData=mRepartidor_historialRepository.getRepartidor_historialMutableLiveData();
    }

    public void listaHistorial( int idrepartidor,int idestadodelivery){
        mRepartidor_historialRepository.listaHistorial(token,idrepartidor,idestadodelivery);
    }

    public LiveData<GsonRepartidor_historial> getRepartidor_historialLiveData(){
        return mRepartidor_historialLiveData;
    }
}
