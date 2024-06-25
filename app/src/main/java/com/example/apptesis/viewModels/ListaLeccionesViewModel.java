package com.example.apptesis.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apptesis.clases.Categoria;
import com.example.apptesis.clases.Leccion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaLeccionesViewModel extends ViewModel {

    private final MutableLiveData<LeccionesResult> leccionesResult = new MutableLiveData<>();

    public LiveData<LeccionesResult> getLeccionesResult() {
        return leccionesResult;
    }

    public void fetchLecciones(String categoriaId) {
        Query query = FirebaseDatabase.getInstance().getReference("categorias")
                .child(categoriaId)
                .child("lecciones")
                .orderByChild("titulo");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Leccion> lecciones = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Leccion leccion = ds.getValue(Leccion.class);
                        lecciones.add(leccion);
                    }
                    leccionesResult.postValue(new LeccionesResult(lecciones, null));
                } else {
                    leccionesResult.postValue(new LeccionesResult(Collections.emptyList(), "No lessons found"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                leccionesResult.postValue(new LeccionesResult(Collections.emptyList(), error.getMessage()));
            }
        });
    }

    public static class LeccionesResult {
        public List<Leccion> lecciones;
        public String error;

        public LeccionesResult(List<Leccion> lecciones, String error) {
            this.lecciones = lecciones;
            this.error = error;
        }
    }

}
