package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

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
import com.lleans.spp_kelompok_2.databinding.Petugas4EditSppBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.domain.model.spp.SppSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.MoneyTextWatcher;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSpp extends Fragment {

    private Petugas4EditSppBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    private SppSharedModel sharedModel;
    private int idSpp;

    public EditSpp() {
        // Required empty public constructor
    }

    private void getBack(SppData data, boolean isDeleted) {
        if (isDeleted) {
            controller.popBackStack(R.id.editSpp, true);
            controller.popBackStack(R.id.rincianSpp_petugas, true);
            controller.popBackStack(R.id.spp_petugas, false);
        } else {
            sharedModel.updateData(data);
            controller.navigateUp();
        }
    }

    private void deleteSpp() {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<SppData>> sppDataCall;

        sppDataCall = apiInterface.deleteSpp(idSpp);
        sppDataCall.enqueue(new Callback<BaseResponse<SppData>>() {
            @Override
            public void onResponse(Call<BaseResponse<SppData>> call, Response<BaseResponse<SppData>> response) {
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
            public void onFailure(@NonNull Call<BaseResponse<SppData>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void editSpp(Long angkatan, Long tahun, Long nominal) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<SppData>> editSppCall;

        editSppCall = apiInterface.putSpp(
                idSpp,
                angkatan,
                tahun,
                nominal);
        editSppCall.enqueue(new Callback<BaseResponse<SppData>>() {
            @Override
            public void onResponse(Call<BaseResponse<SppData>> call, Response<BaseResponse<SppData>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.isSuccessful()) {
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
            public void onFailure(@NonNull Call<BaseResponse<SppData>> call, @NonNull Throwable t) {
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
            Long nominal, angkatan, tahun;

            angkatan = Long.valueOf(binding.angkatan.getText().toString());
            tahun = Long.valueOf(binding.tahun.getText().toString());
            nominal = Utils.unformatRupiah(binding.nominal.getText().toString());
            if (angkatan == null || tahun == null || nominal == null) {
                UtilsUI.toaster(getContext(), "Data tidak boleh kosong!");
            } else {
                UtilsUI.dialog(getContext(), "Simpan data?", "Apakah anda yakin untuk menyimpan data berikut, pastikan data sudah benar.", true).setPositiveButton("Ok", (dialog, which) -> {
                    editSpp(angkatan, tahun, nominal);
                }).show();
            }
        });
        binding.hapus.setOnClickListener(view2 -> {
            UtilsUI.dialog(getContext(), "Hapus data?", "Apakah anda yakin untuk menghapus data berikut.", true).setPositiveButton("Ok", (dialog, which) -> {
                deleteSpp();
            }).show();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas4EditSppBinding.inflate(inflater, container, false);

        UtilsUI.isLoading(binding.refresher, false, false);
        binding.nominal.addTextChangedListener(new MoneyTextWatcher(binding.nominal, Long.valueOf("99999999999")));
        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        sharedModel = new ViewModelProvider(requireActivity()).get(SppSharedModel.class);
        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemSpp -> {
            this.idSpp = detailsItemSpp.getIdSpp();
            binding.angkatan.setText(String.valueOf(detailsItemSpp.getAngkatan()));
            binding.tahun.setText(String.valueOf(detailsItemSpp.getTahun()));
            binding.nominal.setText(String.valueOf(detailsItemSpp.getNominal()));
        });
        return binding.getRoot();
    }

}