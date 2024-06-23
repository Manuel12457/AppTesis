package com.example.apptesis.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apptesis.clases.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.Random;

public class RegistroUsuarioViewModel extends ViewModel {


    private final MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void registerUser(String email, String password, String nombre, String apellido, Map<String, String> mapNombreArchivoUri) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("task", "EXITO EN REGISTRO");

                            String id = firebaseAuth.getCurrentUser().getUid();

                            // Guardar usuario en db
                            DatabaseReference databaseReference = firebaseDatabase.getReference().child("usuarios").child(id);
                            Usuario usuario = new Usuario();
                            usuario.setUsuario_id(id);
                            usuario.setNombre(nombre);
                            usuario.setApellido(apellido);
                            usuario.setCorreo(email);
                            databaseReference.setValue(usuario)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("registro", "USUARIO GUARDADO");

                                            Random rand = new Random();
                                            int randomNum = rand.nextInt(5) + 1;
                                            int position = 1;
                                            for (Map.Entry<String, String> entry : mapNombreArchivoUri.entrySet()) {
                                                if (randomNum == position) {
                                                    DatabaseReference ref = firebaseDatabase.getReference().child("usuarios").child(id).child("imagen");
                                                    ref.child(entry.getKey()).setValue(entry.getValue())
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Log.d("imagen", "IMAGEN GUARDADA");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d("imagen", "ERROR AL GUARDAR IMAGEN: " + e.getMessage());
                                                                }
                                                            });
                                                }
                                                position++;
                                            }

                                            // Enviar correo de verificación
                                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("task", "EXITO EN ENVIO DE CORREO DE VERIFICACION");
                                                    registerResult.postValue(new RegisterResult(true, "Se ha enviado un correo para la verificación de su cuenta"));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    registerResult.postValue(new RegisterResult(false, "Error: " + e.getMessage()));
                                                }
                                            });

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            registerResult.postValue(new RegisterResult(false, "Error: " + e.getMessage()));
                                        }
                                    });

                        } else {
                            registerResult.postValue(new RegisterResult(false, "Error: " + task.getException().getMessage()));
                        }
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

}
