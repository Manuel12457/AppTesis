package com.example.apptesis.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apptesis.R;
import com.example.apptesis.databinding.FragmentNavPerfilBinding;

public class nav_perfil extends Fragment {

    private FragmentNavPerfilBinding binding;
    private TextView myTextView_nombre_completo;
    private TextView myTextView_correo;
    private TextView myTextView_frase;
    private BroadcastReceiver uiModeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CONFIGURATION_CHANGED)) {
                int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        // Light mode
                        myTextView_nombre_completo.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                        myTextView_correo.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                        myTextView_frase.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        // Dark mode
                        myTextView_nombre_completo.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
                        myTextView_correo.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
                        myTextView_frase.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
                        break;
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavPerfilBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        myTextView_nombre_completo = view.findViewById(R.id.textView_perfil_nombre_completo);
        myTextView_correo = view.findViewById(R.id.textView_perfil_correo);
        myTextView_frase= view.findViewById(R.id.textView_perfil_frase);
        // Set initial text color based on current UI mode
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                myTextView_nombre_completo.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                myTextView_correo.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                myTextView_frase.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                myTextView_nombre_completo.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                myTextView_correo.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                myTextView_frase.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                break;
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        requireContext().registerReceiver(uiModeChangedReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister BroadcastReceiver when fragment is destroyed
        requireContext().unregisterReceiver(uiModeChangedReceiver);
    }
}