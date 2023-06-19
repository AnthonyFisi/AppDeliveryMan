package com.mimiperla.motorizadoyego;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.LatLng;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.mimiperla.motorizadoyego.Util.RxBus;
import com.mimiperla.motorizadoyego.View.ProcesUI.listOrder.ListOrderFragment;
import com.mimiperla.motorizadoyego.View.ui.billetera.BilleteraFragment;
import com.mimiperla.motorizadoyego.View.ui.inicio.InicioFragment;
import com.mimiperla.motorizadoyego.View.ui.perfil.PerfilFragment;
import com.mimiperla.motorizadoyego.View.ui.soporte.SoporteFragment;
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

import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        InicioFragment.OnDataPass ,
        BilleteraFragment.BackToInicio,
        PerfilFragment.BackToInicio,
        ListOrderFragment.OnFragmentInteractionListener,
        SoporteFragment.BackToInicio
{


    public static final String CHANNEL_1_ID = "channel1";


    private static final int REQUEST_CODE = 1996 ;

    public static Pusher pusher;

    public static Channel channel ;

    private int idRepartidor;

    private String TAG="MainActivty";

    private static final String INTENT_REPARTIDOR_BI="com.example.motorizadoyego.View.SlapshActivity";

    public static final String COL_NAME="UsuarioDelivery";

    public  Repartidor_Bi mRepartidor_bi;

    private CollectionReference documentReference;

    private NavInflater navInflater;

    private NavGraph graph;

    private NavController nav;

    private LocationCallback locationCallback;

    private FusedLocationProviderClient fusedLocationProviderClient;


    private AppBarConfiguration mAppBarConfiguration;

    private LocationRequest locationRequest;

    private NavigationView navigationView;

    private boolean respuestaSendGps;

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // reciveDataIntent();

        mRepartidor_bi=Repartidor_Bi.repartidor_bi;

        //System.out.println("CODE"+mRepartidor_bi.getCorreo());

        NavHostFragment navHost= (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        nav= navHost.getNavController();
        navInflater = nav.getNavInflater();
        graph = navInflater.inflate(R.navigation.mobile_navigation);


        respuestaSendGps=false;


        declararFirebaseInstance();

        declararFireStoreInstance();

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_perfil, R.id.nav_billetera,R.id.nav_tools,R.id.nav_soporte_cliente, R.id.nav_cerrar_sesion,R.id.nav_lista)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        declararPusher();

        startGpsUpdate();

        initDataDrawer();

        updateLocation();

        solicitarPermisionLocation();

        funPusher();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //chechkSettingAndStartLocationUpdates();
    }

    private void declararPusher(){
        PusherOptions options = new PusherOptions();
        options.setCluster("us2");
        pusher = new Pusher( "205b5aefd2b3aa809c52", options);
        channel= MainActivity.pusher.subscribe("canal-orden-delivery-"+Repartidor_Bi.repartidor_bi.getIdrepartidor());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*
    * SOLICITAR PERMISO
    * */

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
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                //When permission grated
                // call methos
                Toast.makeText(this, "PERMISO CONCEDIDO", Toast.LENGTH_SHORT).show();
                chechkSettingAndStartLocationUpdates();
                updateLocation();
                RxBus.getInstance().publishPermission(true);
                //getCurrentLocation();
            }else {

                Toast.makeText(this, "TO BAD", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void initDataDrawer(){

        View hView =  navigationView.getHeaderView(0);
        TextView nombre_usuario=hView.findViewById(R.id.nombre_usuario);
        TextView correo_usuario=hView.findViewById(R.id.correo_usuario);

        Repartidor_Bi repartidor_bi=Repartidor_Bi.repartidor_bi;
        correo_usuario.setText(repartidor_bi.getCorreo());
        nombre_usuario.setText(repartidor_bi.getNombre_usuario());

        ImageView imageView_USUARIO = hView.findViewById(R.id.imageView_USUARIO);

        if (repartidor_bi.getFoto()!= null) {
            String imageUrl = repartidor_bi.getFoto()
                    .replace("http://", "https://");

            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView_USUARIO);
        }
    }


    private void chechkSettingAndStartLocationUpdates(){

        LocationSettingsRequest request= new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        SettingsClient client= LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask=client.checkLocationSettings(request);

        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                //setting of device are satisfied and we can start updates
                startLocationUpdates();

            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if(e instanceof ResolvableApiException){
                    ResolvableApiException apiException= (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(MainActivity.this,1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
    }

    private void startLocationUpdates(){

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());

    }

    private void stopLocationUpdates(){

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    public static Intent newIntentMainActivity(Context context){
       return new Intent(context,MainActivity.class);
     //   intent.putExtra(INTENT_REPARTIDOR_BI,repartidor_bi);
     //   return intent;
    }

    private void reciveDataIntent(){
        if(getIntent().getSerializableExtra(INTENT_REPARTIDOR_BI) !=null){
            mRepartidor_bi=(Repartidor_Bi) getIntent().getSerializableExtra(INTENT_REPARTIDOR_BI);

            System.out.println("IM HERE " +mRepartidor_bi.getCorreo() +"/"+mRepartidor_bi.getFoto());
        }

    }

    @Override
    public void onDataPass(boolean state) {

        if(state){
            chechkSettingAndStartLocationUpdates();
            Toast.makeText(getApplicationContext(),"INICIA EL ENVIO DE GPS DATA",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"SE DETIENE EL ENVIO DE GPS DATA",Toast.LENGTH_SHORT).show();

            stopLocationUpdates();
        }


    }


    private void declararFirebaseInstance(){
        /*ref= FirebaseDatabase.getInstance().getReference("Delivery")
                .child("Usuarios")
                .child(String.valueOf(mRepartidor_bi.getIdusuario()))
                .child("location");*/
                //.child(String.valueOf(mRestaurante_pedido.getIdventa()));
    }


    private void declararFireStoreInstance(){

        documentReference=FirebaseFirestore.getInstance().collection(COL_NAME);
    }

    private void startGpsUpdate(){

        if(mRepartidor_bi.isActiva() && mRepartidor_bi.isDisponible()){
            chechkSettingAndStartLocationUpdates();
        }
    }


    @Override
    public void back() {
        graph.setStartDestination(R.id.nav_lista);
        nav.setGraph(graph);
    }


    private void updateLocation(){

        locationCallback= new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(locationResult ==null){
                    return;
                }

                for(Location location:locationResult.getLocations()){

                    if(respuestaSendGps){

                        String loc="Long : "+location.getLongitude() +"-"+"Lat"+location.getLatitude();

                        Log.d(TAG,"onLocation"+location.toString());

                        LatLng latLng= new LatLng(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));

                        String gps=location.getLatitude()+","+location.getLongitude();

                        //ENVIAR CONSTANTEMENTE ACTUALIZACIONES DE LA POSISCION
                        documentReference.document(String.valueOf(Repartidor_Bi.repartidor_bi.getIdusuariogeneral())).update("location",gps);
                        //ref.setValue(latLng);
                    }

                }
            }
        };

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private void funPusher() {
        channel.bind("my-event", (channelName, eventName, data) -> {
            JsonParser parser = new JsonParser();
            JsonElement mJson = parser.parse(data);
            Gson gson = new Gson();
            System.out.println(mJson);
            mGsonDelivery_pedido = gson.fromJson(mJson, GsonDelivery_Pedido.class);
            createNotificationChannel();

            String title = "Pedido #"+mGsonDelivery_pedido.getDelivery_information().getIdventa();

            String message = "Direccion : "+mGsonDelivery_pedido.getDelivery_information().getUbicacion_nombre();
            publishNotification(title,message);
        });
        pusher.connect();
    }



    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name="Notification";
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_1_ID,
                    name,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void publishNotification(String titulo,String mensaje){

        Intent i=MainActivity.newIntentMainActivity(this);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher)

                .setContentIntent(pendingIntent)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setShowWhen(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(new long[] { 2000, 2000, 2000, 2000, 2000 })
                .setSound(Uri.parse("android.resource://"
                        + getApplicationContext().getPackageName() + "/" + R.raw.notificacion))
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(1, notification);
    }

}
