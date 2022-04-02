package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

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

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.databinding.Petugas5TambahSiswaBinding;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasSharedModel;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahSiswa extends Fragment {

    private Petugas5TambahSiswaBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    private SiswaSharedModel siswaSharedModel;
    private int idKelas;

    public TambahSiswa() {
        // Required empty public constructor
    }

    private void tambahSiswa(String nisn, String nis, String password, String namaSiswa, String alamat, String noTelp) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<SiswaData>> tambahSiswaCall;

        tambahSiswaCall = apiInterface.postSiswa(
                nisn,
                nis,
                password,
                namaSiswa,
                idKelas,
                alamat,
                noTelp);
        tambahSiswaCall.enqueue(new Callback<BaseResponse<SiswaData>>() {
            @Override
            public void onResponse(Call<BaseResponse<SiswaData>> call, Response<BaseResponse<SiswaData>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.body() != null && response.isSuccessful()) {
                    UtilsUI.toaster(getContext(), response.body().getMessage());
                    siswaSharedModel.updateData(response.body().getDetails());
                    controller.navigateUp();
                } else {
                    try {
                        BaseResponse message = new Gson().fromJson(response.errorBody().charStream(), BaseResponse.class);
                        UtilsUI.toaster(getContext(), message.getMessage());
                    } catch (Exception e) {
                        try {
                            UtilsUI.dialog(getContext(), "Something went wrong!", response.errorBody().string(), false).show();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<SiswaData>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        binding.simpan.setOnClickListener(view1 -> {
            String nisn, nis, password, namaSiswa, alamat, noTelp;

            nisn = binding.nisn.getText().toString();
            nis = binding.nis.getText().toString();
            namaSiswa = binding.nama.getText().toString();
            password = binding.password.getText().toString();
            alamat = binding.alamat.getText().toString();
            noTelp = binding.noTelp.getText().toString();
            if (nisn.isEmpty() || nis.isEmpty() || namaSiswa.isEmpty() || alamat.isEmpty() || noTelp.isEmpty() || password.isEmpty()) {
                UtilsUI.toaster(getContext(), "Data tidak boleh kosong!");
            } else {
                UtilsUI.dialog(getContext(), "Simpan data?", "Apakah anda yakin untuk menyimpan data berikut, pastikan data sudah benar.", true).setPositiveButton("Ok", (dialog, which) -> {
                    tambahSiswa(nisn, nis, password, namaSiswa, alamat, noTelp);
                }).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas5TambahSiswaBinding.inflate(inflater, container, false);

        UtilsUI.isLoading(binding.refresher, false, false);
        KelasSharedModel kelasSharedModel = new ViewModelProvider(requireActivity()).get(KelasSharedModel.class);
        siswaSharedModel = new ViewModelProvider(requireActivity()).get(SiswaSharedModel.class);
        kelasSharedModel.getData().observe(getViewLifecycleOwner(), detailsItemKelas -> {
            this.idKelas = detailsItemKelas.getIdKelas();
        });
        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        return binding.getRoot();
    }

}