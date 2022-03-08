package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

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
import com.lleans.spp_kelompok_2.databinding.Petugas2KelasBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.DetailsItemKelas;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kelas extends Fragment implements UIListener {

    private Petugas2KelasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public Kelas() {
        // Required empty public constructor
    }

    private void UILimiter() {
        binding.btnTambahKelas.setVisibility(View.GONE);
    }

    private void setAdapter(List<DetailsItemKelas> data) {
        KelasCardAdapter cardAdapter = new KelasCardAdapter(data, nav);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(cardAdapter);
    }

    private void getKelas(String keyword) {
        isLoading(true);
        Call<KelasDataList> kelasDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (keyword != null && !Objects.equals(keyword, "")) {
            kelasDataCall = apiInterface.keywordKelas(
                    "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                    keyword);
        } else {
            kelasDataCall = apiInterface.getKelas(
                    "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
        }
        kelasDataCall.enqueue(new Callback<KelasDataList>() {
            @Override
            public void onResponse(Call<KelasDataList> call, Response<KelasDataList> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    setAdapter(response.body().getDetails());
                } else {
                    try {
                        KelasDataList message = new Gson().fromJson(response.errorBody().charStream(), KelasDataList.class);
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
            public void onFailure(@NonNull Call<KelasDataList> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);

        binding.btnTambahKelas.setOnClickListener(v -> nav.navigate(R.id.action_kelas_petugas_to_tambahKelas_petugas));
        binding.searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                getKelas(binding.searchBar.getText().toString());
            }
        });
        binding.refresher.setOnRefreshListener(() -> {
            getKelas(null);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas2KelasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        if (sessionManager.getUserDetail().get(SessionManager.TYPE).equals("petugas")) {
            UILimiter();
        }
        getKelas(null);
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