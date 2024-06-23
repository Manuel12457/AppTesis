package com.example.apptesis.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptesis.R;
import com.example.apptesis.clases.Leccion;
import com.example.apptesis.clases.ProgresoUsuario;

import java.util.ArrayList;
import java.util.Map;

public class ListaLeccionesAdapter extends RecyclerView.Adapter<ListaLeccionesAdapter.LeccionesViewHolder>{

    private ArrayList<Leccion> listaLecciones;
    private Context context;
    private ArrayList<ProgresoUsuario> listaProgreso;
    private String categoria_id;

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    public ArrayList<ProgresoUsuario> getListaProgreso() {
        return listaProgreso;
    }

    public void setListaProgreso(ArrayList<ProgresoUsuario> listaProgreso) {
        this.listaProgreso = listaProgreso;
    }

    public ArrayList<Leccion> getListaLecciones() {
        return listaLecciones;
    }

    public void setListaLecciones(ArrayList<Leccion> listaLecciones) {
        this.listaLecciones = listaLecciones;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ListaLeccionesAdapter(ArrayList<Leccion> listaLecciones, Context context, ArrayList<ProgresoUsuario> listaProgreso, String categoria_id) {
        this.setListaLecciones(listaLecciones);
        this.setContext(context);
        this.setListaProgreso(listaProgreso);
        this.setCategoria_id(categoria_id);
    }

    @NonNull
    @Override
    public ListaLeccionesAdapter.LeccionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_leccion, parent, false);
        return new ListaLeccionesAdapter.LeccionesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaLeccionesAdapter.LeccionesViewHolder holder, int position) {
        Leccion leccion = getListaLecciones().get(position);
        holder.leccion.setText(leccion.getTitulo());

        for (ProgresoUsuario p : listaProgreso) {
            if (p.getLeccion_id().equalsIgnoreCase(leccion.getLeccion_id())) {
                holder.linearCompletado.setVisibility(View.VISIBLE);
                holder.porcentaje.setText("Precisión: " + p.getPorcentaje());
            }
        }

        if (leccion.getImagen() != null) {
            for (Map.Entry<String, String> entry : leccion.getImagen().entrySet()) {
                Uri primeraImagenUri = Uri.parse(entry.getValue());
                Glide.with(getContext())
                        .load(primeraImagenUri)
                        .into(holder.imagen);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("leccion_id", leccion.getLeccion_id());
                bundle.putString("categoria_id", categoria_id);
                Navigation.findNavController(v).navigate(R.id.nav_practica_leccion_detalle, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListaLecciones().size();
    }

    public class LeccionesViewHolder extends RecyclerView.ViewHolder {
        private TextView leccion;
        private TextView porcentaje;
        private ImageView imagen;
        private LinearLayout linearCompletado;

        public LeccionesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.leccion = itemView.findViewById(R.id.idTituloLeccion);
            this.porcentaje = itemView.findViewById(R.id.idProgresoCompletado);
            this.imagen = itemView.findViewById(R.id.idImagen);
            this.linearCompletado = itemView.findViewById(R.id.idLinearCompletado);
        }
    }
}
