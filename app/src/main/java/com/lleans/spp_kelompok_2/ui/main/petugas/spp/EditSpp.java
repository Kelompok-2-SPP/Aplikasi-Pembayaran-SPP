package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.lleans.spp_kelompok_2.databinding.Petugas4EditSppBinding;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranDataList;
import com.lleans.spp_kelompok_2.domain.model.spp.DetailsItemSpp;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSpp extends Fragment implements UIListener {

    private Petugas4EditSppBinding binding;
    private DetailsItemSpp detailsItemSpp;
    private SessionManager sessionManager;
    private NavController nav;

    public EditSpp() {
        // Required empty public constructor
    }

    // Delete SPP function
    private void deleteSpp(Integer idSpp) {
        Call<SppData> sppDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sppDataCall = apiInterface.deleteSpp(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                idSpp
        );
        sppDataCall.enqueue(new Callback<SppData>() {
            @Override
            public void onResponse(Call<SppData> call, Response<SppData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
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

    private void editSpp(Integer idSpp, Integer angkatan, Integer tahun, Integer nominal) {
        isLoading(true);
        Call<SppData> editSppCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        editSppCall = apiInterface.putSpp(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                idSpp,
                angkatan,
                tahun,
                nominal);
        editSppCall.enqueue(new Callback<SppData>() {
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
            Integer idSpp, angkatan, tahun, nominal;
            idSpp = detailsItemSpp.getIdSpp();
            angkatan = Integer.parseInt(binding.angkatan.getText().toString());
            tahun = Integer.parseInt(binding.tahun.getText().toString());
            nominal = Integer.parseInt(binding.nominal.getText().toString());
            if (angkatan == null || tahun == null || nominal == null) {
                toaster("Data harus diisi!");
            } else {
                editSpp(idSpp, angkatan, tahun, nominal);
            }
        });
        binding.hapus.setOnClickListener(view2 -> {
            Integer idSpp;
            idSpp = detailsItemSpp.getIdSpp();
            deleteSpp(idSpp);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas4EditSppBinding.inflate(inflater, container, false);
        isLoading(false);
        sessionManager = new SessionManager(getContext());
        Bundle bundle = getArguments();
        detailsItemSpp = (DetailsItemSpp) bundle.get("spp");

        binding.angkatan.setText(String.valueOf(detailsItemSpp.getAngkatan()));
        binding.tahun.setText(String.valueOf(detailsItemSpp.getTahun()));
        binding.nominal.setText(String.valueOf(detailsItemSpp.getNominal()));

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