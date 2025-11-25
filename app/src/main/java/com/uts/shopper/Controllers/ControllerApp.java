package com.uts.shopper.Controllers;

// No necesitas importar Consumer si usas Runnable
// import java.util.function.Consumer;

import android.util.Log;
import android.widget.Toast;

import com.uts.shopper.helpers.Fetch;

public class ControllerApp {
    private static boolean flagAppStarted = false;
    public void startApp(Runnable callback){
        if (!flagAppStarted){
            flagAppStarted = true;
            // Ejecutamos el código que nos pasaron
            if (callback != null) {
                callback.run();
            }
        }
    }
    public void testUrlApi(String url){
        Log.d("APP_API_DEBUG", "Conectado a: " + url);
        Fetch.GET("/api", (response) -> {
            if (response != null) {
                Log.d("APP_API_DEBUG", "Respuesta cruda: " + response);
            } else {
                Log.e("APP_API_DEBUG", "Error en la petición");
            }
        });
    }
}