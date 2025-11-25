package com.uts.shopper.App;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class AppData {
    private SharedPreferences prefs;
    private Gson gson;
    private AppCompatActivity parent = null;
    public AppData(AppCompatActivity parent){
        this.parent = parent;
        gson = new Gson();
    }
    public void setStorage(String key, String value){
        SharedPreferences prefs = parent.getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply(); // Guarda en segundo plano
    }
    public String getStorage(String key){
        SharedPreferences prefs = parent.getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public  void setToGson(String key, Object value){
        SharedPreferences prefs = parent.getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (value == null) {
            // Si el objeto es nulo, es buena práctica borrar la entrada o guardar nada
            editor.remove(key);
        } else {
            String json = gson.toJson(value);
            editor.putString(key, json);
        }
        editor.apply();
    }
    public <T> T getToGson(String key, Type type) {
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
