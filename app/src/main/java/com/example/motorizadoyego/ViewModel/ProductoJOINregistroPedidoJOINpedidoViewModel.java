package com.example.motorizadoyego.ViewModel;

import android.app.Application;

import com.example.motorizadoyego.Repository.Modelo.Gson.GsonProductoJOINregistroPedidoJOINpedido;
import com.example.motorizadoyego.Repository.Repositorio.ProductoJOINregistroPedidoJOINpedidoRepository;
import com.example.motorizadoyego.View.LoginUI.SessionPrefs;

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
