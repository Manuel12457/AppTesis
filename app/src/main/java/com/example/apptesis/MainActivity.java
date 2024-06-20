package com.example.apptesis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.apptesis.clases.Usuario;
import com.example.apptesis.internet.NetworkChangeReceiver;
import com.example.apptesis.internet.NetworkViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apptesis.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private AlertDialog noConnectionDialog;
    private NetworkViewModel networkViewModel;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        Menu nav_Menu = navigationView.getMenu();
        View headerView = navigationView.getHeaderView(0);
        TextView navNombreUsuario = headerView.findViewById(R.id.nombreUsuario);
        TextView navApellidoUsuario = headerView.findViewById(R.id.apellidoUsuario);
        ImageView navFotoUsuario = headerView.findViewById(R.id.imageViewUsuario);

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

        FirebaseDatabase.getInstance().getReference("usuarios").orderByChild("usuario_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Usuario usuario = ds.getValue(Usuario.class);
                                navNombreUsuario.setText(usuario.getNombre());
                                navApellidoUsuario.setText(usuario.getApellido());
                                for (Map.Entry<String, String> entry : usuario.getImagen().entrySet()) {
                                    Glide.with(MainActivity.this)
                                            .load(entry.getValue())
                                            .into(navFotoUsuario);
                                }

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_perfil, R.id.nav_calendario, R.id.nav_modo_libre, R.id.nav_practica)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        nav_Menu.findItem(R.id.sign_out).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setMessage("¿Seguro que desea cerrar su sesión?");
                builder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MainActivity.this, InicioSesion.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        moveTaskToBack(true);
        return super.getOnBackInvokedDispatcher();
    }
}