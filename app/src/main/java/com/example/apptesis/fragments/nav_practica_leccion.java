package com.example.apptesis.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptesis.R;
import com.example.apptesis.adapters.ListaCategoriasAdapter;
import com.example.apptesis.adapters.ListaLeccionesAdapter;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.Leccion;
import com.example.apptesis.clases.ProgresoUsuario;
import com.example.apptesis.databinding.FragmentNavPracticaBinding;
import com.example.apptesis.databinding.FragmentNavPracticaLeccionBinding;
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

    Query query;
    DatabaseReference databaseReference;
    ListaLeccionesAdapter adapter;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    ArrayList<Leccion> listaLecciones = new ArrayList<>();;
    ArrayList<ProgresoUsuario> listaProgreso = new ArrayList<>();;
    String categoria_id;
    private FragmentNavPracticaLeccionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavPracticaLeccionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.idRecyclerLecciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle bundle = this.getArguments();
        categoria_id = bundle.getString("id");

        listaLecciones = new ArrayList<>();
        adapter = new ListaLeccionesAdapter(listaLecciones, getContext(), listaProgreso, categoria_id);
        recyclerView.setAdapter(adapter);

        query = FirebaseDatabase.getInstance().getReference("categorias").child(categoria_id).child("lecciones").orderByChild("titulo");
        valueEventListener = query.addValueEventListener(new nav_practica_leccion.listener());

        query = FirebaseDatabase.getInstance().getReference("progreso_usuarios").orderByChild("usuario_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        valueEventListener = query.addValueEventListener(new nav_practica_leccion.listenerProgreso());

        return view;
    }

    class listener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.d("SNAPSHOT EXISTS", "SNAPTHOS EXISTS: " + snapshot.exists());
            if (snapshot.exists()) { //Nodo referente existe
                listaLecciones.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Leccion leccion = ds.getValue(Leccion.class);
                    listaLecciones.add(leccion);
                }
                adapter.notifyDataSetChanged();
            } else {
                listaLecciones.clear();
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