package com.example.apptesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.apptesis.internet.NetworkChangeReceiver;
import com.example.apptesis.internet.NetworkViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class CambioContrasenia extends AppCompatActivity {

    boolean correoValido = true;
    int vecesCorreo = 0;

    FirebaseAuth firebaseAuth;
    private AlertDialog noConnectionDialog;
    private NetworkViewModel networkViewModel;
    private NetworkChangeReceiver networkChangeReceiver;

    LinearProgressIndicator linearProgressIndicator;
    Button changePswButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_contrasenia);
        getSupportActionBar().setTitle("Cambio de contraseña");

        linearProgressIndicator = findViewById(R.id.linearProgressIndicator);
        changePswButton = findViewById(R.id.btn_enviarCorreo);

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

        TextInputLayout correo = findViewById(R.id.inputCorreo_cambioPsw);
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

        firebaseAuth = firebaseAuth.getInstance();
    }

    public void validarCorreo(View view) {
        TextInputLayout correo = findViewById(R.id.inputCorreo_cambioPsw);
        linearProgressIndicator.setVisibility(View.VISIBLE);
        changePswButton.setEnabled(false);

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

        if (correoValido) {
            firebaseAuth.sendPasswordResetEmail(correo.getEditText().getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Esconder edittext y cambiar texto
                    Log.d("forgetpsw", "Correo enviado para cambio de contrasenia");
                    Snackbar.make(findViewById(R.id.activity_cambio_contrasenia), "Se le ha enviado un correo para proceder con la solicitud de cambio de contraseña", Snackbar.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    linearProgressIndicator.setVisibility(View.INVISIBLE);
                    changePswButton.setEnabled(true);
                    Snackbar.make(findViewById(R.id.activity_cambio_contrasenia), "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            linearProgressIndicator.setVisibility(View.INVISIBLE);
            changePswButton.setEnabled(true);
        }
    }
}