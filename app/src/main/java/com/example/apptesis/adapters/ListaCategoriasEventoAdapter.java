package com.example.apptesis.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptesis.R;
import com.example.apptesis.clases.Categoria;

import java.util.ArrayList;
import java.util.Map;

public class ListaCategoriasEventoAdapter extends RecyclerView.Adapter<ListaCategoriasEventoAdapter.CategoriasEventoViewHolder>{

    private ArrayList<Categoria> listaCategorias;
    private Context context;

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

    public ListaCategoriasEventoAdapter(ArrayList<Categoria> listaCategorias, Context context) {
        this.setListaCategorias(listaCategorias);
        this.setContext(context);
    }

    @NonNull
    @Override
    public ListaCategoriasEventoAdapter.CategoriasEventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_categoria, parent, false);
        return new ListaCategoriasEventoAdapter.CategoriasEventoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriasEventoViewHolder holder, int position) {
        holder.completado.setVisibility(View.GONE);

        Categoria categoria = getListaCategorias().get(position);
        holder.categoria.setText(categoria.getCategoria());
        holder.descripcion.setText(categoria.getDescripcion());

        if (categoria.getImagen() != null) {
            for (Map.Entry<String, String> entry : categoria.getImagen().entrySet()) {
                Uri primeraImagenUri = Uri.parse(entry.getValue());
                Glide.with(getContext())
                        .load(primeraImagenUri)
                        .into(holder.imagen);
            }
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListaCategorias().size();
    }

    public class CategoriasEventoViewHolder extends RecyclerView.ViewHolder {
        private TextView categoria;
        private TextView descripcion;
        private TextView completado;
        private CheckBox checkBox;
        private ImageView imagen;

        public CategoriasEventoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoria = itemView.findViewById(R.id.idTituloLeccion);
            this.descripcion = itemView.findViewById(R.id.idDescripcion);
            this.completado = itemView.findViewById(R.id.idCompletado);
            this.checkBox = itemView.findViewById(R.id.include_category);
            this.imagen = itemView.findViewById(R.id.idImagen);

        }
    }
}
