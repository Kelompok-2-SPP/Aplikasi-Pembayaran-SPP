package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.KelasPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.DetailsItemKelas;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kelas extends Fragment implements UIListener {

    private KelasPetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public Kelas() {
        // Required empty public constructor
    }

    private void setAdapter(List<DetailsItemKelas> data){
        KelasCardAdapter cardAdapter = new KelasCardAdapter(data, nav);
        binding.rvKelas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvKelas.setAdapter(cardAdapter);
    }

    private void getKelas(String keyword) {
        Call<KelasDataList> kelasDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (keyword != null && !Objects.equals(keyword, "")) {
            kelasDataCall = apiInterface.keywordKelas(
                    "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                    keyword);
            kelasDataCall.enqueue(new Callback<KelasDataList>() {
                @Override
                public void onResponse(Call<KelasDataList> call, Response<KelasDataList> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        setAdapter(response.body().getDetails());
                    } else {
                        // Handling 401 error
                        toaster(response.message());
                    }
                }

                @Override
                public void onFailure(Call<KelasDataList> call, Throwable t) {
                    toaster(t.getLocalizedMessage());
                }
            });
        } else {
            isLoading(true);
            kelasDataCall = apiInterface.getKelas(
                    "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            kelasDataCall.enqueue(new Callback<KelasDataList>() {
                @Override
                public void onResponse(Call<KelasDataList> call, Response<KelasDataList> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        isLoading(false);
//                    toaster(response.body().getDetails().toString());
                        setAdapter(response.body().getDetails());
                    } else {
                        // Handling 401 error
                        isLoading(false);
                        toaster(response.message());
                    }
                }

                @Override
                public void onFailure(Call<KelasDataList> call, Throwable t) {
                    isLoading(false);
                    toaster(t.getLocalizedMessage());
                }
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
        binding.btnTambahKelas.setOnClickListener(v -> nav.navigate(R.id.action_kelas_petugas_to_tambahKelas_petugas));
        binding.editTextTextPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                getKelas(s.toString());
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
        binding = KelasPetugasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        getKelas(null);
        return binding.getRoot();
    }

    @Override
    public void isLoading(Boolean isLoading) {
        binding.refresher.setRefreshing(isLoading);
    }

    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dialog(String title, String message) {

    }
}