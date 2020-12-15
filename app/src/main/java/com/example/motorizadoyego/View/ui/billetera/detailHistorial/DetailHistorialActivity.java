package com.example.motorizadoyego.View.ui.billetera.detailHistorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.Gson.HistorialDetailGson;
import com.example.motorizadoyego.Repository.Modelo.ProductoJOINregistroPedidoJOINpedido;
import com.example.motorizadoyego.Repository.Modelo.Repartidor_historial;
import com.example.motorizadoyego.View.ProcesUI.step3.ProductosResultsAdapter;
import com.example.motorizadoyego.ViewModel.HistorialDetailViewModel;

public class DetailHistorialActivity extends AppCompatActivity implements ProductosResultsAdapter.ClickPedidoDetalle{

    private static final String PEDIDO_DETALLE = "pedido_detalle";
    private TextView NUMERO_ORDEN,GANANCIA,DISTANCIA,DIRECCION_EMPRESA,DIRECCION_USUARIO;

    private RecyclerView HISTORIAL_ORDEN,LISTA_PRODUCTOS;

    private ProductosResultsAdapter adapter;

    private DeliveryStateResultsAdapter adapterState;

    private HistorialDetailViewModel viewModel;

    private Repartidor_historial repartidor_historial;

    private Toolbar mToolbar;

    private ProgressBar progresbar;

    private LinearLayout screen_failed,screen_data;

//    private NestedScrollView nestedScrollView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_historial);
        reciveDataIntent();
        declararWidgets();
        setDataWidget();
        initData();
        clickReload();
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v->{onBackPressed();});
    }

    private void initData(){

        adapter= new ProductosResultsAdapter();

        adapterState=new DeliveryStateResultsAdapter();

        viewModel=new ViewModelProvider(this).get(HistorialDetailViewModel.class);
        viewModel.init();
        viewModel.getHistorialDetailGsonLiveData().observe(this, new Observer<HistorialDetailGson>() {
            @Override
            public void onChanged(HistorialDetailGson historialDetailGson) {
                progresbar.setVisibility(View.GONE);
                if(historialDetailGson!=null){

                    screen_failed.setVisibility(View.GONE);
                    screen_data.setVisibility(View.VISIBLE);

                    DIRECCION_EMPRESA.setText(historialDetailGson.getUbicacionEmpresa());
                    DIRECCION_USUARIO.setText(historialDetailGson.getUbicacionUsuario());

                    System.out.println(historialDetailGson.getListaEstados().size()+"tamanño de lsita estados");

                    System.out.println(historialDetailGson.getListaProducto().size()+"tamanño de lista productos");


                    adapter.setResults(historialDetailGson.getListaProducto(),DetailHistorialActivity.this::pedidoDetalle);
                    LISTA_PRODUCTOS.setAdapter(adapter);
                    HISTORIAL_ORDEN.setAdapter(adapterState);


                    adapterState.setResults(historialDetailGson.getListaEstados());
                }else {

                    Toast.makeText(DetailHistorialActivity.this, "Verificar conexion a internet o volver a intentar", Toast.LENGTH_SHORT).show();
                    screen_failed.setVisibility(View.VISIBLE);
                    screen_data.setVisibility(View.GONE);

                }
            }
        });

        viewModel.detailHistorial(repartidor_historial.getIdempresa(),repartidor_historial.getIdpedido(),repartidor_historial.getIdventa(),repartidor_historial.getIdubicacion());
    }
    private void declararWidgets(){
        NUMERO_ORDEN=findViewById(R.id.NUMERO_ORDEN);
        GANANCIA=findViewById(R.id.GANANCIA);
        DISTANCIA=findViewById(R.id.DISTANCIA);
        DIRECCION_EMPRESA=findViewById(R.id.DIRECCION_EMPRESA);
        DIRECCION_USUARIO=findViewById(R.id.DIRECCION_USUARIO);

        HISTORIAL_ORDEN=findViewById(R.id.HISTORIAL_ORDEN);
        LISTA_PRODUCTOS=findViewById(R.id.LISTA_PRODUCTOS);

        mToolbar=findViewById(R.id.toolbar2);

        screen_failed=findViewById(R.id.screen_failed);

        progresbar=findViewById(R.id.progresbar);

        screen_data=findViewById(R.id.screen_data);

    }

    private void setDataWidget(){

        screen_failed.setVisibility(View.GONE);
        screen_failed.setVisibility(View.GONE);



        NUMERO_ORDEN.setText(String.valueOf(repartidor_historial.getIdventa()));
        GANANCIA.setText(String.valueOf(repartidor_historial.getGanancia_delivery()));
        DISTANCIA.setText(repartidor_historial.getVenta_distancia_delivery_total());


        LISTA_PRODUCTOS.setLayoutManager(new LinearLayoutManager(DetailHistorialActivity.this, LinearLayoutManager.VERTICAL, false));

        HISTORIAL_ORDEN.setLayoutManager(new LinearLayoutManager(DetailHistorialActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    private void reciveDataIntent(){
        if(getIntent().getSerializableExtra(PEDIDO_DETALLE) !=null){
            repartidor_historial=(Repartidor_historial) getIntent().getSerializableExtra(PEDIDO_DETALLE);
        }
    }



    public static Intent newIntentDetailHistorialActivity(Context context, Repartidor_historial repartidor_historial){
        Intent intent= new Intent(context, DetailHistorialActivity.class);
        intent.putExtra(PEDIDO_DETALLE,repartidor_historial);
        return intent;
    }

    @Override
    public void pedidoDetalle(ProductoJOINregistroPedidoJOINpedido producto) {

    }

    private void clickReload(){
        screen_failed.setOnClickListener(v->{
            viewModel.detailHistorial(repartidor_historial.getIdempresa(),repartidor_historial.getIdpedido(),repartidor_historial.getIdventa(),repartidor_historial.getIdubicacion());
        });
    }
}
