package com.lleans.spp_kelompok_2.ui.login;

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
import com.lleans.spp_kelompok_2.Abstract;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.databinding.LoginBinding;
import com.lleans.spp_kelompok_2.domain.model.auth.AuthData;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment implements Abstract {

    private LoginBinding binding;
    private NavController nav;
    private SessionManager sessionManager;

    private String type;

    public Login() {
        // Required empty public constructor
    }

    private void navigate(String type) {
        if (type.equals("siswa")) {
            nav.navigate(R.id.action_login_to_homepage2);
        } else {
            nav.navigate(R.id.action_login_to_homepage3);
        }
    }

    private void authLogin(String username, String password) {
        // Create api client
        Call<AuthData> logind;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (type.equals("siswa")) {
            logind = apiInterface.postAuthSiswa(username, password);
        } else {
            logind = apiInterface.postAuthPetugas(username, password);
        }

        // Practice using client
        logind.enqueue(new Callback<AuthData>() {
            @Override
            public void onResponse(@NonNull Call<AuthData> call, @NonNull Response<AuthData> response) {
                // On success
                if (response.body() != null && response.isSuccessful() && response.body().getDetails().isLogged()) {
                    // Create session and decode jwt
                    JWT token = new JWT(response.body().getDetails().getToken());

                    // Assigning to session Manager
                    if (type.equals("siswa")) {
                        sessionManager.createLogininSessFor(
                                token.getClaim("nama").asString(),
                                token.getClaim("nisn").asString(),
                                type,
                                response.body().getDetails().getToken()
                        );
                    } else {
                        sessionManager.createLogininSessFor(
                                token.getClaim("username").asString(),
                                token.getClaim("id_petugas").asString(),
                                type,
                                response.body().getDetails().getToken());
                    }
                    isLoading(false);

                    // Toast welcome message
                    toaster("Selamat datang " + sessionManager.getUserDetail().get(SessionManager.USERNAME));
                    navigate(type);
                } else if (response.code() == 401) {
                    // Handling 401 error
                    isLoading(false);
                    toaster("Login gagal, Username atau password salah");
                } else {
                    // Handling 500 error
                    isLoading(false);
                    toaster("Login gagal, " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthData> call, @NonNull Throwable t) {
                isLoading(false);
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);
        sessionManager = new SessionManager(getContext());

        // check if its login move to homepage
        if (sessionManager.isLoggedIn()) {
            navigate(sessionManager.getUserDetail().get(SessionManager.TYPE));
        }

        binding.loginBtn.setOnClickListener(v -> {
            isLoading(true);
            String username = binding.usernameEdit.getText().toString();
            String pasword = binding.passwordEdit.getText().toString();
            authLogin(username, pasword);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LoginBinding.inflate(inflater, container, false);
        isLoading(false);
        type = getActivity().getIntent().getStringExtra("type");

        if (type.equals("siswa")) {
            binding.loginHeader.setText("Login Siswa");
            binding.loginDesc.setText("Masukkan NISN dan Password anda yang sudah terdaftar.");
            binding.usernameLabel.setText("NISN");
            binding.usernameEdit.setHint("NISN");
        }
        return binding.getRoot();
    }

    @Override
    public void isLoading(Boolean isLoading) {
        binding.refresher.setEnabled(isLoading);
        binding.refresher.setRefreshing(isLoading);
    }

    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}