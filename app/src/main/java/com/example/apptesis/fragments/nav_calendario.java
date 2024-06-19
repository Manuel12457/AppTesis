package com.example.apptesis.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.apptesis.R;
import com.example.apptesis.databinding.FragmentNavCalendarioBinding;

public class nav_calendario extends Fragment {

    private FragmentNavCalendarioBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavCalendarioBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
}