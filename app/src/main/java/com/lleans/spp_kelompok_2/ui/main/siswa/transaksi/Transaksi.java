package com.lleans.spp_kelompok_2.ui.main.siswa.transaksi;

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
import com.lleans.spp_kelompok_2.databinding.Siswa2TransaksiBinding;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transaksi extends Fragment implements UIListener {

    private Siswa2TransaksiBinding binding;
    private SessionManager sessionManager;
    private NavController navController;

    public Transaksi() {
        // Required empty public constructor
    }

    private void getTransaksi(String tglDibayar, Integer bulanDibayar, Integer tahun_dibayar) {
        isLoading(true);
        Call<PembayaranDataList> pembayaranDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        pembayaranDataCall = apiInterface.getPembayaran(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                null,
                sessionManager.getUserDetail().get(SessionManager.ID),
                tglDibayar, bulanDibayar, tahun_dibayar,
                null,
                null,
                null,
                null);
        pembayaranDataCall.enqueue(new Callback<PembayaranDataList>() {
            @Override
            public void onResponse(Call<PembayaranDataList> call, Response<PembayaranDataList> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
                    TransaksiCardAdapter cardAdapter = new TransaksiCardAdapter(response.body().getDetails(), navController, false);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setAdapter(cardAdapter);
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
        navController = Navigation.findNavController(view);

        getTransaksi(null, null, null);

        binding.refresher.setOnRefreshListener(() -> {
            getTransaksi(null, null, null);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Siswa2TransaksiBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        getTransaksi(null, null, null);
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