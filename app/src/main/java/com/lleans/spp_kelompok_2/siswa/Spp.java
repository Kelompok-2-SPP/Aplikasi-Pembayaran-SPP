package com.lleans.spp_kelompok_2.siswa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.HomepagePetugasBinding;
import com.lleans.spp_kelompok_2.databinding.SppSiswaBinding;

public class Spp extends Fragment {

    private SppSiswaBinding binding;

    public Spp() {
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
        binding = SppSiswaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}