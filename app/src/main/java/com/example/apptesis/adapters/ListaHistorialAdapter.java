package com.example.apptesis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptesis.R;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.Historial;

import java.util.ArrayList;

public class ListaHistorialAdapter extends RecyclerView.Adapter<ListaHistorialAdapter.HistorialViewHolder>{

    private ArrayList<Historial> listaHistorial;
    private Context context;

    public ListaHistorialAdapter(ArrayList<Historial> listaHistorial, Context context) {
        this.listaHistorial = listaHistorial;
        this.context = context;
    }

    public ArrayList<Historial> getListaHistorial() {
        return listaHistorial;
    }

    public void setListaHistorial(ArrayList<Historial> listaHistorial) {
        this.listaHistorial = listaHistorial;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ListaHistorialAdapter.HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_historial, parent, false);
        return new ListaHistorialAdapter.HistorialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaHistorialAdapter.HistorialViewHolder holder, int position) {
        Historial historial = getListaHistorial().get(position);
        holder.gesto.setText(historial.getGesto());
        holder.precision.setText(historial.getPrecision());
        holder.hora.setText(historial.getHora());
    }

    @Override
    public int getItemCount() {
        return getListaHistorial().size();
    }

    public class HistorialViewHolder extends RecyclerView.ViewHolder {
        private TextView gesto;
        private TextView precision;
        private TextView hora;

        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            this.gesto = itemView.findViewById(R.id.idGestoHistorial);
            this.precision = itemView.findViewById(R.id.idPrecisionHistorial);
            this.hora = itemView.findViewById(R.id.idHoraHistorial);
        }
    }
}
