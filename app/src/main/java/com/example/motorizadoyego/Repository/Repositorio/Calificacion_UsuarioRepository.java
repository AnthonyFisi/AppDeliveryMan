package com.example.motorizadoyego.Repository.Repositorio;

import com.example.motorizadoyego.Repository.Modelo.Calificacion_Servicio;
import com.example.motorizadoyego.Repository.Modelo.Calificacion_Usuario;
import com.example.motorizadoyego.Repository.Service.Calificacion_ServicioService;
import com.example.motorizadoyego.Repository.Service.Calificacion_UsuarioService;
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

public class Calificacion_UsuarioRepository {


    private Calificacion_UsuarioService mCalificacion_usuarioService;

    private MutableLiveData<Calificacion_Usuario> mCalificacion_usuarioMutableLiveData;

    public Calificacion_UsuarioRepository(){
        mCalificacion_usuarioMutableLiveData = new MutableLiveData<>();
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client= new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(UrlBase.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mCalificacion_usuarioService= retrofit.create(Calificacion_UsuarioService.class);
    }

    public void agregarCalificacion(String token,Calificacion_Usuario calificacion){
        mCalificacion_usuarioService.agregarCalificacion(token,calificacion).enqueue(new Callback<Calificacion_Usuario>() {
            @Override
            public void onResponse(Call<Calificacion_Usuario> call, Response<Calificacion_Usuario> response) {
                if(response.code()==200 && response.body()!=null){
                    mCalificacion_usuarioMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Calificacion_Usuario> call, Throwable t) {
                mCalificacion_usuarioMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<Calificacion_Usuario> getCalificacion_UsuarioDataLiveData(){
        return  mCalificacion_usuarioMutableLiveData;
    }
}
