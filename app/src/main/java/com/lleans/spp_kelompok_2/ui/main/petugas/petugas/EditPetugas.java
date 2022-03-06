package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.Petugas4EditPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranDataList;
import com.lleans.spp_kelompok_2.domain.model.petugas.DetailsItemPetugas;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPetugas extends Fragment implements UIListener {

    private Petugas4EditPetugasBinding binding;

    private DetailsItemPetugas data;
    private SessionManager sessionManager;
    private NavController nav;

    public EditPetugas() {
        // Required empty public constructor
    }

    // Delete petugas function
    private void deletePetugas(Integer idPetugas) {
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
                    nav.navigateUp();
                } else if (response.code() <= 500) {
                    PetugasData message = new Gson().fromJson(response.errorBody().charStream(), PetugasData.class);
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
            public void onFailure(@NonNull Call<PetugasData> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    private void editPetugas(Integer idPetugas, String username, String password, String namaPetugas) {
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
                    nav.navigateUp();
                } else if (response.code() <= 500) {
                    PetugasData message = new Gson().fromJson(response.errorBody().charStream(), PetugasData.class);
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
            Integer idPetugas;
            idPetugas = data.getIdPetugas();
            username = binding.username.getText().toString();
            password = binding.password.getText().toString();
            namaPetugas = binding.namaPetugas.getText().toString();
            if (username.equals("") || namaPetugas.equals("")) {
                toaster("Data harus diisi!");
            } else {
                if (password.equals("")) {
                    editPetugas(idPetugas, username, null, namaPetugas);
                } else {
                    editPetugas(idPetugas, username, password, namaPetugas);
                }

            }
        });
        binding.hapus.setOnClickListener(view2 -> {
            Integer idPetugas;
            idPetugas = data.getIdPetugas();
            deletePetugas(idPetugas);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas4EditPetugasBinding.inflate(inflater, container, false);
        isLoading(false);
        sessionManager = new SessionManager(getContext());
        Bundle bundle = getArguments();
        data = (DetailsItemPetugas) bundle.get("petugas");
        binding.username.setText(data.getUsername());
        binding.namaPetugas.setText(data.getNamaPetugas());
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
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    // Abstract class for Dialog
    @Override
    public void dialog(String title, Spanned message) {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(getContext());
        as.setTitle(title).setMessage(message).setPositiveButton("Ok", null).show();
    }
}