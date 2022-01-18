package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.PetugasPetugasBinding;

public class Petugas extends Fragment {

    private PetugasPetugasBinding binding;

    public Petugas() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController nav = Navigation.findNavController(view);
        binding.btnTambahPetugas.setOnClickListener(v -> nav.navigate(R.id.action_petugas_petugas_to_tambahpetugas_petugas));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = PetugasPetugasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}