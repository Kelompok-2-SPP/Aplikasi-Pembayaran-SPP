package com.lleans.spp_kelompok_2.ui.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.MainActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isServerOnline();
    }

    private void isServerOnline() {
        Call<BaseResponse> call;
        ApiInterface apiInterface = ApiClient.getClient(null).create(ApiInterface.class);
        call = apiInterface.pingServer();
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.code() < 500) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                } else {
                    UtilsUI.dialog(SplashScreen.this, "Server is down! :<", "The server you're try to reach is currently down, contact admin for further explanation, or check your internet connection.", false).setPositiveButton("Ok", (dialog, which) -> {
                        SplashScreen.this.finish();
                        System.exit(0);
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                UtilsUI.dialog(SplashScreen.this, "Server is down! :<", "The server you're try to reach is currently down, contact admin for further explanation, or check your internet connection.", false).setPositiveButton("Ok", (dialog, which) -> {
                    SplashScreen.this.finish();
                    System.exit(0);
                }).show();
            }
        });
    }

}