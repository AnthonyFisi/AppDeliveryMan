package com.example.motorizadoyego.View.ProcesUI.step6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.motorizadoyego.MainActivity;
import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.Delivery_Pedido;
import com.example.motorizadoyego.Repository.Modelo.Gson.GsonDelivery_Pedido;
import com.example.motorizadoyego.View.ProcesUI.listOrder.ListOrderFragment;
import com.example.motorizadoyego.View.ProcesUI.step5.Step5Activity;

public class Step6Activity extends AppCompatActivity {


    private static final String PEDIDO_DETALLE = "delivery_pedido";

    private Delivery_Pedido delivery_pedido;

    private GsonDelivery_Pedido mGsonDelivery_pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step6);
        reciveDataIntent();

        Toolbar toolbar=findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v->{
           // onBackPressed();
            ListOrderFragment.deleteState(delivery_pedido.getIdventa());

            Intent intent= MainActivity.newIntentMainActivity(Step6Activity.this);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        mGsonDelivery_pedido=new GsonDelivery_Pedido();
        mGsonDelivery_pedido.setDelivery_information(delivery_pedido);
        Step6Fragment fragment= Step6Fragment.newInstance(mGsonDelivery_pedido);
        getSupportFragmentManager().beginTransaction().replace(R.id.step6Fragment_activity,fragment).commit();
    }

    private void reciveDataIntent(){
        if(getIntent().getSerializableExtra(PEDIDO_DETALLE) !=null){
            delivery_pedido=(Delivery_Pedido) getIntent().getSerializableExtra(PEDIDO_DETALLE);
        }
    }



    public static Intent newIntentStep6Activity(Context context, Delivery_Pedido delivery_pedido){
        Intent intent= new Intent(context, Step6Activity.class);
        intent.putExtra(PEDIDO_DETALLE,delivery_pedido);
        return intent;
    }


}
