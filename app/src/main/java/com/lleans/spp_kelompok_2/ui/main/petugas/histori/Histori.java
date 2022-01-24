package com.lleans.spp_kelompok_2.ui.main.petugas.histori;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lleans.spp_kelompok_2.Abstract;
import com.lleans.spp_kelompok_2.databinding.HistoriPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Histori extends Fragment implements Abstract {

    private HistoriPetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public Histori() {
        // Required empty public constructor
    }

    private void getHistori(){
        Call<PembayaranDataList> historiDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        historiDataCall = apiInterface.getPembayaran(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                Integer.valueOf(sessionManager.getUserDetail().get(SessionManager.ID)),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
                );
        historiDataCall.enqueue(new Callback<PembayaranDataList>() {
            @Override
            public void onResponse(Call<PembayaranDataList> call, Response<PembayaranDataList> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
//                    toaster(response.body().getDetails().toString());
                    HistoriCardAdapter cardAdapter = new HistoriCardAdapter(response.body().getDetails(), nav);
                    binding.rvKelas.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.rvKelas.setAdapter(cardAdapter);
                } else {
                    // Handling 401 error
                    isLoading(false);
                    toaster(response.message());
                }
            }

            @Override
            public void onFailure(Call<PembayaranDataList> call, Throwable t) {
                isLoading(false);
                toaster(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = HistoriPetugasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        getHistori();
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