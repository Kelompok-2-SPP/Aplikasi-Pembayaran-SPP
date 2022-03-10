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
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas2SiswaBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasSharedModel;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Siswa extends Fragment implements UIListener {

    private Petugas2SiswaBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    private int idKelas;

    public Siswa() {
        // Required empty public constructor
    }

    private void UILimiter() {
        binding.edit.setVisibility(View.GONE);
        binding.add.setVisibility(View.GONE);

    }

    private void getSiswa(String keyword) {
        isLoading(true);
        Call<SiswaDataList> siswaDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (keyword != null && !Objects.equals(keyword, "")) {
            siswaDataCall = apiInterface.keywordSiswa(
                    "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                    keyword);
        } else {
            siswaDataCall = apiInterface.getSiswa(
                    "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                    null,
                    null,
                    null,
                    idKelas,
                    null,
                    null,
                    null,
                    null);
        }
        siswaDataCall.enqueue(new Callback<SiswaDataList>() {
            @Override
            public void onResponse(Call<SiswaDataList> call, Response<SiswaDataList> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    SiswaCardAdapter cardAdapter = new SiswaCardAdapter(response.body().getDetails(), nav);
                    binding.jumlahSiswa.setText(response.body().getDetails().size() + " Siswa");
                    binding.jumlahSiswa.setVisibility(View.VISIBLE);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setAdapter(cardAdapter);
                } else {
                    try {
                        SiswaDataList message = new Gson().fromJson(response.errorBody().charStream(), SiswaDataList.class);
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
            public void onFailure(@NonNull Call<SiswaDataList> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);

        binding.refresher.setOnRefreshListener(() -> {
            getSiswa(null);
        });

        binding.searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                getSiswa(binding.searchBar.getText().toString());
            }
        });

        binding.add.setOnClickListener(v -> nav.navigate(R.id.action_siswa_petugas_to_tambahSiswa));
        binding.edit.setOnClickListener(v -> nav.navigate(R.id.action_siswa_petugas_to_editKelas));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas2SiswaBinding.inflate(inflater, container, false);
        KelasSharedModel shared = new ViewModelProvider(requireActivity()).get(KelasSharedModel.class);

        sessionManager = new SessionManager(getContext());
        if (sessionManager.getUserDetail().get(SessionManager.TYPE).equals("petugas")) {
            UILimiter();
        }

        shared.getData().observe(getViewLifecycleOwner(), detailsItemKelas -> {
            this.idKelas = detailsItemKelas.getIdKelas();
            binding.namaKelas.setText(detailsItemKelas.getNamaKelas());
            binding.jurusan.setText(detailsItemKelas.getJurusan());
            UtilsUI.nicknameBuilder(getContext(), detailsItemKelas.getNamaKelas(), binding.nick, binding.nickFrame);
            binding.angkatan.setText(String.valueOf(detailsItemKelas.getAngkatan()));
            getSiswa(null);
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
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }

    // Abstract class for Dialog
    @Override
    public void dialog(String title, Spanned message) {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(requireContext());
        as.setTitle(title).setMessage(message).setPositiveButton("Ok", null).show();
    }
}