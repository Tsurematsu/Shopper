package com.uts.shopper.App;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.uts.shopper.Models.ModelCarrito;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AppSessionCarritoManager {
    private AppData appData;
    public AppSessionCarritoManager(AppCompatActivity parent){
        appData = new AppData(parent);
    }
    public void setCarritoList(ArrayList<ModelCarrito> listaCarrito){
        appData.setToGson("carrito", listaCarrito);
    }
    public ArrayList<ModelCarrito> getCarritoList(){
        Type type = new TypeToken<ArrayList<ModelCarrito>>(){}.getType();
        ArrayList<ModelCarrito> lista = appData.getToGson("carrito", type);
        return lista == null ? new ArrayList<>() : lista;
    }
    public void clearCarritoList(){
        ArrayList<ModelCarrito> listaCarrito = new ArrayList<>();
        appData.setToGson("carrito", listaCarrito);
    }
}
