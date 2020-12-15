package com.example.motorizadoyego.View.ProcesUI.step5;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motorizadoyego.MainActivity;
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
import com.example.motorizadoyego.View.ProcesUI.step3.ProductosResultsAdapter;
import com.example.motorizadoyego.View.ProcesUI.step3.Step3Fragment;
import com.example.motorizadoyego.View.ProcesUI.step6.Step6Activity;
import com.example.motorizadoyego.ViewModel.Orden_estado_deliveryViewModel;
import com.example.motorizadoyego.ViewModel.ProductoJOINregistroPedidoJOINpedidoViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.CALL_PHONE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.ContextCompat.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step5Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step5Fragment extends Fragment implements  ProductosResultsAdapter.ClickPedidoDetalle{

    private static final String ARG_GSON_DELIVERY = "pedido";

    private Orden_estado_deliveryViewModel viewModelEstado;

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    private Delivery_Pedido mDelivery_pedido;

    private TextView  fragment_step5_NOMBRE_CLIENTE,fragment_step5_ESTADO_PAGO;

   //private LinearLayout fragment_step5_PAGAR;

    private View mView;

    private ProgressButon progressButon;

    private RecyclerView fragment_step3_LISTA_PRODUCTOS;

    private ProductoJOINregistroPedidoJOINpedidoViewModel viewModelProducto;

    private ProductosResultsAdapter adapter;

    private CardView cardView_mensaje,cardView_llamar;

    private TextView textView_ubicacion,textView_detalle_ubicacion,textView_detalle_extra_ubicacion;

    public Step5Fragment() {
        // Required empty public constructor
    }

    public static Step5Fragment newInstance(GsonDelivery_Pedido gsonDelivery_pedido) {
        Step5Fragment fragment = new Step5Fragment();
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

        viewModelEstado= ViewModelProviders.of(this).get(Orden_estado_deliveryViewModel.class);
        viewModelEstado.init();

        viewModelProducto= new ViewModelProvider(this).get(ProductoJOINregistroPedidoJOINpedidoViewModel.class);
        viewModelProducto.init();

        adapter= new ProductosResultsAdapter();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step5, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        declararWidgets(view);

        if(getArguments()!=null) {

            setDataWidget();

            clickLlegueDestino();

            sendDataEstado();

            viewModelProducto.listaCarrito(mDelivery_pedido.getIdpedido());

            loadDataPedido();

            clickCallPhoneAndMessage();
        }
    }



    private void declararWidgets(View view){

        fragment_step5_NOMBRE_CLIENTE=view.findViewById(R.id.fragment_step5_NOMBRE_CLIENTE);
        fragment_step5_ESTADO_PAGO=view.findViewById(R.id.fragment_step5_ESTADO_PAGO);

        cardView_mensaje=view.findViewById(R.id.cardView_mensaje);

        cardView_llamar=view.findViewById(R.id.cardView_llamar);



        //fragment_step5_NOPAGAR=view.findViewById(R.id.fragment_step5_NOPAGAR);
       // fragment_step5_PAGAR=view.findViewById(R.id.fragment_step5_PAGAR);

        mView=view.findViewById(R.id.layout_button_progress);

        progressButon= new ProgressButon(getContext(),view,"Pedido entregado",3);
        progressButon.initName();

        fragment_step3_LISTA_PRODUCTOS=view.findViewById(R.id.fragment_step3_LISTA_PRODUCTOS);
        fragment_step3_LISTA_PRODUCTOS.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        fragment_step3_LISTA_PRODUCTOS.setAdapter(adapter);


        textView_ubicacion=view.findViewById(R.id.textView_ubicacion);

        textView_detalle_ubicacion=view.findViewById(R.id.textView_detalle_ubicacion);

        textView_detalle_extra_ubicacion=view.findViewById(R.id.textView_detalle_extra_ubicacion);

    }

    private void setDataWidget(){


        fragment_step5_NOMBRE_CLIENTE.setText(mDelivery_pedido.getNombre());
        String costoTotal="S/."+mDelivery_pedido.getVenta_costototal();

        fragment_step5_ESTADO_PAGO.setText(costoTotal);


        textView_ubicacion.setText(mDelivery_pedido.getUbicacion_nombre());


        if(mDelivery_pedido.getUbicacion_comentarios().length()>0){

            textView_detalle_ubicacion.setVisibility(View.VISIBLE);
            textView_detalle_ubicacion.setText(mDelivery_pedido.getUbicacion_comentarios());

        }

        if(mDelivery_pedido.getUbicacion_comentarios().length()>0){

            textView_detalle_extra_ubicacion.setVisibility(View.VISIBLE);
            textView_detalle_extra_ubicacion.setText(mDelivery_pedido.getExtra_ubicacion_comentario());

        }




    }


    private void clickLlegueDestino(){

        mView.setOnClickListener( v->{

            mView.setEnabled(false);

            progressButon.buttonActivated();

            Orden_estado_deliveryPK pk = new Orden_estado_deliveryPK();
            pk.setIdventa(mDelivery_pedido.getIdventa());
            pk.setIdestado_delivery(5);

            Orden_estado_delivery orden_estado_delivery= new Orden_estado_delivery();
            orden_estado_delivery.setId(pk);
            orden_estado_delivery.setIdrepartidor(mDelivery_pedido.getIdrepartidor());
            orden_estado_delivery.setFecha(null);

            viewModelEstado.updateEstado(orden_estado_delivery,mDelivery_pedido.getIdusuario());

        });
    }



    private void sendDataEstado(){
        viewModelEstado.getOrden_estado_deliveryLiveData().observe(getViewLifecycleOwner(), new Observer<Orden_estado_delivery>() {
            @Override
            public void onChanged(Orden_estado_delivery orden_estado_delivery) {

                progressButon.buttoFinished();
                mView.setEnabled(true);

                if(orden_estado_delivery!=null){

                    Toast.makeText(getContext(),"Fue actualizado correctamente",Toast.LENGTH_SHORT).show();


                    mDelivery_pedido.setIdestado_delivery(orden_estado_delivery.getId().getIdestado_delivery());

                    ListOrderFragment.updateState(mDelivery_pedido);

                   // RxBus.getInstance().publishStep5Fragment(mDelivery_pedido );

                    Intent intent= Step6Activity.newIntentStep6Activity(getContext(),mDelivery_pedido);
                    startActivity(intent);
                    requireActivity().finish();

                }else{
                    Toast.makeText(getContext(),"Hubo un problema! Verificar conexion a internet o intentar otra vez.",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void loadDataPedido(){
        viewModelProducto.getProductoJOINregistroPedidoJOINpedidoLiveData().observe(getViewLifecycleOwner(), new Observer<GsonProductoJOINregistroPedidoJOINpedido>() {
            @Override
            public void onChanged(GsonProductoJOINregistroPedidoJOINpedido gsonProductoJOINregistroPedidoJOINpedido) {
                if(gsonProductoJOINregistroPedidoJOINpedido !=null){

                    if(gsonProductoJOINregistroPedidoJOINpedido.getListaProductoJOINregistroPedidoJOINpedido()!=null){
                        adapter.setResults(gsonProductoJOINregistroPedidoJOINpedido.getListaProductoJOINregistroPedidoJOINpedido(), Step5Fragment.this);

                    }

                }
            }
        });
    }

    @Override
    public void pedidoDetalle(ProductoJOINregistroPedidoJOINpedido producto) {

    }

    private void clickCallPhoneAndMessage(){

        cardView_mensaje.setOnClickListener(v->{

            boolean installed= appInstalledOrNot("com.whatsapp");

            if(installed){
                Intent intent= new Intent(Intent.ACTION_VIEW);
                String message="Pedido  #"+mDelivery_pedido.getIdventa();
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+51"+mDelivery_pedido.getCelular()+"&text="+message));
                startActivity(intent);
            }else {

                Toast.makeText(getContext(),"Whatsapp no esta instalado",Toast.LENGTH_SHORT).show();

            }
        });


        cardView_llamar.setOnClickListener(v->{
            Intent intent= new Intent((Intent.ACTION_CALL));
            intent.setData(Uri.parse("tel:"+mDelivery_pedido.getCelular().trim()));

            if(validarPermiso()){

                startActivity(intent);

            }
        });



    }




    private boolean validarPermiso() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(getContext(),CALL_PHONE)== PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CALL_PHONE)) ){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{CALL_PHONE},100);
        }

        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==1 && grantResults[0]== PERMISSION_GRANTED){

                Toast.makeText(getContext(),"Ahora si puedes llamar",Toast.LENGTH_LONG).show();

            }else{
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getActivity().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{CALL_PHONE},100);
                }
            }
        });
        dialogo.show();
    }

    private boolean appInstalledOrNot(String url){
        PackageManager packageManager=getContext().getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            app_installed=true;

        }catch (Exception e){
            app_installed=false;
        }

        return  app_installed;
    }
}

