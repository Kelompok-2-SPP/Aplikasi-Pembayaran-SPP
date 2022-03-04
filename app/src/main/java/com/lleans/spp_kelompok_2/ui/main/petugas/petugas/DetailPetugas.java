package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

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
import com.lleans.spp_kelompok_2.databinding.Petugas3DetailPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.petugas.DetailsItemPetugas;

public class DetailPetugas extends Fragment {

    private Petugas3DetailPetugasBinding binding;
    private DetailsItemPetugas data;

    public DetailPetugas() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController nav = Navigation.findNavController(view);
        Bundle bundle = new Bundle();
        bundle.putSerializable("petugas", data);
        binding.edit.setOnClickListener(v -> nav.navigate(R.id.action_detailPetugas_petuga_to_editPetugas, bundle));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        data = (DetailsItemPetugas) bundle.getSerializable("petugas");
        binding = Petugas3DetailPetugasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}