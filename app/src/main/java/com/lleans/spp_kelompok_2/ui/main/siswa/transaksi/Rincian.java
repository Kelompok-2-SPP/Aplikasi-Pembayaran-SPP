package com.lleans.spp_kelompok_2.ui.main.siswa.transaksi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.databinding.RinciantransaksiSiswaBinding;

public class Rincian extends Fragment {

    private RinciantransaksiSiswaBinding binding;

    public Rincian() {
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
        binding = RinciantransaksiSiswaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}