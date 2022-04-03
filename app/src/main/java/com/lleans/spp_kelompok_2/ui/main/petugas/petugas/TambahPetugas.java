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

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.databinding.Petugas5TambahPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPetugas extends Fragment {

    private Petugas5TambahPetugasBinding binding;
    private ApiInterface apiInterface;
    private NavController controller;

    public TambahPetugas() {
        // Required empty public constructor
    }

    private void tambahPetugas(String username, String password, String namaPetugas) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<PetugasData>> tambahPetugasCall;

        tambahPetugasCall = apiInterface.postPetugas(
                username,
                password,
                namaPetugas,
                null);
        tambahPetugasCall.enqueue(new Callback<BaseResponse<PetugasData>>() {
            @Override
            public void onResponse(Call<BaseResponse<PetugasData>> call, Response<BaseResponse<PetugasData>> response) {
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
            public void onFailure(@NonNull Call<BaseResponse<PetugasData>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void diagSimpan() {
        String username, password, namaPetugas;

        username = binding.username.getText().toString();
        password = binding.password.getText().toString();
        namaPetugas = binding.namaPetugas.getText().toString();
        if (username.isEmpty() || password.isEmpty() || namaPetugas.isEmpty()) {
            UtilsUI.toaster(getContext(), "Data tidak boleh kosong!");
        } else {
            UtilsUI.dialog(getContext(), "Simpan data?", "Apakah anda yakin untuk menyimpan data berikut, pastikan data sudah benar.", true).setPositiveButton("Ok", (dialog, which) -> {
                tambahPetugas(username, password, namaPetugas);
            }).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        binding.simpan.setOnClickListener(view1 -> diagSimpan());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas5TambahPetugasBinding.inflate(inflater, container, false);

        UtilsUI.isLoading(binding.refresher, false, false);
        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        return binding.getRoot();
    }

}