package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

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
import com.lleans.spp_kelompok_2.databinding.RinciansppPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.spp.DetailsItemSpp;

public class RincianSpp extends Fragment {

    private RinciansppPetugasBinding binding;

    private DetailsItemSpp data;

    public RincianSpp() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController nav = Navigation.findNavController(view);
        Bundle bundle = new Bundle();
        bundle.putSerializable("spp", data);
        binding.btnEdit.setOnClickListener(v -> nav.navigate(R.id.action_rincianSpp_petugas_to_editSpp, bundle));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        data = (DetailsItemSpp) bundle.getSerializable("spp");
        binding = RinciansppPetugasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}