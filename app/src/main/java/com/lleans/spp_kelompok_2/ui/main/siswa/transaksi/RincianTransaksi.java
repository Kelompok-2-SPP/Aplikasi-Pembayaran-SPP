package com.lleans.spp_kelompok_2.ui.main.siswa.transaksi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.databinding.Siswa3RincianTransaksiBinding;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.DetailsItemPembayaran;

public class RincianTransaksi extends Fragment {

    private Siswa3RincianTransaksiBinding binding;

    private DetailsItemPembayaran data;

    public RincianTransaksi() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.totalPembayaran.setText(data.getJumlahBayar());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Siswa3RincianTransaksiBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        data = (DetailsItemPembayaran) bundle.get("data");
        return binding.getRoot();
    }
}