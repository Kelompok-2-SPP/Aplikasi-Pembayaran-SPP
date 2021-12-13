package com.lleans.spp_kelompok_2.api;

import android.content.res.Resources;

import com.lleans.spp_kelompok_2.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    private static final String baseUrl = Resources.getSystem().getString(R.string.API_ENDPOINT);

    public static  Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
