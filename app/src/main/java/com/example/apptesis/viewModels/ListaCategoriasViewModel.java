package com.example.apptesis.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apptesis.clases.Categoria;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaCategoriasViewModel extends ViewModel {

    private final MutableLiveData<CategoryResult> categoryResult = new MutableLiveData<>();

    public LiveData<CategoryResult> getCategoryResult() {
        return categoryResult;
    }

    public void fetchCategories() {
        DatabaseReference databaseReferenceListaCategorias = FirebaseDatabase.getInstance().getReference("categorias");

        databaseReferenceListaCategorias.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Categoria> categorias = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Categoria categoria = ds.getValue(Categoria.class);
                        categorias.add(categoria);
                    }
                    categoryResult.postValue(new CategoryResult(categorias, null));
                } else {
                    Log.d("ERROR categorias", "SNAPSHOT NOT FOUND");
                    categoryResult.postValue(new CategoryResult(Collections.emptyList(), "No categories found"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoryResult.postValue(new CategoryResult(Collections.emptyList(), error.getMessage()));
            }
        });
    }

    public static class CategoryResult {
        public List<Categoria> categorias;
        public String error;

        public CategoryResult(List<Categoria> categorias, String error) {
            this.categorias = categorias;
            this.error = error;
        }
    }

}
