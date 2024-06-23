package com.example.apptesis.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptesis.R;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.ProgresoUsuario;

import java.util.ArrayList;
import java.util.Map;

public class ListaCategoriasProgresoAdapter extends RecyclerView.Adapter<ListaCategoriasProgresoAdapter.ListaCategoriasProgresoViewHolder>{

    private ArrayList<Categoria> listaCategorias;
    private Context context;
    private ArrayList<ProgresoUsuario> listaProgreso;

    public ListaCategoriasProgresoAdapter(Context context, ArrayList<Categoria> listaCategorias, ArrayList<ProgresoUsuario> listaProgreso) {
        this.setListaCategorias(listaCategorias);
        this.setContext(context);
        this.setListaProgreso(listaProgreso);
    }

    public ArrayList<Categoria> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(ArrayList<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
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

    @NonNull
    @Override
    public ListaCategoriasProgresoAdapter.ListaCategoriasProgresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_categoria_progreso, parent, false);
        return new ListaCategoriasProgresoAdapter.ListaCategoriasProgresoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaCategoriasProgresoAdapter.ListaCategoriasProgresoViewHolder holder, int position) {
        Categoria categoria = getListaCategorias().get(position);
        holder.categoria.setText(categoria.getCategoria());
        holder.descripcion.setText(categoria.getDescripcion());

        int leccionesCompletadasPorUsuario = 0;
        for (ProgresoUsuario p : listaProgreso) {
            if (p.getCategoria_id().equals(categoria.getCategoria_id())) {
                leccionesCompletadasPorUsuario++;
            }
        }
        holder.completado.setText((Math.ceil(leccionesCompletadasPorUsuario*100/categoria.getLecciones().size()) + "%"));

        if (categoria.getImagen() != null) {
            for (Map.Entry<String, String> entry : categoria.getImagen().entrySet()) {
                Uri primeraImagenUri = Uri.parse(entry.getValue());
                Glide.with(getContext())
                        .load(primeraImagenUri)
                        .into(holder.imagen);
            }
        }

        ListaLeccionesProgresoAdapter listaLeccionesProgresoAdapter = new ListaLeccionesProgresoAdapter(getContext(), categoria.getLecciones(), listaProgreso);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        holder.recyclerView.setAdapter(listaLeccionesProgresoAdapter);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            boolean hidden = true;
            @Override
            public void onClick(View v) {
                if (hidden) {
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    hidden = false;
                } else {
                    holder.recyclerView.setVisibility(View.GONE);
                    hidden = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListaCategorias().size();
    }

    public class ListaCategoriasProgresoViewHolder extends RecyclerView.ViewHolder {
        private TextView categoria;
        private TextView descripcion;
        private TextView completado;
        private ImageView imagen;
        private RecyclerView recyclerView;

        public ListaCategoriasProgresoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoria = itemView.findViewById(R.id.idTituloLeccion);
            this.descripcion = itemView.findViewById(R.id.idDescripcion);
            this.completado = itemView.findViewById(R.id.idCompletado);
            this.imagen = itemView.findViewById(R.id.idImagen);
            this.recyclerView = itemView.findViewById(R.id.idRecyclerCategoria);

        }
    }
}
