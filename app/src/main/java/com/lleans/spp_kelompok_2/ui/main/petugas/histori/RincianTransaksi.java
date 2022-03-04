package com.lleans.spp_kelompok_2.ui.main.petugas.histori;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.lleans.spp_kelompok_2.databinding.Siswa3RincianTransaksiBinding;

public class RincianTransaksi extends Fragment {

    private Siswa3RincianTransaksiBinding binding;
    public RincianTransaksi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Siswa3RincianTransaksiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}