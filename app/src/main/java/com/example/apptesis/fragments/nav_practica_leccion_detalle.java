package com.example.apptesis.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apptesis.MainActivity;
import com.example.apptesis.R;
import com.example.apptesis.adapters.ListaCategoriasAdapter;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.Leccion;
import com.example.apptesis.clases.ProgresoUsuario;
import com.example.apptesis.clases.Usuario;
import com.example.apptesis.databinding.FragmentNavPracticaBinding;
import com.example.apptesis.databinding.FragmentNavPracticaLeccionDetalleBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class nav_practica_leccion_detalle extends Fragment {

    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    ArrayList<Leccion> listaLecciones = new ArrayList<>();
    ArrayList<ProgresoUsuario> listaProgreso = new ArrayList<>();
    private FragmentNavPracticaLeccionDetalleBinding binding;
    String leccion_id;
    String categoria_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavPracticaLeccionDetalleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = this.getArguments();
        leccion_id = bundle.getString("leccion_id");
        categoria_id = bundle.getString("categoria_id");

        TextView navTituloLeccion = view.findViewById(R.id.idTitulo);
        ImageView navImagenUsuario = view.findViewById(R.id.idImagenLeccion);
        TextView navDescripcionLeccion = view.findViewById(R.id.idDescripcionLeccion);
        FloatingActionButton navCompletado = view.findViewById(R.id.floatingActionButton);
        TextView navCompletadoLeccion = view.findViewById(R.id.idCompletadoLeccion);

        FirebaseDatabase.getInstance().getReference("categorias").child(categoria_id).child("lecciones").orderByChild("leccion_id").equalTo(leccion_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Leccion leccion = ds.getValue(Leccion.class);
                                navTituloLeccion.setText(leccion.getTitulo());
                                navDescripcionLeccion.setText(leccion.getDescripcion());
                                for (Map.Entry<String, String> entry : leccion.getImagen().entrySet()) {
                                    Glide.with(getActivity().findViewById(android.R.id.content))
                                            .load(entry.getValue())
                                            .into(navImagenUsuario);
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("progreso_usuarios").orderByChild("usuario_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ProgresoUsuario progresoUsuario = ds.getValue(ProgresoUsuario.class);
                                navCompletado.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_completed)));
                                navCompletado.setImageResource(R.drawable.baseline_check_white_24);
                                navCompletadoLeccion.setText(R.string.practica_aprendida);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return view;
    }

}