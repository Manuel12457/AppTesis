package com.example.apptesis.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.apptesis.R;
import com.example.apptesis.databinding.FragmentNavCalendarioBinding;
import com.example.apptesis.databinding.FragmentNavEventoBinding;

public class nav_evento extends Fragment {

    private FragmentNavEventoBinding binding;
    TextView tituloEvento;
    TextView fechaEvento;
    TextView horaEvento;
    TextView duracionEvento;
    TextView avisoEvento;
    TextView notasEvento;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavEventoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        tituloEvento = view.findViewById(R.id.idTituloEventoDetalle);
        fechaEvento = view.findViewById(R.id.idFechaEventoDetalle);
        horaEvento = view.findViewById(R.id.idHoraEventoDetalle);
        duracionEvento = view.findViewById(R.id.idDuracionEventoDetalle);
        avisoEvento = view.findViewById(R.id.idAvisoEventoDetalle);
        notasEvento = view.findViewById(R.id.idNotasEventoDetalle);
        recyclerView = view.findViewById(R.id.idRecyclerViewEventoEdicion);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_evento_detalle, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.editarEvento) {
                    Navigation.findNavController(view).navigate(R.id.nav_eventoEdicion);
                    return true;
                } else if (menuItem.getItemId() == R.id.eliminarEvento) {

                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
}