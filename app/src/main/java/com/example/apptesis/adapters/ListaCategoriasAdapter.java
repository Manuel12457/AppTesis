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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptesis.R;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.ProgresoUsuario;

import java.util.ArrayList;
import java.util.Map;

public class ListaCategoriasAdapter extends RecyclerView.Adapter<ListaCategoriasAdapter.CategoriasViewHolder> {

    private ArrayList<Categoria> listaCategorias;
    private Context context;
    private ArrayList<ProgresoUsuario> listaProgreso;

    public ArrayList<ProgresoUsuario> getListaProgreso() {
        return listaProgreso;
    }

    public void setListaProgreso(ArrayList<ProgresoUsuario> listaProgreso) {
        this.listaProgreso = listaProgreso;
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

    public ListaCategoriasAdapter(ArrayList<Categoria> listaCategorias, Context context, ArrayList<ProgresoUsuario> listaProgreso) {
        this.setListaCategorias(listaCategorias);
        this.setContext(context);
        this.setListaProgreso(listaProgreso);
    }

    @NonNull
    @Override
    public CategoriasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_categoria, parent, false);
        return new ListaCategoriasAdapter.CategoriasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriasViewHolder holder, int position) {
        Categoria categoria = getListaCategorias().get(position);
        holder.categoria.setText(categoria.getCategoria());
        holder.descripcion.setText(categoria.getDescripcion());

        int leccionesCompletadasPorUsuario = 0;
        for (ProgresoUsuario p : listaProgreso) {
            Log.d("msg", "CATEGORIA PROGRESO ID: " + p.getCategoria_id());
            Log.d("msg", "CATEGORIA ID: " + categoria.getCategoria_id());
            if (p.getCategoria_id().equals(categoria.getCategoria_id())) {
                leccionesCompletadasPorUsuario++;
            }
        }
        holder.completado.setText(leccionesCompletadasPorUsuario + "/" + categoria.getLecciones().size());

        if (categoria.getImagen() != null) {
            for (Map.Entry<String, String> entry : categoria.getImagen().entrySet()) {
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
                bundle.putString("id", categoria.getCategoria_id());
                Navigation.findNavController(v).navigate(R.id.nav_practica_leccion, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListaCategorias().size();
    }

    public class CategoriasViewHolder extends RecyclerView.ViewHolder {
        private TextView categoria;
        private TextView descripcion;
        private TextView completado;
        private ImageView imagen;

        public CategoriasViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoria = itemView.findViewById(R.id.idTituloLeccion);
            this.descripcion = itemView.findViewById(R.id.idDescripcion);
            this.completado = itemView.findViewById(R.id.idCompletado);
            this.imagen = itemView.findViewById(R.id.idImagen);

        }
    }
}
