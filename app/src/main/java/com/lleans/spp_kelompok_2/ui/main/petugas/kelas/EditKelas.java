package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

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
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas4EditKelasBinding;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasSharedModel;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;
import java.sql.Struct;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditKelas extends Fragment {

    private Petugas4EditKelasBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    private KelasSharedModel sharedModel;
    private int idKelas;

    public EditKelas() {
        // Required empty public constructor

    }

    private void getBack(KelasData data, boolean isDeleted) {
        if (isDeleted) {
            controller.popBackStack(R.id.editKelas, true);
            controller.popBackStack(R.id.siswa_petugas, true);
            controller.popBackStack(R.id.kelas_petugas, false);
        } else {
            sharedModel.updateData(data);
            controller.navigateUp();
        }
    }

    private void deleteKelas() {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<KelasData>> kelasDataCall;

        kelasDataCall = apiInterface.deleteKelas(idKelas);
        kelasDataCall.enqueue(new Callback<BaseResponse<KelasData>>() {
            @Override
            public void onResponse(Call<BaseResponse<KelasData>> call, Response<BaseResponse<KelasData>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.body() != null && response.isSuccessful()) {
                    UtilsUI.toaster(getContext(), response.body().getMessage());
                    getBack(response.body().getDetails(), true);
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

    private void editKelas(String namaKelas, String jurusan, Long angkatan) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<KelasData>> editKelasCall;

        editKelasCall = apiInterface.putKelas(
                idKelas,
                namaKelas,
                jurusan,
                angkatan);
        editKelasCall.enqueue(new Callback<BaseResponse<KelasData>>() {
            @Override
            public void onResponse(Call<BaseResponse<KelasData>> call, Response<BaseResponse<KelasData>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.body() != null && response.isSuccessful()) {
                    UtilsUI.toaster(getContext(), response.body().getMessage());
                    getBack(response.body().getDetails(), false);
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
            String namaKelas, jurusan;
            Long angkatan;

            namaKelas = binding.namaKelas.getText().toString();
            jurusan = binding.jurusan.getText().toString();
            angkatan = Long.parseLong(binding.angkatan.getText().toString());
            if (namaKelas.isEmpty() || jurusan.isEmpty() || angkatan == null) {
                UtilsUI.toaster(getContext(), "Data tidak boleh kosong!");
            } else {
                UtilsUI.dialog(getContext(), "Simpan data?", "Apakah anda yakin untuk menyimpan data berikut, pastikan data sudah benar.", true).setPositiveButton("Ok", (dialog, which) -> {
                    editKelas(namaKelas, jurusan, angkatan);
                }).show();
            }
        });

        binding.hapus.setOnClickListener(view2 -> {
            UtilsUI.dialog(getContext(), "Hapus data?", "Apakah anda yakin untuk menghapus data berikut, data siswa di dalam kelas ini akan terhapus beserta data transaksinya.", true).setPositiveButton("Ok", (dialog, which) -> {
                deleteKelas();
            }).show();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas4EditKelasBinding.inflate(inflater, container, false);

        sharedModel = new ViewModelProvider(requireActivity()).get(KelasSharedModel.class);
        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        UtilsUI.isLoading(binding.refresher, false, false);
        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemKelas -> {
            this.idKelas = detailsItemKelas.getIdKelas();
            binding.namaKelas.setText(detailsItemKelas.getNamaKelas());
            binding.jurusan.setText(detailsItemKelas.getJurusan());
            binding.angkatan.setText(String.valueOf(detailsItemKelas.getAngkatan()));
        });
        return binding.getRoot();
    }

}