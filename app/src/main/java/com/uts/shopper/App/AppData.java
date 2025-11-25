package com.uts.shopper.App;

import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.lang.reflect.Type;

public class AppData {
    // Variable global de la clase
    private SharedPreferences prefs;
    private Gson gson;
    private AppCompatActivity parent = null;

    public AppData(AppCompatActivity parent){
        this.parent = parent;
        this.gson = new Gson();

        // CORRECCIÓN: Inicializamos la variable aquí para usarla en toda la clase
        this.prefs = parent.getSharedPreferences("MisPreferencias", MODE_PRIVATE);
    }

    public void setStorage(String key, String value){
        // Ya no necesitamos declarar 'SharedPreferences prefs = ...' aquí
        // Usamos 'this.prefs' que ya está lista desde el constructor
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStorage(String key){
        return prefs.getString(key, "");
    }

    public void setToGson(String key, Object value){
        SharedPreferences.Editor editor = prefs.edit();
        if (value == null) {
            editor.remove(key);
        } else {
            String json = gson.toJson(value);
            editor.putString(key, json);
        }
        editor.apply();
    }

    public <T> T getToGson(String key, Type type) {
        // AQUÍ ESTABA EL ERROR:
        // Antes usabas 'prefs' que era null. Ahora 'prefs' ya tiene valor desde el constructor.
        String json = prefs.getString(key, null);

        if (json == null) {
            return null;
        }
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}