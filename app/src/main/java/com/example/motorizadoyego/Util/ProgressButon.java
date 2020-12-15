package com.example.motorizadoyego.Util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.motorizadoyego.R;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButon {

    private final int state;
    private CardView cardview_general_button;
    private ConstraintLayout constraint_general_button;
    private ProgressBar progressbar_general_button;
    private TextView text_general_button,textView_state;
    private String nombre;

    Animation fade_in;

    public ProgressButon(Context context, View view, String nombre,int state){
        cardview_general_button=view.findViewById(R.id.cardview_general_button);
        constraint_general_button=view.findViewById(R.id.constraint_general_button);
        progressbar_general_button=view.findViewById(R.id.progressbar_general_button);
        text_general_button=view.findViewById(R.id.text_general_button);
        textView_state=view.findViewById(R.id.textView_state);
        this.nombre=nombre;
        this.state=state;
    }

    public void initName(){
        text_general_button.setText(nombre);
        textView_state.setText(String.valueOf(state));
    }

    public void buttonActivated(){

        progressbar_general_button.setVisibility(View.VISIBLE);
        text_general_button.setText("Cargando");
    }

    public void buttoFinished(){
        progressbar_general_button.setVisibility(View.VISIBLE);
        text_general_button.setText(nombre);
    }



}
