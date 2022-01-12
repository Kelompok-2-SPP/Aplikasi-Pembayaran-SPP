package com.lleans.spp_kelompok_2;

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

import com.auth0.android.jwt.JWT;
import com.lleans.spp_kelompok_2.api.ApiClient;
import com.lleans.spp_kelompok_2.api.ApiInterface;
import com.lleans.spp_kelompok_2.databinding.LoginBinding;
import com.lleans.spp_kelompok_2.model.auth.AuthData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment {

    private LoginBinding binding;
    private NavController nav;
    private ApiInterface apiInterface;

    public Login() {
        // Required empty public constructor
    }

    private static boolean isNumeric(String string) {
        int intValue;

        if(string == null || string.equals("")) {
            return false;
        }

        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void login(String username, String password) {
        String type;
        Call<AuthData> logind;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if(isNumeric(username)){
            type = "siswa";
            logind = apiInterface.postAuthSiswa(username, password);
        }else {
            type = "petugas";
            logind = apiInterface.postAuthPetugas(username, password);
        }
        logind.enqueue(new Callback<AuthData>() {
            @Override
            public void onResponse(Call<AuthData> call, Response<AuthData> response) {
                if(response.body() != null && response.isSuccessful() && response.body().getDetails().isLogged()){
                    JWT token = new JWT(response.body().getDetails().getToken());
                    Bundle bundle = new Bundle();
                    if(type == "petugas") {
                        String username = token.getClaim("username").asString();
                        Toast.makeText(getContext(), username, Toast.LENGTH_SHORT);
                        bundle.putString("username", username);
                        nav.navigate(R.id.action_login_to_homepage3, bundle);
                    } else {
                        String username = token.getClaim("nama").asString();
                        Toast.makeText(getContext(), username, Toast.LENGTH_SHORT);
                        bundle.putString("username", username);
                        nav.navigate(R.id.action_login_to_homepage2, bundle);
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthData> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nav = Navigation.findNavController(view);

        binding.loginBtn.setOnClickListener(v -> {
            String username = binding.usernameEdit.getText().toString();
            String pasword = binding.passwordEdit.getText().toString();
            login(username, pasword);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String type = getActivity().getIntent().getStringExtra("type");
        binding = LoginBinding.inflate(inflater, container, false);

        if (type.equals("siswa")) {
            binding.header.setText("Login Siswa");
            binding.logindesc.setText("Masukkan NISN dan Password anda yang sudah terdaftar.");
            binding.usernameLabel.setText("NISN");
        }
        return binding.getRoot();
    }
}