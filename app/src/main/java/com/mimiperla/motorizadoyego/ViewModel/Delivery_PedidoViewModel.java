package com.mimiperla.motorizadoyego.ViewModel;

import android.app.Application;

import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonDeliveryPedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.mimiperla.motorizadoyego.Repository.Repositorio.Delivery_PedidoRepository;
import com.mimiperla.motorizadoyego.View.LoginUI.SessionPrefs;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class Delivery_PedidoViewModel extends AndroidViewModel {

    private Delivery_PedidoRepository mDelivery_pedidoRepository;
    private LiveData<GsonDelivery_Pedido> mDelivery_pedidoLiveData;

    private LiveData<GsonDeliveryPedido> mGsonDeliveryPedidoLiveData;

    public Delivery_PedidoViewModel(@NonNull Application application) {
        super(application);
    }

    private String token;

    public  void init(){
        token= SessionPrefs.get(getApplication()).getTokenPrefs();

        mDelivery_pedidoRepository= new Delivery_PedidoRepository();
        mDelivery_pedidoLiveData=mDelivery_pedidoRepository.getDelivery_PedidoDataLiveData();
        mGsonDeliveryPedidoLiveData=mDelivery_pedidoRepository.getGsonDeliveryPedidoMutableLiveData();


    }

    public void searchDelivery_PedidoById(int idRepartidor){

        mDelivery_pedidoRepository.searchDelivery_PedidoById(token,idRepartidor);
    }


    public LiveData<GsonDelivery_Pedido> getDelivery_PedidoLiveData(){
        return mDelivery_pedidoLiveData;
    }

    public void listDeliveryPedido(int idRepartidor) {
        mDelivery_pedidoRepository.listDeliveryPedido(token, idRepartidor);
    }

    public LiveData<GsonDeliveryPedido> getGsonDeliveryPedidoLiveData(){
        return mGsonDeliveryPedidoLiveData;
    }

}
