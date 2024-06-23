package com.example.apptesis.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class CambioContraseniaViewModel extends ViewModel {

    private final MutableLiveData<PasswordResetResult> passwordResetResult = new MutableLiveData<>();

    public LiveData<PasswordResetResult> getPasswordResetResult() {
        return passwordResetResult;
    }

    public void sendPasswordResetEmail(String email) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        passwordResetResult.postValue(new PasswordResetResult(true, "Correo enviado para cambio de contrasenia"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        passwordResetResult.postValue(new PasswordResetResult(false, "Error: " + e.getMessage()));
                    }
                });
    }

    public static class PasswordResetResult {
        public boolean success;
        public String message;

        public PasswordResetResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

}
