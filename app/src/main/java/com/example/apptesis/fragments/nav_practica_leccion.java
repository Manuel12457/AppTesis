package com.example.apptesis.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apptesis.R;
import com.example.apptesis.adapters.ListaCategoriasAdapter;
import com.example.apptesis.adapters.ListaLeccionesAdapter;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.Leccion;
import com.example.apptesis.clases.ProgresoUsuario;
import com.example.apptesis.databinding.FragmentNavPracticaBinding;
import com.example.apptesis.databinding.FragmentNavPracticaLeccionBinding;
import com.example.apptesis.viewModels.ListaCategoriasViewModel;
import com.example.apptesis.viewModels.ListaLeccionesViewModel;
import com.example.apptesis.viewModels.ListaProgresoUsuarioViewModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class nav_practica_leccion extends Fragment {

    ListaProgresoUsuarioViewModel listaProgresoUsuarioViewModel;
    ListaLeccionesViewModel listaLeccionesViewModel;
    RecyclerView recyclerView;
    ListaLeccionesAdapter adapter;
    ArrayList<Leccion> listaLecciones = new ArrayList<>();
    ArrayList<ProgresoUsuario> listaProgreso = new ArrayList<>();
    String categoria_id;
    String categoria;
    TextView textView;
    LinearProgressIndicator linearProgressIndicator;
    private FragmentNavPracticaLeccionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavPracticaLeccionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        linearProgressIndicator = view.findViewById(R.id.linearProgressIndicator);
        textView = view.findViewById(R.id.textView15);
        recyclerView = view.findViewById(R.id.idRecyclerLecciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            categoria_id = bundle.getString("id");
            categoria = bundle.getString("categoria");
        }
        textView.setText(categoria);

        adapter = new ListaLeccionesAdapter(listaLecciones, getContext(), listaProgreso, categoria_id);
        recyclerView.setAdapter(adapter);

        // ViewModel initialization
        listaLeccionesViewModel = new ViewModelProvider(this).get(ListaLeccionesViewModel.class);
        listaProgresoUsuarioViewModel = new ViewModelProvider(this).get(ListaProgresoUsuarioViewModel.class);

        // Observe changes in lessons data
        listaLeccionesViewModel.getLeccionesResult().observe(getViewLifecycleOwner(), new Observer<ListaLeccionesViewModel.LeccionesResult>() {
            @Override
            public void onChanged(ListaLeccionesViewModel.LeccionesResult leccionesResult) {
                if (leccionesResult.error != null) {
                    // Handle error
                    Log.e("FirebaseError", leccionesResult.error);
                    Snackbar.make(view, leccionesResult.error, Snackbar.LENGTH_LONG).show();
                } else if (leccionesResult.lecciones != null) {
                    listaLecciones.clear();
                    listaLecciones.addAll(leccionesResult.lecciones);
                    adapter.notifyDataSetChanged(); // Update RecyclerView

                    linearProgressIndicator.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        // Fetch lessons data
        listaLeccionesViewModel.fetchLecciones(categoria_id);

        // Observe changes in user progress data
        listaProgresoUsuarioViewModel.getProgresoResult().observe(getViewLifecycleOwner(), new Observer<ListaProgresoUsuarioViewModel.ProgresoResult>() {
            @Override
            public void onChanged(ListaProgresoUsuarioViewModel.ProgresoResult progresoResult) {
                if (progresoResult.error != null) {
                    // Handle error
                    Log.d("ESTADO listaProgreso", progresoResult.error);
                    Snackbar.make(view, progresoResult.error, Snackbar.LENGTH_LONG).show();
                } else {
                    listaProgreso.clear();
                    listaProgreso.addAll(progresoResult.progresoUsuarios);
                    adapter.notifyDataSetChanged(); // Update RecyclerView
                }
            }
        });

        // Fetch user progress data
        listaProgresoUsuarioViewModel.fetchProgresoUsuarios();

        return view;
    }

}