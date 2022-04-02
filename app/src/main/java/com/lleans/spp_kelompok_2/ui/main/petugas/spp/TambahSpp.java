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

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.databinding.Petugas5TambahSppBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.MoneyTextWatcher;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahSpp extends Fragment {

    private Petugas5TambahSppBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    public TambahSpp() {
        // Required empty public constructor
    }

    private void tambahSpp(Long angkatan, Long tahun, Long nominal) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<SppData>> tambahSppCall;

        tambahSppCall = apiInterface.postSpp(
                angkatan,
                tahun,
                nominal);
        tambahSppCall.enqueue(new Callback<BaseResponse<SppData>>() {
            @Override
            public void onResponse(Call<BaseResponse<SppData>> call, Response<BaseResponse<SppData>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.isSuccessful()) {
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

            angkatan = Long.parseLong(binding.angkatan.getText().toString());
            tahun = Long.parseLong(binding.tahun.getText().toString());
            nominal = Utils.unformatRupiah(binding.nominal.getText().toString());
            if (angkatan == null || tahun == null || nominal == null) {
                UtilsUI.toaster(getContext(), "Data tidak boleh kosong!");
            } else {
                UtilsUI.dialog(getContext(), "Simpan data?", "Apakah anda yakin untuk menyimpan data berikut, pastikan data sudah benar.", true).setPositiveButton("Ok", (dialog, which) -> {
                    tambahSpp(angkatan, tahun, nominal);
                }).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas5TambahSppBinding.inflate(inflater, container, false);

        binding.nominal.addTextChangedListener(new MoneyTextWatcher(binding.nominal, Long.valueOf("99999999999")));
        binding.nominal.setText("0");
        UtilsUI.isLoading(binding.refresher, false, false);
        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        return binding.getRoot();
    }

}