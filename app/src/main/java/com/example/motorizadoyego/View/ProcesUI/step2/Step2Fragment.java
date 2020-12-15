package com.example.motorizadoyego.View.ProcesUI.step2;


import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import io.reactivex.disposables.Disposable;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motorizadoyego.MainActivity;
import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Orden_estado_delivery;
import com.example.motorizadoyego.Repository.Modelo.Orden_estado_deliveryPK;

import com.example.motorizadoyego.Util.PageViewModel;
import com.example.motorizadoyego.Util.ProgressButon;
import com.example.motorizadoyego.Util.RxBus;
import com.example.motorizadoyego.View.ProcesUI.step1.DirectionsJSONParser;
import com.example.motorizadoyego.ViewModel.Orden_estado_deliveryViewModel;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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

public class Step2Fragment extends Fragment {

    private static final String ARG_HOME = "com.example.motorizadoyego.View.ui.home";

    private TextView fragment_step2_DISTANCIA,fragment_step2_TIEMPO,fragment_step2_DIRECCION_TIENDA,fragment_step2_NOMBRE_TIENDA;

    private ImageButton fragment_ste2_NAVEGAR_GOOGLE_MAPS;

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    private Delivery_Pedido mDelivery_pedido;

    private Orden_estado_deliveryViewModel viewModelEstado;

    private Location temp;

    private LatLng mLatLng;

    private boolean llegoTienda=true;

    private View mView;

    private ProgressButon progressButon;


    public Step2Fragment() {
        // Required empty public constructor
    }

    public static Step2Fragment newInstance(GsonDelivery_Pedido gsonDelivery_pedido) {
        Step2Fragment fragment = new Step2Fragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_HOME, gsonDelivery_pedido);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        viewModelEstado= new ViewModelProvider(this).get(Orden_estado_deliveryViewModel.class);
        viewModelEstado.init();if (getArguments() != null) {
            mGsonDelivery_pedido=(GsonDelivery_Pedido) getArguments().getSerializable(ARG_HOME);
            mDelivery_pedido=mGsonDelivery_pedido.getDelivery_information();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transformLatLngToLocation();

        declararWidgets(view);

        setDataWidget();

        sendDataEstado();

        clickAceptarPedido();

        reciveLocationUpdate();

        navigateSinceGoogleMapsAPP();

        observeDistanceAndTime();
    }


    private void transformLatLngToLocation(){

        temp=new Location(LocationManager.GPS_PROVIDER);

        temp.setLatitude(Double.valueOf(mDelivery_pedido.getEmpresa_coordenada_x().trim()));
        temp.setLongitude(Double.valueOf(mDelivery_pedido.getEmpresa_coordenada_y().trim()));


    }

    private void declararWidgets(View view){
        fragment_step2_DIRECCION_TIENDA=view.findViewById(R.id.fragment_step2_DIRECCION_TIENDA);
        fragment_step2_DISTANCIA=view.findViewById(R.id.fragment_step2_DISTANCIA);
        fragment_step2_TIEMPO=view.findViewById(R.id.fragment_step2_TIEMPO);
        fragment_step2_NOMBRE_TIENDA=view.findViewById(R.id.fragment_step2_NOMBRRE_TIENDA);
        fragment_ste2_NAVEGAR_GOOGLE_MAPS=view.findViewById(R.id.fragment_ste2_NAVEGAR_GOOGLE_MAPS);


        mView=view.findViewById(R.id.layout_button_progress);

        progressButon= new ProgressButon(getContext(),view,"Estoy en el negocio",2);
        progressButon.initName();


    }

    private void setDataWidget(){

        fragment_step2_NOMBRE_TIENDA.setText(mDelivery_pedido.getNombre_empresa());
        fragment_step2_DIRECCION_TIENDA.setText(mDelivery_pedido.getDireccion_empresa());

    }


    private void sendDataEstado(){
        viewModelEstado.getOrden_estado_deliveryLiveData().observe(getViewLifecycleOwner(), new Observer<Orden_estado_delivery>() {
            @Override
            public void onChanged(Orden_estado_delivery orden_estado_delivery) {

                progressButon.buttoFinished();
                mView.setEnabled(true);

                if(orden_estado_delivery!=null){

                    System.out.println("LLEGAMOS AL DESTINO INDICADO");

                    RxBus.getInstance().publishStep2Fragment(true);

                }else{
                    System.out.println("ERRORRRRRRRRRRRRR");

                }
            }
        });
    }

    private void clickAceptarPedido(){

        mView.setOnClickListener( v->{


            if(llegoTienda){


                mView.setEnabled(false);

                progressButon.buttonActivated();

                Orden_estado_deliveryPK pk = new Orden_estado_deliveryPK();
                pk.setIdventa(mDelivery_pedido.getIdventa());
                pk.setIdestado_delivery(2);

                Orden_estado_delivery orden_estado_delivery= new Orden_estado_delivery();
                orden_estado_delivery.setId(pk);
                orden_estado_delivery.setIdrepartidor(mDelivery_pedido.getIdrepartidor());
                orden_estado_delivery.setFecha(null);

                viewModelEstado.updateEstado(orden_estado_delivery,mDelivery_pedido.getIdusuario());

            }else {

                Toast.makeText(getContext(), "Lo siento aun no llegas", Toast.LENGTH_LONG).show();

            }



        });



    }

    private void navigateSinceGoogleMapsAPP(){

        fragment_ste2_NAVEGAR_GOOGLE_MAPS.setOnClickListener( v->{

            String origen=mLatLng.latitude+","+mLatLng.longitude;

            String destino=mDelivery_pedido.getEmpresa_coordenada_x()+","+mDelivery_pedido.getEmpresa_coordenada_y();

            String ubicacion="https://www.google.com/maps/dir/?api=1&origin="+origen+"&destination="+destino+"&travelmode=driving";



            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(ubicacion));
            startActivity(intent);

        });
    }



    private void calculateDistance(Location locationChange){
        float[] distance = new float[2];
        Location.distanceBetween( temp.getLatitude(), temp.getLongitude(),locationChange.getLatitude(), locationChange.getLongitude(), distance);

        //LA DISTANCIA ESTA EN METROS
        if( distance[0] > 3  ){
            Toast.makeText(getContext(), "AUN NO LLEGAS AL PUNTO INDICADO PUTASOOOOOOOOOOOO", Toast.LENGTH_LONG).show();
            System.out.println("AUN NO LLEGAS AL PUNTO INDICADO PUTASOOOOOOOOOOOO");
        } else {
            llegoTienda=true;
           // Toast.makeText(getContext(), "YA LLEGASTE FELICIDADES" , Toast.LENGTH_LONG).show();
        }

    }


    private void reciveLocationUpdate(){

        RxBus.getInstance().listenLocation().subscribe(new io.reactivex.Observer<Location>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Location location) {

                //calculateDistance(location);

                mLatLng= new LatLng(location.getLatitude(),location.getLongitude());

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

      /*  pageViewModel_location.getRequest_step2_location().observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {

                calculateDistance(location);

            }
        });*/
    }


    private void observeDistanceAndTime(){
        RxBus.getInstance().listenDistanceAndTime().subscribe(new io.reactivex.Observer<HashMap<String,String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HashMap<String,String> distancia_tiempo) {

                Toast.makeText(getContext(),"distance" +distancia_tiempo.get("distance"),Toast.LENGTH_LONG).show();

                fragment_step2_DISTANCIA.setText(distancia_tiempo.get("distance"));

                fragment_step2_TIEMPO.setText(distancia_tiempo.get("duration"));


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
