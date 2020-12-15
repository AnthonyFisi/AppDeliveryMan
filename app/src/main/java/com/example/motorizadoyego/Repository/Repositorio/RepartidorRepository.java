package com.example.motorizadoyego.Repository.Repositorio;

import com.example.motorizadoyego.Repository.Modelo.Repartidor;
import com.example.motorizadoyego.Repository.Service.RepartidorService;
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

public class RepartidorRepository {


    private RepartidorService mRepartidorService;

    private MutableLiveData<Repartidor> mRepartidorMutableLiveData;

    public RepartidorRepository(){
        mRepartidorMutableLiveData= new  MutableLiveData<>();
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client= new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(UrlBase.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRepartidorService= retrofit.create(RepartidorService.class);
    }

    public void updateRepartidorState( String token, int idRepartidor, boolean state){

        mRepartidorService.updateRepartidorState(token,idRepartidor,state).enqueue(new Callback<Repartidor>() {
            @Override
            public void onResponse(Call<Repartidor> call, Response<Repartidor> response) {

                if(response.body()!=null && response.code()==200){
                    mRepartidorMutableLiveData.postValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<Repartidor> call, Throwable t) {

                mRepartidorMutableLiveData.postValue(null);
            }
        });

    }

    public LiveData<Repartidor> getRepartidorMutableLiveData(){
        return mRepartidorMutableLiveData;
    }
}
