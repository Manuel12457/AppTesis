package com.example.apptesis.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.apptesis.R;
import com.example.apptesis.adapters.ListaEventosAdapter;
import com.example.apptesis.clases.Evento;
import com.example.apptesis.databinding.FragmentNavCalendarioBinding;
import com.example.apptesis.sqllite.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

public class nav_calendario extends Fragment {

    private FragmentNavCalendarioBinding binding;
    TextView textoFechaCalendario;
    CalendarView calendarView;
    private RecyclerView eventRecyclerView;
    private ListaEventosAdapter listaEventosAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<Evento> listaEventos;
    String MES[] = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavCalendarioBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), bundle.getString("exito"), Snackbar.LENGTH_LONG).show();
        }

        textoFechaCalendario = view.findViewById(R.id.idTextFechaCalendario);
        calendarView = view.findViewById(R.id.idCalendarView);
        eventRecyclerView = view.findViewById(R.id.idRecyclerViewCalendar);
        databaseHelper = new DatabaseHelper(getContext());

        listaEventos = new ArrayList<>();
        listaEventosAdapter = new ListaEventosAdapter(listaEventos, getContext());
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventRecyclerView.setAdapter(listaEventosAdapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                textoFechaCalendario.setText(dayOfMonth + " " + MES[month]);
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                Log.d("SELECTED DATE", selectedDate);
                loadEventsForDate(selectedDate);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_calendario, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nuevoEvento) {
                    Navigation.findNavController(view).navigate(R.id.nav_eventoEdicion);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void loadEventsForDate(String date) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {
                "evento_id",
                "titulo",
                "duracion",
                "tiempo_aviso",
                "notas",
                "fecha_hora"
        };

        String selection = "fecha_hora LIKE ?";
        String[] selectionArgs = { "%" + date + "%" };

        Cursor cursor = db.query(
                "eventos",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        listaEventos.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("evento_id"));
            String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
            String duracion = cursor.getString(cursor.getColumnIndexOrThrow("duracion"));
            String fecha_hora = cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora"));
            String tiempo_aviso = cursor.getString(cursor.getColumnIndexOrThrow("tiempo_aviso"));
            String notas = cursor.getString(cursor.getColumnIndexOrThrow("notas"));
            Evento event = new Evento(Integer.toString(id), titulo, duracion, fecha_hora, tiempo_aviso, notas);
            listaEventos.add(event);
        }
        cursor.close();

        listaEventosAdapter.updateEventList(listaEventos);
    }

}