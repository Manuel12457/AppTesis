package com.example.apptesis.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apptesis.R;
import com.example.apptesis.clases.Leccion;
import com.example.apptesis.clases.ProgresoUsuario;
import com.example.apptesis.clases.Usuario;
import com.example.apptesis.databinding.FragmentNavPracticaLeccionDetalleBinding;
import com.example.apptesis.viewModels.ListaLeccionesViewModel;
import com.example.apptesis.viewModels.ListaProgresoUsuarioViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class nav_practica_leccion_detalle extends Fragment {

    ListaLeccionesViewModel listaLeccionesViewModel;
    private FragmentNavPracticaLeccionDetalleBinding binding;
    String leccion_id;
    String categoria_id;
    boolean aprendido;
    LinearProgressIndicator linearProgressIndicator;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavPracticaLeccionDetalleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            leccion_id = bundle.getString("leccion_id");
            categoria_id = bundle.getString("categoria_id");
            aprendido = Boolean.parseBoolean(bundle.getString("aprendido"));
        }
        Log.d("aprendido", String.valueOf(aprendido));

        TextView navTituloLeccion = view.findViewById(R.id.idTitulo);
        ImageView navImagenUsuario = view.findViewById(R.id.idImagenLeccion);
        TextView navDescripcionLeccion = view.findViewById(R.id.idDescripcionLeccion);
        FloatingActionButton navCompletado = view.findViewById(R.id.floatingActionButton);
        TextView navCompletadoLeccion = view.findViewById(R.id.idCompletadoLeccion);
        linearProgressIndicator = view.findViewById(R.id.linearProgressIndicator);
        linearLayout = view.findViewById(R.id.linearLayoutDetalleLeccion);

        listaLeccionesViewModel = new ViewModelProvider(this).get(ListaLeccionesViewModel.class);
        // Observando cambios en las lecciones
        listaLeccionesViewModel.getLeccionesResult().observe(getViewLifecycleOwner(), new Observer<ListaLeccionesViewModel.LeccionesResult>() {
            @Override
            public void onChanged(ListaLeccionesViewModel.LeccionesResult leccionesResult) {
                if (leccionesResult.error != null) {
                    // Manejar error aquí
                    Log.e("FirebaseError", leccionesResult.error);
                    Snackbar.make(view, leccionesResult.error, Snackbar.LENGTH_LONG).show();
                } else if (leccionesResult.lecciones != null && !leccionesResult.lecciones.isEmpty()) {
                    for (Leccion l : leccionesResult.lecciones) {
                        if (l.getLeccion_id().equals(leccion_id)) {
                            navTituloLeccion.setText(l.getTitulo());
                            navDescripcionLeccion.setText(l.getDescripcion());
                            for (Map.Entry<String, String> entry : l.getImagen().entrySet()) {
                                Glide.with(getActivity())
                                        .load(entry.getValue())
                                        .into(navImagenUsuario);
                            }

                        }
                    }
                    linearProgressIndicator.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        // Fetch lessons data
        listaLeccionesViewModel.fetchLecciones(categoria_id);

        if (aprendido) {
            navCompletado.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_completed)));
            navCompletado.setImageResource(R.drawable.baseline_check_white_24);
            navCompletadoLeccion.setText(R.string.practica_aprendida);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button fab = view.findViewById(R.id.idProbarGestoButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_practica_practica);
            }
        });
    }
}