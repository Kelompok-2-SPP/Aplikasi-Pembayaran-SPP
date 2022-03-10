package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.lleans.spp_kelompok_2.databinding.Petugas5TambahSiswaBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasSharedModel;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahSiswa extends Fragment implements UIListener {

    private Petugas5TambahSiswaBinding binding;
    private SessionManager sessionManager;
    private NavController nav;
    private SiswaSharedModel siswaSharedModel;

    private int idKelas;

    public TambahSiswa() {
        // Required empty public constructor
    }

    private void tambahSiswa(String nisn, String nis, String password, String namaSiswa, String alamat, String noTelp) {
        isLoading(true);
        Call<SiswaData> tambahSiswaCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        tambahSiswaCall = apiInterface.postSiswa(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                nisn,
                nis,
                password,
                namaSiswa,
                idKelas,
                alamat,
                noTelp);
        tambahSiswaCall.enqueue(new Callback<SiswaData>() {
            @Override
            public void onResponse(Call<SiswaData> call, Response<SiswaData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    siswaSharedModel.updateData(response.body().getDetails());
                    nav.navigateUp();
                } else {
                    try {
                        SiswaData message = new Gson().fromJson(response.errorBody().charStream(), SiswaData.class);
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
            public void onFailure(@NonNull Call<SiswaData> call, @NonNull Throwable t) {
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
            String nisn, nis, password, namaSiswa, alamat, noTelp;

            nisn = binding.nisn.getText().toString();
            nis = binding.nis.getText().toString();
            namaSiswa = binding.nama.getText().toString();
            password = binding.password.getText().toString();
            alamat = binding.alamat.getText().toString();
            noTelp = binding.noTelp.getText().toString();
            if (nisn.equals("") || nis.equals("") || namaSiswa.equals("") || alamat.equals("") || noTelp.equals("")) {
                toaster("Data harus diisi!");
            } else {
                tambahSiswa(nisn, nis, password, namaSiswa, alamat, noTelp);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas5TambahSiswaBinding.inflate(inflater, container, false);
        isLoading(false);
        KelasSharedModel kelasSharedModel = new ViewModelProvider(requireActivity()).get(KelasSharedModel.class);
        siswaSharedModel = new ViewModelProvider(requireActivity()).get(SiswaSharedModel.class);

        kelasSharedModel.getData().observe(getViewLifecycleOwner(), detailsItemKelas -> {
            this.idKelas = detailsItemKelas.getIdKelas();
        });

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