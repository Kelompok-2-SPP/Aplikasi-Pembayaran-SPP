package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas2PetugasBinding;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Petugas extends Fragment {

    private Petugas2PetugasBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    private PetugasCardAdapter cardAdapter;
    private final TextWatcher searchAction = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (cardAdapter != null) {
                cardAdapter.getFilter().filter(s);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public Petugas() {
        // Required empty public constructor
    }

    public void notFoundHandling(boolean check) {
        if (check) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.notFound.getRoot().setVisibility(View.VISIBLE);
            UtilsUI.simpleAnimation(binding.notFound.getRoot());
        } else {
            binding.notFound.getRoot().setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(List<PetugasData> data) {
        cardAdapter = new PetugasCardAdapter(data, controller, this);
        notFoundHandling(cardAdapter.getItemCount() == 0);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(cardAdapter);
    }

    private void getPetugas() {
        UtilsUI.isLoading(binding.refresher, true, true);
        Call<BaseResponse<List<PetugasData>>> petugasDataCall;

        petugasDataCall = apiInterface.getPetugas(
                null,
                null,
                null,
                null,
                null,
                null);
        petugasDataCall.enqueue(new Callback<BaseResponse<List<PetugasData>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PetugasData>>> call, Response<BaseResponse<List<PetugasData>>> response) {
                UtilsUI.isLoading(binding.refresher, true, false);
                if (response.body() != null && response.isSuccessful()) {
                    setAdapter(response.body().getDetails());
                } else {
                    if (response.code() == 404) {
                        notFoundHandling(true);
                    } else {
                        try {
                            BaseResponse message = new Gson().fromJson(response.errorBody().charStream(), BaseResponse.class);
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
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<PetugasData>>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, true, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void searchFocus(boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(binding.searchBar.getWindowToken(), 0);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        binding.refresher.setOnRefreshListener(this::getPetugas);
        binding.searchBar.addTextChangedListener(searchAction);
        binding.searchBar.setOnFocusChangeListener((v, hasFocus) -> searchFocus(hasFocus));
        binding.btnTambah.setOnClickListener(v -> controller.navigate(R.id.action_petugas_petugas_to_tambahpetugas_petugas));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas2PetugasBinding.inflate(inflater, container, false);

        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        getPetugas();
        UtilsUI.simpleAnimation(binding.btnTambah);
        return binding.getRoot();
    }
}