package com.example.apptesis.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.ProgresoUsuario;
import com.example.apptesis.databinding.FragmentNavPracticaBinding;
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

    DatabaseReference databaseReference;
    Query query;
    ListaCategoriasAdapter adapter;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    ArrayList<Categoria> listaCategorias = new ArrayList<>();
    ArrayList<ProgresoUsuario> listaProgreso = new ArrayList<>();

    private FragmentNavPracticaBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavPracticaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.idRecyclerCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaCategorias = new ArrayList<>();
        adapter = new ListaCategoriasAdapter(listaCategorias, getContext(), listaProgreso);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("categorias");
        valueEventListener = databaseReference.addValueEventListener(new nav_practica.listener());

        query = FirebaseDatabase.getInstance().getReference("progreso_usuarios").orderByChild("usuario_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        valueEventListener = query.addValueEventListener(new nav_practica.listenerProgreso());

        return view;
    }

    class listener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) { //Nodo referente existe
                listaCategorias.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Categoria categoria = ds.getValue(Categoria.class);
                    listaCategorias.add(categoria);
                }
                adapter.notifyDataSetChanged();
            } else {
                listaCategorias.clear();
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("msg", "Error onCancelled", error.toException());
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Error: " + error.toString(), Snackbar.LENGTH_LONG).show();
        }
    }

    class listenerProgreso implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) { //Nodo referente existe
                listaProgreso.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ProgresoUsuario progresoUsuario = ds.getValue(ProgresoUsuario.class);
                    listaProgreso.add(progresoUsuario);
                }
                adapter.notifyDataSetChanged();
            } else {
                listaProgreso.clear();
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("msg", "Error onCancelled", error.toException());
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Error: " + error.toString(), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        query.removeEventListener(valueEventListener);
        //databaseReference.removeEventListener(valueEventListener);
        binding = null;
    }
}