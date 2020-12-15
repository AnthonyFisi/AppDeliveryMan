package com.example.motorizadoyego.View.LoginUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.motorizadoyego.Repository.Modelo.JwtResponse;

import java.util.ArrayList;
import java.util.List;


/**
 * Manejador de preferencias de la sesión del afiliado
 */
public class SessionPrefs {

    public static final String PREFS_NAME = "SALUDMOCK_PREFS";
    public static final String PREF_AFFILIATE_ID = "PREF_USER_ID";
    public static final String PREF_AFFILIATE_NAME = "PREF_AFFILIATE_NAME";
    public static final String PREF_AFFILIATE_CORREO = "PREF_AFFILIATE_CORREO";
    public static final String PREF_AFFILIATE_GENDER = "PREF_AFFILIATE_GENDER";
    public static final String PREF_AFFILAITE_TOKEN = "PREF_AFFILAITE_TOKEN";


     List<String> roles;

    private final SharedPreferences mPrefs;

    private boolean mIsLoggedIn = false;

    private static SessionPrefs INSTANCE;

    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    private SessionPrefs(Context context) {
        mPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PREF_AFFILAITE_TOKEN, null));
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    public JwtResponse data(){

        System.out.println(mPrefs.getString(PREF_AFFILAITE_TOKEN,"")+"TOKENNNN FUE GUARDADO");
        return new JwtResponse(mPrefs.getString(PREF_AFFILAITE_TOKEN,""),
                                                mPrefs.getString(PREF_AFFILIATE_GENDER,""),
                                                Long.valueOf(mPrefs.getString(PREF_AFFILIATE_ID,"")),
                                                mPrefs.getString(PREF_AFFILIATE_NAME,""),
                                                mPrefs.getString(PREF_AFFILIATE_CORREO,""),
                                               roles
                                                );


    }

    public String getTokenPrefs(){
        return mPrefs.getString(PREF_AFFILIATE_GENDER,"")+" "+ mPrefs.getString(PREF_AFFILAITE_TOKEN,"");
    }

    public void saveJwtResponse(JwtResponse response) {
        if (response != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_AFFILIATE_ID, String.valueOf(response.getId()));
            editor.putString(PREF_AFFILIATE_NAME, String.valueOf(response.getUsername()));
            editor.putString(PREF_AFFILIATE_CORREO, response.getEmail());
            editor.putString(PREF_AFFILIATE_GENDER,response.getTokenType());
            editor.putString(PREF_AFFILAITE_TOKEN, response.getAccessToken());
            editor.apply();

            mIsLoggedIn = true;

            editor.commit();
        }
    }

    public void logOut(){
        mIsLoggedIn = false;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_AFFILIATE_ID, null);
        editor.putString(PREF_AFFILIATE_NAME, null);
        editor.putString(PREF_AFFILIATE_CORREO, null);
        editor.putString(PREF_AFFILIATE_GENDER, null);
        editor.putString(PREF_AFFILAITE_TOKEN, null);
        editor.apply();
    }
}
