package com.example.apptesis.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptesis.R;
import com.example.apptesis.databinding.FragmentNavCalendarioBinding;
import com.example.apptesis.databinding.FragmentNavConfiguracionBinding;

public class nav_configuracion extends Fragment {

    FragmentNavConfiguracionBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavConfiguracionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();



        return view;
    }
}