package com.example.apptesis.clases;

import java.util.Map;

public class ProgresoUsuario {

    private String progreso_usuario_id;
    private String usuario_id;
    private String categoria_id;

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    private String leccion_id;
    private String fecha_hora;
    private String porcentaje;

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getProgreso_usuario_id() {
        return progreso_usuario_id;
    }

    public void setProgreso_usuario_id(String progreso_usuario_id) {
        this.progreso_usuario_id = progreso_usuario_id;
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
