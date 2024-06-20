package com.example.apptesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.apptesis.clases.Usuario;
import com.example.apptesis.internet.NetworkChangeReceiver;
import com.example.apptesis.internet.NetworkViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class RegistroUsuario extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    ArrayList<String> listaCorreosRegistrados = new ArrayList<>();

    String id;
    HashMap<String, String> mapNombreArchivoUri = new HashMap<>();

    boolean nombreValido = true;
    boolean apellidoValido = true;
    boolean correoValido = true;
    boolean passwordValido = true;
    boolean verifyPasswordValido = true;

    int vecesNombre = 0;
    int vecesApellido = 0;
    int vecesCorreo = 0;
    int vecesPassword = 0;

    private AlertDialog noConnectionDialog;
    private NetworkViewModel networkViewModel;
    private NetworkChangeReceiver networkChangeReceiver;

    LinearProgressIndicator linearProgressIndicator;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        getSupportActionBar().setTitle("Registro de usuario");

        linearProgressIndicator = findViewById(R.id.linearProgressIndicator);
        registerButton = findViewById(R.id.btn_registrarseForm);

        mapNombreArchivoUri.put("321d159e-4d0a-48bd-ae4f-2b457cebae85", "https://firebasestorage.googleapis.com/v0/b/tesis-b2e20.appspot.com/o/img%2Fusuarios%2F321d159e-4d0a-48bd-ae4f-2b457cebae85.jpg?alt=media&token=427b5a76-df46-4fab-aeb7-7eaa2b3f6997");
        mapNombreArchivoUri.put("8616a378-b035-4014-a9ba-1e509f7d541f", "https://firebasestorage.googleapis.com/v0/b/tesis-b2e20.appspot.com/o/img%2Fusuarios%2F8616a378-b035-4014-a9ba-1e509f7d541f.jpg?alt=media&token=56e92831-970d-4cb1-a6f4-3029c3bb4796");
        mapNombreArchivoUri.put("a1755a8c-7149-4f8e-afd6-5dd042a9e977", "https://firebasestorage.googleapis.com/v0/b/tesis-b2e20.appspot.com/o/img%2Fusuarios%2Fa1755a8c-7149-4f8e-afd6-5dd042a9e977.jpg?alt=media&token=47632aec-3e2c-478c-a55e-3e3352535993");
        mapNombreArchivoUri.put("ab16435a-2d2e-4ce3-94f2-709e5af8da66", "https://firebasestorage.googleapis.com/v0/b/tesis-b2e20.appspot.com/o/img%2Fusuarios%2Fab16435a-2d2e-4ce3-94f2-709e5af8da66.jpg?alt=media&token=eb6ba5cd-778b-4fe2-aa49-9a7b44fe817f");
        mapNombreArchivoUri.put("df673c38-12f2-4c57-ba77-825cc8161445", "https://firebasestorage.googleapis.com/v0/b/tesis-b2e20.appspot.com/o/img%2Fusuarios%2Fdf673c38-12f2-4c57-ba77-825cc8161445.jpg?alt=media&token=e0ab6cae-f30a-4090-ac89-1efbf78b884c");

        // Diálogo de alerta: Verificación de internet
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sin conexión a Internet")
                .setMessage("Por favor, verifica tu conexión a Internet para poder continuar")
                .setIcon(R.drawable.baseline_signal_wifi_off_24)
                .setCancelable(false);
        noConnectionDialog = builder.create();

        networkViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(NetworkViewModel.class);

        networkViewModel.getNetworkState().observe(this, isConnected -> {
            if (isConnected) {
                if (noConnectionDialog.isShowing()) {
                    noConnectionDialog.dismiss();
                }
            } else {
                if (!noConnectionDialog.isShowing()) {
                    noConnectionDialog.show();
                }
            }
        });
        networkChangeReceiver = new NetworkChangeReceiver(isConnected -> {
            networkViewModel.setNetworkState(isConnected);
        });

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
        // Diálogo de alerta: Verificación de internet


        firebaseAuth = firebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("usuarios");
        valueEventListener = databaseReference.addValueEventListener(new listener());

        TextInputLayout nombre = findViewById(R.id.inputNombre_registro);
        nombre.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (nombre.isErrorEnabled()) {
                    if ((nombre.getEditText().getText().toString() != null && !nombre.getEditText().getText().toString().equals(""))) {
                        nombre.setErrorEnabled(false);
                        nombreValido = true;
                    } else {
                        nombre.setError("Ingrese su nombre");
                        nombreValido = false;
                    }
                }

                if (!nombre.isErrorEnabled() && vecesNombre != 0) {
                    if ((nombre.getEditText().getText().toString() != null && !nombre.getEditText().getText().toString().equals(""))) {
                        nombre.setErrorEnabled(false);
                        nombreValido = true;
                    } else {
                        nombre.setError("Ingrese su nombre");
                        nombreValido = false;
                    }
                }
            }
        });

        TextInputLayout apellido = findViewById(R.id.inputApellido_registro);
        apellido.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (apellido.isErrorEnabled()) {
                    if ((apellido.getEditText().getText().toString() != null && !apellido.getEditText().getText().toString().equals(""))) {
                        apellido.setErrorEnabled(false);
                        apellidoValido = true;
                    } else {
                        apellido.setError("Ingrese su apellido");
                        apellidoValido = false;
                    }
                }

                if (!apellido.isErrorEnabled() && vecesApellido != 0) {
                    if ((apellido.getEditText().getText().toString() != null && !apellido.getEditText().getText().toString().equals(""))) {
                        apellido.setErrorEnabled(false);
                        apellidoValido = true;
                    } else {
                        apellido.setError("Ingrese su apellido");
                        apellidoValido = false;
                    }
                }
            }
        });

        TextInputLayout correo = findViewById(R.id.inputCorreo_registro);
        correo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (correo.isErrorEnabled()) {
                    if ((correo.getEditText().getText().toString() != null && !correo.getEditText().getText().toString().equals(""))) {
                        correo.setErrorEnabled(false);
                        correoValido = true;
                    } else {
                        correo.setError("Ingrese su correo");
                        correoValido = false;
                    }
                }

                if (!correo.isErrorEnabled() && vecesCorreo != 0) {
                    if ((correo.getEditText().getText().toString() != null && !correo.getEditText().getText().toString().equals(""))) {
                        correo.setErrorEnabled(false);
                        correoValido = true;
                    } else {
                        correo.setError("Ingrese su correo");
                        correoValido = false;
                    }
                }
            }
        });

        TextInputLayout password = findViewById(R.id.inputPassword_registro);
        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                boolean longitudContrasenia = password.getEditText().getText().toString().length() >= 10;
                boolean patronNumerico = Pattern.compile("[0-9]").matcher(password.getEditText().getText().toString()).find();
                boolean patronCaracteresEspeciales = Pattern.compile("[^a-zA-Z0-9]").matcher(password.getEditText().getText().toString()).find();
                if (password.isErrorEnabled()) {
                    if ((password.getEditText().getText().toString() != null && !password.getEditText().getText().toString().equals(""))) {
                        if (!longitudContrasenia) {
                            password.setError("Mínimo 10 caracteres de longitud");
                            passwordValido = false;
                        } else if (!patronNumerico || !patronCaracteresEspeciales) {
                            password.setError("Mínimo un número y caracter especial");
                            passwordValido = false;
                        } else {
                            password.setErrorEnabled(false);
                            passwordValido = true;
                        }
                    } else {
                        password.setError("Ingrese su contraseña");
                        passwordValido = false;
                    }
                }

                if (!password.isErrorEnabled() && vecesPassword != 0) {
                    if ((password.getEditText().getText().toString() != null && !password.getEditText().getText().toString().equals(""))) {
                        password.setErrorEnabled(false);
                        passwordValido = true;
                    } else {
                        password.setError("Ingrese su contraseña");
                        passwordValido = false;
                    }
                }
            }
        });

        TextInputLayout verifyPassword = findViewById(R.id.inputVerifyPassword_registro);
        verifyPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence pass, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable verifyPass) {
                //Log.d("msg", "verifyPassword: " + verifyPass.toString() + " | password: " + password.getEditText().getText().toString());
                //Log.d("msg", String.valueOf(verifyPass.toString().equals(password.getEditText().getText().toString())));
                if (verifyPass.toString().equals(password.getEditText().getText().toString())) {
                    //verifyPassword es identica a password
                    //Log.d("msg", "COINCIDENCIA");
                    verifyPassword.setErrorEnabled(false);
                    verifyPasswordValido = true;
                } else {
                    //verifyPassword NO es identica a password
                    verifyPassword.setError("Las contraseñas no coinciden");
                    verifyPasswordValido = false;
                }
            }
        });
    }

    public void validarRegistro(View view) {

        linearProgressIndicator.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        TextInputLayout nombre = findViewById(R.id.inputNombre_registro);
        TextInputLayout apellido = findViewById(R.id.inputApellido_registro);
        TextInputLayout correo = findViewById(R.id.inputCorreo_registro);
        TextInputLayout password = findViewById(R.id.inputPassword_registro);
        TextInputLayout verifyPassword = findViewById(R.id.inputVerifyPassword_registro);

        boolean nombreValido = true;
        if (nombre.getEditText().getText().toString() != null && !nombre.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            nombre.setErrorEnabled(false);
        } else {
            nombre.setError("Ingrese su nombre");
            nombreValido = false;
        }

        boolean apellidoValido = true;
        if (apellido.getEditText().getText().toString() != null && !apellido.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            apellido.setErrorEnabled(false);
        } else {
            apellido.setError("Ingrese su apellido");
            apellidoValido = false;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
        boolean correoValido = true;
        if (correo.getEditText().getText().toString() != null && !correo.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            if (!correo.getEditText().getText().toString().matches(emailPattern)) {
                //Texto ingresado NO cumple con el patron de un correo electronico
                correo.setError("Ingrese un correo válido");
                correoValido = false;
            } else {
                //Validar si usuario existe en el sistema
                correo.setErrorEnabled(false);
            }
        } else {
            //Texto NO ha sido ingresado en el edittext
            correo.setError("Ingrese un correo");
            correoValido = false;
        }

        boolean passwordValido = true;
        if (password.getEditText().getText().toString() != null && !password.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            vecesPassword++;
            password.setErrorEnabled(false);
        } else {
            vecesPassword++;
            password.setError("Ingrese una contraseña");
            passwordValido = false;
        }

        if (verifyPassword.getEditText().getText().toString() != null && !verifyPassword.getEditText().getText().toString().equals("")) {
            //Texto ha sido ingresado en el edittext
            verifyPassword.setErrorEnabled(false);
        } else {
            verifyPassword.setError("Debe verificar su contraseña");
            verifyPasswordValido = false;
        }

        if (nombreValido && apellidoValido && correoValido && passwordValido && verifyPasswordValido) {

            firebaseAuth.createUserWithEmailAndPassword(correo.getEditText().getText().toString(), password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("task", "EXITO EN REGISTRO");

                        id = firebaseAuth.getCurrentUser().getUid();

                        //Guardar usuario en db
                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("usuarios").child(firebaseAuth.getCurrentUser().getUid());
                        Usuario usuario = new Usuario();
                        usuario.setUsuario_id(id);
                        usuario.setNombre(nombre.getEditText().getText().toString());
                        usuario.setApellido(apellido.getEditText().getText().toString());
                        usuario.setCorreo(correo.getEditText().getText().toString());
                        databaseReference.setValue(usuario)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("registro", "USUARIO GUARDADO");

                                        Random rand = new Random();
                                        int randomNum = rand.nextInt((4 - 1) + 1) + 1;
                                        int position = 1;
                                        for (Map.Entry<String, String> entry : mapNombreArchivoUri.entrySet()) {
                                            if (randomNum == position) {
                                                DatabaseReference ref = firebaseDatabase.getReference().child("usuarios").child(id).child("imagen");
                                                ref.child(entry.getKey()).setValue(entry.getValue())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

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
                                        linearProgressIndicator.setVisibility(View.INVISIBLE);
                                        registerButton.setEnabled(true);
                                        Snackbar.make(findViewById(R.id.activity_registro_usuario), "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                });

                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("task", "EXITO EN ENVIO DE CORREO DE VERIFICACION");
                                Intent intent = new Intent(RegistroUsuario.this, Presentacion.class);
                                intent.putExtra("exito", "Se ha enviado un correo para la verificación de su cuenta");
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                linearProgressIndicator.setVisibility(View.INVISIBLE);
                                registerButton.setEnabled(true);
                                Snackbar.make(findViewById(R.id.activity_registro_usuario), "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        linearProgressIndicator.setVisibility(View.INVISIBLE);
                        registerButton.setEnabled(true);
                        Snackbar.make(findViewById(R.id.activity_registro_usuario), "Error: " + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            linearProgressIndicator.setVisibility(View.INVISIBLE);
            registerButton.setEnabled(true);
        }
    }

    class listener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                listaCorreosRegistrados.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Usuario usuario = ds.getValue(Usuario.class);
                    listaCorreosRegistrados.add(usuario.getCorreo());
                }
            } else {
                listaCorreosRegistrados.clear();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("msg", "Error onCancelled", error.toException());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RegistroUsuario.this);
                builder.setMessage("¿Seguro que desea regresar a la pantalla de inicio? Perderá los datos ingresados");
                builder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegistroUsuario.this, Presentacion.class);
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}