package com.uts.shopper.Controllers;

import android.se.omapi.Session;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.uts.shopper.Models.Producto;
import com.uts.shopper.helpers.Fetch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ControllerMain {
    public void CargarProductos(Consumer<ArrayList<Producto>> callback){
        try {
            Fetch.GetList("/api/testProducts", (lista) -> {
                if (lista == null)  return;
                ArrayList<Producto> productos = new ArrayList<>();
                for (Map<String, Object> item : lista) {
                    productos.add(new Producto(
                            (String) item.get("titulo"),
                            (String) item.get("descripcion"),
                            (String) item.get("imgenUrl"),
                            Double.parseDouble((String) item.get("precioUnitairo")),
                            Double.parseDouble((String) item.get("costoEnvio")),
                            Integer.parseInt((String) item.get("cantidad")),
                            Integer.parseInt((String) item.get("calificacion"))
                    ));
                }
                callback.accept(productos);
            });
        } catch (Exception e) {
            Log.e("APP_API_DEBUG", e.getMessage());
        }
    }
}
