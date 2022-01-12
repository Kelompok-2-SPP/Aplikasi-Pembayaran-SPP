package com.lleans.spp_kelompok_2.petugas.spp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = SppPetugasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Contoh navigation
        final NavController nav = Navigation.findNavController(view);
        // Cari id navigation di nav graph
        binding.petugasLanjut.setOnClickListener(v -> nav.navigate(R.id.action_spp_petugas_to_rincianSpp_petugas));
    }
}