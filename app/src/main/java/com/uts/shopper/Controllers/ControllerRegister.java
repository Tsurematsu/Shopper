package com.uts.shopper.Controllers;

import android.util.Log;

import com.uts.shopper.Models.ModelRegisterUserRequest;
import com.uts.shopper.helpers.Fetch;

import java.util.function.Consumer;

public class ControllerRegister {
    public void RegistoPersona(ModelRegisterUserRequest data, Consumer<Boolean> onResult){
        try {
            Fetch.PostMap(data,"/api/personas/register", (lista) -> {
                if (lista == null)  return;
                if (lista.containsKey("error")) {
                    String msg = (String) lista.get("error");
                    Log.d("APP_API_DEBUG", "Error registro ---> " + msg);
                    onResult.accept(false);
                    return;
                }
                onResult.accept(true);
            });
        } catch (Exception e) {
            Log.e("APP_API_DEBUG", e.getMessage());
            onResult.accept(true);

        }
    }
}
