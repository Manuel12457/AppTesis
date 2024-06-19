package com.example.apptesis.widgets;

import android.util.Log;

import androidx.annotation.NonNull;
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

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    public RegistroUsuarioViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void registrarUsuario(String nombre, String apellido, String correo, String password, Map<String, String> mapNombreArchivoUri, MutableLiveData<Boolean> registroExitoso, MutableLiveData<String> errorMensaje) {
        firebaseAuth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = firebaseAuth.getCurrentUser().getUid();

                    // Guardar usuario en la base de datos
                    DatabaseReference databaseReference = firebaseDatabase.getReference("usuarios").child(id);
                    Usuario usuario = new Usuario();
                    usuario.setUsuario_id(id);
                    usuario.setNombre(nombre);
                    usuario.setApellido(apellido);
                    usuario.setCorreo(correo);
                    databaseReference.setValue(usuario)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Random rand = new Random();
                                    int randomNum = rand.nextInt((5 - 1) + 1) + 1;
                                    int position = 1;
                                    for (Map.Entry<String, String> entry : mapNombreArchivoUri.entrySet()) {
                                        if (randomNum == position) {
                                            DatabaseReference ref = firebaseDatabase.getReference("usuarios").child(id).child("imagen");
                                            ref.child(entry.getKey()).setValue(entry.getValue())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            // Imagen guardada con éxito
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            errorMensaje.postValue("Error al guardar la imagen: " + e.getMessage());
                                                        }
                                                    });
                                        }
                                        position++;
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    errorMensaje.postValue("Error al guardar el usuario: " + e.getMessage());
                                }
                            });

                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            registroExitoso.postValue(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            errorMensaje.postValue("Error al enviar el correo de verificación: " + e.getMessage());
                        }
                    });

                } else {
                    errorMensaje.postValue("Error en el registro: " + task.getException().getMessage());
                }
            }
        });
    }

}
