package com.example.apptesis.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InicioSesionViewModel extends ViewModel {

    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                        FirebaseDatabase.getInstance().getReference("usuarios")
                                                .orderByChild("usuario_id")
                                                .equalTo(firebaseAuth.getCurrentUser().getUid())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            loginResult.postValue(new LoginResult(true, null));
                                                        } else {
                                                            loginResult.postValue(new LoginResult(false, "Usuario no encontrado en la base de datos."));
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        loginResult.postValue(new LoginResult(false, "Error: " + error.getMessage()));
                                                    }
                                                });
                                    } else {
                                        loginResult.postValue(new LoginResult(false, "Su cuenta no ha sido verificada. Verifíquela para poder ingresar"));
                                    }
                                }
                            });
                        } else {
                            loginResult.postValue(new LoginResult(false, task.getException().getMessage()));
                        }
                    }
                });
    }

    public static class LoginResult {
        public boolean success;
        public String message;

        public LoginResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

}
