package com.example.apptesis.clases;

import java.util.Map;

public class Leccion {

    private String leccion_id;
    private String titulo;
    private String descripcion;
    private Map<String, String> imagen;

    public String getLeccion_id() {
        return leccion_id;
    }

    public void setLeccion_id(String leccion_id) {
        this.leccion_id = leccion_id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Map<String, String> getImagen() {
        return imagen;
    }

    public void setImagen(Map<String, String> imagen) {
        this.imagen = imagen;
    }
}
