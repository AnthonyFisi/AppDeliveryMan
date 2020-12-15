package com.example.motorizadoyego.Util;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {
    /**
     * Live Data Instance
     */
    private MutableLiveData<Boolean> mName = new MutableLiveData<>();


    public void setName(Boolean name) {
        System.out.println("LLEGARON LOS DATOSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        mName.setValue(name);
    }
    public LiveData<Boolean> getName() {

        System.out.println("NO RETORN NADA ");


        return mName;
    }

}