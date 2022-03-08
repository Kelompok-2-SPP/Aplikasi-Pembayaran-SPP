package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas3RincianSppBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.spp.DetailsItemSpp;
import com.lleans.spp_kelompok_2.domain.model.spp.SppSharedModel;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

public class RincianSpp extends Fragment {

    private Petugas3RincianSppBinding binding;
    private SessionManager sessionManager;

    public RincianSpp() {
        // Required empty public constructor
    }

    private void UILimiter() {
        if (sessionManager.getUserDetail().get(SessionManager.TYPE).equals("siswa")) {
            binding.edit.setVisibility(View.GONE);
        } else {
            binding.edit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController nav = Navigation.findNavController(view);

        binding.edit.setOnClickListener(v -> nav.navigate(R.id.action_rincianSpp_petugas_to_editSpp));
        binding.cetak.setOnClickListener(v -> {
            try {
                binding.edit.setVisibility(View.GONE);
                Utils.exportToPNG(getContext(), binding.layout, binding.tahunSpp.getText().toString() + "_" + binding.title.getText().toString() + "_" + binding.idSpp.getText().toString());
                UILimiter();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas3RincianSppBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        SppSharedModel sharedModel = new ViewModelProvider(requireActivity()).get(SppSharedModel.class);

        UILimiter();

        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemSpp -> {
            binding.title.setText("Angkatan " + detailsItemSpp.getAngkatan());
            binding.tahunSpp.setText("Total SPP Tahun " + detailsItemSpp.getTahun());
            binding.totalSpp.setText(Utils.formatRupiah(detailsItemSpp.getNominal()));

            binding.idSpp.setText(String.valueOf(detailsItemSpp.getIdSpp()));
            binding.angkatan.setText(String.valueOf(detailsItemSpp.getAngkatan()));
            binding.tahun.setText(String.valueOf(detailsItemSpp.getTahun()));
            binding.total.setText(Utils.formatRupiah(detailsItemSpp.getNominal()));
        });

        return binding.getRoot();
    }
}