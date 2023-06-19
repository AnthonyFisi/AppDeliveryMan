package com.mimiperla.motorizadoyego.ViewModel;

import android.app.Application;

import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.HistorialDetailGson;
import com.mimiperla.motorizadoyego.Repository.Repositorio.HistorialDetailRepository;
import com.mimiperla.motorizadoyego.View.LoginUI.SessionPrefs;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class HistorialDetailViewModel extends AndroidViewModel {

    private HistorialDetailRepository mHistorialDetailRepository;

    private LiveData<HistorialDetailGson> mHistorialDetailGsonLiveData;

    private String token;

    public HistorialDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        token= SessionPrefs.get(getApplication()).getTokenPrefs();
        mHistorialDetailRepository=new HistorialDetailRepository();
        mHistorialDetailGsonLiveData=mHistorialDetailRepository.getHistorialDetailGsonMutableLiveData();
    }

    public void detailHistorial( int idempresa, int idpedido, int idventa,int idubicacion){
        mHistorialDetailRepository.detailHistorial(token, idempresa, idpedido, idventa,idubicacion);
    }

    public  LiveData<HistorialDetailGson> getHistorialDetailGsonLiveData(){
        return mHistorialDetailGsonLiveData;
    }
}
