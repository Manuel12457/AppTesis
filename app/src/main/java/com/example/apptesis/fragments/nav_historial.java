package com.example.apptesis.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apptesis.Presentacion;
import com.example.apptesis.R;
import com.example.apptesis.RegistroUsuario;
import com.example.apptesis.adapters.ListaHistorialAdapter;
import com.example.apptesis.adapters.ListaLeccionesAdapter;
import com.example.apptesis.clases.Historial;
import com.example.apptesis.databinding.FragmentNavHistorialBinding;
import com.example.apptesis.databinding.FragmentNavModoLibreBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class nav_historial extends Fragment {

    FragmentNavHistorialBinding binding;
    String titulo;
    TextView tituloHistorial;
    TextView noHistorial;
    ArrayList<Historial> listaHistorial;
    RecyclerView recyclerView;
    ListaHistorialAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavHistorialBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            titulo = bundle.getString("historial");
            listaHistorial = bundle.getParcelableArrayList("listaHistorial");
        }

        noHistorial = view.findViewById(R.id.textView14);
        tituloHistorial = view.findViewById(R.id.textViewHistorial);
        tituloHistorial.setText(titulo);
        recyclerView = view.findViewById(R.id.idRecyclerViewHistorial);
        if (listaHistorial != null && !listaHistorial.isEmpty()) {
            noHistorial.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ListaHistorialAdapter(listaHistorial, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_historial, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.borrarHistorial) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                    builder.setMessage("¿Seguro que desea eliminar el historial de reconocimiento?");
                    builder.setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    listaHistorial.clear();
                                    adapter.notifyDataSetChanged();
                                    noHistorial.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                            });
                    builder.setNegativeButton("Cancelar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }*/

}