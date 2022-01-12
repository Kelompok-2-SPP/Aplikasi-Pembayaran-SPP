package com.lleans.spp_kelompok_2.petugas.histori;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.RinciantransaksiSiswaBinding;
import com.lleans.spp_kelompok_2.databinding.TambahkelasPetugasBinding;

public class RincianTransaksi extends Fragment {

    private RinciantransaksiSiswaBinding binding;
    public RincianTransaksi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = RinciantransaksiSiswaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}