package com.example.apptesis.clases;

import java.util.ArrayList;
import java.util.Map;

public class Categoria {

    private String categoria_id;
    private String categoria;
    private String descripcion;
    private Map<String, String> imagen;
    private Map<String, Leccion> lecciones;

    public Map<String, Leccion> getLecciones() {
        return lecciones;
    }

    public void setLecciones(Map<String, Leccion> lecciones) {
        this.lecciones = lecciones;
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
