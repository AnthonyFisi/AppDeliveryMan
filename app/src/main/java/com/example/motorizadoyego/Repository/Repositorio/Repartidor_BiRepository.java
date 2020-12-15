package com.example.motorizadoyego.Repository.Repositorio;

import com.example.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.example.motorizadoyego.Repository.Service.Repartidor_BiService;
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

public class Repartidor_BiRepository {


    private Repartidor_BiService mRepartidor_biService;

    private MutableLiveData<Repartidor_Bi> mRepartidor_biMutableLiveData;

    public Repartidor_BiRepository()
    {
       mRepartidor_biMutableLiveData= new  MutableLiveData<>();
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client= new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(UrlBase.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRepartidor_biService= retrofit.create(Repartidor_BiService.class);
    }


    public void informacionBasica(String token,int idusuario){

        mRepartidor_biService.informacionBasica(token,idusuario).enqueue(new Callback<Repartidor_Bi>() {
            @Override
            public void onResponse(Call<Repartidor_Bi> call, Response<Repartidor_Bi> response) {
                if(response.body()!=null && response.code()==200){
                    mRepartidor_biMutableLiveData.postValue(response.body());
                }else {
                    mRepartidor_biMutableLiveData.postValue(null);

                }

            }

            @Override
            public void onFailure(Call<Repartidor_Bi> call, Throwable t) {
                mRepartidor_biMutableLiveData.postValue(null);

            }
        });
    }

    public LiveData<Repartidor_Bi> getRepartidorLiveData(){
        return mRepartidor_biMutableLiveData;
    }

}
