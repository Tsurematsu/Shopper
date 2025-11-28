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
            Fetch.GetList("/api/productos/getProductos", (lista) -> {
                if (lista == null)  return;
                ArrayList<ModelProducto> modelProductos = new ArrayList<>();
                for (Map<String, Object> item : lista) {
                    String text_titulo = String.valueOf(item.get("titulo"));
                    String text_descripcion = String.valueOf(item.get("descripcion"));
                    String text_imagenUrl = String.valueOf(item.get("imagenUrl"));
                    double number_precioUnitario = Double.parseDouble(String.valueOf(item.get("precioUnitario")));
                    double number_costoEnvio = Double.parseDouble(String.valueOf(item.get("costoEnvio")));
                    double number_cantidad_double = Double.parseDouble(String.valueOf(item.get("cantidad")));
                    int number_cantidad = (int) Math.floor(number_cantidad_double);
                    double bumber_calificacion = Double.parseDouble(String.valueOf(item.get("calificacion")));
                    String number_id = String.valueOf((int) Math.floor(Double.parseDouble(String.valueOf(item.get("id")))));

                    ModelProducto onProduct = new ModelProducto(
                            text_titulo,
                            text_descripcion,
                            text_imagenUrl,
                            number_precioUnitario,
                            number_costoEnvio,
                            number_cantidad,
                            bumber_calificacion,
                            number_id
                    );
                    modelProductos.add(onProduct);
                }
                callback.accept(modelProductos);
            });
        } catch (Exception e) {
            Log.e("APP_API_DEBUG", "Error Cotroller main ->" + e.getMessage());
        }
    }
}
