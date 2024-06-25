package com.example.apptesis.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apptesis.R;
import com.example.apptesis.adapters.ListaCategoriasAdapter;
import com.example.apptesis.adapters.ListaCategoriasProgresoAdapter;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.ProgresoUsuario;
import com.example.apptesis.databinding.FragmentNavPracticaBinding;
import com.example.apptesis.viewModels.ListaCategoriasViewModel;
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

public class nav_practica extends Fragment {

    ListaCategoriasViewModel listaCategoriasViewModel;
    ListaProgresoUsuarioViewModel listaProgresoUsuarioViewModel;
    RecyclerView recyclerView;
    ListaCategoriasAdapter adapter;
    ArrayList<Categoria> listaCategoria = new ArrayList<>();
    ArrayList<ProgresoUsuario> listaProgreso = new ArrayList<>();
    private FragmentNavPracticaBinding binding;
    LinearProgressIndicator linearProgressIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavPracticaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        linearProgressIndicator = view.findViewById(R.id.linearProgressIndicator);
        recyclerView = view.findViewById(R.id.idRecyclerCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ListaCategoriasAdapter(listaCategoria, getContext(), listaProgreso);
        recyclerView.setAdapter(adapter);

        // ViewModel initialization
        listaCategoriasViewModel = new ViewModelProvider(this).get(ListaCategoriasViewModel.class);
        listaProgresoUsuarioViewModel = new ViewModelProvider(this).get(ListaProgresoUsuarioViewModel.class);

        // Observe category data changes
        listaCategoriasViewModel.getCategoryResult().observe(getViewLifecycleOwner(), new Observer<ListaCategoriasViewModel.CategoryResult>() {
            @Override
            public void onChanged(ListaCategoriasViewModel.CategoryResult categoryResult) {
                if (categoryResult.error != null) {
                    // Handle error
                    Log.d("ESTADO listaCategoria", categoryResult.error);
                    Snackbar.make(view, categoryResult.error, Snackbar.LENGTH_LONG).show();
                } else {
                    listaCategoria.clear();
                    listaCategoria.addAll(categoryResult.categorias);
                    adapter.notifyDataSetChanged(); // Update RecyclerView

                    linearProgressIndicator.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        // Observe progress data changes
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

        // Fetch category and progress data
        fetchCategoryAndProgressData();

        return view;
    }

    private void fetchCategoryAndProgressData() {
        // Fetch categories
        listaCategoriasViewModel.fetchCategories();

        // Fetch progress data
        listaProgresoUsuarioViewModel.fetchProgresoUsuarios();
    }

}