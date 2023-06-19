package com.mimiperla.motorizadoyego.View.ProcesUI.step4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Step4Activity extends AppCompatActivity {


    private static final String PEDIDO_DETALLE = "delivery_pedido";

    private Delivery_Pedido delivery_pedido;

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    private static final int REQUEST_CODE = 1996 ;

    private LocationCallback locationCallback;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4);

        reciveDataIntent();

       /* Toolbar toolbar=findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v->{
            onBackPressed();
        });*/

       solicitarPermisionLocation();

        mGsonDelivery_pedido=new GsonDelivery_Pedido();
        mGsonDelivery_pedido.setDelivery_information(delivery_pedido);
        Step4Fragment fragment=Step4Fragment.newInstance(mGsonDelivery_pedido);
        getSupportFragmentManager().beginTransaction().replace(R.id.step4Fragment_activity,fragment).commit();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(400);
        locationRequest.setFastestInterval(200);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        chechkSettingAndStartLocationUpdates();

        gpsData();

    }

    private void reciveDataIntent(){
        if(getIntent().getSerializableExtra(PEDIDO_DETALLE) !=null){
            delivery_pedido=(Delivery_Pedido) getIntent().getSerializableExtra(PEDIDO_DETALLE);
        }
    }



    public static Intent newIntentStep4Activity(Context context, Delivery_Pedido delivery_pedido){
        Intent intent= new Intent(context, Step4Activity.class);
        intent.putExtra(PEDIDO_DETALLE,delivery_pedido);
        return intent;
    }



    private void solicitarPermisionLocation(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions

                // muestra una ventana o Dialog en donde el usuario debe
                // dar permisos para el uso del GPS de su dispositivo.
                // El método dialogoSolicitarPermisoGPS() lo crearemos más adelante.
                dialogoSolicitarPermisoGPS();
                //return;
            }
        }
    }

    private void dialogoSolicitarPermisoGPS(){
        if (ActivityCompat.checkSelfPermission(Step4Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Step4Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Step4Activity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this, "Permiso habilitado", Toast.LENGTH_SHORT).show();

            }else {
                //finish();
                Toast.makeText(this, "No podra empezar el servicio ", Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }


    private void gpsData(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    //mLatLng=new LatLng(location.getLatitude(),location.getLongitude());

                    System.out.println("ALWAYS DRAW");


                }
            }
        };
    }

    private void chechkSettingAndStartLocationUpdates() {

        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);

        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                //setting of device are satisfied and we can start updates
                System.out.println("succes");
                startLocationUpdates();

            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        System.out.println("fail succes");

                        apiException.startResolutionForResult(Step4Activity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        System.out.println("fail");

                        ex.printStackTrace();
                    }

                }
            }
        });
    }

    private void startLocationUpdates() {

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void stopLocationUpdates() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
