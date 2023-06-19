package com.mimiperla.motorizadoyego.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import com.mimiperla.motorizadoyego.MainActivity;
import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.JwtResponse;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.mimiperla.motorizadoyego.Util.MyApp;
import com.mimiperla.motorizadoyego.Util.MyReceiver;
import com.mimiperla.motorizadoyego.View.LoginUI.NetworkActivity;
import com.mimiperla.motorizadoyego.View.LoginUI.SessionPrefs;
import com.mimiperla.motorizadoyego.View.LoginUI.ui.login.LoginActivity;
import com.mimiperla.motorizadoyego.ViewModel.Repartidor_BiViewModel;

public class SplashActivity extends AppCompatActivity implements MyReceiver.ConnectivityReciverListener {

    private boolean response=false;

    private Repartidor_BiViewModel viewModel;

    private JwtResponse mJwtResponse;

    private boolean responseDataLoad = false;

    private  Repartidor_Bi mRepartidor_bi;

    boolean isConnected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewModel= new ViewModelProvider(this).get(Repartidor_BiViewModel.class);
        viewModel.init();

        checkInternetConnection();


        if (SessionPrefs.get(this).isLoggedIn()) {

            response=true;
            mJwtResponse=SessionPrefs.get(this).data();
            viewModel.informacionBasica(SessionPrefs.get(SplashActivity.this).getTokenPrefs(),mJwtResponse.getId().intValue());

            loadData();

        }else{

            new Handler().postDelayed(() -> {
                Intent intent= LoginActivity.newIntentLoginActivity(getApplicationContext());
                startActivity(intent);
                finish();
            },3000);

        }

        loadData();

    }



    private void loadData(){


        if(response){

            viewModel.getRepartidor_biLiveData().observe(this, new Observer<Repartidor_Bi>() {
                @Override
                public void onChanged(Repartidor_Bi repartidor_bi) {

                    if(isConnected){

                        if(repartidor_bi !=null){

                            //VA IR DIRECTAMENTE AL HOME DE LA APLICACION

                            Repartidor_Bi.repartidor_bi=repartidor_bi;

                            Intent intent= MainActivity.newIntentMainActivity(SplashActivity.this);
                            startActivity(intent);
                            finish();

                        }else {

                            Intent intent= LoginActivity.newIntentLoginActivity(getApplicationContext());
                            startActivity(intent);
                            finish();

                        }

                    }else {

                        Intent intent= new Intent(getApplicationContext(), NetworkActivity.class);
                        startActivity(intent);
                        finish();

                    }



                }
            });

        }

    }
    private void checkInternetConnection() {

       isConnected=MyReceiver.isConnected();

    }

    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter intentFilter= new IntentFilter();

        intentFilter.addAction(ConnectivityManager.EXTRA_CAPTIVE_PORTAL);

        MyReceiver myReciver= new MyReceiver();

        registerReceiver(myReciver,intentFilter);

        MyApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected=isConnected;
    }
}
