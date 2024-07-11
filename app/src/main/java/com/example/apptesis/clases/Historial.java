package com.example.apptesis.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Historial implements Parcelable {

    private String gesto;
    private String precision;
    private String hora;

    public Historial(String gesto, String precision, String hora) {
        this.gesto = gesto;
        this.precision = precision;
        this.hora = hora;
    }

    public Historial(Parcel in) {
        gesto = in.readString();
        precision = in.readString();
        hora = in.readString();
    }

    public static final Creator<Historial> CREATOR = new Creator<Historial>() {
        @Override
        public Historial createFromParcel(Parcel in) {
            return new Historial(in);
        }

        @Override
        public Historial[] newArray(int size) {
            return new Historial[size];
        }
    };

    public String getGesto() {
        return gesto;
    }

    public void setGesto(String gesto) {
        this.gesto = gesto;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(gesto);
        dest.writeString(precision);
        dest.writeString(hora);
    }
}
