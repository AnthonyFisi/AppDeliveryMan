package com.mimiperla.motorizadoyego.View.ProcesUI.listOrder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.Delivery_Pedido;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PedidoResultsAdapter extends RecyclerView.Adapter<PedidoResultsAdapter.PedidoResultsHolder>{

    private List<Delivery_Pedido> results=new ArrayList<>();

    private PedidoInterface mPedidoInterface;

    @NonNull
    @Override
    public PedidoResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido,parent,false);
        return new PedidoResultsAdapter.PedidoResultsHolder(view,mPedidoInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoResultsHolder holder, int position) {
        Delivery_Pedido delivery_pedido=results.get(position);


        //Input date in String format
        String input = delivery_pedido.getVenta_fechaentrega().toString();
        //Date/time pattern of input date
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        Date date = null;
        String output = null;
        try{
            //Conversion of input String to date
            date= df.parse(input);
            //old date format to new date format
            output = outputformat.format(date);

            holder.textView_hora.setText(output);

        }catch(ParseException pe){
            pe.printStackTrace();
        }


        String codigo_pedido="N° "+delivery_pedido.getIdventa();
        holder.textView_codigo_pedido.setText(codigo_pedido);
        holder.textView_direccion.setText(delivery_pedido.getUbicacion_nombre());

        if(delivery_pedido.getIdestado_delivery()==3){

            holder.linearLayout_state.setVisibility(View.VISIBLE);
            holder.textView_state.setText("2");
            holder.textView_name_state.setText("Llegue al destino");
        }

        if(delivery_pedido.getIdestado_delivery()==4){

            holder.linearLayout_state.setVisibility(View.VISIBLE);
            holder.textView_state.setText("2");
            holder.textView_name_state.setText("Entregar los productos");
        }

        if(delivery_pedido.getIdestado_delivery()==4){

            holder.linearLayout_state.setVisibility(View.VISIBLE);
            holder.textView_state.setText("2");
            holder.textView_name_state.setText("Calificacion");
        }


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Delivery_Pedido> results,PedidoInterface pedidoInterface){
        this.mPedidoInterface=pedidoInterface;
        this.results=results;
        notifyDataSetChanged();
    }

    public void addItem(Delivery_Pedido delivery_pedido,PedidoInterface pedidoInterface){
        results.add(delivery_pedido);
        this.mPedidoInterface=pedidoInterface;
        notifyDataSetChanged();
    }

    public void removeAll(){
        results.clear();
        notifyDataSetChanged();
    }


    public class PedidoResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        PedidoInterface mPedidoInterface;

        TextView textView_codigo_pedido,textView_hora,textView_direccion,textView_state,textView_name_state;

        LinearLayout linearLayout_state;


        private PedidoResultsHolder(@NonNull View itemView,PedidoInterface pedidoInterface) {
            super(itemView);
            this.mPedidoInterface=pedidoInterface;
            textView_direccion=itemView.findViewById(R.id.textView_direccion);
            textView_codigo_pedido=itemView.findViewById(R.id.textView_codigo_pedido);
            textView_hora=itemView.findViewById(R.id.textView_hora);
            textView_state=itemView.findViewById(R.id.textView_state);
            textView_name_state=itemView.findViewById(R.id.textView_name_state);
            linearLayout_state=itemView.findViewById(R.id.linearLayout_state);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("click de prueba");
            mPedidoInterface.clickPedido(results.get(getAdapterPosition()));
        }
    }

    public interface PedidoInterface{
        void clickPedido(Delivery_Pedido delivery_pedido);
    }
}
