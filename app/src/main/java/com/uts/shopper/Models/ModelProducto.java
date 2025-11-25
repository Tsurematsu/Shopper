package com.uts.shopper.Models;

import java.io.Serializable;

public class ModelProducto implements Serializable {
    public String id;
    public String titulo;
    public String descripcion;
    public String imagenUrl;
    public double precioUnitairo;
    public double costoEnvio;
    public int cantidad;
    public double calificacion;

    public ModelProducto(String titulo, String descripcion, String imagenUrl, double precioUnitairo, double costoEnvio, int cantidad, double calificacion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.precioUnitairo = precioUnitairo;
        this.costoEnvio = costoEnvio;
        this.cantidad = cantidad;
        this.calificacion = calificacion;
    }

    public ModelProducto(String titulo, String descripcion, String imagenUrl, double precioUnitairo, double costoEnvio, int cantidad, double calificacion, String id) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.precioUnitairo = precioUnitairo;
        this.costoEnvio = costoEnvio;
        this.cantidad = cantidad;
        this.calificacion = calificacion;
        this.id = id;
    }
}
