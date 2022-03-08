package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.databinding.Petugas5TambahTransaksiBinding;
import com.lleans.spp_kelompok_2.ui.utils.MoneyTextWatcher;

public class TambahStatus extends Fragment {

    Petugas5TambahTransaksiBinding binding;

    public TambahStatus() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas5TambahTransaksiBinding.inflate(inflater, container, false);
        binding.jumlahBayar.addTextChangedListener(new MoneyTextWatcher(binding.jumlahBayar, Long.valueOf("99999999999")));
        return binding.getRoot();
    }
}