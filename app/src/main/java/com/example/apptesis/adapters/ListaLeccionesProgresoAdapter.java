package com.example.apptesis.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptesis.R;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.Leccion;
import com.example.apptesis.clases.ProgresoUsuario;

import java.util.ArrayList;
import java.util.Map;

public class ListaLeccionesProgresoAdapter extends RecyclerView.Adapter<ListaLeccionesProgresoAdapter.ListaLeccionesProgresoViewHolder>{

    private Map<String, Leccion> lecciones;
    private Context context;
    private ArrayList<ProgresoUsuario> listaProgreso;

    public Map<String, Leccion> getLecciones() {
        return lecciones;
    }

    public void setLecciones(Map<String, Leccion> lecciones) {
        this.lecciones = lecciones;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<ProgresoUsuario> getListaProgreso() {
        return listaProgreso;
    }

    public void setListaProgreso(ArrayList<ProgresoUsuario> listaProgreso) {
        this.listaProgreso = listaProgreso;
    }

    public ListaLeccionesProgresoAdapter(Context context, Map<String, Leccion> lecciones, ArrayList<ProgresoUsuario> listaProgreso) {
        this.setLecciones(lecciones);
        this.setContext(context);
        this.setListaProgreso(listaProgreso);
    }

    @NonNull
    @Override
    public ListaLeccionesProgresoAdapter.ListaLeccionesProgresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_leccion_progreso, parent, false);
        return new ListaLeccionesProgresoAdapter.ListaLeccionesProgresoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaLeccionesProgresoAdapter.ListaLeccionesProgresoViewHolder holder, int position) {
        Leccion leccion = getLecciones().get(position);
        Log.d("TITULO", leccion.getTitulo());
        holder.titulo.setText(leccion.getTitulo());

        for (ProgresoUsuario p : listaProgreso) {
            if (p.getLeccion_id().equals(leccion.getLeccion_id())) {
                Log.d("COMPLETADO", leccion.getTitulo());
                holder.completado.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ListaLeccionesProgresoViewHolder extends RecyclerView.ViewHolder {
        private TextView titulo;
        private TextView completado;
        public ListaLeccionesProgresoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titulo = itemView.findViewById(R.id.idTituloLeccion);
            this.completado = itemView.findViewById(R.id.idProgresoCompletado);

        }
    }
}
