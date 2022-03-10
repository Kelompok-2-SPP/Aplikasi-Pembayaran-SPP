package com.lleans.spp_kelompok_2.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.databinding.LoginBinding;
import com.lleans.spp_kelompok_2.domain.model.auth.AuthData;
import com.lleans.spp_kelompok_2.ui.MainActivity;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment implements UIListener {

    private LoginBinding binding;
    private NavController navController;
    private SessionManager sessionManager;

    private String loginType;

    public Login() {
        // Required empty public constructor
    }

    // For navigating on Login fragment
    private void navigate(String type) {
        MainActivity.act.finish();
        if (type.equals("siswa")) {
            navController.navigate(R.id.action_login_to_homepage2);
        } else {
            navController.navigate(R.id.action_login_to_homepage3);
        }
    }

    // Function for Auth
    private void authLogin(String username, String password) {
        // Define Call data, make retrofit client
        Call<AuthData> logind;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Check login type
        if (loginType.equals("siswa")) {
            logind = apiInterface.postAuthSiswa(username, password);
        } else {
            logind = apiInterface.postAuthPetugas(username, password);
        }

        // Call back listener
        logind.enqueue(new Callback<AuthData>() {
            @Override
            public void onResponse(@NonNull Call<AuthData> call, @NonNull Response<AuthData> response) {
                isLoading(false);
                // On success response
                if (response.body() != null && response.isSuccessful() && response.body().getDetails().isLogged()) {
                    // Decode token
                    JWT token = new JWT(response.body().getDetails().getToken());

                    // Check login type
                    if (loginType.equals("siswa")) {
                        sessionManager.createLogininSessFor(
                                token.getClaim("nama").asString(),
                                token.getClaim("nisn").asString(),
                                loginType,
                                response.body().getDetails().getToken()
                        );
                    } else {
                        sessionManager.createLogininSessFor(
                                token.getClaim("username").asString(),
                                token.getClaim("id_petugas").asString(),
                                token.getClaim("level").asString(),
                                response.body().getDetails().getToken());
                    }
                    toaster("Selamat datang " + sessionManager.getUserDetail().get(SessionManager.USERNAME));
                    navigate(loginType);
                    // On failure code
                } else if (response.errorBody() != null) {
                    try {
                        AuthData message = new Gson().fromJson(response.errorBody().charStream(), AuthData.class);
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
            public void onFailure(@NonNull Call<AuthData> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Define navController, Session, and Color
        navController = Navigation.findNavController(view);

        // Check if session is logged in
        if (sessionManager.isLoggedIn()) {
            navigate(Objects.requireNonNull(sessionManager.getUserDetail().get(SessionManager.TYPE)));
        }

        // Button listener
        binding.login.setOnClickListener(v -> {
            String username, password;
            username = binding.username.getText().toString();
            password = binding.password.getText().toString();
            if (username.equals("") || password.equals("")) {
                toaster("Data tidak boleh kosong.");
            } else {
                isLoading(true);
                authLogin(username, password);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout, define binding, get Intent from MainActivity
        binding = LoginBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        isLoading(false);
        loginType = requireActivity().getIntent().getStringExtra("type");

        // Check login type
        if (loginType.equals("siswa")) {
            binding.header.setText("Login Siswa");
            binding.description.setText("Masukkan NISN dan Password anda yang sudah terdaftar.");
            binding.usernameLabel.setText("NISN");
            binding.username.setHint("NISN");
            binding.username.setInputType(InputType.TYPE_CLASS_NUMBER);
            binding.username.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }
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