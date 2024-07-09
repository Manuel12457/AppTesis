package com.example.apptesis.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import com.example.apptesis.Presentacion;
import com.example.apptesis.R;
import com.example.apptesis.RegistroUsuario;
import com.example.apptesis.adapters.ListaCategoriasEventoAdapter;
import com.example.apptesis.adapters.ListaCategoriasEventoDetalleAdapter;
import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.CategoriaSQLite;
import com.example.apptesis.databinding.FragmentNavCalendarioBinding;
import com.example.apptesis.databinding.FragmentNavEventoBinding;
import com.example.apptesis.sqllite.DatabaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class nav_evento extends Fragment {

    private FragmentNavEventoBinding binding;
    TextView tituloEvento;
    TextView fechaEvento;
    TextView horaEvento;
    TextView duracionEvento;
    TextView avisoEvento;
    TextView notasEvento;
    private DatabaseHelper databaseHelper;
    ListaCategoriasEventoDetalleAdapter listaCategoriasEventoDetalleAdapter;
    ArrayList<CategoriaSQLite> listaCategoriasSQL;
    String id_evento;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavEventoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        databaseHelper = new DatabaseHelper(getContext());

        tituloEvento = view.findViewById(R.id.idTituloEventoDetalle);
        fechaEvento = view.findViewById(R.id.idFechaEventoDetalle);
        horaEvento = view.findViewById(R.id.idHoraEventoDetalle);
        duracionEvento = view.findViewById(R.id.idDuracionEventoDetalle);
        avisoEvento = view.findViewById(R.id.idAvisoEventoDetalle);
        notasEvento = view.findViewById(R.id.idNotasEventoDetalle);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id_evento = bundle.getString("id");
            loadEventData(Integer.parseInt(id_evento));
            listaCategoriasSQL = databaseHelper.getCategoriesByEventId(Integer.parseInt(id_evento));
        }

        recyclerView = view.findViewById(R.id.idRecyclerViewEventoEdicion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaCategoriasEventoDetalleAdapter = new ListaCategoriasEventoDetalleAdapter(listaCategoriasSQL, getContext());
        recyclerView.setAdapter(listaCategoriasEventoDetalleAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_evento_detalle, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.editarEvento) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id_evento);
                    Navigation.findNavController(view).navigate(R.id.nav_eventoEdicion, bundle);
                    return true;
                } else if (menuItem.getItemId() == R.id.eliminarEvento) {
                    deleteEventAndCategories(view);
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
            tituloEvento.setText(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));

            String[] splitStr = cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora")).split("\\s+");
            fechaEvento.setText(formatDate(splitStr[0]));
            // FECHA FINAL
            DateTimeFormatter formatter = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("HH:mm");
            }
            LocalTime hora = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                hora = LocalTime.parse(splitStr[1], formatter);
            }

            int minutos = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("duracion")));
            LocalTime nuevaHora = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                nuevaHora = hora.plusMinutes(minutos);
            }
            String nuevaHoraStr = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                nuevaHoraStr = nuevaHora.format(formatter);
            }
            // FECHA FINAL
            horaEvento.setText(splitStr[1] + " - " + nuevaHoraStr);
            duracionEvento.setText(cursor.getString(cursor.getColumnIndexOrThrow("duracion")) + " min");
            avisoEvento.setText(cursor.getString(cursor.getColumnIndexOrThrow("tiempo_aviso")) + " min. antes");
            notasEvento.setText(cursor.getString(cursor.getColumnIndexOrThrow("notas")));
        }
        cursor.close();
    }

    public static String formatDate(String inputDate) {
        // Define the input and output date formats
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE d 'de' MMMM", new Locale("es", "ES"));

        Date date;
        String formattedDate = "";
        try {
            // Parse the input date
            date = inputFormat.parse(inputDate);
            // Format the date to the desired output format
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    private void deleteEventAndCategories(View view) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setMessage("¿Seguro que desea eliminar este evento?");
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int resultEvento = databaseHelper.deleteEvento(Integer.parseInt(id_evento));
                        if (resultEvento > 0) {
                            //Navigation.findNavController(view).navigate(R.id.nav_calendario, bundle);
                            Log.d("SUCCESS", "Event deleted successfully");
                            for (CategoriaSQLite c : listaCategoriasSQL) {
                                int resultCategoria = databaseHelper.deleteCategory(Integer.parseInt(c.getCategoria_id()));
                                if (resultCategoria > 0) {
                                    Log.d("SUCCESS", "Category deleted successfully");
                                } else {
                                    // Update failed
                                    Log.d("FAILURE", "Failed to delete category");
                                }
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("exito", "Se ha eliminado el evento exitosamente");
                            Navigation.findNavController(view).navigate(R.id.nav_calendario, bundle);
                        } else {
                            // Update failed
                            Log.d("FAILURE", "Failed to update event");
                        }
                    }
                });
        builder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();

    }
}