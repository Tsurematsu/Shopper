package com.uts.shopper.Models;

public class ModelSessionUser {
    public String id;
    public String email;
    public String usuario;
    public boolean isAdmin;

    public ModelSessionUser(){

    }
    public ModelSessionUser(String id, String email, String usuario, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.usuario = usuario;
        this.isAdmin = isAdmin;
    }
}
