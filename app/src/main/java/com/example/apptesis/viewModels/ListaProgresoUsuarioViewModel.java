package com.example.apptesis.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apptesis.clases.ProgresoUsuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaProgresoUsuarioViewModel extends ViewModel {

    private final MutableLiveData<ProgresoResult> progresoResult = new MutableLiveData<>();

    public LiveData<ProgresoResult> getProgresoResult() {
        return progresoResult;
    }

    public void fetchProgresoUsuarios() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReferenceListaProgresoUsuario = FirebaseDatabase.getInstance().getReference("progreso_usuarios");

        databaseReferenceListaProgresoUsuario.orderByChild("usuario_id").equalTo(firebaseAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<ProgresoUsuario> progresoUsuarios = new ArrayList<>();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ProgresoUsuario progresoUsuario = ds.getValue(ProgresoUsuario.class);
                                progresoUsuarios.add(progresoUsuario);
                            }
                            progresoResult.postValue(new ProgresoResult(progresoUsuarios, null));
                        } else {
                            progresoResult.postValue(new ProgresoResult(Collections.emptyList(), "No progress found"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progresoResult.postValue(new ProgresoResult(Collections.emptyList(), error.getMessage()));
                    }
                });
    }

    public static class ProgresoResult {
        public List<ProgresoUsuario> progresoUsuarios;
        public String error;

        public ProgresoResult(List<ProgresoUsuario> progresoUsuarios, String error) {
            this.progresoUsuarios = progresoUsuarios;
            this.error = error;
        }
    }

}
