package com.uts.shopper.Auxiliar;


import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.uts.shopper.helpers.Fetch;

import java.util.function.Consumer;

public class AuxMain {
    private AppCompatActivity parent = null;

    public AuxMain(AppCompatActivity parent){
        this.parent = parent;
    }
    private static String flagUrl = "";
    public void StartingConnection(Consumer<String> Connected){
        if(!flagUrl.isEmpty()){
            Connected.accept(flagUrl);
            return;
        }

        String[] posiblesServidores = {
                "http://192.168.0.35:8080", //Conexion de prueba en parcial
                "http://192.168.80.23:8080", // Conexion de test PC de mesa
                "http://10.10.23.32:8080", // Conexion PORTABLE
        };

        Fetch.findWorkingHost(posiblesServidores, (url) -> {
            parent.runOnUiThread(()->{
                flagUrl = url;
                Connected.accept(url);
            });}, () -> {
            parent.runOnUiThread(() -> {
                Log.e("APP_API_DEBUG", "ERROR DE CONEXION CON EL SERVIDOR.");
                Toast.makeText(parent, "Servidor no disponible", Toast.LENGTH_LONG).show();
            });
        });
    };

    public static ActivityResultLauncher<Intent> makeLauncher(AppCompatActivity parent){
     return parent.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // AQUI LLEGAMOS CUANDO SE CIERRA "B"

                    // Verificamos que todo salió bien (RESULT_OK)
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Obtenemos los datos
                        Intent data = result.getData();
                        if (data != null) {
                            String mensajeRecibido = data.getStringExtra("CLAVE_MENSAJE");
                            // Haces lo que quieras con el dato
                            System.out.println("Dato recibido: " + mensajeRecibido);
                        }
                    }
                }
            });
    }
}
