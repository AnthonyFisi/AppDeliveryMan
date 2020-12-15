package com.example.motorizadoyego.View.LoginUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.JwtResponse;
import com.example.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.example.motorizadoyego.View.LoginUI.ui.login.LoginActivity;
import com.example.motorizadoyego.View.SplashActivity;
import com.example.motorizadoyego.ViewModel.Repartidor_BiViewModel;

public class NetworkActivity extends AppCompatActivity {

    private LinearLayout load_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        load_again=findViewById(R.id.load_again);
        load_again.setOnClickListener( v->{
            Intent intent= new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        });

    }


}
