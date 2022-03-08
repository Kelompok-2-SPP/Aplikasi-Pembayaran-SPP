package com.lleans.spp_kelompok_2.ui.main.siswa.transaksi;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Siswa3RincianTransaksiBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranSharedModel;
import com.lleans.spp_kelompok_2.domain.model.siswa.DetailsItemSiswa;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.CustomRequestPermission;

import java.io.IOException;

public class RincianTransaksi extends Fragment {

    private Siswa3RincianTransaksiBinding binding;
    private NavController nav;
    private SessionManager sessionManager;

    private String namaSiswa, idPembayaran;

    public RincianTransaksi() {
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
        nav = Navigation.findNavController(view);

        binding.edit.setOnClickListener(v -> {
            nav.navigate(R.id.action_rincianTransaksi_siswa_to_editStatus);
        });
        binding.cetak.setOnClickListener(v -> {
            CustomRequestPermission as = new CustomRequestPermission(getActivity());
            as.checkPermission();
            try {
                binding.edit.setVisibility(View.GONE);
                Utils.exportToPNG(getContext(), binding.layout, namaSiswa + "_" + binding.tglBayar.getText().toString() + "_" + idPembayaran);
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
        binding = Siswa3RincianTransaksiBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());

        PembayaranSharedModel sharedModel = new ViewModelProvider(requireActivity()).get(PembayaranSharedModel.class);

        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemPembayaran -> {
            this.namaSiswa = detailsItemPembayaran.getSiswa().getNama();
            this.idPembayaran = String.valueOf(detailsItemPembayaran.getIdPembayaran());
            UILimiter();
            if (Utils.statusPembayaran(detailsItemPembayaran.getSpp().getNominal(), detailsItemPembayaran.getJumlahBayar())) {
                binding.status.setImageResource(R.drawable.icon_warning);
                binding.frameStatus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
                binding.jumlah.setText(Utils.kurangBayar(detailsItemPembayaran.getSpp().getNominal(), detailsItemPembayaran.getJumlahBayar()));
            } else {
                binding.status.setImageResource(R.drawable.icon_checked);
                binding.jumlah.setText(Utils.formatRupiah(detailsItemPembayaran.getJumlahBayar()));
            }
            binding.spp.setText(detailsItemPembayaran.getTahunSpp() + " • " + Utils.getMonth(detailsItemPembayaran.getBulanSpp()));
            binding.tglBayar.setText(Utils.formatDateStringToLocal(detailsItemPembayaran.getTglBayar()));
            binding.idSpp.setText(String.valueOf(detailsItemPembayaran.getSpp().getIdSpp()));
            binding.angkatan.setText(String.valueOf(detailsItemPembayaran.getSpp().getAngkatan()));
            binding.tahun.setText(String.valueOf(detailsItemPembayaran.getTahunSpp()));
            binding.nominal.setText(Utils.formatRupiah(detailsItemPembayaran.getSpp().getNominal()));
            if (detailsItemPembayaran.getPetugas() == null)
                binding.cardPetugas.setVisibility(View.GONE);
            else
                binding.petugas.setText(detailsItemPembayaran.getPetugas().getNamaPetugas());
        });

        return binding.getRoot();
    }
}