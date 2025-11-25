package com.uts.shopper.Controllers;

import android.util.Log;

import com.uts.shopper.Models.ModelProducto;
import com.uts.shopper.helpers.Fetch;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class ControllerMain {
    public void CargarProductos(Consumer<ArrayList<ModelProducto>> callback){
        try {
            Fetch.GetList("/api/personas/test", (lista) -> {
                if (lista == null)  return;
                ArrayList<ModelProducto> modelProductos = new ArrayList<>();
                for (Map<String, Object> item : lista) {
                    modelProductos.add(new ModelProducto(
                            (String) item.get("titulo"),
                            (String) item.get("descripcion"),
                            (String) item.get("imgenUrl"),
                            Double.parseDouble((String) item.get("precioUnitairo")),
                            Double.parseDouble((String) item.get("costoEnvio")),
                            Integer.parseInt((String) item.get("cantidad")),
                            Double.parseDouble((String) item.get("calificacion"))
                    ));
                }
                callback.accept(modelProductos);
            });
        } catch (Exception e) {
            Log.e("APP_API_DEBUG", e.getMessage());
        }
    }
}
