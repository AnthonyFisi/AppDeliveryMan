package com.example.motorizadoyego.Repository.Repositorio;

import com.example.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDeliveryPedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.example.motorizadoyego.Repository.Service.Delivery_PedidoService;
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

public class Delivery_PedidoRepository {


    private Delivery_PedidoService mDelivery_pedidoService;
    private MutableLiveData<GsonDelivery_Pedido> mDelivery_pedidoMutableLiveData;

    private MutableLiveData<GsonDeliveryPedido> mGsonDeliveryPedidoMutableLiveData;

    public Delivery_PedidoRepository(){
        mDelivery_pedidoMutableLiveData= new  MutableLiveData<>();
        mGsonDeliveryPedidoMutableLiveData=new MutableLiveData<>();

        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client= new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(UrlBase.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mDelivery_pedidoService= retrofit.create(Delivery_PedidoService.class);
    }

    public void searchDelivery_PedidoById(String token,int idRepartidor) {
        mDelivery_pedidoService.searchDelivery_PedidoById(token,idRepartidor).enqueue(new Callback<GsonDelivery_Pedido>() {
            @Override
            public void onResponse(Call<GsonDelivery_Pedido> call, Response<GsonDelivery_Pedido> response) {

                if(response.code()==500){
                    System.out.println("code 500");
                    mDelivery_pedidoMutableLiveData.postValue(null);

                }

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("code 200");

                    mDelivery_pedidoMutableLiveData.postValue(response.body());
                }



            }

            @Override
            public void onFailure(Call<GsonDelivery_Pedido> call, Throwable t) {
                System.out.println("code  FAILURE");

                mDelivery_pedidoMutableLiveData.postValue(null);

            }
        });

    }

    public LiveData<GsonDelivery_Pedido> getDelivery_PedidoDataLiveData(){
        return  mDelivery_pedidoMutableLiveData;
    }


    public void listDeliveryPedido(String token,int idRepartidor) {
        mDelivery_pedidoService.listDeliveryPedido(token, idRepartidor).enqueue(new Callback<GsonDeliveryPedido>() {
            @Override
            public void onResponse(Call<GsonDeliveryPedido> call, Response<GsonDeliveryPedido> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("code 200");

                    mGsonDeliveryPedidoMutableLiveData.postValue(response.body());
                }else {
                    mGsonDeliveryPedidoMutableLiveData.postValue(null);

                }
            }

            @Override
            public void onFailure(Call<GsonDeliveryPedido> call, Throwable t) {
                mGsonDeliveryPedidoMutableLiveData.postValue(null);

            }
        });
    }

    public MutableLiveData<GsonDeliveryPedido> getGsonDeliveryPedidoMutableLiveData(){
        return mGsonDeliveryPedidoMutableLiveData;
    }

}
