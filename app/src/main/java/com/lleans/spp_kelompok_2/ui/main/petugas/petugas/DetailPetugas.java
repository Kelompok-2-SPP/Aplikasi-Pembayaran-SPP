package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas3DetailPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.main.petugas.aktivitas.AktivitasCardAdapter;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPetugas extends Fragment {

    private Petugas3DetailPetugasBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    private int idPetugas;

    public DetailPetugas() {
        // Required empty public constructor
    }

    private void setAdapter(List<PembayaranData> data) {
        AktivitasCardAdapter cardAdapter = new AktivitasCardAdapter(data, controller, "detailPetugas", null);
        cardAdapter.setItemCount(3);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(cardAdapter);
    }

    private void getAktivitas() {
        UtilsUI.isLoading(binding.refresher, true, true);
        Call<BaseResponse<List<PembayaranData>>> pembayaranDataListCall;

        pembayaranDataListCall = apiInterface.getPembayaran(
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
        pembayaranDataListCall.enqueue(new Callback<BaseResponse<List<PembayaranData>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PembayaranData>>> call, Response<BaseResponse<List<PembayaranData>>> response) {
                UtilsUI.isLoading(binding.refresher, true, false);
                if (response.body() != null && response.isSuccessful()) {
                    setAdapter(response.body().getDetails());
                } else {
                    if (response.code() == 404) {
                        UtilsUI.toaster(getContext(), "Aktivitas petugas tidak ditemukan");
                    } else {
                        try {
                            BaseResponse message = new Gson().fromJson(response.errorBody().charStream(), BaseResponse.class);
                            UtilsUI.toaster(getContext(), message.getMessage());
                        } catch (Exception e) {
                            try {
                                UtilsUI.dialog(getContext(), "Something went wrong!", response.errorBody().string(), false).show();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<PembayaranData>>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, true, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });

    }

    private void getSharedModel() {
        PetugasSharedModel sharedModel = new ViewModelProvider(requireActivity()).get(PetugasSharedModel.class);
        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemPetugas -> {
            this.idPetugas = detailsItemPetugas.getIdPetugas();
            UtilsUI.nicknameBuilder(getActivity().getApplicationContext(), detailsItemPetugas.getNamaPetugas(), binding.nick, binding.nickFrame);
            binding.nama.setText(detailsItemPetugas.getNamaPetugas());
            binding.username.setText(detailsItemPetugas.getUsername());
            binding.level.setText(detailsItemPetugas.getLevel().equals("petugas") ? "Petugas" : "Admin");
            getAktivitas();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        Bundle bundle = new Bundle();
        bundle.putBoolean("fromHomepage", false);
        binding.refresher.setOnRefreshListener(this::getAktivitas);
        binding.aktivitas.setOnClickListener(v -> controller.navigate(R.id.action_detailPetugas_petuga_to_aktivitas_petugas, bundle));
        binding.edit.setOnClickListener(v -> controller.navigate(R.id.action_detailPetugas_petuga_to_editPetugas));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas3DetailPetugasBinding.inflate(inflater, container, false);

        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        getSharedModel();
        return binding.getRoot();
    }

}