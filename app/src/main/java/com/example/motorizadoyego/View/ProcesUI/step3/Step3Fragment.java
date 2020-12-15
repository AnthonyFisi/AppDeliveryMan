package com.example.motorizadoyego.View.ProcesUI.step3;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonProductoJOINregistroPedidoJOINpedido;
import com.example.motorizadoyego.Repository.Modelo.Orden_estado_delivery;
import com.example.motorizadoyego.Repository.Modelo.Orden_estado_deliveryPK;
import com.example.motorizadoyego.Repository.Modelo.ProductoJOINregistroPedidoJOINpedido;

import com.example.motorizadoyego.Util.ProgressButon;
import com.example.motorizadoyego.Util.RxBus;
import com.example.motorizadoyego.View.ProcesUI.listOrder.ListOrderFragment;
import com.example.motorizadoyego.View.ProcesUI.step4.Step4Activity;
import com.example.motorizadoyego.ViewModel.Orden_estado_deliveryViewModel;
import com.example.motorizadoyego.ViewModel.ProductoJOINregistroPedidoJOINpedidoViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class Step3Fragment extends Fragment  implements ProductosResultsAdapter.ClickPedidoDetalle {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_GSON_DELIVERY = "param1";

   // private NavController navController;

    private Orden_estado_deliveryViewModel viewModelEstado;

    private ProductoJOINregistroPedidoJOINpedidoViewModel viewModelProducto;

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    private Delivery_Pedido mDelivery_pedido;

    private RecyclerView fragment_step3_LISTA_PRODUCTOS;

    private ProductosResultsAdapter adapter;

   // private TextView  fragment_step3_CODIGO_PEDIDO,fragment_step3_ESTADO_PAGO;

    private LinearLayout fragment_step3_NOPAGAR,fragment_step3_PAGAR;

    private View mView;

    private ProgressButon progressButon;

    private TextView textView_codigo_pedido,textView_hora,textView_ubicacion,textView_detalle_ubicacion,
            textView_total,textView_detalle_extra_ubicacion;

    private CardView cardView_google_maps;

    private LatLng mLatLng;

    private LocationCallback locationCallback;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationRequest locationRequest;

    public Step3Fragment() {
        // Required empty public constructor
    }

    public static Step3Fragment newInstance(GsonDelivery_Pedido gsonDelivery_pedido) {
        Step3Fragment fragment = new Step3Fragment();
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

        adapter= new ProductosResultsAdapter();
        viewModelEstado= ViewModelProviders.of(this).get(Orden_estado_deliveryViewModel.class);
        viewModelEstado.init();


        viewModelProducto= ViewModelProviders.of(this).get(ProductoJOINregistroPedidoJOINpedidoViewModel.class);
        viewModelProducto.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //navController= Navigation.findNavController(view);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(400);
        locationRequest.setFastestInterval(200);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



        declararWidgets(view);

        loadDataPedido();



        setDataWidget();

        sendDataEstado();

        clickProductoListo();

        chechkSettingAndStartLocationUpdates();

        gpsData();

        cardView_google_maps.setOnClickListener(v->{

            String origen=mLatLng.latitude+","+mLatLng.longitude;

            String destino=mDelivery_pedido.getUsuario_coordenada_x()+","+mDelivery_pedido.getUsuario_coordenada_y();

            String ubicacion="https://www.google.com/maps/dir/?api=1&origin="+origen+"&destination="+destino+"&travelmode=driving";



            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(ubicacion));
            startActivity(intent);

        });

    }

    private void loadDataPedido(){
        viewModelProducto.getProductoJOINregistroPedidoJOINpedidoLiveData().observe(getViewLifecycleOwner(), new Observer<GsonProductoJOINregistroPedidoJOINpedido>() {
            @Override
            public void onChanged(GsonProductoJOINregistroPedidoJOINpedido gsonProductoJOINregistroPedidoJOINpedido) {
                if(gsonProductoJOINregistroPedidoJOINpedido !=null){

                    if(gsonProductoJOINregistroPedidoJOINpedido.getListaProductoJOINregistroPedidoJOINpedido()!=null){
                        adapter.setResults(gsonProductoJOINregistroPedidoJOINpedido.getListaProductoJOINregistroPedidoJOINpedido(),Step3Fragment.this);

                    }

                }
            }
        });
    }



    private void declararWidgets(View view){

       // fragment_step3_CODIGO_PEDIDO=view.findViewById(R.id.textView_codigo_pedido);

        //fragment_step3_NOMBRE_TIENDA=view.findViewById(R.id.fragment_step3_NOMBRE_TIENDA);

       // fragment_step3_ESTADO_PAGO=view.findViewById(R.id.fragment_step3_ESTADO_PAGO);

        fragment_step3_NOPAGAR=view.findViewById(R.id.fragment_step3_NOPAGAR);

        fragment_step3_PAGAR=view.findViewById(R.id.fragment_step3_PAGAR);

        fragment_step3_PAGAR.setVisibility(View.GONE);

        fragment_step3_NOPAGAR.setVisibility(View.GONE);


        fragment_step3_LISTA_PRODUCTOS=view.findViewById(R.id.fragment_step3_LISTA_PRODUCTOS);
        fragment_step3_LISTA_PRODUCTOS.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        mView=view.findViewById(R.id.layout_button_progress);

        progressButon= new ProgressButon(getContext(),view,"Iniciar entrega",1);
        progressButon.initName();

        textView_codigo_pedido=view.findViewById(R.id.textView_codigo_pedido);

        textView_hora=view.findViewById(R.id.textView_hora);

        textView_ubicacion=view.findViewById(R.id.textView_ubicacion);

        textView_detalle_ubicacion=view.findViewById(R.id.textView_detalle_ubicacion);

        textView_total=view.findViewById(R.id.textView_total);

        textView_detalle_extra_ubicacion=view.findViewById(R.id.textView_detalle_extra_ubicacion);


        cardView_google_maps=view.findViewById(R.id.cardView_google_maps);
    }

    private void setDataWidget(){


        if(mGsonDelivery_pedido!=null) {

            String codigo = "N° " + mDelivery_pedido.getIdventa();

           // fragment_step3_CODIGO_PEDIDO.setText(codigo);

            //fragment_step3_NOMBRE_TIENDA.setText(mDelivery_pedido.getNombre_empresa());

            String costoTotal = "S/." + mDelivery_pedido.getVenta_costototal();


            textView_codigo_pedido.setText(codigo);

            //mRestaurante_pedido.getve
            String day = (new SimpleDateFormat("EEEE")).format(mDelivery_pedido.getVenta_fechaentrega().getTime()); // "Tuesday"

            String month = (new SimpleDateFormat("MMMM")).format(mDelivery_pedido.getVenta_fechaentrega().getTime()); // "April"

            String year = (new SimpleDateFormat("yyyy")).format(mDelivery_pedido.getVenta_fechaentrega().getTime()); // "April"

            int numberDay = mDelivery_pedido.getVenta_fechaentrega().getDate();

            String pattern = "hh:mm:ss a";
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            String fecha=dateFormat.format(mDelivery_pedido.getVenta_fechaentrega());


            String fecha_entrega = day + " " + numberDay + " de " + month + "," + year+" "+ fecha;

            textView_hora.setText(fecha_entrega);

            textView_ubicacion.setText(mDelivery_pedido.getUbicacion_nombre());


            if(mDelivery_pedido.getUbicacion_comentarios()!=null){

                textView_detalle_ubicacion.setVisibility(View.VISIBLE);
                textView_detalle_ubicacion.setText(mDelivery_pedido.getUbicacion_comentarios());

            }

            if(mDelivery_pedido.getUbicacion_comentarios()!=null){

                textView_detalle_extra_ubicacion.setVisibility(View.VISIBLE);
                textView_detalle_extra_ubicacion.setText(mDelivery_pedido.getExtra_ubicacion_comentario());

            }




            textView_total.setText(costoTotal);


            fragment_step3_LISTA_PRODUCTOS.setAdapter(adapter);

            viewModelProducto.listaCarrito(mDelivery_pedido.getIdpedido());

        }
    }



    private void sendDataEstado(){
        viewModelEstado.getOrden_estado_deliveryLiveData().observe(getViewLifecycleOwner(), new Observer<Orden_estado_delivery>() {
            @Override
            public void onChanged(Orden_estado_delivery orden_estado_delivery) {

                progressButon.buttoFinished();

                mView.setEnabled(true);

                if(orden_estado_delivery!=null){

                    Toast.makeText(getContext(),"Usuario notificado del estado",Toast.LENGTH_SHORT).show();

                    mDelivery_pedido.setIdestado_delivery(orden_estado_delivery.getId().getIdestado_delivery());

                    ListOrderFragment.updateState(mDelivery_pedido);

                    //RxBus.getInstance().publishStep3Fragment(mDelivery_pedido);
                    Intent intent= Step4Activity.newIntentStep4Activity(getContext(),mDelivery_pedido);
                    startActivity(intent);

                    requireActivity().finish();

                }else{
                    Toast.makeText(getContext(),"Sucedio un problema,volver a intentarlo",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void clickProductoListo(){

        mView.setOnClickListener( v->{


            mView.setEnabled(false);

            progressButon.buttonActivated();

            Orden_estado_deliveryPK pk = new Orden_estado_deliveryPK();
            pk.setIdventa(mDelivery_pedido.getIdventa());
            pk.setIdestado_delivery(3);

            Orden_estado_delivery orden_estado_delivery= new Orden_estado_delivery();
            orden_estado_delivery.setId(pk);
            orden_estado_delivery.setIdrepartidor(mDelivery_pedido.getIdrepartidor());
            orden_estado_delivery.setFecha(null);

            viewModelEstado.updateEstado(orden_estado_delivery,mDelivery_pedido.getIdusuario());

        });
    }


    @Override
    public void pedidoDetalle(ProductoJOINregistroPedidoJOINpedido producto) {

      //  Intent intent=DetailProductActivity.newIntentCancelarPedidoActivity(getContext(),producto);
      //  startActivity(intent);


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




}
/*
        Step3FragmentDirections.ActionStep3FragmentToProductoDetalleFragment2 action=Step3FragmentDirections.actionStep3FragmentToProductoDetalleFragment2(producto);

        navController.navigate(action);*/