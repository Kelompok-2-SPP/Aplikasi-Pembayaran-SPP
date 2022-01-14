package com.lleans.spp_kelompok_2.ui.main.petugas.histori;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.databinding.HistoriPetugasBinding;

public class Histori extends Fragment {

    private HistoriPetugasBinding binding;

    public Histori() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = HistoriPetugasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}