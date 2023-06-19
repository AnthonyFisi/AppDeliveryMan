package com.mimiperla.motorizadoyego.View.ui.billetera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.Gson.GsonRepartidor_historial;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_historial;
import com.mimiperla.motorizadoyego.View.ui.billetera.detailHistorial.DetailHistorialActivity;
import com.mimiperla.motorizadoyego.ViewModel.Repartidor_historialViewModel;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.List;

public class BilleteraFragment extends Fragment implements Historial_PedidoResultsAdapter.ClickPedidoReciente{

    private Repartidor_historialViewModel viewModel;
    private Historial_PedidoResultsAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout linearlayout_EMPTY_HISTORIAL,linearLayout_RESULT_HISTORIAL;
    private TextView venta_hoy, venta_total;
    private ProgressBar progres_historial;

    private BackToInicio mBackToInicio;

    private ImageView ic_back;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new Historial_PedidoResultsAdapter();
        viewModel = new ViewModelProvider(this).get(Repartidor_historialViewModel.class);
        viewModel.init();
        viewModel.getRepartidor_historialLiveData().observe(this, new Observer<GsonRepartidor_historial>() {
            @Override
            public void onChanged(GsonRepartidor_historial gsonRepartidor_historial) {

                progres_historial.setVisibility(View.GONE);

                if (gsonRepartidor_historial != null) {

                    adapter.setResults(gsonRepartidor_historial.getListaHistorial(), BilleteraFragment.this);
                    linearLayout_RESULT_HISTORIAL.setVisibility(View.VISIBLE);

                    linearlayout_EMPTY_HISTORIAL.setVisibility(View.GONE);

                    calculateVentaTotal(gsonRepartidor_historial.getListaHistorial());
                } else {
                    linearLayout_RESULT_HISTORIAL.setVisibility(View.GONE);

                    linearlayout_EMPTY_HISTORIAL.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.fragment_slideshow, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // searchOrder(view);

        venta_hoy = view.findViewById(R.id.venta_hoy);
        venta_total = view.findViewById(R.id.venta_total);
        linearlayout_EMPTY_HISTORIAL = view.findViewById(R.id.linearlayout_EMPTY_HISTORIAL);
        linearLayout_RESULT_HISTORIAL=view.findViewById(R.id.linearLayout_RESULT_HISTORIAL);

        linearlayout_EMPTY_HISTORIAL.setVisibility(View.GONE);
        linearLayout_RESULT_HISTORIAL.setVisibility(View.GONE);

        //imagen.setVisibility(View.GONE);

        progres_historial=view.findViewById(R.id.progres_historial);
        progres_historial.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.fragment_historial_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


        viewModel.listaHistorial(Repartidor_Bi.repartidor_bi.getIdrepartidor(),4);

        ic_back=view.findViewById(R.id.ic_back);

        ic_back.setOnClickListener(v->{
            backToInicio();
        });

    }





    private void calculateVentaTotal(List<Repartidor_historial> list) {

        float total_hoy = 0, total_general = 0;

        for (Repartidor_historial pedido : list) {

            Timestamp time = new Timestamp(System.currentTimeMillis());

            Timestamp timeYesterday = new Timestamp(time.getTime() - (60000) * 60 * 24);

            Timestamp timeTomorrow = new Timestamp(time.getTime() + (60000) * 60 * 24);

            if (pedido.getFechahistorial().after(timeYesterday) && pedido.getFechahistorial().before(timeTomorrow)) {
                total_hoy += pedido.getGanancia_delivery();
            }

            total_general += pedido.getGanancia_delivery();

        }

        String total = "S/ " + total_general;

        String hoy = "S/ " + total_hoy;

        venta_total.setText(total);

        venta_hoy.setText(hoy);
    }

    @Override
    public void clickPedido(Repartidor_historial restaurante_pedido, int posisiton) {

        Intent intent = DetailHistorialActivity.newIntentDetailHistorialActivity(getContext(),restaurante_pedido);
        startActivity(intent);
    }

    private void backToInicio() {
        mBackToInicio.back();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mBackToInicio = (BilleteraFragment.BackToInicio) context;
    }

    public interface BackToInicio{
        void  back();
    }

}