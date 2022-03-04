package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

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
import com.lleans.spp_kelompok_2.databinding.Petugas4EditSiswaBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.DetailsItemKelas;
import com.lleans.spp_kelompok_2.domain.model.siswa.DetailsItemSiswa;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSiswa extends Fragment implements UIListener {

    private Petugas4EditSiswaBinding binding;
    private DetailsItemSiswa detailsItemSiswa;
    private DetailsItemKelas kelas;
    private SessionManager sessionManager;
    private NavController nav;

    public EditSiswa() {
        // Required empty public constructor
    }

    // Delete siswa function
    private void deleteSiswa(String nisn) {
        Call<SiswaData> siswaDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        siswaDataCall = apiInterface.deleteSiswa(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                nisn
        );
        siswaDataCall.enqueue(new Callback<SiswaData>() {
            @Override
            public void onResponse(Call<SiswaData> call, Response<SiswaData> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
                    toaster(response.body().getMessage());
                    nav.navigateUp();
                } else if (response.errorBody() != null) {
                    isLoading(false);
                    SiswaData message = new Gson().fromJson(response.errorBody().charStream(), SiswaData.class);
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
            public void onFailure(Call<SiswaData> call, Throwable t) {
                isLoading(false);
                toaster(t.getLocalizedMessage());
            }
        });
    }


    private void editSiswa(String nisn, String newNisn, String nis, String password, String nama, Integer idKelas, String alamat, String noTelp) {
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
                if (response.isSuccessful()) {
                    isLoading(false);
                    toaster(response.body().getMessage());
                    nav.navigateUp();
                } else {
                    // Handling 401 error
                    isLoading(false);
                    toaster(response.message());
                }
            }

            @Override
            public void onFailure(Call<SiswaData> call, Throwable t) {
                isLoading(false);
                toaster(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);

        binding.simpan.setOnClickListener(view1 -> {
            String newNisn, nis, password, nama, alamat, noTelp;
            Integer idKelas;
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
                    editSiswa(detailsItemSiswa.getNisn(), newNisn, nis, null, nama, detailsItemSiswa.getIdKelas(), alamat, noTelp);
                } else {
                    editSiswa(detailsItemSiswa.getNisn(), newNisn, nis, password, nama, detailsItemSiswa.getIdKelas(), alamat, noTelp);
                }
            }
        });
        binding.hapus.setOnClickListener(view2 -> {
            String nisn;
            nisn = detailsItemSiswa.getNisn();
            deleteSiswa(nisn);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas4EditSiswaBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        Bundle bundle = getArguments();
        detailsItemSiswa = (DetailsItemSiswa) bundle.get("siswa");
        kelas = (DetailsItemKelas) bundle.get("kelas");
        binding.nisn.setText(detailsItemSiswa.getNisn());
        binding.nisn.setText(detailsItemSiswa.getNis());
        binding.nama.setText(detailsItemSiswa.getNama());
        binding.alamat.setText(detailsItemSiswa.getAlamat());
        binding.noTelp.setText(detailsItemSiswa.getNoTelp());
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