package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.Petugas3DetailPetugasBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranDataList;
import com.lleans.spp_kelompok_2.domain.model.petugas.DetailsItemPetugas;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.main.petugas.aktivitas.AktivitasCardAdapter;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPetugas extends Fragment implements UIListener {

    private Petugas3DetailPetugasBinding binding;
    private NavController navController;
    private SessionManager sessionManager;

    private int idPetugas;

    public DetailPetugas() {
        // Required empty public constructor
    }

    private void getAktivitas() {
        isLoading(true);
        Call<PembayaranDataList> pembayaranDataListCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        pembayaranDataListCall = apiInterface.getPembayaran(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                idPetugas,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        pembayaranDataListCall.enqueue(new Callback<PembayaranDataList>() {
            @Override
            public void onResponse(Call<PembayaranDataList> call, Response<PembayaranDataList> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    AktivitasCardAdapter cardAdapter = new AktivitasCardAdapter(response.body().getDetails(), navController, false, true);
                    cardAdapter.setItemCount(3);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setAdapter(cardAdapter);
                } else if (response.code() <= 500){
                    PembayaranDataList message = new Gson().fromJson(response.errorBody().charStream(), PembayaranDataList.class);
                    toaster(message.getMessage());
                } else {
                    try {
                        dialog("Something went wrong !", Html.fromHtml(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
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
        navController = Navigation.findNavController(view);
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromHomepage", false);

        binding.refresher.setOnRefreshListener(() -> {
            getAktivitas();
        });

        binding.aktivitas.setOnClickListener(v -> navController.navigate(R.id.action_detailPetugas_petuga_to_aktivitas_petugas, bundle));
        binding.edit.setOnClickListener(v -> navController.navigate(R.id.action_detailPetugas_petuga_to_editPetugas));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        PetugasSharedModel sharedModel = new ViewModelProvider(requireActivity()).get(PetugasSharedModel.class);
        sessionManager = new SessionManager(getContext());
        binding = Petugas3DetailPetugasBinding.inflate(inflater, container, false);

        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemPetugas -> {
            this.idPetugas = detailsItemPetugas.getIdPetugas();
            Utils.nicknameBuilder(getContext(), detailsItemPetugas.getNamaPetugas(), binding.nick, binding.nickFrame);
            binding.nama.setText(detailsItemPetugas.getNamaPetugas());
            binding.username.setText(detailsItemPetugas.getUsername());
            binding.level.setText(detailsItemPetugas.getLevel().equals("petugas") ? "Petugas" : "Admin");
            getAktivitas();
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