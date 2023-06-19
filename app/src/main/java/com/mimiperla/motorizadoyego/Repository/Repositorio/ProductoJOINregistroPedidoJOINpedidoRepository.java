package com.mimiperla.motorizadoyego.Repository.Repositorio;

import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonProductoJOINregistroPedidoJOINpedido;
import com.mimiperla.motorizadoyego.Repository.Service.ProductoJOINregistroPedidoJOINpedidoService;
import com.mimiperla.motorizadoyego.Repository.UrlBase;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductoJOINregistroPedidoJOINpedidoRepository {



    private ProductoJOINregistroPedidoJOINpedidoService productoJOINregistroPedidoJOINpedidoService;
    private MutableLiveData<GsonProductoJOINregistroPedidoJOINpedido> gsonProductoJOINregistroPedidoJOINpedidoMutableLiveData;

    public ProductoJOINregistroPedidoJOINpedidoRepository(){
        gsonProductoJOINregistroPedidoJOINpedidoMutableLiveData =new MutableLiveData<>();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        productoJOINregistroPedidoJOINpedidoService = new retrofit2.Retrofit.Builder()
                .baseUrl(UrlBase.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductoJOINregistroPedidoJOINpedidoService.class);

    }



    public void listaCarrito(String token,int idPedido){
        productoJOINregistroPedidoJOINpedidoService.listaCarrito(token,idPedido).enqueue(new Callback<GsonProductoJOINregistroPedidoJOINpedido>() {
            @Override
            public void onResponse(Call<GsonProductoJOINregistroPedidoJOINpedido> call, Response<GsonProductoJOINregistroPedidoJOINpedido> response) {
                if(response.body() !=null){

                    gsonProductoJOINregistroPedidoJOINpedidoMutableLiveData.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<GsonProductoJOINregistroPedidoJOINpedido> call, Throwable t) {

            }
        });
    }

    public LiveData<GsonProductoJOINregistroPedidoJOINpedido> getProductoJOINregistroPedidoJOINpedidoLiveData(){


        return gsonProductoJOINregistroPedidoJOINpedidoMutableLiveData;
    }

}
