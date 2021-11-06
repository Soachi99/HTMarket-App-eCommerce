package com.example.htmarketfinal.Model;

public class Historial {

    private String Detalles, Direccion, Estado, Telefono, fecha, tiempo;
    private Long Total;

    public  Historial()
    {

    }

    public Historial(String detalles, String direccion, String estado, String telefono, String fecha, String tiempo, Long total) {
        Detalles = detalles;
        Direccion = direccion;
        Estado = estado;
        Telefono = telefono;
        this.fecha = fecha;
        this.tiempo = tiempo;
        Total = total;
    }

    public String getDetalles() {
        return Detalles;
    }

    public void setDetalles(String detalles) {
        Detalles = detalles;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public Long getTotal() {
        return Total;
    }

    public void setTotal(Long total) {
        Total = total;
    }
}
