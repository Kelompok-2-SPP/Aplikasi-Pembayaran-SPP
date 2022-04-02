package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

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
import com.lleans.spp_kelompok_2.databinding.Petugas4EditPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPetugas extends Fragment {

    private Petugas4EditPetugasBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    private PetugasSharedModel sharedModel;
    private int idPetugas;

    public EditPetugas() {
        // Required empty public constructor
    }

    private void getBack(PetugasData data, boolean isDeleted){
        if (isDeleted) {
            controller.popBackStack(R.id.editPetugas, true);
            controller.popBackStack(R.id.detailPetugas_petuga, true);
            controller.popBackStack(R.id.petugas_petugas, false);
        }else {
            sharedModel.updateData(data);
            controller.navigateUp();
        }
    }

    private void deletePetugas() {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<PetugasData>> petugasDataCall;

        petugasDataCall = apiInterface.deletePetugas(idPetugas);
        petugasDataCall.enqueue(new Callback<BaseResponse<PetugasData>>() {
            @Override
            public void onResponse(Call<BaseResponse<PetugasData>> call, Response<BaseResponse<PetugasData>> response) {
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
            public void onFailure(@NonNull Call<BaseResponse<PetugasData>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void editPetugas(String username, String password, String namaPetugas) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<PetugasData>> editPetugasCall;

        editPetugasCall = apiInterface.putPetugas(
                idPetugas,
                username,
                password,
                namaPetugas,
                null);
        editPetugasCall.enqueue(new Callback<BaseResponse<PetugasData>>() {
            @Override
            public void onResponse(Call<BaseResponse<PetugasData>> call, Response<BaseResponse<PetugasData>> response) {
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
            public void onFailure(@NonNull Call<BaseResponse<PetugasData>> call, @NonNull Throwable t) {
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
            String username, password, namaPetugas;

            username = binding.username.getText().toString();
            password = binding.password.getText().toString();
            namaPetugas = binding.namaPetugas.getText().toString();
            if (username.isEmpty() || namaPetugas.isEmpty()) {
                UtilsUI.toaster(getContext(), "Data tidak boleh kosong!");
            } else {
                UtilsUI.dialog(getContext(), "Simpan data?", "Apakah anda yakin untuk menyimpan data berikut, pastikan data sudah benar.", true).setPositiveButton("Ok", (dialog, which) -> {
                    if (password.isEmpty()) {
                        editPetugas(username, null, namaPetugas);
                    } else {
                        editPetugas(username, password, namaPetugas);
                    }
                }).show();
            }
        });
        binding.hapus.setOnClickListener(view2 -> {
            UtilsUI.dialog(getContext(), "Hapus data?", "Apakah anda yakin untuk menghapus data berikut.", true).setPositiveButton("Ok", (dialog, which) -> {
                deletePetugas();
            }).show();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas4EditPetugasBinding.inflate(inflater, container, false);

        UtilsUI.isLoading(binding.refresher, false, false);
        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        sharedModel = new ViewModelProvider(requireActivity()).get(PetugasSharedModel.class);
        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemPetugas -> {
            this.idPetugas = detailsItemPetugas.getIdPetugas();
            binding.username.setText(detailsItemPetugas.getUsername());
            binding.namaPetugas.setText(detailsItemPetugas.getNamaPetugas());
        });
        return binding.getRoot();
    }

}