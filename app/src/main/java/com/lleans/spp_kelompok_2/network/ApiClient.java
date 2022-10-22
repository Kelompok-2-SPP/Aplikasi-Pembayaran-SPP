package com.lleans.spp_kelompok_2.network;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String API_URL = "https://praktek-ukk-spp-lleans.koyeb.app/api/";

    private static Retrofit retrofit;
    private static String savToken;

    public static synchronized Retrofit getClient(String token) {

        if (token != null && !token.equals(savToken)) {
            savToken = token;
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }).build();
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else if (retrofit == null && token == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}