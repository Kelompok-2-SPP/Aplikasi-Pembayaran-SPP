package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

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
import com.lleans.spp_kelompok_2.databinding.Petugas5TambahKelasBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKelas extends Fragment implements UIListener {

    private Petugas5TambahKelasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public TambahKelas() {
        // Required empty public constructor
    }

    private void tambahKelas(String namaKelas, String jurusan, Integer angkatan) {
        isLoading(true);
        Call<KelasData> tambahKelasCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        tambahKelasCall = apiInterface.postKelas(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                namaKelas,
                jurusan,
                angkatan);
        tambahKelasCall.enqueue(new Callback<KelasData>() {
            @Override
            public void onResponse(Call<KelasData> call, Response<KelasData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    nav.navigateUp();
                } else {
                    try {
                        KelasData message = new Gson().fromJson(response.errorBody().charStream(), KelasData.class);
                        toaster(message.getMessage());
                    } catch (Exception e) {
                        try {
                            dialog("Something went wrong !", Html.fromHtml(response.errorBody().string()));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            // On failure response
            @Override
            public void onFailure(@NonNull Call<KelasData> call, @NonNull Throwable t) {
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
            String namakelas, jurusan;
            Integer angkatan;
            namakelas = binding.namaKelas.getText().toString();
            jurusan = binding.jurusan.getText().toString();
            angkatan = Integer.parseInt(binding.angkatan.getText().toString());
            if(namakelas.equals("") || jurusan.equals("") || angkatan == null) {
                toaster("Data harus diisi!");
            } else {
                tambahKelas(namakelas, jurusan, angkatan);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas5TambahKelasBinding.inflate(inflater, container, false);
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
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }

    // Abstract class for Dialog
    @Override
    public void dialog(String title, Spanned message) {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(requireContext());
        as.setTitle(title).setMessage(message).setPositiveButton("Ok", null).show();
    }
}