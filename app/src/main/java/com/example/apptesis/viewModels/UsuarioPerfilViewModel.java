package com.example.apptesis.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apptesis.clases.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsuarioPerfilViewModel extends ViewModel {

    private final MutableLiveData<UserResult> userResult = new MutableLiveData<>();

    public LiveData<UserResult> getUserResult() {
        return userResult;
    }

    public void fetchUserData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReferenceUsuarioPerfil = FirebaseDatabase.getInstance().getReference("usuarios");

        databaseReferenceUsuarioPerfil.orderByChild("usuario_id").equalTo(firebaseAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<Usuario> usuarios = new ArrayList<>();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Usuario usuario = ds.getValue(Usuario.class);
                                usuarios.add(usuario);
                            }
                            userResult.postValue(new UserResult(usuarios, null));
                        } else {
                            userResult.postValue(new UserResult(null, "No user found"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        userResult.postValue(new UserResult(null, error.getMessage()));
                    }
                });
    }

    public static class UserResult {
        public List<Usuario> usuarios;
        public String error;

        public UserResult(List<Usuario> usuarios, String error) {
            this.usuarios = usuarios;
            this.error = error;
        }
    }

}
