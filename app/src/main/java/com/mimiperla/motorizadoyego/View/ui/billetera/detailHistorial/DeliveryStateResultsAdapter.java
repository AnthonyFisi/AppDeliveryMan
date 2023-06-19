package com.mimiperla.motorizadoyego.View.ui.billetera.detailHistorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.Orden_estado_delivery;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryStateResultsAdapter  extends  RecyclerView.Adapter<DeliveryStateResultsAdapter.DeliveryStateResultsHolder>{

    private List<Orden_estado_delivery> results=new ArrayList<>();


    @NonNull
    @Override
    public DeliveryStateResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estado_delivery,parent,false);
        return new DeliveryStateResultsAdapter.DeliveryStateResultsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryStateResultsHolder holder, int position) {
        Orden_estado_delivery orden_estado_delivery=results.get(position);

        int numero_estado=position+1;

        holder.numero_estado_delivery.setText(String.valueOf(numero_estado));

        String state=listaNombres().get(position);
        holder.estado_delivery.setText(state);

        //Input date in String format
        String input = orden_estado_delivery.getFecha().toString();
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

            //holder.textView_hora.setText(output);

            holder.horario_delivery.setText(output);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    void setResults(List<Orden_estado_delivery> results){
        this.results=results;
        notifyDataSetChanged();
    }

    public class DeliveryStateResultsHolder extends RecyclerView.ViewHolder{

        TextView numero_estado_delivery,estado_delivery,horario_delivery;

        DeliveryStateResultsHolder(@NonNull View itemView) {
            super(itemView);
            estado_delivery=itemView.findViewById(R.id.estado_delivery);
            numero_estado_delivery=itemView.findViewById(R.id.numero_estado_delivery);

            horario_delivery=itemView.findViewById(R.id.horario_delivery);

        }
    }

    private List<String> listaNombres(){

        List<String> list=new ArrayList<>();
        list.add("Pedido aceptado");
        list.add("Llegamos a la tienda");
        list.add("Tenemos los productos");
        list.add("Enviamos foto de comprobante");
        list.add("Nos encontramos en camino");
        list.add("Entregamos los productos");

        return list;
    }

}
