package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas2PetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Petugas extends Fragment implements UIListener {

    private Petugas2PetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public Petugas() {
        // Required empty public constructor
    }

    private void getPetugas(String keyword) {
        isLoading(true);
        Call<PetugasDataList> petugasDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (keyword != null && !Objects.equals(keyword, "")) {
            petugasDataCall = apiInterface.keywordPetugas(
                    "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                    keyword);
            petugasDataCall.enqueue(new Callback<PetugasDataList>() {
                @Override
                public void onResponse(Call<PetugasDataList> call, Response<PetugasDataList> response) {
                    isLoading(false);
                    if (response.body() != null && response.isSuccessful()) {
                        PetugasCardAdapter cardAdapter = new PetugasCardAdapter(response.body().getDetails(), nav);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(cardAdapter);
                    } else if (response.errorBody() != null) {
                        PetugasDataList message = new Gson().fromJson(response.errorBody().charStream(), PetugasDataList.class);
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
                public void onFailure(@NonNull Call<PetugasDataList> call, @NonNull Throwable t) {
                    isLoading(false);
                    dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
                }
            });
        } else {
            petugasDataCall = apiInterface.getPetugas(
                    "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            petugasDataCall.enqueue(new Callback<PetugasDataList>() {
                @Override
                public void onResponse(Call<PetugasDataList> call, Response<PetugasDataList> response) {
                    isLoading(false);
                    if (response.body() != null && response.isSuccessful()) {
                        PetugasCardAdapter cardAdapter = new PetugasCardAdapter(response.body().getDetails(), nav);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(cardAdapter);
                    } else if (response.code() <= 500) {
                        PetugasDataList message = new Gson().fromJson(response.errorBody().charStream(), PetugasDataList.class);
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
                public void onFailure(@NonNull Call<PetugasDataList> call, @NonNull Throwable t) {
                    isLoading(false);
                    dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
                }
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
        binding.refresher.setOnRefreshListener(() -> {
            getPetugas(null);
        });
        binding.searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                getPetugas(binding.searchBar.getText().toString());
            }
        });
        binding.btnTambah.setOnClickListener(v -> nav.navigate(R.id.action_petugas_petugas_to_tambahpetugas_petugas));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas2PetugasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        getPetugas(null);
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