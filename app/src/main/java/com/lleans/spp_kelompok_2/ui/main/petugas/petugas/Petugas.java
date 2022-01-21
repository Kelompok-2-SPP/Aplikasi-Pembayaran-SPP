package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lleans.spp_kelompok_2.Abstract;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.PetugasPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.main.petugas.siswa.SiswaCardAdapter;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Petugas extends Fragment implements Abstract {

    private PetugasPetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public Petugas() {
        // Required empty public constructor
    }

    private void getPetugas() {
        isLoading(true);
        Call<PetugasData> petugasDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        petugasDataCall = apiInterface.getPetugas(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                null,
                null,
                null,
                null,
                null);
        petugasDataCall.enqueue(new Callback<PetugasData>() {
            @Override
            public void onResponse(Call<PetugasData> call, Response<PetugasData> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
//                    toaster(response.body().getDetails().toString());
                    PetugasCardAdapter cardAdapter = new PetugasCardAdapter(response.body().getDetails(), nav);
                    binding.rvPetugas.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.rvPetugas.setAdapter(cardAdapter);
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
        binding.btnTambahPetugas.setOnClickListener(v -> nav.navigate(R.id.action_petugas_petugas_to_tambahpetugas_petugas));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = PetugasPetugasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        getPetugas();
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