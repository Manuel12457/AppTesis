package com.example.apptesis.clases;

public class CategoriaSQLite {

    private String categoria_id;
    private String categoria;
    private String descripcion;
    private String imagen;
    private String id_evento;

    public CategoriaSQLite(String categoria_id, String categoria, String descripcion, String imagen, String id_evento) {
        this.categoria_id = categoria_id;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.id_evento = id_evento;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getId_evento() {
        return id_evento;
    }

    public void setId_evento(String id_evento) {
        this.id_evento = id_evento;
    }
}
