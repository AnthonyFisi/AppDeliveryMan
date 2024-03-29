package com.mimiperla.motorizadoyego.View.ui.soporte;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.mimiperla.motorizadoyego.R;
import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_Bi;

import org.jetbrains.annotations.NotNull;

public class SoporteFragment extends Fragment {

    private ImageButton button_WHATSAPP,button_CELULAR;

    private String number="937417980";
    private String message="Hola Yego,Soy "+ Repartidor_Bi.repartidor_bi.getNombre_usuario() + " "+Repartidor_Bi.repartidor_bi.getApellido() +" con codigo "+Repartidor_Bi.repartidor_bi.getIdrepartidor()+"!! Necesito ayuda";
    private ImageView ic_back;

    private BackToInicio mBackToInicio;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_share, container, false);

        declararWidget(root);
        clickButton();

        ic_back.setOnClickListener(v->{
            backToInicio();
        });

        return root;


    }


    private void declararWidget(View view){
        button_CELULAR=view.findViewById(R.id.button_CELULAR);
        button_WHATSAPP=view.findViewById(R.id.button_WHATSAPP);
        //button_CHAT=view.findViewById(R.id.button_CHAT);
        ic_back=view.findViewById(R.id.ic_back);
    }

    private void clickButton(){


        button_CELULAR.setOnClickListener( v->{
            Intent intent= new Intent((Intent.ACTION_CALL));
            intent.setData(Uri.parse("tel:"+number));
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE )!= PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(),"Habilitar el permiso",Toast.LENGTH_SHORT).show();
                requestPermission();
            }else{
                startActivity(intent);
            }
        });

        button_WHATSAPP.setOnClickListener( v->{

            boolean installed= appInstalledOrNot("com.whatsapp");

            if(installed){
                Intent intent= new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+51"+number+"&text="+message));
                startActivity(intent);
            }else {
                Toast.makeText(getContext(),"Whatsapp no esta instalado",Toast.LENGTH_SHORT).show();

            }

        });
    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String []{Manifest.permission.CALL_PHONE},1);
    }

    private boolean appInstalledOrNot(String url){
        PackageManager packageManager=getContext().getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            app_installed=true;

        }catch (Exception e){
            app_installed=false;
        }

        return  app_installed;
    }


    private void backToInicio() {
        mBackToInicio.back();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mBackToInicio = (SoporteFragment.BackToInicio) context;
    }

    public interface BackToInicio{
        void  back();
    }


}