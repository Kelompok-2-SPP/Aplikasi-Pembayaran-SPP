package com.lleans.spp_kelompok_2.petugas.aktivitas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.AktivitasPetugasBinding;
import com.lleans.spp_kelompok_2.databinding.TambahkelasPetugasBinding;

public class Aktivitas extends Fragment {

    private AktivitasPetugasBinding binding;

    public Aktivitas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = AktivitasPetugasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}