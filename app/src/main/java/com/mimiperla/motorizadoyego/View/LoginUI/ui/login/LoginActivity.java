package com.mimiperla.motorizadoyego.View.LoginUI.ui.login;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mimiperla.motorizadoyego.MainActivity;
import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.JwtResponse;
import com.mimiperla.motorizadoyego.Repository.Modelo.LoginRequest;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.mimiperla.motorizadoyego.Util.MyApp;
import com.mimiperla.motorizadoyego.Util.MyReceiver;
import com.mimiperla.motorizadoyego.View.LoginUI.SessionPrefs;
import com.mimiperla.motorizadoyego.ViewModel.AuthViewModel;
import com.mimiperla.motorizadoyego.ViewModel.Repartidor_BiViewModel;

public class LoginActivity extends AppCompatActivity implements MyReceiver.ConnectivityReciverListener{


    private AuthViewModel mAuthViewModel;

    private ProgressBar loadingProgressBar;

    private EditText usernameEditText,passwordEditText;

    private CardView loginButton;

    private Repartidor_BiViewModel viewModelRepartidor;

    private LinearLayout layout_correo,layout_constrasena;

    private TextView error_correo,error_contrasena,load_text;

    private LinearLayout show_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authResult();
        checkInternet();

        declararWidgets();
        iniciarSesion();
        getRepartidorData();
        changeStateEditext();
        activeLayout();
        showPassword();



    }


    private void authResult(){
        mAuthViewModel=new ViewModelProvider(this).get(AuthViewModel.class);
        mAuthViewModel.init();
        mAuthViewModel.getJwtResponseLiveData().observe(this, new Observer<JwtResponse>() {
            @Override
            public void onChanged(JwtResponse jwtResponse) {

                loadingProgressBar.setVisibility(View.GONE);


                if(jwtResponse!=null){
                    SessionPrefs.get(LoginActivity.this).logOut();
                    SessionPrefs.get(LoginActivity.this).saveJwtResponse(jwtResponse);

                    viewModelRepartidor.informacionBasica(SessionPrefs.get(LoginActivity.this).getTokenPrefs(),jwtResponse.getId().intValue());

                }else{

                    loadingProgressBar.setVisibility(View.GONE);

                    afterIniciarSession();
                    Toast.makeText(LoginActivity.this,"Verificar correo y contraseña ingresada",Toast.LENGTH_LONG).show();
                }

            }
        });

    }




    public static Intent newIntentLoginActivity(Context context){
        return new Intent(context,LoginActivity.class);
    }

    //------------------------------------------


    private void declararWidgets(){
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar= findViewById(R.id.loading);
        loadingProgressBar.setVisibility(View.GONE);

        layout_correo=findViewById(R.id.layout_correo);
        layout_constrasena=findViewById(R.id.layout_contrasena);
        error_correo=findViewById(R.id.error_correo);
        error_contrasena=findViewById(R.id.error_contrasena);
        load_text=findViewById(R.id.load_text);

        show_password=findViewById(R.id.show_password);
        show_password.setVisibility(View.GONE);

        usernameEditText.setCursorVisible(false);
        passwordEditText.setCursorVisible(false);

    }


    private void getRepartidorData(){
        viewModelRepartidor=new ViewModelProvider(this).get(Repartidor_BiViewModel.class);
        viewModelRepartidor.init();
        viewModelRepartidor.getRepartidor_biLiveData().observe(this, new Observer<Repartidor_Bi>() {
            @Override
            public void onChanged(Repartidor_Bi repartidor_bi) {
                loadingProgressBar.setVisibility(View.GONE);
                if(repartidor_bi !=null){

                    Repartidor_Bi.repartidor_bi=repartidor_bi;

                    Intent intent= MainActivity.newIntentMainActivity(getApplicationContext());
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(),"Intentelo nuevamente",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void iniciarSesion(){

        loginButton.setOnClickListener(v->{

            boolean respuesta1=false;
            boolean respuesta2=false;


            if(usernameEditText.getText().length()>0 ){
                respuesta1=true;

            }else {
                error_correo.setVisibility(View.VISIBLE);
            }
            if(passwordEditText.getText().length()>0){
                respuesta2=true;
            }else {
                error_contrasena.setVisibility(View.VISIBLE);
            }

            if(respuesta1 && respuesta2){

                loadingProgressBar.setVisibility(View.VISIBLE);
                load_text.setVisibility(View.GONE);

                LoginRequest loginRequest= new LoginRequest();
                loginRequest.setUsername(usernameEditText.getText().toString());
                loginRequest.setPassword(passwordEditText.getText().toString());
               // viewModel.registrarUsuarioEmpresa(loginRequest);
                mAuthViewModel.authenticationUser(loginRequest);


            }else {
                Toast.makeText(this, "Ingresar datos correctos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeStateEditext(){
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()>0){
                    layout_correo.setBackground(getResources().getDrawable(R.drawable.border_inicio_sesion_able));
                    error_correo.setVisibility(View.GONE);
                }else {
                    layout_correo.setBackground(getResources().getDrawable(R.drawable.border_inicio_sesion));

                }
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()>0){
                    layout_constrasena.setBackground(getResources().getDrawable(R.drawable.border_inicio_sesion_able));
                    error_contrasena.setVisibility(View.GONE);
                    show_password.setVisibility(View.VISIBLE);
                }else {
                    show_password.setVisibility(View.GONE);
                    layout_constrasena.setBackground(getResources().getDrawable(R.drawable.border_inicio_sesion));
                }
            }
        });
    }

    private void activeLayout(){
        usernameEditText.setOnClickListener(v->{
            layout_correo.setBackground(getResources().getDrawable(R.drawable.border_inicio_sesion_able));
            layout_constrasena.setBackground(getResources().getDrawable(R.drawable.border_inicio_sesion));
            usernameEditText.setCursorVisible(true);
        });

        passwordEditText.setOnClickListener(v->{
            layout_correo.setBackground(getResources().getDrawable(R.drawable.border_inicio_sesion));
            layout_constrasena.setBackground(getResources().getDrawable(R.drawable.border_inicio_sesion_able));
            passwordEditText.setCursorVisible(true);
        });
    }

    private void afterIniciarSession(){
        usernameEditText.setText("");
        passwordEditText.setText("");

        layout_correo.setBackground(getResources().getDrawable(R.drawable.border_inicio_sesion));
        layout_constrasena.setBackground(getResources().getDrawable(R.drawable.border_inicio_sesion));

        load_text.setVisibility(View.VISIBLE);

    }

    private void showPassword(){

        show_password.setOnClickListener(v->{

            if(show_password.getTag().toString().equals("able")){
                System.out.println("mostramos contrsena");
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }else {
                System.out.println("ocultamos contrsena");

                passwordEditText.setTransformationMethod(null);
            }

        });



    }

    //------------------------------------------




    public void checkInternet(){
        checkInternetConnection();
    }

    private void checkInternetConnection() {

        boolean isConnected=MyReceiver.isConnected();

        showSnakcBar(isConnected);

        if(!isConnected){
            changeActivity();
        }

    }

    private void changeActivity() {

        Toast.makeText(this, getString(R.string.verify_conection), Toast.LENGTH_SHORT).show();

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
        if(!isConnected){
            changeActivity();
        }
        showSnakcBar(isConnected);
    }

     private void showSnakcBar(boolean isConnected) {

        String message="";
        int color;

        if(isConnected){
            message="You are online";
            color= Color.WHITE;
        }else{
            message="You are offline";
            color= Color.RED;
        }

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

    }
}
