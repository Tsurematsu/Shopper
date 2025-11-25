package com.uts.shopper.App;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class AppData {
    private AppCompatActivity parent = null;
    public AppData(AppCompatActivity parent){
        this.parent = parent;
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
}
