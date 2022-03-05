package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

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
import com.lleans.spp_kelompok_2.databinding.Petugas6TambahPetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerAdapter;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPetugas extends Fragment implements UIListener {

    private Petugas6TambahPetugasBinding binding;
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
            public void onFailure(Call<PetugasData> call, Throwable t) {
                isLoading(false);
                toaster(t.getLocalizedMessage());
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
            level = "1".equals(binding.level.getSelectedItem().toString()) ? "admin":"petugas";
            if(username.equals("") || password.equals("") || namaPetugas.equals("")) {
                toaster("Data harus diisi!");
            } else {
                tambahPetugas(username, password, namaPetugas, level);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas6TambahPetugasBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());

        binding.level.setAdapter(new SpinnerAdapter(binding.getRoot().getContext(), spinnerList(), false));

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