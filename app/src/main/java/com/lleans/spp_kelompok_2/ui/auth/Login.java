package com.lleans.spp_kelompok_2.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.databinding.LoginBinding;
import com.lleans.spp_kelompok_2.domain.model.AuthData;
import com.lleans.spp_kelompok_2.ui.MainActivity;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment {

    private LoginBinding binding;
    private NavController navController;
    private SessionManager sessionManager;

    private String loginType;

    public Login() {
        // Required empty public constructor
    }

    private void navigate(String type) {
        MainActivity.act.finish();
        if (type.equals("siswa")) {
            navController.navigate(R.id.action_login_to_homepage2);
        } else {
            navController.navigate(R.id.action_login_to_homepage3);
        }
    }

    private void authLogin(String username, String password) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<AuthData>> logind;
        ApiInterface apiInterface = ApiClient.getClient(null).create(ApiInterface.class);

        if (loginType.equals("siswa")) {
            logind = apiInterface.postAuthSiswa(username, password);
        } else {
            logind = apiInterface.postAuthPetugas(username, password);
        }

        logind.enqueue(new Callback<BaseResponse<AuthData>>() {
            @Override
            public void onResponse(Call<BaseResponse<AuthData>> call, Response<BaseResponse<AuthData>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.body() != null && response.isSuccessful() && response.body().getDetails().isLogged()) {
                    JWT token = new JWT(response.body().getDetails().getToken());

                    if (loginType.equals("siswa")) {
                        sessionManager.createLogininSessFor(
                                token.getClaim("nama").asString(),
                                token.getClaim("nisn").asString(),
                                loginType,
                                response.body().getDetails().getToken()
                        );
                    } else {
                        sessionManager.createLogininSessFor(
                                token.getClaim("nama_petugas").asString(),
                                token.getClaim("id_petugas").asString(),
                                token.getClaim("level").asString(),
                                response.body().getDetails().getToken());
                    }
                    UtilsUI.toaster(getContext(), "Selamat datang " + sessionManager.getUserDetail().get(SessionManager.USERNAME));
                    navigate(loginType);

                } else if (response.errorBody() != null) {
                    try {
                        BaseResponse<AuthData> message = new Gson().fromJson(response.errorBody().charStream(), BaseResponse.class);
                        UtilsUI.toaster(getContext(), message.getMessage());
                    } catch (Exception e) {
                        try {
                            UtilsUI.dialog(getContext(), "Something went wrong!", response.errorBody().string(), false).show();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<AuthData>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        if (sessionManager.isLoggedIn()) {
            navigate(sessionManager.getUserDetail().get(SessionManager.TYPE));
        }
        binding.login.setOnClickListener(v -> {
            String username, password;
            username = binding.username.getText().toString();
            password = binding.password.getText().toString();
            if (username.equals("") || password.equals("")) {
                UtilsUI.toaster(getContext(), "Data tidak boleh kosong.");
            } else {
                authLogin(username, password);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LoginBinding.inflate(inflater, container, false);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        UtilsUI.isLoading(binding.refresher, false, false);
        UtilsUI.simpleAnimation(binding.login);
        loginType = requireActivity().getIntent().getStringExtra("type");
        if (loginType.equals("siswa")) {
            binding.header.setText("Login Siswa");
            binding.description.setText("Masukkan NISN dan Password anda yang sudah terdaftar.");
            binding.usernameLabel.setText("NISN");
            binding.username.setHint("NISN");
            binding.username.setInputType(InputType.TYPE_CLASS_NUMBER);
            binding.username.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }
        binding.username.requestFocus();
        return binding.getRoot();
    }

}