package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.Petugas4EditKelasBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditKelas extends Fragment implements UIListener {

    private Petugas4EditKelasBinding binding;

    private SessionManager sessionManager;
    private NavController nav;
    private KelasSharedModel shared;

    private int idKelas;

    public EditKelas() {
        // Required empty public constructor

    }

    // Delete kelas function
    private void deleteKelas() {
        isLoading(true);
        Call<KelasData> kelasDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        kelasDataCall = apiInterface.deleteKelas(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                idKelas
        );
        kelasDataCall.enqueue(new Callback<KelasData>() {
            @Override
            public void onResponse(Call<KelasData> call, Response<KelasData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    shared.updateData(response.body().getDetails());
                    nav.navigateUp();
                } else if (response.code() <= 500) {
                    KelasData message = new Gson().fromJson(response.errorBody().charStream(), KelasData.class);
                    toaster(message.getMessage());
                } else {
                    try {
                        dialog("Something went wrong !", Html.fromHtml(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // On failure response
            @Override
            public void onFailure(@NonNull Call<KelasData> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    // Edit kelas function
    private void editKelas(String namaKelas, String jurusan, Integer angkatan) {
        isLoading(true);
        Call<KelasData> editKelasCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        editKelasCall = apiInterface.putKelas(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                idKelas,
                namaKelas,
                jurusan,
                angkatan);
        editKelasCall.enqueue(new Callback<KelasData>() {
            @Override
            public void onResponse(Call<KelasData> call, Response<KelasData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    shared.updateData(response.body().getDetails());
                    nav.navigateUp();
                } else if (response.code() <= 500) {
                    KelasData message = new Gson().fromJson(response.errorBody().charStream(), KelasData.class);
                    toaster(message.getMessage());
                } else {
                    try {
                        dialog("Something went wrong !", Html.fromHtml(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // On failure response
            @Override
            public void onFailure(@NonNull Call<KelasData> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);

        binding.simpan.setOnClickListener(view1 -> {
            String namaKelas, jurusan;
            Integer angkatan;

            namaKelas = binding.namaKelas.getText().toString();
            jurusan = binding.jurusan.getText().toString();
            angkatan = Integer.parseInt(binding.angkatan.getText().toString());

            if (namaKelas.equals("") || jurusan.equals("") || angkatan == 0) {
                toaster("Data harus diisi!");
            } else {
                editKelas(namaKelas, jurusan, angkatan);
            }
        });

        binding.hapus.setOnClickListener(view2 -> {
           deleteKelas();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas4EditKelasBinding.inflate(inflater, container, false);
        shared = new ViewModelProvider(requireActivity()).get(KelasSharedModel.class);

        sessionManager = new SessionManager(getContext());
        isLoading(false);

        shared.getData().observe(getViewLifecycleOwner(), detailsItemKelas -> {
            this.idKelas = detailsItemKelas.getIdKelas();
            binding.namaKelas.setText(detailsItemKelas.getNamaKelas());
            binding.jurusan.setText(detailsItemKelas.getJurusan());
            binding.angkatan.setText(String.valueOf(detailsItemKelas.getAngkatan()));
        });

        return binding.getRoot();
    }

    // Abstract class for loadingBar
    @Override
    public void isLoading(Boolean isLoading) {
        binding.refresher.setEnabled(isLoading);
        binding.refresher.setRefreshing(isLoading);
    }

    // Abstract class for Toast
    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    // Abstract class for Dialog
    @Override
    public void dialog(String title, Spanned message) {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(getContext());
        as.setTitle(title).setMessage(message).setPositiveButton("Ok", null).show();
    }
}