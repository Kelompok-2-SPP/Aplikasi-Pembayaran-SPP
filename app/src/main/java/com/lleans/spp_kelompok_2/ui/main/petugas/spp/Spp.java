package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

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
import com.lleans.spp_kelompok_2.databinding.Petugas2SppBinding;
import com.lleans.spp_kelompok_2.domain.model.spp.SppDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Spp extends Fragment implements UIListener {

    private Petugas2SppBinding binding;
    private NavController nav;
    private SessionManager sessionManager;

    public Spp() {
        // Required empty public constructor
    }

    private void UILimiter() {
        binding.add.setVisibility(View.GONE);
    }

    private void getSpp(String keyword) {
        isLoading(true);
        Call<SppDataList> sppData;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (keyword != null && !Objects.equals(keyword, "")) {
            sppData = apiInterface.keywordSpp(
                    "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                    keyword
            );
        } else {
            sppData = apiInterface.getSpp(
                    "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }
        sppData.enqueue(new Callback<SppDataList>() {
            @Override
            public void onResponse(Call<SppDataList> call, Response<SppDataList> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    SppCardAdapter cardAdapter = new SppCardAdapter(response.body().getDetails(), nav, false);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setAdapter(cardAdapter);
                } else {
                    try {
                        SppDataList message = new Gson().fromJson(response.errorBody().charStream(), SppDataList.class);
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
            public void onFailure(@NonNull Call<SppDataList> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas2SppBinding.inflate(inflater, container, false);
        binding.searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                getSpp(binding.searchBar.getText().toString());
            }
        });
        binding.refresher.setOnRefreshListener(() -> {
            getSpp(null);
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(getContext());
        if (sessionManager.getUserDetail().get(SessionManager.TYPE).equals("petugas")) {
            UILimiter();
        }
        // Contoh navigation
        getSpp(null);
        nav = Navigation.findNavController(view);
        // Cari id navigation di nav graph
        binding.add.setOnClickListener(v -> nav.navigate(R.id.action_spp_petugas_to_tambahspp_petugas));
    }

    // Abstract class for loadingBar
    @Override
    public void isLoading(Boolean isLoading) {
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