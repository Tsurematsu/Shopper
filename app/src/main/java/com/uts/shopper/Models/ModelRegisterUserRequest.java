package com.uts.shopper.Models;

public class ModelRegisterUserRequest {
    public String usuario;
    public String email;
    public String contrasena;
    public boolean is_admin =false;

    public ModelRegisterUserRequest(String email, String usuario, String contrasena, boolean is_admin) {
        this.usuario = usuario;
        this.email = email;
        this.contrasena = contrasena;
        this.is_admin = is_admin;
    }
}
