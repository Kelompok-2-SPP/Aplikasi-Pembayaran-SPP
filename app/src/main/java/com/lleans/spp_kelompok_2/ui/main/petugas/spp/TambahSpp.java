package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.Petugas5TambahSppBinding;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahSpp extends Fragment implements UIListener {

    private Petugas5TambahSppBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public TambahSpp() {
        // Required empty public constructor
    }

    private void tambahSpp(Integer angkatan, Integer tahun, Integer nominal){
        isLoading(true);
        Call<SppData> tambahSppCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        tambahSppCall = apiInterface.postSpp(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                angkatan,
                tahun,
                nominal);
        tambahSppCall.enqueue(new Callback<SppData>() {
            @Override
            public void onResponse(Call<SppData> call, Response<SppData> response) {
                isLoading(false);
                if (response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    nav.navigateUp();
                } else if (response.code() <= 500){
                    SppData message = new Gson().fromJson(response.errorBody().charStream(), SppData.class);
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
            public void onFailure(@NonNull Call<SppData> call, @NonNull Throwable t) {
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
            Integer angkatan, tahun, nominal;
            angkatan = Integer.parseInt(binding.angkatan.getText().toString());
            tahun = Integer.parseInt(binding.tahun.getText().toString());
            nominal = Integer.parseInt(binding.nominal.getText().toString());
            if(angkatan == null || tahun == null || nominal == null) {
                toaster("Data harus diisi!");
            } else {
                tambahSpp(angkatan, tahun, nominal);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas5TambahSppBinding.inflate(inflater, container, false);
        isLoading(false);
        sessionManager = new SessionManager(getContext());
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