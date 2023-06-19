package com.mimiperla.motorizadoyego.View.ProcesUI.step3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.ProductoJOINregistroPedidoJOINpedido;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductosResultsAdapter extends RecyclerView.Adapter<ProductosResultsAdapter.ProductosResultsHolder> {

    private List<ProductoJOINregistroPedidoJOINpedido> results = new ArrayList<>();

    private ClickPedidoDetalle mClickPedidoDetalle;


    @NonNull
    @Override
    public ProductosResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto,parent,false);
        return new ProductosResultsAdapter.ProductosResultsHolder(view,mClickPedidoDetalle);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosResultsHolder holder, int position) {

        ProductoJOINregistroPedidoJOINpedido producto= results.get(position);

        holder.item_producto_NOMBRE.setText(producto.getProducto_nombre());
        holder.item_producto_DETALLE.setText(producto.getComentario());
        holder.item_producto_CANTIDAD.setText(String.valueOf(producto.getRegistropedido_cantidadtotal()));
        String total="S/ "+producto.getRegistropedido_preciototal();

        holder.item_producto_TOTAL.setText(total);

        if (producto.getProducto_uriimagen()!= null) {
            String imageUrl =producto.getProducto_uriimagen()
                    .replace("http://", "https://");

            Glide.with(holder.itemView)
                    .load(imageUrl)
                    .into(holder.item_producto_IMAGEN);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public  void setResults(List<ProductoJOINregistroPedidoJOINpedido> results,ClickPedidoDetalle clickPedidoDetalle){
        this.results=results;
        this.mClickPedidoDetalle=clickPedidoDetalle;
        notifyDataSetChanged();
    }

    public  class  ProductosResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView item_producto_NOMBRE,item_producto_CANTIDAD,item_producto_DETALLE,item_producto_TOTAL;
        private ImageView item_producto_IMAGEN;

        private ClickPedidoDetalle mClickPedidoDetalle;

        ProductosResultsHolder(@NonNull View itemView, ClickPedidoDetalle clickPedidoDetalle) {
            super(itemView);
           item_producto_IMAGEN=itemView.findViewById(R.id.item_product_IMAGEN);
           item_producto_NOMBRE=itemView.findViewById(R.id.item_producto_NOMBRE);
            item_producto_DETALLE=itemView.findViewById(R.id.item_producto_DETALLE);
            item_producto_TOTAL=itemView.findViewById(R.id.item_producto_TOTAL);
            item_producto_CANTIDAD=itemView.findViewById(R.id.item_producto_CANTIDAD);

            this.mClickPedidoDetalle=clickPedidoDetalle;

           itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mClickPedidoDetalle.pedidoDetalle(results.get(getAdapterPosition()));
        }
    }

    public interface ClickPedidoDetalle{
        void pedidoDetalle(ProductoJOINregistroPedidoJOINpedido producto);
    }

}
