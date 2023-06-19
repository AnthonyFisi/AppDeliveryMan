package com.mimiperla.motorizadoyego.View.ui.inicio;

import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.disposables.Disposable;

import com.bumptech.glide.Glide;
import com.mimiperla.motorizadoyego.MainActivity;
import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonDeliveryPedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.mimiperla.motorizadoyego.Util.RxBus;
import com.mimiperla.motorizadoyego.View.ProcesUI.step5.Step5Fragment;
import com.mimiperla.motorizadoyego.View.ProcesUI.step6.Step6Fragment;
import com.mimiperla.motorizadoyego.View.ProcesUI.StepPictureFragment;
import com.mimiperla.motorizadoyego.View.ProcesUI.listOrder.ListOrderFragment;
import com.mimiperla.motorizadoyego.View.ProcesUI.step1.DirectionsJSONParser;
import com.mimiperla.motorizadoyego.View.ProcesUI.step1.Step1Fragment;
import com.mimiperla.motorizadoyego.View.ProcesUI.step2.Step2Fragment;
import com.mimiperla.motorizadoyego.View.ProcesUI.step3.Step3Fragment;
import com.mimiperla.motorizadoyego.View.ProcesUI.step4.Step4Fragment;
import com.mimiperla.motorizadoyego.View.ui.tools.ToolsFragment;
import com.mimiperla.motorizadoyego.ViewModel.Delivery_PedidoViewModel;
import com.mimiperla.motorizadoyego.ViewModel.RepartidorViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

public class InicioFragment extends Fragment implements OnMapReadyCallback {

    private Delivery_PedidoViewModel viewModel;
    //private Delivery_Pedido mDelivery_pedido;
    private TextView fragment_orden_GANANCIA, fragment_orden_DISTANCIA,
            fragment_orden_TIEMPO, fragment_orden_ORIGEN, fragment_orden_DESTINO,textView_message;
    private GsonDelivery_Pedido mGsonDelivery_pedido;
    //private GsonDeliveryPedido mGsonDeliveryPedido;
    private List<Delivery_Pedido> listDeliveryPedido;
    private OnDataPass dataPasser;
    private RepartidorViewModel viewModelRepartidor;
    private Repartidor_Bi mRepartidor_bi;
    private LinearLayout fragment_home_linear_deuda, fragment_home_linear_desactivado,
            fragment_home_linear_espera, change_state_repartidor;
    private Button fragment_home_button_ESTADO_REPARTIDOR;//fragment_home_button_activado,fragment_home_button_desactivado;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private MediaPlayer soundNotificationOrder;
    private ArrayList markerPoints;
    private FragmentTransaction transaction;
    private ImageView home_fragment_IMAGEN_REPARTIDOR;
    private Fragment initFragment, step1Fragment, step2Fragment, step3Fragment, stepPictureFragment,
            step4Fragment, step5Fragment, step6Fragment,listOrderFragment;
    private boolean lockUpdateGps;
    private CardView cardview_IMAGEN_REPARTIDOR;
    private Context mContext;
    //GUARDAR LA ULTIMA POSICION DE LA PERSONA
    private Location mLocation;
    private LocationCallback locationCallback ;
    private LocationRequest locationRequest;
    private GoogleMap mMap;
    private ProgressBar progressBar;


    public InicioFragment() {
        markerPoints = new ArrayList();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lockUpdateGps = false;

        viewModel = new ViewModelProvider(this).get(Delivery_PedidoViewModel.class);
        viewModel.init();

        viewModelRepartidor = new ViewModelProvider(this).get(RepartidorViewModel.class);
        viewModelRepartidor.init();

        mRepartidor_bi = Repartidor_Bi.repartidor_bi;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        soundNotificationOrder = soundNotificationOrder.create(getContext(), R.raw.notificacion);

        //Fragment de inicio
        initFragment = new ToolsFragment();
        //mGsonDeliveryPedido =  new GsonDeliveryPedido();

        listDeliveryPedido =  new ArrayList<>();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mapView = view.findViewById(R.id.google_map_aceptar_orden);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        getActivity().getSupportFragmentManager().beginTransaction().remove(initFragment).commit();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout2, initFragment).commit();

        transaction = getActivity().getSupportFragmentManager().beginTransaction();

      //  declararFragments();

        declararWidgets(view);
        loadDataPedido();
        setDataWidgetEssential();
        updateStateRepartidor();
        changeDisponibilidadEmpresa();
        funPusher();
        obervarStep1Fragment();
        observarStep2Fragment();
        observarListPedidoFragment();
        observarStep3Fragment();
        observarStepPictureFragment();
        observarStep4Fragment();
        observarStep5Fragment();
        observarStep6Fragment();
        gpsData();
        observarPermission();



        fragment_home_linear_espera.setOnClickListener(v->{
            //listOrderFragment= ListOrderFragment.newInstance(mGsonDeliveryPedido);
            transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout2, listOrderFragment).commit();
            fragment_home_linear_espera.setVisibility(View.GONE);
        });

        closeLista();

    }

    private void declararFragments() {
        step1Fragment = Step1Fragment.newInstanceStep1Fragment(mGsonDelivery_pedido);
        step2Fragment = Step2Fragment.newInstance(mGsonDelivery_pedido);
        step3Fragment = Step3Fragment.newInstance(mGsonDelivery_pedido);
        stepPictureFragment = StepPictureFragment.newInstance(mGsonDelivery_pedido);
        step4Fragment = Step4Fragment.newInstance(mGsonDelivery_pedido);
        step5Fragment = Step5Fragment.newInstance(mGsonDelivery_pedido);
        step6Fragment = Step6Fragment.newInstance(mGsonDelivery_pedido);
    }

    private void loadDataPedido() {
        viewModel.getGsonDeliveryPedidoLiveData().observe(getViewLifecycleOwner(), new Observer<GsonDeliveryPedido>() {
            @Override
            public void onChanged(GsonDeliveryPedido gsonDeliveryPedido) {
                fragment_home_linear_espera.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                if (gsonDeliveryPedido != null) {
                    //mGsonDeliveryPedido=gsonDeliveryPedido;
                    //mDelivery_pedido = delivery_pedido.getDelivery_information();
                  //  mGsonDelivery_pedido = delivery_pedido;
                    setDataWidget();
                    //home_fragment_APP_BAR.setVisibility(View.GONE);
                   // fragment_home_linear_espera.setVisibility(View.GONE);
                   // fragment_home_button_ESTADO_REPARTIDOR.setVisibility(View.GONE);
                 //   home_fragment_IMAGEN_REPARTIDOR.setVisibility(View.GONE);
                   // cardview_IMAGEN_REPARTIDOR.setVisibility(View.GONE);
                    declararFragments();
                    String total_pedido=gsonDeliveryPedido.getListaDeliveryPedido().size()+" pedido";
                    textView_message.setText(total_pedido);
                    //reciveOrderPending();
                } else {
                    // home_fragment_APP_BAR.setVisibility(View.VISIBLE);
                    String total_pedido="0 pedidos";
                    textView_message.setText(total_pedido);
                }

            }
        });
    }

    private void declararWidgets(View view) {

        fragment_orden_GANANCIA = view.findViewById(R.id.fragment_orden_GANANCIA);
        fragment_orden_DISTANCIA = view.findViewById(R.id.fragment_orden_DISTANCIA);
        fragment_orden_TIEMPO = view.findViewById(R.id.fragment_orden_TIEMPO);
        fragment_orden_ORIGEN = view.findViewById(R.id.fragment_orden_ORIGEN);
        fragment_orden_DESTINO = view.findViewById(R.id.fragment_orden_DESTINO);
        //fragment_home_button_activado=view.findViewById(R.id.fragment_home_button_activado);
        // fragment_home_button_desactivado=view.findViewById(R.id.fragment_home_button_desactivado);
        //home_fragment_APP_BAR=view.findViewById(R.id.home_fragment_APP_BAR);
        change_state_repartidor = view.findViewById(R.id.change_state_repartidor);
        fragment_home_button_ESTADO_REPARTIDOR = view.findViewById(R.id.fragment_home_button_ESTADO_REPARTIDOR);
        fragment_home_linear_deuda = view.findViewById(R.id.fragment_home_linear_deuda);
        fragment_home_linear_desactivado = view.findViewById(R.id.fragment_home_linear_desactivado);
        fragment_home_linear_espera = view.findViewById(R.id.fragment_home_linear_espera);
        home_fragment_IMAGEN_REPARTIDOR = view.findViewById(R.id.fragment_home_IMAGEN_REPARTIDOR);
        cardview_IMAGEN_REPARTIDOR=view.findViewById(R.id.cardview_IMAGEN_REPARTIDOR);
        progressBar=view.findViewById(R.id.progresbar);
        textView_message=view.findViewById(R.id.textView_message);
        progressBar=view.findViewById(R.id.progressbar_pedido);
    }

    private void setDataWidget() {
  /*     fragment_orden_GANANCIA.setText(String.valueOf(mDelivery_pedido.getCosto_delivery()));
        fragment_orden_DISTANCIA.setText(mDelivery_pedido.getDistancia_delivery());
        fragment_orden_TIEMPO.setText(mDelivery_pedido.getTiempo());
        fragment_orden_ORIGEN.setText(mDelivery_pedido.getDireccion_empresa());
        fragment_orden_DESTINO.setText(mDelivery_pedido.getUbicacion_nombre());
*/
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

                    mLocation = location;

                    RxBus.getInstance().publishLocation(location);

                    if (!lockUpdateGps) {

                        drawRouteHome(location);
                    }
                }
            }
        };
    }

    private void setDataWidgetEssential() {
        //fragment_home_button_activado.setVisibility(View.GONE);
        //fragment_home_button_desactivado.setVisibility(View.GONE);
 /*       fragment_home_linear_deuda.setVisibility(View.GONE);
        fragment_home_linear_desactivado.setVisibility(View.GONE);
        fragment_home_linear_espera.setVisibility(View.GONE);
*/
        if (Repartidor_Bi.repartidor_bi.getFoto() != null) {
            String imageUrl =Repartidor_Bi.repartidor_bi.getFoto()
                    .replace("http://", "https://");

            Glide.with(this)
                    .load(imageUrl)
                    .into(home_fragment_IMAGEN_REPARTIDOR);
        }

        if (mRepartidor_bi.isActiva()) {

            if (mRepartidor_bi.isDisponible()) {

                // fragment_home_button_activado.setVisibility(View.VISIBLE);
                fragment_home_linear_espera.setVisibility(View.VISIBLE);
                chechkSettingAndStartLocationUpdates();
                fragment_home_button_ESTADO_REPARTIDOR.setBackground(getResources().getDrawable(R.drawable.home_fragment_border_activado));
                fragment_home_button_ESTADO_REPARTIDOR.setText("Activado");
                viewModel.listDeliveryPedido(mRepartidor_bi.getIdrepartidor());

            } else {

                //fragment_home_button_desactivado.setVisibility(View.VISIBLE);
                fragment_home_linear_desactivado.setVisibility(View.VISIBLE);

            }
        } else {

            fragment_home_button_ESTADO_REPARTIDOR.setEnabled(false);
            fragment_home_button_ESTADO_REPARTIDOR.setVisibility(View.GONE);
            fragment_home_linear_deuda.setVisibility(View.VISIBLE);

        }
    }

    private void funPusher() {
        MainActivity.channel.bind("my-event", (channelName, eventName, data) -> {
            if (getActivity() != null) {

                getActivity().runOnUiThread(() -> {
                    try {

                        lockUpdateGps = true; 
                        soundNotificationOrder.start();
                        vibratePhoneNotification(2000);

                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonElement mJson = parser.parse(data);
                        mGsonDelivery_pedido = gson.fromJson(mJson, GsonDelivery_Pedido.class);

                        Delivery_Pedido mDelivery_pedido = mGsonDelivery_pedido.getDelivery_information();

                        listDeliveryPedido.add(mDelivery_pedido);


                        setDataWidget();

                        //home_fragment_APP_BAR.setVisibility(View.GONE);
                       fragment_home_button_ESTADO_REPARTIDOR.setVisibility(View.GONE);
                        home_fragment_IMAGEN_REPARTIDOR.setVisibility(View.GONE);

                        cardview_IMAGEN_REPARTIDOR.setVisibility(View.GONE);

                        fragment_home_linear_espera.setVisibility(View.GONE);
                        fragment_home_linear_deuda.setVisibility(View.GONE);
                        fragment_home_linear_desactivado.setVisibility(View.GONE);

                        drawRouteStep1Fragment(mGsonDelivery_pedido.getDelivery_information());

                        stopLocationUpdates();
                        refreshViewPedido();
                        publishNotification(mGsonDelivery_pedido.getDelivery_information().getIdventa(),mGsonDelivery_pedido.getDelivery_information().getUbicacion_nombre());

                        declararFragments();

                        /*step1Fragment = Step1Fragment.newInstanceStep1Fragment(mGsonDelivery_pedido);
                        transaction = getActivity().getSupportFragmentManager().beginTransaction();*/

                        transaction.replace(R.id.frameLayout2, step1Fragment).commit();



                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });
            }

        });
        MainActivity.pusher.connect();
    }


    private void reciveOrderPending() {


      /*  if (mDelivery_pedido.getIdestado_delivery() == 1) {
            drawRouteStep2Fragment(2);

            transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.frameLayout2, step2Fragment).commit();

        }
        if (mDelivery_pedido.getIdestado_delivery() == 2) {


            transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.frameLayout2, step3Fragment).commit();

        }
        if (mDelivery_pedido.getIdestado_delivery() == 3) {

            transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.frameLayout2, stepPictureFragment).commit();

        }

        if (mDelivery_pedido.getIdestado_delivery() == 6) {

            drawRouteStep2Fragment(4);


            transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.frameLayout2, step4Fragment).commit();

        }

        if (mDelivery_pedido.getIdestado_delivery() == 4) {


            transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.frameLayout2, step5Fragment).commit();

        }

        if (mDelivery_pedido.getIdestado_delivery() == 5) {

            // transaction.replace(R.id.frameLayout2,step6Fragment).commit();

        }*/


    }

    private void changeDisponibilidadEmpresa() {

        if (mRepartidor_bi.isActiva()) {
            // fragment_home_button_desactivado.setOnClickListener( v-> viewModelRepartidor.updateRepartidorState(mRepartidor_bi.getIdrepartidor(),true));

            //fragment_home_button_activado.setOnClickListener( v-> viewModelRepartidor.updateRepartidorState(mRepartidor_bi.getIdrepartidor(),false));


            fragment_home_button_ESTADO_REPARTIDOR.setOnClickListener((v) -> {
                        viewModelRepartidor.updateRepartidorState(mRepartidor_bi.getIdrepartidor(), !mRepartidor_bi.isDisponible());
                        fragment_home_button_ESTADO_REPARTIDOR.setEnabled(false);
                        change_state_repartidor.setVisibility(View.VISIBLE);
                        fragment_home_linear_espera.setVisibility(View.GONE);
                    }
            );


        }
    }

    private void updateStateRepartidor() {
        viewModelRepartidor.getRepartidorLiveData().observe(getViewLifecycleOwner(), new Observer<Repartidor>() {
            @Override
            public void onChanged(Repartidor repartidor) {
                change_state_repartidor.setVisibility(View.GONE);
                fragment_home_button_ESTADO_REPARTIDOR.setEnabled(true);
                if (repartidor != null) {

                    passData(repartidor.isDisponible());

                    Repartidor_Bi.repartidor_bi.setDisponible(repartidor.isDisponible());

                    if (repartidor.isDisponible()) {
/*
                        fragment_home_button_desactivado.setVisibility(View.GONE);
                        fragment_home_button_activado.setVisibility(View.VISIBLE);*/


                        fragment_home_button_ESTADO_REPARTIDOR.setBackground(getResources().getDrawable(R.drawable.home_fragment_border_activado));
                        fragment_home_button_ESTADO_REPARTIDOR.setText("Disponible");
                        fragment_home_linear_desactivado.setVisibility(View.GONE);
                        fragment_home_linear_deuda.setVisibility(View.GONE);
                        fragment_home_linear_espera.setVisibility(View.VISIBLE);


                        chechkSettingAndStartLocationUpdates();


                    } else {

                        /*

                        fragment_home_button_activado.setVisibility(View.GONE);
                        fragment_home_button_desactivado.setVisibility(View.VISIBLE);
*/

                        fragment_home_button_ESTADO_REPARTIDOR.setBackground(getResources().getDrawable(R.drawable.home_fragment_border_desactivado));
                        fragment_home_button_ESTADO_REPARTIDOR.setText("No disponible");


                        fragment_home_linear_deuda.setVisibility(View.GONE);
                        fragment_home_linear_espera.setVisibility(View.GONE);
                        fragment_home_linear_desactivado.setVisibility(View.VISIBLE);

                        stopLocationUpdates();

                    }


                } else {
                    fragment_home_linear_espera.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void chechkSettingAndStartLocationUpdates() {

        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        SettingsClient client = LocationServices.getSettingsClient(getContext());

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);

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

                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(getActivity(), 1001);
                    } catch (IntentSender.SendIntentException ex) {
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

    private void drawRouteHome(Location location) {


        mMap.clear();

        double lat = location.getLatitude();
        double log = location.getLongitude();


        LatLng loc = new LatLng(lat, log);

/*
        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(loc);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gps));

        mMap.addMarker(options).showInfoWindow();
*/
        mMap.addCircle(new CircleOptions()
                .center(loc)
                .radius(8)
                .strokeWidth(8)
                .strokeColor(mContext.getResources().getColor(R.color.background_oficial))
                .fillColor(mContext.getResources().getColor(R.color.background_oficial_light))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 18));


    }

    private void vibratePhoneNotification(long millis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE))
                    .vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {

            ((Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE)).vibrate(millis);
        }
    }

    private void drawRouteStep1Fragment(Delivery_Pedido mDelivery_pedido) {

        mMap.clear();

        double latDes = Double.parseDouble(mDelivery_pedido.getUsuario_coordenada_x().trim());
        double logDes = Double.parseDouble(mDelivery_pedido.getUsuario_coordenada_y().trim());


        double latOri = Double.parseDouble(mDelivery_pedido.getEmpresa_coordenada_x().trim());
        double logOri = Double.parseDouble(mDelivery_pedido.getEmpresa_coordenada_y().trim());


        LatLng locationDestino = new LatLng(latDes, logDes);
        LatLng locationOrigen = new LatLng(latOri, logOri);


        markerPoints.add(locationOrigen);


        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(locationOrigen);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.icontienda));
        options.title(mDelivery_pedido.getNombre_empresa());

        mMap.addMarker(options).showInfoWindow();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationOrigen, 17));

        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(locationOrigen)      // Sets the center of the map to Mountain View
                .zoom(18)                   // Sets the zoom
                .bearing(45)                // Sets the orientation of the camera to east
                .tilt(70)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        */


        markerPoints.add(locationDestino);

        // Creating MarkerOptions
        MarkerOptions options2 = new MarkerOptions();

        // Setting the position of the marker
        options2.position(locationDestino);
        options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconhome));
        //options2.title(mDelivery_pedido.getno);
        mMap.addMarker(options2);


        LatLng origin = (LatLng) markerPoints.get(0);
        LatLng dest = (LatLng) markerPoints.get(1);


        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);


        System.out.println(url);

        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);


    }

    private void drawRouteStep2Fragment(int numeroFragment,Delivery_Pedido mDelivery_pedido) {
        double latDes = 0, logDes = 0;

        mMap.clear();


        double latOri = mLocation.getLatitude();
        double logOri = mLocation.getLongitude();


        LatLng locationOrigen = new LatLng(latOri, logOri);


        markerPoints.add(locationOrigen);


        // Creating MarkerOptions
        //MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
      //  options.position(locationOrigen);
       // options.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gps));



        mMap.addCircle(new CircleOptions()
                .center(locationOrigen)
                .radius(10)
                .strokeWidth(100)
                .strokeColor(getResources().getColor(R.color.background_oficial_light))
                .fillColor(getResources().getColor(R.color.background_oficial))
        );
        //options.title(mDelivery_pedido.getNombre_empresa());

        //mMap.addMarker(options).showInfoWindow();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationOrigen, 17));

        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(locationOrigen)      // Sets the center of the map to Mountain View
                .zoom(18)                   // Sets the zoom
                .bearing(45)                // Sets the orientation of the camera to east
                .tilt(70)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        if (numeroFragment == 2) {
            latDes = Double.parseDouble(mDelivery_pedido.getEmpresa_coordenada_x().trim());
            logDes = Double.parseDouble(mDelivery_pedido.getEmpresa_coordenada_y().trim());
            LatLng locationDestino = new LatLng(latDes, logDes);


            markerPoints.add(locationDestino);

            // Creating MarkerOptions
            MarkerOptions options2 = new MarkerOptions();

            // Setting the position of the marker
            options2.position(locationDestino);
            options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.icontienda));
            //options2.title(mDelivery_pedido.getno);
            mMap.addMarker(options2);

        } else {
            if (numeroFragment == 4) {
                latDes = Double.parseDouble(mDelivery_pedido.getUsuario_coordenada_x().trim());
                logDes = Double.parseDouble(mDelivery_pedido.getUsuario_coordenada_y().trim());
                LatLng locationDestino = new LatLng(latDes, logDes);


                markerPoints.add(locationDestino);

                // Creating MarkerOptions
                MarkerOptions options2 = new MarkerOptions();

                // Setting the position of the marker
                options2.position(locationDestino);
                options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconhome));
                //options2.title(mDelivery_pedido.getno);
                mMap.addMarker(options2);

            }
        }


        LatLng origin = (LatLng) markerPoints.get(0);
        LatLng dest = (LatLng) markerPoints.get(1);


        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);


        System.out.println(url);

        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);


    }

    public static void calculateDistanceAndTime(String data) {

        HashMap<String,String> listaTiempoDistancia= new HashMap<String,String>();

        JSONObject parse ;
        JSONArray jRoutes;
        JSONArray jLegs;
        try {
            parse = new JSONObject(data);
            jRoutes=parse.getJSONArray("routes");
            jLegs = ( (JSONObject)jRoutes.get(0)).getJSONArray("legs");

            for(int j=0;j<jLegs.length();j++) {

                JSONObject object= jLegs.getJSONObject(j);

                System.out.println("DISTANCIA" + object.getJSONObject("distance").get("text")+"SOLO DISTANCIA");

                String dis=object.getJSONObject("distance").get("text").toString();

                listaTiempoDistancia.put("distance",dis);

                System.out.println("TIEMPO "+object.getJSONObject("duration").get("text")+"SOLO DISTANCIA");

                String tiem=object.getJSONObject("duration").get("text").toString();

                listaTiempoDistancia.put("duration",tiem);



            }

            //PUBLICANDO LA DISTANCIA Y TIEMPO
            RxBus.getInstance().publishDistanceAndTime(listaTiempoDistancia);



        } catch (JSONException e) {

            e.printStackTrace();
        }

    }


    //--------------------DIBUJAR LA RUTA DE ENVIO------------------------

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);

            System.out.println(result+"RESULT");

            calculateDistanceAndTime(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(20);

                lineOptions.color(Color.rgb(50, 90, 115));
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";
        //API KEY
        String API_KEY = "&key=AIzaSyCxOO2oNnTXOAqPA7CUQScev5N_-5S-uW4";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + API_KEY;

        /*String m=
                "https://maps.googleapis.com/maps/api/directions/json?origin=-12.1689212,-76.9275876&detination=-12.1690929,-76.9277597&key=AIzaSyAovb3NQYJdlU_a8SwdrWIe2cj-e2NWOmM"
*/
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    //--------------------DIBUJAR LA RUTA DE ENVIO-------------------------


    private void obervarStep1Fragment() {

        RxBus.getInstance().listen().subscribe(new io.reactivex.Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

                lockUpdateGps = false;

                System.out.println("REGRESAMOS O NADA MANO");

                if (s.equals("FALSE")) {


                    fragment_home_button_ESTADO_REPARTIDOR.setVisibility(View.VISIBLE);


                    home_fragment_IMAGEN_REPARTIDOR.setVisibility(View.VISIBLE);

                    fragment_home_linear_espera.setVisibility(View.VISIBLE);

                    transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    transaction.remove(step1Fragment).commit();

                    mMap.clear();


                } else {


                    fragment_home_linear_espera.setVisibility(View.GONE);

                    home_fragment_IMAGEN_REPARTIDOR.setVisibility(View.GONE);
                    cardview_IMAGEN_REPARTIDOR.setVisibility(View.GONE);


                    fragment_home_button_ESTADO_REPARTIDOR.setVisibility(View.GONE);

                    mGsonDelivery_pedido.getDelivery_information().setIdestado_delivery(1);

                    transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frameLayout2, step2Fragment).commit();

                    //drawRouteStep2Fragment(2,de);


                }

                System.out.println("LLEGA LA PUBLICACION" + s);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void observarStep2Fragment() {

        RxBus.getInstance().listenStep2Fragment().subscribe(new io.reactivex.Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

                if (aBoolean) {

                    fragment_home_linear_espera.setVisibility(View.GONE);

                    mGsonDelivery_pedido.getDelivery_information().setIdestado_delivery(2);

                    step3Fragment = Step3Fragment.newInstance(mGsonDelivery_pedido);

                    transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frameLayout2, step3Fragment).commit();

                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void observarListPedidoFragment() {

        RxBus.getInstance().listenListPedidoFragment().subscribe(new io.reactivex.Observer<Delivery_Pedido>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Delivery_Pedido delivery_pedido) {

                mGsonDelivery_pedido=new GsonDelivery_Pedido();

                mGsonDelivery_pedido.setDelivery_information(delivery_pedido);

                step3Fragment = Step3Fragment.newInstance(mGsonDelivery_pedido);

                mGsonDelivery_pedido.getDelivery_information().setIdestado_delivery(3);

                fragment_home_linear_espera.setVisibility(View.GONE);


                transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.frameLayout2, step3Fragment).commit();



            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void observarStep3Fragment() {

        RxBus.getInstance().listenStep3Fragment().subscribe(new io.reactivex.Observer<Delivery_Pedido>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Delivery_Pedido delivery_pedido) {

                mGsonDelivery_pedido=new GsonDelivery_Pedido();

                mGsonDelivery_pedido.setDelivery_information(delivery_pedido);



                System.out.println(delivery_pedido.getUbicacion_nombre()+" ubicacion del usuarioi");

                stepPictureFragment = StepPictureFragment.newInstance(mGsonDelivery_pedido);

               // mGsonDelivery_pedido.getDelivery_information().setIdestado_delivery(3);

                    fragment_home_linear_espera.setVisibility(View.GONE);

                    transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frameLayout2, stepPictureFragment).commit();



            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void observarStepPictureFragment() {

        RxBus.getInstance().listenStepPictureFragment().subscribe(new io.reactivex.Observer<Delivery_Pedido>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(@NonNull Delivery_Pedido delivery_pedido) {
                mGsonDelivery_pedido=new GsonDelivery_Pedido();

                mGsonDelivery_pedido.setDelivery_information(delivery_pedido);

                step4Fragment = Step4Fragment.newInstance(mGsonDelivery_pedido);

                mGsonDelivery_pedido.getDelivery_information().setIdestado_delivery(7);

                    drawRouteStep2Fragment(4,delivery_pedido);

                    fragment_home_linear_espera.setVisibility(View.GONE);

                    transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frameLayout2, step4Fragment).commit();



            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void observarStep4Fragment() {

        RxBus.getInstance().listenStep4Fragment().subscribe(new io.reactivex.Observer<Delivery_Pedido>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Delivery_Pedido delivery_pedido) {

                mGsonDelivery_pedido=new GsonDelivery_Pedido();


                mGsonDelivery_pedido.setDelivery_information(delivery_pedido);

                step5Fragment = Step5Fragment.newInstance(mGsonDelivery_pedido);

                mGsonDelivery_pedido.getDelivery_information().setIdestado_delivery(4);

                    fragment_home_linear_espera.setVisibility(View.GONE);

                    transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frameLayout2, step5Fragment).commit();

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void observarStep5Fragment() {

        RxBus.getInstance().listenStep5Fragment().subscribe(new io.reactivex.Observer<Delivery_Pedido>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Delivery_Pedido delivery_pedido) {
                mGsonDelivery_pedido=new GsonDelivery_Pedido();

                mGsonDelivery_pedido.setDelivery_information(delivery_pedido);

                step6Fragment = Step6Fragment.newInstance(mGsonDelivery_pedido);

                    fragment_home_linear_espera.setVisibility(View.GONE);

                    transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.frameLayout2, step6Fragment).commit();



            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void observarStep6Fragment() {

        RxBus.getInstance().listenStep6Fragment().subscribe(new io.reactivex.Observer<Delivery_Pedido>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Delivery_Pedido delivery_pedido) {



                    fragment_home_linear_espera.setVisibility(View.VISIBLE);

                    transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();

                    //transaction.remove(step6Fragment).commit();

                    transaction.replace(R.id.frameLayout2, initFragment).commit();

                    mGsonDelivery_pedido = null;

                    fragment_home_button_ESTADO_REPARTIDOR.setVisibility(View.VISIBLE);

                    //LIM´PIAR TODO LOS MPAS
                    mMap.clear();

                    deletePedidoFinish(delivery_pedido);


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void passData(boolean state) {
        dataPasser.onDataPass(state);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        dataPasser = (InicioFragment.OnDataPass) context;
    }

    public interface OnDataPass {
        void onDataPass(boolean state);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onResume() {
        super.onResume();
/*
        if (mGsonDelivery_pedido != null) {
            System.out.println("ENTRAMOS EN SEGUNDO PLANO");
            if (mGsonDelivery_pedido.getDelivery_information().getIdestado_delivery() == 0) {
                System.out.println("HERE");

                transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.frameLayout2, step1Fragment).commit();
            }
        } else {
            System.out.println("no EN SEGUNDO PLANO");

        }*/
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);

        mContext= childFragment.getContext();
    }


    private void observarPermission() {

        RxBus.getInstance().listenPermission().subscribe(new io.reactivex.Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

                if (aBoolean) {
                    gpsData();

                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void closeLista() {

        RxBus.getInstance().listenListPedido().subscribe(new io.reactivex.Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

                if (aBoolean) {

                    transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();

                    transaction.remove(listOrderFragment).commit();

                    fragment_home_linear_espera.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void refreshViewPedido(){
        String total_pedido="";

        if(listDeliveryPedido.size()==1){
            total_pedido=listDeliveryPedido.size()+" pedido";

        }else {
            total_pedido=listDeliveryPedido.size()+" pedidos";
        }

        textView_message.setText(total_pedido);

    }

    private void deletePedidoFinish(Delivery_Pedido delivery_pedido){

        int position=0;

        int count=0;

        for(Delivery_Pedido pedido:listDeliveryPedido){

            if(pedido.getIdventa()==delivery_pedido.getIdventa()){
                position=count;
            }
            count++;
        }


        listDeliveryPedido.remove(position);

        refreshViewPedido();

    }




    private void publishNotification(int idventa,String ubicacion){
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr =(NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);

        int icono = R.mipmap.ic_launcher;
      //  Intent i=InicioActivity.newIntentInicioActivity(getContext(),true);
        //PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, 0, 0);

        String menssage="Pedido #"+idventa+" ubicacion"+ubicacion;

        mBuilder =new NotificationCompat.Builder(getContext())
               // .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle("Yegoo")
                .setContentText(menssage)
                .setVibrate(new long[] {100, 250, 100, 500})
                .setAutoCancel(true);



        mNotifyMgr.notify(1, mBuilder.build());

    }
}