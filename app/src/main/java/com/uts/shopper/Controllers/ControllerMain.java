package com.uts.shopper.Controllers;

import androidx.appcompat.app.AppCompatActivity;

public class ControllerMain {
    private AppCompatActivity parent = null;
    public ControllerMain(AppCompatActivity parent){
        this.parent = parent;
    }
    public Runnable panelOnSelectProduct = null;
    public void CargarProductos(String url){

    }
}
