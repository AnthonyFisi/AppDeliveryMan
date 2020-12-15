package com.example.motorizadoyego.Repository.Repositorio;

import com.example.motorizadoyego.Repository.Modelo.Orden_estado_delivery;
import com.example.motorizadoyego.Repository.Service.Orden_estado_deliveryService;
import com.example.motorizadoyego.Repository.UrlBase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Orden_estado_deliveryRepository {



    private Orden_estado_deliveryService orden_estado_deliveryService;

    private MutableLiveData<Orden_estado_delivery> gsonOrden_estado_deliveryMutableLiveData;

    public Orden_estado_deliveryRepository(){
        gsonOrden_estado_deliveryMutableLiveData=new MutableLiveData<>();
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client= new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(UrlBase.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orden_estado_deliveryService = retrofit.create(Orden_estado_deliveryService.class);
    }


    public void  updateEstado(String token,Orden_estado_delivery estado,int idUsuario) {
        orden_estado_deliveryService.updateEstadoDelivery(token,estado, idUsuario).enqueue(new Callback<Orden_estado_delivery>() {
            @Override
            public void onResponse(Call<Orden_estado_delivery> call, Response<Orden_estado_delivery> response) {
                if (response.body() != null && response.code() == 200) {

                    gsonOrden_estado_deliveryMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Orden_estado_delivery> call, Throwable t) {
                gsonOrden_estado_deliveryMutableLiveData.postValue(null);

            }
        });

    }


    public LiveData<Orden_estado_delivery> getListOrden_estado_deliveryLiveData(){
        return gsonOrden_estado_deliveryMutableLiveData;
    }

}
