package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lleans.spp_kelompok_2.Abstract;
import com.lleans.spp_kelompok_2.databinding.TambahkelasPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKelas extends Fragment implements Abstract {

    private TambahkelasPetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public TambahKelas() {
        // Required empty public constructor
    }

    private void tambahKelas(String namaKelas, String jurusan, Integer angkatan) {
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
        binding = TambahkelasPetugasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        return binding.getRoot();
    }

    @Override
    public void isLoading(Boolean isLoading) {

    }

    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}