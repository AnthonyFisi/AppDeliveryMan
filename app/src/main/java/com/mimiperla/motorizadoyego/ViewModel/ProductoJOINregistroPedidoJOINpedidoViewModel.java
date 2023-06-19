package com.mimiperla.motorizadoyego.ViewModel;

import android.app.Application;

import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonProductoJOINregistroPedidoJOINpedido;
import com.mimiperla.motorizadoyego.Repository.Repositorio.ProductoJOINregistroPedidoJOINpedidoRepository;
import com.mimiperla.motorizadoyego.View.LoginUI.SessionPrefs;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ProductoJOINregistroPedidoJOINpedidoViewModel extends AndroidViewModel {

    private ProductoJOINregistroPedidoJOINpedidoRepository productoJOINregistroPedidoJOINpedidoRepository;
    private LiveData<GsonProductoJOINregistroPedidoJOINpedido> gsonProductoJOINregistroPedidoJOINpedidoLiveData;

    public ProductoJOINregistroPedidoJOINpedidoViewModel(@NonNull Application application) {
        super(application);
    }


    public void init() {

        productoJOINregistroPedidoJOINpedidoRepository = new ProductoJOINregistroPedidoJOINpedidoRepository();
        gsonProductoJOINregistroPedidoJOINpedidoLiveData = productoJOINregistroPedidoJOINpedidoRepository.getProductoJOINregistroPedidoJOINpedidoLiveData();
    }


    public void listaCarrito(int idPedido) {
        String token= SessionPrefs.get(getApplication()).getTokenPrefs();

       productoJOINregistroPedidoJOINpedidoRepository.listaCarrito(token,idPedido);
    }


    public LiveData<GsonProductoJOINregistroPedidoJOINpedido> getProductoJOINregistroPedidoJOINpedidoLiveData() {

        return gsonProductoJOINregistroPedidoJOINpedidoLiveData;
    }
}
