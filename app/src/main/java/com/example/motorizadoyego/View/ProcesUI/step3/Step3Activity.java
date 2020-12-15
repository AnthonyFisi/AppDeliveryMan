package com.example.motorizadoyego.View.ProcesUI.step3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDeliveryPedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Repartidor_historial;

public class Step3Activity extends AppCompatActivity {

    private static final String PEDIDO_DETALLE = "delivery_pedido";

    private Delivery_Pedido delivery_pedido;

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);
        reciveDataIntent();

        Toolbar toolbar=findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v->{
            onBackPressed();
        });

        mGsonDelivery_pedido=new GsonDelivery_Pedido();
        mGsonDelivery_pedido.setDelivery_information(delivery_pedido);
        Step3Fragment fragment=Step3Fragment.newInstance(mGsonDelivery_pedido);
        getSupportFragmentManager().beginTransaction().replace(R.id.step3Fragment_activity,fragment).commit();
    }

    private void reciveDataIntent(){
        if(getIntent().getSerializableExtra(PEDIDO_DETALLE) !=null){
            delivery_pedido=(Delivery_Pedido) getIntent().getSerializableExtra(PEDIDO_DETALLE);
        }
    }



    public static Intent newIntentStep3Activity(Context context, Delivery_Pedido delivery_pedido){
        Intent intent= new Intent(context, Step3Activity.class);
        intent.putExtra(PEDIDO_DETALLE,delivery_pedido);
        return intent;
    }
}
