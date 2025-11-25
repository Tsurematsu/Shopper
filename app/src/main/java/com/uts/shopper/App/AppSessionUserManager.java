package com.uts.shopper.App;

import androidx.appcompat.app.AppCompatActivity;

import com.uts.shopper.Models.ModelSessionUser;

public class AppSessionUserManager {
    private AppCompatActivity parent = null;
    private AppData appData;
    public AppSessionUserManager(AppCompatActivity parent){
        this.parent = parent;
        appData = new AppData(parent);
    }
    public void saveUserSession(ModelSessionUser user){
        appData.setStorage("usuario", user.usuario);
        appData.setStorage("email", user.email);
        appData.setStorage("isAdmin", String.valueOf(user.isAdmin));
        appData.setStorage("id", user.id);
    }
    public ModelSessionUser getUserSession(){
        ModelSessionUser response = new ModelSessionUser();
        response.usuario = appData.getStorage("usuario");
        response.email = appData.getStorage("email");
        response.isAdmin = Boolean.parseBoolean(appData.getStorage("isAdmin").isEmpty() ?"false":appData.getStorage("isAdmin"));
        response.id = appData.getStorage("id");
        return response;
    }

    public void clearUserSession(){
        appData.setStorage("usuario", "");
        appData.setStorage("email", "");
        appData.setStorage("isAdmin", "");
        appData.setStorage("id", "");
    }
}
