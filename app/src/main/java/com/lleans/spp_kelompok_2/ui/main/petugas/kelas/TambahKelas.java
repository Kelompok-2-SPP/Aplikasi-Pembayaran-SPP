package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.databinding.Petugas5TambahKelasBinding;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKelas extends Fragment {

    private Petugas5TambahKelasBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    public TambahKelas() {
        // Required empty public constructor
    }

    private void tambahKelas(String namaKelas, String jurusan, Long angkatan) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<KelasData>> tambahKelasCall;

        tambahKelasCall = apiInterface.postKelas(
                namaKelas,
                jurusan,
                angkatan);
        tambahKelasCall.enqueue(new Callback<BaseResponse<KelasData>>() {
            @Override
            public void onResponse(Call<BaseResponse<KelasData>> call, Response<BaseResponse<KelasData>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.body() != null && response.isSuccessful()) {
                    UtilsUI.toaster(getContext(), response.body().getMessage());
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
            public void onFailure(@NonNull Call<BaseResponse<KelasData>> call, @NonNull Throwable t) {
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
            String namakelas, jurusan;
            Long angkatan;

            namakelas = binding.namaKelas.getText().toString();
            jurusan = binding.jurusan.getText().toString();
            angkatan = Long.parseLong(binding.angkatan.getText().toString());
            if (namakelas.isEmpty() || jurusan.isEmpty() || angkatan == null) {
                UtilsUI.toaster(getContext(), "Data tidak boleh kosong!");
            } else {
                UtilsUI.dialog(getContext(), "Simpan data?", "Apakah anda yakin untuk menyimpan data berikut, pastikan data sudah benar.", true).setPositiveButton("Ok", (dialog, which) -> {
                    tambahKelas(namakelas, jurusan, angkatan);
                }).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas5TambahKelasBinding.inflate(inflater, container, false);

        UtilsUI.isLoading(binding.refresher, false, false);
        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        return binding.getRoot();
    }

}