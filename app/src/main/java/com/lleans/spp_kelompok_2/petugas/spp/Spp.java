package com.lleans.spp_kelompok_2.petugas.spp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.SppPetugasBinding;

public class Spp extends Fragment {

    private SppPetugasBinding binding;

    public Spp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = SppPetugasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}