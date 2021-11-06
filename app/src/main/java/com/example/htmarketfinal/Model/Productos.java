package com.example.htmarketfinal.Model;

public class Productos {
    public String categoria, descripcion, nombre;
    public Long image, precio;


    public Productos() {

    }

    public Productos(String categoria, String descripcion, String nombre, Long image, Long precio) {
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.image = image;
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getImage() {
        return image;
    }

    public Long getPrecio() {
        return precio;
    }
}

