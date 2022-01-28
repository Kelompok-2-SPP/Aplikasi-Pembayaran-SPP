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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.SppPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.spp.SppDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Spp extends Fragment implements UIListener {

    private SppPetugasBinding binding;
    private NavController nav;
    private SessionManager sessionManager;

    public Spp() {
        // Required empty public constructor
    }

    private void UILimiter(){
        binding.btnTambahSpp.setVisibility(View.GONE);
    }

    private void getSpp() {
        Call<SppDataList> sppData;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sppData = apiInterface.getSpp(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                null,
                null,
                null,
                null,
                null
        );
        sppData.enqueue(new Callback<SppDataList>() {
            @Override
            public void onResponse(Call<SppDataList> call, Response<SppDataList> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(true);
                    SppCardAdapter cardAdapter = new SppCardAdapter(response.body().getDetails(), nav);
                    binding.rvSpp.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.rvSpp.setAdapter(cardAdapter);
                } else {
                    isLoading(false);
                    toaster(response.message());
                }
            }

            @Override
            public void onFailure(Call<SppDataList> call, Throwable t) {
                isLoading(false);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = SppPetugasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(getContext());
        if(sessionManager.getUserDetail().get(SessionManager.TYPE).equals("petugas")){
            UILimiter();
        }
        // Contoh navigation
        getSpp();
        nav = Navigation.findNavController(view);
        // Cari id navigation di nav graph
        binding.btnTambahSpp.setOnClickListener(v -> nav.navigate(R.id.action_spp_petugas_to_tambahspp_petugas));
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