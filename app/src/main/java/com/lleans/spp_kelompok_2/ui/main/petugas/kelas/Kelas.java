package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

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
import com.lleans.spp_kelompok_2.databinding.KelasPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.main.siswa.transaksi.TransaksiCardAdapter;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kelas extends Fragment implements Abstract {

    private KelasPetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public Kelas() {
        // Required empty public constructor
    }

    private void getKelas(){
        isLoading(true);
        Call<KelasData> kelasDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        kelasDataCall = apiInterface.getKelas(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                null,
                null,
                null,
                null,
                null);
        kelasDataCall.enqueue(new Callback<KelasData>() {
            @Override
            public void onResponse(Call<KelasData> call, Response<KelasData> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
//                    toaster(response.body().getDetails().toString());
                    KelasCardAdapter cardAdapter = new KelasCardAdapter(response.body().getDetails(), nav);
                    binding.rvKelas.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.rvKelas.setAdapter(cardAdapter);
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
        binding.btnTambahKelas.setOnClickListener(v -> nav.navigate(R.id.action_kelas_petugas_to_tambahKelas_petugas));
        binding.refresher.setOnRefreshListener(() -> {
            getKelas();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = KelasPetugasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        getKelas();
        return binding.getRoot();
    }

    @Override
    public void isLoading(Boolean isLoading) {
//        binding.refresher.setRefreshing(isLoading);
    }

    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}