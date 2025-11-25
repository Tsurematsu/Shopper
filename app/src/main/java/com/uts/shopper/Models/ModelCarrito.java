package com.uts.shopper.Models;

import java.io.Serializable;

public class ModelCarrito implements Serializable {
    public String id;
    public String titulo;
    public int cantidadSeleccionada;
    public double costoEnvio;
    public double precioUnitairo;
    public String imagenUrl;


    public ModelCarrito() {}
    public ModelCarrito(String id, String titulo, int cantidadSeleccionada, double costoEnvio, double precioUnitairo, String imagenUrl) {
        this.id = id;
        this.titulo = titulo;
        this.cantidadSeleccionada = cantidadSeleccionada;
        this.costoEnvio = costoEnvio;
        this.precioUnitairo = precioUnitairo;
        this.imagenUrl = imagenUrl;
    }

}
