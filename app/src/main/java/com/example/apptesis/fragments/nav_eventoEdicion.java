package com.example.apptesis.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptesis.R;
import com.example.apptesis.databinding.FragmentNavEventoEdicionBinding;
import com.example.apptesis.databinding.FragmentNavPerfilBinding;

public class nav_eventoEdicion extends Fragment {

    private FragmentNavEventoEdicionBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavEventoEdicionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();



        return view;
    }
}