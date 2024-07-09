package com.example.apptesis.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptesis.R;
import com.example.apptesis.adapters.ListaCategoriasEventoAdapter;
import com.example.apptesis.adapters.ListaCategoriasProgresoAdapter;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.CategoriaSQLite;
import com.example.apptesis.clases.Evento;
import com.example.apptesis.databinding.FragmentNavEventoEdicionBinding;
import com.example.apptesis.databinding.FragmentNavPerfilBinding;
import com.example.apptesis.sqllite.DatabaseHelper;
import com.example.apptesis.viewModels.ListaCategoriasViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

public class nav_eventoEdicion extends Fragment {

    private FragmentNavEventoEdicionBinding binding;
    private ListaCategoriasViewModel listaCategoriasViewModel;
    private ListaCategoriasEventoAdapter listaCategoriasEventoAdapter;
    private ArrayList<Categoria> listaCategoria = new ArrayList<>();

    boolean tituloValido = true;
    boolean fechaValido = true;
    boolean horaValido = true;
    boolean avisoValido = true;

    int vecesTitulo = 0;
    int vecesFecha = 0;
    int vecesHora = 0;
    int vecesAviso = 0;
    int vecesCuerpo = 0;

    TextInputLayout titulo;
    TextInputLayout fecha;
    TextInputLayout hora;
    TextView tiempo;
    String duracion = "10";
    String tiempoSlider;
    Slider slider;
    RecyclerView recyclerView;
    RadioGroup radioGroup;
    RadioButton radiobutton;
    String radiobuttonText = "10";
    TextInputLayout cuerpo;
    CircularProgressIndicator circularProgressIndicator;
    private DatabaseHelper databaseHelper;
    String id_evento;
    ArrayList<CategoriaSQLite> listaCategoriasSQL;
    RadioButton buttonTen;
    RadioButton buttonThirty;
    RadioButton buttonSixty;
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

        listaCategoriasSQL = new ArrayList<>();

        databaseHelper = new DatabaseHelper(getContext());
        circularProgressIndicator = view.findViewById(R.id.idProgressCircular);

        buttonTen = view.findViewById(R.id.radio_ten_min);
        buttonThirty = view.findViewById(R.id.radio_thrity_min);
        buttonSixty = view.findViewById(R.id.radio_sixty_min);

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
        fecha.getEditText().setKeyListener(null);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        calendar.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        calendar.setTimeInMillis(today);

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccione una fecha de inicio")
                .setSelection(today)
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        fecha.getEditText().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                datePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedDate = sdf.format(selection);
                    fecha.getEditText().setText(formattedDate);
                });

                return true;  // Consume the touch event
            }
            return false;
        });
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
        hora.getEditText().setKeyListener(null);
        hora.getEditText().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(currentHour)
                        .setMinute(currentMinute)
                        .setTitleText("Seleccione una hora de inicio")
                        .build();

                timePicker.show(getParentFragmentManager(), "MATERIAL_TIME_PICKER");

                timePicker.addOnPositiveButtonClickListener(dialog -> {
                    int selectedHour = timePicker.getHour();
                    int selectedMinute = timePicker.getMinute();

                    // Check if the selected time is valid (current or future time)
                    if (selectedHour > currentHour || (selectedHour == currentHour && selectedMinute >= currentMinute)) {
                        String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        hora.getEditText().setText(formattedTime);
                    } else {
                        Toast.makeText(getContext(), "Seleccione una hora válida", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;  // Consume the touch event
            }
            return false;
        });
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
                tiempo.setText(String.format("%.0f", value) + " min");
                duracion = String.format("%.0f", value);
            }
        });

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radiobutton = view.findViewById(checkedId);
                radiobuttonText = radiobutton.getText().toString();
                if (radiobuttonText.equals("30 min")) {
                    radiobuttonText = "30";
                } else if (radiobuttonText.equals("60 min")) {
                    radiobuttonText = "60";
                } else {
                    radiobuttonText = "10";
                }
            }
        });

        cuerpo = view.findViewById(R.id.inputCuerpoMensaje);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id_evento = bundle.getString("id");
            loadEventData(Integer.parseInt(id_evento));
            listaCategoriasSQL = databaseHelper.getCategoriesByEventId(Integer.parseInt(id_evento));
        }

        //RECYCLER VIEW
        recyclerView = view.findViewById(R.id.idRecyclerViewEventoEdicion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaCategoriasEventoAdapter = new ListaCategoriasEventoAdapter(listaCategoria, getContext(), listaCategoriasSQL);
        recyclerView.setAdapter(listaCategoriasEventoAdapter);

        // Configurar el ViewModel
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
                    listaCategoriasEventoAdapter.notifyDataSetChanged();

                    circularProgressIndicator.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        // Fetch categories
        listaCategoriasViewModel.fetchCategories();
        //RECYCLER VIEW

        return view;
    }

    public void validarEvento() {

        titulo = view.findViewById(R.id.inputTitulo_evento);
        fecha = view.findViewById(R.id.inputFecha_evento);
        hora = view.findViewById(R.id.inputHora_evento);
        cuerpo = view.findViewById(R.id.inputCuerpoMensaje);

        if (titulo.getEditText().getText().toString() != null && !titulo.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            titulo.setErrorEnabled(false);
        } else {
            titulo.setError("Ingrese un título para el evento");
            tituloValido = false;
        }

        if (fecha.getEditText().getText().toString() != null && !fecha.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            fecha.setErrorEnabled(false);
        } else {
            fecha.setError("Seleccione una fecha de inicio");
            fechaValido = false;
        }

        if (hora.getEditText().getText().toString() != null && !hora.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            hora.setErrorEnabled(false);
        } else {
            hora.setError("Seleccione una hora de inicio");
            horaValido = false;
        }

        Log.d("TITULO", titulo.getEditText().getText().toString());
        Log.d("FECHA", fecha.getEditText().getText().toString());
        Log.d("HORA", hora.getEditText().getText().toString());
        Log.d("DURACION", duracion);
        Log.d("AVISO", radiobuttonText);
        Log.d("CUERPO", cuerpo.getEditText().getText().toString());

        if (tituloValido && fechaValido && horaValido) {

            SparseBooleanArray sparseBooleanArray = listaCategoriasEventoAdapter.getSelectedItems();
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            if (id_evento != null && !id_evento.equals("")) {
                updateEvent();
                for (CategoriaSQLite c : listaCategoriasSQL) {
                    int result = databaseHelper.deleteCategory(Integer.parseInt(c.getCategoria_id()));
                    if (result > 0) {
                        Log.d("EXITO", "Category deleted successfully");

                    } else {
                        Log.d("FALLO", "Failed to delete category");
                    }
                }
                for (int i = 0; i < sparseBooleanArray.size(); i++) {
                    int key = sparseBooleanArray.keyAt(i);
                    int j = 0;
                    for (Categoria c : listaCategoria) {
                        if (j == key) {
                            ContentValues categoriaValues = new ContentValues();
                            categoriaValues.put("categoria", c.getCategoria());
                            categoriaValues.put("descripcion", c.getDescripcion());

                            if (c.getImagen() != null) {
                                for (Map.Entry<String, String> entry : c.getImagen().entrySet()) {
                                    categoriaValues.put("imagen", entry.getValue());
                                }
                            }
                            categoriaValues.put("id_evento", id_evento);

                            long newCategoriaId = db.insert("categorias", null, categoriaValues);

                            if (newCategoriaId != -1) {
                                Bundle bundle = new Bundle();
                                bundle.putString("exito", "Se ha editado el evento exitosamente");
                                Navigation.findNavController(view).navigate(R.id.nav_calendario, bundle);
                                Log.d("Exito", "Evento y categoría guardados");
                            } else {
                                Log.d("Error categoria", "Evento guardado, pero error al guardar categoría");
                            }
                        }
                        j++;
                    }
                }

            } else {

                //GUARDAR EN SQLITE
                ContentValues values = new ContentValues();
                // EVENTO
                values.put("titulo", titulo.getEditText().getText().toString());
                values.put("fecha_hora", fecha.getEditText().getText().toString() + " " + hora.getEditText().getText().toString());
                values.put("duracion", duracion);
                values.put("tiempo_aviso", radiobuttonText);
                values.put("notas", cuerpo.getEditText().getText().toString());

                long newEventId  = db.insert("eventos", null, values);

                if (newEventId != -1) {

                    for (int i = 0; i < sparseBooleanArray.size(); i++) {
                        int key = sparseBooleanArray.keyAt(i);
                        int j = 0;
                        for (Categoria c : listaCategoria) {
                            if (j == key) {
                                ContentValues categoriaValues = new ContentValues();
                                categoriaValues.put("categoria", c.getCategoria());
                                categoriaValues.put("descripcion", c.getDescripcion());

                                if (c.getImagen() != null) {
                                    for (Map.Entry<String, String> entry : c.getImagen().entrySet()) {
                                        categoriaValues.put("imagen", entry.getValue());
                                    }
                                }
                                categoriaValues.put("id_evento", newEventId);

                                long newCategoriaId = db.insert("categorias", null, categoriaValues);

                                if (newCategoriaId != -1) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("exito", "Se ha creado el evento exitosamente");
                                    Navigation.findNavController(view).navigate(R.id.nav_calendario, bundle);
                                    Log.d("Exito", "Evento y categoría guardados");
                                } else {
                                    Log.d("Error categoria", "Evento guardado, pero error al guardar categoría");
                                }
                            }
                            j++;
                        }
                    }

                } else {
                    Log.d("Error evento", "Error al guardar evento");
                }
            }

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
                    validarEvento();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void loadEventData(int eventId) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM eventos WHERE evento_id = ?",
                new String[]{String.valueOf(eventId)}
        );

        if (cursor.moveToFirst()) {
            titulo.getEditText().setText(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));

            String[] splitStr = cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora")).split("\\s+");
            fecha.getEditText().setText(splitStr[0]);
            hora.getEditText().setText(splitStr[1]);

            tiempo.setText(cursor.getString(cursor.getColumnIndexOrThrow("duracion")));
            slider.setValue(Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow("duracion"))));

            String ta = cursor.getString(cursor.getColumnIndexOrThrow("tiempo_aviso"));
            if (ta.equals("10")) {
                buttonTen.toggle();
            } else if (ta.equals("30")) {
                buttonThirty.toggle();
            } else if (ta.equals("60")) {
                buttonSixty.toggle();
            }

            cuerpo.getEditText().setText(cursor.getString(cursor.getColumnIndexOrThrow("notas")));

        }
        cursor.close();
    }

    private void updateEvent() {

        int result = databaseHelper.updateEvent(Integer.parseInt(id_evento), titulo.getEditText().getText().toString(), fecha.getEditText().getText().toString() + " " + hora.getEditText().getText().toString(), duracion,radiobuttonText, cuerpo.getEditText().getText().toString());
        if (result > 0) {
            //Navigation.findNavController(view).navigate(R.id.nav_calendario, bundle);
            Log.d("SUCCESS", "Event updated successfully");
        } else {
            // Update failed
            Log.d("FAILURE", "Failed to update event");
        }
    }

}