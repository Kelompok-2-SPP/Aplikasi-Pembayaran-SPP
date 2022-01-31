package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.PetugasEditSppBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.DetailsItemKelas;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.domain.model.siswa.DetailsItemSiswa;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
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

    private PetugasEditSppBinding binding;
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
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
                    toaster(response.body().getMessage());
                    nav.navigateUp();
                } else if (response.errorBody() != null) {
                    isLoading(false);
                    SppData message = new Gson().fromJson(response.errorBody().charStream(), SppData.class);
                    toaster(message.getMessage());
                } else {
                    try {
                        toaster(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SppData> call, Throwable t) {
                isLoading(false);
                toaster(t.getLocalizedMessage());
            }
        });
    }

    private void editSpp(Integer idSpp, Integer angkatan, Integer tahun, Integer nominal) {
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
                if (response.isSuccessful()) {
                    isLoading(false);
                    toaster(response.body().getMessage());
                    nav.navigateUp();
                } else {
                    // Handling 401 error
                    // error karna server /eksternal
                    isLoading(false);
                    try {
                        toaster(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SppData> call, Throwable t) {
                // error karna masalah hp /internal
                isLoading(false);
                toaster(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
        binding.btnSimpanSpp.setOnClickListener(view1 -> {
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
        binding.btnHapusSpp.setOnClickListener(view2 -> {
            Integer idSpp;
            idSpp = detailsItemSpp.getIdSpp();
            deleteSpp(idSpp);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = PetugasEditSppBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        Bundle bundle = getArguments();
        detailsItemSpp = (DetailsItemSpp) bundle.get("spp");

        binding.angkatan.setText(String.valueOf(detailsItemSpp.getAngkatan()));
        binding.tahun.setText(String.valueOf(detailsItemSpp.getTahun()));
        binding.nominal.setText(String.valueOf(detailsItemSpp.getNominal()));

        return binding.getRoot();
    }

    @Override
    public void isLoading(Boolean isLoading) {

    }

    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dialog(String title, String message) {

    }
}