package com.example.apptesis.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apptesis.R;
import com.example.apptesis.adapters.ListaCategoriasProgresoAdapter;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.Leccion;
import com.example.apptesis.clases.ProgresoUsuario;
import com.example.apptesis.clases.Usuario;
import com.example.apptesis.databinding.FragmentNavPerfilBinding;
import com.example.apptesis.viewModels.ListaCategoriasViewModel;
import com.example.apptesis.viewModels.ListaProgresoUsuarioViewModel;
import com.example.apptesis.viewModels.UsuarioPerfilViewModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class nav_perfil extends Fragment {

    private UsuarioPerfilViewModel usuarioPerfilViewModel;
    private ListaCategoriasViewModel listaCategoriasViewModel;
    private ListaProgresoUsuarioViewModel listaProgresoUsuarioViewModel;
    private ArrayList<ProgresoUsuario> listaProgreso = new ArrayList<>();
    private ArrayList<Categoria> listaCategoria = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListaCategoriasProgresoAdapter listaCategoriasProgresoAdapter;
    private FragmentNavPerfilBinding binding;
    private TextView myTextView_nombre_completo;
    private TextView myTextView_correo;
    private TextView myTextView_frase;
    LinearProgressIndicator linearProgressIndicator;
    ScrollView scrollView;

    private BroadcastReceiver uiModeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CONFIGURATION_CHANGED)) {
                int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        myTextView_nombre_completo.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                        myTextView_correo.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                        myTextView_frase.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        myTextView_nombre_completo.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
                        myTextView_correo.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
                        myTextView_frase.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
                        break;
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNavPerfilBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        myTextView_nombre_completo = view.findViewById(R.id.textView_perfil_nombre_completo);
        myTextView_correo = view.findViewById(R.id.textView_perfil_correo);
        myTextView_frase = view.findViewById(R.id.textView_perfil_frase);
        linearProgressIndicator = view.findViewById(R.id.linearProgressIndicator);
        scrollView = view.findViewById(R.id.scrollviewPerfil);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        // Verifica si las vistas son nulas
        if (myTextView_nombre_completo != null || myTextView_correo != null || myTextView_frase != null) {
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    myTextView_nombre_completo.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    myTextView_correo.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    myTextView_frase.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    myTextView_nombre_completo.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    myTextView_correo.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    myTextView_frase.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    break;
            }
        };

        TextView navNombreCompletoUsuario = view.findViewById(R.id.textView_perfil_nombre_completo);
        TextView navCorreoUsuario = view.findViewById(R.id.textView_perfil_correo);
        ImageView navImagenUsuario = view.findViewById(R.id.imageViewUsuario);

        usuarioPerfilViewModel = new ViewModelProvider(this).get(UsuarioPerfilViewModel.class);
        usuarioPerfilViewModel.getUserResult().observe(getViewLifecycleOwner(), new Observer<UsuarioPerfilViewModel.UserResult>() {
            @Override
            public void onChanged(UsuarioPerfilViewModel.UserResult userResult) {
                if (userResult.error != null) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), userResult.error, Snackbar.LENGTH_LONG).show();
                } else if (userResult.usuarios != null && !userResult.usuarios.isEmpty()) {
                    Usuario usuario = userResult.usuarios.get(0);
                    navNombreCompletoUsuario.setText(usuario.getNombre() + " " + usuario.getApellido());
                    navCorreoUsuario.setText(usuario.getCorreo());
                    for (Map.Entry<String, String> entry : usuario.getImagen().entrySet()) {
                        Glide.with(getActivity().findViewById(android.R.id.content))
                                .load(entry.getValue())
                                .into(navImagenUsuario);
                    }

                    linearProgressIndicator.setVisibility(View.INVISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        });
        usuarioPerfilViewModel.fetchUserData();

        listaCategoriasViewModel = new ViewModelProvider(this).get(ListaCategoriasViewModel.class);
        listaCategoriasViewModel.getCategoryResult().observe(getViewLifecycleOwner(), new Observer<ListaCategoriasViewModel.CategoryResult>() {
            @Override
            public void onChanged(ListaCategoriasViewModel.CategoryResult categoryResult) {
                if (categoryResult.error != null) {
                    Log.d("ESTADO listaCategoria", categoryResult.error);
                    Snackbar.make(view, categoryResult.error, Snackbar.LENGTH_LONG).show();
                } else {
                    listaCategoria.clear();
                    listaCategoria.addAll(categoryResult.categorias);
                    listaCategoriasProgresoAdapter.notifyDataSetChanged(); // Actualiza el RecyclerView
                }
            }
        });
        listaCategoriasViewModel.fetchCategories();

        listaProgresoUsuarioViewModel = new ViewModelProvider(this).get(ListaProgresoUsuarioViewModel.class);
        listaProgresoUsuarioViewModel.getProgresoResult().observe(getViewLifecycleOwner(), new Observer<ListaProgresoUsuarioViewModel.ProgresoResult>() {
            @Override
            public void onChanged(ListaProgresoUsuarioViewModel.ProgresoResult progresoResult) {
                if (progresoResult.error != null) {
                    Log.d("ESTADO listaProgreso", progresoResult.error);
                    Snackbar.make(view, progresoResult.error, Snackbar.LENGTH_LONG).show();
                } else {
                    listaProgreso.clear();
                    listaProgreso.addAll(progresoResult.progresoUsuarios);
                    listaCategoriasProgresoAdapter.notifyDataSetChanged(); // Actualiza el RecyclerView
                }
            }
        });
        listaProgresoUsuarioViewModel.fetchProgresoUsuarios();

        recyclerView = view.findViewById(R.id.idRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaCategoriasProgresoAdapter = new ListaCategoriasProgresoAdapter(getContext(), listaCategoria, listaProgreso);
        recyclerView.setAdapter(listaCategoriasProgresoAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        requireContext().registerReceiver(uiModeChangedReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireContext().unregisterReceiver(uiModeChangedReceiver);
    }

}