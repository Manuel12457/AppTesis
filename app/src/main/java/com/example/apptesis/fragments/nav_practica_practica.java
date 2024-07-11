package com.example.apptesis.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.Manifest;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptesis.Presentacion;
import com.example.apptesis.R;
import com.example.apptesis.RegistroUsuario;
import com.example.apptesis.databinding.FragmentNavCalendarioBinding;
import com.example.apptesis.databinding.FragmentNavPracticaPracticaBinding;
import com.example.apptesis.viewModels.NuevoProgresoUsuarioViewModel;
import com.example.apptesis.viewModels.RegistroUsuarioViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class nav_practica_practica extends Fragment {

    private FragmentNavPracticaPracticaBinding binding;
    private TextView idGestoReconocido;
    private TextView isPrecisionReconocido;
    private static final int CAMERA_PERMISSION_CODE = 1001;
    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;

    private NuevoProgresoUsuarioViewModel nuevoProgresoUsuarioViewModel;
    String leccion_id;
    String categoria_id;
    String img;
    ImageView imageView;
    FloatingActionButton f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavPracticaPracticaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            leccion_id = bundle.getString("leccion_id");
            categoria_id = bundle.getString("categoria_id");
            img = bundle.getString("img");
        }

        imageView = view.findViewById(R.id.idImagenLeccion);
        Glide.with(getActivity().findViewById(android.R.id.content))
                .load(img)
                .into(imageView);

        previewView = view.findViewById(R.id.view_finder);
        idGestoReconocido = view.findViewById(R.id.idGestoReconocido);
        isPrecisionReconocido = view.findViewById(R.id.isPrecisionReconocido);

        nuevoProgresoUsuarioViewModel = new ViewModelProvider(this).get(NuevoProgresoUsuarioViewModel.class);
        nuevoProgresoUsuarioViewModel.getRegisterResult().observe(getViewLifecycleOwner(), new Observer<NuevoProgresoUsuarioViewModel.RegisterResult>() {
            @Override
            public void onChanged(NuevoProgresoUsuarioViewModel.RegisterResult registerResult) {
                if (registerResult.success) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Un nuevo porcentaje de precisión ha sido alcanzado", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Hubo un problema en el registro de un nuevo porcentaje de precisión", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            startCamera();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_practica, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.configuracionPractica) {
                    Navigation.findNavController(view).navigate(R.id.nav_configuracion);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void bindCameraUseCases() {
        if (cameraProvider == null) {
            throw new IllegalStateException("Camera initialization failed.");
        }

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void progresoLogrado() {

        // Verificar si gesto reconocido es igual al gesto de la práctica
        String porcentaje = "";
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());
        nuevoProgresoUsuarioViewModel.registerUserProgress(categoria_id, leccion_id, timeStamp, porcentaje);
    }

}