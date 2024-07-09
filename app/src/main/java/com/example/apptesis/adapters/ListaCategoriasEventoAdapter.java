package com.example.apptesis.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.SparseBooleanArray;
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
import com.example.apptesis.clases.CategoriaSQLite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ListaCategoriasEventoAdapter extends RecyclerView.Adapter<ListaCategoriasEventoAdapter.CategoriasEventoViewHolder>{

    private ArrayList<Categoria> listaCategorias;
    private ArrayList<CategoriaSQLite> listaCategoriasSQL;

    public ArrayList<CategoriaSQLite> getListaCategoriasSQL() {
        return listaCategoriasSQL;
    }

    public void setListaCategoriasSQL(ArrayList<CategoriaSQLite> listaCategoriasSQL) {
        this.listaCategoriasSQL = listaCategoriasSQL;
    }

    private Context context;
    private SparseBooleanArray selectedItems;

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

    public ListaCategoriasEventoAdapter(ArrayList<Categoria> listaCategorias, Context context, ArrayList<CategoriaSQLite> listaCategoriasSQL) {
        this.setListaCategorias(listaCategorias);
        this.setContext(context);
        this.setListaCategoriasSQL(listaCategoriasSQL);
        this.selectedItems = new SparseBooleanArray();
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
        holder.checkBox.setVisibility(View.VISIBLE);

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

        if (getListaCategoriasSQL() != null && !getListaCategoriasSQL().isEmpty()) {
            for (CategoriaSQLite c : getListaCategoriasSQL()) {
                if (c.getCategoria().equals(categoria.getCategoria())) {
                    holder.checkBox.toggle();
                    selectedItems.put(position, true);
                    break;
                }
            }
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    if (isChecked) {
                        selectedItems.put(adapterPosition, true);
                    } else {
                        selectedItems.delete(adapterPosition);
                    }
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

    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }

}
