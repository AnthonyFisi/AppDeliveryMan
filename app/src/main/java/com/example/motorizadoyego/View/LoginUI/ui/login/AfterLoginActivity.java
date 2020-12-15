package com.example.motorizadoyego.View.LoginUI.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.motorizadoyego.MainActivity;
import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.JwtResponse;
import com.example.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.example.motorizadoyego.View.LoginUI.SessionPrefs;
import com.example.motorizadoyego.ViewModel.Repartidor_BiViewModel;

public class AfterLoginActivity extends AppCompatActivity {

    private Repartidor_BiViewModel viewModel;

    private Repartidor_Bi mRepartidor_bi;

    private ProgressBar loadingProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        loadingProgressBar=findViewById(R.id.loading_after_login_activity);

        loadingProgressBar.setVisibility(View.VISIBLE);


        loadData();



    }


    private void loadData(){

            viewModel= new ViewModelProvider(this).get(Repartidor_BiViewModel.class);
            viewModel.init();
            viewModel.getRepartidor_biLiveData().observe(this, new Observer<Repartidor_Bi>() {
                @Override
                public void onChanged(Repartidor_Bi repartidor_bi) {

                    if(repartidor_bi !=null){

                        loadingProgressBar.setVisibility(View.VISIBLE);

                        Repartidor_Bi.repartidor_bi=repartidor_bi;

                        mRepartidor_bi= repartidor_bi;

                        Intent intent=MainActivity.newIntentMainActivity(getApplicationContext());
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(),"Intentelo nuevamente",Toast.LENGTH_SHORT).show();

                    }

                }
            });


        JwtResponse mJwtResponse= SessionPrefs.get(this).data();
        String token=mJwtResponse.getTokenType()+" "+mJwtResponse.getAccessToken();
        viewModel.informacionBasica(SessionPrefs.get(AfterLoginActivity.this).getTokenPrefs(),mJwtResponse.getId().intValue());

    }


    public static Intent newIntentAfterLoginActivity(Context context){
        return new Intent(context,AfterLoginActivity.class);
    }




}
