package com.mimiperla.motorizadoyego.Repository.Modelo.Gson;

import com.mimiperla.motorizadoyego.Repository.Modelo.Repartidor_historial;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonRepartidor_historial {

    @SerializedName("listaHistorial")
    @Expose
    private List<Repartidor_historial> listaHistorial;

    public List<Repartidor_historial> getListaHistorial() {
        return listaHistorial;
    }

    public void setListaHistorial(List<Repartidor_historial> listaHistorial) {
        this.listaHistorial = listaHistorial;
    }
}
