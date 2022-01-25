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
import com.lleans.spp_kelompok_2.domain.model.kelas.DetailsItemKelas;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Siswa extends Fragment implements Abstract {

    private SiswaPetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    private DetailsItemKelas kelas;

    public Siswa() {
        // Required empty public constructor
    }

    private void getSiswa(Integer idKelas){
        isLoading(true);
        Call<SiswaDataList> siswaDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
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
        siswaDataCall.enqueue(new Callback<SiswaDataList>() {
            @Override
            public void onResponse(Call<SiswaDataList> call, Response<SiswaDataList> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
                    binding.jumlahSiswa.setText(response.body().getDetails().size() + " Siswa");
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
            public void onFailure(Call<SiswaDataList> call, Throwable t) {
                isLoading(false);
                toaster(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        bundle.putInt("idKelas", kelas.getIdKelas());
        bundle2.putSerializable("data", kelas);
        binding.btnTambahSiswa.setOnClickListener(v -> nav.navigate(R.id.action_siswa_petugas_to_tambahSiswa, bundle));
        binding.btnEdit.setOnClickListener(v -> nav.navigate(R.id.action_siswa_petugas_to_editKelas, bundle2));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = SiswaPetugasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        Bundle bundle = getArguments();
        kelas = (DetailsItemKelas) bundle.get("data");
        binding.namaKelas.setText(kelas.getNamaKelas());
        getSiswa(kelas.getIdKelas());
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