package com.lleans.spp_kelompok_2.ui.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasDataList;
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
        Call<KelasDataList> call;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        call = apiInterface.keywordKelas(null, null);
        call.enqueue(new Callback<KelasDataList>() {
            @Override
            public void onResponse(Call<KelasDataList> call, Response<KelasDataList> response) {
                if (response.code() < 500) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                } else {
                    fail();
                }
            }

            @Override
            public void onFailure(Call<KelasDataList> call, Throwable t) {
                fail();
            }
        });
    }

    private void fail() {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(this);
        as.setTitle("Server is down! :< ")
                .setMessage("The server you're try to reach is currently down, contact admin for further explanation, or check your internet connection.")
                .setPositiveButton("Ok", (dialog, which) -> System.exit(0))
                .show();
    }
}