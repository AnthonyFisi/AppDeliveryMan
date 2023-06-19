package com.mimiperla.motorizadoyego.View.ui.billetera;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_historial;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Historial_PedidoResultsAdapter extends  RecyclerView.Adapter<Historial_PedidoResultsAdapter.Restaurante_PedidoResultsHolder> implements Filterable {

    private List<Repartidor_historial> results= new ArrayList<>();
    private ClickPedidoReciente mClickPedidoReciente;
    private List<Repartidor_historial> resultsFiltered= new ArrayList<>();

    @NonNull
    @Override
    public Restaurante_PedidoResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_historial_item,parent,false);
        return  new Historial_PedidoResultsAdapter.Restaurante_PedidoResultsHolder(view,mClickPedidoReciente);
    }

    @Override
    public void onBindViewHolder(@NonNull Restaurante_PedidoResultsHolder holder, int position) {

        Repartidor_historial restaurante_pedido=resultsFiltered.get(position);


        if(position>=1){

            Repartidor_historial restaurante_pedido1=resultsFiltered.get(position-1);

            String patternFecha = "yyyy-MM-dd";


            @SuppressLint("SimpleDateFormat") DateFormat dateFormat1 = new SimpleDateFormat(patternFecha);
            String fechaPosition1=dateFormat1.format(restaurante_pedido1.getFechahistorial());


            @SuppressLint("SimpleDateFormat") DateFormat dateFormat2 = new SimpleDateFormat(patternFecha);
            String fechaPosititon2=dateFormat2.format(restaurante_pedido.getFechahistorial());

            holder.fecha.setText(fechaPosititon2);

            if(!fechaPosition1.equals(fechaPosititon2)){
                //ESCRIBIR EL DIA DE HOY
                String day = (new SimpleDateFormat("EEEE")).format(restaurante_pedido.getFechahistorial().getTime()); // "Tuesday"
                holder.fecha_historial.setVisibility(View.VISIBLE);
                String dayUpper =dayAndmonthAndYear(day);
                holder.dia_semana.setText(dayUpper);
            }


        }else {

            //restaurante_pedido.getFechahistorial().

            String patternFecha = "yyyy-MM-dd";

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat2 = new SimpleDateFormat(patternFecha);
            String fechaPosititon2=dateFormat2.format(restaurante_pedido.getFechahistorial());
            String fechaNow= Calendar.getInstance().getTime().toString();
            holder.fecha.setText(fechaPosititon2);
            if(fechaNow.equals(fechaPosititon2)){
                //ESCRIBIR EL DIA DE HOY
                holder.fecha_historial.setVisibility(View.VISIBLE);
                holder.dia_semana.setText("Hoy");
            }else {
                String day = (new SimpleDateFormat("EEEE")).format(restaurante_pedido.getFechahistorial().getTime()); // "Tuesday"
                holder.fecha_historial.setVisibility(View.VISIBLE);
                String dayUpper =dayAndmonthAndYear(day);
                holder.dia_semana.setText(dayUpper);
            }
        }


        String pattern = "hh:mm:ss a";
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(pattern);
        String fecha=dateFormat.format(restaurante_pedido.getFechahistorial());

        String orden="Orden N° "+restaurante_pedido.getIdpedido();
        holder.fragment_home_item_orden_NUMERO_ORDEN.setText(orden);



        holder.fragment_home_item_orden_HORA_DE_LLEGADA.setText(fecha);


        holder.fragment_home_item_orden_COSTO_TOTAL.setText(String.valueOf(restaurante_pedido.getGanancia_delivery()));


    }

    @Override
    public int getItemCount() {
        return resultsFiltered.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setResults(List<Repartidor_historial> results, ClickPedidoReciente clickPedidoReciente){
        this.mClickPedidoReciente=clickPedidoReciente;
        this.results=results;
        this.resultsFiltered=results;
        System.out.println("cantidad" +results.size());
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    resultsFiltered = results;
                } else {
                    List<Repartidor_historial> filteredList = new ArrayList<>();
                    for (Repartidor_historial row : results) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String numeroOrden=row.getIdempresa()+""+
                                row.getIdpedido()+""+
                                row.getIdventa();
                        if (numeroOrden.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    resultsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = resultsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                resultsFiltered= (ArrayList<Repartidor_historial>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Restaurante_PedidoResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ClickPedidoReciente mClickPedidoReciente;
        private CardView item_historial;
        private TextView dia_semana,fecha;
        private LinearLayout fecha_historial;
        private TextView fragment_home_item_orden_COSTO_TOTAL,fragment_home_item_orden_HORA_DE_LLEGADA,fragment_home_item_orden_NUMERO_ORDEN,fragment_home_item_orden_NOMBRE_CLIENTE,fragment_home_item_orden_CANTIDAD_PRODUCTOS;
        Restaurante_PedidoResultsHolder(@NonNull View itemView, ClickPedidoReciente clickPedidoReciente) {
            super(itemView);
            this.mClickPedidoReciente=clickPedidoReciente;
            fragment_home_item_orden_HORA_DE_LLEGADA=itemView.findViewById(R.id.fragment_home_item_orden_HORA_DE_LLEGADA);
            fragment_home_item_orden_NUMERO_ORDEN=itemView.findViewById(R.id.fragment_home_item_orden_NUMERO_ORDEN);
            fragment_home_item_orden_NOMBRE_CLIENTE=itemView.findViewById(R.id.fragment_home_item_orden_NOMBRE_CLIENTE);
            fragment_home_item_orden_CANTIDAD_PRODUCTOS=itemView.findViewById(R.id.fragment_home_item_orden_CANTIDAD_PRODUCTOS);
            fragment_home_item_orden_COSTO_TOTAL=itemView.findViewById(R.id.fragment_home_item_orden_COSTO_TOTAL);
            dia_semana=itemView.findViewById(R.id.dia_semana);
            item_historial=itemView.findViewById(R.id.item_historial);
            fecha=itemView.findViewById(R.id.fecha);
            //itemView.setOnClickListener(this);
            fecha_historial=itemView.findViewById(R.id.fecha_historial);
            item_historial.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(view.findViewById(R.id.item_historial)==item_historial){
                mClickPedidoReciente.clickPedido(resultsFiltered.get(getAdapterPosition()),getAdapterPosition());

            }

        }
    }

    public interface ClickPedidoReciente{

        void clickPedido(Repartidor_historial restaurante_pedido, int posisiton);
    }

    private String dayAndmonthAndYear(String day){
  /*      String month = (new SimpleDateFormat("MMMM")).format(fecha.getTime()); // "April"

        String year = (new SimpleDateFormat("yyyy")).format(fecha.getTime()); // "April"
*/
        return day.substring(0, 1).toUpperCase() + day.substring(1);//+" "+month+" , "+year;
    }
}
