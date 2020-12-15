package com.example.motorizadoyego.View.ProcesUI.listOrder;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.motorizadoyego.MainActivity;
import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDeliveryPedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Repartidor;
import com.example.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.example.motorizadoyego.Util.RxBus;
import com.example.motorizadoyego.View.ProcesUI.step3.Step3Activity;
import com.example.motorizadoyego.View.ProcesUI.step4.Step4Activity;
import com.example.motorizadoyego.View.ProcesUI.step6.Step6Activity;
import com.example.motorizadoyego.ViewModel.Delivery_PedidoViewModel;
import com.example.motorizadoyego.ViewModel.RepartidorViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;


public class ListOrderFragment extends Fragment implements PedidoResultsAdapter.PedidoInterface {

    private static final String GSON_DELIVERYPEDIDO = "delivery_pedido";

    private OnFragmentInteractionListener mListener;

    private Delivery_PedidoViewModel viewModel;

    private LinearLayout linearlayout_empty;

    private RecyclerView recyclerView_pedidos;

    private ProgressBar progressBar;

    private PedidoResultsAdapter adapter;

    private GsonDeliveryPedido mGsonDeliveryPedido;

    private ImageView imageView_foto_repartidor;

    private TextView textView_codigo,textView_nombre;

    private Repartidor_Bi mRepartidor_bi;

    private RepartidorViewModel viewModelRepartidor;

    private MediaPlayer mediaPlayer;

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    public static List<Delivery_Pedido> lista;

    private SwipeRefreshLayout swipeRefreshLayout_pedido;



    public ListOrderFragment() {
        // Required empty public constructor
    }

    public static ListOrderFragment newInstance(GsonDeliveryPedido gsonDelivery_pedido) {

        ListOrderFragment fragment= new ListOrderFragment();
        Bundle bundle= new Bundle();
        bundle.putSerializable(GSON_DELIVERYPEDIDO,gsonDelivery_pedido);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new PedidoResultsAdapter();

        lista=new ArrayList<>();

        viewModel=new ViewModelProvider(this).get(Delivery_PedidoViewModel.class);
        viewModel.init();
        viewModel.getGsonDeliveryPedidoLiveData().observe(this, new Observer<GsonDeliveryPedido>() {
            @Override
            public void onChanged(GsonDeliveryPedido gsonDeliveryPedido) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout_pedido.setRefreshing(false);

                if(gsonDeliveryPedido!=null){

                    lista.addAll(gsonDeliveryPedido.getListaDeliveryPedido());

                    adapter.setResults(lista,ListOrderFragment.this);

                    linearlayout_empty.setVisibility(View.GONE);


                }else {

                    linearlayout_empty.setVisibility(View.VISIBLE);

                }
            }
        });

        mRepartidor_bi = Repartidor_Bi.repartidor_bi;

        viewModelRepartidor = new ViewModelProvider(this).get(RepartidorViewModel.class);
        viewModelRepartidor.init();

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.notificacion);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        declararWidgets(view);
        setDataWidgets();
        funPusher();
        viewModel.listDeliveryPedido(Repartidor_Bi.repartidor_bi.getIdrepartidor());

        swipeRefreshLayout_pedido.setOnRefreshListener(() -> {

            viewModel.listDeliveryPedido(Repartidor_Bi.repartidor_bi.getIdrepartidor());

            adapter.removeAll();
        });

    }

    private void declararWidgets(View view){

        linearlayout_empty=view.findViewById(R.id.linearlayout_empty);
        recyclerView_pedidos=view.findViewById(R.id.recyclerView_pedidos);
        progressBar=view.findViewById(R.id.progressbar_list_order);

        imageView_foto_repartidor=view.findViewById(R.id.imageView_foto_repartidor);

        textView_codigo=view.findViewById(R.id.textView_codigo);

        textView_nombre=view.findViewById(R.id.textView_nombre);

        swipeRefreshLayout_pedido=view.findViewById(R.id.swipeRefreshLayout_pedido);

    }

    private void setDataWidgets(){
        linearlayout_empty.setVisibility(View.GONE);

        recyclerView_pedidos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView_pedidos.setAdapter(adapter);

        if (mRepartidor_bi.getFoto() != null) {
            String imageUrl = Repartidor_Bi.repartidor_bi.getFoto()
                    .replace("http://", "https://");

            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView_foto_repartidor);
        }

        String codio_repartidor="#"+mRepartidor_bi.getIdrepartidor();
        textView_codigo.setText(codio_repartidor);

        textView_nombre.setText(mRepartidor_bi.getNombre_usuario());



        //CUENTA REPARTIDOR

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void clickPedido(Delivery_Pedido delivery_pedido) {


        stateViewRepartidor(delivery_pedido);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void funPusher() {
        MainActivity.channel.bind("my-event", (channelName, eventName, data) -> {
            if (getActivity() != null) {

                getActivity().runOnUiThread(() -> {
                    try {

                        mediaPlayer.start();

                         vibrate(2000);

                        linearlayout_empty.setVisibility(View.GONE);

                        JsonParser parser = new JsonParser();
                        JsonElement mJson = parser.parse(data);
                        Gson gson = new Gson();

                        mGsonDelivery_pedido = gson.fromJson(mJson, GsonDelivery_Pedido.class);

                        lista.add(mGsonDelivery_pedido.getDelivery_information());

                        adapter.addItem(mGsonDelivery_pedido.getDelivery_information(), ListOrderFragment.this);

                       // publishNotification(mGsonDelivery_pedido.getDelivery_information().getIdventa(),
                         //       mGsonDelivery_pedido.getDelivery_information().getUbicacion_nombre());

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });
            }

        });
        MainActivity.pusher.connect();
    }


    private void vibrate(long millis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE))
                    .vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {

            ((Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE)).vibrate(millis);
        }
    }

/*    private void publishNotification(int idventa,String ubicacion){
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

    }*/

    private void stateViewRepartidor(Delivery_Pedido delivery_pedido){

        if(delivery_pedido.getIdestado_delivery()==1){

            Intent intent= Step3Activity.newIntentStep3Activity(getContext(),delivery_pedido);
            startActivity(intent);
        }
        if(delivery_pedido.getIdestado_delivery()==3){


            Intent intent= Step4Activity.newIntentStep4Activity(getContext(),delivery_pedido);
            startActivity(intent);
        }

        if(delivery_pedido.getIdestado_delivery()==4){

            Intent intent= Step6Activity.newIntentStep6Activity(getContext(),delivery_pedido);
            startActivity(intent);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if(lista.size()>0){

            adapter.notifyDataSetChanged();

        }
    }

    public static void updateState(Delivery_Pedido delivery_pedido){

        for(Delivery_Pedido pedido:lista){

            if(pedido.getIdventa()==delivery_pedido.getIdventa()){

                pedido.setIdestado_delivery(pedido.getIdestado_delivery());
            }
        }
    }

    public static void deleteState(int idventa){

        int position=0;
        int count=0;

        for(Delivery_Pedido pedido:lista){

            if(pedido.getIdventa()==idventa){
                position=count;
            }

            count++;
        }

        lista.remove(position);
    }
}
