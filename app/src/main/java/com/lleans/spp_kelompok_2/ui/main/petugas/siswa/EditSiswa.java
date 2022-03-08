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
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.Petugas4EditSiswaBinding;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSiswa extends Fragment implements UIListener {

    private Petugas4EditSiswaBinding binding;
    private SiswaSharedModel sharedModel;
    private SessionManager sessionManager;
    private NavController nav;

    private String nisn;

    public EditSiswa() {
        // Required empty public constructor
    }

    // Delete siswa function
    private void deleteSiswa() {
        isLoading(true);
        Call<SiswaData> siswaDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        siswaDataCall = apiInterface.deleteSiswa(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                nisn
        );
        siswaDataCall.enqueue(new Callback<SiswaData>() {
            @Override
            public void onResponse(Call<SiswaData> call, Response<SiswaData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    sharedModel.updateData(response.body().getDetails());
                    nav.popBackStack(R.id.siswa_petugas, false);
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


    private void editSiswa(String nisn, String newNisn, String nis, String password, String nama, Integer idKelas, String alamat, String noTelp) {
        isLoading(true);
        Call<SiswaData> editSiswaCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        editSiswaCall = apiInterface.putSiswa(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                nisn,
                newNisn,
                nis,
                password,
                nama,
                idKelas,
                alamat,
                noTelp);
        editSiswaCall.enqueue(new Callback<SiswaData>() {
            @Override
            public void onResponse(Call<SiswaData> call, Response<SiswaData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    sharedModel.updateData(response.body().getDetails());
                    nav.navigateUp();
                } else if (response.code() <= 500) {
                    SiswaData message = new Gson().fromJson(response.errorBody().charStream(), SiswaData.class);
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
            String newNisn, nis, password, nama, alamat, noTelp;

            newNisn = binding.nisn.getText().toString();
            nis = binding.nis.getText().toString();
            password = binding.password.getText().toString();
            nama = binding.nama.getText().toString();
            alamat = binding.alamat.getText().toString();
            noTelp = binding.noTelp.getText().toString();
//            idKelas = Integer.parseInt(binding.angkatan.getText().toString());
            if (newNisn.equals("") || nis.equals("") || nama.equals("") || alamat.equals("") || noTelp.equals("")) {
                toaster("Data harus diisi!");
            } else {
                if (password.equals("")) {
                    editSiswa(nisn, newNisn, nis, null, nama, null, alamat, noTelp);
                } else {
                    editSiswa(nisn, newNisn, nis, password, nama, null, alamat, noTelp);
                }
            }
        });
        binding.hapus.setOnClickListener(view2 -> {
            deleteSiswa();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas4EditSiswaBinding.inflate(inflater, container, false);
        isLoading(false);
        sessionManager = new SessionManager(getContext());
        sharedModel = new ViewModelProvider(requireActivity()).get(SiswaSharedModel.class);
        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemSiswa -> {
            this.nisn = detailsItemSiswa.getNisn();
            binding.nisn.setText(detailsItemSiswa.getNisn());
            binding.nis.setText(detailsItemSiswa.getNis());
            binding.nama.setText(detailsItemSiswa.getNama());
            binding.alamat.setText(detailsItemSiswa.getAlamat());
            binding.noTelp.setText(detailsItemSiswa.getNoTelp());
        });

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