package com.uts.shopper.Models;

public class ModelLoginUserRequest {
    String usuario;
    String contrasena;
    public ModelLoginUserRequest(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
}
