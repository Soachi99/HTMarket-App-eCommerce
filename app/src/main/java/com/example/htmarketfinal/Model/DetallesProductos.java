package com.example.htmarketfinal.Model;

public class DetallesProductos {

    private String Cantidad, Nombre, Precio;

    public DetallesProductos(){

    }

    public DetallesProductos(String cantidad, String nombre, String precio) {
        Cantidad = cantidad;
        Nombre = nombre;
        Precio = precio;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }
}
