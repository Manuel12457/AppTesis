package com.example.apptesis.widgets;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InicioSesionViewModel extends ViewModel {
    private MutableLiveData<Boolean> isAuthenticated;
    private MutableLiveData<String> errorMessage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public void AuthViewModel() {
        isAuthenticated = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
    }

    public LiveData<Boolean> getIsAuthenticated() {
        return isAuthenticated;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                String userId = firebaseAuth.getCurrentUser().getUid();
                                databaseReference.orderByChild("id").equalTo(userId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    isAuthenticated.setValue(true);
                                                } else {
                                                    errorMessage.setValue("No se encontraron datos de usuario.");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                errorMessage.setValue(error.getMessage());
                                            }
                                        });
                            } else {
                                errorMessage.setValue("Su cuenta no ha sido verificada. Verifíquela para poder ingresar.");
                            }
                        }
                    });
                } else {
                    errorMessage.setValue(task.getException().getMessage());
                }
            }
        });
    }
}
