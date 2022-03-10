package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.Petugas3StatusSiswaBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranDataList;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusSiswa extends Fragment implements UIListener {

    private Petugas3StatusSiswaBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    private String nisn;

    public StatusSiswa() {
        // Required empty public constructor
    }

    private void UILimiter() {
        binding.edit.setVisibility(View.GONE);
    }

    private void getTransaksi() {
        isLoading(true);
        Call<PembayaranDataList> pembayaranDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        pembayaranDataCall = apiInterface.getPembayaran(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                null,
                nisn,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        pembayaranDataCall.enqueue(new Callback<PembayaranDataList>() {
            @Override
            public void onResponse(Call<PembayaranDataList> call, Response<PembayaranDataList> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    StatusSiswaCardAdapter cardAdapter = new StatusSiswaCardAdapter(response.body().getDetails(), nav);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setAdapter(cardAdapter);
                } else {
                    try {
                        PembayaranDataList message = new Gson().fromJson(response.errorBody().charStream(), PembayaranDataList.class);
                        toaster(message.getMessage());
                    } catch (Exception e) {
                        try {
                            dialog("Something went wrong !", Html.fromHtml(response.errorBody().string()));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            // On failure response
            @Override
            public void onFailure(@NonNull Call<PembayaranDataList> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);

        binding.refresher.setOnRefreshListener(this::getTransaksi);
        binding.edit.setOnClickListener(v -> nav.navigate(R.id.action_statussiswa_petugas_to_editSiswa));
        binding.add.setOnClickListener(v -> nav.navigate(R.id.action_statussiswa_petugas_to_tambahStatus));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas3StatusSiswaBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        SiswaSharedModel sharedModel = new ViewModelProvider(requireActivity()).get(SiswaSharedModel.class);

        if (sessionManager.getUserDetail().get(SessionManager.TYPE).equals("petugas")) {
            UILimiter();
        }
        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemSiswa -> {
            this.nisn = detailsItemSiswa.getNisn();
            UtilsUI.nicknameBuilder(getContext(), detailsItemSiswa.getNama(), binding.nick, binding.nickFrame);
            binding.nama.setText(detailsItemSiswa.getNama());
            binding.nisn.setText(String.valueOf(detailsItemSiswa.getNisn()));
            binding.nis.setText(detailsItemSiswa.getNis());
            binding.alamat.setText(detailsItemSiswa.getAlamat());
            binding.noTelp.setText(String.valueOf(detailsItemSiswa.getNoTelp()));
            getTransaksi();
        });

        return binding.getRoot();
    }

    // Abstract class for loadingBar
    @Override
    public void isLoading(Boolean isLoading) {
        binding.refresher.setRefreshing(isLoading);
    }

    // Abstract class for Toast
    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    // Abstract class for Dialog
    @Override
    public void dialog(String title, Spanned message) {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(getContext());
        as.setTitle(title).setMessage(message).setPositiveButton("Ok", null).show();
    }
}