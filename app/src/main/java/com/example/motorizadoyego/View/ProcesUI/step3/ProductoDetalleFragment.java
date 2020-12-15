package com.example.motorizadoyego.View.ProcesUI.step3;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.motorizadoyego.R;
import com.example.motorizadoyego.Repository.Modelo.ProductoJOINregistroPedidoJOINpedido;

import org.w3c.dom.Text;


public class ProductoDetalleFragment extends Fragment {

    private static final String ARG_PRODUCTO_DETALLE = "producto_detalle";


    private ProductoJOINregistroPedidoJOINpedido producto;

    private TextView fragment_producto_NOMBRE,fragment_producto_DETALLE,fragment_producto_CANTIDAD,fragment_producto_PRECIO_TOTAL,fragment_producto_COMENTARIOS;

    private ImageView fragment_producto_IMAGEN;




    public ProductoDetalleFragment() {
        // Required empty public constructor
    }


    public static ProductoDetalleFragment newInstance(ProductoJOINregistroPedidoJOINpedido producto) {
        ProductoDetalleFragment fragment = new ProductoDetalleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCTO_DETALLE, producto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           producto = (ProductoJOINregistroPedidoJOINpedido) getArguments().getSerializable(ARG_PRODUCTO_DETALLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_producto_detalle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        declararWidget(view);
/*
        if(getArguments() !=null){

            ProductoDetalleFragmentArgs args= ProductoDetalleFragmentArgs.fromBundle(getArguments());
            producto=args.getPedido();

        }*/

        setDataWidget();

    }



    private void declararWidget(View view){

        fragment_producto_IMAGEN=view.findViewById(R.id.fragment_producto_IMAGEN);

        fragment_producto_NOMBRE=view.findViewById(R.id.fragment_producto_NOMBRE);
        fragment_producto_DETALLE=view.findViewById(R.id.fragment_producto_DETALLE);
        fragment_producto_CANTIDAD=view.findViewById(R.id.fragment_producto_CANTIDAD);
        fragment_producto_PRECIO_TOTAL=view.findViewById(R.id.fragment_producto_PRECIO_TOTAL);
        fragment_producto_COMENTARIOS=view.findViewById(R.id.fragment_producto_COMENTARIOS);
    }

    private void setDataWidget(){

        if(producto!=null){

            if (producto.getProducto_uriimagen()!= null) {
                String imageUrl =producto.getProducto_uriimagen()
                        .replace("http://", "https://");

                Glide.with(this)
                        .load(imageUrl)
                        .into( fragment_producto_IMAGEN);
            }

            fragment_producto_NOMBRE.setText(producto.getProducto_nombre());
            fragment_producto_DETALLE.setText(producto.getProducto_detalle());

            String cantidad=producto.getRegistropedido_cantidadtotal()+" productos";

            fragment_producto_CANTIDAD.setText(cantidad);

            String costoTotal="S/. "+producto.getRegistropedido_preciototal();
            fragment_producto_PRECIO_TOTAL.setText(costoTotal);

            fragment_producto_COMENTARIOS.setText(producto.getComentario());
        }


    }



}
