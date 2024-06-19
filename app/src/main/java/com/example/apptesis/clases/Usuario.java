package com.example.apptesis.clases;

import java.util.Map;

public class Usuario {

    private String usuario_id;
    private String nombre;
    private String apellido;
    private String correo;
    private Map<String, String> imagen;

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Map<String, String> getImagen() {
        return imagen;
    }

    public void setImagen(Map<String, String> imagen) {
        this.imagen = imagen;
    }

}
