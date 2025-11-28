package com.uts.shopper.Controllers;

import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uts.shopper.Models.ModelProducto;
import com.uts.shopper.R;
import com.uts.shopper.helpers.Fetch;
import com.uts.shopper.helpers.FileHelper;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class ControllerAdmin {
    public void uploadImage(FileHelper fileHelper, ImageView elementImage, AppCompatActivity parent, Consumer<String> succes) {
        Gson gson = new Gson();
        fileHelper.requestImage(path->{
            Fetch.upload("/api/upload", "archivo", path, response->{
                Map<String, Object> map = gson.fromJson(response, new TypeToken<Map<String, Object>>(){}.getType());
                String relative_path = (String) map.get("relative_path");
                String host = Fetch.urlAPI;
                String serverPath = host + "/" + relative_path;
                parent.runOnUiThread(() -> {
                    succes.accept(serverPath);
                    Glide.with(parent)
                            .load(serverPath)
                            .placeholder(R.drawable.icon_download)
                            .error(R.drawable.icon_download)
                            .centerCrop()
                            .into(elementImage);
                });
            });
        });
    }

    public void addProduct(ModelProducto producto, Runnable succes){
        Fetch.PostMap(producto, "/api/productos/addproduct", Response->succes.run());
    }

    public void removeProduct(String id, Runnable succes){
        Fetch.GET( "/api/productos/deleteProductos?id=" + id, Response->succes.run());
    }

    public void getProductos(Consumer<ArrayList<ModelProducto>> success){
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
                success.accept(modelProductos);
            });
        } catch (Exception e) {
            Log.e("APP_API_DEBUG", "ControllerAdmin ->" + e.getMessage());
        }
    }
}
