package com.example.apptesis.clases;

import java.util.Map;

public class Evento {

    private String evento_id;
    private String titulo;
    private String duracion;
    private String fechaHora;
    private String tiempoAviso;
    private String notas;

    public Evento(String evento_id, String titulo, String duracion, String fechaHora, String tiempoAviso, String notas) {
        this.evento_id = evento_id;
        this.titulo = titulo;
        this.duracion = duracion;
        this.fechaHora = fechaHora;
        this.tiempoAviso = tiempoAviso;
        this.notas = notas;
    }

    public String getEvento_id() {
        return evento_id;
    }

    public void setEvento_id(String evento_id) {
        this.evento_id = evento_id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTiempoAviso() {
        return tiempoAviso;
    }

    public void setTiempoAviso(String tiempoAviso) {
        this.tiempoAviso = tiempoAviso;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
