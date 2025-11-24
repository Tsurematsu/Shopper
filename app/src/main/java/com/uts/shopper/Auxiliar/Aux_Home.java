package com.uts.shopper.Auxiliar;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uts.shopper.helpers.Fetch;

import java.util.function.Consumer;

public class Aux_Home {
    private AppCompatActivity parent = null;
    public Aux_Home(AppCompatActivity parent){
        this.parent = parent;
    }
    public void connection(Consumer<String> passCall){
        String[] posiblesServidores = {
                "http://localhost:8080", //Conexion para desarrollo
                "http://192.168.0.35:8080", //Conexion de prueba en vivo
                "http://192.168.80.23:8080", // Conexion de test PC de mesa
        };
        Fetch.findWorkingHost(posiblesServidores, (url) -> {
            parent.runOnUiThread(() -> {passCall.accept(url);});}, () -> {
            parent.runOnUiThread(() -> {
                Log.e("API_DEBUG", "ERROR DE CONEXION CON EL SERVIDOR.");
                Toast.makeText(parent, "Servidor no disponible", Toast.LENGTH_LONG).show();
            });
        });
    }
}
