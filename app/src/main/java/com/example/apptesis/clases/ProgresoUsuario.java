package com.example.apptesis.clases;

import java.util.Map;

public class ProgresoUsuario {

    private String progreso_usuario_id;
    private String catgoria_id;
    private String leccion_id;
    private String fecha_hora;
    private String porcentaje;

    public String getProgreso_usuario_id() {
        return progreso_usuario_id;
    }

    public void setProgreso_usuario_id(String progreso_usuario_id) {
        this.progreso_usuario_id = progreso_usuario_id;
    }

    public String getCatgoria_id() {
        return catgoria_id;
    }

    public void setCatgoria_id(String catgoria_id) {
        this.catgoria_id = catgoria_id;
    }

    public String getLeccion_id() {
        return leccion_id;
    }

    public void setLeccion_id(String leccion_id) {
        this.leccion_id = leccion_id;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }
}
