package com.lleans.spp_kelompok_2.ui.main.petugas.aktivitas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.lleans.spp_kelompok_2.databinding.Siswa3RincianTransaksiBinding;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.DetailsItemPembayaran;

public class RincianTransaski extends Fragment {

    private Siswa3RincianTransaksiBinding binding;

    private DetailsItemPembayaran data;

    public RincianTransaski() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Siswa3RincianTransaksiBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        data = (DetailsItemPembayaran) bundle.get("pembayaran");

//        if(Utils.statusPembayaran(data.getSpp().getNominal(), data.getJumlahBayar())){
//            binding.imageView2.setImageResource(R.drawable.tunggakan);
//            binding.textView3.setText("Belum Lunas");
//            binding.textView3.setTextColor(16750848);
//            binding.totalPembayaran.setText("- Rp" + Utils.kurangBayar(data.getSpp().getNominal(), data.getJumlahBayar()));
//        }else {
//            binding.totalPembayaran.setText("Rp"+data.getJumlahBayar());
//        }



        return binding.getRoot();
    }
}