package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.Petugas6TambahSppBinding;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahSpp extends Fragment implements UIListener {

    private Petugas6TambahSppBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public TambahSpp() {
        // Required empty public constructor
    }

    private void tambahSpp(Integer angkatan, Integer tahun, Integer nominal){
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
        binding = Petugas6TambahSppBinding.inflate(inflater, container, false);
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

    @Override
    public void dialog(String title, String message) {

    }
}