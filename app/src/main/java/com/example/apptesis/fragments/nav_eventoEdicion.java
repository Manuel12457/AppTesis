package com.example.apptesis.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.apptesis.R;
import com.example.apptesis.adapters.ListaCategoriasEventoAdapter;
import com.example.apptesis.adapters.ListaCategoriasProgresoAdapter;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.databinding.FragmentNavEventoEdicionBinding;
import com.example.apptesis.databinding.FragmentNavPerfilBinding;
import com.example.apptesis.viewModels.ListaCategoriasViewModel;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class nav_eventoEdicion extends Fragment {

    private FragmentNavEventoEdicionBinding binding;
    private ListaCategoriasViewModel listaCategoriasViewModel;
    private ListaCategoriasEventoAdapter listaCategoriasEventoAdapter;
    private ArrayList<Categoria> listaCategoria = new ArrayList<>();

    boolean tituloValido = true;
    boolean fechaValido = true;
    boolean horaValido = true;
    boolean avisoValido = true;
    boolean cuerpoValido = true;

    int vecesTitulo = 0;
    int vecesFecha = 0;
    int vecesHora = 0;
    int vecesAviso = 0;
    int vecesCuerpo = 0;

    TextInputLayout titulo;
    TextInputLayout fecha;
    TextInputLayout hora;
    TextView tiempo;
    String tiempoSlider;
    Slider slider;
    RecyclerView recyclerView;
    TextInputLayout spinnera;
    AutoCompleteTextView spinner;
    String aviso;
    TextInputLayout cuerpo;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavEventoEdicionBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        titulo = view.findViewById(R.id.inputTitulo_evento);
        titulo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (titulo.isErrorEnabled()) {
                    if ((titulo.getEditText().getText().toString() != null && !titulo.getEditText().getText().toString().equals(""))) {
                        titulo.setErrorEnabled(false);
                        tituloValido = true;
                    } else {
                        titulo.setError("Ingrese un título para el evento");
                        tituloValido = false;
                    }
                }

                if (!titulo.isErrorEnabled() && vecesTitulo != 0) {
                    if ((titulo.getEditText().getText().toString() != null && !titulo.getEditText().getText().toString().equals(""))) {
                        titulo.setErrorEnabled(false);
                        tituloValido = true;
                    } else {
                        titulo.setError("Ingrese un título para el evento");
                        tituloValido = false;
                    }
                }
            }
        });

        fecha = view.findViewById(R.id.inputFecha_evento);
        fecha.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fecha.isErrorEnabled()) {
                    if ((fecha.getEditText().getText().toString() != null && !fecha.getEditText().getText().toString().equals(""))) {
                        fecha.setErrorEnabled(false);
                        fechaValido = true;
                    } else {
                        fecha.setError("Seleccione una fecha de inicio");
                        fechaValido = false;
                    }
                }

                if (!fecha.isErrorEnabled() && vecesFecha != 0) {
                    if ((fecha.getEditText().getText().toString() != null && !fecha.getEditText().getText().toString().equals(""))) {
                        fecha.setErrorEnabled(false);
                        fechaValido = true;
                    } else {
                        fecha.setError("Seleccione una fecha de inicio");
                        fechaValido = false;
                    }
                }
            }
        });

        hora = view.findViewById(R.id.inputHora_evento);
        hora.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (hora.isErrorEnabled()) {
                    if ((hora.getEditText().getText().toString() != null && !hora.getEditText().getText().toString().equals(""))) {
                        hora.setErrorEnabled(false);
                        horaValido = true;
                    } else {
                        fecha.setError("Seleccione una hora de inicio");
                        horaValido = false;
                    }
                }

                if (!hora.isErrorEnabled() && vecesHora != 0) {
                    if ((hora.getEditText().getText().toString() != null && !hora.getEditText().getText().toString().equals(""))) {
                        hora.setErrorEnabled(false);
                        horaValido = true;
                    } else {
                        hora.setError("Seleccione una hora de inicio");
                        horaValido = false;
                    }
                }
            }
        });

        tiempo = view.findViewById(R.id.idTiempoEventoEdicion);
        slider = view.findViewById(R.id.idSlider);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                tiempoSlider = String.valueOf(value);
                tiempo.setText(value + " m");
            }
        });

        //RECYCLER VIEW
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
                }
            }
        });
        listaCategoriasViewModel.fetchCategories();

        recyclerView = view.findViewById(R.id.idRecyclerViewEventoEdicion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaCategoriasEventoAdapter = new ListaCategoriasEventoAdapter(listaCategoria, getContext());
        recyclerView.setAdapter(listaCategoriasEventoAdapter);
        //RECYCLER VIEW

        spinnera = view.findViewById(R.id.menu_aviso);
        spinner = view.findViewById(R.id.idFiltroHistorial);

        String[] listaAviso = {"10 min", "30 min", "60 min"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, listaAviso
        );
        spinner.setAdapter(adapter);

        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                spinnera.setErrorEnabled(false);
                aviso = spinner.getText().toString();
            }
        });

        cuerpo = view.findViewById(R.id.inputCuerpoMensaje);
        cuerpo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (cuerpo.isErrorEnabled()) {
                    if ((cuerpo.getEditText().getText().toString() != null && !cuerpo.getEditText().getText().toString().equals(""))) {
                        if (cuerpo.getEditText().getText().toString().length() > 500) {
                            cuerpo.setError("El cuerpo del mensaje debe ser menor a 500 caracteres");
                            cuerpoValido = false;
                        } else {
                            cuerpo.setErrorEnabled(false);
                            cuerpoValido = true;
                        }
                    }
                }

                if (!cuerpo.isErrorEnabled() && vecesCuerpo != 0) {
                    if ((cuerpo.getEditText().getText().toString() != null && !cuerpo.getEditText().getText().toString().equals(""))) {
                        if (cuerpo.getEditText().getText().toString().length() > 500) {
                            cuerpo.setError("El cuerpo del mensaje debe ser menor a 500 caracteres");
                            cuerpoValido = false;
                        } else {
                            cuerpo.setErrorEnabled(false);
                            cuerpoValido = true;
                        }
                    }
                }
            }
        });

        return view;
    }

    public void validarEvento() {

        titulo = view.findViewById(R.id.inputTitulo_evento);
        fecha = view.findViewById(R.id.inputFecha_evento);
        hora = view.findViewById(R.id.inputHora_evento);
        cuerpo = view.findViewById(R.id.inputCuerpoMensaje);

        tituloValido = true;
        if (titulo.getEditText().getText().toString() != null && !titulo.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            titulo.setErrorEnabled(false);
        } else {
            titulo.setError("Ingrese un título para el evento");
            tituloValido = false;
        }

        fechaValido = true;
        if (fecha.getEditText().getText().toString() != null && !fecha.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            fecha.setErrorEnabled(false);
        } else {
            fecha.setError("Seleccione una fecha de inicio");
            fechaValido = false;
        }

        horaValido = true;
        if (hora.getEditText().getText().toString() != null && !hora.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            hora.setErrorEnabled(false);
        } else {
            hora.setError("Seleccione una hora de inicio");
            horaValido = false;
        }

        cuerpoValido = true;
        if ((cuerpo.getEditText().getText().toString() != null && !cuerpo.getEditText().getText().toString().equals(""))) {
            if (cuerpo.getEditText().getText().toString().length() > 500) {
                cuerpo.setError("El cuerpo del mensaje debe ser menor a 500 caracteres");
                cuerpoValido = false;
            } else {
                cuerpo.setErrorEnabled(false);
                cuerpoValido = true;
            }
        }

        if (tituloValido && fechaValido && horaValido && cuerpoValido) {

            //GUARDAR EN SQLITE

        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_evento_edicion, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.aceptarEditarEvento) {
                    Navigation.findNavController(view).navigate(R.id.nav_calendario);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

}