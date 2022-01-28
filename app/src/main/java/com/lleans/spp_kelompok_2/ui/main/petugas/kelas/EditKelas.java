package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

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
import com.lleans.spp_kelompok_2.databinding.PetugasEditKelasBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.DetailsItemKelas;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditKelas extends Fragment implements UIListener {

    private PetugasEditKelasBinding binding;
    private DetailsItemKelas detailsItemKelas;
    private SessionManager sessionManager;
    private NavController nav;
    private Bundle bundle;


    public EditKelas() {
        // Required empty public constructor

    }

    // Delete kelas function
    private void deleteKelas(Integer idKelas) {
        Call<KelasData> kelasDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        kelasDataCall = apiInterface.deleteKelas(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                idKelas
        );
        kelasDataCall.enqueue(new Callback<KelasData>() {
            @Override
            public void onResponse(Call<KelasData> call, Response<KelasData> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
                    toaster(response.body().getMessage());
                    nav.navigateUp();
                } else if (response.errorBody() != null) {
                    isLoading(false);
                    KelasData message = new Gson().fromJson(response.errorBody().charStream(), KelasData.class);
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
            public void onFailure(Call<KelasData> call, Throwable t) {
                isLoading(false);
                toaster(t.getLocalizedMessage());
            }
        });
    }

    // Edit kelas function
    private void editKelas(String namaKelas, String jurusan, Integer angkatan) {
        Call<KelasData> editKelasCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        editKelasCall = apiInterface.putKelas(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                detailsItemKelas.getIdKelas(),
                namaKelas,
                jurusan,
                angkatan);
        editKelasCall.enqueue(new Callback<KelasData>() {
            @Override
            public void onResponse(Call<KelasData> call, Response<KelasData> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
                    toaster(response.body().getMessage());
                    nav.navigateUp();
                } else if (response.errorBody() != null) {
                    isLoading(false);
                    KelasData message = new Gson().fromJson(response.errorBody().charStream(), KelasData.class);
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
            public void onFailure(Call<KelasData> call, Throwable t) {
                isLoading(false);
                toaster(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);

        binding.simpanKelas.setOnClickListener(view1 -> {
            String namakelas, jurusan;
            Integer angkatan;
            namakelas = binding.idKelas.getText().toString();
            jurusan = binding.jurusan.getText().toString();
            angkatan = Integer.parseInt(binding.angkatan.getText().toString());
            if (namakelas.equals("") || jurusan.equals("") || angkatan == null) {
                toaster("Data harus diisi!");
            } else {
                editKelas(namakelas, jurusan, angkatan);
            }
        });

        binding.hapusKelas.setOnClickListener(view2 -> {
            Integer idKelas;
            idKelas = detailsItemKelas.getIdKelas();
            deleteKelas(idKelas);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = PetugasEditKelasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());

        bundle = getArguments();
        detailsItemKelas = (DetailsItemKelas) bundle.get("kelas");

        binding.angkatan.setText(String.valueOf(detailsItemKelas.getAngkatan()));
        binding.jurusan.setText(detailsItemKelas.getJurusan());
        binding.idKelas.setText(detailsItemKelas.getNamaKelas());

        return binding.getRoot();
    }

    @Override
    public void isLoading(Boolean isLoading) {
        binding.refresher.setRefreshing(isLoading);
    }

    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dialog(String title, String message) {

    }
}