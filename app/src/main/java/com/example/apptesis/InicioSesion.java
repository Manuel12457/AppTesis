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
import android.widget.ProgressBar;

import com.example.apptesis.internet.NetworkChangeReceiver;
import com.example.apptesis.internet.NetworkViewModel;
import com.example.apptesis.viewModels.InicioSesionViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class InicioSesion extends AppCompatActivity {

    //FirebaseAuth firebaseAuth;
    private InicioSesionViewModel inicioSesionViewModel;
    private AlertDialog noConnectionDialog;
    private NetworkViewModel networkViewModel;
    private NetworkChangeReceiver networkChangeReceiver;

    boolean correoValido = true;
    boolean passwordValido = true;
    int vecesCorreo = 0;
    int vecesPassword = 0;

    LinearProgressIndicator linearProgressIndicator;
    Button iniSessionButton;
    Button changePswButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        getSupportActionBar().setTitle("Iniciar sesión");

        linearProgressIndicator = findViewById(R.id.linearProgressIndicator);
        iniSessionButton = findViewById(R.id.btn_ingresar);
        changePswButton = findViewById(R.id.btn_registrarse3);

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

        TextInputLayout correo = findViewById(R.id.inputCorreo_iniSesion);
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

        TextInputLayout password = findViewById(R.id.inputPassword_iniSesion);
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

        inicioSesionViewModel = new ViewModelProvider(this).get(InicioSesionViewModel.class);
        inicioSesionViewModel.getLoginResult().observe(this, new Observer<InicioSesionViewModel.LoginResult>() {
            @Override
            public void onChanged(InicioSesionViewModel.LoginResult loginResult) {
                if (loginResult.success) {
                    Intent intent = new Intent(InicioSesion.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    linearProgressIndicator.setVisibility(View.INVISIBLE);
                    iniSessionButton.setEnabled(true);
                    changePswButton.setEnabled(true);
                    Snackbar.make(findViewById(R.id.activity_inicio_sesion), "Error: " + loginResult.message, Snackbar.LENGTH_LONG).show();
                }
            }
        });
        //firebaseAuth = firebaseAuth.getInstance();
    }

    public void cambiarContrasenia(View view){
        Intent intent = new Intent(this, CambioContrasenia.class);
        startActivity(intent);
    }

    public void validarInicioSesion(View view) {

        linearProgressIndicator.setVisibility(View.VISIBLE);
        iniSessionButton.setEnabled(false);
        changePswButton.setEnabled(false);

        TextInputLayout correo = findViewById(R.id.inputCorreo_iniSesion);
        TextInputLayout password = findViewById(R.id.inputPassword_iniSesion);

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
            password.setErrorEnabled(false);
        } else {
            password.setError("Ingrese una contraseña");
            passwordValido = false;
        }

        if (correoValido && passwordValido) {
            linearProgressIndicator.setVisibility(View.VISIBLE);
            iniSessionButton.setEnabled(false);
            changePswButton.setEnabled(false);
            inicioSesionViewModel.login(correo.getEditText().getText().toString(), password.getEditText().getText().toString());
        } else {
            linearProgressIndicator.setVisibility(View.INVISIBLE);
            iniSessionButton.setEnabled(true);
            changePswButton.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }
}