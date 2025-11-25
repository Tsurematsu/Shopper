package com.uts.shopper.Controllers;

import android.util.Log;

import com.uts.shopper.Models.ModelRequestLoginUser;
import com.uts.shopper.Models.ModelResultLoginUser;
import com.uts.shopper.helpers.Fetch;

import java.util.function.Consumer;

public class ControllerLogin {
    public void LoginPersona(ModelRequestLoginUser data, Consumer<ModelResultLoginUser> onResult){
        try {
            Fetch.PostMap(data,"/api/personas/login", (lista) -> {
                if (lista.containsKey("error")) {
                    String msg = String.valueOf(lista.get("error"));
                    Log.d("APP_API_DEBUG", "Error Login ---> " + msg);
                    onResult.accept(new ModelResultLoginUser());
                    return;
                }
                if (lista.containsKey("id")) {
                    ModelResultLoginUser responseData = new ModelResultLoginUser();
                    responseData.id = String.valueOf(lista.get("id"));
                    responseData.usuario = String.valueOf(lista.get("usuario"));
                    responseData.isAdmin = Boolean.parseBoolean(String.valueOf(lista.get("isAdmin")));
                    responseData.logged = true;
                    onResult.accept(responseData);
                    return;
                }
                onResult.accept(new ModelResultLoginUser());
            });
        } catch (Exception e) {
            Log.e("APP_API_DEBUG", e.getMessage());
        }
    }
}
