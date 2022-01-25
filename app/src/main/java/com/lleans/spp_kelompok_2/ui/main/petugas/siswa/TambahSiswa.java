package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.TambahsiswaPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.DetailsItemKelas;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahSiswa extends Fragment implements UIListener {

    private TambahsiswaPetugasBinding binding;
    private SessionManager sessionManager;
    private NavController nav;

    private DetailsItemKelas kelas;

    public TambahSiswa() {
        // Required empty public constructor
    }

    private void tambahSiswa(String nisn, String nis, String password,String namaSiswa, String alamat, String noTelp){
        Call<SiswaData> tambahSiswaCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        tambahSiswaCall = apiInterface.postSiswa(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                nisn,
                nis,
                password,
                namaSiswa,
                kelas.getIdKelas(),
                alamat,
                noTelp);
        tambahSiswaCall.enqueue(new Callback<SiswaData>() {
            @Override
            public void onResponse(Call<SiswaData> call, Response<SiswaData> response) {
                if (response.body() != null && response.isSuccessful()) {
                    isLoading(false);
                    toaster(response.body().getMessage());
                    nav.navigateUp();
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
        binding.simpanSiswa.setOnClickListener(view1 -> {
            String nisn, nis, password, namaSiswa, alamat, noTelp;
            nisn = binding.NISN.getText().toString();
            nis = binding.NIS.getText().toString();
            namaSiswa = binding.namaSiswa.getText().toString();
            password = binding.password.getText().toString();
            alamat = binding.alamat.getText().toString();
            noTelp = binding.telp.getText().toString();
            if(nisn.equals("") || nis.equals("") || namaSiswa.equals("") || alamat.equals("") || noTelp.equals("")) {
                toaster("Data harus diisi!");
            } else {
                tambahSiswa(nisn, nis, password, namaSiswa, alamat, noTelp);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = TambahsiswaPetugasBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        kelas = (DetailsItemKelas) bundle.getSerializable("kelas");
        sessionManager = new SessionManager(getContext());
        return binding.getRoot();
    }

    @Override
    public void isLoading(Boolean isLoading) {

    }

    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dialog(String title, String message) {

    }
}