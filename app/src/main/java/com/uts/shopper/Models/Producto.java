package com.uts.shopper.Models;

public class Producto {
    public String titulo;
    public String descripcion;
    public String imagenUrl;
    public long precioUnitairo;
    public long costoEnvio;
    public int cantidad;
    public int calificacion;

    public Producto(String titulo, String descripcion, String imagenUrl, long precioUnitairo, long costoEnvio, int cantidad, int calificacion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.precioUnitairo = precioUnitairo;
        this.costoEnvio = costoEnvio;
        this.cantidad = cantidad;
        this.calificacion = calificacion;
    }
}
