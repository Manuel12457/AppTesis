package com.example.apptesis.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptesis.R;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.Evento;
import com.example.apptesis.clases.ProgresoUsuario;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ListaEventosAdapter extends RecyclerView.Adapter<ListaEventosAdapter.EventosViewHolder>{

    private ArrayList<Evento> listaEventos;
    private Context context;

    public ArrayList<Evento> getListaEventos() {
        return listaEventos;
    }

    public void setListaEventos(ArrayList<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ListaEventosAdapter(ArrayList<Evento> listaEventos, Context context) {
        this.setListaEventos(listaEventos);
        this.setContext(context);
    }

    @NonNull
    @Override
    public ListaEventosAdapter.EventosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_evento, parent, false);
        return new ListaEventosAdapter.EventosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaEventosAdapter.EventosViewHolder holder, int position) {
        Evento evento = getListaEventos().get(position);
        holder.textTituloEvento.setText(evento.getTitulo());
        String[] splitStr = evento.getFechaHora().split("\\s+");
        holder.textHora.setText(splitStr[1]);

        // FECHA FINAL
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH:mm");
        }
        LocalTime hora = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            hora = LocalTime.parse(splitStr[1], formatter);
        }

        int minutos = Integer.parseInt(evento.getDuracion());
        LocalTime nuevaHora = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nuevaHora = hora.plusMinutes(minutos);
        }
        String nuevaHoraStr = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nuevaHoraStr = nuevaHora.format(formatter);
        }
        // FECHA FINAL

        holder.textoDuracionEvento.setText(splitStr[1] + " - " + nuevaHoraStr);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", evento.getEvento_id());
                Navigation.findNavController(v).navigate(R.id.nav_evento, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getListaEventos().size();
    }

    public class EventosViewHolder extends RecyclerView.ViewHolder {
        private TextView textHora;
        private TextView textTituloEvento;
        private TextView textoDuracionEvento;

        public EventosViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textHora = itemView.findViewById(R.id.idTextHora);
            this.textTituloEvento = itemView.findViewById(R.id.idTextTituloEvento);
            this.textoDuracionEvento = itemView.findViewById(R.id.idTextoDuracionEvento);

        }
    }

    public void updateEventList(ArrayList<Evento> newEventList) {
        setListaEventos(newEventList);
        notifyDataSetChanged();
    }
}
