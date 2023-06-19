package com.mimiperla.motorizadoyego.View.ProcesUI.step6;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mimiperla.motorizadoyego.MainActivity;
import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.Calificacion_Servicio;
import com.mimiperla.motorizadoyego.Repository.Modelo.Calificacion_Usuario;
import com.mimiperla.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.mimiperla.motorizadoyego.Util.ProgressButon;
import com.mimiperla.motorizadoyego.Util.RxBus;
import com.mimiperla.motorizadoyego.View.ProcesUI.listOrder.ListOrderFragment;
import com.mimiperla.motorizadoyego.ViewModel.Calificacion_ServicioViewModel;
import com.mimiperla.motorizadoyego.ViewModel.Calificacion_UsuarioViewModel;
import com.hsalf.smileyrating.SmileyRating;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Step6Fragment extends Fragment {

    private static final String ARG_GSON_DELIVERY = "argumento";

    private Boolean calificacion_servicio=false,calificacion_usuario=false;

    private Calificacion_ServicioViewModel viewModel_Servicio;

    private Calificacion_UsuarioViewModel viewModel_Usuario;

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    private int puntaje_usuario=0,puntaje_servicio=0;

    private Delivery_Pedido mDelivery_pedido;

    private View mView;

    private ProgressButon progressButon;

    private LinearLayout fragment_inicio;

    public Step6Fragment() {
        // Required empty public constructor
    }

    public static Step6Fragment newInstance(GsonDelivery_Pedido gsonDelivery_pedido) {
        Step6Fragment fragment = new Step6Fragment();
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
        viewModel_Servicio= new ViewModelProvider(this).get(Calificacion_ServicioViewModel.class);
        viewModel_Servicio.init();

        viewModel_Usuario= new ViewModelProvider(this).get(Calificacion_UsuarioViewModel.class);
        viewModel_Usuario.init();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step6, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        if(mGsonDelivery_pedido!=null) {

            declararServicioWidget(view);
            declararUsuarioWidget(view);

            mView = view.findViewById(R.id.layout_button_progress);

            progressButon = new ProgressButon(getContext(), view, "Enviar calificacion",5);
            progressButon.initName();

            fragment_inicio = view.findViewById(R.id.fragment_inicio);


            clickSendCalificacion();

            requestDataCalificacion();

            clickInicio();

        }

    }

    private void clickSendCalificacion(){
        mView.setOnClickListener( v->{
            if(calificacion_servicio && calificacion_usuario){

                mView.setEnabled(false);

                progressButon.buttonActivated();

                sendData();

            }
        });
    }

    private void sendData() {

        Calificacion_Usuario calificacion_usuario= new Calificacion_Usuario();
        calificacion_usuario.setIdcalificacion_usuario(0);
        calificacion_usuario.setIdventa(mGsonDelivery_pedido.getDelivery_information().getIdventa());
        calificacion_usuario.setIdusuario(mGsonDelivery_pedido.getDelivery_information().getIdusuariogeneral());
        calificacion_usuario.setCalificacion(puntaje_usuario);

        viewModel_Usuario.agregarCalificacion(calificacion_usuario);


        Calificacion_Servicio calificacion_servicio= new Calificacion_Servicio();
        calificacion_servicio.setIdcalificacion_servicio(0);
        calificacion_servicio.setIdventa(mGsonDelivery_pedido.getDelivery_information().getIdventa());
        calificacion_servicio.setIdusuario(Repartidor_Bi.repartidor_bi.getIdusuariogeneral());
        calificacion_servicio.setCalificacion(puntaje_servicio);

        viewModel_Servicio.agregarCalificacion(calificacion_servicio);
    }

    private void requestDataCalificacion(){
        viewModel_Usuario.getCalificacion_usuarioLiveData().observe(getViewLifecycleOwner(), new Observer<Calificacion_Usuario>() {
            @Override
            public void onChanged(Calificacion_Usuario calificacion_usuario) {
                progressButon.buttoFinished();
                mView.setEnabled(true);

                if(calificacion_usuario!=null){

                    //RxBus.getInstance().publishStep6Fragment(mDelivery_pedido);
                    Toast.makeText(getContext(),"Calificacion fue enviado con exito",Toast.LENGTH_LONG).show();

                    //ELIMINAR LA ACTIVYT Y VOLVER ABRIR LA ACTIVITY
                    ListOrderFragment.deleteState(mDelivery_pedido.getIdventa());

                    Intent intent= MainActivity.newIntentMainActivity(getContext());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();

                }else{
                    Toast.makeText(getContext(),"Sucedio un error,volver a intentarlo",Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    private  void declararUsuarioWidget(View view ) {



        SmileyRating smileyRating = view.findViewById(R.id.smile_rating_USUARIO);

        smileyRating.setTitle(SmileyRating.Type.GREAT, "Muy bueno");
        smileyRating.setTitle(SmileyRating.Type.GOOD, "Bueno");
        smileyRating.setTitle(SmileyRating.Type.OKAY, "Normal");
        smileyRating.setTitle(SmileyRating.Type.BAD, "Malo");
        smileyRating.setTitle(SmileyRating.Type.TERRIBLE, "Terrible");

        smileyRating.setRating(SmileyRating.Type.OKAY);


        smileyRating.setSmileySelectedListener(type -> {
            if (SmileyRating.Type.GREAT == type) {
                Log.i(TAG, "Wow, the user gave high rating");
                puntaje_usuario=5;
                calificacion_usuario=true;
            }

            if (SmileyRating.Type.GOOD == type) {
                Log.i(TAG, "Wow, the user gave high rating");
                puntaje_usuario=4;
                calificacion_usuario=true;
            }

            if (SmileyRating.Type.OKAY == type) {
                Log.i(TAG, "Wow, the user gave high rating");
                puntaje_usuario=3;
                calificacion_usuario=true;
            }

            if (SmileyRating.Type.BAD == type) {
                Log.i(TAG, "Wow, the user gave high rating");
                puntaje_usuario=2;
                calificacion_usuario=true;
            }

            if (SmileyRating.Type.TERRIBLE == type) {
                Log.i(TAG, "Wow, the user gave high rating");
                puntaje_usuario=1;
                calificacion_usuario=true;
            }
        });


    }

    private  void declararServicioWidget(View view ) {



        SmileyRating smileyRating = view.findViewById(R.id.smile_rating_SERVICIO);

        smileyRating.setTitle(SmileyRating.Type.GREAT, "Muy bueno");
        smileyRating.setTitle(SmileyRating.Type.GOOD, "Bueno");
        smileyRating.setTitle(SmileyRating.Type.OKAY, "Normal");
        smileyRating.setTitle(SmileyRating.Type.BAD, "Malo");
        smileyRating.setTitle(SmileyRating.Type.TERRIBLE, "Terrible");

        smileyRating.setRating(SmileyRating.Type.OKAY);


        smileyRating.setSmileySelectedListener(type -> {
            if (SmileyRating.Type.GREAT == type) {
                Log.i(TAG, "Wow, the user gave high rating");
                puntaje_servicio=5;
                calificacion_servicio=true;
            }

            if (SmileyRating.Type.GOOD == type) {
                Log.i(TAG, "Wow, the user gave high rating");
                puntaje_servicio=4;
                calificacion_servicio=true;
            }

            if (SmileyRating.Type.OKAY == type) {
                Log.i(TAG, "Wow, the user gave high rating");
                puntaje_servicio=3;
                calificacion_servicio=true;
            }

            if (SmileyRating.Type.BAD == type) {
                Log.i(TAG, "Wow, the user gave high rating");
                puntaje_servicio=2;
                calificacion_servicio=true;
            }

            if (SmileyRating.Type.TERRIBLE == type) {
                Log.i(TAG, "Wow, the user gave high rating");
                puntaje_servicio=1;
                calificacion_servicio=true;
            }
        });


    }


    private void clickInicio(){

        fragment_inicio.setOnClickListener(v->{
            ListOrderFragment.deleteState(mDelivery_pedido.getIdventa());

            sendData();
            RxBus.getInstance().publishStep6Fragment(mDelivery_pedido);

        });
    }


}
