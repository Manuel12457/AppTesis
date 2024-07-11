package com.example.apptesis.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.apptesis.R;
import com.example.apptesis.clases.Historial;
import com.example.apptesis.databinding.FragmentNavModoLibreBinding;
import com.example.apptesis.databinding.FragmentNavModoLibreGaleriaBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class nav_modoLibre_galeria extends Fragment {

    private FragmentNavModoLibreGaleriaBinding binding;
    private FloatingActionButton floatingActionButton;
    private ImageView imageView;
    private VideoView videoView;
    private static final int Read_Permission = 101;
    private static final int PICK_MEDIA = 1;
    private TextView seleccionRecurso;
    private ArrayList<Historial> listaHistorial = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavModoLibreGaleriaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        seleccionRecurso = view.findViewById(R.id.textView9);
        floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        imageView = view.findViewById(R.id.imageResult);
        videoView = view.findViewById(R.id.videoView);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 y superior
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("PERMISSION", "NO PERMISSION");
                        requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, Read_Permission);
                    } else {
                        openGallery();
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("PERMISSION", "NO PERMISSION");
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
                    } else {
                        openGallery();
                    }
                }
            }
        });

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen o video"), PICK_MEDIA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Read_Permission) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PERMISSION", "PERMISSION GRANTED");
                openGallery();
            } else {
                Log.d("PERMISSION", "PERMISSION DENIED");
                Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_MEDIA && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedUri = data.getData();
            if (selectedUri != null) {
                handleMedia(selectedUri);
            } else if (data.getClipData() != null && data.getClipData().getItemCount() > 0) {
                selectedUri = data.getClipData().getItemAt(0).getUri();
                handleMedia(selectedUri);
            } else {
                Toast.makeText(requireContext(), "No se ha seleccionado ningún archivo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleMedia(Uri selectedUri) {
        String mimeType = requireContext().getContentResolver().getType(selectedUri);
        if (mimeType != null) {
            if (mimeType.startsWith("image/")) {
                videoView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                seleccionRecurso.setVisibility(View.GONE);
                imageView.setImageURI(selectedUri);
            } else if (mimeType.startsWith("video/")) {
                imageView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                seleccionRecurso.setVisibility(View.GONE);
                videoView.setVideoURI(selectedUri);
                videoView.start();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_modo_libre, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.historialModoLibre) {
                    Bundle bundle = new Bundle();
                    bundle.putString("historial", "Historial - Modo libre");
                    bundle.putParcelableArrayList("listaHistorial", listaHistorial);
                    Navigation.findNavController(view).navigate(R.id.nav_historial, bundle);
                    return true;
                } else if (menuItem.getItemId() == R.id.configuracionModoLibre) {
                    Navigation.findNavController(view).navigate(R.id.nav_configuracion);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

}