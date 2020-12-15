package com.example.motorizadoyego.View.ProcesUI.step3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.ProductoJOINregistroPedidoJOINpedido;

public class DetailProductActivity extends AppCompatActivity {


    private static final String PRODUCTO_DETALLE = "producto_detalle";

    private ProductoJOINregistroPedidoJOINpedido producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        reciveDataIntent();

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v->{
            onBackPressed();
        });

        ProductoDetalleFragment fragment=ProductoDetalleFragment.newInstance(producto);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_DETAIL_PRODUCT,fragment).commit();
    }



    private void reciveDataIntent(){
        if(getIntent().getSerializableExtra(PRODUCTO_DETALLE) !=null){
           producto=(ProductoJOINregistroPedidoJOINpedido) getIntent().getSerializableExtra(PRODUCTO_DETALLE);
        }
    }



    public static Intent newIntentCancelarPedidoActivity(Context context, ProductoJOINregistroPedidoJOINpedido producto){
        Intent intent= new Intent(context, DetailProductActivity.class);
        intent.putExtra(PRODUCTO_DETALLE,producto);
        return intent;
    }


}
