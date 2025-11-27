package com.uts.shopper.Controllers;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uts.shopper.Models.ModelProducto;
import com.uts.shopper.R;
import com.uts.shopper.helpers.Fetch;
import com.uts.shopper.helpers.FileHelper;

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

    public void addProduct(ModelProducto producto){
        Fetch.PostMap(producto, "/api/productos/addproduct", Response->{

        });
    }
}
