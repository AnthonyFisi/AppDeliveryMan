package com.mimiperla.motorizadoyego.View.ProcesUI.step4;


import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.Orden_estado_delivery;
import com.mimiperla.motorizadoyego.Repository.Modelo.Orden_estado_deliveryPK;

import com.mimiperla.motorizadoyego.Util.ProgressButon;
import com.mimiperla.motorizadoyego.View.ProcesUI.listOrder.ListOrderFragment;
import com.mimiperla.motorizadoyego.View.ProcesUI.step1.DirectionsJSONParser;
import com.mimiperla.motorizadoyego.View.ProcesUI.step5.Step5Activity;
import com.mimiperla.motorizadoyego.ViewModel.Orden_estado_deliveryViewModel;
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

import static com.mimiperla.motorizadoyego.R.string.*;



public class Step4Fragment extends Fragment  implements OnMapReadyCallback {


    private static final String ARG_GSON_DELIVERY = "param1";

    private Orden_estado_deliveryViewModel viewModelEstado;

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    private Delivery_Pedido mDelivery_pedido;

    private TextView fragment_step4_DIRECCION_USUARIO,fragment_step4_DISTANCIA,fragment_step4_TIEMPO;

    private CardView fragment_ste5_NAVEGAR_GOOGLE_MAPS;

    private Location temp;

    private boolean llegoTienda;

    private LatLng mLatLng;

    private View mView;

    private ProgressButon progressButon;

    private GoogleMap mMap;

    private LocationCallback locationCallback;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationRequest locationRequest;

    private boolean firstTime;

    private ArrayList points1;


    public Step4Fragment() {
        // Required empty public constructor
    }



    public static Step4Fragment newInstance(GsonDelivery_Pedido gsonDelivery_pedido) {
        Step4Fragment fragment = new Step4Fragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GSON_DELIVERY, gsonDelivery_pedido);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mGsonDelivery_pedido=(GsonDelivery_Pedido) getArguments().getSerializable(ARG_GSON_DELIVERY);
            mDelivery_pedido=mGsonDelivery_pedido.getDelivery_information();

        }

        llegoTienda=true;

        firstTime=true;

       // points1=null;
        points1 = new ArrayList();


        viewModelEstado= ViewModelProviders.of(this).get(Orden_estado_deliveryViewModel.class);

        viewModelEstado.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_step4, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(400);
        locationRequest.setFastestInterval(200);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        chechkSettingAndStartLocationUpdates();

        gpsData();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        declararWidgets(view);

        if(mGsonDelivery_pedido!=null) {




            transformLatLngToLocation();

            MapView mapView = view.findViewById(R.id.google_map_aceptar_orden);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);


            setDataWidget();

            clickLlegueDestino();

            sendDataEstado();

            navigateSinceGoogleMapsAPP();


        }





    }

    private void navigateSinceGoogleMapsAPP(){
        fragment_ste5_NAVEGAR_GOOGLE_MAPS.setOnClickListener( v->{

            String origen=mLatLng.latitude+","+mLatLng.longitude;

            String destino=mDelivery_pedido.getUsuario_coordenada_x()+","+mDelivery_pedido.getUsuario_coordenada_y();

            String ubicacion="https://www.google.com/maps/dir/?api=1&origin="+origen+"&destination="+destino+"&travelmode=driving";



            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(ubicacion));
            startActivity(intent);

        });
    }

    private void transformLatLngToLocation(){

        temp=new Location(LocationManager.GPS_PROVIDER);

        temp.setLatitude(Double.valueOf(mDelivery_pedido.getEmpresa_coordenada_x().trim()));
        temp.setLongitude(Double.valueOf(mDelivery_pedido.getEmpresa_coordenada_y().trim()));


    }


    private void declararWidgets(View view){

        fragment_step4_DIRECCION_USUARIO=view.findViewById(R.id.fragment_step4_DIRECCION_USUARIO);
        fragment_step4_TIEMPO=view.findViewById(R.id.fragment_step4_TIEMPO);
        fragment_step4_DISTANCIA=view.findViewById(R.id.fragment_step4_DISTANCIA);
        fragment_ste5_NAVEGAR_GOOGLE_MAPS=view.findViewById(R.id.fragment_ste5_NAVEGAR_GOOGLE_MAPS);

        mView=view.findViewById(R.id.layout_button_progress);

        progressButon= new ProgressButon(getContext(),view,"Llegue al destino",2);
        progressButon.initName();

    }

    private void setDataWidget(){

        fragment_step4_DIRECCION_USUARIO.setText(mDelivery_pedido.getUbicacion_nombre());
        fragment_step4_TIEMPO.setText(String.valueOf(mDelivery_pedido.getTiempo()));
        fragment_step4_DISTANCIA.setText(String.valueOf(mDelivery_pedido.getDistancia_delivery()));

    }


    private void clickLlegueDestino(){

        if(llegoTienda){
            mView.setOnClickListener( v->{

                mView.setEnabled(false);

                progressButon.buttonActivated();

                Orden_estado_deliveryPK pk = new Orden_estado_deliveryPK();
                pk.setIdventa(mDelivery_pedido.getIdventa());
                pk.setIdestado_delivery(4);

                Orden_estado_delivery orden_estado_delivery= new Orden_estado_delivery();
                orden_estado_delivery.setId(pk);
                orden_estado_delivery.setIdrepartidor(mDelivery_pedido.getIdrepartidor());
                orden_estado_delivery.setFecha(null);

                viewModelEstado.updateEstado(orden_estado_delivery,mDelivery_pedido.getIdusuario());

            });
        }else {

            Toast.makeText(getContext(), "Lo siento aun no llegas", Toast.LENGTH_LONG).show();
        }


    }



    private void sendDataEstado(){
        viewModelEstado.getOrden_estado_deliveryLiveData().observe(getViewLifecycleOwner(), new Observer<Orden_estado_delivery>() {
            @Override
            public void onChanged(Orden_estado_delivery orden_estado_delivery) {

                progressButon.buttoFinished();
                mView.setEnabled(true);

                if(orden_estado_delivery!=null){

                    System.out.println("FUE ACTUALIZADO EXITOSAMENTE ");


                    mDelivery_pedido.setIdestado_delivery(orden_estado_delivery.getId().getIdestado_delivery());

                    //ListOrderFragment.updateState(mDelivery_pedido);

                    stopLocationUpdates();

                   //6 RxBus.getInstance().publishStep4Fragment(mDelivery_pedido);
                  //  Step4FragmentDirections.ActionStep4FragmentToStep5Fragment action=Step4FragmentDirections.actionStep4FragmentToStep5Fragment(mGsonDelivery_pedido,mGsonDelivery_pedido);
                    //nav.navigate(action);

                    Intent intent= Step5Activity.newIntentStep5Activity(getContext(),mDelivery_pedido);
                    startActivity(intent);

                    requireActivity().finish();



                }else{
                    System.out.println("ERRORRRRRRRRRRRRR");

                }
            }
        });
    }








    private void calculateDistance(Location locationChange){



        float[] distance = new float[2];


        Location.distanceBetween( temp.getLatitude(), temp.getLongitude(),locationChange.getLatitude(), locationChange.getLongitude()
                , distance);

        //LA DISTANCIA ESTA EN METROS
        if( distance[0] > 3  ){
            Toast.makeText(getContext(), "AUN NO LLEGAS AL PUNTO INDICADO", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "YA LLEGASTE FELICIDADES" , Toast.LENGTH_LONG).show();
        }


    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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

                    mLatLng=new LatLng(location.getLatitude(),location.getLongitude());

                    System.out.println("ALWAYS DRAW");

                    if(mDelivery_pedido!=null){
                        drawRoute(location);

                    }

                }
            }
        };
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

                        apiException.startResolutionForResult(getActivity(), 1001);
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


    private void drawRoute(Location mLocation) {

        double latDes = 0, logDes = 0;

        mMap.clear();

        ArrayList markerPoints=new ArrayList();

        double latOri = mLocation.getLatitude();

        double logOri = mLocation.getLongitude();

        LatLng locationOrigen = new LatLng(latOri, logOri);

        markerPoints.add(locationOrigen);

        // Creating MarkerOptions
     //   MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        //  options.position(locationOrigen);
        // options.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gps));

        mMap.addCircle(
                new CircleOptions()
                .center(locationOrigen)
                .radius(16)
                .strokeWidth(10)
                .strokeColor(getResources().getColor(R.color.mainColorTransparent))
                .fillColor(getResources().getColor(R.color.mainColor))
        );

       // options.title("Yo");

      //  mMap.addMarker(options).showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationOrigen, 16));

        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(locationOrigen)      // Sets the center of the map to Mountain View
                .zoom(18)                   // Sets the zoom
                .bearing(45)                // Sets the orientation of the camera to east
                .tilt(70)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        latDes = Double.parseDouble(mDelivery_pedido.getUsuario_coordenada_x().trim());
        logDes = Double.parseDouble(mDelivery_pedido.getUsuario_coordenada_y().trim());
        LatLng locationDestino = new LatLng(latDes, logDes);


        markerPoints.add(locationDestino);

        // Creating MarkerOptions
        MarkerOptions options2 = new MarkerOptions();

        // Setting the position of the marker
        options2.position(locationDestino);
        options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconhome));
        //options2.title(mDelivery_pedido.getUbicacion_comentarios());
       // options2.snippet("Destino");
        mMap.addMarker(options2);

        mMap.addMarker(options2).showInfoWindow();


        LatLng origin = (LatLng) markerPoints.get(0);
        LatLng dest = (LatLng) markerPoints.get(1);


        if(firstTime){
            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            System.out.println(url);

            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);

            firstTime=false;

        }


        System.out.println(points1.size()+"TAMANO");

        if(points1.size()>0){



            PolylineOptions lineOptions = new PolylineOptions();

            lineOptions.addAll(points1);
            lineOptions.width(7);
            lineOptions.color(getResources().getColor(R.color.mainColorTransparent));
            lineOptions.geodesic(true);

            mMap.addPolyline(lineOptions);
        }

    }

    private  void calculateDistanceAndTime(String data) {

        JSONObject parse ;
        JSONArray jRoutes;
        JSONArray jLegs;
        try {
            parse = new JSONObject(data);
            jRoutes=parse.getJSONArray("routes");
            jLegs = ( (JSONObject)jRoutes.get(0)).getJSONArray("legs");

            for(int j=0;j<jLegs.length();j++) {

                JSONObject object= jLegs.getJSONObject(j);

               // System.out.println("DISTANCIA" + object.getJSONObject("distance").get("text")+"SOLO DISTANCIA");

                String dis=object.getJSONObject("distance").get("text").toString();

                //listaTiempoDistancia.put("distance",dis);

               // System.out.println("TIEMPO "+object.getJSONObject("duration").get("text")+"SOLO DISTANCIA");

                String tiem=object.getJSONObject("duration").get("text").toString();

               // listaTiempoDistancia.put("duration",tiem);

                fragment_step4_DISTANCIA.setText(dis);

                fragment_step4_TIEMPO.setText(tiem);


            }

            //PUBLICANDO LA DISTANCIA Y TIEMPO
           // RxBus.getInstance().publishDistanceAndTime(listaTiempoDistancia);



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
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            ArrayList points=null ;


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
                    points1.add(position);

                }

                lineOptions.addAll(points);
                lineOptions.width(7);

                lineOptions.color(getResources().getColor(R.color.mainColorTransparent));
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

        String API_KEY = "&key="+getString(googlemaps_place_apikey);

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



}
