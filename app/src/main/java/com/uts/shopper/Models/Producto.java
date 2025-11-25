package com.uts.shopper.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Producto  implements Serializable {
    public String titulo;
    public String descripcion;
    public String imagenUrl;
    public double precioUnitairo;
    public double costoEnvio;
    public int cantidad;
    public double calificacion;

    public Producto(String titulo, String descripcion, String imagenUrl, double precioUnitairo, double costoEnvio, int cantidad, double calificacion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.precioUnitairo = precioUnitairo;
        this.costoEnvio = costoEnvio;
        this.cantidad = cantidad;
        this.calificacion = calificacion;
    }
}
