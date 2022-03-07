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
import com.lleans.spp_kelompok_2.databinding.Petugas5TambahPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranDataList;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerAdapter;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPetugas extends Fragment implements UIListener {

    private Petugas5TambahPetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public TambahPetugas() {
        // Required empty public constructor
    }

    private List<SpinnerInterface> spinnerList() {
        List<SpinnerInterface> list = new ArrayList<>();

        SpinnerInterface petugas = new SpinnerInterface();
        petugas.setName("Petugas");
        petugas.setValue(0);
        list.add(petugas);

        SpinnerInterface admin = new SpinnerInterface();
        petugas.setName("Admin");
        petugas.setValue(1);
        list.add(admin);

        return list;
    }

    private void tambahPetugas(String username, String password, String namaPetugas, String level) {
        isLoading(true);
        Call<PetugasData> tambahPetugasCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        tambahPetugasCall = apiInterface.postPetugas("Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                username,
                password,
                namaPetugas,
                level);
        tambahPetugasCall.enqueue(new Callback<PetugasData>() {
            @Override
            public void onResponse(Call<PetugasData> call, Response<PetugasData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    nav.navigateUp();
                } else if (response.code() <= 500){
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
            String username, password, namaPetugas, level;

            username = binding.username.getText().toString();
            password = binding.password.getText().toString();
            namaPetugas = binding.namaPetugas.getText().toString();
            binding.level.setOnItemClickListener((parent, view2, position, id) -> {
                parent.getItemAtPosition(position);
//                parent.get == 1 ? "admin":"petugas";
            });
            if(username.equals("") || password.equals("") || namaPetugas.equals("")) {
                toaster("Data harus diisi!");
            } else {
                tambahPetugas(username, password, namaPetugas, "petugas");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas5TambahPetugasBinding.inflate(inflater, container, false);
        isLoading(false);
        sessionManager = new SessionManager(getContext());
        binding.level.setAdapter(new SpinnerAdapter(binding.getRoot().getContext(), spinnerList(), false));

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