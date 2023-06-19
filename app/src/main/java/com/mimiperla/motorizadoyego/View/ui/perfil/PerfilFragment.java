package com.mimiperla.motorizadoyego.View.ui.perfil;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_Bi;
import com.mimiperla.motorizadoyego.Repository.Modelo.Usuario;
import com.mimiperla.motorizadoyego.ViewModel.UsuarioViewModel;

import org.jetbrains.annotations.NotNull;

public class PerfilFragment extends Fragment {

    private ImageView imagen_repartidor;
    private TextView nombre_repartidor, apellido_repartidor, correo_repartidor,perfil_CAMBIAR_CELULAR;
            /*numero_celular_repartidor, calificacion_repartidor,
            tipo_vehiculo_repartidor, placa_vehiculo_repartidor, tipo_brevete_repartidor;
*/



    private Repartidor_Bi mRepartidor_bi;

    private BackToInicio mBackToInicio;

    private EditText numero_celular_repartidor;

    private CardView cardView_change,cardView_cancelar;

    private LinearLayout linearlayout_progressbar;

    private ImageView ic_back;

    private UsuarioViewModel viewModel;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel=new ViewModelProvider(this).get(UsuarioViewModel.class);
        viewModel.init();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRepartidor_bi=Repartidor_Bi.repartidor_bi;
        declararDataWidget(view);
        setDataWidget();

        ic_back=view.findViewById(R.id.ic_back);

        ic_back.setOnClickListener(v->{
            backToInicio();
        });

        responseDataUpdate();

        clickEditNumber();
    }

    private void declararDataWidget(View view) {
        imagen_repartidor = view.findViewById(R.id.imagen_repartidor);
        nombre_repartidor = view.findViewById(R.id.nombre_repartidor);
        apellido_repartidor = view.findViewById(R.id.apellido_repartidor);
        correo_repartidor = view.findViewById(R.id.correo_repartidor);

        numero_celular_repartidor=view.findViewById(R.id.numero_celular_repartidor);

        perfil_CAMBIAR_CELULAR=view.findViewById(R.id.perfil_CAMBIAR_CELULAR);

        cardView_change=view.findViewById(R.id.cardView_change);

        cardView_cancelar=view.findViewById(R.id.cardView_cancelar);

        linearlayout_progressbar=view.findViewById(R.id.linearlayout_progressbar);

        /*numero_celular_repartidor = view.findViewById(R.id.numero_celular_repartidor);

        calificacion_repartidor = view.findViewById(R.id.calificacion_repartidor);
        tipo_vehiculo_repartidor = view.findViewById(R.id.tipo_vehiculo_repartidor);
        placa_vehiculo_repartidor = view.findViewById(R.id.placa_vehiculo_repartidor);
        tipo_brevete_repartidor = view.findViewById(R.id.tipo_brevete_repartidor);*/

    }

    private void setDataWidget() {

        if (mRepartidor_bi.getFoto() != null) {
            String imageUrl =mRepartidor_bi.getFoto()
                    .replace("http://", "https://");

            Glide.with(this)
                    .load(imageUrl)
                    .into(imagen_repartidor);
        }



        nombre_repartidor.setText(mRepartidor_bi.getNombre_usuario());
        apellido_repartidor.setText(mRepartidor_bi.getApellido());
        correo_repartidor.setText(mRepartidor_bi.getCorreo());
       // numero_celular_repartidor.setText(mRepartidor_bi.get);
    /*    calificacion_repartidor.setText("0");
        tipo_vehiculo_repartidor.setText("moto");
        placa_vehiculo_repartidor.setText(mRepartidor_bi.getPlaca_vehiculo());
        tipo_brevete_repartidor.setText("2b");*/

        numero_celular_repartidor.setText(Repartidor_Bi.repartidor_bi.getCelular());
        numero_celular_repartidor.setEnabled(false);


    }

    private void clickEditNumber(){
        perfil_CAMBIAR_CELULAR.setOnClickListener(v->{
            numero_celular_repartidor.setEnabled(true);
            numero_celular_repartidor.setCursorVisible(true);
            numero_celular_repartidor.setFocusable(true);
            // perfil_CELULAR.setTextCursorDrawable(getResources().getDrawable(R.color.mainColor));

            cardView_change.setVisibility(View.VISIBLE);
            cardView_cancelar.setVisibility(View.VISIBLE);

            perfil_CAMBIAR_CELULAR.setVisibility(View.GONE);


            numero_celular_repartidor.requestFocus();
           numero_celular_repartidor.setFocusableInTouchMode(true);

            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(numero_celular_repartidor, InputMethodManager.SHOW_FORCED);

        });

        cardView_change.setOnClickListener(v->{

            if(numero_celular_repartidor.getText().toString().length()==9 && String.valueOf(numero_celular_repartidor.getText().toString().charAt(0)).equals(String.valueOf(9))){
                //  view.getWindow().setSoftInputMode(
                //WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                linearlayout_progressbar.setVisibility(View.VISIBLE);
                viewModel.updateCelular(Repartidor_Bi.repartidor_bi.getIdusuariogeneral(),numero_celular_repartidor.getText().toString());

                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }else{
                Toast.makeText(getContext(), "Ingresar numero correcto", Toast.LENGTH_LONG).show();
            }

            //  linearlayout_progressbar.setVisibility(View.VISIBLE);
        });

        cardView_cancelar.setOnClickListener(v->{
            perfil_CAMBIAR_CELULAR.setVisibility(View.VISIBLE);
            cardView_cancelar.setVisibility(View.GONE);
            cardView_change.setVisibility(View.GONE);

            numero_celular_repartidor.setFocusable(false);
            numero_celular_repartidor.setEnabled(false);
            numero_celular_repartidor.setCursorVisible(false);

            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });

    }

    private void backToInicio() {
        mBackToInicio.back();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mBackToInicio = (PerfilFragment.BackToInicio) context;
    }

    public interface BackToInicio{
        void  back();
    }


    private void responseDataUpdate(){
        viewModel.getUsuarioLiveData().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                linearlayout_progressbar.setVisibility(View.GONE);
                cardView_cancelar.setVisibility(View.GONE);
                cardView_change.setVisibility(View.GONE);
                perfil_CAMBIAR_CELULAR.setVisibility(View.VISIBLE);
                numero_celular_repartidor.setFocusable(false);
                numero_celular_repartidor.setEnabled(false);
                numero_celular_repartidor.setCursorVisible(false);


                if(usuario!=null){
                    numero_celular_repartidor.setText(usuario.getCelular());
                   // Cliente_Bi.getCliente().setCelular();
                    Repartidor_Bi.repartidor_bi.setCelular(usuario.getCelular());
                    Toast.makeText(getContext(),"Actualizacion exitosa",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getContext(),"Lo sentimos presentamos problemas",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}