package com.example.motorizadoyego.Repository.Repositorio;

import com.example.motorizadoyego.Repository.Modelo.JwtResponse;
import com.example.motorizadoyego.Repository.Modelo.LoginRequest;
import com.example.motorizadoyego.Repository.Service.AuthService;
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

public class AuthRepository {


    private AuthService mAuthService;

    private MutableLiveData<JwtResponse> mJwtResponseMutableLiveData;

    public AuthRepository(){

        mJwtResponseMutableLiveData= new MutableLiveData<>();
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client= new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(UrlBase.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mAuthService= retrofit.create(AuthService.class);
    }


    public void authenticationUser (LoginRequest loginRequest){
        mAuthService.authenticationUser(loginRequest).enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {

                if(response.code()==401){

                    System.out.println("ESOTY EN NULL");
                    mJwtResponseMutableLiveData.postValue(null);

                }

                if(response.body()!= null && response.code()==200){

                    mJwtResponseMutableLiveData.postValue(response.body());

                }

            }

            @Override
            public void onFailure(Call<JwtResponse> call, Throwable t) {

                mJwtResponseMutableLiveData.postValue(null);


            }
        });
    }


    public LiveData<JwtResponse>  getJwtResponse(){
        return mJwtResponseMutableLiveData;
    }


}
