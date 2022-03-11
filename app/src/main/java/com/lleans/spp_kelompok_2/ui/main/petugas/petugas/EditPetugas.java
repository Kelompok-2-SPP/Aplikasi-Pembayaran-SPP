package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.lleans.spp_kelompok_2.databinding.Petugas4EditPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPetugas extends Fragment implements UIListener {

    private Petugas4EditPetugasBinding binding;

    private SessionManager sessionManager;
    private NavController nav;
    private PetugasSharedModel sharedModel;

    private int idPetugas;

    public EditPetugas() {
        // Required empty public constructor
    }

    // Delete petugas function
    private void deletePetugas() {
        isLoading(true);
        Call<PetugasData> petugasDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        petugasDataCall = apiInterface.deletePetugas(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                idPetugas
        );
        petugasDataCall.enqueue(new Callback<PetugasData>() {
            @Override
            public void onResponse(Call<PetugasData> call, Response<PetugasData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    sharedModel.updateData(response.body().getDetails());
                    nav.popBackStack(R.id.editPetugas, true);
                    nav.popBackStack(R.id.detailPetugas_petuga, true);
                    nav.popBackStack(R.id.petugas_petugas, false);
                } else {
                    try {
                        PetugasData message = new Gson().fromJson(response.errorBody().charStream(), PetugasData.class);
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
            public void onFailure(@NonNull Call<PetugasData> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    private void editPetugas(String username, String password, String namaPetugas) {
        isLoading(true);
        Call<PetugasData> editPetugasCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        editPetugasCall = apiInterface.putPetugas(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                idPetugas,
                username,
                password,
                namaPetugas,
                null);
        editPetugasCall.enqueue(new Callback<PetugasData>() {
            @Override
            public void onResponse(Call<PetugasData> call, Response<PetugasData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    sharedModel.updateData(response.body().getDetails());
                    nav.navigateUp();
                } else {
                    try {
                        PetugasData message = new Gson().fromJson(response.errorBody().charStream(), PetugasData.class);
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
            public void onFailure(@NonNull Call<PetugasData> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
        binding.simpan.setOnClickListener(view1 -> {
            String username, password, namaPetugas;

            username = binding.username.getText().toString();
            password = binding.password.getText().toString();
            namaPetugas = binding.namaPetugas.getText().toString();
            if (username.equals("") || namaPetugas.equals("")) {
                toaster("Data harus diisi!");
            } else {
                if (password.equals("")) {
                    editPetugas(username, null, namaPetugas);
                } else {
                    editPetugas(username, password, namaPetugas);
                }

            }
        });
        binding.hapus.setOnClickListener(view2 -> {
            deletePetugas();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas4EditPetugasBinding.inflate(inflater, container, false);
        isLoading(false);
        sessionManager = new SessionManager(getContext());
        sharedModel = new ViewModelProvider(requireActivity()).get(PetugasSharedModel.class);

        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemPetugas -> {
            this.idPetugas = detailsItemPetugas.getIdPetugas();
            binding.username.setText(detailsItemPetugas.getUsername());
            binding.namaPetugas.setText(detailsItemPetugas.getNamaPetugas());
        });

        return binding.getRoot();
    }

    // Abstract class for loadingBar
    @Override
    public void isLoading(Boolean isLoading) {
        binding.refresher.setEnabled(isLoading);
        binding.refresher.setRefreshing(isLoading);
    }

    // Abstract class for Toast
    @Override
    public void toaster(String text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }

    // Abstract class for Dialog
    @Override
    public void dialog(String title, Spanned message) {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(requireContext());
        as.setTitle(title).setMessage(message).setPositiveButton("Ok", null).show();
    }
}