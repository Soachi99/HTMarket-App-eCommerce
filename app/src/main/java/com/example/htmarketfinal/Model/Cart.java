package com.example.htmarketfinal.Model;

public class Cart {

    private String Cantidad, fecha, image, nombre, precio, tiempo;

    public Cart(){

    }

    public Cart(String cantidad, String fecha, String image, String nombre, String precio, String tiempo) {
        Cantidad = cantidad;
        this.fecha = fecha;
        this.image = image;
        this.nombre = nombre;
        this.precio = precio;
        this.tiempo = tiempo;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public String getImage() {
        return image;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public String getTiempo() {
        return tiempo;
    }
}
