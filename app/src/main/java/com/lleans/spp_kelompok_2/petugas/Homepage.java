package com.lleans.spp_kelompok_2.petugas;

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
import com.lleans.spp_kelompok_2.databinding.HomepagePetugasBinding;

public class Homepage extends Fragment {

    private HomepagePetugasBinding binding;
    private String username;

    public Homepage() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Contoh navigation
        final NavController nav = Navigation.findNavController(view);
        // Cari id navigation di nav graph
        binding.datakelas.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_kelas_petugas));
        binding.histori.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_histori_petugas));
        binding.datasiswa.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_siswa));
        binding.SppSemua.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_spp_petugas));
        binding.aktivitasPetugas.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_aktivitasPetugas));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = HomepagePetugasBinding.inflate(inflater, container, false);
        username = getArguments().getString("username");
        binding.header.setText("Hai, " + username);

        return binding.getRoot();
    }
}