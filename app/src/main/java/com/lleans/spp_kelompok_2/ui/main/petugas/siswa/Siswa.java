package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.os.Bundle;
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

import com.lleans.spp_kelompok_2.Abstract;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.SiswaPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.main.petugas.kelas.KelasCardAdapter;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Siswa extends Fragment implements Abstract {

    private SiswaPetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    public Siswa() {
        // Required empty public constructor
    }

    private void getSiswa(){
        isLoading(true);
        Call<SiswaData> siswaDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        siswaDataCall = apiInterface.getSiswa(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        siswaDataCall.enqueue(new Callback<SiswaData>() {
            @Override
            public void onResponse(Call<SiswaData> call, Response<SiswaData> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
//                    toaster(response.body().getDetails().toString());
                    SiswaCardAdapter cardAdapter = new SiswaCardAdapter(response.body().getDetails(), nav);
                    binding.rvSiswa.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.rvSiswa.setAdapter(cardAdapter);
                } else {
                    // Handling 401 error
                    isLoading(false);
                    toaster(response.message());
                }
            }

            @Override
            public void onFailure(Call<SiswaData> call, Throwable t) {
                isLoading(false);
                toaster(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
        binding.btnTambahSiswa.setOnClickListener(v -> nav.navigate(R.id.action_siswa_petugas_to_tambahSiswa));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = SiswaPetugasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        getSiswa();
        return binding.getRoot();
    }

    @Override
    public void isLoading(Boolean isLoading) {

    }

    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}