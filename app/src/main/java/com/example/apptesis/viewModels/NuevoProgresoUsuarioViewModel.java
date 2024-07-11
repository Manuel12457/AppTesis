package com.example.apptesis.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apptesis.clases.ProgresoUsuario;
import com.example.apptesis.clases.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.Random;

public class NuevoProgresoUsuarioViewModel extends ViewModel {

    private final MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void registerUserProgress(String categoria_id, String leccion_id, String fecha_hora, String porcentaje) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        String id = firebaseAuth.getCurrentUser().getUid();
        String idgenerado = "progreso" + generarCodigo(20);
        // Guardar usuario en db
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("progreso_usuarios");
        ProgresoUsuario progresoUsuario = new ProgresoUsuario();
        progresoUsuario.setProgreso_usuario_id(idgenerado);
        progresoUsuario.setUsuario_id(id);
        progresoUsuario.setCategoria_id(categoria_id);
        progresoUsuario.setPorcentaje(porcentaje);
        progresoUsuario.setFecha_hora(fecha_hora);
        progresoUsuario.setLeccion_id(leccion_id);
        databaseReference.child(idgenerado).setValue(progresoUsuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("registro", "PROGRESO GUARDADO");
                        registerResult.setValue(new RegisterResult(true, "Progreso guardado exitosamente"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("no registro", "PROGRESO NO GUARDADO");
                        registerResult.setValue(new RegisterResult(false, "Fallo al guardar el progreso"));
                    }
                });
    }

    public static class RegisterResult {
        public boolean success;
        public String message;

        public RegisterResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    public String generarCodigo(int longitud) {
        String AlphaNumericStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder s = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            int ch = (int) (AlphaNumericStr.length() * Math.random());
            s.append(AlphaNumericStr.charAt(ch));
        }
        return s.toString();
    }
}
