package com.example.htmarketfinal.Model;

public class Usuarios
{
    // DATOS DE USUARIOS
    private String Celular, Contra, Correo, Usuario;

    public Usuarios()
    {

    }

    public Usuarios(String celular, String contra, String correo, String usuario) {
        Celular = celular;
        Contra = contra;
        Correo = correo;
        Usuario = usuario;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public String getContra() {
        return Contra;
    }

    public void setContra(String contra) {
        Contra = contra;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }
}
