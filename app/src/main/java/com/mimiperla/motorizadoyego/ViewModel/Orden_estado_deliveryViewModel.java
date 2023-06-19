package com.mimiperla.motorizadoyego.ViewModel;

import android.app.Application;


import com.mimiperla.motorizadoyego.Repository.Modelo.Orden_estado_delivery;
import com.mimiperla.motorizadoyego.Repository.Repositorio.Orden_estado_deliveryRepository;
import com.mimiperla.motorizadoyego.View.LoginUI.SessionPrefs;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class Orden_estado_deliveryViewModel extends AndroidViewModel {

    private Orden_estado_deliveryRepository orden_estado_deliveryRepository;

    private LiveData<Orden_estado_delivery> gsonOrden_estado_deliveryLiveData;

    public void init(){
        orden_estado_deliveryRepository= new Orden_estado_deliveryRepository();
        gsonOrden_estado_deliveryLiveData= orden_estado_deliveryRepository.getListOrden_estado_deliveryLiveData();

    }

    public Orden_estado_deliveryViewModel(@NonNull Application application) {
        super(application);
    }

    public void   updateEstado(Orden_estado_delivery estado, int idUsuario){
        String token= SessionPrefs.get(getApplication()).getTokenPrefs();

        orden_estado_deliveryRepository.updateEstado(token,estado,idUsuario);
    }

    public  LiveData<Orden_estado_delivery> getOrden_estado_deliveryLiveData(){
        return  gsonOrden_estado_deliveryLiveData;
    }
}
