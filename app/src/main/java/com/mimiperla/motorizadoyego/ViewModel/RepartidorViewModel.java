package com.mimiperla.motorizadoyego.ViewModel;

import android.app.Application;

import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor;
import com.mimiperla.motorizadoyego.Repository.Repositorio.RepartidorRepository;
import com.mimiperla.motorizadoyego.View.LoginUI.SessionPrefs;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RepartidorViewModel extends AndroidViewModel {

    private RepartidorRepository mRepartidorRepository;

    private LiveData<Repartidor> mRepartidorLiveData;


    public RepartidorViewModel(@NonNull Application application) {
        super(application);
    }


    public void init() {
        mRepartidorRepository = new RepartidorRepository();

        mRepartidorLiveData = mRepartidorRepository.getRepartidorMutableLiveData();


    }

    public void updateRepartidorState(int idRepartidor, boolean state) {

        String token = SessionPrefs.get(getApplication()).getTokenPrefs();
        mRepartidorRepository.updateRepartidorState(token, idRepartidor, state);
    }


    public  LiveData<Repartidor> getRepartidorLiveData(){
        return mRepartidorLiveData;
    }
}