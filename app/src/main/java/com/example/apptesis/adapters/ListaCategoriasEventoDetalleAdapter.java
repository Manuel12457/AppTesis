package com.example.apptesis.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptesis.R;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.CategoriaSQLite;

import java.util.ArrayList;
import java.util.Map;

public class ListaCategoriasEventoDetalleAdapter extends RecyclerView.Adapter<ListaCategoriasEventoDetalleAdapter.CategoriasEventoDetalleViewHolder>{

    private ArrayList<CategoriaSQLite> listaCategorias;
    private Context context;

    public ListaCategoriasEventoDetalleAdapter(ArrayList<CategoriaSQLite> listaCategorias, Context context) {
        this.listaCategorias = listaCategorias;
        this.context = context;
    }

    public ArrayList<CategoriaSQLite> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(ArrayList<CategoriaSQLite> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ListaCategoriasEventoDetalleAdapter.CategoriasEventoDetalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_categoria, parent, false);
        return new ListaCategoriasEventoDetalleAdapter.CategoriasEventoDetalleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaCategoriasEventoDetalleAdapter.CategoriasEventoDetalleViewHolder holder, int position) {
        holder.completado.setVisibility(View.GONE);

        CategoriaSQLite categoria = getListaCategorias().get(position);
        holder.categoria.setText(categoria.getCategoria());
        holder.descripcion.setText(categoria.getDescripcion());

        Uri primeraImagenUri = Uri.parse(categoria.getImagen());
        Glide.with(getContext())
                .load(primeraImagenUri)
                .into(holder.imagen);

    }

    @Override
    public int getItemCount() {
        return getListaCategorias().size();
    }

    public class CategoriasEventoDetalleViewHolder extends RecyclerView.ViewHolder {
        private TextView categoria;
        private TextView descripcion;
        private TextView completado;
        private ImageView imagen;

        public CategoriasEventoDetalleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoria = itemView.findViewById(R.id.idTituloLeccion);
            this.descripcion = itemView.findViewById(R.id.idDescripcion);
            this.completado = itemView.findViewById(R.id.idCompletado);
            this.imagen = itemView.findViewById(R.id.idImagen);

        }
    }
}
