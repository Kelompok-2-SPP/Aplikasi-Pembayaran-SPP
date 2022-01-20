package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

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

import com.lleans.spp_kelompok_2.Abstract;
import com.lleans.spp_kelompok_2.databinding.TambahpetugasPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPetugas extends Fragment implements Abstract {

    private TambahpetugasPetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public TambahPetugas() {
        // Required empty public constructor
    }

    private void tambahPetugas(String username, String namaPetugas, String password) {
        Call<PetugasData> tambahPetugasCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        tambahPetugasCall = apiInterface.postPetugas("Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),username,password,namaPetugas,null);
        tambahPetugasCall.enqueue(new Callback<PetugasData>() {
            @Override
            public void onResponse(Call<PetugasData> call, Response<PetugasData> response) {
                if (response.body() != null && response.isSuccessful()) {
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
            public void onFailure(Call<PetugasData> call, Throwable t) {
                isLoading(false);
                toaster(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
        binding.simpanPetugas.setOnClickListener(view1 -> {
            String username, password, namaPetugas;
            username = binding.unamePetugas.getText().toString();
            password = binding.passPetugas.getText().toString();
            namaPetugas = binding.namaPetugas.getText().toString();
            if(username.equals("") || password.equals("") || namaPetugas.equals("")) {
                toaster("Data harus diisi!");
            } else {
                tambahPetugas(username, password, namaPetugas);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = TambahpetugasPetugasBinding.inflate(inflater, container, false);
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