package com.example.motorizadoyego.Repository.Repositorio;

import com.example.motorizadoyego.Repository.Modelo.Gson.GsonRepartidor_historial;
import com.example.motorizadoyego.Repository.Modelo.Repartidor_historial;
import com.example.motorizadoyego.Repository.Service.Repartidor_historialService;
import com.example.motorizadoyego.Repository.UrlBase;

import androidx.lifecycle.MutableLiveData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repartidor_historialRepository {


    private Repartidor_historialService mRepartidor_historialService;

    private MutableLiveData<GsonRepartidor_historial> mRepartidor_historialMutableLiveData;

    public Repartidor_historialRepository(){
        mRepartidor_historialMutableLiveData= new  MutableLiveData<>();
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client= new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(UrlBase.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRepartidor_historialService= retrofit.create(Repartidor_historialService.class);
    }

    public void listaHistorial( String token,int idrepartidor,int idestadodelivery){
        mRepartidor_historialService.listaHistorial(token, idrepartidor, idestadodelivery).enqueue(new Callback<GsonRepartidor_historial>() {
            @Override
            public void onResponse(Call<GsonRepartidor_historial> call, Response<GsonRepartidor_historial> response) {
                if(response.code()==200 && response.body()!=null){
                    mRepartidor_historialMutableLiveData.postValue(response.body());
                }else {
                    mRepartidor_historialMutableLiveData.postValue(null);

                }
            }

            @Override
            public void onFailure(Call<GsonRepartidor_historial> call, Throwable t) {

            }
        });
    }


    public MutableLiveData<GsonRepartidor_historial> getRepartidor_historialMutableLiveData(){
        return mRepartidor_historialMutableLiveData;
    }
}
