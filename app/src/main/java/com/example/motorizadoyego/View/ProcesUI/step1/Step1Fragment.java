package com.example.motorizadoyego.View.ProcesUI.step1;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Orden_estado_delivery;
import com.example.motorizadoyego.Repository.Modelo.Orden_estado_deliveryPK;
import com.example.motorizadoyego.Util.PageViewModel;
import com.example.motorizadoyego.Util.ProgressButon;
import com.example.motorizadoyego.Util.RxBus;
import com.example.motorizadoyego.ViewModel.Orden_estado_deliveryViewModel;
import java.util.concurrent.TimeUnit;


public class Step1Fragment extends Fragment {


    private static String GSON_DELIVERY="com.example.motorizadoyego.View.ui.home";

    private Orden_estado_deliveryViewModel viewModelEstado;

    private Delivery_Pedido mDelivery_pedido;

    private TextView fragment_orden_GANANCIA,fragment_orden_DISTANCIA,fragment_orden_TIEMPO,fragment_orden_ORIGEN,fragment_orden_DESTINO,fragment_step1_TIME;

    //private Button fragment_orden_ACEPTAR;

    private ProgressBar fragment_step1_TIME_PROGRES;

    private static SparseArray<CountDownTimer> countDownMap;

    private ImageButton fragment_step1_CERRAR;

    private PageViewModel pageViewModel;

    private CountDownTimer countDownTimer;

    private View mView;

    private ProgressButon progressButon;

    public Step1Fragment() { }

    public static Step1Fragment newInstanceStep1Fragment(GsonDelivery_Pedido gsonDelivery_pedido) {

        Step1Fragment step1Fragment= new Step1Fragment();
        Bundle bundle= new Bundle();
        bundle.putSerializable(GSON_DELIVERY,gsonDelivery_pedido);
        step1Fragment.setArguments(bundle);
        return step1Fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null) {

            Bundle bundle = getArguments();

            GsonDelivery_Pedido gsonDelivery_pedido = (GsonDelivery_Pedido) bundle.getSerializable(GSON_DELIVERY);

            assert gsonDelivery_pedido != null;
            mDelivery_pedido = gsonDelivery_pedido.getDelivery_information();
        }

        pageViewModel =  ViewModelProviders.of(requireActivity()).get(PageViewModel.class);


        viewModelEstado=new  ViewModelProvider(this).get(Orden_estado_deliveryViewModel.class);
        viewModelEstado.init();
        countDownMap= new SparseArray<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_step1, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      declararWidgets(view);

      clickAceptarPedido();

      setDataWidget();

        sendDataEstado();

        clickCerrarPedido();


    }


    private void declararWidgets(View view){

        fragment_orden_GANANCIA=view.findViewById(R.id.fragment_orden_GANANCIA);
        fragment_orden_DISTANCIA=view.findViewById(R.id.fragment_orden_DISTANCIA);
        fragment_orden_TIEMPO=view.findViewById(R.id.fragment_orden_TIEMPO);
        fragment_orden_ORIGEN=view.findViewById(R.id.fragment_orden_ORIGEN);
        fragment_orden_DESTINO=view.findViewById(R.id.fragment_orden_DESTINO);
        //fragment_orden_ACEPTAR=view.findViewById(R.id.fragment_orden_ACEPTAR);
        fragment_step1_TIME_PROGRES=view.findViewById(R.id.fragment_step1_TIME_PROGRES);
        fragment_step1_TIME=view.findViewById(R.id.fragment_step1_TIME);
        fragment_step1_CERRAR=view.findViewById(R.id.fragment_step1_CERRAR);

        mView=view.findViewById(R.id.layout_button_progress);

        progressButon= new ProgressButon(getContext(),view,"Aceptar pedido",1);
        progressButon.initName();


    }

    private void setDataWidget(){
        fragment_orden_GANANCIA.setText(String.valueOf(mDelivery_pedido.getCosto_delivery()));

        fragment_orden_DISTANCIA.setText(mDelivery_pedido.getDistancia_delivery());
        fragment_orden_TIEMPO.setText(mDelivery_pedido.getTiempo());
        fragment_orden_ORIGEN.setText(mDelivery_pedido.getDireccion_empresa());
        fragment_orden_DESTINO.setText(mDelivery_pedido.getUbicacion_nombre());

        fragment_step1_TIME_PROGRES.setMax(60);

         countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {

                String tiempo = hmsTimeFormatter(millisUntilFinished);
                fragment_step1_TIME.setText(tiempo);
                fragment_step1_TIME_PROGRES.setProgress((int) millisUntilFinished / 1000);

            }

            public void onFinish() {

                System.out.println("    no entras a cancelar el pedido despues de 5 segundos");

                Toast.makeText(getContext(),"Acabas de rechazar un pedido!!!",Toast.LENGTH_SHORT).show();

                System.out.println("N");
                pageViewModel.setName(false);
                RxBus.getInstance().publish("FALSE");


            }
        }.start();

        countDownMap.put(fragment_step1_TIME.getId(), countDownTimer);

    }

    private void sendDataEstado(){
        viewModelEstado.getOrden_estado_deliveryLiveData().observe(getViewLifecycleOwner(), orden_estado_delivery -> {

            progressButon.buttoFinished();
            mView.setEnabled(true);

            if(orden_estado_delivery!=null  ){

                Toast.makeText(getContext(),"Se unio a una nueva entrega",Toast.LENGTH_SHORT).show();

                pageViewModel.setName(true);

                RxBus.getInstance().publish("TRUE");

                countDownMap.clear();

                //countDownTimer.onFinish();



            }else{

                Toast.makeText(getContext(),"Hubo un problema! Verificar conexion a internet o intentar otra vez.",Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void clickAceptarPedido(){

        mView.setOnClickListener( v->{

            mView.setEnabled(false);

            progressButon.buttonActivated();

            countDownTimer.cancel();


            Orden_estado_deliveryPK pk = new Orden_estado_deliveryPK();
            pk.setIdventa(mDelivery_pedido.getIdventa());
            pk.setIdestado_delivery(1);

            Orden_estado_delivery orden_estado_delivery= new Orden_estado_delivery();
            orden_estado_delivery.setId(pk);
            orden_estado_delivery.setIdrepartidor(mDelivery_pedido.getIdrepartidor());
            orden_estado_delivery.setFecha(null);

            viewModelEstado.updateEstado(orden_estado_delivery,mDelivery_pedido.getIdusuario());


        });
    }


    private void clickCerrarPedido(){

        fragment_step1_CERRAR.setOnClickListener( v-> pageViewModel.setName(false));
    }

    private String hmsTimeFormatter(long milliSeconds) {
        String hms = String.format("%02d",
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return hms;
    }

}




