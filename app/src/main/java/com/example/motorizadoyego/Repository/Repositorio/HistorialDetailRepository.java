package com.example.motorizadoyego.Repository.Repositorio;

import com.example.motorizadoyego.Repository.Modelo.Gson.HistorialDetailGson;
import com.example.motorizadoyego.Repository.Service.HistorialDetailService;
import com.example.motorizadoyego.Repository.UrlBase;

import androidx.lifecycle.MutableLiveData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistorialDetailRepository {


    private HistorialDetailService mHistorialDetailService;

    private MutableLiveData<HistorialDetailGson> mHistorialDetailGsonMutableLiveData;

    public HistorialDetailRepository(){
        mHistorialDetailGsonMutableLiveData= new  MutableLiveData<>();
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client= new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(UrlBase.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mHistorialDetailService= retrofit.create(HistorialDetailService.class);
    }

    public void detailHistorial(String auth, int idempresa, int idpedido, int idventa,int idubicacion){
        mHistorialDetailService.detailHistorial(auth, idempresa, idpedido, idventa,idubicacion).enqueue(new Callback<HistorialDetailGson>() {
            @Override
            public void onResponse(Call<HistorialDetailGson> call, Response<HistorialDetailGson> response) {
                if(response.code()==200 && response.body()!=null){
                    mHistorialDetailGsonMutableLiveData.postValue(response.body());
                }else {
                    mHistorialDetailGsonMutableLiveData.postValue(null);

                }
            }

            @Override
            public void onFailure(Call<HistorialDetailGson> call, Throwable t) {
                mHistorialDetailGsonMutableLiveData.postValue(null);

            }
        });
    }

    public MutableLiveData<HistorialDetailGson> getHistorialDetailGsonMutableLiveData(){
        return mHistorialDetailGsonMutableLiveData;
    }
}
