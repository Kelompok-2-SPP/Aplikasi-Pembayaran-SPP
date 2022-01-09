package com.lleans.spp_kelompok_2.petugas.siswa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.SiswaPetugasBinding;

public class Siswa extends Fragment {

    private SiswaPetugasBinding binding;

    public Siswa() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = SiswaPetugasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}