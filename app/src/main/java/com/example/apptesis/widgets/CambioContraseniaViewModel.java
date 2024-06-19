package com.example.apptesis.widgets;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Closeable;

public class CambioContraseniaViewModel extends AndroidViewModel {

    private FirebaseAuth firebaseAuth;
    public MutableLiveData<Boolean> successLiveData;
    public MutableLiveData<String> errorLiveData;

    public CambioContraseniaViewModel(@NonNull Application application) {
        super(application);
        firebaseAuth = FirebaseAuth.getInstance();
        successLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public void sendPasswordResetEmail(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> successLiveData.setValue(true))
                .addOnFailureListener(e -> errorLiveData.setValue(e.getMessage()));
    }

}
